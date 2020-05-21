package gui;

import gamelogic.GameType;
import gamelogic.TileType;
import net.NetworkBroker;

/**
 * A játékbeállításokat tárolja
 */
public class GameOptions {
    private GameType gameType;
    private int timerStartValue;
    private int difficulty;
    private String name;
    private String oppName;
    private TileType playerColor;

    private NetworkBroker networkBroker;
    private boolean loaded = false;

    /**
     * @param gameType        játékmód
     * @param timerStartValue maximális játékidő egy játékoshoz
     * @param difficulty      nehézségi szint egyszemélyes mód esetén
     * @param playerColor     a kezdeményező játékos kezdi-e a játékot
     * @param name            játékos neve
     * @param oppName         ellenfél játékos neve
     * @param timerStartValue maximális játékidő egy játékoshoz
     * @param playerColor     ennél a gépnél lévő játékos színe
     * @param networkBroker   üzenetküldéshez a bróke
     */
    private GameOptions(GameType gameType, int timerStartValue, int difficulty, String name, String oppName, TileType playerColor, NetworkBroker networkBroker) {
        this.gameType = gameType;
        this.timerStartValue = timerStartValue;
        this.difficulty = difficulty;
        this.name = name;
        this.oppName = oppName;
        this.playerColor = playerColor;
        this.networkBroker = networkBroker;
    }

    public GameOptions(GameType gameType, boolean loaded) {
        this.gameType = gameType;
        this.loaded = loaded;
    }

    /**
     * Inicializálja a beállításokat gép ellen
     *
     * @param timerStartValue maximális játékidő egy játékoshoz
     * @param difficulty      nehézségi szint egyszemélyes mód esetén
     * @param playerColor     a kezdeményező játékos kezdi-e a játékot
     * @return játékbeállítások objektum
     */
    public static GameOptions createSingleGame(int timerStartValue, int difficulty, TileType playerColor) {
        return new GameOptions(GameType.SINGLE, timerStartValue, difficulty, null, null, playerColor, null);
    }

    /**
     * Inicializálja a beállításokat lokális játékhoz
     *
     * @param timerStartValue maximális játékidő egy játékoshoz
     * @return játékbeállítások objektum
     */
    public static GameOptions createLocalGame(int timerStartValue) {
        return new GameOptions(GameType.LOCAL, timerStartValue, 0, null, null, null, null);
    }

    /**
     * Inicializálja a beállításokat hálózati játékhoz
     *
     * @param name            játékos neve
     * @param oppName         ellenfél játékos neve
     * @param timerStartValue maximális játékidő egy játékoshoz
     * @param playerColor     ennél a gépnél lévő játékos színe
     * @param networkBroker   üzenetküldéshez a bróker
     * @return játékbeállítások objektum
     */
    public static GameOptions createOnlineGame(String name, String oppName, int timerStartValue, TileType playerColor, NetworkBroker networkBroker) {
        return new GameOptions(GameType.ONLINE, timerStartValue, 0, name, oppName, playerColor, networkBroker);
    }

    public static GameOptions createLoadedGame() {
        return new GameOptions(GameType.SINGLE, true);
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getTimerStartValue() {
        return timerStartValue;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }

    public TileType getPlayerColor() {
        return playerColor;
    }

    public String getOppName() {
        return oppName;
    }

    public NetworkBroker getNetworkBroker() {
        return networkBroker;
    }

    public boolean isLoaded() {
        return loaded;
    }
}
