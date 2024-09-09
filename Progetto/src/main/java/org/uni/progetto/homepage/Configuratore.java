package org.uni.progetto.homepage;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class Configuratore extends Application {
    private Group modelRoot;
    private double anchorY;
    //private double anchorX;
    //private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private SubScene createGroup() throws IOException {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-7);
        camera.setTranslateX(3.5);
        camera.setTranslateY(3);
        modelRoot = loadModel(getClass().getResource("/configuratore/Tesla Cybertruck.obj"));
        modelRoot.setTranslateX(3);
        modelRoot.setTranslateY(3);

        Transform t = new Rotate(30, new Point3D(0,1,0));
        modelRoot.getTransforms().add(t);
        Group model = new Group(modelRoot);
        SubScene subScene = new SubScene(model, 1024, 630, true, null);
        subScene.setCamera(camera);
        return subScene;
    }

    private Group loadModel(URL url) throws IOException {
        modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()){
            modelRoot.getChildren().add(view);
        }
        return modelRoot;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Configuratore.class.getResource("/FXML/Configuratore.fxml"));
        BorderPane root = fxmlLoader.load();
        ConfiguratoreController controller = fxmlLoader.getController();
        stage.setTitle("Configuratore");
        controller.add3DModel(createGroup());
        Scene scene = new Scene(root, 1024, 768);
        initMouseControl(modelRoot, stage);
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.setMaxWidth(1024);
        stage.setMaxHeight(768);
        stage.setScene(scene);

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

    private void initMouseControl(Group modelRoot, Stage stage) {
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