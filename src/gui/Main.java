package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ((GuiController)fxmlLoader.getController()).setStage(window);
        window.setTitle("Reversi");
        window.setResizable(false);
        window.setScene(new Scene(root, 654, 460));
        window.show();

    }




    public static void main(String[] args) {
        launch(args);
    }
}
