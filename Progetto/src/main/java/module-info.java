module org.uni.progetto {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jimObjModelImporterJFX;
    requires java.compiler;


    opens org.uni.progetto.homepage to javafx.fxml;
    exports org.uni.progetto.homepage;
}