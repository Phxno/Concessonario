package org.uni.progetto.homepage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Homepage extends Application { 
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Homepage.class.getResource("/FXML/Homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Concessionario");
        stage.setScene(scene);


        //limitiamo la dimensione della nostra pagina a 1024x768 pixel "chiedere al prof prima"
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.setMaxWidth(1024);
        stage.setMaxHeight(768);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}




