package gui;

import gamelogic.GameLoop;
import javafx.scene.canvas.Canvas;

public class GuiController {

    Canvas canvas;

    private void startGame() {
        GameLoop game = new GameLoop(canvas);
    }
}
