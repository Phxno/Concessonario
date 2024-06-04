module org.uni.progetto {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens org.uni.progetto.homepage to javafx.fxml;
    exports org.uni.progetto.homepage;
}