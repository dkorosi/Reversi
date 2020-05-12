package gamelogic;

import gui.Drawer;
import gui.GameOptions;
import gui.Timer;
import javafx.scene.canvas.Canvas;

public class GameLoop implements Runnable {

    private Drawer drawer;
    private Board board;
    private Player currentPlayer;
    private Player opponentPlayer;
    private Timer timer;

    public GameLoop(Canvas canvas, GameOptions options) {
        this.board = new Board(8, 8);

        currentPlayer = new LocalPlayer("DARK", TileType.DARK, options.getTimerStartValue());
        opponentPlayer = new LocalPlayer("LIGHT", TileType.LIGHT, options.getTimerStartValue());

        // Itt lesz majd a megfelelő játékosinicializálás
        switch (options.getGameType()) {
            case SINGLE:
                break;
            case LOCAL:
                break;
            case ONLINE:
                break;
        }

        this.drawer = new Drawer(canvas, board);

//        this.timer = new Timer(time);
//        Thread timerTh = new Thread(this.timer);
//        timerTh.start();
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void run() {
        boolean stop = false;
        drawer.start();
        while (true) {

            if (stop) {
                drawer.stop();
                break;
            }

            stop = !board.isActive();

            // Játéklogika implementációja, függvényeket hívogatunk, melyek visszatérési értékeiből tudjuk,
            // hogy vége van-e a játéknak
            //stop = player.nextMove();
        }
    }

    public int getTimerVal() {
        return timer.getTime();
    }

    public void setTimerVal(int time) {
        this.timer.setTime(time);
    }

    public void stopTimer() {
        this.timer.stop();
    }

    public void move(Coordinate pos) {
        Player temp;
        if (board.getCurrent() == currentPlayer.getColor()) {
            if (board.isValidMove(pos)) {
                currentPlayer.setNextMove(pos);
                currentPlayer.makeMove(board);
//                currentPlayer.setTimer(this.timer.getTime());
//                this.timer.setTime(opponentPlayer.getTimer());
                temp = currentPlayer;
                currentPlayer = opponentPlayer;
                opponentPlayer = temp;
            }

        } else {
//            currentPlayer.setTimer(this.timer.getTime());
//            this.timer.setTime(opponentPlayer.getTimer());
            temp = currentPlayer;
            currentPlayer = opponentPlayer;
            opponentPlayer = temp;
        }
    }
}
