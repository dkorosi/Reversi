package gamelogic;

import gui.Drawer;
import javafx.scene.canvas.Canvas;

public class GameLoop implements Runnable {

    private Drawer drawer;
    private Board board;

    public GameLoop(Canvas canvas) {
        Board board = new Board();

        this.drawer = new Drawer(canvas, board);
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
