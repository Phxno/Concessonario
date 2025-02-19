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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.Desktop;

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
    @FXML
    private Label title;

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

    private void showPrevPopup(String config){
        prevPopUp.setVisible(true);
        String[] get_id = config.split(" ");
        JsonElement jsonElement =  getJsonById(get_id[0], "preventivi.json");
        title.setText("Preventivo n° " + jsonElement.getAsJsonObject().get("id").getAsString());
        scadenza.setText("scade il " + createdataScadenza(jsonElement.getAsJsonObject().get("dataCreazione").getAsString()));
        carText.setText(jsonElement.getAsJsonObject().get("macchina").getAsString());
        addConf(jsonElement.getAsJsonObject().get("configurazione").getAsJsonArray());
        prezzoAuto.setText(jsonElement.getAsJsonObject().get("prezzo").getAsString());
        prezzoUsato.setText(jsonElement.getAsJsonObject().get("prezzoUsato").getAsString());
        prezzoUsato2.setText(jsonElement.getAsJsonObject().get("prezzoUsato").getAsString());
        sconto.setText(jsonElement.getAsJsonObject().get("sconto").getAsString());
        prezzoFinale.setText(calcPrezzoFinale(prezzoAuto.getText(), prezzoUsato.getText(), sconto.getText()));

        pay_confirm.setOnAction(event ->{


            // Estrarre l'ultima parola dalla stringa della Label
            String[] words = title.getText().split(" ");
            String lastWord = words[words.length - 1]; // Ultima parola


            JsonObject jsonObject = getJsonById(lastWord, "preventivi.json");// preventivo

            int idOrdine = 0;
            try {
                idOrdine = createId("ordini.json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (jsonObject != null) {
                jsonObject.addProperty("dataFinalizzazione", LocalDate.now().toString());
                jsonObject.addProperty("dataConsegna", createDataConsegna(items));
                jsonObject.addProperty("id", idOrdine);
                jsonObject.addProperty("prezzo", prezzoFinale.getText());
                jsonObject.remove("prezzoUsato");
                jsonObject.remove("usato");
                jsonObject.remove("sconto");

            }
            saveOrdine(jsonObject);

            JsonElement sedi = readJson("sedi.json");

            JsonArray sediArray = sedi.getAsJsonArray();//sedi

            String negozioConsegna = jsonElement.getAsJsonObject().get("negozioConsegna").getAsString();

            // Scansiona le sedi
            for (JsonElement sede : sediArray) {
                JsonObject sedeObject = sede.getAsJsonObject();

                // Controlla se il nome della sede corrisponde
                if (sedeObject.get("Nome").getAsString().equals(negozioConsegna)) {

                    // Recupera la lista degli ordini o la crea se non esiste
                    JsonArray ordini;
                    if (sedeObject.has("Ordini")) {
                        ordini = sedeObject.getAsJsonArray("Ordini");
                    } else {
                        ordini = new JsonArray();
                        sedeObject.add("Ordini", ordini);
                    }

                    // Aggiunge l'ID dell'ordine solo se non è già presente
                    if (!ordini.contains(new JsonPrimitive(idOrdine))) {
                        ordini.add(idOrdine);
                    }
                    break;
                }
            }
            try (FileWriter writer = new FileWriter("sedi.json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(sediArray, writer);
            }catch (IOException e) {
            System.err.println("Errore nella lettura/scrittura di sedi.json: " + e.getMessage());
        }


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pagamento");
            alert.setHeaderText("Pagamento effettuato con successo");
            alert.setContentText("Il pagamento è stato effettuato con successo. Grazie per aver scelto il nostro concessionario!");
            alert.showAndWait();
            removePrev(lastWord);
            generaPDFConferma(sediArray, "" + idOrdine, "ordini.json");
            loadContextMenu();
            items.clear();
            prevPopUp.setVisible(false);
        });

        back.setOnAction(event ->{
            items.clear();
            prevPopUp.setVisible(false);
        });

    }

    public static void generaPDFConferma(JsonArray sedi, String idOrdine,  String filePathOrdini) {
        try {
            // Legge il file ordini.json
            JsonObject ordineTrovato = getJsonById(idOrdine, filePathOrdini);

            String sedeNome = ordineTrovato.get("negozioConsegna").getAsString();
            String sedeIndirizzo = "Indirizzo non trovato";


            for (JsonElement sede : sedi) {
                JsonObject sedeObj = sede.getAsJsonObject();
                if (sedeObj.get("Nome").getAsString().equals(sedeNome)) {
                    sedeIndirizzo = sedeObj.get("Indirizzo").getAsString();
                    break;
                }
            }


            // Percorso della cartella Download dell'utente
            String downloadPath = getDownloadFolderPath() + "/Conferma_Ordine_" + idOrdine + ".pdf";
            System.out.println(downloadPath);

            // Creazione del PDF
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);


                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    System.out.println("PDF creato con successo");

                    PDImageXObject logo = PDImageXObject.createFromFile("/Users/tommasobelle/Documents/Uni/Secondo Anno/Ingegneria del software/Concessonario/Progetto/src/main/resources/Images/Logo concessionario colorato.png", document);
                    contentStream.drawImage(logo, 50, 700, 100, 50); // Posizione e dimensione del logo

                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, 700);
                    contentStream.showText("Conferma Ordine #" + idOrdine);
                    contentStream.endText();
                    System.out.println("Titolo inserito con successo");

                    contentStream.setFont(PDType1Font.HELVETICA, 14);
                    int yPosition = 650;

                    for (String key : ordineTrovato.keySet()) {
                        if (!key.equals("configurazione")){
                            contentStream.beginText();
                            contentStream.newLineAtOffset(100, yPosition);
                            contentStream.showText(key + ": " + ordineTrovato.get(key).getAsString());
                            contentStream.endText();
                            yPosition -= 20;
                        }
                    }

                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosition - 20);
                    contentStream.showText("Sede: " + sedeNome);
                    contentStream.endText();

                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosition - 40);
                    contentStream.showText("Indirizzo: " + sedeIndirizzo);
                    contentStream.endText();
                    System.out.println("Contenuto inserito con successo");
                }catch (IOException e) {
                    System.err.println("Errore durante la scrittura del contenuto del PDF: " + e.getMessage());
                }

                document.save(downloadPath);
            }

            System.out.println("PDF generato con successo: " + downloadPath);

            // Aprire automaticamente il PDF dopo il download
            File pdfFile = new File(downloadPath);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
                System.out.println("PDF aperto con successo");
            }

        } catch (IOException e) {
            System.err.println("Errore nella generazione del PDF: " + e.getMessage());
        }
    }


    private void saveOrdine(JsonObject jsonObject){
        try {
            // Legge il file JSON esistente
            JsonArray ordiniArray;
            try (FileReader reader = new FileReader("ordini.json")) {
                ordiniArray = JsonParser.parseReader(reader).getAsJsonArray();
            }

            // Aggiunge il nuovo preventivo alla lista ordini
            ordiniArray.add(jsonObject);

            // Scrive il file aggiornato
            try (FileWriter writer = new FileWriter("ordini.json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                writer.write(gson.toJson(ordiniArray));
            }

        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del preventivo: " + e.getMessage());
        }
    }

    public static String getDownloadFolderPath() {
        String userHome = System.getProperty("user.home");

        // Percorsi standard delle cartelle Download su diversi sistemi operativi
        String[] possiblePaths = {
                userHome + "/Downloads",         // Linux/macOS
                userHome + "/Scaricati",         // macOS in italiano
                userHome + "/Desktop/Downloads", // Alcune distro Linux
                userHome + "\\Downloads"         // Windows
        };

        for (String path : possiblePaths) {
            if (Files.exists(Paths.get(path))) {
                return path; // Restituisce il primo percorso valido
            }
        }

        return userHome; // Se la cartella Download non esiste, usa la home come fallback
    }

    private static JsonObject getJsonById(String id, String filepath) {
        JsonArray jsonArray = Objects.requireNonNull(readJson(filepath)).getAsJsonArray();
        if (jsonArray.isEmpty()) {
            return null; // Nessun oggetto nel file JSON
        }
        else{
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                if (jsonObject.get("id").getAsString().equals(id)) {
                    System.out.println("id trovato");
                    return jsonObject;
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

        JsonArray jsonArray = Objects.requireNonNull(readJson("preventivi.json")).getAsJsonArray();

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

    private String createDataConsegna(ObservableList<Object> items){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate oggi = LocalDate.now();
        LocalDate dataConsegna = oggi.plusMonths(1);
        for (Object item : items) {
            if (item instanceof String stringItem) {
                if (stringItem.contains("ADAS")) {
                    dataConsegna = dataConsegna.plusDays(10);
                }
                if (stringItem.contains("Sedili Riscaldati")) {
                    dataConsegna = dataConsegna.plusDays(10);
                }
                if (stringItem.contains("Telecamera Posteriore")) {
                    dataConsegna = dataConsegna.plusDays(10);
                }
                if (stringItem.contains("Finestrini Oscurati")) {
                    dataConsegna = dataConsegna.plusDays(10);
                }
            }
            if (item instanceof Color colorItem) {
                if(!colorItem.equals(Color.WHITE) && !colorItem.equals(Color.BLACK)){
                    dataConsegna = dataConsegna.plusDays(10);
                }
            }
        }
        return dataConsegna.format(formatter);
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

    private void loadContextMenu(){
        contextMenu.getItems().clear();
        for (String config : searchMyPrev()) {
            MenuItem item = new MenuItem(config);
            item.setOnAction(event -> {
                slideMenuTo(-200, true, false, null);
                contextMenu.hide();
                showPrevPopup(config);
            });
            contextMenu.getItems().add(item);
        }
    }

    private static JsonElement readJson(String file) {
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
        JsonArray jsonArray = Objects.requireNonNull(readJson("preventivi.json")).getAsJsonArray();
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
            //userObject.get("username").getAsString().equals(user) && userObject.get("password").getAsString().equals(pass)
            //Creiamo un oggetto JSON per ogni utente
            for (JsonElement userElement : usersArray) {
                JsonObject userObject = userElement.getAsJsonObject();

                //compariamo per ogni utente il campo username e password con quelli inseriti dall'utente
                if (userObject.has("username") && userObject.has("password")) {//controlliamo che i campi username e password siano presenti
                    if (campi_corretti(user,userObject.get("username").getAsString(),pass,userObject.get("password").getAsString())) { //se i campi username e password sono corretti
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

                        if(userObject.get("type-user").getAsInt() < 2){
                            open_dipendente(nome + " " + cognome,userObject.get("type-user").getAsInt());
                        }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean campi_vuoti(String user, String pass) {
        return (user.isEmpty() || pass.isEmpty());
    }

    public boolean campi_corretti(String user,String userD, String pass,String passD) {
        return (user.equals(userD) && pass.equals(passD));
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

    void open_dipendente(String dip, Integer type_user) throws IOException {
        Stage stage = (Stage) marche_button.getScene().getWindow();
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

    @FXML
    void open_modelli(ActionEvent event) throws IOException {

        Stage stage = (Stage) modelli_button.getScene().getWindow();
        stage.close();
        // Carica la scena modelli
        FXMLLoader fxmlLoader = new FXMLLoader(org.uni.progetto.homepage.Homepage.class.getResource("/FXML/Modelli.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);  //dimensione finestra 1024x768 pixel
        ModelliController controller = fxmlLoader.getController();
        controller.initialize("All");
        stage.setTitle("Modelli");
        stage.setScene(scene);
        stage.show();
    }

}




