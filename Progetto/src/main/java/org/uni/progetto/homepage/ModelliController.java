package org.uni.progetto.homepage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.effect.BoxBlur;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModelliController {

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

    @FXML
    private Button marche_button;

    @FXML
    private Button home_button;
    @FXML
    private VBox main_pane;

    @FXML
    private Button login_button;


    public void initialize(String marca) {
        slider_menu.setTranslateX(-200);
        slider_login.setTranslateX(200);
        menuback.setVisible(false);
        loginback.setVisible(false);
        menu_slider();
        login_slider();
        UserSession userSession = UserSession.getInstance();
        if (userSession != null) {
            nome_login.setText(userSession.getFirstName());
            cognome_login.setText(userSession.getLastName());
            nome_utente_login.setText(userSession.getUsername());
            vbox_login.setVisible(false);
            vbox_dati_utente.setVisible(true);
        } else {
            vbox_login.setVisible(true);
            vbox_dati_utente.setVisible(false);
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader("modelli_auto.json")) {
            List<AutoBase> autoList = gson.fromJson(reader, new TypeToken<List<AutoBase>>() {}.getType());

            if (!marca.equals("All")) {
                autoList = autoList.stream()
                        .filter(auto -> auto.getMarca().equalsIgnoreCase(marca))
                        .toList();
            }

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setStyle("-fx-border-color: #B3C8CF; -fx-border-width: 2px;"); // Stile di debug

            VBox content = new VBox();
            content.setStyle("-fx-border-color: #B3C8CF; -fx-border-width: 2px;"); // Stile di debug
            for (AutoBase auto : autoList) {
                VBox banner = creaBanner(auto);
                content.getChildren().add(banner);
                content.setPrefHeight(Region.USE_COMPUTED_SIZE);
                content.setMinHeight(Region.USE_PREF_SIZE);
                content.setMaxHeight(Double.MAX_VALUE);
            }
            scrollPane.setContent(content);
            //main_pane.getChildren().clear();
            main_pane.getChildren().add(scrollPane);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private VBox creaBanner(AutoBase auto) {

        VBox banner = new VBox();
        banner.setAlignment(Pos.CENTER);
        banner.getStyleClass().add("banner");

        String imageUrl = Objects.requireNonNull(getClass().getResource(auto.getImmagine())).toExternalForm();
        ImageView immagine = new ImageView(new Image(imageUrl));

        immagine.setFitWidth(1024 * 0.33);
        immagine.setFitHeight(768 * 0.33);

        Text marcaModello = new Text("\nMarca - Modello: " + auto.getMarca() + " " + auto.getModello() + "\n");
        marcaModello.getStyleClass().add("marca-modello");

        Text prezzo = new Text("\bPrezzo: " + auto.getPrezzo() + " €");
        prezzo.getStyleClass().add("prezzo");

        Text descrizione = new Text("\bDescrizione: " + auto.getDescrizione() + "\n");
        descrizione.getStyleClass().add("descrizione");

        Button button = new Button("Configura");
        button.setOnAction(event -> {
            try {
                open_configuratore(auto.getModello());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        button.getStyleClass().add("button");

        marcaModello.setWrappingWidth(1024 * 0.50);
        prezzo.setWrappingWidth(1024 * 0.50);
        descrizione.setWrappingWidth(1024 * 0.50);

        banner.getChildren().addAll(immagine, marcaModello, prezzo, descrizione, button);

        return banner;
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

        if (campi_vuoti(user, pass)) {
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
                    if (campi_corretti(user,userObject.get("username").getAsString(),pass,userObject.get("password").getAsString())) {
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

                        if(userObject.get("type-user").getAsInt() < 2){
                            open_dipendente(nome + " " + cognome,userObject.get("type-user").getAsInt());
                        }
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
    public boolean campi_vuoti(String user, String pass) {
        return (user.isEmpty() || pass.isEmpty());
    }

    public boolean campi_corretti(String user,String userD, String pass,String passD) {
        return (user.equals(userD) && pass.equals(pass));
    }

    @FXML
    void registration_button(ActionEvent event) throws IOException {
        Stage stage = (Stage) bottone_registrati_qui.getScene().getWindow();
        stage.close();

        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Homepage.class.getResource("/FXML/Registration.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Registrazione");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void logout(ActionEvent event) {
        UserSession.getInstance().logout();
        username.clear();
        password.clear();
        vbox_login.setVisible(true);
        vbox_dati_utente.setVisible(false);
    }

    @FXML
    void open_marche(ActionEvent event) throws IOException {

        Stage stage = (Stage) marche_button.getScene().getWindow();
        stage.close();
        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Homepage.class.getResource("/FXML/Marche.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Marche");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void open_home(ActionEvent event) throws IOException {

        Stage stage = (Stage) home_button.getScene().getWindow();
        stage.close();
        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Homepage.class.getResource("/FXML/Homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Concessionario");
        stage.setScene(scene);
        stage.show();
    }



    void open_dipendente(String dip, Integer type_user) throws IOException {
        Stage stage = (Stage) login_button.getScene().getWindow();
        stage.close();
        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Homepage.class.getResource("/FXML/Dipendente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        DipendenteController controller = fxmlLoader.getController();
        controller.initialize(dip,type_user);
        stage.setTitle("Dipendente");
        stage.setScene(scene);
        stage.show();
    }

    void open_configuratore(String modello) throws IOException {
        Stage stage = (Stage) marche_button.getScene().getWindow();
        stage.close();
        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Homepage.class.getResource("/FXML/Configuratore.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        ConfiguratoreController controller = fxmlLoader.getController();
        controller.initMacchina(modello);
        stage.setTitle("Configuratore");
        stage.setScene(scene);
        stage.show();
    }
}



