/**
 * Manages a LineChart with multiple series for aerospace data visualization.
 * This class utilizes JavaFX for chart rendering and data manipulation.
 */
package com.aerospace.gui3d;

import javafx.animation.AnimationTimer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.util.*;

import javafx.scene.chart.CategoryAxis;

/**
 * Manages a LineChart with multiple series for aerospace data visualization.
 * 
 */
public class LineChartManager {

    private LineChart<String, Number> lineChart;
    private Map<String, XYChart.Series<String, Number>> seriesMap;
    private String activeSeriesName;
    private double yValue;
    private double yValueAltitude;
    private double yValueTemperaturaInterna;
    private double yValueTemperaturaExterna;
    private double yValueUmidade;
    private double yValuePotenciaBateria;
    private double yValuePotenciaPainelSolar;
    private double yValuePressao;

    private double yValueSensorUV;

    private long startTimeMs; // Tempo inicial em milissegundos

    /**
     * Constructor for LineChartManager.
     *
     * @param lineChart The LineChart instance to manage.
     */
    public LineChartManager(LineChart<String, Number> lineChart) {
        this.lineChart = lineChart;
        this.startTimeMs = System.currentTimeMillis();
        this.seriesMap = new LinkedHashMap<>();
        initializeChart();
        setupDataUpdate();
        setDefaultColors();
        customizeChart();
    }

    /**
     * Initializes the LineChart with default settings. Configures axes and adds
     * default series.
     */
    private void initializeChart() {
        // Configurar eixos
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Tempo");
        yAxis.setLabel("Valor");

        // Definir valores mínimos e máximos nos eixos
        yAxis.setAutoRanging(false); // Desativar o ajuste automático do eixo Y
        yAxis.setLowerBound(0); // Valor mínimo do eixo Y
        yAxis.setUpperBound(100); // Valor máximo do eixo Y

        // Criar o gráfico de linha
        lineChart.setCreateSymbols(false); // Não mostrar símbolos nos pontos
        lineChart.setLegendVisible(false); // Ocultar legenda

        // Limpar dados existentes
        lineChart.getData().clear();
        seriesMap.clear();

        // Adicionar séries padrão
        addSeries("Temperatura Interna");
        addSeries("Temperatura Externa");
        addSeries("Altitude");
        addSeries("Potência da Bateria");
        addSeries("Potência do Painel Solar");
        addSeries("Pressão");
        addSeries("Sensor UV");
        addSeries("Umidade");

        // Definir série ativa inicial como "Altitude"
        setActiveSeries("Altitude");

        // Definir título do gráfico dinamicamente com base na série ativa
        lineChart.setTitle(getActiveSeriesName());
    }

    /**
     * Sets up an AnimationTimer to update chart data periodically.
     */
    private void setupDataUpdate() {
        // Usar AnimationTimer para atualizar os dados a cada segundo
        new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                long elapsedMillis = System.currentTimeMillis() - startTimeMs;
                if (elapsedMillis - lastUpdate >= 1000) { // 1 segundo em milissegundos
                    addRandomData(elapsedMillis);
                    lastUpdate = elapsedMillis;
                }
            }
        }.start();
    }

    /**
     * Adds random data to the active series.
     *
     * @param elapsedMillis Elapsed time in milliseconds.
     */
    private void addRandomData(long elapsedMillis) {
        // Determinar qual série está ativa atualmente
        String activeSeriesName = getActiveSeriesName();

        // Verificar qual yValue usar com base na série ativa
        switch (activeSeriesName) {
            case "Temperatura Interna":
                yValue = yValueTemperaturaInterna;
                break;
            case "Temperatura Externa":
                yValue = yValueTemperaturaExterna;
                break;
            case "Umidade":
                yValue = yValueUmidade;
                break;
            case "Potência da Bateria":
                yValue = yValuePotenciaBateria;
                break;
            case "Potência do Painel Solar":
                yValue = yValuePotenciaPainelSolar;
                break;
            case "Pressão":
                yValue = yValuePressao;
                break;
            case "Sensor UV":
                yValue = yValueSensorUV;
                break;
            case "Altitude":
            default:
                yValue = yValueAltitude;
                break;
        }

        // Adicionar dados aleatórios para a série ativa
        XYChart.Series<String, Number> activeSeries = seriesMap.get(activeSeriesName);
        if (activeSeries != null) {
            activeSeries.getData().add(new XYChart.Data<>(String.valueOf(elapsedMillis / 1000), yValue));

            // Limitar a quantidade de pontos no gráfico para manter a performance
            if (activeSeries.getData().size() > 20) {
                activeSeries.getData().remove(0);
            }
        }
    }

    /**
     * Adds a new series to the LineChart.
     *
     * @param seriesName The name of the series to add.
     */
    public void addSeries(String seriesName) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);
        seriesMap.put(seriesName, series);
        lineChart.getData().add(series);
    }

    /**
     * Sets the active series to display and hides others.
     *
     * @param seriesName The name of the series to set as active.
     */
    public void setActiveSeries(String seriesName) {
        // Limpar os dados da série ativa atual
        XYChart.Series<String, Number> currentActiveSeries = seriesMap.get(getActiveSeriesName());
        if (currentActiveSeries != null) {
            currentActiveSeries.getData().clear();
        }

        // Ocultar todas as séries e mostrar apenas a série selecionada
        for (XYChart.Series<String, Number> series : seriesMap.values()) {
            boolean isActive = series.getName().equals(seriesName);
            if (series.getNode() != null) {
                series.getNode().setVisible(isActive);
            }

            // Atualizar título do gráfico com o nome da série ativa
            if (isActive) {
                lineChart.setTitle(seriesName);
            }
        }
        activeSeriesName = seriesName;

        // Definir valores mínimos e máximos nos eixos
        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);

        // Limpar os dados da série selecionada para garantir que não haja dados antigos visíveis
        XYChart.Series<String, Number> selectedSeries = seriesMap.get(seriesName);
        if (selectedSeries != null) {
            selectedSeries.getData().clear();
        }

        // Forçar o gráfico a atualizar seu layout para refletir as mudanças
        lineChart.layout();
    }

    /**
     * Sets the background color of the LineChart.
     *
     * @param red Red component of the color (0-255).
     * @param green Green component of the color (0-255).
     * @param blue Blue component of the color (0-255).
     */
    public void setChartBackground(int red, int green, int blue) {
        // Definir cor de fundo do gráfico
        String style = String.format("-fx-background-color: rgb(%d, %d, %d);", red, green, blue);
        lineChart.setStyle(style);
    }

    /**
     * Sets the line color of all series in the LineChart.
     *
     * @param red Red component of the color (0-255).
     * @param green Green component of the color (0-255).
     * @param blue Blue component of the color (0-255).
     */
    public void setLineColor(int red, int green, int blue) {
        // Definir cor da linha do gráfico
        String style = String.format("-fx-stroke: rgb(%d, %d, %d);", red, green, blue);
        for (XYChart.Series<String, Number> series : seriesMap.values()) {
            if (series.getNode() != null) {
                series.getNode().setStyle(style);
            }
        }
    }

    /**
     * Sets the text color of all data points in the LineChart.
     *
     * @param red Red component of the color (0-255).
     * @param green Green component of the color (0-255).
     * @param blue Blue component of the color (0-255).
     */
    public void setTextColor(int red, int green, int blue) {
        // Definir cor do texto do gráfico
        String style = String.format("-fx-text-fill: rgb(%d, %d, %d);", red, green, blue);
        for (XYChart.Series<String, Number> series : seriesMap.values()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null && data.getNode().lookup(".chart-line-symbol") != null) {
                    data.getNode().lookup(".chart-line-symbol").setStyle(style);
                }
            }
        }
    }

    /**
     * Sets the chart style to use white colors for title and axis labels.
     */
    public void setChartStyleToWhite() {
        // Definir cor branca para o título e os rótulos dos eixos
        setChartTitleStyle("-fx-text-fill: white;");
        setAxisLabelStyle("-fx-tick-label-fill: white;");
    }

    /**
     * Sets the style of the LineChart title.
     *
     * @param style The CSS style to apply to the title.
     */
    private void setChartTitleStyle(String style) {
        // Definir estilo do título do gráfico
        Label titleLabel = (Label) lineChart.lookup(".chart-title");
        if (titleLabel != null) {
            titleLabel.setStyle(style);
        }
    }

    /**
     * Sets the style of the X and Y axis labels.
     *
     * @param style The CSS style to apply to the axis labels.
     */
    private void setAxisLabelStyle(String style) {
        // Definir estilo dos rótulos dos eixos X e Y
        CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        xAxis.setStyle(style);
        yAxis.setStyle(style);
    }

    /**
     * Sets default colors for background, line, and text. Uses black
     * background, white line color, and white text color.
     */
    private void setDefaultColors() {
        // Cores padrão
        setChartBackground(0, 0, 0); // Preto
        setLineColor(255, 255, 255); // Branco
        setTextColor(255, 255, 255); // Branco
        setChartStyleToWhite();
    }

    /**
     * Customizes the appearance of the LineChart. Sets rounded border and
     * modifies plot area background.
     */
    private void customizeChart() {
        // Definir borda arredondada
        Region chartRegion = (Region) lineChart.lookup(".chart-plot-background");
        if (chartRegion != null) {
            chartRegion.setStyle("-fx-background-radius: 20px; -fx-background-color: rgba(0, 0, 0, 1);");
        }

        // Modificar o background da área de plotagem (dados do gráfico)
        Region plotBackground = (Region) lineChart.lookup(".chart-plot-background");
        if (plotBackground != null) {
            plotBackground.setStyle("-fx-background-color: rgba(0, 0, 0, 1);");
        }

        // Modificar o background da área de tabulação (eixos, legendas)
    }

    /**
     * Retrieves the name of the currently active series.
     *
     * @return The name of the active series.
     */
    public String getActiveSeriesName() {
        return activeSeriesName == null ? "" : activeSeriesName;
    }

    public double getyValue() {
        return yValue;
    }

    public void setyValue(double yValue) {
        this.yValue = yValue;
    }

    public double getyValueAltitude() {
        return yValueAltitude;
    }

    public void setyValueAltitude(double yValueAltitude) {
        this.yValueAltitude = yValueAltitude;
    }

    public double getyValueTemperaturaInterna() {
        return yValueTemperaturaInterna;
    }

    public void setyValueTemperaturaInterna(double yValueTemperaturaInterna) {
        this.yValueTemperaturaInterna = yValueTemperaturaInterna;
    }

    public double getyValueTemperaturaExterna() {
        return yValueTemperaturaExterna;
    }

    public void setyValueTemperaturaExterna(double yValueTemperaturaExterna) {
        this.yValueTemperaturaExterna = yValueTemperaturaExterna;
    }

    public double getyValueUmidade() {
        return yValueUmidade;
    }

    public void setyValueUmidade(double yValueUmidade) {
        this.yValueUmidade = yValueUmidade;
    }

    public double getyValuePotenciaBateria() {
        return yValuePotenciaBateria;
    }

    public void setyValuePotenciaBateria(double yValuePotenciaBateria) {
        this.yValuePotenciaBateria = yValuePotenciaBateria;
    }

    public double getyValuePotenciaPainelSolar() {
        return yValuePotenciaPainelSolar;
    }

    public void setyValuePotenciaPainelSolar(double yValuePotenciaPainelSolar) {
        this.yValuePotenciaPainelSolar = yValuePotenciaPainelSolar;
    }

    public double getyValuePressao() {
        return yValuePressao;
    }

    public void setyValuePressao(double yValuePressao) {
        this.yValuePressao = yValuePressao;
    }

    public double getyValueSensorUV() {
        return yValueSensorUV;
    }

    public void setyValueSensorUV(double yValueSensorUV) {
        this.yValueSensorUV = yValueSensorUV;
    }
}
