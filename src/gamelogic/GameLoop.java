package gamelogic;

import gui.Drawer;
import javafx.scene.canvas.Canvas;

public class GameLoop implements Runnable {

    private Drawer drawer;
    private Board board;

    public GameLoop(Canvas canvas) {
        Player one = new Player("One", TileType.LIGHT, 0);
        Player two = new Player("Two", TileType.DARK, 0);
        this.board = new Board(one, two);

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

            // Játéklogika implementációja, függvényeket hívogatunk, melyek visszatérési értékeiből tudjuk,
            // hogy vége van-e a játéknak
            //stop = player.nextMove();
        }
    }
}
