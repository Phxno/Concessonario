package org.uni.progetto.homepage;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RegistrationController {
    @FXML
    private TextField cognome;

    @FXML
    private DatePicker data;

    @FXML
    private TextField nome;

    @FXML
    private PasswordField password;

    @FXML
    private TextField telefono;

    @FXML
    private TextField user;


    @FXML
    private PasswordField password1;


    @FXML
    private TextField email;

    @FXML
    private Button bottone_registrazione;

    @FXML
    private Button bottone_logo;



    @FXML
    void registration_function(ActionEvent event) throws IOException {
        String name = nome.getText();
        String surname = cognome.getText();
        String phone = telefono.getText();
        String date = data.toString();
        String username = user.getText();
        String pass = password.getText();
        String email1 = email.getText();
        String pass1 = password1.getText();
        int id = 2;

        if (controllo_campi(name, surname, phone, date, username, pass)) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Campi vuoti", "Per favore, riempire tutti i campi");
            return;
        }

        if(!(controlla_numero(phone))){
            showAlert(Alert.AlertType.ERROR, "Errore", "Numero di telefono non valido", "Per favore, inserire un numero di telefono valido");
            return;
        }

        if(!(controlla_password(pass))){
            showAlert(Alert.AlertType.ERROR, "Errore", "Password non valida", "Per favore, inserire una password lunga 5 caratteri, con almeno una lettera maiuscola, una lettera minuscola, un numero e un carattere speciale");
            return;
        }

        if(!(different_password(pass, pass1))){
            showAlert(Alert.AlertType.ERROR, "Errore", "Le password non corrispondono", "Per favore, inserire due password uguali");
            return;
        }

        if(!(check_email(email1))){
            showAlert(Alert.AlertType.ERROR, "Errore", "Email non valida", "Per favore, inserire un'email valida");
            return;
        }

        if(checkIfUserIsPresent(username)){
            showAlert(Alert.AlertType.ERROR, "Errore", "Username già usato", "Per favore, inserire un altro username");
            return;
        }

        JsonObject dati = new JsonObject();

        dati.addProperty("name", name);
        dati.addProperty("surname", surname);
        dati.addProperty("phone", phone);
        dati.addProperty("data", date);
        dati.addProperty("username", username);
        dati.addProperty("email", email1);
        dati.addProperty("password", pass);
        dati.addProperty("type-user",id);

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
        password1.clear();

        Stage stage = (Stage) bottone_registrazione.getScene().getWindow();
        stage.close();

        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Homepage.class.getResource("/FXML/Homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel

        // Crea un nuovo Stage per la homepage
        Stage homepageStage = new Stage();
        homepageStage.setTitle("Concessionario");
        homepageStage.setScene(scene);
        homepageStage.show();
    }

    @FXML
    void back_home(ActionEvent event) throws IOException {
        Stage stage = (Stage) bottone_logo.getScene().getWindow();
        stage.close();

        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Homepage.class.getResource("/FXML/Homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Concessionario");
        stage.setScene(scene);
        stage.show();
    }


    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }



    boolean controllo_campi(String name, String surname, String phone, String date, String username, String pass) { //controlla se ci sono campi vuoti
        return (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || date.isEmpty() || username.isEmpty() || pass.isEmpty());
    }

    boolean controlla_numero(String phone) { //controlla se il numero di telefono è composto solo da numeri e se è lungo 10 caratteri
        return phone.matches("[0-9]+") && phone.length() == 10;
    }

    boolean controlla_password(String pass) {
        return pass.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!/£.;#&@%()$?*^+=])(?=\\S+$).{5,}$"); //controlla se la password contiene almeno 5 caratteri, una lettera maiuscola, una lettera minuscola, un numero e un carattere speciale
    }

    boolean different_password(String pass, String pass1) {
        return pass.equals(pass1);
    }

    boolean check_email(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean checkIfUserIsPresent(String username) {
        JsonArray dati_presenti;
        try (FileReader reader = new FileReader("dati_utente.json")) {
            JsonElement parsed = JsonParser.parseReader(reader);
            if (parsed.isJsonArray()) {
                dati_presenti = parsed.getAsJsonArray();
            } else {
                dati_presenti = new JsonArray();
            }
        } catch (IOException | JsonSyntaxException e) {
            dati_presenti = new JsonArray();
        }
        for (JsonElement user : dati_presenti) {
            if (user.getAsJsonObject().get("username").getAsString().equals(username)) {
                return true;
            }

        }


        return false;
    }


}

