module StahlU.aufgabenverwaltung {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens StahlU.aufgabenverwaltung to javafx.fxml, com.google.gson;
    exports StahlU.aufgabenverwaltung;
}