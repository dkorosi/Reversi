package gamelogic;

import ai.AiPlayer;
import gui.Drawer;
import gui.GameOptions;
import gui.StatisticRow;
import javafx.scene.canvas.Canvas;
import net.NetworkBroker;
import net.OnlinePlayer;

import java.io.FileWriter;
import java.io.IOException;

public class GameLoop implements Runnable {

    private Drawer drawer;
    private Board board;
    // Aki elindította a játékot
    private Player player;
    // Ellenfél játékos, lehet AI, lokális játékos, vagy online játékos
    private Player opponent;

    private GameType gameType;
    private boolean stop = false;
    private NetworkBroker networkBroker = null;


    public GameLoop(Canvas canvas, GameOptions options) {
        gameType = options.getGameType();
        int height = 8;
        int width = 8;
        this.board = new Board(width, height);

        // Itt lesz majd a megfelelő játékosinicializálás
        switch (gameType) {
            case SINGLE:
                player = new LocalPlayer("Player 1", options.getPlayerColor(), options.getTimerStartValue());
                opponent = new AiPlayer("Cpu(" + (options.getDifficulty() + 1) + ")",
                        options.getPlayerColor().enemyTileType(), options.getDifficulty(), width, height);
                break;
            case LOCAL:
                player = new LocalPlayer("Black", TileType.DARK, options.getTimerStartValue());
                opponent = new LocalPlayer("White", TileType.LIGHT, options.getTimerStartValue());
                break;
            case ONLINE:
                this.networkBroker = options.getNetworkBroker();
                player = new LocalPlayer(options.getName(), options.getPlayerColor(), options.getTimerStartValue());

                opponent = new OnlinePlayer(options.getOppName(), options.getPlayerColor().enemyTileType(),
                        options.getTimerStartValue());

                // Event listenere-eket regisztráljuk lépésre és kilépésre
                networkBroker.registerEventListener("move;", opponent::setNextMove);
                networkBroker.registerEventListener("stop;", this::exitGame);
                break;
        }

        this.drawer = new Drawer(canvas, board);
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public Player getCurrentPlayer() {
        if (player.getColor() == board.getCurrent())
            return player;
        else
            return opponent;
    }

    public Player getIdlePlayer() {
        if (player.getColor() == board.getCurrent())
            return opponent;
        else
            return player;
    }

    public Player getOutOfTimePlayer() {
        if (player.timeRunOut())
            return player;
        else if (opponent.timeRunOut())
            return opponent;
        else
            return null;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void run() {
        drawer.start();
        while (board.isActive() && !stop) {
            Player currentPlayer = getCurrentPlayer();
            if (currentPlayer.timeRunOut()) {
                exitGame(null);
            }
            if (currentPlayer.isLocal()) {
                Thread.yield();
            } else {
                currentPlayer.makeMove(board);
            }
        }
        saveResults();
        drawer.setStop();
    }

    private void saveResults() {

        TileType winner = board.getWinning();
        String winnerName;
        if (winner == TileType.EMPTY) {
            winnerName = "DRAW";
        } else
            winnerName = getNameByTile(winner);

        try (FileWriter csvWriter = new FileWriter("./resources/statistics.csv", true)) {

            csvWriter.write(new StatisticRow(gameType.toString(), player.getName(), opponent.getName(), winnerName).toCsvLine());
        } catch (IOException e) {
            System.err.println("Error writing to statistics csv");
        }
    }

    public void move(String message) {
        Player player = getCurrentPlayer();
        if (player.isLocal()) {
            player.setNextMove(message);
            player.makeMove(board);
            if (networkBroker != null) {
                networkBroker.sendToSocket(message);
            }
        }
    }

    public Boolean exitGame(String message) {
        stop = true;

        Player outOfTimePlayer = getOutOfTimePlayer();
        // Time ran out
        if (outOfTimePlayer != null) {
            board.setAll(outOfTimePlayer.getColor().enemyTileType());
        } else {
            // Játékot feladta az ellenfél, mi nyerünk 64-0-ra
            if (message != null) {
                board.setAll(player.getColor());
            } else {

                if (networkBroker != null)
                    networkBroker.sendStop();
                // Mi léptünk ki
                if (gameType == GameType.LOCAL) {
                    board.setAll(getIdlePlayer().getColor());
                } else
                    board.setAll(player.getColor());
            }
        }
        return true;
    }

    public String getNameByTile(TileType tileType) {
        if (player.getColor() == tileType)
            return player.getName();
        else
            return opponent.getName();
    }
}
