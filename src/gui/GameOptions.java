package gui;

import gamelogic.GameType;
import gamelogic.TileType;

/**
 * A játékbeállításokat tárolja
 */
public class GameOptions {
    private GameType gameType;
    private int timerStartValue;
    private int difficulty;
    private String ipAddress;
    private String name;
    private TileType playerColor;

    /**
     * Inicializálja a beállításokat
     *
     * @param gameType        játékmód
     * @param timerStartValue maximális játékidő egy játékoshoz
     * @param difficulty      nehézségi szint egyszemélyes mód esetén
     * @param ipAddress       IP cím többszemélyes mód esetén
     * @param name            játékos neve
     * @param playerColor     a kezdeményező játékos kezdi-e a játékot
     */
    public GameOptions(GameType gameType, double timerStartValue, int difficulty, String ipAddress, String name, TileType playerColor) {
        this.gameType = gameType;


        this.timerStartValue = (int) (timerStartValue * 60); //converting to int from double
        this.difficulty = difficulty;
        this.ipAddress = ipAddress;
        this.name = name;
        this.playerColor = playerColor;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public String getName() {
        return name;
    }

    public TileType getPlayerColor() {
        return playerColor;
    }
}
