module org.uni.progetto {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jimObjModelImporterJFX;
    requires java.compiler;
    requires java.desktop;
    requires javafx.swing;


    opens org.uni.progetto.homepage to javafx.fxml,com.google.gson;

    exports org.uni.progetto.homepage;

}