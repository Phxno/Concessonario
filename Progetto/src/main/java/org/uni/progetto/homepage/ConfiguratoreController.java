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
import javafx.geometry.Side;
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
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfiguratoreController{

    // <editor-fold desc="Variabili Utili">
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
    private Button noAccess;
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
    private Button modelli;
    @FXML
    private Button myConf;
    @FXML
    private Label car_name;
    @FXML
    private ScrollPane scrollPane = new ScrollPane(imageContainer);
    @FXML
    private AnchorPane saveConfPopup;
    @FXML
    private TextField nomeConf;
    @FXML
    private Button saveConfButton;
    @FXML
    private Button annullaConfButton;

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
    ContextMenu contextMenu = new ContextMenu();
    // </editor-fold>

    /**
     * interfaccia per una callBack mentre si crea il form per l'usato per non evitare errori
     */
    public interface FormUsedCallback {
        void onFormConfirmed(int id);
    }

    // <editor-fold desc="Metodi creazione macchina e interfaccia">

    /**
     * Questo metodo inizializza la macchina con i valori di default
     * @param mod
     * @throws IOException
     */
    public void initMacchina(String mod)throws IOException {
        login_popup.setVisible(false);
        validation_popup.setVisible(false);
        form_used.setVisible(false);
        saveConfPopup.setVisible(false);
        checkUserSession();
        searchCar(mod);
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
        save.setOnAction(e -> {
            try {
                saveFunc();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
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
                openMarc();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        modelli.setOnAction(event -> {
            try {
                openMod();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        myConf.setOnAction(event -> {
            contextMenu.show(myConf, Side.BOTTOM, 0, 0);
        });

    }

    /**
     * Questo metodo controlla se l'utente è loggato
     */
    private void checkUserSession(){
        UserSession userSession = UserSession.getInstance();
        if (userSession != null) {
            // Se esiste, l'utente è loggato
            // Mostra i dati dell'utente
            User.setText(UserSession.getInstance().getUsername());
            isAuthenticated = true;
            myConf.setVisible(true);
            loadContextMenu();
        } else {
            // Se non esiste, l'utente non è loggato
            User.setText("Guest");
            myConf.setVisible(false);
            isAuthenticated = false;
        }
    }

    /**
     * Questo metodo ti fa mostra un PopUp per accedere al tuo account o registrarti se non l'hai ancora fatto
     * e vuoi inviare un preventivo o salvare la tua configurazioen
     */
    private void showLoginPopup(){
        login_popup.setVisible(true);
        login_popup.toFront();
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
                            myConf.setVisible(true);
                            loadContextMenu();
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

        noAccess.setOnAction(event -> {
            login_popup.setVisible(false);
        });
    }

    /**
     * Questo metodo cerca l'auto nel file JSON
     * @param modelloToFind modello dell'auto da cercare
     */
    private void searchCar(String modelloToFind) {
        try (FileReader reader = new FileReader("configuratore.json")) {
            // Parsa il file JSON in un JsonArray
            JsonElement parsed = JsonParser.parseReader(reader);
            if (!parsed.isJsonArray()) {
                throw new IllegalStateException("Il file JSON non contiene un array.");
            }

            JsonArray jsonArray = parsed.getAsJsonArray();
            JsonObject foundCar = null;

            // Iteriamo l'array per trovare l'oggetto con il modello desiderato
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.get("modello").getAsString().equals(modelloToFind)) {
                    foundCar = jsonObject;
                    break;
                }
            }

            // Se abbiamo trovato l'auto, convertiamo il JSON in un oggetto AutoConf
            if (foundCar != null) {
                Gson gson = new Gson();
                AutoConf auto = gson.fromJson(foundCar, AutoConf.class);

                // Salva i valori nelle variabili globali
                car = auto.getMarca() + " " + auto.getModello();
                Motore_batteria = auto.getListaMotori();
                Base_price = auto.getPrezzo();
                PezFin = auto.getFinestrini();
                PezCol = auto.getListaPezziAutoColorati();
                posizioneCamera = auto.getPosizioneCamera();
            } else {
                throw new IllegalArgumentException("Nessuna configurazione trovata per il modello: " + modelloToFind);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Questo metodo chiama createGroup() per creare la macchina 3D
     */
    private void createCar(){
        SubScene subScene;
        try {
            subScene = createGroup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        add3DModel(subScene);
    }

    /**
     * Questo metodo prende il file della macchina in 3D e lo carica con la libreria ObjModelImporter
     * fissa la camera e la posizione della macchina nello spazio e chiama la funzione initMouseControl
     * che permette all'utente di ruotare la macchina a piaciemento
     * @return SubScene la scena 3D della macchina
     * @throws IOException se il file non viene trovato
     */
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

    /**
     * Questo metodo attacca la scena nell' anchorPane auto per visualizzare la macchina 3D
     * @param model scena 3D della macchina
     */
    private void add3DModel(SubScene model) {
        Platform.runLater(() -> {
            auto.getChildren().add(model);
        });
    }

    /**
     * Questo metodo rimuove la scena 3D dalla anchorPane auto
     */
    private void remove3DModel() {
        Platform.runLater(() -> {
            auto.getChildren().removeIf(node -> node instanceof SubScene);
        });
    }

    /**
     * Questo metodo della libreria ObjModelImporter carica il file 3D della macchina
     * e lo dichiara come un oggetto Group
     * @param url URL del file 3D
     * @return Group il modello 3D della macchina
     */
    private Group loadModel(URL url) {
        Gmodel = new Group();
        importer = new ObjModelImporter();
        importer.read(url);
        for (MeshView view : importer.getImport()){
            Gmodel.getChildren().add(view);
        }
        return Gmodel;
    }

    /**
     * Questo metodo permette all'utente di ruotare la macchina 3D solo sulla Y
     * @param subScene  scena 3D della macchina
     * @param modelRoot modello 3D della macchina
     */
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
    // </editor-fold>

    // <editor-fold desc="Metodi per la gestione del menu">

    /**
     * Questo metodo apre la finestra delle marche
     * @throws IOException
     */
    private void openMarc() throws IOException {
        Stage stage = (Stage) marche.getScene().getWindow();
        stage.close();
        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(Registration.class.getResource("/FXML/Marche.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Marche");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Questo metodo apre la finestra dei modelli
     * @throws IOException
     */
    private void openMod() throws IOException{
        Stage stage = (Stage) modelli.getScene().getWindow();
        stage.close();
        // Carica la scena della homepage
        FXMLLoader fxmlLoader = new FXMLLoader(Registration.class.getResource("/FXML/Modelli.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        stage.setTitle("Modelli");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Questo metodo apre la finestra della homepage
     * @throws IOException
     */
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

    /**
     * Questo metodo crea un'animazione per lo slider del menu
     */
    public void menu_slider() {
        BoxBlur blur = new BoxBlur(6, 6, 3); // Crea un'istanza di BoxBlur
        butt_men.setOnMouseClicked(event -> slideMenuTo(0, false, true, blur));
        butt_men_back.setOnMouseClicked(event -> slideMenuTo(-200, true, false, null));
    }

    /**
     * Questo metodo crea un'animazione per lo slider del menu
     * @param x posizione finale dello slider
     * @param menuVisible visibilità del menu
     * @param menubackVisible visibilità del menuback
     * @param blurEffect effetto di sfocatura
     */
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

    // </editor-fold>

    // <editor-fold desc="Metodi per la gestione delle configurazioni">

    /**
     * Questo metodo se il cliente preme il pulsante (base o Full Optional)
     * imposta tutti gli altri tasti per velocizzare la configurazione
     * in caso il sappia gia come voglia la macchina
     */
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

    /**
     * Questo metodo aggiunge o toglie il prezzo dell'Optional al prezzo totale solo per la shortcut
     * @param checkBox checkBox dell'optional
     * @param optional prezzo dell'optional
     * @param isSelected stato dell'optional
     */
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

    /**
     * Questo metodo se premi la ChoiceBox del motore/batteria ti imposta il motore che prefersci
     */
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

    /**
     * Questo metoto ti aggiunge o toglie  l'Optional Telacamera Posteriore alla macchina
     */
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

    /**
     * Questo metodo ti aggiuge o toglie l'Optional Finestrini Oscurati alla macchina
     */
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

    /**
     * Questo metodo ti aggiunge o toglie l'Optional ADAS avanzati alla macchina
     */
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

    /**
     * Questo metodo ti aggiunge o toglie l'Optional Sedili Riscaldatni alla macchina
     */
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

    /**
     * Questo metodo ti seleziona il colore da scegliere per la tua macchina
     */
    private void setColo(){
        Color newColor = color_picker.getValue();
        reload3DModel(newColor);
    }

    /**
     * Questo metod cambia il colore della macchina
     * @param newColor  colore da assegnare alla macchina
     */
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

    /**
     * Questo metodo imposta sul modello 3D i finestrini oscurati
     */
    private void setFinOscu() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.BLACK);
        material.setSpecularColor(Color.BLACK);
        for(int a : PezFin){
            ((MeshView) Gmodel.getChildren().get(a)).setMaterial(material);
        }

    }

    /**
     * Questo metodo imposta sul modello 3D i finestrini normali
     */
    private void setFinLight() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.WHITE);
        material.setSpecularColor(Color.WHITE);
        for(int a : PezFin){
            ((MeshView) Gmodel.getChildren().get(a)).setMaterial(material);
        }
    }

    /**
     * Questo metodo aggiunge il prezzo dell'Optional al prezzo totale
     * @param optional prezzo dell'optional
     */
    private void addPrice(int optional) {
        String text = price.getText().replace("€", "").trim();
        double currentPrice = Double.parseDouble(text);
        price.setText((currentPrice + optional) + " €");
    }

    /**
     * Questo metodo rimuove il prezzo dell'Optional al prezzo totale
     * @param optional prezzo dell'optional
     */
    private void removePrice(int optional) {
        String text = price.getText().replace("€", "").trim();
        double currentPrice = Double.parseDouble(text);
        price.setText((currentPrice - optional) + " €");
    }

    // </editor-fold>

    // <editor-fold desc="Metodi per la gestione dell'invio preventivi">

    /**
     * Questo metodo quando premi il pulsante dell'invio richista di preventivo ti chiedera di selezionare la sede di
     * dove volere ritirare la macchina e poi ti chiede sa hai un usato da farci ritirare per avere uno sconto ulteriore
     * sul prezzo finale della macchina che stai per comprare
     */
    private void sendfunc(){
        if (!isAuthenticated){
            showLoginPopup();
        }
        else{
            showValidationPopup();
        }

    }

    /**
     * Questo metodo ti fa vedere il PopUP per inseire i dati della sede e dell'usato e la conferma dell'invio preventivo
     */
    private void showValidationPopup(){
        AtomicInteger id_usato = new AtomicInteger();
        validation_popup.setVisible(true);

        si.setOnAction(event ->{
            if (si.isSelected()){
                no.setSelected(false);

                createFormUsed(id -> {
                    id_usato.set(id); // Salva l'ID ricevuto
                });
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
            JsonArray conf = saveConf();
            try {
                saveInJson("preventivi.json", creaJsonObject(
                        "utente", UserSession.getInstance().getFirstName() + " " + UserSession.getInstance().getLastName(),
                        "macchina", car,
                        "configurazione", conf,
                        "usato", id_usato,
                        "dataCreazione", getDataDiOggi(),
                        "dataConsegna", "",
                        "prezzo", price.getText(),
                        "sconto", sconto + "%",
                        "negozioConsegna", sede.getValue(),
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

    /**
     * Se hai un usato da farci ritirare ti chiede di inserire i dati della macchina usata con relative immagini
     * nel nuovo Form comparso a schermo
     * @return id dell'usato
     */
    private void createFormUsed(FormUsedCallback callback) {
        AtomicInteger id = new AtomicInteger();
        try {
            id.set(createId("usato.json"));
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

            // Mostra la finestra di dialogo per selezionare i file
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);

            // Se l'utente ha selezionato dei file, carica le immagini
            if (selectedFiles != null && !selectedFiles.isEmpty()) {
                for (File file : selectedFiles) {
                    Image image = new Image(file.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(120); // Larghezza dell'immagine
                    imageView.setFitHeight(120); // Altezza dell'immagine
                    imageView.setPreserveRatio(true); // Mantieni le proporzioni
                    imageContainer.getChildren().add(imageView); // Aggiungi l'ImageView al FlowPane
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
            int id_usato = id.get();

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
            callback.onFormConfirmed(id_usato);
        });

        back.setOnAction(event -> {
            form_used.setVisible(false);
            clearUsedForm();
            si.setSelected(false);
            callback.onFormConfirmed(0);
        });
    }

    /**
     * Questo metodo elimina l'usato dal file JSON in base all'id specificato
     * @param id id dell'usato da eliminare
     * @throws IOException se si verifica un errore durante la lettura o la scrittura del file JSON
     */
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

    /**
     * Questo metodo controlla semplicemente che tutti i campi siano stati compilati
     * @param car nome dell'auto
     * @param date data di immatricolazione
     * @param km chilometraggio
     * @return TRUE se almeno un campo è vuoto, altrimenti FALSE
     */
    private boolean controllaCampi(String car, String date, String km){
        return (car.isEmpty() || date.isEmpty() || km.isEmpty());
    }

    /**
     * Questo metodo pulisce i campi del form dell'usato
     */
    private void clearUsedForm(){
        car_info.clear();
        km_info.clear();
        date_info.clear();
        II_mano.setSelected(false);
        III_mano.setSelected(false);
        imageContainer.getChildren().clear();
    }

    /**
     * Questo metodo converte un'immagine in un formato Base64  per salvarla nel file JSON
     * @param image immagine da convertire
     * @return stringa Base64 dell'immagine
     */
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

    /**
     * Questo metodo crea la data di oggi nel formato yyyy-MM-dd
     * @return data di oggi
     */
    private String getDataDiOggi() {
        // Ottieni la data di oggi
        LocalDate oggi = LocalDate.now();
        // Definisci il formato desiderato
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Formatta la data nel formato yyyy-MM-dd
        return oggi.format(formatter);
    }
    // </editor-fold>

    // <editor-fold desc="Metodi per la gestione del salvataggio delle configurazioni">

    /**
     * Questo metodo ti permette di salvare la configurazione della macchina  nella sezione tua personale MieConfigurazioni
     * @throws IOException se si verifica un errore durante la lettura o la scrittura del file JSON
     */
    private void saveFunc() throws IOException {
        if (!isAuthenticated){
            System.out.println("User is not authenticated, showing login popup.");
            showLoginPopup();
        }
        else {
            showConfirmSaveConf();
        }
    }

    /**
     * Questo metodo ti fa vedere il PopUP per salvare la configurazione dove devi inserire il nome della Tua configurazione
     */
    private void showConfirmSaveConf(){
        saveConfPopup.setVisible(true);
        saveConfPopup.toFront();
        saveConfButton.setOnAction(event -> {
            String nome = nomeConf.getText();
            if (nome.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText("Nome non valido");
                alert.setContentText("Devi inserire un nome per la configurazione prima di salvarla.");
                alert.showAndWait();
                return;
            }
            JsonArray conf = saveConf();
            try {
                if (checkIfAlredaySaved(nome)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attenzione");
                    alert.setHeaderText("Configurazione già salvata");
                    alert.setContentText("Hai già salvato questa configurazione. Inserisci un nome diverso.");
                    alert.showAndWait();
                    return;
                }
                String[] parts = car.split(" ",2);
                saveInJson("configurazioni.json", creaJsonObject(
                        "nomeConfigurazione", nome,
                        "utente", UserSession.getInstance().getUsername(),
                        "marca", parts[0],
                        "modello", parts[1],
                        "configurazione", conf,
                        "dataCreazione", getDataDiOggi(),
                        "id", createId("configurazioni.json")
                ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            saveConfPopup.setVisible(false);
            nomeConf.clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operazione Riuscita");
            alert.setHeaderText("Configurazione salvata con successo!");
            alert.setContentText("Puoi visualizzarla nella sezione 'Le mie configurazioni'.");
            alert.showAndWait();
            loadContextMenu();
        });

        annullaConfButton.setOnAction(event -> {
            saveConfPopup.setVisible(false);
            nomeConf.clear();
        });
    }

    /**
     * Questo metodo controlla se la configurazione è già stata salvata
     * @param nomeConf nome della configurazione
     * @return TRUE se la configurazione è già stata salvata, altrimenti FALSE
     */
    private boolean checkIfAlredaySaved(String nomeConf){
        try (FileReader reader = new FileReader("configurazioni.json")) {
            // Parsa il file JSON in un JsonArray
            JsonElement parsed = JsonParser.parseReader(reader);
            if (!parsed.isJsonArray()) {
                throw new IllegalStateException("Il file JSON non contiene un array.");
            }

            JsonArray jsonArray = parsed.getAsJsonArray();

            if (jsonArray.isEmpty()) {
                return false; // Nessun oggetto nel file JSON
            }
            else{
                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    if (jsonObject.get("nomeConfigurazione").getAsString().equals(nomeConf) &&
                            jsonObject.get("utente").getAsString().equals(UserSession.getInstance().getUsername())) {
                        return true;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Questo metodo ti ritorna una lista di tutte le configurazioni salvate dall'utente
     * @return lista delle configurazioni salvate
     */
    private JsonArray saveConf(){
        JsonArray conf = new JsonArray();
        Color color = color_picker.getValue();
        String hexColor = String.format("#%02x%02x%02x",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        conf.add(hexColor);
        conf.add(choice.getValue());
        conf.add(tele_pos.isSelected());
        conf.add(fin_oscu.isSelected());
        conf.add(adas.isSelected());
        conf.add(sed_ris.isSelected());

        return conf;
    }

    /**
     * Questo metodo mi carica le mie configurazioni salvate in caso io volessi vederle o modificarle alla premuta
     * del pulsante MieConfigurazioni
     */
    private void loadContextMenu(){
        contextMenu.getItems().clear();
        for (String config : searchMyConf()) {
            MenuItem item = new MenuItem(config);
            item.setOnAction(event -> {
                try {
                    remove3DModel();
                    loadConf(config);
                    slideMenuTo(-200, true, false, null);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            contextMenu.getItems().add(item);
        }
    }

    /**
     * Questo metodo cerca le configurazioni salvate dall'utente nel file JSON in base al nome utente con cui sei Loggato
     * @return lista delle configurazioni salvate
     */
    private ArrayList<String> searchMyConf(){
        ArrayList<String> myConf = new ArrayList<>();
        try (FileReader reader = new FileReader("configurazioni.json")) {
            // Parsa il file JSON in un JsonArray
            JsonElement parsed = JsonParser.parseReader(reader);
            if (!parsed.isJsonArray()) {
                throw new IllegalStateException("Il file JSON non contiene un array.");
            }

            JsonArray jsonArray = parsed.getAsJsonArray();

            if (jsonArray.isEmpty()) {
                return myConf; // Nessun oggetto nel file JSON
            }
            else{
                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    if (jsonObject.get("utente").getAsString().equals(UserSession.getInstance().getUsername())) {
                        myConf.add(jsonObject.get("nomeConfigurazione").getAsString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myConf;
    }

    /**
     * Questo metodo carica la configurazione selezionata dall'utente nel configuratore
     * @param config nome della configurazione
     * @throws IOException se si verifica un errore durante la lettura del file JSON
     */
    private void loadConf(String config) throws IOException {
        searchCar(getModelFrommyConf(config));  // Cerca l'auto e aggiorna le variabili globali
        car_name.setText(car);
        createCar();  // Crea la macchina 3D

        ArrayList<Object> confList = new ArrayList<>();

        try (FileReader reader = new FileReader("configurazioni.json")) {
            JsonElement parsed = JsonParser.parseReader(reader);
            if (!parsed.isJsonArray()) {
                throw new IllegalStateException("Il file JSON non contiene un array.");
            }

            JsonArray jsonArray = parsed.getAsJsonArray();
            if (jsonArray.isEmpty()) {
                System.err.println("Il JSON è vuoto.");
                return;
            }

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.get("nomeConfigurazione").getAsString().equals(config)) {
                    JsonElement confElement = jsonObject.get("configurazione");


                    if (!confElement.isJsonArray()) {
                        throw new IllegalStateException("La configurazione non è un array valido.");
                    }

                    JsonArray confArray = confElement.getAsJsonArray();
                    confList.clear(); // Assicura che la lista sia pulita
                    for (JsonElement item : confArray) {
                        if (item.isJsonPrimitive()) {
                            confList.add(item.getAsString());  // Salva sempre come stringa
                        } else {
                            confList.add(item.toString()); // Ultima risorsa se non è primitivo
                        }
                    }
                    break;  // Trova la prima configurazione valida ed esce
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Errore durante il parsing del JSON.");
        }
        

        if (confList.size() < 6) {
            System.err.println("Errore: Configurazione incompleta. Elementi disponibili: " + confList.size());
            return;
        }

        String hexColor = confList.get(0).toString();

        Platform.runLater(() -> {
            try {

                color_picker.setValue(Color.web(hexColor));
                reload3DModel(color_picker.getValue());


                choice.getItems().clear();
                choice.getItems().addAll(Motore_batteria);
                choice.setValue(confList.get(1).toString());saveConf();

                tele_pos.setSelected(Boolean.parseBoolean(confList.get(2).toString()));
                fin_oscu.setSelected(Boolean.parseBoolean(confList.get(3).toString()));
                if (fin_oscu.isSelected()) {
                    setFinOscu();
                } else {
                    setFinLight();
                }
                adas.setSelected(Boolean.parseBoolean(confList.get(4).toString()));
                sed_ris.setSelected(Boolean.parseBoolean(confList.get(5).toString()));

                price.setText(Base_price + " €");
                if(choice.getValue().equals(Motore_batteria[1]))addPrice(Optionals.ENGINE.getOptionals());
                if(tele_pos.isSelected())addPrice(Optionals.TEL_POS.getOptionals());
                if(fin_oscu.isSelected())addPrice(Optionals.MIRRORS.getOptionals());
                if(adas.isSelected())addPrice(Optionals.ADAS.getOptionals());
                if(sed_ris.isSelected())addPrice(Optionals.HEATS_SEATS.getOptionals());

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Errore durante l'aggiornamento della UI.");
            }
        });
    }


    /**
     * Questo metodo trova il modello dell'auto in base al nome della configurazione
     * @param nameConf nome della configurazione
     * @return modello dell'auto
     */
    private String getModelFrommyConf(String nameConf){
        try (FileReader reader = new FileReader("configurazioni.json")) {
            // Parsa il file JSON in un JsonArray
            JsonElement parsed = JsonParser.parseReader(reader);
            if (!parsed.isJsonArray()) {
                throw new IllegalStateException("Il file JSON non contiene un array.");
            }

            JsonArray jsonArray = parsed.getAsJsonArray();

            if (jsonArray.isEmpty()) {
                return nameConf;
            }
            else{
                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    if (jsonObject.get("nomeConfigurazione").getAsString().equals(nameConf)) {
                        return jsonObject.get("modello").getAsString();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold desc="Metodi per la gestione dei file JSON">

    /**
     * Questo metodo crea un oggetto JsonObject a partire da una serie di argomenti chiave-valore
     * @param args argomenti chiave-valore
     * @return JsonObject creato
     */
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
            } else if (valore instanceof JsonArray) {
                jsonObject.add(chiave, (JsonArray) valore);
            } else if (valore instanceof JsonObject) { //
                jsonObject.add(chiave, (JsonObject) valore);
            } else {
                // Se il valore non è di un tipo supportato, lo convertiamo in stringa
                jsonObject.addProperty(chiave, valore.toString());
            }
        }

        return jsonObject;
    }

    /**
     * Questo metodo salva un oggetto JsonObject in un file JSON
     * @param filePath percorso del file JSON
     * @param dati dati da salvare
     */
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

    /**
     * Questo metodo crea un ID univoco per un nuovo oggetto JSON
     * @param filepath percorso del file JSON
     * @return ID univoco
     * @throws IOException se si verifica un errore durante la lettura del file JSON
     */
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

    // </editor-fold>

}
