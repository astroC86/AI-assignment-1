package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Hacky approach due to JavaFX runtime components are missing error:
// https://stackoverflow.com/questions/59771324/error-javafx-runtime-components-are-missing-and-are-required-to-run-this-appli
public class Main {

    public static class MainInner extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            Parent root = new FXMLLoader(Main.class.getResource("/main.fxml")).load();

            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 300, 275));
            primaryStage.show();
        }

    }

    public static void main(String[] args) {
        Application.launch(MainInner.class);
    }

}
