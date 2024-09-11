package org.uni.progetto.homepage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.*;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import com.google.gson.*;
import java.time.LocalDate;

public class PreventivoController {

    @FXML
    private Button backButton;

    @FXML
    private Label clientName;

    @FXML
    private Button confirmButton;

    @FXML
    private Button configButton;

    @FXML
    private Button usedButton;

    @FXML
    private DatePicker dataShipping;

    @FXML
    private Label prevNumber;

    @FXML
    private Label price;

    @FXML
    private Label sale;

    @FXML
    private Label shopName;

    private PrevClass prev;
    private String dip;
    private int t_user;

    public void initialize(PrevClass prev, String dip, int t_user){
        this.t_user = t_user;
        this.prev = prev;
        clientName.setText(prev.getUtente().getName());
        prevNumber.setText(prev.getId());
        price.setText(prev.getPrezzo());
        sale.setText(prev.getSconto() + "%");
        shopName.setText(prev.getNegozioConsegna());
        LocalDate data = LocalDate.parse(prev.getDataConsegna());
        dataShipping.setValue(data);
        this.dip = dip;
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        loadDipendente();
    }

    @FXML
    void confirm(ActionEvent event) throws IOException {
        loadDipendente();
    }
    @FXML
    void config(ActionEvent event) throws IOException {

    }

    @FXML
    void used(ActionEvent event) throws IOException {

    }
    private void loadDipendente() throws IOException{
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(Dipendente.class.getResource("/FXML/Dipendente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        DipendenteController controller = fxmlLoader.getController();
        controller.initialize(dip, this.t_user);
        stage.setTitle("Concessionario - Dipendente");
        stage.setScene(scene);
        stage.show();
    }
  }
