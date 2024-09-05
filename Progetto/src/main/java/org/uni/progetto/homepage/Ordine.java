package org.uni.progetto.homepage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Ordine extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Ordine.class.getResource("/FXML/Ordine.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Concessionario - Ordine");
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.setMaxWidth(1024);
        stage.setMaxHeight(768);
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}
