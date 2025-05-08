package StahlU.Aufgabenverwaltung;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class Aufgabe {

    private String beschreibung;

    private boolean erledigt;
    private transient SimpleStringProperty beschreibungProperty;
    private transient SimpleBooleanProperty erledigtProperty;

    public Aufgabe(String beschreibung) {
        this.beschreibung = beschreibung;
        this.erledigt = false;
        this.beschreibungProperty = new SimpleStringProperty(beschreibung);
        this.erledigtProperty = new SimpleBooleanProperty(false);
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
        if (erledigtProperty != null) {
            erledigtProperty.set(erledigt);
        }
    }

    public SimpleBooleanProperty erledigtProperty() {
        if (erledigtProperty == null) {
            erledigtProperty = new SimpleBooleanProperty(erledigt);
        }
        return erledigtProperty;
    }
}