package gui;

/**
 * A játékbeállításokat tárolja
 */
public class GameOptions {
    private GameType gameType;
    private double timerStartValue;
    private int difficulty;
    private String ipAddress;
    private String name;
    private boolean isStartingPlayer;

    /**
     * Inicializálja a beállításokat
     * @param gameType játékmód
     * @param timerStartValue maximális játékidő egy játékoshoz
     * @param difficulty nehézségi szint egyszemélyes mód esetén
     * @param ipAddress IP cím többszemélyes mód esetén
     * @param name játékos neve
     * @param isStartingPlayer a kezdeményező játékos kezdi-e a játékot
     */
    public GameOptions(GameType gameType, double timerStartValue, int difficulty, String ipAddress, String name, boolean isStartingPlayer) {
        this.gameType = gameType;
        this.timerStartValue = timerStartValue;
        this.difficulty = difficulty;
        this.ipAddress = ipAddress;
        this.name = name;
        this.isStartingPlayer = isStartingPlayer;
    }

    public GameType getGameType() {
        return gameType;
    }

    public double getTimerStartValue() {
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

    public boolean isStartingPlayer() {
        return isStartingPlayer;
    }
}
