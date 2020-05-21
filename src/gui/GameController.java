package gui;

import gamelogic.GameLoop;
import gamelogic.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameController {

    private Scene menuScene;
    private Scene gameScene;

    private GameLoop gameLoop;

    @FXML
    private Parent root;

    @FXML
    private Text lightName;

    @FXML
    private Text lightTimer;

    @FXML
    private Text darkTiles;

    @FXML
    private Text lightTiles;

    @FXML
    private Text darkName;

    @FXML
    private Text darkTimer;

    @FXML
    private Rectangle darkRect;

    @FXML
    private Rectangle lightRect;

    @FXML
    private Canvas canvas;

    @FXML
    void changeSceneToMenu(ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        gameLoop.exitGame(null);
        window.setScene((this.menuScene));
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    boolean startGame(GameOptions options) {

        GameLoop game = new GameLoop(this.canvas, options);
        if (game.getDrawer() == null)
            return false;
        this.gameLoop = game;
        Thread gameThread = new Thread(game);
        game.getDrawer().setController(this);

        gameThread.start();
        gameThread.setName("Game thread");
        Player firstPlayer = gameLoop.getCurrentPlayer();
        darkName.setText(firstPlayer.getName());
        darkRect.setVisible(false);
        darkTimer.setText(firstPlayer.getTimerString());
        Player secondPlayer = gameLoop.getIdlePlayer();
        lightName.setText(secondPlayer.getName());
        lightRect.setVisible(true);
        lightTimer.setText(secondPlayer.getTimerString());
        return true;
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

        gameLoop.move("move;" + x + ";" + y);
    }

    public Text getLightName() {
        return lightName;
    }

    public void setLightName(Text lightName) {
        this.lightName = lightName;
    }

    public Text getLightTimer() {
        return lightTimer;
    }

    public void setLightTimer(Text lightTimer) {
        this.lightTimer = lightTimer;
    }

    public Text getDarkTiles() {
        return darkTiles;
    }

    public void setDarkTiles(Text darkTiles) {
        this.darkTiles = darkTiles;
    }

    public Text getLightTiles() {
        return lightTiles;
    }

    public void setLightTiles(Text lightTiles) {
        this.lightTiles = lightTiles;
    }

    public Text getDarkName() {
        return darkName;
    }

    public void setDarkName(Text darkName) {
        this.darkName = darkName;
    }

    public Text getDarkTimer() {
        return darkTimer;
    }

    public void setDarkTimer(Text darkTimer) {
        this.darkTimer = darkTimer;
    }

    public Rectangle getDarkRect() {
        return darkRect;
    }

    public void setDarkRect(Rectangle darkRect) {
        this.darkRect = darkRect;
    }

    public Rectangle getLightRect() {
        return lightRect;
    }

    public void setLightRect(Rectangle lightRect) {
        this.lightRect = lightRect;
    }

    public Parent getRoot() {
        return root;
    }

    public void setScenes(Scene menuScene, Scene gameScene) {
        this.menuScene = menuScene;
        this.gameScene = gameScene;
    }

    public Scene getGameScene() {
        return gameScene;
    }
}
