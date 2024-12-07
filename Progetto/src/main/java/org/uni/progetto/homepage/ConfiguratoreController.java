package org.uni.progetto.homepage;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.skin.CellSkinBase;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javafx.application.Application.launch;

public class ConfiguratoreController {
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
    private CheckBox tetto_vetro;
    @FXML
    private CheckBox sed_ris;
    @FXML
    private Label price;
    @FXML
    private Button save;
    @FXML
    private Button send;


    private String[] type = {"Base", "Custom", "Full Optional"};
    private String[] Motore_batteria = {"Base", "Massimo"};

    private Integer[] PezCol = {0,1,2,3,4,5,6,7,8,9,10,11};
    private Integer[] PezFin ={13,14,16};
    ObjModelImporter importer = new ObjModelImporter();



    private void add3DModel(SubScene model) {
    Platform.runLater(() -> {
        auto.getChildren().add(model);
    });

    }

    public void initMacchina()throws IOException {
        SubScene subScene;
        Optionals optttt = new Optionals();
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
        shortcut.setOnAction(e -> {
            if (Objects.equals(shortcut.getValue(), type[0])) {
                choice.setValue(Motore_batteria[0]);
                optttt.setEngine(0);
                tele_pos.setSelected(false);
                optttt.setTel_pos(0);
                fin_oscu.setSelected(false);
                optttt.setMirror(0);
                setFinLight();
                tetto_vetro.setSelected(false);
                optttt.setRoofGlass(0);
                sed_ris.setSelected(false);
                optttt.setHeat_seats(0);
            }
            else if (Objects.equals(shortcut.getValue(), type[2])) {
                choice.setValue(Motore_batteria[1]);
                optttt.setEngine(1);
                tele_pos.setSelected(true);
                optttt.setTel_pos(1);
                fin_oscu.setSelected(true);
                optttt.setMirror(1);
                setFinOscu();
                tetto_vetro.setSelected(true);
                optttt.setRoofGlass(1);
                sed_ris.setSelected(true);
                optttt.setHeat_seats(1);
            }
        });
        choice.setOnAction(e ->{
            if(Objects.equals(choice.getValue(), Motore_batteria[0])){
                optttt.setEngine(0);
                if(!fin_oscu.isSelected() && !tetto_vetro.isSelected() && !sed_ris.isSelected() && !tele_pos.isSelected()){
                    shortcut.setValue(type[0]);
                }
                else {
                    shortcut.setValue(type[1]);
                }
            }
            else{
                if(fin_oscu.isSelected() && tetto_vetro.isSelected() && sed_ris.isSelected() && tele_pos.isSelected()){
                    shortcut.setValue(type[2]);
                }
                else {
                    shortcut.setValue(type[1]);
                }
            }
        });
        tele_pos.setOnAction(e -> {
            if (tele_pos.isSelected()) {
                optttt.setTel_pos(1);
                if (fin_oscu.isSelected() && tetto_vetro.isSelected() && sed_ris.isSelected() && Objects.equals(choice.getValue(), Motore_batteria[2])) {
                    shortcut.setValue(type[2]);
                }
                else {
                    shortcut.setValue(type[1]);
                }
            }
            else {
                if(!fin_oscu.isSelected() && !tetto_vetro.isSelected() && !sed_ris.isSelected() && Objects.equals(choice.getValue(), Motore_batteria[0])) {
                    shortcut.setValue(type[0]);
                }
                else {
                    shortcut.setValue(type[1]);
                }
            }
        });
        fin_oscu.setOnAction(e -> {
            ;
            if (fin_oscu.isSelected()) {
                setFinOscu();
                if (tele_pos.isSelected() && tetto_vetro.isSelected() && sed_ris.isSelected() && Objects.equals(choice.getValue(), Motore_batteria[2])) {
                    shortcut.setValue(type[2]);
                }
                else {
                    shortcut.setValue(type[1]);
                }
            }
            else {
                setFinLight();
                if(!tele_pos.isSelected() && !tetto_vetro.isSelected() && !sed_ris.isSelected() && Objects.equals(choice.getValue(), Motore_batteria[0])) {
                    shortcut.setValue(type[0]);
                }
                else {
                    shortcut.setValue(type[1]);
                }
            }
        });
        tetto_vetro.setOnAction(e -> {
            if (tetto_vetro.isSelected()) {

                if (tele_pos.isSelected() && fin_oscu.isSelected() && sed_ris.isSelected() && Objects.equals(choice.getValue(), Motore_batteria[2])) {
                    shortcut.setValue(type[2]);
                }
                else {
                    shortcut.setValue(type[1]);
                }
            }
            else {
                if(!tele_pos.isSelected() && !fin_oscu.isSelected() && !sed_ris.isSelected() && Objects.equals(choice.getValue(), Motore_batteria[0])) {
                    shortcut.setValue(type[0]);
                }
                else {
                    shortcut.setValue(type[1]);
                }
            }
        });
        sed_ris.setOnAction(e -> {
            if (sed_ris.isSelected()) {
                if (tele_pos.isSelected() && fin_oscu.isSelected() && tetto_vetro.isSelected() && Objects.equals(choice.getValue(), Motore_batteria[2])) {
                    shortcut.setValue(type[2]);
                }
                else {
                    shortcut.setValue(type[1]);
                }
            }
            else {
                if(!tele_pos.isSelected() && !fin_oscu.isSelected() && !tetto_vetro.isSelected() && Objects.equals(choice.getValue(), Motore_batteria[0])) {
                    shortcut.setValue(type[0]);
                }
                else {
                    shortcut.setValue(type[1]);
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
        private void addPrice(double priceToAdd) {
            String text = price.getText().replace("€", "").trim();
            double currentPrice = Double.parseDouble(text);
            price.setText((currentPrice + priceToAdd) + " €");
        }

    private SubScene createGroup() throws IOException {
        Group modelRoot;
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-8);
        camera.setTranslateX(3.5);
        camera.setTranslateY(3);

        modelRoot = loadModel(getClass().getResource("/configuratore/Tesla Model.obj"));
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
    // Rinomina il file .obj per forzare il rilevamento delle modifiche

    importer = new ObjModelImporter();
    importer.read(url);

    for (MeshView view : importer.getImport()){
        Gmodel.getChildren().add(view);
    }
    System.out.println(Gmodel.getChildren());
    // Rinomina il file .obj al suo nome originale


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
