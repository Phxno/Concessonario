package org.uni.progetto.homepage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Side;

import java.io.*;
import java.time.LocalDate;


import javafx.scene.Scene;
import javafx.stage.Stage;
import com.google.gson.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class ChangeAutoController {
    @FXML
    private Label adasLabel;

    @FXML
    private Button backButton;

    @FXML
    private ImageView backButtonLogo;

    @FXML
    private Button cancelButtonUsato;

    @FXML
    private Button confirmButton;

    @FXML
    private Button confirmButtonUsato;

    @FXML
    private Label fineLabel;

    @FXML
    private Pane form_used;

    @FXML
    private FlowPane imageContainer;

    @FXML
    private Label labelAnnoImm;

    @FXML
    private Label labelKm;

    @FXML
    private Label labelMarcaModello;

    @FXML
    private Label labelProprietari;

    @FXML
    private Label marcaLabel;

    @FXML
    private Label modelloLabel;

    @FXML
    private Label motoreLabel;

    @FXML
    private TextField newAdas;

    @FXML
    private TextField newFine;

    @FXML
    private TextField newMotore;

    @FXML
    private TextField newPrice;

    @FXML
    private TextField newSedili;

    @FXML
    private TextField newTele;

    @FXML
    private Label oldPirceLabel;

    @FXML
    private Label priceAdas;

    @FXML
    private Label priceFine;

    @FXML
    private Label priceMotore;

    @FXML
    private Label priceMotore1;

    @FXML
    private Label priceMotore2;

    @FXML
    private Label priceSedili;

    @FXML
    private Label priceTele;

    @FXML
    private Label priceTele1;

    @FXML
    private Label sediliLabel;

    @FXML
    private Label teleLabel;

    @FXML
    private TextArea textAreaDesc;

    @FXML
    private TextField valutTextFIeld;

    private int t_user;
    private String dip;
    private String infoCar;
    public void initialize(String dip, int t_user, String infoCar){
        this.t_user = t_user;
        this.dip = dip;
        this.infoCar = infoCar;
        getFromModelli();
        getFromConf();

        backButtonLogo.setOnMouseClicked(event -> {
            try {
                loadDipendente();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }
    @FXML
    void back(ActionEvent event) {

    }

    @FXML
    void cancelUsato(ActionEvent event) {

    }

    @FXML
    void confirm(ActionEvent event) {

    }

    @FXML
    void confirmUsato(ActionEvent event) {

    }

    private void getFromModelli(){
        Gson gson = new Gson();
        JsonArray infoArray = new JsonArray();
        try (Reader reader = new FileReader("modelli_auto.json")) {
            infoArray = gson.fromJson(reader, JsonArray.class);
            //Creiamo un oggetto JSON per ogni utente
            for (JsonElement infoElement : infoArray) {
                JsonObject autoObject = infoElement.getAsJsonObject();
                if ((autoObject.get("Marca").getAsString() + " - "+ autoObject.get("modello").getAsString()).equals(infoCar)) {
                    oldPirceLabel.setText(autoObject.get("prezzo").getAsString());
                    textAreaDesc.setText(autoObject.get("descrizione").getAsString());
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private void getFromConf(){
        Gson gson = new Gson();
        JsonArray infoArray = new JsonArray();
        try (Reader reader = new FileReader("configuratore.json")) {
            infoArray = gson.fromJson(reader, JsonArray.class);
            //Creiamo un oggetto JSON per ogni utente
            for (JsonElement infoElement : infoArray) {
                JsonObject autoObject = infoElement.getAsJsonObject();
                if ((autoObject.get("Marca").getAsString() + " - "+ autoObject.get("modello").getAsString()).equals(infoCar)) {
                    JsonArray optPrice = autoObject.getAsJsonArray();
                    priceMotore.setText(optPrice.get(0).getAsString());
                    priceTele.setText(optPrice.get(1).getAsString());
                    priceFine.setText(optPrice.get(2).getAsString());
                    priceAdas.setText(optPrice.get(3).getAsString());
                    priceSedili.setText(optPrice.get(4).getAsString());
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
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
