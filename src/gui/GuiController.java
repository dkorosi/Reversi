package gui;

import gamelogic.GameLoop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ToggleGroup;

public class GuiController {

    @FXML
    private ToggleGroup startingColorTG;

    @FXML
    private ToggleGroup difficultyTG;

    Canvas canvas;

    public ToggleGroup getStartingColorTG() {

        return startingColorTG;
    }

    public ToggleGroup getDifficultyTG() {
        return difficultyTG;
    }
    @FXML
    void startGame(ActionEvent event) {
        GameLoop game = new GameLoop(canvas);
    }

}
