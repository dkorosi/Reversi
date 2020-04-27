package gui;

import gamelogic.GameLoop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
    Canvas canvas;

    public ToggleGroup getStartingColorTG() {

        return startingColorTG;
    }

    public ToggleGroup getDifficultyTG() {
        return difficultyTG;
    }

    @FXML
    void startSingleGame(ActionEvent event) {
        GameLoop game = new GameLoop(canvas);
        //stage.setScene();
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
    double getTimerSliderVal(MouseEvent event) {
        System.out.println("Slide");
        return timerSlider.getValue();
    }

    @FXML
    String getMultiNameText() {
        return multiNameText.getText();
    }
    @FXML
    String getMultiIPAddrText() {
        return multiIPAddrText.getText();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
