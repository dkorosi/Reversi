package gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.NetworkBroker;

import java.util.function.Function;

public class PopupController {

    @FXML
    private Label popupLabel;

    @FXML
    private AnchorPane serverPane;

    @FXML
    private ButtonBar clientButtonBar;

    private Stage popupWindow;

    private MenuController menuController;
    private NetworkBroker networkBroker;
    private GameOptions gameOptions;
    private Function<GameOptions, Void> changeSceneToCanvas;

    public void setStage(Stage popupWindow) {
        this.popupWindow = popupWindow;
    }

    /**
     * Feljön a popup, hogy várunk klienst
     */
    public void showServer() {
        clientButtonBar.setVisible(false);
        serverPane.setVisible(true);
        popupLabel.setText("Waiting for client to connect...");
        popupWindow.show();
    }

    /**
     * Szerverre való meghívás és gombok kijelzése
     *
     * @param gameOptions játékbeállítások
     */
    public void showClient(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
        clientButtonBar.setVisible(true);
        serverPane.setVisible(false);
        popupLabel.setText("Invited by:\n" + gameOptions.getOppName());
        popupWindow.show();
    }

    @FXML
    void acceptInvitation() {
        String toSend = "clientstart;" + gameOptions.getOppName() + ";" + gameOptions.getName() + ";" +
                gameOptions.getTimerStartValue() + ";" + gameOptions.getPlayerColor().enemyTileType();
        networkBroker.sendToSocket(toSend);
        popupWindow.close();
        changeSceneToCanvas.apply(gameOptions);
    }

    @FXML
    void declineInvitation() {
        networkBroker.sendStop();
        popupWindow.close();
    }

    @FXML
    void cancelConnection() {
        networkBroker.sendStop();
        closePopup(null);
    }

    public Boolean closePopup(String message) {
        Platform.runLater(() -> {
            popupWindow.close();
        });
        return true;
    }


    public void setNetworkBroker(NetworkBroker networkBroker) {
        this.networkBroker = networkBroker;
    }

    public void setSceneChangeFunction(Function<GameOptions, Void> changeSceneToCanvas) {
        this.changeSceneToCanvas = changeSceneToCanvas;
    }
}
