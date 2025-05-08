package StahlU.aufgabenverwaltung;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;


public class Mitarbeiter {

    private String name;

    private ObservableList<Aufgabe> aufgaben;
    private transient SimpleStringProperty nameProperty;
    private transient SimpleDoubleProperty fortschrittProperty;

    public Mitarbeiter(String name) {
        this.name = name;
        this.aufgaben = FXCollections.observableArrayList();
        this.nameProperty = new SimpleStringProperty(name);
        this.fortschrittProperty = new SimpleDoubleProperty(0.0);
    }

    public String getName() {
        return name;
    }

    public SimpleStringProperty nameProperty() {
        if (nameProperty == null) {
            nameProperty = new SimpleStringProperty(name);
        }
        return nameProperty;
    }

    public ObservableList<Aufgabe> getAufgaben() {
        return aufgaben;
    }

    public double getFortschritt() {
        if (fortschrittProperty == null) {
            fortschrittProperty = new SimpleDoubleProperty(0.0);
        }
        return fortschrittProperty.get();
    }

    public SimpleDoubleProperty fortschrittProperty() {
        if (fortschrittProperty == null) {
            fortschrittProperty = new SimpleDoubleProperty(0.0);
        }
        return fortschrittProperty;
    }

    public void addAufgabe(String beschreibung) {
        aufgaben.add(new Aufgabe(beschreibung));
        updateFortschritt();
    }

    public void removeAufgabe(Aufgabe aufgabe) {
        aufgaben.remove(aufgabe);
        updateFortschritt();
    }

    public void aufgabeErledigt(Aufgabe aufgabe, boolean erledigt) {
        aufgabe.setErledigt(erledigt);
        updateFortschritt();
    }



    private void updateFortschritt() {
        if (aufgaben.isEmpty()) {
            if (fortschrittProperty != null) {
                fortschrittProperty.set(0);
            }
            return;
        }

        long erledigteAufgaben = aufgaben.stream()
                .filter(Aufgabe::isErledigt)
                .count();

        if (fortschrittProperty != null) {
            fortschrittProperty.set( (double) erledigteAufgaben / aufgaben.size());
        }
    }
    public Mitarbeiter toSerializableMitarbeiter() {
        Mitarbeiter serializableMitarbeiter = new Mitarbeiter(this.name);
        serializableMitarbeiter.aufgaben = FXCollections.observableArrayList(new ArrayList<>(this.aufgaben)); // ObservableList korrekt erstellen
        return serializableMitarbeiter;
    }

}