package org.uni.progetto.homepage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    private Button okayPrev;

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
    @FXML
    private TextField MenAutoTextField;

    @FXML
    private BorderPane MenAuto;



    private final ObservableList<String> orders = FXCollections.observableArrayList();
    private final ObservableList<String> prevs = FXCollections.observableArrayList();

    private final ObservableList<String> auto = FXCollections.observableArrayList();
    private final ArrayList<String> standbyPrevs = new ArrayList<String>();
    private final ArrayList<String> standbyOrders = new ArrayList<String>();

    private final ArrayList<String> standbyAuto = new ArrayList<String>();
    private final ArrayList<JsonObject> gsonPrevsList = new ArrayList<JsonObject>();
    private final ArrayList<JsonObject> gsonOrderList = new ArrayList<JsonObject>();
    private final ArrayList<JsonObject> gsonAutoList = new ArrayList<JsonObject>();

    private final ObservableList<String> filters = FXCollections.observableArrayList("Data","Cliente","Negozio","Marca");
    @FXML
    private ChoiceBox<String> filterBox;


    private JsonArray preventiviArray;

    @FXML
    private ListView<String> prevList = new ListView<String>(prevs);

    @FXML
    private ListView<String> ordersList = new ListView<String>(orders);

    @FXML
    private ListView<String> autoList = new ListView<String>();


    private int t_user;
    private String dip;

    public void initialize(String dip, int t_user){
      MenPrev.setVisible(false);
      MenOrdini.setVisible(false);
      UData.setText(dip);
      filterBox.getItems().addAll(filters);
      filterBox.setValue(filters.get(0));
      filterBox.setOnAction(e -> {
        if (filterBox.getValue().equals("Data")) sortID();
        else if (filterBox.getValue().equals("Cliente")) sortCliente();
        else if (filterBox.getValue().equals("Negozio")) sortConsegna();
        else if (filterBox.getValue().equals("Marca")) sortMarca();
      });
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
        UserSession userSession = UserSession.getInstance();

        if (userSession != null) userSession.logout();
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
      String prev = prevList.getSelectionModel().getSelectedItem();
      int id = -1;
      if (prev != null){
        PrevClass prevTemp = null;
        MenPrev.setVisible(false);
        String temp = "";
        for (JsonObject prevObject : gsonPrevsList) {
          if (filterBox.getValue().equals("Data")) temp = prevObject.get("id").getAsString() + " - "+ prevObject.get("utente").getAsString();
          else if (filterBox.getValue().equals("Cliente")) temp = prevObject.get("utente").getAsString() + " - id: " + prevObject.get("id").getAsString();
          else if (filterBox.getValue().equals("Negozio")) temp = prevObject.get("negozioConsegna").getAsString() + " - id: " + prevObject.get("id").getAsString();
          else if (filterBox.getValue().equals("Marca")) temp = prevObject.get("macchina").getAsString() + " - id: " + prevObject.get("id").getAsString();
          if (temp.equals(prev)){
            prevTemp = new PrevClass(prevObject);
            break;
          }
        }
        prevs.clear();
        filterBox.setValue(filterBox.getItems().get(0));
        MenPrevTextField.setText("");
        if (prevTemp != null) loadPreventivi(prevTemp);
      } 
    }

    @FXML
    void OkayOrders(ActionEvent event) throws IOException {
      String ord = ordersList.getSelectionModel().getSelectedItem();
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
    void OkayAuto(ActionEvent event) throws IOException {
        String selected = autoList.getSelectionModel().getSelectedItem();
        if (selected != null && !selected.isEmpty()) loadAuto(selected);
    }

    @FXML 
    void Nay(ActionEvent event) throws IOException{
      MenPrev.setVisible(false);
      MenOrdini.setVisible(false);
      MenAuto.setVisible(false);
      filterBox.setValue(filterBox.getItems().get(0));
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
    void refreshAutoList(){
        auto.clear();
        Collections.sort(standbyAuto);
        for (String car: standbyAuto){
            if (car.contains(MenAutoTextField.getText())){
                auto.add(car);
            }
        }
        autoList.setItems(auto);
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
    void showAuto(ActionEvent event) throws IOException{
        standbyAuto.clear();
        MenAuto.setVisible(true);
        gsonAutoList.clear();
        String file = "modelli_auto.json";
        Gson gson = new Gson();

        try (Reader reader = new FileReader(file)){
            JsonArray autoArray = gson.fromJson(reader, JsonArray.class);

            for (JsonElement autoElement : autoArray) {

                JsonObject autoObject = autoElement.getAsJsonObject();
                gsonAutoList.add(autoObject);
                standbyAuto.add(autoObject.get("marca").getAsString() + " - "+ autoObject.get("modello").getAsString());
            }
            refreshAutoList();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void loadOrdini(OrderClass ord) throws IOException { 
      Stage stage = (Stage) Ordini.getScene().getWindow();
      stage.close();
      FXMLLoader fxmlLoader = new FXMLLoader(Homepage.class.getResource("/FXML/Ordine.fxml"));
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
    private void loadAuto(String selected) throws IOException{
        Stage stage = (Stage) autoButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(Homepage.class.getResource("/FXML/ChangeAuto.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        ChangeAutoController controller = fxmlLoader.getController();
        controller.initialize(dip,1,selected);
        stage.setTitle("Concessionario - Modifica Auto");
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.setMaxWidth(1024);
        stage.setMaxHeight(768);
        stage.setScene(scene);
        stage.show();
    }
    private void loadPreventivi(PrevClass prev) throws IOException {
      Stage stage = (Stage) Preventivi.getScene().getWindow();
      stage.close();
      FXMLLoader fxmlLoader = new FXMLLoader(Homepage.class.getResource("/FXML/Preventivo.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 320, 240);
      PreventivoController controller = fxmlLoader.getController();
      controller.initialize(prev, dip, t_user);
      stage.setTitle("Concessionario - Preventivo");
      stage.setMinWidth(1024);
      stage.setMinHeight(768);
      stage.setMaxWidth(1024);
      stage.setMaxHeight(768);
      stage.setScene(scene);
      stage.show();
    }

}
