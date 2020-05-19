package gui;

import gamelogic.GameType;
import gamelogic.TileType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.NetUtils;
import net.NetworkBroker;
import net.NetworkConnection;

import java.io.IOException;
import java.net.UnknownHostException;

import static gamelogic.GameType.ONLINE;
import static gamelogic.GameType.SINGLE;


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

    private NetworkBroker networkBroker;

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

        setDefaultOnlineMessage();
    }

    private void setDefaultOnlineMessage() {
        try {
            onlineMessage.setText("Your IP address is " + NetUtils.getIpAddress());
        } catch (UnknownHostException e) {
            onlineMessage.setText("Your IP address is unavailable");
        }
    }

    @FXML
    void startSingleGame() {
        TileType selectedColor = getColor(SINGLE);
        int diff = (int) difficultyTG.getSelectedToggle().getUserData();
        int timer = getTimerSliderSingleVal();
        changeSceneToCanvas(GameOptions.createSingleGame(timer, diff, selectedColor));
    }

    @FXML
    void loadSingleGame() {
        // Itt a timer a betöltött játéké legyen

//        changeSceneToCanvas(event, SINGLE, getTimerSliderSingleVal());
    }

    @FXML
    void startLocalGame() {
        changeSceneToCanvas(GameOptions.createLocalGame(getTimerSliderMultiVal()));
    }


    @FXML
    String getMultiNameText() {
        String name = multiNameText.getText();
        if (name.isEmpty()) {
            name = "Player";
        }

        return name.replace(';', ' ');
    }

    @FXML
    String getMultiIPAddrText() {
        return multiIPAddrText.getText();
    }


    //
    //  NETWORK CONNECTION HANDLING
    //

    /**
     * HOST OLDAL
     * Megfelelő gombnyomás után el kezdünk hostolni egy szervert, melyre várunk egy klienst
     */
    @FXML
    void startListening() {
        if (networkBroker != null)
            networkBroker.sendToSocket("stop;");

        networkBroker = new NetworkBroker(this);
        NetworkConnection connection = new NetworkConnection(networkBroker);

        Thread netThread = new Thread(connection);

        netThread.start();
        String name = getMultiNameText();
        int timer = getTimerSliderMultiVal();
        TileType color = getColor(ONLINE);
        networkBroker.sendToSocket("serverstart;" + name + ";" + timer + ";" + color);
    }

    /**
     * HOST OLDAL
     * Ha a kliens üzenetet küldött, hogy elfogadja a játékot, visszaküldte a beállításokat és elindítjuk
     *
     * @param networkBroker bróker
     */
    public void clientConnected(NetworkBroker networkBroker) {
        String message = networkBroker.getReceivedMessage();

        if (message.startsWith("clientstart")) {
            // Üzenet: clientstart;yourName;oppName;timer;yourColor
            String[] split = message.split(";");

            GameOptions gameOptions = GameOptions.createOnlineGame(split[1], split[2], Integer.parseInt(split[3]),
                    TileType.valueOf(split[4]), networkBroker);

            changeSceneToCanvas(gameOptions);
        }
    }

    /**
     * KLIENS OLDAL
     * Kliens ezzel kezd el socket-re kapcsolódni
     */
    @FXML
    void connectToGame() {
        // Ha már van életben kapcsolat, leállítjuk
        if (networkBroker != null) {
            networkBroker.sendToSocket("stop;");
        }
        networkBroker = new NetworkBroker(this);
        String ipAddr = getMultiIPAddrText();
        NetworkConnection connection = new NetworkConnection(networkBroker, ipAddr);

        Thread netThread = new Thread(connection);
        netThread.start();
    }

    /**
     * KLIENS OLDAL
     * Kapcsolatfelvétel után fogadjuk a szerver üzenetét, kiírunk egy meghívást a felhasználónak
     *
     * @param invMessage szerver által küldött üzenet
     */
    public void serverInvited(String invMessage) {

        // Üzenet: serverstart;oppName;timer;oppColor
        String[] split = invMessage.split(";");

        String serverName = split[1];
        invitationButtonBar.setVisible(true);
        networkBroker.addReceivedMessage("start;");
        onlineMessage.setText("Game invitation by " + serverName);
    }

    /**
     * KLIENS OLDAL
     * Felhasználó elfogadja a meghívást, küldünk válaszüzenetet, elkezdjük a játékot
     */
    @FXML
    void acceptInvitation() {
        // serverstart;hostplayername;timer;hostcolor
        String message = networkBroker.getReceivedMessage();
        if (message != null) {

            String[] split = message.split(";");

            String oppName = split[1];
            String timer = split[2];
            String oppColor = split[3];
            String yourName = getMultiNameText();
            TileType yourColor = TileType.valueOf(oppColor).enemyTileType();

            networkBroker.sendToSocket("clientstart;" + oppName + ";" + yourName + ";" + timer + ";" + oppColor);
            GameOptions gameOptions = GameOptions.createOnlineGame(yourName, oppName, Integer.parseInt(timer), yourColor, networkBroker);
            changeSceneToCanvas(gameOptions);
        }
    }

    /**
     * Felhasználó elutasítja a meghívást
     */
    @FXML
    void declineInvitation() {
        networkBroker.sendToSocket("stop;");
        setDefaultOnlineMessage();
        invitationButtonBar.setVisible(false);
    }

    private void changeSceneToCanvas(GameOptions gameOptions) {
        // Eltüntetjük az accept, decline gombokat, hogy játék vége után ne jelenjen meg
        setDefaultOnlineMessage();
        invitationButtonBar.setVisible(false);

        // Bármelyik node alapján megkapjuk az ablakot
        Stage window = (Stage) timerSliderSingle.getScene().getWindow();

        Scene gameScene;
        if (gameController == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/canvasDrawer.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                System.err.println("Cannot load fxml for game");
                return;
            }

            gameController = loader.getController();
            gameScene = new Scene(gameController.getRoot(), 700, 510);

            // passing menu scene so drawer controller can change back, and game scene so we can change to it
            gameController.setScenes(window.getScene(), gameScene);
        } else {
            gameScene = gameController.getGameScene();
        }

        gameController.startGame(gameOptions);
        window.setScene((gameScene));
        window.show();
    }

    private TileType getColor(GameType gameType) {
        if (gameType == SINGLE)
            return (TileType) startingColorTG.getSelectedToggle().getUserData();
        else if (gameType == ONLINE)
            return (TileType) startingColorTGMulti.getSelectedToggle().getUserData();
        else
            return TileType.DARK;
    }

    private int getTimerSliderSingleVal() {
        return (int) (timerSliderSingle.getValue() * 60);
    }

    private int getTimerSliderMultiVal() {
        return (int) (timerSliderMulti.getValue() * 60);
    }

}

