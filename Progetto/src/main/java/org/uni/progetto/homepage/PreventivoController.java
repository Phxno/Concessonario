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
    private Label prevLabel;

    @FXML
    private Label prevNumber;

    @FXML
    private Label dataCreazione;

    @FXML
    private Label price;

    @FXML
    private Label sale;

    @FXML
    private Label shopName;

    @FXML
    private Label model;

    @FXML
    private Pane form_used;

    @FXML
    private Label labelMarcaModello;
    @FXML
    private Label labelAnnoImm;
    @FXML
    private Label labelKm;
    @FXML
    private Label labelProprietari;
    @FXML
    private TextField valutTextFIeld;
    @FXML
    private FlowPane imageContainer;
    @FXML
    private ImageView backButtonLogo;



    private PrevClass prev;
    private String dip;
    private int t_user;
    private String valutazione;
    ContextMenu contextMenu = new ContextMenu();
    ArrayList<String> usato = new ArrayList<>();

    public void initialize(PrevClass prev, String dip, int t_user){
        form_used.setVisible(false);
        this.t_user = t_user;
        this.prev = prev;
        clientName.setText(prev.getUtente().getName() + " " + prev.getUtente().getSurname());
        price.setText(prev.getPrezzo());
        sale.setText(prev.getSconto());
        model.setText(prev.getMacchina());
        shopName.setText(prev.getNegozioConsegna());
        dataCreazione.setText(""+LocalDate.now());
        prevLabel.setText("");
        prevNumber.setText("");
        if (t_user == 1){
          usedButton.setVisible(false);
          confirmButton.setVisible(false);
        } else prevNumber.setText(prev.getId());
        this.dip = dip;
        if (Integer.parseInt(prev.getUsato()) != 0) {
            this.usato = getUsato();
            labelMarcaModello.setText(usato.get(1));
            labelAnnoImm.setText(usato.get(2));
            labelKm.setText(usato.get(3));
            labelProprietari.setText(usato.get(4));
        } else usedButton.setVisible(false);
        backButtonLogo.setOnMouseClicked(event -> {
            try {
                loadDipendente();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        JsonArray configurazione = prev.getConfigurazione();
        Rectangle colorSquare = new Rectangle(20, 20, Color.valueOf(configurazione.get(0).getAsString()));
        // Create a MenuItem to hold the color square

        for (int i = 0; i < configurazione.size(); i++) {
            MenuItem item;
            String temp = "";
            if (i == 0) {
                item = new MenuItem("Color");
                item.setGraphic(colorSquare);
            } else {
                temp = configurazione.get(i).getAsString();
                item = new MenuItem(temp);
            }
            if (!temp.isEmpty() || i == 0) {
                contextMenu.getItems().add(item);
            }
        }

    }

    @FXML
    void back(ActionEvent event) throws IOException {
        loadDipendente();
    }

    @FXML
    void confirm(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFERMA");
        alert.setHeaderText(null);
        alert.setContentText("Inviare preventivo?");
        ButtonType button1 = new ButtonType("Annulla");
        ButtonType button2 = new ButtonType("Conferma");

        alert.getButtonTypes().setAll(button1, button2);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            ButtonType clickedButton = result.get();
            if (clickedButton == button2) {
                savePreventivo();
                loadDipendente();
            }
        }

    }

    private void savePreventivo() {
        Gson gsonToWrite = new GsonBuilder().setPrettyPrinting().create();
        Gson gson = new Gson();
        JsonArray prevsArray = new JsonArray();
        try (Reader reader = new FileReader("preventivi.json")) {
            prevsArray = gson.fromJson(reader, JsonArray.class);
            //Creiamo un oggetto JSON per ogni utente
            for (JsonElement prevElement : prevsArray) {
                JsonObject prevObject = prevElement.getAsJsonObject();
                if (prevObject.get("id").getAsString().equals(prev.getId())) {
                    prevElement.getAsJsonObject().addProperty("prezzoUsato", valutazione);
                    prevElement.getAsJsonObject().addProperty("dataCreazione", LocalDate.now().toString());
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        if (!prevsArray.isEmpty()) {
            try (FileWriter writer = new FileWriter("preventivi.json")) {
                    //System.out.println(prevsArray);
                    gson.toJson(prevsArray, writer);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    @FXML
    void config(ActionEvent event) throws IOException {
        contextMenu.show(configButton, Side.RIGHT, 0, 0);
    }

    @FXML
    void used(ActionEvent event) throws IOException {
        form_used.setVisible(true);
    }
    private Image convertFrom64ToImage(String base64String){
        try {
            // Decode the Base64 string into a byte array
            byte[] imageBytes = Base64.getDecoder().decode(base64String);

            // Convert the byte array to an Image
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            return new Image(byteArrayInputStream);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;  // Invalid Base64 string
        }

    }


    private ArrayList<String> getUsato(){
        ArrayList<String> usato = new ArrayList<>();
        String file = "usato.json";
        Gson gson = new Gson();

        try (Reader reader = new FileReader(file)){
            JsonArray usedArray = gson.fromJson(reader, JsonArray.class);

            for (JsonElement orderElement : usedArray) {

                JsonObject usedObject = orderElement.getAsJsonObject();
                if (usedObject.get("id").getAsString().equals(prev.getUsato())){
                    for (Map.Entry<String, JsonElement> entry : usedObject.entrySet()){
                        if (entry.getKey().equals("img")){
                            for (JsonElement element : entry.getValue().getAsJsonArray()){
                                Image image = convertFrom64ToImage(element.getAsString());
                                ImageView imageView = new ImageView(image);
                                imageView.setFitWidth(120); // Larghezza dell'immagine
                                imageView.setFitHeight(120); // Altezza dell'immagine
                                imageView.setPreserveRatio(true); // Mantieni le proporzioni
                                imageContainer.getChildren().add(imageView);
                            }
                        }else usato.add(entry.getValue().getAsString());
                    }
                }

            }

        } catch (IOException e){
            e.printStackTrace();
        }

        return usato;
    }

    @FXML
    void cancelUsato(ActionEvent event) throws IOException {
        form_used.setVisible(false);
    }
    @FXML
    void confirmUsato(ActionEvent event) throws IOException {
        if (valutTextFIeld.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("VALUTAZIONE USATO VUOTA");
            alert.setHeaderText(null);
            alert.setContentText("Attenzione, non è stato valutato l'usato. La cifra verrà messa automaticamente a 0, confermare?");
            // Add custom buttons
            ButtonType button1 = new ButtonType("Cancella");
            ButtonType button2 = new ButtonType("Conferma");

            alert.getButtonTypes().setAll(button1, button2);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                ButtonType clickedButton = result.get();
                if (clickedButton == button2) {
                    valutazione = "0";
                    form_used.setVisible(false);
                }
            }
        } else {
            if (valutTextFIeld.getText().matches("\\d+")) {
                valutazione = valutTextFIeld.getText();
                form_used.setVisible(false);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText(null);
                alert.setContentText("Inserire solo cifre per cortesia");
                alert.showAndWait();
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
