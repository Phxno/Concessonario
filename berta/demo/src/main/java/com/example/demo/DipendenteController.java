package com.example.demo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;

public class DipendenteController {

    @FXML
    private Button Cliente;

    @FXML
    private BorderPane MenPrev;

    @FXML
    private Button Ordini;

    @FXML
    private Button Preventivi;

    @FXML
    private Button Nay;

    @FXML
    private Button Okay;

    @FXML
    private Label UData;

    public void initialize(){
      MenPrev.setVisible(false);
      UData.setText("Prova 123");
    }

    private void spawnPrev(){
      MenPrev.setVisible(true);
    }
    
    @FXML
    void Okay(ActionEvent event) throws IOException{
      MenPrev.setVisible(false);
    }

    @FXML
    void Prevs(ActionEvent event){
      spawnPrev();
    }

}
