package gui;

import gamelogic.GameType;
import gamelogic.TileType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.NetUtils;

import java.io.IOException;
import java.net.UnknownHostException;

import static gamelogic.GameType.*;


public class MenuController {

    @FXML
    private Slider timerSliderSingle;
    @FXML
    private Slider timerSliderMulti;
    @FXML
    private ToggleGroup startingColorTG;

    @FXML
    private ToggleGroup startingColorTGMulti;
    @FXML
    private ToggleGroup difficultyTG;
    @FXML
    private TextField multiNameText;

    @FXML
    private TextField multiIPAddrText;

    @FXML
    private Text onlineMessage;

    @FXML
    private ButtonBar invitationButtonBar;

    private GameController gameController;

    public ToggleGroup getStartingColorTG() {
        return startingColorTG;
    }

    public ToggleGroup getDifficultyTG() {
        return difficultyTG;
    }

    /**
     * Inicializál néhány FXML node-ot
     */
    @FXML
    public void initialize() {
        startingColorTG.getToggles().get(0).setUserData(TileType.LIGHT);
        startingColorTG.getToggles().get(1).setUserData(TileType.DARK);
        startingColorTGMulti.getToggles().get(0).setUserData(TileType.LIGHT);
        startingColorTGMulti.getToggles().get(1).setUserData(TileType.DARK);
        difficultyTG.getToggles().get(0).setUserData(0);
        difficultyTG.getToggles().get(1).setUserData(1);
        difficultyTG.getToggles().get(2).setUserData(2);

        try {
            onlineMessage.setText("Your IP address is " + NetUtils.getIpAddress());
        } catch (UnknownHostException e) {
            onlineMessage.setText("Your IP address is unavailable");
        }
    }

    @FXML
    void startSingleGame(ActionEvent event) throws IOException {
        changeSceneToCanvas(event, SINGLE, this.timerSliderSingle.getValue()); //Előbb change scene aztán init
    }

    @FXML
    void loadSingleGame(ActionEvent event) throws IOException {
        changeSceneToCanvas(event, SINGLE, this.timerSliderSingle.getValue()); //Előbb change scene aztán init
    }

    @FXML
    void startLocalGame(ActionEvent event) throws IOException {
        String name = getMultiNameText();
        changeSceneToCanvas(event, LOCAL, this.timerSliderMulti.getValue()); //Előbb change scene aztán init
    }

    @FXML
    void startOnlineGame(ActionEvent event) throws IOException {
        String ipAddr = getMultiIPAddrText();
        String name = getMultiNameText();
        changeSceneToCanvas(event, ONLINE, this.timerSliderMulti.getValue()); //Előbb change scene aztán init
        //GameLoop game = new GameLoop(canvas_sb);
    }

    @FXML
    double getTimerSliderSingleVal(MouseEvent event) {
        return timerSliderSingle.getValue();
    }

    @FXML
    double getTimerSliderMultiVal(MouseEvent event) {
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

    @FXML
    void acceptInvitation(ActionEvent event) {

    }

    @FXML
    void declineInvitation(ActionEvent event) {

    }

    private void changeSceneToCanvas(ActionEvent event, GameType gameType, double timer) throws IOException {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene gameScene;
        if (gameController == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/canvasDrawer.fxml"));
            loader.load();
            gameController = loader.getController();
            gameScene = new Scene(gameController.getRoot(), 700, 510);

            // passing menu scene so drawer controller can change back, and game scene so we can change to it
            gameController.setScenes(window.getScene(), gameScene);
        } else {
            gameScene = gameController.getGameScene();
        }

        // Kiválaszott szín
        TileType selectedColor = (TileType) startingColorTG.getSelectedToggle().getUserData();

        int diff = (int) difficultyTG.getSelectedToggle().getUserData();

        GameOptions gameOptions = new GameOptions(gameType, timer, diff, getMultiIPAddrText(), getMultiNameText(), selectedColor);

        gameController.startGame(gameOptions);
        window.setScene((gameScene));
        window.show();
    }

}


