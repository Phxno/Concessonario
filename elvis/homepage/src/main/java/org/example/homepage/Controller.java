package org.example.homepage;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller  {
    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private AnchorPane slider;

    public void initialize(){
        slider.setTranslateX(-200); //impostiamo la posizione iniziale dello slider a -200 cosi da renderla invisibile appena parte l'applicazione
        menu_slider();
    }




    public void menu_slider(){
        menu.setOnMouseClicked(event -> slideMenuTo(0, false, true)); //gestione degli eventi: quando clicchiamo il tasto menu spostiamo lo slider a 0 e rendiamo visibile il tasto menuback
        menuback.setOnMouseClicked(event -> slideMenuTo(-200, true, false)); //gestione degli eventi: quando clicchiamo il tasto menuback spostiamo lo slider a -200 e rendiamo visibile il tasto menu
    }

    private void slideMenuTo(int x, boolean menuVisible, boolean menubackVisible) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(slider);

        slide.setToX(x);
        slide.play();

        slide.setOnFinished((ActionEvent e)-> {
            menu.setVisible(menuVisible);
            menuback.setVisible(menubackVisible);
        });
    }
}