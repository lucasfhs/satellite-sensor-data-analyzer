package com.aerospace.gui3d;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main JavaFX application class.
 * This class initializes and launches the JavaFX application, loading the primary FXML file.
 */
public class App extends Application {

    private static Scene scene;
    private static AppMode mode = AppMode.DEMO;
    /**
     * Starts the JavaFX application.
     *
     * @param stage The primary stage for the application
     * @throws IOException If the primary FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        String initialView = "primary";
        if (getParameters().getRaw().contains("--demo")) {
            mode = AppMode.DEMO;
            initialView = "Data3DViewer";
        }
        scene = new Scene(loadFXML(initialView), 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Satellite Sensor Data Analyzer");
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        var icon = App.class.getResourceAsStream("assets/img/cubeicon.png");
        if (icon != null) {
            stage.getIcons().add(new Image(icon));
        }
        stage.show();
    }

    public static void openViewer(AppMode selectedMode) throws IOException {
        mode = selectedMode;
        setRoot("Data3DViewer");
    }

    public static AppMode getMode() {
        return mode;
    }
    /**
     * Changes the root FXML file of the scene.
     *
     * @param fxml The name of the FXML file to load (without the ".fxml" extension)
     * @throws IOException If the specified FXML file cannot be loaded
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    /**
     * Loads an FXML file and returns its root node.
     *
     * @param fxml The name of the FXML file to load (without the ".fxml" extension)
     * @return The root node of the loaded FXML file
     * @throws IOException If the specified FXML file cannot be loaded
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/" +  fxml + ".fxml"));
        return fxmlLoader.load();
    }
    /**
     * The main method to launch the JavaFX application.
     *
     * @param args Command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        launch();
    }

}
