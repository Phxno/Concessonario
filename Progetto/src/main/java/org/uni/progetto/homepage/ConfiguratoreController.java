package org.uni.progetto.homepage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.PhongMaterial;
import com.google.gson.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfiguratoreController{
    private Group Gmodel;
    private double anchorY;
    //private double anchorX;
    //private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    //private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private DoubleProperty angleY = new SimpleDoubleProperty(0);
    @FXML
    private AnchorPane auto;
    @FXML
    private Button butt_men;
    @FXML
    private Button butt_men_back;
    @FXML
    private VBox slider_men;
    @FXML
    private ChoiceBox<String> shortcut;
    @FXML
    private ColorPicker color_picker;
    @FXML
    private ChoiceBox<String> choice;
    @FXML
    private CheckBox tele_pos;
    @FXML
    private CheckBox fin_oscu;
    @FXML
    private CheckBox adas;
    @FXML
    private CheckBox sed_ris;
    @FXML
    private Label price;
    @FXML
    private Label User;
    @FXML
    private Button save;
    @FXML
    private Button send;
    @FXML
    private Pane login_popup;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Button registrati;
    @FXML
    private Pane validation_popup;
    @FXML
    private CheckBox si;
    @FXML
    private CheckBox no;
    @FXML
    private ChoiceBox<String> sede;
    @FXML
    private Button back_conf;
    @FXML
    private Button send_conf;
    @FXML
    private Button back;
    @FXML
    private Button confirm;
    @FXML
    private Pane form_used;
    @FXML
    private FlowPane imageContainer;
    @FXML
    private Button load_img;
    @FXML
    private TextField car_info;
    @FXML
    private TextField km_info;
    @FXML
    private TextField date_info;
    @FXML
    private CheckBox II_mano;
    @FXML
    private CheckBox III_mano;
    @FXML
    private Button home;
    @FXML
    private Button marche;
    @FXML
    private Button my_conf;
    @FXML
    private Label car_name;

    private String[] concessionari = {"Verona", "Milano", "Aosta"};
    private String[] type = {"Base", "Full Optional"};
    private String[] Motore_batteria;
    private double Base_price;
    private int[] PezCol;
    private int[] PezFin;
    private Boolean isAuthenticated;
    private double[] posizioneCamera;
    private String car;
    private int sconto;

    ObjModelImporter importer = new ObjModelImporter();

    public void initMacchina(int id)throws IOException {
        sconto = (id == 2) ? 5 : 0;
        login_popup.setVisible(false);
        validation_popup.setVisible(false);
        form_used.setVisible(false);
        checkUserSession();
        searchCar(id);
        car_name.setText(car);
        createCar();

        slider_men.setTranslateX(-200); //impostiamo la posizione iniziale dello slider a -200 cosi da renderla invisibile appena parte l'applicazione
        butt_men_back.setVisible(false); //rendiamo invisibile il tasto menuback cosi che appena avviamo il codice non sia possibile cliccarlo
        menu_slider();
        choice.getItems().addAll(Motore_batteria);
        shortcut.getItems().addAll(type);
        shortcut.setValue(type[0]);
        choice.setValue(Motore_batteria[0]);
        price.setText(Base_price + " € ");
        sede.getItems().addAll(concessionari);

        shortcut.setOnAction(e -> setShortcut());
        choice.setOnAction(e -> setChoice());
        tele_pos.setOnAction(e -> setTelePos());
        fin_oscu.setOnAction(e -> setFin());
        adas.setOnAction(e -> setAdas());
        sed_ris.setOnAction(e -> setSedRis());
        color_picker.setOnAction(e -> setColo());
        save.setOnAction(e -> saveFunc());
        send.setOnAction(e -> sendfunc());
        home.setOnAction(event -> {
            try {
                openHome();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        marche.setOnAction(event -> {
            try {
                openMod();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }

    private void checkUserSession(){
        UserSession userSession = UserSession.getInstance();
        if (userSession != null) {
            // Se esiste, l'utente è loggato
            // Mostra i dati dell'utente
            User.setText(UserSession.getInstance().getUsername());
            isAuthenticated = true;

        } else {
            // Se non esiste, l'utente non è loggato
            User.setText("Guest");
            isAuthenticated = false;
        }
    }

    private void searchCar(int idToSearch){
        try (FileReader reader = new FileReader("configuratore.json")) {
            // Parsa il file JSON in un JsonArray
            JsonElement parsed = JsonParser.parseReader(reader);
            if (!parsed.isJsonArray()) {
                throw new IllegalStateException("Il file JSON non contiene un array.");
            }

            JsonArray jsonArray = parsed.getAsJsonArray();

            // Verifica se l'ID è valido rispetto al numero di oggetti
            if (idToSearch > jsonArray.size()) {
                throw new IllegalArgumentException("L'ID supera il numero di oggetti nel file JSON.");
            }

            // Ottieni l'oggetto JSON corrispondente all'ID
            JsonObject jsonObject = jsonArray.get(idToSearch - 1).getAsJsonObject();
            // Mappa l'oggetto JSON alla classe Auto
            Gson gson = new Gson();
            AutoConf auto = gson.fromJson(jsonObject, AutoConf.class);

            // Salva i valori nelle variabili globali
            car = auto.getMarca() + " " + auto.getModello();
            Motore_batteria = auto.getListaMotori();
            Base_price = auto.getPrezzo();
            PezFin = auto.getFinestrini();
            PezCol = auto.getListaPezziAutoColorati();
            posizioneCamera = auto.getPosizioneCamera();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCar(){
        SubScene subScene;
        try {
            subScene = createGroup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        add3DModel(subScene);
    }

    private void add3DModel(SubScene model) {
        Platform.runLater(() -> {
            auto.getChildren().add(model);
        });
    }

    private void openMod() throws IOException {
        Stage stage = (Stage) marche.getScene().getWindow();
        stage.close();
        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(Registration.class.getResource("/FXML/Marche.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Marche");
        stage.setScene(scene);
        stage.show();
    }

    private void openHome() throws IOException {
        Stage stage = (Stage) home.getScene().getWindow();
        stage.close();
        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(Registration.class.getResource("/FXML/Homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Homepage");
        stage.setScene(scene);
        stage.show();
    }

    private void setShortcut() {
        if (shortcut.getValue() == null) {
            return; // Se il valore è null, esci dal metodo
        }

        if (Objects.equals(shortcut.getValue(), type[0])) {
            // Disabilita le opzioni
            setOptionalState(tele_pos, Optionals.TEL_POS.getOptionals(), false);
            setOptionalState(fin_oscu, Optionals.MIRRORS.getOptionals(), false);
            setFinLight();
            setOptionalState(adas, Optionals.ADAS.getOptionals(), false);
            setOptionalState(sed_ris, Optionals.HEATS_SEATS.getOptionals(), false);
            choice.setValue(Motore_batteria[0]);
        } else if (Objects.equals(shortcut.getValue(), type[1])) {
            // Abilita le opzioni
            setOptionalState(tele_pos, Optionals.TEL_POS.getOptionals(), true);
            setOptionalState(fin_oscu, Optionals.MIRRORS.getOptionals(), true);
            setFinOscu();
            setOptionalState(adas, Optionals.ADAS.getOptionals(), true);
            setOptionalState(sed_ris, Optionals.HEATS_SEATS.getOptionals(), true);
            choice.setValue(Motore_batteria[1]);
        }
    }

    private void setOptionalState(CheckBox checkBox, int optional, boolean isSelected) {
        if (checkBox.isSelected() != isSelected) {
            if (isSelected) {
                addPrice(optional);
            } else {
                removePrice(optional);
            }
            checkBox.setSelected(isSelected);
        }
    }

    private void setChoice() {
        if(choice.getValue().equals(Motore_batteria[0])){
            if(!tele_pos.isSelected() && !fin_oscu.isSelected() && !adas.isSelected() && !sed_ris.isSelected())shortcut.setValue(type[0]);
            else if (tele_pos.isSelected() && fin_oscu.isSelected() && adas.isSelected() && sed_ris.isSelected())shortcut.setValue("Custom");
            removePrice(Optionals.ENGINE.getOptionals());
        }
        else{
            if(tele_pos.isSelected() && fin_oscu.isSelected() && adas.isSelected() && sed_ris.isSelected())shortcut.setValue(type[1]);
            else if (!tele_pos.isSelected() && !fin_oscu.isSelected() && !adas.isSelected() && !sed_ris.isSelected())shortcut.setValue("Custom");
            addPrice(Optionals.ENGINE.getOptionals());
        }
    }

    private void setTelePos(){
        if (tele_pos.isSelected()) {
            addPrice(Optionals.TEL_POS.getOptionals());
            if (fin_oscu.isSelected() && adas.isSelected() && sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[1])) {
                shortcut.setValue(type[1]);
            }
            else {
                shortcut.setValue("Custom");
            }
        }
        else {
            removePrice(Optionals.TEL_POS.getOptionals());
            if(!fin_oscu.isSelected() && !adas.isSelected() && !sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[0])) {
                shortcut.setValue(type[0]);
            }
            else {
                shortcut.setValue("Custom");
            }
        }

    }

    private void setFin(){
        if (fin_oscu.isSelected()) {
            setFinOscu();
            addPrice(Optionals.MIRRORS.getOptionals());
            if (tele_pos.isSelected() && adas.isSelected() && sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[1])) {
                shortcut.setValue(type[1]);
            }
            else {
                shortcut.setValue("Custom");
            }
        }
        else {
            removePrice(Optionals.MIRRORS.getOptionals());
            setFinLight();
            if(!tele_pos.isSelected() && !adas.isSelected() && !sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[0])) {
                shortcut.setValue(type[0]);
            }
            else {
                shortcut.setValue("Custom");
            }
        }
    }

    private void setAdas(){
        if (adas.isSelected()) {
            addPrice(Optionals.ADAS.getOptionals());
            if (tele_pos.isSelected() && fin_oscu.isSelected() && sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[1])) {
                shortcut.setValue(type[1]);
            }
            else {
                shortcut.setValue("Custom");
            }
        }
        else {
            removePrice(Optionals.ADAS.getOptionals());
            if(!tele_pos.isSelected() && !fin_oscu.isSelected() && !sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[0])) {
                shortcut.setValue(type[0]);
            }
            else {
                shortcut.setValue("Custom");
            }
        }
    }

    private void setSedRis(){
        if (sed_ris.isSelected()) {
            addPrice(Optionals.HEATS_SEATS.getOptionals());
            if (tele_pos.isSelected() && fin_oscu.isSelected() && adas.isSelected() && choice.getValue().equals(Motore_batteria[1])) {
                shortcut.setValue(type[1]);
            }
            else {
                shortcut.setValue("Custom");
            }
        }
        else {
            removePrice(Optionals.HEATS_SEATS.getOptionals());
            if(!tele_pos.isSelected() && !fin_oscu.isSelected() && !adas.isSelected() && choice.getValue().equals(Motore_batteria[0])) {
                shortcut.setValue(type[0]);
            }
            else {
                shortcut.setValue("Custom");
            }
        }
    }

    private void setColo(){
        Color newColor = color_picker.getValue();
        reload3DModel(newColor);
    }

    private void saveFunc(){
        if (!isAuthenticated){
            showLoginPopup();
        }
    }

    private void sendfunc(){
        if (!isAuthenticated){
            showLoginPopup();
        }
        else{
            showValidationPopup();

        }

    }

    private void showLoginPopup(){
        login_popup.setVisible(true);
        login.setOnAction(event -> {
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
                            User.setText(username); //aggiorniamo il campo User con il nome dell'utente
                            isAuthenticated = true;
                            login_popup.setVisible(false);
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
            }catch(IOException e){
                e.printStackTrace();
            }

        });
        registrati.setOnAction(event -> {
            try {
                Stage stage = (Stage) login.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Registration.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
                stage.setTitle("Registrazione");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void showValidationPopup(){
        AtomicInteger id_usato = new AtomicInteger();
        validation_popup.setVisible(true);

        si.setOnAction(event ->{
            if (si.isSelected()){
                no.setSelected(false);
                id_usato.set(createFormUsed());
            }
        });
        no.setOnAction(event -> {
            if(no.isSelected()){
                si.setSelected(false);
                try {
                    deleteUsed(id_usato);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        send_conf.setOnAction(event -> {
            ArrayList<Object> conf = saveConf();
            try {
                saveInJson("preventivi.json", creaJsonObject(
                        "utente", UserSession.getInstance().getFirstName() + " " + UserSession.getInstance().getLastName(),
                        "macchina", car,
                        "configurazione", conf,
                        "usato", id_usato,
                        "dataCreazione", getDataDiOggi(),
                        "prezzo", price.getText(),
                        "sconto", sconto + "%",
                        "negozioConsegna", sede.getValue(),
                        "dataConsegna", calculateDataConsegna(conf),
                        "id", createId("preventivi.json")
                ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operazione Riuscita");
            alert.setHeaderText("L'operazione è stata completata con successo!");
            alert.setContentText("Puoi procedere con le prossime azioni.");
            alert.showAndWait();
            try {
                openHome();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        back_conf.setOnAction(event -> {
            validation_popup.setVisible(false);
            try {
                deleteUsed(id_usato);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private ArrayList<Object> saveConf(){
        ArrayList<Object> conf = new ArrayList<>();
        conf.add(color_picker.getValue());
        conf.add(choice.getValue());
        conf.add(tele_pos.isSelected());
        conf.add(fin_oscu.isSelected());
        conf.add(adas.isSelected());
        conf.add(sed_ris.isSelected());

        return conf;
    }

    private String calculateDataConsegna(ArrayList<Object> conf){
        // Ottieni la data di oggi
        LocalDate data = LocalDate.now();
        data.plusMonths(1);

        // Itera attraverso l'ArrayList
        for (Object condizione : conf) {
            if ((condizione instanceof Boolean && (Boolean) condizione) ||
                    (condizione instanceof String && condizione.equals(Motore_batteria[1])) ||
                    (condizione instanceof Color && !condizione.equals(Color.WHITE) && !condizione.equals(Color.BLACK))){
                // Se la condizione è true, aggiungi 10 giorni
                data = data.plusDays(10);
            }
        }

        // Formatta la data nel formato yyyy-MM-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return data.format(formatter);
    }

    private int createFormUsed() {
        int id;
        try {
            id = createId("usato.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        form_used.setVisible(true);
        form_used.toFront();

        load_img.setOnAction(event ->{
            Stage stage = (Stage) load_img.getScene().getWindow();

            // Crea un FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleziona un'immagine");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg", "*.gif"));

            // Mostra la finestra di dialogo per selezionare un file

            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

            // Se l'utente ha selezionato dei file, carica le immagini
            if (selectedFiles != null && !selectedFiles.isEmpty()) {
                for (File file : selectedFiles) {
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(130); // Imposta la larghezza dell'immagine
                    imageView.setFitHeight(130); // Imposta l'altezza dell'immagine
                    imageView.setPreserveRatio(true); // Mantieni le proporzioni
                    imageContainer.getChildren().add(imageView); // Aggiungi l'ImageView al contenitore
                }
            }
        });

        II_mano.setOnAction(event -> {
            if (II_mano.isSelected() &&  III_mano.isSelected()){
                III_mano.setSelected(false);
            }
        });

        III_mano.setOnAction(event -> {
            if (II_mano.isSelected() &&  III_mano.isSelected()){
                II_mano.setSelected(false);
            }
        });

        confirm.setOnAction(event -> {
            String car_infoText = car_info.getText();
            String date_infoText = date_info.getText();
            String km_infoText = km_info.getText();

            // Controlla se tutti i campi sono compilati e se ci sono immagini
            if (controllaCampi(car_infoText, date_infoText, km_infoText) ||
                    (!II_mano.isSelected() && !III_mano.isSelected()) ||
                    imageContainer.getChildren().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText("Non compilati tutti i campi");
                alert.setContentText("Devi compilare tutti i campi e inserire le immagini prima di confermare");
                alert.showAndWait();
                return; // Esci dal metodo se i campi non sono validi
            }
            JsonArray immaginiBase64 = new JsonArray();
            for (Node node : imageContainer.getChildren()) {
                if (node instanceof ImageView) {
                    ImageView imageView = (ImageView) node;
                    Image image = imageView.getImage();
                    String base64 = convertImageToBase64(image);
                    if (base64 != null) {
                        immaginiBase64.add(base64);
                    }
                }
            }
            saveInJson("usato.json", creaJsonObject(
                    "id", id,
                    "car", car_infoText,
                    "date", date_infoText,
                    "km", km_infoText,
                    "usata", II_mano.isSelected() ? "2°mano" : "3 mano o +",
                    "img", immaginiBase64
            ));
            form_used.setVisible(false);
            clearUsedForm();
        });

        back.setOnAction(event -> {
            form_used.setVisible(false);
            clearUsedForm();
        });

        return id;
    }

    private String getDataDiOggi() {
        // Ottieni la data di oggi
        LocalDate oggi = LocalDate.now();
        // Definisci il formato desiderato
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Formatta la data nel formato yyyy-MM-dd
        return oggi.format(formatter);
    }

    private static JsonObject creaJsonObject(Object... args) {
        // Verifica che il numero di argomenti sia pari
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Il numero di argomenti deve essere pari (chiavi e valori).");
        }

        // Creazione del JsonObject
        JsonObject jsonObject = new JsonObject();

        // Aggiunta delle coppie chiave-valore al JsonObject
        for (int i = 0; i < args.length; i += 2) {
            if (!(args[i] instanceof String)) {
                throw new IllegalArgumentException("Le chiavi devono essere di tipo String.");
            }
            String chiave = (String) args[i];
            Object valore = args[i + 1];

            // Gestione dei tipi di valore
            if (valore instanceof String) {
                jsonObject.addProperty(chiave, (String) valore);
            } else if (valore instanceof Number) {
                jsonObject.addProperty(chiave, (Number) valore);
            } else if (valore instanceof Boolean) {
                jsonObject.addProperty(chiave, (Boolean) valore);
            } else {
                // Se il valore non è di un tipo supportato, lo convertiamo in stringa
                jsonObject.addProperty(chiave, valore.toString());
            }
        }

        return jsonObject;
    }

    private void saveInJson(String filePath, JsonObject dati) {
        try {
            JsonArray dati_presenti;
            try (FileReader reader = new FileReader(filePath)) {
                JsonElement parsed = JsonParser.parseReader(reader);
                dati_presenti = parsed.isJsonArray() ? parsed.getAsJsonArray() : new JsonArray();
            } catch (IOException | JsonSyntaxException e) {
                dati_presenti = new JsonArray();
            }

            dati_presenti.add(dati); // Aggiungi i nuovi dati all'array esistente

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(dati_presenti.toString()); // Scrivi i dati aggiornati nel file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertImageToBase64(Image image) {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream); // Puoi cambiare "png" con il formato desiderato
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void deleteUsed(AtomicInteger id) throws IOException {
        int idToRemove = id.get();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonArray;

        // Leggi il file JSON esistente
        File file = new File("usato.json");
        if (!file.exists() || file.length() == 0) {
            return; // Nessun file, nulla da rimuovere
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
        } catch (JsonSyntaxException e) {
            throw new IOException("Formato JSON non valido", e);
        }

        // Filtra gli oggetti, rimuovendo quello con l'ID specifico
        JsonArray newArray = new JsonArray();
        boolean removed = false;
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject obj = jsonArray.get(i).getAsJsonObject();
            if (obj.has("id") && obj.get("id").getAsInt() == idToRemove) {
                removed = true; // Oggetto rimosso
            } else {
                newArray.add(obj); // Aggiungi solo gli oggetti che non hanno l'ID da rimuovere
            }
        }
        // Scrivi l'array aggiornato nel file JSON
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(newArray, writer);
        }
    }

    private void clearUsedForm(){
        car_info.clear();
        km_info.clear();
        date_info.clear();
        II_mano.setSelected(false);
        III_mano.setSelected(false);
        imageContainer.getChildren().clear();
    }

    private synchronized int createId(String filepath) throws IOException {
        Set<Integer> existingIds = new HashSet<>();
        File file = new File(filepath);

        // Se il file esiste e non è vuoto, leggere gli ID esistenti
        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder jsonText = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonText.append(line);
                }
                if (!jsonText.toString().trim().isEmpty()) {
                    JsonArray jsonArray = JsonParser.parseString(jsonText.toString()).getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject obj = jsonArray.get(i).getAsJsonObject();
                        if (obj.has("id")) {
                            existingIds.add(obj.get("id").getAsInt());
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Errore nella lettura del file JSON: " + e.getMessage());
                throw new IOException("Errore nella lettura del file JSON", e);
            }
        }

        // Trova il primo ID disponibile
        int newId = 1;
        while (existingIds.contains(newId)) {
            newId++;
        }

        return newId;
    }

    private boolean controllaCampi(String car, String date, String km){
        return (car.isEmpty() || date.isEmpty() || km.isEmpty());
    }

    private void reload3DModel(Color newColor) {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(newColor);
        material.setSpecularColor(newColor);

        /*((MeshView) Gmodel.getChildren().get(var)).setMaterial(material);
        System.out.println(var);
        var++;
         */

        for (int a : PezCol){
            ((MeshView) Gmodel.getChildren().get(a)).setMaterial(material);
        }
    }

    private void setFinOscu() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.BLACK);
        material.setSpecularColor(Color.BLACK);
        for(int a : PezFin){
            ((MeshView) Gmodel.getChildren().get(a)).setMaterial(material);
        }

    }

    private void setFinLight() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.WHITE);
        material.setSpecularColor(Color.WHITE);
        for(int a : PezFin){
            ((MeshView) Gmodel.getChildren().get(a)).setMaterial(material);
        }
    }

    public void menu_slider() {
            BoxBlur blur = new BoxBlur(6, 6, 3); // Crea un'istanza di BoxBlur
            butt_men.setOnMouseClicked(event -> slideMenuTo(0, false, true, blur));
            butt_men_back.setOnMouseClicked(event -> slideMenuTo(-200, true, false, null));
        }

    private void slideMenuTo(int x, boolean menuVisible, boolean menubackVisible, BoxBlur blurEffect) {
        TranslateTransition slideMenu = new TranslateTransition(); //creiamo un'istanza di TranslateTransition
        slideMenu.setDuration(Duration.seconds(0.4)); //tempo di transizione
        slideMenu.setNode(slider_men); //impostiamo il nodo su cui effettuare la transizione

        slideMenu.setToX(x); //impostiamo la posizione finale dello slider
        slideMenu.play(); //avviamo la transizione

        slideMenu.setOnFinished((ActionEvent e) -> {
            auto.setEffect(blurEffect); // Imposta l'effetto di sfocatura sulla pagina principale
            auto.setDisable(blurEffect != null); // Disabilita car_home se l'effetto di sfocatura è applicato
            butt_men.setVisible(menuVisible);
            butt_men_back.setVisible(menubackVisible);
        });
    }

    private void addPrice(int optional) {
        String text = price.getText().replace("€", "").trim();
        double currentPrice = Double.parseDouble(text);
        price.setText((currentPrice + optional) + " €");
    }

    private void removePrice(int optional) {
        String text = price.getText().replace("€", "").trim();
        double currentPrice = Double.parseDouble(text);
        price.setText((currentPrice - optional) + " €");
    }

    private SubScene createGroup() throws IOException {
        Group modelRoot;
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(posizioneCamera[0]);
        camera.setTranslateX(posizioneCamera[1]);
        camera.setTranslateY(posizioneCamera[2]);

        // Genera il nome del file sostituendo gli spazi con "_"
        String fileName = (car + ".obj").replace(" ", "_");

        // Percorso completo nella cartella "configuratore"
        String filePath = Paths.get("/configuratore", fileName).toString();

        // Stampa il percorso finale
        System.out.println("Percorso del file JSON: " + filePath);

        modelRoot = loadModel(getClass().getResource(filePath));
        modelRoot.setTranslateX(3);
        modelRoot.setTranslateY(4);


        Transform t = new Rotate(30, new Point3D(0,1,0));
        modelRoot.getTransforms().add(t);
        SubScene subScene = new SubScene(modelRoot, 1024, 630, true, null);
        subScene.setCamera(camera);
        initMouseControl(subScene, modelRoot);
        return subScene;
    }

    private Group loadModel(URL url) throws IOException {
        Gmodel = new Group();
        importer = new ObjModelImporter();
        importer.read(url);
        for (MeshView view : importer.getImport()){
            Gmodel.getChildren().add(view);
        }
        return Gmodel;
    }

    private void initMouseControl(SubScene subScene, Group modelRoot){
        //Rotate xRotate;
        Rotate yRotate;
        modelRoot.getTransforms().addAll(
                //xRotate = new Rotate(0, Rotate.X_AXIS)
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        //xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        subScene.setOnMousePressed(event -> {
            //anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            //anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        subScene.setOnMouseDragged(event -> {
            //angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY - event.getSceneX());
        });
        // Rotate the model automatically
        /*
        RotateTransition rt = new RotateTransition(Duration.seconds(30), modelRoot);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setCycleCount(RotateTransition.INDEFINITE);
        rt.play();
         */
    }
    /*
    class SmartGroup extends Group{
        Rotate r;
        Transform t = new Rotate();

        void rotateByX(int angle){
            r = new Rotate(angle, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
        void rotateByY(int angle){
            r = new Rotate(angle, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
*/

}
