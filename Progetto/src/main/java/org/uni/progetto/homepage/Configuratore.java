package org.uni.progetto.homepage;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;


public class Configuratore extends Application {


    @Override

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Configuratore.class.getResource("/FXML/Configuratore.fxml"));
        BorderPane root = fxmlLoader.load();
        stage.setTitle("Configuratore");
        Scene scene = new Scene(root, 1024, 768);
        ConfiguratoreController controller = fxmlLoader.getController();
        controller.initMacchina();
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.setMaxWidth(1024);
        stage.setMaxHeight(768);
        stage.setScene(scene);
        stage.show();


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
    }
}

