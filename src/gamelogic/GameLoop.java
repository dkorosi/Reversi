package gamelogic;

import gui.Drawer;
import javafx.scene.canvas.Canvas;

public class GameLoop implements Runnable {

    private Drawer drawer;
    private Board board;
    private Player currentPlayer;
    private Player opponentPlayer;

    public GameLoop(Canvas canvas) {
        this.board = new Board(8, 8);
        Player one = new LocalPlayer("One",this.board , TileType.DARK, 0);
        Player two = new LocalPlayer("Two", this.board, TileType.LIGHT, 0);

        currentPlayer = one;
        opponentPlayer = two;
        this.drawer = new Drawer(canvas, board);
    }

    public Drawer getDrawer() {
        return drawer;
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

    public void move(Coordinate pos) {
        Player temp;
        if (board.getCurrent() == currentPlayer.getColor() && board.isValidMove(pos)) {
            currentPlayer.setNextMove(pos);
            currentPlayer.makeMove();
            temp = currentPlayer;
            currentPlayer = opponentPlayer;
            opponentPlayer = temp;
        }
    }
}
