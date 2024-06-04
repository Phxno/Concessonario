package org.uni.progetto.homepage;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import com.google.gson.*;
import java.io.FileReader;


public class DipendenteController {

    @FXML
    private Button Cliente;

    @FXML
    private BorderPane MenPrev;

    @FXML
    private Button Ordini;

    @FXML
    private Button Preventivi;

    @FXML
    private Button Nay;

    @FXML
    private Button Okay;

    @FXML
    private Label UData;

    public void initialize(){
      MenPrev.setVisible(false);
      UData.setText(getPreventivi());
    }

    private void spawnPrev(){
      MenPrev.setVisible(true);
    }
    
    @FXML
    void Okay(ActionEvent event) throws IOException{
      MenPrev.setVisible(false);
    }

    @FXML
    void Prevs(ActionEvent event){
      spawnPrev();
    }

    private String getPreventivi(){
      String file = "dati_utente.json";
      JsonObject dati = new JsonObject();
            //proviamo a leggere il file dati_utente.json
            JsonArray dati_presenti;
            try (FileReader reader = new FileReader(file)) {
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
      return "";
    }

}
