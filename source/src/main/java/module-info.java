module com.example.source {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;

    opens com.example.source to javafx.fxml;
    exports com.example.source;
}