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
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


public class DipendenteController {

    @FXML
    private Button Cliente;

    @FXML
    private BorderPane MenPrev;

    @FXML
    private BorderPane FindUser;

    @FXML
    private Button Ordini;

    @FXML
    private Button Preventivi;

    @FXML
    private Button NayPrev;

    @FXML
    private Button OkayPrev;

    @FXML
    private Button NayClient;

    @FXML
    private Button OkayClient;

    @FXML
    private Label UData;

    @FXML
    private TextField findUserTextField;

    private ObservableList<String> names = FXCollections.observableArrayList();
    private ArrayList<String> standbyNames = new ArrayList<String>();

    @FXML
    private ListView<String> userList = new ListView<String>(names);

    public void initialize(){
      MenPrev.setVisible(false);
      FindUser.setVisible(false);
      UData.setText(getDipendente());
    }

    private void spawnPrev(){
      MenPrev.setVisible(true);
    }
    
    @FXML
    void OkayPrev(ActionEvent event) throws IOException{
      MenPrev.setVisible(false);
    }
    @FXML
    void OkayClient(ActionEvent event) throws IOException{
      FindUser.setVisible(false);
      names.clear();
    }

    @FXML
    void Prevs(ActionEvent event){
      spawnPrev();
    }

    private String getDipendente(){
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
    private void showPreventivi(){
      String file = "ordini.json";
      Gson gson = new Gson();

      try (Reader reader = new FileReader(file)){
        JsonArray ordiniArray = gson.fromJson(reader, JsonArray.class);
      } catch (IOException e){
        e.printStackTrace();
      }
    }

    @FXML
    void refreshUserList(){
      names.clear();
      for (String name: standbyNames){
        if (name.contains(findUserTextField.getText())){
          names.add(name);
        }
      }
      userList.setItems(names);
    }

    @FXML
    void getUserSearch(ActionEvent event){
      FindUser.setVisible(true);
      String file = "dati_utente.json";
      Gson gson = new Gson();
      ArrayList<JsonObject> gsonUserList = new ArrayList<JsonObject>();

      try (Reader reader = new FileReader(file)){
        JsonArray usersArray = gson.fromJson(reader, JsonArray.class);
            //Creiamo un oggetto JSON per ogni utente
        for (JsonElement userElement : usersArray) {

          JsonObject userObject = userElement.getAsJsonObject();
          gsonUserList.add(userObject);
          if (userObject.get("type-user").getAsInt() == 2) {
              String temp = userObject.get("name").getAsString().concat(" ").concat(userObject.get("surname").getAsString()); 
              standbyNames.add(temp);
          }
        }
        refreshUserList();
      } catch (IOException e){
        e.printStackTrace();
      }
    }

}
