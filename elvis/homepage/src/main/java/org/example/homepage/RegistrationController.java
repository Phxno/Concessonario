package org.example.homepage;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
    void registration_function(ActionEvent event) {
        String name = nome.getText();
        String surname = cognome.getText();
        String phone = telefono.getText();
        String Hbox = data.toString().getBytes().toString();
        String username = user.getText();
        String pass = password.getText();


        JsonObject dati = new JsonObject();

        dati.addProperty("name", name);
        dati.addProperty("surname", surname);
        dati.addProperty("phone", phone);
        dati.addProperty("data", Hbox);
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
                dati_presenti = JsonParser.parseReader(reader).getAsJsonArray();
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
    }

}

