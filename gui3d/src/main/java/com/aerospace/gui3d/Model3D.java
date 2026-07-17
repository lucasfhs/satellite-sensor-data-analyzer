/**
 * Represents a 3D model viewer with camera controls and model manipulation capabilities.
 * This class utilizes JavaFX for rendering and interaction.
 */
package com.aerospace.gui3d;

import java.io.IOException;
import javafx.animation.RotateTransition;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.geometry.Bounds;
import javafx.util.Duration;

/**
 * A class representing a 3D model viewer with camera controls and model
 * manipulation capabilities.
 */
public class Model3D {

    private Group root;
    private PerspectiveCamera camera;
    private MeshView meshView;

    public MeshView getMeshView() {
        return meshView;
    }

    public void setMeshView(MeshView meshView) {
        this.meshView = meshView;
    }
    private final double cameraSpeed = 5.0;
    private final double minDistance = 30.0;
    private double rotateX = 0;
    private double rotateY = 0;
    private double rotateZ = 0;

    /**
     * Constructs a Model3D object, initializing the root group, camera, scene,
     * and loading the 3D model.
     */
    public Model3D() {
        root = new Group();
        initCamera();
        initScene();
        initMesh();
    }

    /**
     * Initializes the camera with specific settings.
     */
    private void initCamera() {
        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.setTranslateY(0); // Ajuste para posicionar a câmera como desejado
        camera.setTranslateX(0); // Ajuste para posicionar a câmera como desejado
        camera.setTranslateZ(-465); // Afastando a câmera para trás
    }

    /**
     * Initializes the JavaFX scene with the root group and sets up event
     * handling. - Please avoid use this.
     */
    private void initScene() {
        Scene scene = new Scene(root, 600, 600, true);
        scene.setFill(Color.BLACK);
        scene.setCamera(camera);

        scene.setOnKeyPressed(event -> handleKeyPressed(event));
    }

    /**
     * Initializes the 3D mesh by loading it from an OBJ file and configuring
     * its appearance.
     */
    private void initMesh() {
        TriangleMesh mesh = null;
        try {
            mesh = OBJLoader.load(Model3D.class.getResourceAsStream(
                    "/com/aerospace/gui3d/assets/obj/CubesatSTL.obj"));

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        meshView = new MeshView(mesh);
        adjustPositionToCenter(meshView);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.WHITE);
        meshView.setMaterial(material);

        root.getChildren().add(meshView);
    }

    /**
     * Adjusts the position of the mesh view to center it within its bounding
     * box.
     *
     * @param meshView The MeshView object to adjust.
     */
    private void adjustPositionToCenter(MeshView meshView) {
        Bounds bounds = meshView.getBoundsInLocal();
        double centerX = (bounds.getMaxX() + bounds.getMinX()) / 2;
        double centerY = (bounds.getMaxY() + bounds.getMinY()) / 2;
        double centerZ = (bounds.getMaxZ() + bounds.getMinZ()) / 2;

        meshView.setTranslateX(meshView.getTranslateX() - centerX);
        meshView.setTranslateY(meshView.getTranslateY() - centerY);
        meshView.setTranslateZ(meshView.getTranslateZ() - centerZ);
    }

    /**
     * Handles key pressed events for camera movement and model rotation.
     *
     * @param event The KeyEvent object representing the key pressed event.
     */
    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                moveCameraForward();
                break;
            case S:
                moveCameraBackward();
                break;
            case A:
                moveCameraLeft();
                break;
            case D:
                moveCameraRight();
                break;
            case Q:
                moveCameraUp();
                break;
            case E:
                moveCameraDown();
                break;
            case NUMPAD8:
                rotateModel(5.0, 0, 0);
                break;
            case NUMPAD2:
                rotateModel(-5.0, 0, 0);
                break;
            case NUMPAD4:
                rotateModel(0, 5.0, 0);
                break;
            case NUMPAD6:
                rotateModel(0, -5.0, 0);
                break;
            case NUMPAD7:
                rotateModel(0, 0, 5.0);
                break;
            case NUMPAD9:
                rotateModel(0, 0, -5.0);
                break;
            default:
                break;
        }

        System.out.println("Camera Position: X=" + camera.getTranslateX()
                + ", Y=" + camera.getTranslateY()
                + ", Z=" + camera.getTranslateZ());
        System.out.println("Model Rotation: X=" + rotateX
                + ", Y=" + rotateY
                + ", Z=" + rotateZ);
    }

    /**
     * Moves the camera forward along its current viewing direction.
     */
    public void moveCameraForward() {
        camera.setTranslateZ(camera.getTranslateZ() + cameraSpeed);
        limitCameraDistance();
    }

    /**
     * Moves the camera backward along its current viewing direction.
     */
    public void moveCameraBackward() {
        camera.setTranslateZ(camera.getTranslateZ() - cameraSpeed);
        limitCameraDistance();
    }

    /**
     * Moves the camera to the left along the x-axis.
     */
    public void moveCameraLeft() {
        camera.setTranslateX(camera.getTranslateX() - cameraSpeed);
        limitCameraDistance();
    }

    /**
     * Moves the camera to the right along the x-axis.
     */
    public void moveCameraRight() {
        camera.setTranslateX(camera.getTranslateX() + cameraSpeed);
        limitCameraDistance();
    }

    /**
     * Moves the camera up along the y-axis.
     */
    public void moveCameraUp() {
        camera.setTranslateY(camera.getTranslateY() - cameraSpeed);
        limitCameraDistance();
    }

    /**
     * Moves the camera down along the y-axis.
     */
    public void moveCameraDown() {
        camera.setTranslateY(camera.getTranslateY() + cameraSpeed);
        limitCameraDistance();
    }

    /**
     * Limits the camera's distance to keep it from getting too close to the
     * model.
     */
    private void limitCameraDistance() {
        double distanceToModel = Math.sqrt(
                camera.getTranslateX() * camera.getTranslateX()
                + camera.getTranslateY() * camera.getTranslateY()
                + camera.getTranslateZ() * camera.getTranslateZ());

        if (distanceToModel < minDistance) {
            camera.setTranslateX(camera.getTranslateX() * minDistance / distanceToModel);
            camera.setTranslateY(camera.getTranslateY() * minDistance / distanceToModel);
            camera.setTranslateZ(camera.getTranslateZ() * minDistance / distanceToModel);
        }
    }

    /**
     * Rotates the model around its center using the specified angles.
     *
     * @param angleX The angle to rotate around the x-axis.
     * @param angleY The angle to rotate around the y-axis.
     * @param angleZ The angle to rotate around the z-axis.
     */
    public void rotateModel(double angleX, double angleY, double angleZ) {
        if (meshView == null) {
            return;
        }
        rotateX = angleX;
        rotateY = angleY;
        rotateZ = angleZ;

        double totalAngle = Math.sqrt(angleX * angleX + angleY * angleY + angleZ * angleZ); // Calcula o ângulo total
        javafx.geometry.Point3D vector = new javafx.geometry.Point3D(angleX, angleY, angleZ);
        if (vector.magnitude() == 0) {
            return;
        }
        javafx.geometry.Point3D axis = vector.normalize(); // Normaliza o vetor de eixo

        RotateTransition rotation = new RotateTransition(Duration.seconds(1), meshView);
        rotation.setAxis(axis);
        rotation.setByAngle(totalAngle);
        rotation.setCycleCount(1); // Apenas uma rotação completa
        rotation.setAutoReverse(false);
        rotation.play();
    }

    /**
     * Retrieves the root group of the 3D scene.
     *
     * @return The root Group object.
     */
    public Group getRoot() {
        return root;
    }

    /**
     * Retrieves the camera used in the 3D scene.
     *
     * @return The PerspectiveCamera object.
     */
    public PerspectiveCamera getCamera() {
        return camera;
    }
}
