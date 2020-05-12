package gui;

import gamelogic.Coordinate;
import gamelogic.GameLoop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class DrawerController {

    private GameType gameType;
    private Scene MenuScene;
    private GameLoop gameLoop;

    @FXML
    private Canvas canvas;

    @FXML
    private Text timerCountDown;

    @FXML
    private Text nameText;

    @FXML
    void changeSceneToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/gui.fxml"));
        Parent root = loader.load();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

//        this.gameLoop.stopTimer();
        window.setScene((this.MenuScene));
        window.show();
    }

    public void refreshTimer() {
        return;
       /* int min = gameLoop.getTimerVal() / 60;
        int sec = gameLoop.getTimerVal() - min * 60;

        String plusSec;
        String plusMin;

        if (sec < 10)
            plusSec = "0";
        else
            plusSec = "";

        if (min < 10)
            plusMin = "0";
        else
            plusMin = "";

        String timerString = plusMin + min + ":" + plusSec + sec;
        this.timerCountDown.setText(timerString);*/
    }


    void initDrawerController(Scene menu, GameOptions options) {
        this.MenuScene = menu;

        GameLoop game = new GameLoop(this.canvas, options);
        this.gameLoop = game;
        Thread gameThread = new Thread(game);
        game.getDrawer().setController(this);
        gameThread.start();
        nameText.setText(gameLoop.getCurrentPlayer().getName());
        System.out.println("Start Single GAME DC");
    }

    Canvas getCanvas() {
        return this.canvas;
    }


    @FXML
    void canvasClicked(MouseEvent event) {
        double boardHeight = this.gameLoop.getBoard().getHeight();
        double boardWidth = this.gameLoop.getBoard().getWidth();

        double rectangleWidth = this.canvas.getWidth() / boardWidth; // getter-rel kell lekérdezni a board méretét ( 8-as)
        double rectangleHeight = (this.canvas.getHeight()) / boardHeight - 1;

        int x = (int) (event.getX() / rectangleWidth);
        int y = (int) (event.getY() / rectangleHeight);
        Coordinate cor = new Coordinate(x, y);
        gameLoop.move(cor);
        refreshTimer();

        nameText.setText(gameLoop.getCurrentPlayer().getName());
    }

}
