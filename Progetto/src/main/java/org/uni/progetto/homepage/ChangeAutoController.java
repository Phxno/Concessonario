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
    private Button confirmButton;

    @FXML
    private Label fineLabel;

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
    private Label oldPriceLabel;

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
    void confirm(ActionEvent event) throws IOException{
        Alert alertMod = new Alert(Alert.AlertType.CONFIRMATION);
        alertMod.setTitle("Conferma salvataggio");
        alertMod.setHeaderText(null);
        alertMod.setContentText("Vuoi confermare il salvataggio?");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Valori bassi");
        alert.setHeaderText(null);
        alert.setContentText("I valori inseriti risultano 10 volte più bassi rispetto al valore precedente, procedere?");
        ButtonType button1 = new ButtonType("Annulla");
        ButtonType button2 = new ButtonType("Conferma");

        Alert alertError = new Alert(Alert.AlertType.WARNING);
        alertError.setTitle("Attenzione");
        alertError.setHeaderText(null);
        alertError.setContentText("Errore nella compilazione dei campi, ricontrollare per favore");

        alert.getButtonTypes().setAll(button1, button2);
        alertMod.getButtonTypes().setAll(button1, button2);
        Optional<ButtonType> result = Optional.empty();
        Optional<ButtonType> resultError = Optional.empty();
        if ((!checkEmpty(newPrice) && checkInt(newPrice))) {
            if (Integer.parseInt(oldPriceLabel.getText()) / Integer.parseInt(newPrice.getText()) > 10) result = alert.showAndWait();
        } else if(!checkEmpty(newTele) && checkInt(newTele)){
            if (Integer.parseInt(priceTele.getText()) / Integer.parseInt(newTele.getText()) > 10) result = alert.showAndWait();
        } else if(!checkEmpty(newFine) && checkInt(newFine)) {
            if (Integer.parseInt(priceFine.getText()) / Integer.parseInt(newFine.getText()) > 10) result = alert.showAndWait();
        }else if (!checkEmpty(newMotore) && checkInt(newMotore)){
            if (Integer.parseInt(priceMotore.getText()) / Integer.parseInt(newMotore.getText()) > 10) result = alert.showAndWait();
        } else if (!checkEmpty(newAdas) && checkInt(newAdas)) {
            if (Integer.parseInt(priceAdas.getText()) / Integer.parseInt(newAdas.getText()) > 10) result = alert.showAndWait();
        } else if (!checkEmpty(newSedili) && checkInt(newSedili)){
            if (Integer.parseInt(priceSedili.getText()) / Integer.parseInt(newSedili.getText()) > 10) result = alert.showAndWait();
        } else {
            if (!(checkEmpty(newPrice) && checkEmpty(newTele) && checkEmpty(newFine) && checkEmpty(newMotore) && checkEmpty(newAdas) && checkEmpty(newSedili))){
                resultError = alertError.showAndWait();
            }

        }
        if (textAreaDesc.getText().isEmpty()){
            alertError.showAndWait();
        }else {
            if (resultError.isEmpty()){
                if (result.isEmpty()) result = alertMod.showAndWait();
                if (result.isPresent()) {
                    ButtonType clickedButton = result.get();
                    if (clickedButton == button2) {
                        saveIntoConf();
                        saveIntoModelli();
                        loadDipendente();
                    }
                }
            }
        }
    }
    private boolean checkEmpty(TextField temp){
        boolean result = temp.getText().isEmpty();
        return result;
    }
    private boolean checkInt(TextField temp){
        boolean result = false;
        try {
            Integer number = Integer.parseInt(temp.getText());
            if (number > 0) result = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }
    private void getFromModelli(){
        Gson gson = new Gson();
        JsonArray infoArray = new JsonArray();
        try (Reader reader = new FileReader("modelli_auto.json")) {
            infoArray = gson.fromJson(reader, JsonArray.class);
            //Creiamo un oggetto JSON per ogni utente
            for (JsonElement infoElement : infoArray) {
                JsonObject autoObject = infoElement.getAsJsonObject();
                if ((autoObject.get("marca").getAsString() + " - "+ autoObject.get("modello").getAsString()).equals(infoCar)) {
                    marcaLabel.setText(autoObject.get("marca").getAsString());
                    modelloLabel.setText(autoObject.get("modello").getAsString());
                    oldPriceLabel.setText(autoObject.get("prezzo").getAsString());
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
                if ((autoObject.get("marca").getAsString() + " - "+ autoObject.get("modello").getAsString()).equals(infoCar)) {
                    JsonArray optPrice = autoObject.get("prezzoOptional").getAsJsonArray();
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
    private void saveIntoModelli(){
        Gson gson = new Gson();
        JsonArray autoArray = new JsonArray();
        try (Reader reader = new FileReader("modelli_auto.json")) {
            autoArray = gson.fromJson(reader, JsonArray.class);
            //Creiamo un oggetto JSON per ogni utente
            for (JsonElement autoElement : autoArray) {
                JsonObject autoObject = autoElement.getAsJsonObject();
                if ((autoObject.get("marca").getAsString() + " - "+ autoObject.get("modello").getAsString()).equals(infoCar)) {
                    autoElement.getAsJsonObject().addProperty("prezzo", newPrice.getText().isEmpty() ? autoObject.get("prezzo").getAsString() : newPrice.getText());
                    autoElement.getAsJsonObject().addProperty("descrizione", textAreaDesc.getText());
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        if (!autoArray.isEmpty()) {
            try (FileWriter writer = new FileWriter("modelli_auto.json")) {
                //System.out.println(prevsArray);
                gson.toJson(autoArray, writer);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private void saveIntoConf(){
        Gson gson = new Gson();
        JsonArray autoArray = new JsonArray();
        try (Reader reader = new FileReader("configuratore.json")) {
            autoArray = gson.fromJson(reader, JsonArray.class);
            //Creiamo un oggetto JSON per ogni utente
            for (JsonElement autoElement : autoArray) {
                JsonObject autoObject = autoElement.getAsJsonObject();
                if ((autoObject.get("marca").getAsString() + " - "+ autoObject.get("modello").getAsString()).equals(infoCar)) {
                    autoElement.getAsJsonObject().addProperty("prezzo", newPrice.getText().isEmpty() ? oldPriceLabel.getText() : newPrice.getText());
                    JsonArray temp = new JsonArray();
                    temp.add(newMotore.getText().isEmpty() ? priceMotore.getText() : newMotore.getText());
                    temp.add(newTele.getText().isEmpty() ? priceTele.getText() : newTele.getText());
                    temp.add(newFine.getText().isEmpty() ? priceFine.getText() : newFine.getText());
                    temp.add(newAdas.getText().isEmpty() ? priceAdas.getText() : newAdas.getText());
                    temp.add(newSedili.getText().isEmpty() ? priceSedili.getText() : newSedili.getText());
                    autoElement.getAsJsonObject().add("prezzoOptional", temp);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        if (!autoArray.isEmpty()) {
            try (FileWriter writer = new FileWriter("configuratore.json")) {
                //System.out.println(prevsArray);
                gson.toJson(autoArray, writer);
            } catch (IOException e){
                e.printStackTrace();
            }
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
