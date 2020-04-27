package gui;

import gamelogic.GameLoop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiController {
    private Stage stage;
    @FXML
    private Slider timerSliderSingle;
    @FXML
    private Slider timerSliderMulti;
    @FXML
    private ToggleGroup startingColorTG;
    @FXML
    private ToggleGroup difficultyTG;
    @FXML
    private TextField multiNameText;

    @FXML
    private TextField multiIPAddrText;
    //second scene
    @FXML
    Canvas canvas;
    @FXML
    private TextField timerCountDown;


    public ToggleGroup getStartingColorTG() {

        return startingColorTG;
    }

    public ToggleGroup getDifficultyTG() {
        return difficultyTG;
    }

    @FXML
    void startSingleGame(ActionEvent event) throws IOException {
        GameLoop game = new GameLoop(canvas);
        changeSceneToCanvas(event);
        System.out.println("Start Single GAME");
    }
    @FXML
    void startLocalGame(ActionEvent event) {
        String Name =   getMultiNameText();
        GameLoop game = new GameLoop(canvas);
        System.out.println("Start Local Game");
        System.out.println("The name is:"+ Name);
    }
    @FXML
    void startOnlineGame(ActionEvent event) {
        String IPAddr = getMultiIPAddrText();
        String Name =   getMultiNameText();
        GameLoop game = new GameLoop(canvas);
        System.out.println("Start Multi Game");
        System.out.println("The name is: "+ Name + "\n" + "The IP Address is: " + IPAddr);
    }
    @FXML
    double getTimerSliderSingleVal(MouseEvent event) {
        System.out.println("Slide");
        return timerSliderSingle.getValue();
    }
    @FXML
    double getTimerSliderMultiVal(MouseEvent event) {
        System.out.println("Slide");
        return timerSliderMulti.getValue();
    }


    @FXML
    String getMultiNameText() {
        return multiNameText.getText();
    }
    @FXML
    String getMultiIPAddrText() {
        return multiIPAddrText.getText();
    }

    public void changeSceneToCanvas(ActionEvent event) throws IOException {
        Parent canvasDrawerParent = FXMLLoader.load(getClass().getResource("canvasDrawer.fxml"));

        Scene canvasDrawerScene = new Scene(canvasDrawerParent, 700, 510);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene((canvasDrawerScene));
        window.show();

    }
    public void changeSceneToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));

        Scene guiScene = new Scene(root, 654, 460);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene((guiScene));
        window.show();
    }
}


