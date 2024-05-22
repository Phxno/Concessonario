package org.example.homepage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.effect.BoxBlur;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class HomepageController {

    @FXML
    private AnchorPane car_home;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private AnchorPane slider_menu;


    @FXML
    private AnchorPane slider_login;


    @FXML
    private Button login;

    @FXML
    private Button loginback;


    @FXML
    private TextField username;


    @FXML
    private PasswordField password;

    @FXML
    private Button bottone_registrati_qui;

    @FXML
    private VBox vbox_dati_utente;

    @FXML
    private VBox vbox_login;


    @FXML
    private Label nome_login;

    @FXML
    private Label cognome_login;

    @FXML
    private Label nome_utente_login;


    public void initialize() {
        slider_menu.setTranslateX(-200); //impostiamo la posizione iniziale dello slider a -200 cosi da renderla invisibile appena parte l'applicazione
        slider_login.setTranslateX(200);
        menuback.setVisible(false); //rendiamo invisibile il tasto menuback cosi che appena avviamo il codice non sia possibile cliccarlo
        loginback.setVisible(false);
        menu_slider();  //chiamiamo la funzione menu_slider
        login_slider(); //chiamiamo la funzione login_slider

    }


    public void menu_slider() {
        BoxBlur blur = new BoxBlur(6, 6, 3); // Crea un'istanza di BoxBlur
        menu.setOnMouseClicked(event -> slideMenuTo(0, false, true, blur)); //gestione degli eventi: quando clicchiamo il tasto menu spostiamo lo slider a 0 e rendiamo visibile il tasto menuback
        menuback.setOnMouseClicked(event -> slideMenuTo(-200, true, false, null)); //gestione degli eventi: quando clicchiamo il tasto menuback spostiamo lo slider a -200 e rendiamo visibile il tasto menu
    }

    private void slideMenuTo(int x, boolean menuVisible, boolean menubackVisible, BoxBlur blurEffect) {
        TranslateTransition slideMenu = new TranslateTransition(); //creiamo un'istanza di TranslateTransition
        slideMenu.setDuration(Duration.seconds(0.4)); //tempo di transizione
        slideMenu.setNode(slider_menu); //impostiamo il nodo su cui effettuare la transizione

        slideMenu.setToX(x); //impostiamo la posizione finale dello slider
        slideMenu.play(); //avviamo la transizione

        slideMenu.setOnFinished((ActionEvent e) -> {
            car_home.setEffect(blurEffect); // Imposta l'effetto di sfocatura sulla pagina principale
            car_home.setDisable(blurEffect != null); // Disabilita car_home se l'effetto di sfocatura è applicato
            menu.setVisible(menuVisible);
            menuback.setVisible(menubackVisible);
        });
    }


    public void login_slider() {
        BoxBlur blur = new BoxBlur(6, 6, 3); // Crea un'istanza di BoxBlur
        login.setOnMouseClicked(event -> slideLoginTo(0, false, true, blur)); //gestione degli eventi: quando clicchiamo il tasto menu spostiamo lo slider a 0 e rendiamo visibile il tasto menuback
        loginback.setOnMouseClicked(event -> slideLoginTo(200, true, false, null)); //gestione degli eventi: quando clicchiamo il tasto menuback spostiamo lo slider a -200 e rendiamo visibile il tasto menu
    }

    private void slideLoginTo(int x, boolean menuVisible, boolean menubackVisible, BoxBlur blurEffect) {
        TranslateTransition slideLogin = new TranslateTransition(); //creiamo un'istanza di TranslateTransition
        slideLogin.setDuration(Duration.seconds(0.4)); //tempo di transizione
        slideLogin.setNode(slider_login); //impostiamo il nodo su cui effettuare la transizione

        slideLogin.setToX(x); //impostiamo la posizione finale dello slider
        slideLogin.play(); //avviamo la transizione

        slideLogin.setOnFinished((ActionEvent e) -> {
            car_home.setEffect(blurEffect); // Imposta l'effetto di sfocatura sulla pagina principale
            car_home.setDisable(blurEffect != null); // Disabilita car_home se l'effetto di sfocatura è applicato
            login.setVisible(menuVisible);
            loginback.setVisible(menubackVisible);
        });
    }


    @FXML
    void login(ActionEvent event) {
        String user = username.getText();
        String pass = password.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Campi vuoti");
            alert.setContentText("Inserisci username e password");
            alert.showAndWait();
            return;
        }

        Gson gson = new Gson();
        boolean isAuthenticated = false; //variabile booleana per controllare se l'utente è autenticato


        try (Reader reader = new FileReader("dati_utente.json")) {
            // Convertiamo il file JSON in un oggetto Java
            JsonArray usersArray = gson.fromJson(reader, JsonArray.class);

            //Creiamo un oggetto JSON per ogni utente
            for (JsonElement userElement : usersArray) {
                JsonObject userObject = userElement.getAsJsonObject();

                //compariamo per ogni utente il campo username e password con quelli inseriti dall'utente
                if (userObject.has("username") && userObject.has("password")) {
                    if (userObject.get("username").getAsString().equals(user) && userObject.get("password").getAsString().equals(pass)) {
                        String nome = userObject.get("name").getAsString();
                        String cognome = userObject.get("surname").getAsString();
                        String username = userObject.get("username").getAsString();
                        UserSession.getInstance(nome, cognome, username);
                        nome_login.setText(nome);
                        cognome_login.setText(cognome);
                        nome_utente_login.setText(user);
                        vbox_login.setVisible(false);
                        vbox_dati_utente.setVisible(true);
                        isAuthenticated = true;

                    }
                }
            }
            if (!isAuthenticated) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText("Errore di login");
                alert.setContentText("I dati inseriti non sono corretti. Riprova.");
                alert.showAndWait();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void registration_button(ActionEvent event) throws IOException {
        Stage stage = (Stage) bottone_registrati_qui.getScene().getWindow();
        stage.close();

        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(Registration.class.getResource("/FXML/Registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Registrazione");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void logout(ActionEvent event) {
        UserSession.getInstance(null, null, null);
        username.clear();
        password.clear();
        vbox_login.setVisible(true);
        vbox_dati_utente.setVisible(false);
    }

}


