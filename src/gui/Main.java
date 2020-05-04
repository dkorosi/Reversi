package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    Stage window;

    @Override
    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
        window.setTitle("Reversi");
        window.setResizable(false);
        window.setScene(new Scene(root, 654, 460));
        window.show();
        window.getIcons().add(new Image("gui/reversi_img.png"));

    }


    public static void main(String[] args) {
        launch(args);
    }
}
