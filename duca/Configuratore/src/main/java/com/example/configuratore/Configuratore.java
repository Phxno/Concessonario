package com.example.configuratore;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class Configuratore extends Application {
    private SmartGroup modelRoot;

    private double anchorY;
    //private double anchorX;
    //private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private Scene createScene() throws IOException {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-7);
        camera.setTranslateX(3);
        camera.setTranslateY(3);
        modelRoot = loadModel(getClass().getResource("Audi_RS_6_Avant.obj"));
        modelRoot.setTranslateX(3);
        modelRoot.setTranslateY(3);

        Transform t = new Rotate(30, new Point3D(0,1,0));
        modelRoot.getTransforms().add(t);

        Group root = new Group(modelRoot);

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setCamera(camera);

        return scene;

    }

    private SmartGroup loadModel(URL url) throws IOException {
        modelRoot = new SmartGroup();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()){
            modelRoot.getChildren().add(view);
        }
        return modelRoot;
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(createScene());

        initMouseControl(modelRoot, stage);

        //Rotare the model with keyboard WASD

        /*stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()){
                case W:
                    modelRoot.rotateByX(10);
                    break;
                case S:
                    modelRoot.rotateByX(-10);
                    break;
                case A:
                    modelRoot.rotateByY(10);
                    break;
                case D:
                    modelRoot.rotateByY(-10);
                    break;
            }


        });
        */

        stage.show();

    }

    private void initMouseControl(SmartGroup modelRoot, Stage stage) {
        //Rotate xRotate;
        Rotate yRotate;
        modelRoot.getTransforms().addAll(
                //xRotate = new Rotate(0, Rotate.X_AXIS)
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        //xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        modelRoot.setOnMousePressed(event -> {
            //anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            //anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        modelRoot.setOnMouseDragged(event -> {
            //angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY - event.getSceneX());
        });
        // Rotate the model automatically
        /*
        RotateTransition rt = new RotateTransition(Duration.seconds(30), modelRoot);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setCycleCount(RotateTransition.INDEFINITE);
        rt.play();


         */



    }

    public static void main(String[] args) {
        launch(args);
    }
}

class SmartGroup extends Group{
    Rotate r;
    Transform t = new Rotate();

    void rotateByX(int angle){
        r = new Rotate(angle, Rotate.X_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }
    void rotateByY(int angle){
        r = new Rotate(angle, Rotate.Y_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }
}