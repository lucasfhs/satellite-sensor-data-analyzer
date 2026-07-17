package com.aerospace.gui3d.controllers;

import com.aerospace.gui3d.LineChartManager;
import com.aerospace.gui3d.Model3D;
import com.aerospace.gui3d.Updater;
import com.aerospace.gui3d.App;
import com.aerospace.gui3d.AppMode;
import com.aerospace.gui3d.telemetry.DatabaseTelemetrySource;
import com.aerospace.gui3d.telemetry.DemoTelemetrySource;
import com.aerospace.gui3d.telemetry.TelemetrySource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.chart.LineChart;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.MeshView;
/**
 * Controller class for managing 3D data visualization and GUI interactions.
 */
public class Data3DViewerController {

    @FXML
    private Label labelAccelerationX;

    @FXML
    private Label labelAccelerationY;

    @FXML
    private Label labelAccelerationZ;

    @FXML
    private Label labelRotationX;

    @FXML
    private Label labelRotationY;

    @FXML
    private Label labelRotationZ;

    @FXML
    private Label labelSpeedX;

    @FXML
    private Label labelSpeedY;

    @FXML
    private Label labelSpeedZ;

    @FXML
    private Label labelAltitude;

    @FXML
    private Label labelBateria;

    @FXML
    private Label labelCorrenteBateria;

    @FXML
    private Label labelTensaoBateria;

    @FXML
    private Label labelPotenciaBateria;

    @FXML
    private Label labelTensaoPlacaSolar;

    @FXML
    private Label labelCorrentePlacaSolar;

    @FXML
    private Label labelPotenciaPlacaSolar;

    @FXML
    private Label labelGas1;

    @FXML
    private Label labelGas2;

    @FXML
    private Label labelLuz1;

    @FXML
    private Label labelLuz2;

    @FXML
    private Label labelPontoOrvalho;

    @FXML
    private Label labelPressao;

    @FXML
    private Label labelSensorUV;

    @FXML
    private Label labelTemperaturaExterna;

    @FXML
    private Label labelTemperaturaInterna;

    @FXML
    private Label labelUmidade;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private MenuBar menuBarTrocarGrafico;

    @FXML
    private MenuItem menuItemAltitude;

    @FXML
    private MenuItem menuItemPotenciaBateria;

    @FXML
    private MenuItem menuItemPotenciaPainelSolar;

    @FXML
    private MenuItem menuItemPressao;

    @FXML
    private MenuItem menuItemSensorUV;

    @FXML
    private MenuItem menuItemUmidade;

    @FXML
    private MenuItem menuItemTemperatura;

    @FXML
    private AnchorPane anchorPane3d;

    @FXML
    private Label labelMode;

    @FXML
    private Label labelConnectionStatus;

    private LineChartManager chartManager;
    /**
     * Configures actions for the menu items in the GUI.
     */
    private void configureMenuActions() {
        menuItemTemperatura.setOnAction(event -> chartManager.setActiveSeries("Temperatura Interna"));
        menuItemAltitude.setOnAction(event -> chartManager.setActiveSeries("Altitude"));
        menuItemPotenciaBateria.setOnAction(event -> chartManager.setActiveSeries("Potência da Bateria"));
        menuItemPotenciaPainelSolar.setOnAction(event -> chartManager.setActiveSeries("Potência do Painel Solar"));
        menuItemPressao.setOnAction(event -> chartManager.setActiveSeries("Pressão"));
        menuItemSensorUV.setOnAction(event -> chartManager.setActiveSeries("Sensor UV"));
        menuItemUmidade.setOnAction(event -> chartManager.setActiveSeries("Umidade"));
    }
    /**
     * Initializes the controller after FXML elements have been loaded.
     * Sets up the 3D model view and starts data updating with an Updater.
     */
    @FXML
    private void initialize() {
        int defaultRotateX = 120;
        int defaultRotateY = 0;
        int defaultRotateZ = 35;
        chartManager = new LineChartManager(lineChart);
        configureMenuActions();
        menuItemAltitude.fire();
        Model3D model3D = new Model3D();
        MeshView meshView = model3D.getMeshView();
        anchorPane3d.getChildren().add(meshView);
        meshView.setScaleX(2);
        meshView.setScaleY(2);
        meshView.setScaleZ(2);
        meshView.setTranslateX(210);
        meshView.setTranslateY(150);
        meshView.setTranslateZ(0);
        model3D.rotateModel(defaultRotateX, defaultRotateY, defaultRotateZ);
        Label[] labels = {
            labelAccelerationX, labelAccelerationY, labelAccelerationZ,
            labelRotationX, labelRotationY, labelRotationZ,
            labelSpeedX, labelSpeedY, labelSpeedZ,
            labelAltitude, labelBateria, labelCorrenteBateria,
            labelTensaoBateria, labelPotenciaBateria, labelCorrentePlacaSolar,
            labelTensaoPlacaSolar, labelPotenciaPlacaSolar, labelGas1,
            labelGas2, labelLuz1, labelLuz2, labelPontoOrvalho,
            labelPressao, labelSensorUV, labelTemperaturaExterna,
            labelTemperaturaInterna, labelUmidade
        };

        AppMode mode = App.getMode();
        labelMode.setText(mode.getDisplayName());
        labelConnectionStatus.setText(mode == AppMode.DEMO
                ? "Gerando telemetria local..."
                : "Conectando ao PostgreSQL...");
        TelemetrySource source = mode == AppMode.DEMO
                ? new DemoTelemetrySource()
                : new DatabaseTelemetrySource();
        Updater updater = new Updater(source, labels, model3D, chartManager,
                labelConnectionStatus::setText);
        updater.startUpdating();

    }
}
