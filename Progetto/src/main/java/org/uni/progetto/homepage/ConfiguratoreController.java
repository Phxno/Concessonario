package org.uni.progetto.homepage;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ConfiguratoreController {
    @FXML
    private AnchorPane auto;

    @FXML
    private Button butt_men;

    @FXML
    private Button butt_men_back;

    @FXML
    private VBox slider_men;



    public void add3DModel(SubScene model) {
    Platform.runLater(() -> {
        auto.getChildren().add(model);
    });

    }
    public void initialize() {
        slider_men.setTranslateX(-200); //impostiamo la posizione iniziale dello slider a -200 cosi da renderla invisibile appena parte l'applicazione
        butt_men_back.setVisible(false); //rendiamo invisibile il tasto menuback cosi che appena avviamo il codice non sia possibile cliccarlo
        menu_slider();
    }
        public void menu_slider() {
            BoxBlur blur = new BoxBlur(6, 6, 3); // Crea un'istanza di BoxBlur
            butt_men.setOnMouseClicked(event -> slideMenuTo(0, false, true, blur)); //gestione degli eventi: quando clicchiamo il tasto menu spostiamo lo slider a 0 e rendiamo visibile il tasto menuback
            butt_men_back.setOnMouseClicked(event -> slideMenuTo(-200, true, false, null)); //gestione degli eventi: quando clicchiamo il tasto menuback spostiamo lo slider a -200 e rendiamo visibile il tasto menu
        }

        private void slideMenuTo(int x, boolean menuVisible, boolean menubackVisible, BoxBlur blurEffect) {
            TranslateTransition slideMenu = new TranslateTransition(); //creiamo un'istanza di TranslateTransition
            slideMenu.setDuration(Duration.seconds(0.4)); //tempo di transizione
            slideMenu.setNode(slider_men); //impostiamo il nodo su cui effettuare la transizione

            slideMenu.setToX(x); //impostiamo la posizione finale dello slider
            slideMenu.play(); //avviamo la transizione

            slideMenu.setOnFinished((ActionEvent e) -> {
                auto.setEffect(blurEffect); // Imposta l'effetto di sfocatura sulla pagina principale
                auto.setDisable(blurEffect != null); // Disabilita car_home se l'effetto di sfocatura è applicato
                butt_men.setVisible(menuVisible);
                butt_men_back.setVisible(menubackVisible);
            });
        }
}   // End of class ConfiguratoreController
