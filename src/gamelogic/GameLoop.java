package gamelogic;

import ai.AiPlayer;
import gui.Drawer;
import gui.GameOptions;
import gui.StatisticRow;
import javafx.scene.canvas.Canvas;
import net.NetworkBroker;
import net.OnlinePlayer;

import java.io.*;
import java.util.Properties;

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

    // Ha elmentettük a játékot
    private boolean saved = false;


    public GameLoop(Canvas canvas, GameOptions options) {
        if (options.isLoaded()) {
            boolean successfulLoaded = loadGame();
            if (!successfulLoaded)
                return;
        } else {
            gameType = options.getGameType();
            int boardDim = getBoardDim();
            this.board = new Board(boardDim, boardDim);

            // A megfelelő játékosinicializálás
            switch (gameType) {
                case SINGLE:
                    player = new LocalPlayer("Player 1", options.getPlayerColor(), options.getTimerStartValue());
                    opponent = new AiPlayer("Cpu(" + (options.getDifficulty() + 1) + ")",
                            options.getPlayerColor().enemyTileType(), options.getDifficulty(), boardDim, boardDim);
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
        }
        this.drawer = new Drawer(canvas, board);
    }

    private int getBoardDim() {
        Properties prop = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("./data/config.properties")) {
            prop.load(fileInputStream);
            String boardDimString = prop.getProperty("board_dim", "8");
            return Integer.parseInt(boardDimString);
        } catch (IOException e) {
            System.err.println("Config file not found");
            return 8;
        }
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
        // Ha nem mentettünk játékot, akkor vége
        if (!saved)
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

        try (FileWriter csvWriter = new FileWriter("./data/statistics.csv", true)) {

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
                } else if (gameType == GameType.SINGLE) {
                    // Ha single és még fut a játék, elmentjük
                    if (board.isActive()) {
                        saveGame();
                        saved = true;
                    }
                } else
                    board.setAll(player.getColor());
            }
        }
        return true;
    }

    private void saveGame() {
        try (ObjectOutput boardOut = new ObjectOutputStream(new FileOutputStream("./data/savegame/board.ser"));
             ObjectOutput playerOut = new ObjectOutputStream(new FileOutputStream("./data/savegame/player.ser"));
             ObjectOutput opponentOut = new ObjectOutputStream(new FileOutputStream("./data/savegame/opponent.ser"))) {
            boardOut.writeObject(board);
            playerOut.writeObject(player);
            opponentOut.writeObject(opponent);
        } catch (IOException e) {
            System.err.println("Cannot save game");
        }
    }

    private boolean loadGame() {
        try (ObjectInput boardIn = new ObjectInputStream(new FileInputStream("./data/savegame/board.ser"));
             ObjectInput playerIn = new ObjectInputStream(new FileInputStream("./data/savegame/player.ser"));
             ObjectInput opponentIn = new ObjectInputStream(new FileInputStream("./data/savegame/opponent.ser"))) {
            board = (Board) boardIn.readObject();
            player = (Player) playerIn.readObject();
            opponent = (Player) opponentIn.readObject();
            gameType = GameType.SINGLE;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Cannot load game");
            return false;
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
