package org.uni.progetto.homepage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class Configuratore extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Configuratore.class.getResource("/FXML/Configuratore.fxml"));
        BorderPane root = fxmlLoader.load();
        stage.setTitle("Configuratore");
        Scene scene = new Scene(root, 1024, 768);
        ConfiguratoreController controller = fxmlLoader.getController();
        controller.initMacchina(2);
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.setMaxWidth(1024);
        stage.setMaxHeight(768);
        stage.setScene(scene);
        stage.show();
    }
}

