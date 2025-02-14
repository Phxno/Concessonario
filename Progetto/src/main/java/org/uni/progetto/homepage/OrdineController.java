package org.uni.progetto.homepage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.google.gson.*;
import java.time.LocalDate;

public class OrdineController {
    
    @FXML
    private AnchorPane info;

    @FXML
    private Button backButton;

    @FXML
    private Label clientName;

    @FXML
    private Button confirmButton;

    @FXML
    private Button contactButton;

    @FXML
    private Button closeButton;

    @FXML
    private DatePicker dataShipping;

    @FXML
    private Label orderNumber;

    @FXML
    private Label price;

    @FXML
    private Label sale;

    @FXML
    private Label shopName;

    @FXML
    private Label emailText;

    @FXML
    private Label cellText;

    private OrderClass order;
    private String dip;
    
    public void initialize(OrderClass ord, String dip){
      order = ord;
      clientName.setText(order.getUtente().getName());
      orderNumber.setText(order.getId());
      price.setText(order.getPrezzo());
      sale.setText(order.getSconto() + "%");
      shopName.setText(order.getNegozioConsegna());
      LocalDate data = LocalDate.parse(order.getDataConsegna());
      dataShipping.setValue(data);
      this.dip = dip;
    }

    @FXML
    void back(ActionEvent event) throws IOException {
      loadDipendente();
    }

    @FXML
    void confirm(ActionEvent event) throws IOException {
      loadDipendente();
    }

    @FXML
    void contact(ActionEvent event) {
      info.setVisible(true);
      cellText.setText(order.getUtente().getPhone());
      emailText.setText(order.getUtente().getEmail());
    }
    private void loadDipendente() throws IOException{
      Stage stage = (Stage) confirmButton.getScene().getWindow();
      stage.close();
      FXMLLoader fxmlLoader = new FXMLLoader(Homepage.class.getResource("/FXML/Dipendente.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
      DipendenteController controller = fxmlLoader.getController();
      controller.initialize(dip, 0);
      stage.setTitle("Concessionario - Dipendente");
      stage.setScene(scene);
      stage.show();
    }
    @FXML
    void close(ActionEvent event) {
      info.setVisible(false);
    }
}
