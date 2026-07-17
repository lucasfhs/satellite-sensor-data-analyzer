package com.aerospace.gui3d;

import com.aerospace.gui3d.jpa.Dados;
import com.aerospace.gui3d.telemetry.TelemetrySource;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.Label;

/** Polls a telemetry source without blocking the JavaFX application thread. */
public final class Updater {

    private final TelemetrySource source;
    private final Label[] labels;
    private final Model3D model3d;
    private final LineChartManager lineChartManager;
    private final Consumer<String> statusConsumer;
    private double previousX;
    private double previousY;
    private double previousZ;

    public Updater(TelemetrySource source, Label[] labels, Model3D model3d,
                   LineChartManager lineChartManager, Consumer<String> statusConsumer) {
        this.source = Objects.requireNonNull(source);
        this.labels = labels;
        this.model3d = model3d;
        this.lineChartManager = lineChartManager;
        this.statusConsumer = statusConsumer;
    }

    public void startUpdating() {
        Thread.ofPlatform().daemon().name("telemetry-updater").start(() -> {
            try (source) {
                while (!Thread.currentThread().isInterrupted()) {
                    Dados latest = source.readLatest();
                    if (latest != null) {
                        Platform.runLater(() -> {
                            updateLabels(latest);
                            statusConsumer.accept("Telemetria recebida");
                        });
                    } else {
                        Platform.runLater(() -> statusConsumer.accept("Aguardando telemetria..."));
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
            } catch (Exception error) {
                Platform.runLater(() -> statusConsumer.accept("Sem conexão: " + rootMessage(error)));
            }
        });
    }

    private void updateLabels(Dados data) {
        labels[0].setText(format(data.getAcelerometroX()));
        labels[1].setText(format(data.getAcelerometroY()));
        labels[2].setText(format(data.getAcelerometroZ()));
        labels[3].setText(format(data.getAnguloX()));
        labels[4].setText(format(data.getAnguloY()));
        labels[5].setText(format(data.getAnguloZ()));
        labels[6].setText(format(data.getVelocidadeAngularX()));
        labels[7].setText(format(data.getVelocidadeAngularY()));
        labels[8].setText(format(data.getVelocidadeAngularZ()));
        labels[9].setText(format(data.getAltitude()));
        labels[10].setText(format(data.getBateria()));
        labels[11].setText(format(data.getCorrenteBateria()));
        labels[12].setText(format(data.getTensaoBateria()));
        labels[13].setText(format(product(data.getCorrenteBateria(), data.getTensaoBateria())));
        labels[14].setText(format(data.getCorrentePlacaSolar()));
        labels[15].setText(format(data.getTensaoPlacaSolar()));
        labels[16].setText(format(product(data.getCorrentePlacaSolar(), data.getTensaoPlacaSolar())));
        labels[17].setText(format(data.getGas1()));
        labels[18].setText(format(data.getGas2()));
        labels[19].setText(format(data.getLuz1()));
        labels[20].setText(format(data.getLuz2()));
        labels[21].setText(format(data.getPontoOrvalho()));
        labels[22].setText(format(data.getPressao()));
        labels[23].setText(format(data.getSensorUV()));
        labels[24].setText(format(data.getTemperaturaExterna()));
        labels[25].setText(format(data.getTemperaturaInterna()));
        labels[26].setText(format(data.getUmidade()));

        double angleX = value(data.getAnguloX());
        double angleY = value(data.getAnguloY());
        double angleZ = value(data.getAnguloZ());
        if (previousX != angleX || previousY != angleY || previousZ != angleZ) {
            model3d.rotateModel(angleX, angleY, angleZ);
            previousX = angleX;
            previousY = angleY;
            previousZ = angleZ;
        }

        lineChartManager.setyValueTemperaturaInterna(value(data.getTemperaturaInterna()));
        lineChartManager.setyValueTemperaturaExterna(value(data.getTemperaturaExterna()));
        lineChartManager.setyValueAltitude(value(data.getAltitude()));
        lineChartManager.setyValueUmidade(value(data.getUmidade()));
        lineChartManager.setyValuePotenciaBateria(product(data.getCorrenteBateria(), data.getTensaoBateria()));
        lineChartManager.setyValuePotenciaPainelSolar(product(data.getCorrentePlacaSolar(), data.getTensaoPlacaSolar()));
        lineChartManager.setyValuePressao(value(data.getPressao()));
        lineChartManager.setyValueSensorUV(value(data.getSensorUV()));
    }

    private static String format(Float value) {
        return value == null ? "--" : String.format("%.2f", value);
    }

    private static String format(double value) {
        return String.format("%.2f", value);
    }

    private static double value(Float value) {
        return value == null ? 0 : value;
    }

    private static double product(Float left, Float right) {
        return value(left) * value(right);
    }

    private static String rootMessage(Throwable error) {
        Throwable current = error;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        String message = current.getMessage();
        return message == null || message.isBlank() ? current.getClass().getSimpleName() : message;
    }
}
