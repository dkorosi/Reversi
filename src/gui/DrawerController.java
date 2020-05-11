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
    private double singleTimer;
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
        loader.setLocation(getClass().getResource("gui.fxml"));
        Parent root = loader.load();

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        this.gameLoop.stopTimer();
        window.setScene((this.MenuScene));
        window.show();
    }

    void initDrawerControllerSingle(Scene Menu, GameType type, double singleTimer, boolean black, int difficulty) {
        this.MenuScene = Menu;
        this.gameType = type;
        this.singleTimer = singleTimer;

        int min = (int) (this.singleTimer);
        int sec = (int) ((this.singleTimer - min) * 60);
        int whole = 60 * min + sec;

        GameLoop game = new GameLoop(this.canvas, difficulty, black, whole);
        this.gameLoop = game;
        game.getDrawer().setController(this);
        Thread th = new Thread(game);
        th.start();
        nameText.setText(gameLoop.getCurrentPlayer().getName());
        System.out.println("Start Single GAME DC");
    }

    public void refreshTimer() {
        int min = gameLoop.getTimerVal() / 60;
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
        this.timerCountDown.setText(timerString);
    }


    void initDrawerControllerLocal(Scene menu, GameType type, double singleTimer, boolean black) {
        this.MenuScene = menu;
        this.gameType = type;
        this.singleTimer = singleTimer;

        int min = (int) (this.singleTimer);
        int sec = (int) ((this.singleTimer - min) * 60);

        int whole = 60 * min + sec;

        GameLoop game = new GameLoop(this.canvas, black, whole);
        this.gameLoop = game;
        Thread th = new Thread(game);
        game.getDrawer().setController(this);
        th.start();
        nameText.setText(gameLoop.getCurrentPlayer().getName());
        System.out.println("Start Single GAME DC");

    }


    void initDrawerControllerOnline(Scene menu, GameType type, double singleTimer, boolean black, String ip, String name) {
        this.MenuScene = menu;
        this.gameType = type;
        this.singleTimer = singleTimer;

        int min = (int) (this.singleTimer);
        int sec = (int) ((this.singleTimer - min) * 60);
        int whole = 60 * min + sec;

        GameLoop game = new GameLoop(this.canvas, black, ip, name, whole);
        this.gameLoop = game;
        game.getDrawer().setController(this);
        Thread th = new Thread(game);
        th.start();
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
        double xPos = event.getX();
        double yPos = event.getY();
        double rectangleWidth = this.canvas.getWidth() / boardWidth; // getter -el kell lekérdezni a board méretét ( 8-as)
        double rectangleHeight = (this.canvas.getHeight()) / boardHeight - 1;
        int x = (int) (xPos / rectangleWidth);
        int y = (int) (yPos / rectangleHeight);
        Coordinate cor = new Coordinate(x, y);
        gameLoop.move(cor);
        refreshTimer();


        nameText.setText(gameLoop.getCurrentPlayer().getName());
    }


}
