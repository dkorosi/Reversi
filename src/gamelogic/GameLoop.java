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
        this.board = new Board(8, 8);

        player = new LocalPlayer("Player", options.getPlayerColor(), options.getTimerStartValue());

        // Itt lesz majd a megfelelő játékosinicializálás
        switch (gameType) {
            case SINGLE:
                opponent = new AiPlayer("Ai", options.getPlayerColor().enemyTileType(), 0, options.getDifficulty());
                break;
            case LOCAL:
                opponent = new LocalPlayer("Player2", options.getPlayerColor().enemyTileType(), options.getTimerStartValue());
                break;
            case ONLINE:
                break;

        }

        this.drawer = new Drawer(canvas, board);
        this.drawer.setTimer(player.getTimer());
//        this.timer = new Timer(time);
//        Thread timerTh = new Thread(this.timer);
//        timerTh.start();
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

    public Board getBoard() {
        return board;
    }

    @Override
    public void run() {
        drawer.start();
        while (board.isActive()) {
            Player currentPlayer = getCurrentPlayer();
            if (currentPlayer.isOutside()) {
                Thread.yield();
//                synchronized (this) {
//                    if (getCurrentPlayer().isOutside()) {
//                        try {
//                            wait();
//                        } catch (InterruptedException ignored) {
//                        }
//                    }
//                }
            } else {
                currentPlayer.makeMove(board);
            }

        }
        drawer.setStop();
    }

    /*public int getTimerVal() {
        return timer.getTime();
    }

    public void setTimerVal(int time) {
        this.timer.setTime(time);
    }

    public void stopTimer() {
        this.timer.stop();
    }*/

    public void move(Coordinate pos) {
        Player player = getCurrentPlayer();
        if (player.isOutside()) {
            player.setNextMove(pos);
            player.makeMove(board);
        }
    }
}
