module com.example.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.google.gson;

    opens com.example.weatherapp to javafx.fxml, com.google.gson;
    exports com.example.weatherapp;
    exports com.example.weatherapp.dataClasses;
    opens com.example.weatherapp.dataClasses to com.google.gson, javafx.fxml;
}