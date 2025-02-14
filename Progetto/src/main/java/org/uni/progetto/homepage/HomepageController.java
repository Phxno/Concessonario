package org.uni.progetto.homepage;

import com.google.gson.*;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.effect.BoxBlur;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;

public class HomepageController {
    private ObservableList<Object> items = FXCollections.observableArrayList();

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
    private Button modelli_button;
    @FXML
    private Button myPrev;
    @FXML
    private AnchorPane prevPopUp;
    @FXML
    private TextField carText;
    @FXML
    private ListView<Object> confList = new ListView<>(items);
    @FXML
    private TextField prezzoUsato;
    @FXML
    private TextField prezzoUsato2;
    @FXML
    private TextField prezzoAuto;
    @FXML
    private TextField sconto;
    @FXML
    private TextField prezzoFinale;
    @FXML
    private Label scadenza;
    @FXML
    private Button back;
    @FXML
    private Button pay_confirm;

    private ContextMenu contextMenu = new ContextMenu();



    public void initialize() {
        slider_menu.setTranslateX(-200); //impostiamo la posizione iniziale dello slider a -200 cosi da renderla invisibile appena parte l'applicazione
        slider_login.setTranslateX(200);
        menuback.setVisible(false); //rendiamo invisibile il tasto menuback cosi che appena avviamo il codice non sia possibile cliccarlo
        loginback.setVisible(false);
        prevPopUp.setVisible(false);
        menu_slider();  //chiamiamo la funzione menu_slider
        login_slider(); //chiamiamo la funzione login_slider
        UserSession userSession = UserSession.getInstance();
        if (userSession != null) {
            // Se esiste, l'utente è loggato
            // Mostra i dati dell'utente
            nome_login.setText(userSession.getFirstName());
            cognome_login.setText(userSession.getLastName());
            nome_utente_login.setText(userSession.getUsername());
            vbox_login.setVisible(false);
            vbox_dati_utente.setVisible(true);
            myPrev.setVisible(true);
            loadContextMenu();
        } else {
            // Se non esiste, l'utente non è loggato
            // Mostra lo slider di login
            vbox_login.setVisible(true);
            vbox_dati_utente.setVisible(false);
            myPrev.setVisible(false);
        }
        myPrev.setOnAction(event ->{
                contextMenu.show(myPrev, Side.BOTTOM, 0, 0);
                });
    }

    private void showPrevPopup() {
        prevPopUp.setVisible(true);
        JsonElement jsonElement = getMyPrev();
        scadenza.setText("scade il " + createdataScadenza(jsonElement.getAsJsonObject().get("dataCreazione").getAsString()));
        carText.setText(jsonElement.getAsJsonObject().get("macchina").getAsString());
        addConf(jsonElement.getAsJsonObject().get("configurazione").getAsJsonArray());
        prezzoAuto.setText(jsonElement.getAsJsonObject().get("prezzo").getAsString());
        prezzoUsato.setText(jsonElement.getAsJsonObject().get("prezzoUsato").getAsString());
        prezzoUsato2.setText(jsonElement.getAsJsonObject().get("prezzoUsato").getAsString());
        sconto.setText(jsonElement.getAsJsonObject().get("sconto").getAsString());
        prezzoFinale.setText(calcPrezzoFinale(prezzoAuto.getText(), prezzoUsato.getText(), sconto.getText()));

        pay_confirm.setOnAction(event ->{

        });

    }

    private JsonElement getMyPrev(){
        String user = UserSession.getInstance().getFirstName() + " " + UserSession.getInstance().getLastName();
        JsonArray jsonArray = Objects.requireNonNull(readPrev("preventivi.json")).getAsJsonArray();
        if (jsonArray.isEmpty()) {
            return null; // Nessun oggetto nel file JSON
        }
        else{
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.get("utente").getAsString().equals(user) &&
                        !jsonObject.get("prezzoUsato").getAsString().isEmpty()) {
                    return element;
                }
            }
        }
        return null;
    }

    private void addConf(JsonArray jsonArray){
        // Estrazione dati dal JsonArray
        String colorCode = jsonArray.get(0).getAsString();
        String engine = jsonArray.get(1).getAsString();
        boolean rearCamera = jsonArray.get(2).getAsBoolean();
        boolean windows = jsonArray.get(3).getAsBoolean();
        boolean adas = jsonArray.get(4).getAsBoolean();
        boolean heatedSeats = jsonArray.get(5).getAsBoolean();

        // Aggiunge il colore come primo elemento
        items.add(Color.web(colorCode));

        // Aggiunge il motore
        items.add("Motore: " + engine);

        // Aggiunge solo gli optional con valore `true`
        if (rearCamera) items.add("Telecamera Posteriore");
        if (windows) items.add("Finestrini Elettrici");
        if (adas) items.add("ADAS");
        if (heatedSeats) items.add("Sedili Riscaldati");

        // Creazione della ListView
        confList.setItems(items);

        // Personalizzazione delle celle per gestire il colore
        confList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else if (item instanceof Color colorItem) {
                    // Crea un rettangolo colorato per mostrare il colore
                    Rectangle colorBox = new Rectangle(100, 20, colorItem);
                    setGraphic(colorBox);
                    setText("Colore: " + colorItem);
                } else {
                    setText(item.toString());
                    setGraphic(null);
                }
            }
        });
    }

    private ArrayList<String> searchMyPrev(){
        ArrayList<String> myConf = new ArrayList<>();
        String user = UserSession.getInstance().getFirstName() + " " + UserSession.getInstance().getLastName();

        JsonArray jsonArray = Objects.requireNonNull(readPrev("preventivi.json")).getAsJsonArray();

        if (jsonArray.isEmpty()) {
            return myConf; // Nessun oggetto nel file JSON
        }
        else{
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.get("utente").getAsString().equals(user) &&
                        !jsonObject.get("prezzoUsato").getAsString().isEmpty() &&
                        !isPreventivoScaduto(jsonObject.get("dataCreazione").getAsString())) {
                    myConf.add(jsonObject.get("id") + " - " + jsonObject.get("dataCreazione"));
                }
                if (isPreventivoScaduto(jsonObject.get("dataCreazione").getAsString())) {
                    removePrev(jsonObject.get("id").getAsString());
                }
            }
        }
        return myConf;
    }

    private static boolean isPreventivoScaduto(String dataCreazione) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate dataPreventivo = LocalDate.parse(dataCreazione, formatter);
            LocalDate dataScadenza = dataPreventivo.plusDays(20);
            LocalDate oggi = LocalDate.now();

            return oggi.isAfter(dataScadenza); // True se il preventivo è scaduto
        } catch (DateTimeParseException e) {
            System.out.println("Formato data non valido! Usa yyyy-MM-dd");
            return false;
        }
    }
    private String createdataScadenza(String dataCreazione){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataPreventivo = LocalDate.parse(dataCreazione, formatter);
        LocalDate dataScadenza = dataPreventivo.plusDays(20);
        return dataScadenza.format(formatter);
    }



    private void loadContextMenu(){
        contextMenu.getItems().clear();
        for (String config : searchMyPrev()) {
            MenuItem item = new MenuItem(config);
            item.setOnAction(event -> {
                slideMenuTo(-200, true, false, null);
                contextMenu.hide();
                showPrevPopup();
            });
            contextMenu.getItems().add(item);
        }
    }

    private JsonElement readPrev(String file) {
        try (FileReader reader = new FileReader(file)) {
            // Parsa il file JSON in un JsonArray
            JsonElement parsed = JsonParser.parseReader(reader);
            if (!parsed.isJsonArray()) {
                throw new IllegalStateException("Il file JSON non contiene un array.");
            }
            return parsed;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String calcPrezzoFinale(String prezzoAuto, String prezzoUsato, String sconto){
        double prezzoAutoD = Double.parseDouble(prezzoAuto.replace("€", ""));
        double prezzoUsatoD = Double.parseDouble(prezzoUsato);
        double scontoI = Integer.parseInt(sconto.replace("%", ""));
        double prezzoFinaleD = (prezzoAutoD * (100 - scontoI)/100 ) - prezzoUsatoD;
        return String.valueOf(prezzoFinaleD);
    }

    private void removePrev(String id){
        JsonArray jsonArray = Objects.requireNonNull(readPrev("preventivi.json")).getAsJsonArray();
        JsonArray newJsonArray = new JsonArray();
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            if (!jsonObject.get("id").getAsString().equals(id)) {
                newJsonArray.add(jsonObject);
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("preventivi.json")) {
            gson.toJson(newJsonArray, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        myPrev.setVisible(true);
                        loadContextMenu();

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

    @FXML
    void registration_button(ActionEvent event) throws IOException {
        Stage stage = (Stage) bottone_registrati_qui.getScene().getWindow();
        stage.close();

        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Registration.class.getResource("/FXML/Registration.fxml"));
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
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Registration.class.getResource("/FXML/Marche.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Marche");
        stage.setScene(scene);
        stage.show();
    }

    void open_dipendente(String dip, Integer type_user) throws IOException {
        Stage stage = (Stage) marche_button.getScene().getWindow();
        stage.close();
        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Dipendente.class.getResource("/FXML/Dipendente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        DipendenteController controller = fxmlLoader.getController();
        controller.initialize(dip,type_user);
        stage.setTitle("Dipendente");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void open_modelli(ActionEvent event) throws IOException {

        Stage stage = (Stage) modelli_button.getScene().getWindow();
        stage.close();
        // Carica la scena modelli
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Registration.class.getResource("/FXML/Modelli.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        ModelliController controller = fxmlLoader.getController();
        controller.initialize("All");
        stage.setTitle("Modelli");
        stage.setScene(scene);
        stage.show();
    }


   /* void open_segreteria(String seg) throws IOException {
        Stage stage = (Stage) marche_button.getScene().getWindow();
        stage.close();
        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Segreteria.class.getResource("/FXML/Segreteria.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        SegreteriaController controller = fxmlLoader.getController();
        controller.initialize(seg);
        stage.setTitle("Segreteria");
        stage.setScene(scene);
        stage.show();
    }*/
}




