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
    private BorderPane MenOrdini;

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
    private Button NayOrders;

    @FXML
    private Button OkayOrders;

    @FXML
    private Label UData;

    @FXML
    private TextField findUserTextField;
    
    @FXML
    private TextField MenPrevTextField;

    @FXML
    private TextField MenOrdiniTextField;

    private final ObservableList<String> names = FXCollections.observableArrayList();
    private final ArrayList<String> standbyNames = new ArrayList<String>();
    private final ArrayList<JsonObject> gsonUserList = new ArrayList<JsonObject>();

    @FXML
    private ListView<String> userList = new ListView<String>(names);

    public void initialize(){
      MenPrev.setVisible(false);
      FindUser.setVisible(false);
      MenOrdini.setVisible(false);
      UData.setText(getDipendente());
    }

    @FXML
    void OkayPrev(ActionEvent event) throws IOException{
      MenPrev.setVisible(false);
    }
    @FXML
    void OkayClient(ActionEvent event) throws IOException{
      System.out.println(userList.getSelectionModel().getSelectedItem());
      names.clear();
      FindUser.setVisible(false);
      findUserTextField.setText("");
    }
    @FXML 
    void Nay(ActionEvent event) throws IOException{
      MenPrev.setVisible(false);
      FindUser.setVisible(false);
      MenOrdini.setVisible(false);
      findUserTextField.setText("");
    }

    @FXML
    void getOrders(ActionEvent event) throws IOException{
      MenOrdini.setVisible(true);
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
    @FXML
    private void getPreventivi() throws IOException{
      MenPrev.setVisible(true);
      String file = "ordini.json";
      Gson gson = new Gson();

      try (Reader reader = new FileReader(file)){
        JsonArray preventiviArray = gson.fromJson(reader, JsonArray.class);
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
      standbyNames.clear();
      FindUser.setVisible(true);
      gsonUserList.clear();
      String file = "dati_utente.json";
      Gson gson = new Gson();

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
