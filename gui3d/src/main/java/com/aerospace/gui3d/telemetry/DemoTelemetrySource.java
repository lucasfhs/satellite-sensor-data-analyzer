package com.aerospace.gui3d.telemetry;

import com.aerospace.gui3d.jpa.Dados;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/** Produces plausible, continuously changing telemetry without external services. */
public final class DemoTelemetrySource implements TelemetrySource {

    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final long startedAt = System.nanoTime();

    @Override
    public Dados readLatest() {
        double seconds = (System.nanoTime() - startedAt) / 1_000_000_000.0;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Dados data = new Dados();

        data.setId((int) seconds + 1);
        data.setCubesat(1);
        data.setAcelerometroX(value(0.12 * Math.sin(seconds / 2), random, 0.02));
        data.setAcelerometroY(value(0.09 * Math.cos(seconds / 3), random, 0.02));
        data.setAcelerometroZ(value(9.81, random, 0.03));
        data.setAnguloX((float) ((120 + seconds * 2.5) % 360));
        data.setAnguloY((float) (8 * Math.sin(seconds / 4)));
        data.setAnguloZ((float) ((35 + seconds * 1.4) % 360));
        data.setAltitude(value(62 + 8 * Math.sin(seconds / 8), random, 0.5));
        data.setBateria(value(Math.max(15, 93 - seconds / 180), random, 0.08));
        data.setCorrenteBateria(value(1.35 + 0.18 * Math.sin(seconds / 3), random, 0.02));
        data.setCorrentePlacaSolar(value(2.1 + 0.45 * Math.sin(seconds / 5), random, 0.03));
        data.setGas1(value(18 + 2 * Math.sin(seconds / 7), random, 0.2));
        data.setGas2(value(11 + 1.4 * Math.cos(seconds / 6), random, 0.2));
        data.setLuz1(value(710 + 45 * Math.sin(seconds / 5), random, 4));
        data.setLuz2(value(665 + 38 * Math.cos(seconds / 5), random, 4));
        data.setPontoOrvalho(value(11 + Math.sin(seconds / 9), random, 0.1));
        data.setPressao(value(78 + 3 * Math.sin(seconds / 10), random, 0.15));
        data.setSensorUV(value(5.4 + 0.7 * Math.sin(seconds / 6), random, 0.05));
        data.setTemperaturaExterna(value(-12 + 2 * Math.sin(seconds / 8), random, 0.1));
        data.setTemperaturaInterna(value(24 + 1.5 * Math.sin(seconds / 10), random, 0.08));
        data.setTensaoBateria(value(12.4 - seconds / 36_000, random, 0.01));
        data.setTensaoPlacaSolar(value(18.2 + Math.sin(seconds / 5), random, 0.04));
        data.setUmidade(value(43 + 4 * Math.cos(seconds / 9), random, 0.2));
        data.setVelocidade(value(7.5 + Math.sin(seconds / 4), random, 0.04));
        data.setVelocidadeAngularX(value(2.5 + 0.3 * Math.sin(seconds), random, 0.03));
        data.setVelocidadeAngularY(value(1.4 + 0.2 * Math.cos(seconds), random, 0.03));
        data.setVelocidadeAngularZ(value(0.8 + 0.2 * Math.sin(seconds / 2), random, 0.02));
        data.setDataObtencao(LocalDateTime.now().format(TIMESTAMP));
        return data;
    }

    private static float value(double base, ThreadLocalRandom random, double noise) {
        return (float) (base + random.nextDouble(-noise, noise));
    }
}
