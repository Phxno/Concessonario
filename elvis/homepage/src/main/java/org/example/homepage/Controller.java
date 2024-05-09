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
import javafx.scene.effect.BoxBlur;

public class Controller  {

    @FXML
    private AnchorPane car_home;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private AnchorPane slider;

    public void initialize(){
        slider.setTranslateX(-200); //impostiamo la posizione iniziale dello slider a -200 cosi da renderla invisibile appena parte l'applicazione
        menuback.setVisible(false); //rendiamo invisibile il tasto menuback cosi che appena avviamo il codice non sia possibile cliccarlo
        menu_slider();  //chiamiamo la funzione menu_slider
    }




    public void menu_slider(){
        BoxBlur blur = new BoxBlur(6, 6, 3); // Crea un'istanza di BoxBlur
        menu.setOnMouseClicked(event -> slideMenuTo(0, false, true,blur)); //gestione degli eventi: quando clicchiamo il tasto menu spostiamo lo slider a 0 e rendiamo visibile il tasto menuback
        menuback.setOnMouseClicked(event -> slideMenuTo(-200, true, false,null)); //gestione degli eventi: quando clicchiamo il tasto menuback spostiamo lo slider a -200 e rendiamo visibile il tasto menu
    }

    private void slideMenuTo(int x, boolean menuVisible, boolean menubackVisible, BoxBlur blurEffect){
        TranslateTransition slide = new TranslateTransition(); //creiamo un'istanza di TranslateTransition
        slide.setDuration(Duration.seconds(0.4)); //tempo di transizione
        slide.setNode(slider); //impostiamo il nodo su cui effettuare la transizione

        slide.setToX(x); //impostiamo la posizione finale dello slider
        slide.play(); //avviamo la transizione

        slide.setOnFinished((ActionEvent e)-> {
            car_home.setEffect(blurEffect); // Imposta l'effetto di sfocatura sulla pagina principale
            car_home.setDisable(blurEffect != null); // Disabilita car_home se l'effetto di sfocatura è applicato
            menu.setVisible(menuVisible);
            menuback.setVisible(menubackVisible);
        });
    }

}