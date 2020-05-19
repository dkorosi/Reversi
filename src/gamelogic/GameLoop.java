package gamelogic;

import ai.AiPlayer;
import gui.Drawer;
import gui.GameOptions;
import javafx.scene.canvas.Canvas;

public class GameLoop implements Runnable {

    private GameType gameType;
    private Drawer drawer;
    private Board board;
    // Aki elindította a játékot
    private Player player;
    // Ellenfél játékos, lehet AI, lokális játékos, vagy online játékos
    private Player opponent;


    public GameLoop(Canvas canvas, GameOptions options) {
        this.gameType = options.getGameType();
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
                player = new LocalPlayer("Player 1", TileType.DARK, options.getTimerStartValue());
                opponent = new LocalPlayer("Player 2", TileType.LIGHT, options.getTimerStartValue());
                break;
            case ONLINE:
                player = new LocalPlayer(options.getName(), options.getPlayerColor(), options.getTimerStartValue());
//                opponent = new OnlinePlayer(options.getName(), options.getPlayerColor(), options.getTimerStartValue());
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

    public Board getBoard() {
        return board;
    }

    @Override
    public void run() {
        drawer.start();
        while (board.isActive()) {
            Player currentPlayer = getCurrentPlayer();
            if (currentPlayer.isHuman()) {
                Thread.yield();
            } else {
                currentPlayer.makeMove(board);
            }

        }
        drawer.setStop();
    }

    public void move(Coordinate pos) {
        Player player = getCurrentPlayer();
        if (player.isHuman()) {
            player.setNextMove(pos);
            player.makeMove(board);
        }
    }
}
