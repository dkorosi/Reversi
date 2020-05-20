package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/gui.fxml"));
        window.setTitle("Reversi");
        window.setResizable(false);
        window.setScene(new Scene(root));
        window.show();
        window.getIcons().add(new Image("/reversi_img.png"));

    }


    public static void main(String[] args) {
        launch(args);
    }
}
