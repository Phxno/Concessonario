package org.example.homepage;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.effect.BoxBlur;
import com.google.gson.Gson;

public class HomepageController {

    @FXML
    private AnchorPane car_home;

    @FXML
    private Button menu;

    @FXML
    private Button menuback;

    @FXML
    private AnchorPane slider_menu;


    @FXML
    private AnchorPane slider_login;


    @FXML
    private Button login;

    @FXML
    private Button loginback;

    @FXML
    private Button login_button;


    @FXML
    private TextField username;


    @FXML
    private PasswordField password;


    public void initialize(){
        slider_menu.setTranslateX(-200); //impostiamo la posizione iniziale dello slider a -200 cosi da renderla invisibile appena parte l'applicazione
        slider_login.setTranslateX(200);
        menuback.setVisible(false); //rendiamo invisibile il tasto menuback cosi che appena avviamo il codice non sia possibile cliccarlo
        loginback.setVisible(false);
        menu_slider();  //chiamiamo la funzione menu_slider
        login_slider(); //chiamiamo la funzione login_slider

    }




    public void menu_slider(){
        BoxBlur blur = new BoxBlur(6, 6, 3); // Crea un'istanza di BoxBlur
        menu.setOnMouseClicked(event -> slideMenuTo(0, false, true,blur)); //gestione degli eventi: quando clicchiamo il tasto menu spostiamo lo slider a 0 e rendiamo visibile il tasto menuback
        menuback.setOnMouseClicked(event -> slideMenuTo(-200, true, false,null)); //gestione degli eventi: quando clicchiamo il tasto menuback spostiamo lo slider a -200 e rendiamo visibile il tasto menu
    }

    private void slideMenuTo(int x, boolean menuVisible, boolean menubackVisible, BoxBlur blurEffect){
        TranslateTransition slideMenu = new TranslateTransition(); //creiamo un'istanza di TranslateTransition
        slideMenu.setDuration(Duration.seconds(0.4)); //tempo di transizione
        slideMenu.setNode(slider_menu); //impostiamo il nodo su cui effettuare la transizione

        slideMenu.setToX(x); //impostiamo la posizione finale dello slider
        slideMenu.play(); //avviamo la transizione

        slideMenu.setOnFinished((ActionEvent e)-> {
            car_home.setEffect(blurEffect); // Imposta l'effetto di sfocatura sulla pagina principale
            car_home.setDisable(blurEffect != null); // Disabilita car_home se l'effetto di sfocatura è applicato
            menu.setVisible(menuVisible);
            menuback.setVisible(menubackVisible);
        });
    }




    public void login_slider(){
        BoxBlur blur = new BoxBlur(6, 6, 3); // Crea un'istanza di BoxBlur
        login.setOnMouseClicked(event -> slideLoginTo(0, false, true,blur)); //gestione degli eventi: quando clicchiamo il tasto menu spostiamo lo slider a 0 e rendiamo visibile il tasto menuback
        loginback.setOnMouseClicked(event -> slideLoginTo(200, true, false,null)); //gestione degli eventi: quando clicchiamo il tasto menuback spostiamo lo slider a -200 e rendiamo visibile il tasto menu
    }

    private void slideLoginTo(int x, boolean menuVisible, boolean menubackVisible, BoxBlur blurEffect){
        TranslateTransition slideLogin = new TranslateTransition(); //creiamo un'istanza di TranslateTransition
        slideLogin.setDuration(Duration.seconds(0.4)); //tempo di transizione
        slideLogin.setNode(slider_login); //impostiamo il nodo su cui effettuare la transizione

        slideLogin.setToX(x); //impostiamo la posizione finale dello slider
        slideLogin.play(); //avviamo la transizione

        slideLogin.setOnFinished((ActionEvent e)-> {
            car_home.setEffect(blurEffect); // Imposta l'effetto di sfocatura sulla pagina principale
            car_home.setDisable(blurEffect != null); // Disabilita car_home se l'effetto di sfocatura è applicato
            login.setVisible(menuVisible);
            loginback.setVisible(menubackVisible);
        });
    }


    @FXML
    void login(ActionEvent event) {
        String user = username.getText();
        String pass = password.getText();

        Gson gson = new Gson();
 /*       try (Reader reader = new FileReader("loginInfo.json")) {
            // Convert JSON File to Java Object
            LoginInfo loginInfo = gson.fromJson(reader, LoginInfo.class);

            // Comparing the username and password
            if (loginInfo.getUsername().equals(user) && loginInfo.getPassword().equals(pass)) {
                System.out.println("Login successful");
            } else {
                System.out.println("Invalid username or password");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}

