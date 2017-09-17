package FxDatenbank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

/**
 * Eine einfache (nur lesende) Darstellung von Tabellen einer Datenbank
 *
 * @author Uwe Krause
 */
public class FxDatenbank extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.setTitle("Datenbank Anschauer v0.4");

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
