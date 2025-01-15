package org.uni.progetto.homepage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import javafx.scene.paint.PhongMaterial;
import com.google.gson.*;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.application.Application.launch;

public class ConfiguratoreController{
    private Group Gmodel;
    private double anchorY;
    //private double anchorX;
    //private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    //private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private DoubleProperty angleY = new SimpleDoubleProperty(0);
    @FXML
    private AnchorPane auto;
    @FXML
    private Button butt_men;
    @FXML
    private Button butt_men_back;
    @FXML
    private VBox slider_men;
    @FXML
    private ChoiceBox<String> shortcut;
    @FXML
    private ColorPicker color_picker;
    @FXML
    private ChoiceBox<String> choice;
    @FXML
    private CheckBox tele_pos;
    @FXML
    private CheckBox fin_oscu;
    @FXML
    private CheckBox adas;
    @FXML
    private CheckBox sed_ris;
    @FXML
    private Label price;
    @FXML
    private Label User;
    @FXML
    private Button save;
    @FXML
    private Button send;

    private String[] type = {"Base", "Full Optional"};


    private String[] Motore_batteria = {"Base", "Massimo"};
    private double Base_price = 80000;
    private Integer[] PezCol = {0,1,2,3,4,5,6,7,8,9,10,11};
    private Integer[] PezFin ={13,14,16};
    private Boolean isLooggedIn;

    ObjModelImporter importer = new ObjModelImporter();
    private final ArrayList<JsonObject> gsonMacchine = new ArrayList<JsonObject>();





    public ConfiguratoreController() throws FileNotFoundException {
    }

    private void add3DModel(SubScene model) {
    Platform.runLater(() -> {
        auto.getChildren().add(model);
    });
    }

    public void initMacchina()throws IOException {
        SubScene subScene;
        String file = "configuratore.json";
        Gson gson = new Gson();
        UserSession userSession = UserSession.getInstance();
        if (userSession != null) {
            // Se esiste, l'utente è loggato
            // Mostra i dati dell'utente
            User.setText(UserSession.getInstance().getUsername());
            isLooggedIn = true;

        } else {
            // Se non esiste, l'utente non è loggato
            // Mostra lo slider di login
            User.setText("Guest");
            isLooggedIn = false;
        }

        try(Reader reader = new FileReader(file)){
            JsonArray ordersArray = gson.fromJson(reader, JsonArray.class);
            for(JsonElement orderElement : ordersArray){
                JsonObject orderObject = orderElement.getAsJsonObject();
                gsonMacchine.add(orderObject);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            subScene = createGroup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        add3DModel(subScene);
        slider_men.setTranslateX(-200); //impostiamo la posizione iniziale dello slider a -200 cosi da renderla invisibile appena parte l'applicazione
        butt_men_back.setVisible(false); //rendiamo invisibile il tasto menuback cosi che appena avviamo il codice non sia possibile cliccarlo
        menu_slider();
        choice.getItems().addAll(Motore_batteria);
        shortcut.getItems().addAll(type);
        shortcut.setValue(type[0]);
        choice.setValue(Motore_batteria[0]);
        price.setText(Base_price + " € ");

        shortcut.setOnAction(e -> {
            if (Objects.equals(shortcut.getValue(), type[0])) {
                if (tele_pos.isSelected())removePrice(Optionals.TEL_POS.getOptionals());
                tele_pos.setSelected(false);
                if (fin_oscu.isSelected())removePrice(Optionals.MIRRORS.getOptionals());
                fin_oscu.setSelected(false);
                setFinLight();
                if(adas.isSelected())removePrice(Optionals.ADAS.getOptionals());
                adas.setSelected(false);
                if(sed_ris.isSelected())removePrice(Optionals.HEATS_SEATS.getOptionals());
                sed_ris.setSelected(false);
                choice.setValue(Motore_batteria[0]);

            }
            else if (Objects.equals(shortcut.getValue(), type[1])) {
                if(!tele_pos.isSelected())addPrice(Optionals.TEL_POS.getOptionals());
                tele_pos.setSelected(true);
                if(!fin_oscu.isSelected())addPrice(Optionals.MIRRORS.getOptionals());
                fin_oscu.setSelected(true);
                setFinOscu();
                if(!adas.isSelected())addPrice(Optionals.ADAS.getOptionals());
                adas.setSelected(true);
                if(!sed_ris.isSelected())addPrice(Optionals.HEATS_SEATS.getOptionals());
                sed_ris.setSelected(true);
                choice.setValue(Motore_batteria[1]);
            }
        });
        choice.setOnAction(e ->{
            if(choice.getValue().equals(Motore_batteria[0])){
                if(!tele_pos.isSelected() && !fin_oscu.isSelected() && !adas.isSelected() && !sed_ris.isSelected())shortcut.setValue(type[0]);
                else if (tele_pos.isSelected() && fin_oscu.isSelected() && adas.isSelected() && sed_ris.isSelected())shortcut.setValue("Custom");
                removePrice(Optionals.ENGINE.getOptionals());
            }
            else{
                if(tele_pos.isSelected() && fin_oscu.isSelected() && adas.isSelected() && sed_ris.isSelected())shortcut.setValue(type[1]);
                else if (!tele_pos.isSelected() && !fin_oscu.isSelected() && !adas.isSelected() && !sed_ris.isSelected())shortcut.setValue("Custom");
                addPrice(Optionals.ENGINE.getOptionals());
            }
        });

        tele_pos.setOnAction(e -> {
            if (tele_pos.isSelected()) {
                addPrice(Optionals.TEL_POS.getOptionals());
                if (fin_oscu.isSelected() && adas.isSelected() && sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[1])) {
                    shortcut.setValue(type[1]);
                }
                else {
                    shortcut.setValue("Custom");
                }
            }
            else {
                removePrice(Optionals.TEL_POS.getOptionals());
                if(!fin_oscu.isSelected() && !adas.isSelected() && !sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[0])) {
                    shortcut.setValue(type[0]);
                }
                else {
                    shortcut.setValue("Custom");
                }
            }

        });
        fin_oscu.setOnAction(e -> {
            if (fin_oscu.isSelected()) {
                setFinOscu();
                addPrice(Optionals.MIRRORS.getOptionals());
                if (tele_pos.isSelected() && adas.isSelected() && sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[1])) {
                    shortcut.setValue(type[1]);
                }
                else {
                    shortcut.setValue("Custom");
                }
            }
            else {
                removePrice(Optionals.MIRRORS.getOptionals());
                setFinLight();
                if(!tele_pos.isSelected() && !adas.isSelected() && !sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[0])) {
                    shortcut.setValue(type[0]);
                }
                else {
                    shortcut.setValue("Custom");
                }
            }

        });
        adas.setOnAction(e -> {
            if (adas.isSelected()) {
                addPrice(Optionals.ADAS.getOptionals());
                if (tele_pos.isSelected() && fin_oscu.isSelected() && sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[1])) {
                    shortcut.setValue(type[1]);
                }
                else {
                    shortcut.setValue("Custom");
                }
            }
            else {
                removePrice(Optionals.ADAS.getOptionals());
                if(!tele_pos.isSelected() && !fin_oscu.isSelected() && !sed_ris.isSelected() && choice.getValue().equals(Motore_batteria[0])) {
                    shortcut.setValue(type[0]);
                }
                else {
                    shortcut.setValue("Custom");
                }
            }
        });
        sed_ris.setOnAction(e -> {

            if (sed_ris.isSelected()) {
                addPrice(Optionals.HEATS_SEATS.getOptionals());
                if (tele_pos.isSelected() && fin_oscu.isSelected() && adas.isSelected() && choice.getValue().equals(Motore_batteria[1])) {
                    shortcut.setValue(type[1]);
                }
                else {
                    shortcut.setValue("Custom");
                }
            }
            else {
                removePrice(Optionals.HEATS_SEATS.getOptionals());
                if(!tele_pos.isSelected() && !fin_oscu.isSelected() && !adas.isSelected() && choice.getValue().equals(Motore_batteria[0])) {
                    shortcut.setValue(type[0]);
                }
                else {
                    shortcut.setValue("Custom");
                }
            }
        });
        color_picker.setOnAction(e -> {
            Color newColor = color_picker.getValue();
            reload3DModel(newColor);
        });
    }

    private void reload3DModel(Color newColor) {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(newColor);
        material.setSpecularColor(newColor);
        /*((MeshView) Gmodel.getChildren().get(var)).setMaterial(material);
        System.out.println(var);
        var++;

         */
        for (int a : PezCol){
            ((MeshView) Gmodel.getChildren().get(a)).setMaterial(material);
            }

        }


    private void setFinOscu() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.BLACK);
        material.setSpecularColor(Color.BLACK);
        for(int a : PezFin){
            ((MeshView) Gmodel.getChildren().get(a)).setMaterial(material);
        }

    }
    private void setFinLight() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.WHITE);
        material.setSpecularColor(Color.WHITE);
        for(int a : PezFin){
            ((MeshView) Gmodel.getChildren().get(a)).setMaterial(material);
        }
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

    private void addPrice(int optional) {
        String text = price.getText().replace("€", "").trim();
        double currentPrice = Double.parseDouble(text);
        price.setText((currentPrice + optional) + " €");
    }
    private void removePrice(int optional) {
        String text = price.getText().replace("€", "").trim();
        double currentPrice = Double.parseDouble(text);
        price.setText((currentPrice - optional) + " €");
    }

    private SubScene createGroup() throws IOException {
        Group modelRoot;
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-8);
        camera.setTranslateX(3.5);
        camera.setTranslateY(3);

        modelRoot = loadModel(getClass().getResource("/configuratore/Porsche_918_Spyder_2016.obj"));
        modelRoot.setTranslateX(3);
        modelRoot.setTranslateY(4);


        Transform t = new Rotate(30, new Point3D(0,1,0));
        modelRoot.getTransforms().add(t);
        SubScene subScene = new SubScene(modelRoot, 1024, 630, true, null);
        subScene.setCamera(camera);
        initMouseControl(subScene, modelRoot);
        return subScene;
    }

    private Group loadModel(URL url) throws IOException {
        Gmodel = new Group();
        importer = new ObjModelImporter();
        importer.read(url);
        for (MeshView view : importer.getImport()){
            Gmodel.getChildren().add(view);
        }
        return Gmodel;
    }


    private void initMouseControl(SubScene subScene, Group modelRoot){
        //Rotate xRotate;
        Rotate yRotate;
        modelRoot.getTransforms().addAll(
                //xRotate = new Rotate(0, Rotate.X_AXIS)
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        //xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        subScene.setOnMousePressed(event -> {
            //anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            //anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        subScene.setOnMouseDragged(event -> {
            //angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY - event.getSceneX());
        });
        // Rotate the model automatically
        /*
        RotateTransition rt = new RotateTransition(Duration.seconds(30), modelRoot);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setCycleCount(RotateTransition.INDEFINITE);
        rt.play();
         */
    }
    /*
class SmartGroup extends Group{
    Rotate r;
    Transform t = new Rotate();

    void rotateByX(int angle){
        r = new Rotate(angle, Rotate.X_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }
    void rotateByY(int angle){
        r = new Rotate(angle, Rotate.Y_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }
}
*/

}   // End of class ConfiguratoreController
