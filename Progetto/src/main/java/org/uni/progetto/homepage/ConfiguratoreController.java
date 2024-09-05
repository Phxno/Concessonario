package org.uni.progetto.homepage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class ConfiguratoreController {
    @FXML
    private AnchorPane auto;


    public void add3DModel(SubScene model) {
    Platform.runLater(() -> {
        auto.getChildren().add(model);
    });

}
}   // End of class Controller
