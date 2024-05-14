package org.example.homepage;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RegistrationController {
    @FXML
    private TextField cognome;

    @FXML
    private HBox data;

    @FXML
    private TextField nome;

    @FXML
    private PasswordField password;

    @FXML
    private TextField telefono;

    @FXML
    private TextField user;

    @FXML
    private Button bottone_registrazione;


    @FXML
    void registration_function(ActionEvent event) throws IOException {
        String name = nome.getText();
        String surname = cognome.getText();
        String phone = telefono.getText();
        String date = data.toString().getBytes().toString();
        String username = user.getText();
        String pass = password.getText();

        if (controllo_campi(name, surname, phone, date, username, pass)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Campi vuoti");
            alert.setContentText("Per favore, riempire tutti i campi");
            alert.showAndWait();
            return;
        }

        if(!(controlla_numero(phone))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Numero di telefono non valido");
            alert.setContentText("Per favore, inserire un numero di telefono valido");
            alert.showAndWait();
            return;
        }

        if(!(controlla_password(pass))){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Password non valida");
            alert.setContentText("Per favore, inserire una password valida");
            alert.showAndWait();
            return;
        }


        JsonObject dati = new JsonObject();

        dati.addProperty("name", name);
        dati.addProperty("surname", surname);
        dati.addProperty("phone", phone);
        dati.addProperty("data", date);
        dati.addProperty("username", username);
        dati.addProperty("password", pass);

        System.out.println("User data: " + dati);



        try {
            //proviamo a leggere il file dati_utente.json
            JsonArray dati_presenti;
            try (FileReader reader = new FileReader("dati_utente.json")) {
                JsonElement parsed = JsonParser.parseReader(reader);
                if (parsed.isJsonArray()) {
                    dati_presenti = parsed.getAsJsonArray();
                } else {
                    dati_presenti = new JsonArray();
                }
                //se il file esiste, leggiamo i dati, userData contiene i dati dell'utente sottoforma di oggetto JSON
            } catch (IOException | JsonSyntaxException e) {
                //se il file non esiste, creiamo un nuovo array vuoto
                dati_presenti = new JsonArray();
            }

            // aggiungiamo i dati dell'utente all'array
            dati_presenti.add(dati);
            System.out.println("Updated user data: " + dati_presenti);

            //scriviamo i dati aggiornati nel file dati_utente.json
            try (FileWriter writer = new FileWriter("dati_utente.json")) {
                writer.write(dati_presenti.toString());
            }
        } catch (IOException e) {
            //in caso di errore stampiamo l'errore
            e.printStackTrace();
        }

        nome.clear();
        cognome.clear();
        telefono.clear();
        user.clear();
        password.clear();

        Stage stage = (Stage) bottone_registrazione.getScene().getWindow();
        stage.close();

        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(Homepage.class.getResource("/FXML/Homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel

        // Crea un nuovo Stage per la homepage
        Stage homepageStage = new Stage();
        homepageStage.setTitle("Concessionario");
        homepageStage.setScene(scene);
        homepageStage.show();
    }

    boolean controllo_campi(String name, String surname, String phone, String date, String username, String pass) { //controlla se ci sono campi vuoti
        return (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || date.isEmpty() || username.isEmpty() || pass.isEmpty());
    }

    boolean controlla_numero(String phone) { //controlla se il numero di telefono è composto solo da numeri e se è lungo 10 caratteri
        return phone.matches("[0-9]+") && phone.length() == 10;
    }

    boolean controlla_password(String pass) {
        return pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"); //almeno 8 caratteri, almeno una lettera maiuscola, almeno una lettera minuscola, almeno un numero, almeno un carattere speciale
    }

}

