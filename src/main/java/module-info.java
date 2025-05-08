module ulrichstahl.aufgabenverwaltung {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens StahlU.Aufgabenverwaltung to javafx.fxml, com.google.gson;
    exports StahlU.Aufgabenverwaltung;
}