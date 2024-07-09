package org.uni.progetto.homepage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


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
      Gson gson = new Gson();
      String userData = "";

      try (Reader reader = new FileReader(file)){
        JsonArray usersArray = gson.fromJson(reader, JsonArray.class);
            //Creiamo un oggetto JSON per ogni utente
        for (JsonElement userElement : usersArray) {
          JsonObject userObject = userElement.getAsJsonObject();
          if (userObject.get("type-user").getAsInt() == 0){
            userData = userObject.get("name").getAsString().concat(" ").concat(userObject.get("surname").getAsString());
          }
        }
      } catch (IOException e){
        e.printStackTrace();
      }
      return userData;

    }

}
