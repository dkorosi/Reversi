package gui;

import gamelogic.GameType;
import gamelogic.TileType;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.NetUtils;
import net.NetworkBroker;
import net.NetworkConnection;

import java.io.BufferedReader;
import java.io.FileReader;
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
    private TableView<StatisticRow> statisticsTable;
    @FXML
    private TableColumn<StatisticRow, String> gameTypeCol;
    @FXML
    private TableColumn<StatisticRow, String> firstPlayerCol;
    @FXML
    private TableColumn<StatisticRow, String> secondPlayerCol;
    @FXML
    private TableColumn<StatisticRow, String> winnerCol;

    private GameController gameController;

    private NetworkBroker networkBroker;
    private PopupController popupController;
    private boolean statisticsLoaded;

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

        gameTypeCol.setCellValueFactory(new PropertyValueFactory<>("gameType"));
        firstPlayerCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        secondPlayerCol.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        winnerCol.setCellValueFactory(new PropertyValueFactory<>("winnerName"));

        loadStatistics();
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
        changeSceneToCanvas(GameOptions.createLoadedGame());
    }

    @FXML
    void startLocalGame() {
        changeSceneToCanvas(GameOptions.createLocalGame(getTimerSliderMultiVal()));
    }


    @FXML
    String getMultiNameText() {
        String name = multiNameText.getText();
        if (name.isEmpty()) {
            name = "You";
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
            networkBroker.sendStop();

        networkBroker = new NetworkBroker();
        NetworkConnection connection = new NetworkConnection(networkBroker);

        Thread netThread = new Thread(connection);

        netThread.start();
        netThread.setName("Net Thread");
        String name = getMultiNameText();
        if (name.equals("You"))
            name = "Host";
        int timer = getTimerSliderMultiVal();
        TileType color = getColor(ONLINE);
        networkBroker.sendToSocket("serverstart;" + name + ";" + timer + ";" + color);

        if (popupController == null) {
            try {
                popupController = getPopupController();
            } catch (IOException ignored) {
                System.err.println("Popup window cannot be created");
                return;
            }
        }

        networkBroker.registerEventListener("clientstart;", this::clientConnected);
        networkBroker.registerEventListener("stop;", popupController::closePopup);
        popupController.setNetworkBroker(networkBroker);
        popupController.showServer();
    }

    private PopupController getPopupController() throws IOException {
        Parent popupRoot;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/popup.fxml"));
        popupRoot = loader.load();

        PopupController controller = loader.getController();
        Scene popupScene = new Scene(popupRoot);
        Stage popupWindow = new Stage(StageStyle.UNDECORATED);
        popupWindow.setScene(popupScene);
        controller.setStage(popupWindow);
        return controller;
    }

    /**
     * HOST OLDAL
     * Ha a kliens üzenetet küldött, hogy elfogadja a játékot, visszaküldte a beállításokat és elindítjuk
     *
     * @param message üzenet (stop vagy kezdés)
     */
    private Boolean clientConnected(String message) {
        Platform.runLater(() -> {
            // Üzenet: clientstart;yourName;oppName;timer;yourColor
            String[] split = message.split(";");

            String yourName = split[1];
            String oppName = split[2];
            if (oppName.equals("You"))
                oppName = "client";

            String timer = split[3];
            String yourColor = split[4];
            GameOptions gameOptions = GameOptions.createOnlineGame(yourName, oppName, Integer.parseInt(timer),
                    TileType.valueOf(yourColor), networkBroker);

            popupController.closePopup(null);
            changeSceneToCanvas(gameOptions);
        });

        return true;
    }

    /**
     * KLIENS OLDAL
     * Kliens ezzel kezd el socket-re kapcsolódni
     */
    @FXML
    void connectToGame() {
        // Ha már van életben kapcsolat, leállítjuk
        if (networkBroker != null) {
            networkBroker.sendStop();
        }
        networkBroker = new NetworkBroker();
        networkBroker.registerEventListener("serverstart;", this::showClientPopup);
        String ipAddr = getMultiIPAddrText();
        NetworkConnection connection = new NetworkConnection(networkBroker, ipAddr);

        Thread netThread = new Thread(connection);
        netThread.start();
        netThread.setName("Net thread");
    }

    /**
     * KLIENS OLDAL
     * Ha a szerver indított játékhívást, a bróker ezt a függvényt hívja, kliensnek feldob egy popup ablakot, ahol elfogadhatja a játékot
     *
     * @param message bróker által fogadott üzenet
     * @return visszatérés alapján, a bróker kitörölheti mint event listener
     */
    Boolean showClientPopup(String message) {
        Platform.runLater(() -> {
            // serverstart;hostplayername;timer;hostcolor

            if (popupController == null) {
                try {
                    popupController = getPopupController();
                } catch (IOException ignored) {
                    System.err.println("Popup window cannot be created");
                    return;
                }
            }

            String[] split = message.split(";");

            String oppName = split[1];
            String timer = split[2];
            String oppColor = split[3];
            String yourName = getMultiNameText();
            if (yourName.equals("You"))
                yourName = "Client";

            TileType yourColor = TileType.valueOf(oppColor).enemyTileType();

            GameOptions gameOptions = GameOptions.createOnlineGame(yourName, oppName, Integer.parseInt(timer), yourColor, networkBroker);
            popupController.setNetworkBroker(networkBroker);
            popupController.setSceneChangeFunction(this::changeSceneToCanvas);
            popupController.showClient(gameOptions);
        });

        return true;
    }


    @FXML
    void loadStatistics() {
        if (!statisticsLoaded) {
            try (BufferedReader reader = new BufferedReader(new FileReader("./data/statistics.csv"))) {
                ObservableList<StatisticRow> data = statisticsTable.getItems();
                data.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    StatisticRow row = StatisticRow.createStatisticRow(line);
                    data.add(row);
                }
            } catch (IOException e) {
                System.err.println("Error occurred reading statistics.csv or not exists");
            }
        }
        statisticsLoaded = true;
    }

    /**
     * Átállítja a képet a játéktérre, elindítja a játékot
     *
     * @param gameOptions játékbeállítások
     * @return semmi
     */
    Void changeSceneToCanvas(GameOptions gameOptions) {

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
                return null;
            }

            gameController = loader.getController();
            gameScene = new Scene(gameController.getRoot());

            // passing menu scene so drawer controller can change back, and game scene so we can change to it
            gameController.setScenes(window.getScene(), gameScene);
        } else {
            gameScene = gameController.getGameScene();
        }

        boolean successfulStarted = gameController.startGame(gameOptions);
        if (successfulStarted) {
            window.setScene(gameScene);
            // Majd töltsük újra a táblát
            statisticsLoaded = false;
        }
        return null;
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

