package org.uni.progetto.homepage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.Scene;
import javafx.stage.Stage;


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
    private Button backHomepageButton;

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
    private final ObservableList<String> orders = FXCollections.observableArrayList();
    private final ObservableList<String> prevs = FXCollections.observableArrayList();
    private final ArrayList<String> standbyNames = new ArrayList<String>();
    private final ArrayList<String> standbyPrevs = new ArrayList<String>();
    private final ArrayList<String> standbyOrders = new ArrayList<String>();
    private final ArrayList<JsonObject> gsonUserList = new ArrayList<JsonObject>();
    private final ArrayList<JsonObject> gsonPrevsList = new ArrayList<JsonObject>();
    private final ArrayList<JsonObject> gsonOrderList = new ArrayList<JsonObject>();
    @FXML
    private ListView<String> userList = new ListView<String>(names);

    @FXML
    private ListView<String> prevList = new ListView<String>(prevs);

    @FXML
    private ListView<String> ordersList = new ListView<String>(orders);

    public void initialize(String dip){
      MenPrev.setVisible(false);
      FindUser.setVisible(false);
      MenOrdini.setVisible(false);
      UData.setText(dip);
    }

    @FXML
    void backHomepage(ActionEvent event) throws IOException{
      Stage stage = (Stage) backHomepageButton.getScene().getWindow();
      stage.close();
      FXMLLoader fxmlLoader = new FXMLLoader(Homepage.class.getResource("/FXML/Homepage.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
      stage.setTitle("Concessionario - Homepage");
      stage.setMinWidth(1024);
      stage.setMinHeight(768);
      stage.setMaxWidth(1024);
      stage.setMaxHeight(768);
      stage.setScene(scene);
      stage.show();
    }

    @FXML
    void OkayPrev(ActionEvent event) throws IOException{
      MenPrev.setVisible(false);
      System.out.println(prevList.getSelectionModel().getSelectedItem());
      orders.clear();
      MenPrevTextField.setText("");
    }
    @FXML
    void OkayClient(ActionEvent event) throws IOException{
      System.out.println(userList.getSelectionModel().getSelectedItem());
      names.clear();
      FindUser.setVisible(false);
      findUserTextField.setText("");
    }
    @FXML
    void OkayOrders(ActionEvent event) throws IOException {
      String ord = ordersList.getSelectionModel().getSelectedItem();
      System.out.println(ord);
      int id = -1;
      if (ord != null){
        OrderClass ordTemp = null;
        MenOrdini.setVisible(false);
        for (JsonObject orderObject : gsonOrderList) {
          String temp = orderObject.get("id").getAsString() + " - "+ orderObject.get("utente").getAsString();
          if (temp.equals(ord)){
            ordTemp = new OrderClass(orderObject);
            break;
          }
        }
        orders.clear();
        MenOrdiniTextField.setText("");
        if (ordTemp != null) loadOrdini(ordTemp);
      }
    }
    @FXML 
    void Nay(ActionEvent event) throws IOException{
      MenPrev.setVisible(false);
      FindUser.setVisible(false);
      MenOrdini.setVisible(false);
      findUserTextField.setText("");
    }

    @FXML
    void refreshOrdersList(){
      orders.clear();
      for (String order: standbyOrders){
        if (order.contains(MenOrdiniTextField.getText())){
          orders.add(order);
        }
      }
      ordersList.setItems(orders);
    }

    @FXML
    void getOrders(ActionEvent event){
      standbyOrders.clear();
      MenOrdini.setVisible(true);
      gsonOrderList.clear();
      String file = "ordini.json";
      Gson gson = new Gson();

      try (Reader reader = new FileReader(file)){
        JsonArray ordersArray = gson.fromJson(reader, JsonArray.class);

        for (JsonElement orderElement : ordersArray) {

          JsonObject orderObject = orderElement.getAsJsonObject();
          gsonOrderList.add(orderObject);
          standbyOrders.add(orderObject.get("id").getAsString() + " - "+ orderObject.get("utente").getAsString());
        }
        refreshOrdersList();
      } catch (IOException e){
        e.printStackTrace();
      }
    }

    @FXML
    void refreshPrevList(){
      prevs.clear();
      for (String prev: standbyPrevs){
        if (prev.contains(MenPrevTextField.getText())){
          prevs.add(prev);
        }
      }
      prevList.setItems(prevs);
    }

    @FXML
    void getPreventivi(ActionEvent event){
      standbyPrevs.clear();
      MenPrev.setVisible(true);
      gsonPrevsList.clear();
      String file = "preventivi.json";
      Gson gson = new Gson();

      try (Reader reader = new FileReader(file)){
        JsonArray preventiviArray = gson.fromJson(reader, JsonArray.class);

        for (JsonElement prevElement : preventiviArray) {

          JsonObject prevObject = prevElement.getAsJsonObject();
          gsonPrevsList.add(prevObject);
          standbyPrevs.add(prevObject.get("utente").getAsString());
        }
        refreshPrevList();
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
  private void loadOrdini(OrderClass ord) throws IOException { 
    Stage stage = (Stage) Ordini.getScene().getWindow();
    stage.close();
    FXMLLoader fxmlLoader = new FXMLLoader(Ordine.class.getResource("/FXML/Ordine.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 320, 240);
    OrdineController controller = fxmlLoader.getController();
    controller.initialize(ord);
    stage.setTitle("Concessionario - Ordine");
    stage.setMinWidth(1024);
    stage.setMinHeight(768);
    stage.setMaxWidth(1024);
    stage.setMaxHeight(768);
    stage.setScene(scene);
    stage.show();
  }
}
