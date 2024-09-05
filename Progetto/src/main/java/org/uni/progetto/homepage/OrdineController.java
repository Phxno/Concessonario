package org.uni.progetto.homepage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OrdineController {

    @FXML
    private Button backButton;

    @FXML
    private Label clientName;

    @FXML
    private Button confirmButton;

    @FXML
    private Button contactButton;

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
    void back(ActionEvent event) throws IOException {
      loadDipendente();
    }

    @FXML
    void confirm(ActionEvent event) throws IOException {
      loadDipendente();
    }

    @FXML
    void contact(ActionEvent event) {

    }
    private void loadDipendente() throws IOException{
      Stage stage = (Stage) confirmButton.getScene().getWindow();
      stage.close();
      FXMLLoader fxmlLoader = new FXMLLoader(Dipendente.class.getResource("/FXML/Dipendente.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
      DipendenteController controller = fxmlLoader.getController();
      controller.initialize("Matteo Bertaiola");
      stage.setTitle("Concessionario - Dipendente");
      stage.setScene(scene);
      stage.show();
    }
}
