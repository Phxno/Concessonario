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
import java.util.Collections;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class DipendenteController {

    @FXML
    private BorderPane MenPrev;

    @FXML
    private BorderPane MenOrdini;

    @FXML
    private Button backHomepageButton;

    @FXML
    private Button Ordini;

    @FXML
    private Button Preventivi;

    @FXML
    private Button autoButton;

    @FXML
    private Button NayPrev;

    @FXML
    private Button OkayPrev;

    @FXML
    private Button NayOrders;

    @FXML
    private Button OkayOrders;

    @FXML
    private Label UData;
    
    @FXML
    private TextField MenPrevTextField;

    @FXML
    private TextField MenOrdiniTextField;

    private final ObservableList<String> orders = FXCollections.observableArrayList();
    private final ObservableList<String> prevs = FXCollections.observableArrayList();
    private final ArrayList<String> standbyPrevs = new ArrayList<String>();
    private final ArrayList<String> standbyOrders = new ArrayList<String>();
    private final ArrayList<JsonObject> gsonPrevsList = new ArrayList<JsonObject>();
    private final ArrayList<JsonObject> gsonOrderList = new ArrayList<JsonObject>();

    private final ObservableList<String> filters = FXCollections.observableArrayList("Data","Cliente","Negozio","Marca");
    @FXML
    private ChoiceBox<String> filterBox;


    private JsonArray preventiviArray;

    @FXML
    private ListView<String> prevList = new ListView<String>(prevs);

    @FXML
    private ListView<String> ordersList = new ListView<String>(orders);

    private int t_user;
    private String dip;

    public void initialize(String dip, int t_user){
      MenPrev.setVisible(false);
      MenOrdini.setVisible(false);
      UData.setText(dip);
      filterBox.getItems().addAll(filters);
      filterBox.setValue(filters.get(0));
      this.t_user = t_user;
      this.dip = dip;
      switch (t_user) {
        case 0: // dipendente
          autoButton.setVisible(false);
          break;
        case 1: // segreteria
          Ordini.setVisible(false);
          break;
      }
    }

    @FXML
    void showFilters(ActionEvent event){
      if (filterBox.isShowing()){
        filterBox.hide();
      } else {
        filterBox.show();
      }
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
      String prev = ordersList.getSelectionModel().getSelectedItem();
      System.out.println(prev);
      int id = -1;
      if (prev != null){
        PrevClass prevTemp = null;
        MenPrev.setVisible(false);
        for (JsonObject prevObject : gsonPrevsList) {
          String temp = prevObject.get("id").getAsString() + " - "+ prevObject.get("utente").getAsString();
          if (temp.equals(prev)){
            prevTemp = new PrevClass(prevObject);
            break;
          }
        }
        prevs.clear();
        MenPrevTextField.setText("");
        if (prevTemp != null) loadPreventivi(prevTemp);
      } 
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
      MenOrdini.setVisible(false);
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
      Collections.sort(standbyPrevs);
      for (String prev: standbyPrevs){
        if (prev.contains(MenPrevTextField.getText())){
          prevs.add(prev);
        }
      }
      prevList.setItems(prevs);
    }

    @FXML
    void sortCliente(){
      standbyPrevs.clear();
      for (JsonElement prevElement : preventiviArray) {
        JsonObject prevObject = prevElement.getAsJsonObject();
        gsonPrevsList.add(prevObject);
        standbyPrevs.add(prevObject.get("utente").getAsString() + " - id: " + prevObject.get("id").getAsString());
      }
      refreshPrevList();
    }

    @FXML
    void sortMarca(){
      standbyPrevs.clear();
      for (JsonElement prevElement : preventiviArray) {
        JsonObject prevObject = prevElement.getAsJsonObject();
        gsonPrevsList.add(prevObject);
        standbyPrevs.add(prevObject.get("macchina").getAsString() + " - id: " + prevObject.get("id").getAsString());
      }
      refreshPrevList();
    }

    @FXML
    void sortConsegna(){
      standbyPrevs.clear();
      for (JsonElement prevElement : preventiviArray) {
        JsonObject prevObject = prevElement.getAsJsonObject();
        gsonPrevsList.add(prevObject);
        standbyPrevs.add(prevObject.get("negozioConsegna").getAsString() + " - id: " + prevObject.get("id").getAsString());
      }
      refreshPrevList();
    }

    @FXML
    void sortID(){
      standbyPrevs.clear();
      for (JsonElement prevElement : preventiviArray) {
        JsonObject prevObject = prevElement.getAsJsonObject();
        gsonPrevsList.add(prevObject);
        standbyPrevs.add(prevObject.get("id").getAsString() + " - " + prevObject.get("utente").getAsString());
      }
      refreshPrevList();
    }
    @FXML
    void getPreventivi(ActionEvent event){
      MenPrev.setVisible(true);
      gsonPrevsList.clear();
      String file = "preventivi.json";
      Gson gson = new Gson();

      try (Reader reader = new FileReader(file)){
        preventiviArray = gson.fromJson(reader, JsonArray.class);
        sortID();
      } catch (IOException e){
        e.printStackTrace();
      }
    }

    @FXML
    void showAuto(ActionEvent event) throws IOException{}

    private void loadOrdini(OrderClass ord) throws IOException { 
      Stage stage = (Stage) Ordini.getScene().getWindow();
      stage.close();
      FXMLLoader fxmlLoader = new FXMLLoader(Ordine.class.getResource("/FXML/Ordine.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 320, 240);
      OrdineController controller = fxmlLoader.getController();
      controller.initialize(ord, dip);
      stage.setTitle("Concessionario - Ordine");
      stage.setMinWidth(1024);
      stage.setMinHeight(768);
      stage.setMaxWidth(1024);
      stage.setMaxHeight(768);
      stage.setScene(scene);
      stage.show();
    }
    private void loadPreventivi(PrevClass prev) throws IOException {}
}
