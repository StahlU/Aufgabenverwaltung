package StahlU.Aufgabenverwaltung;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class Controller {
    public TextField neuerMitarbeiterField;
    public TextField searchMitarbeiter;
    public TextField neueAufgabeField;
    public Mitarbeiter selectedMitarbeiter;

    public ListView<Mitarbeiter> mitarbeiterListView;

    public ListView<Aufgabe> aufgabenListView;

    private final ObservableList<Mitarbeiter> mitarbeiterList = FXCollections.observableArrayList();
    private final ObservableList<Mitarbeiter> mitarbeiterSearchList = FXCollections.observableArrayList();
    private final ObservableList<Aufgabe> leereAufgabenListe = FXCollections.observableArrayList();


    public void initialize() {

        mitarbeiterList.setAll(JsonStorage.load());
//        mitarbeiterList.setAll(AufgabenGenerator.generiereListe(1000));
        mitarbeiterListView.setItems(mitarbeiterList);

        Platform.runLater(() -> {
            mitarbeiterListView.getScene().getWindow().setOnCloseRequest(event -> {
                JsonStorage.save(mitarbeiterList);
            });
        });

        mitarbeiterListView.setOnMouseClicked(this::handleMitarbeiterClick);


            mitarbeiterListView.setCellFactory(lv -> new ListCell<Mitarbeiter>() {
                private final HBox hbox = new HBox(10);
                private final Label nameLabel = new Label();
                private final Region spacer = new Region();
                private final ProgressBar progressBar = new ProgressBar();
                private final Image iconEmpty = new Image(getClass().getResourceAsStream("/ulrichstahl/aufgabenverwaltung/icons/icon_empty.png"));
                private final Image iconPartial = new Image(getClass().getResourceAsStream("/ulrichstahl/aufgabenverwaltung/icons/icon_partial.png"));
                private final Image iconDone = new Image(getClass().getResourceAsStream("/ulrichstahl/aufgabenverwaltung/icons/icon_done.png"));

                private final ImageView iconView = new ImageView();

                {
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    progressBar.setPrefWidth(100);
                    iconView.setFitWidth(20);
                    iconView.setFitHeight(20);
                    HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
                    hbox.getChildren().addAll(nameLabel, spacer, progressBar, iconView);
                }

                @Override
                protected void updateItem(Mitarbeiter mitarbeiter, boolean empty) {
                    super.updateItem(mitarbeiter, empty);
                    if (empty || mitarbeiter == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        nameLabel.setText(mitarbeiter.getName());
                        progressBar.progressProperty().unbind();
                        progressBar.progressProperty().bind(mitarbeiter.fortschrittProperty());

                        double fortschritt = mitarbeiter.getFortschritt();
                        if (fortschritt >= 1.0) {
                            iconView.setImage(iconDone);
                        } else if (fortschritt > 0.0) {
                            iconView.setImage(iconPartial);
                        } else {
                            iconView.setImage(iconEmpty);
                        }

                        setGraphic(hbox);
                    }
                }
            });

        aufgabenListView.setCellFactory(lv -> new ListCell<Aufgabe>() {
            @Override
            protected void updateItem(Aufgabe aufgabe, boolean empty) {
                super.updateItem(aufgabe, empty);
                if (empty || aufgabe == null) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.setPrefWidth(aufgabenListView.getWidth() - 20);

                    CheckBox checkBox = new CheckBox(aufgabe.getBeschreibung());
                    checkBox.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(checkBox, javafx.scene.layout.Priority.ALWAYS);

                    Button deleteButton = new Button("Löschen");
                    deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");

                    checkBox.selectedProperty().bindBidirectional(aufgabe.erledigtProperty());
                    checkBox.selectedProperty().addListener((obs, oldVal, status) -> {
                        if (mitarbeiterListView.getSelectionModel().getSelectedItem() != null) {
                            mitarbeiterListView.getSelectionModel().getSelectedItem().aufgabeErledigt(aufgabe, status);
                            mitarbeiterListView.refresh();

                        }
                    });

                    deleteButton.setOnAction(event -> {
                        if (mitarbeiterListView.getSelectionModel().getSelectedItem() != null) {
                            mitarbeiterListView.getSelectionModel().getSelectedItem().removeAufgabe(aufgabe);
                            mitarbeiterListView.refresh();

                        }
                    });

                    hbox.getChildren().addAll(checkBox, deleteButton);
                    setGraphic(hbox);
                }
            }
        });

        neuerMitarbeiterField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addMitarbeiter(new ActionEvent());
                JsonStorage.save(mitarbeiterList);
                mitarbeiterListView.refresh();
            }
        });

        neueAufgabeField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addAufgabe(new ActionEvent());
                JsonStorage.save(mitarbeiterList);
                mitarbeiterListView.refresh();
            }
        });


    }


    private void handleMitarbeiterClick(MouseEvent mouseEvent) {

        selectedMitarbeiter = mitarbeiterListView.getSelectionModel().getSelectedItem();

        if (selectedMitarbeiter != null) {
            aufgabenListView.setItems(selectedMitarbeiter.getAufgaben());
//            System.out.println("Mitarbeiter angeklickt: " + selectedMitarbeiter +": "+selectedMitarbeiter.getArr());

                if (selectedMitarbeiter.getAufgaben() != null) {
                    aufgabenListView.setItems(selectedMitarbeiter.getAufgaben());
                } else {
                    aufgabenListView.setItems(leereAufgabenListe);
                }

        }
    }


    @FXML
    public void search(KeyEvent actionEvent) {

        mitarbeiterSearchList.clear();
        for (Mitarbeiter mitarbeiter : mitarbeiterList) {
            if (mitarbeiter.getName().toLowerCase().contains(searchMitarbeiter.getText().toLowerCase())){
                mitarbeiterSearchList.add(mitarbeiter);

            }
            mitarbeiterListView.setItems(mitarbeiterSearchList);
        }
        if (mitarbeiterSearchList.isEmpty()){
            mitarbeiterListView.setItems(mitarbeiterList);
        }
    }

    @FXML
    public void addMitarbeiter(ActionEvent actionEvent) {
        String name = neuerMitarbeiterField.getText();

        if(!name.isEmpty()){
            mitarbeiterList.add(new Mitarbeiter(name));
            searchMitarbeiter.clear();
            neuerMitarbeiterField.clear();
            if (mitarbeiterListView.getItems() != mitarbeiterList){
                mitarbeiterListView.setItems(mitarbeiterList);
            }
            JsonStorage.save(mitarbeiterList);
            mitarbeiterListView.refresh();


        }
    }

    @FXML
    public void addAufgabe(ActionEvent actionEvent) {

        selectedMitarbeiter = mitarbeiterListView.getSelectionModel().getSelectedItem();
        String aufgabeText = neueAufgabeField.getText();

        if (selectedMitarbeiter != null && !aufgabeText.isEmpty()) {

            selectedMitarbeiter.addAufgabe(aufgabeText);
            neueAufgabeField.clear();
            JsonStorage.save(mitarbeiterList);
            mitarbeiterListView.refresh();
        }

    }

    @FXML
    public void removeMitarbeiter(ActionEvent actionEvent) {
        mitarbeiterList.remove(selectedMitarbeiter);
        aufgabenListView.setItems(leereAufgabenListe);
        JsonStorage.save(mitarbeiterList);
        mitarbeiterListView.refresh();

    }

}