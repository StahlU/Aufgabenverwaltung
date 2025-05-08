package StahlU.Aufgabenverwaltung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("progress tracker.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 645, 400);

        stage.setTitle("Mitarbeiter Aufgabenverwaltung!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();



    }
}