package gui;

import gamelogic.Board;
import gamelogic.Coordinate;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Drawer extends AnimationTimer {

    // Ha vége a játéknak ezt állítjuk be
    private boolean stop = false;

    private Canvas canvas;
    private Board board;

    public Drawer(Canvas canvas, Board board) {
        this.canvas = canvas;
        this.board = board;

    }

    // Minden képfrissítésnél lefut
    @Override
    public void handle(long l) {
        drawBoard();

        if (stop)
            this.stop();
    }

    private void drawBoard() {
        // Itt a Canvas-ra kirajzoljuk a Board-ot
        int x = 0;
        int y = 0;

    this.board.getTile(new Coordinate(x,y));

    }
}
