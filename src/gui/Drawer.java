package gui;

import gamelogic.Board;
import gamelogic.Coordinate;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static gamelogic.TileType.DARK;
import static gamelogic.TileType.LIGHT;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Drawer extends AnimationTimer {

    // Ha vége a játéknak ezt állítjuk be
    private boolean stop = false;
    private Canvas canvas;
    private Board board;
    private DrawerController controller;


    public Drawer(Canvas canvas, Board board) {
        this.canvas = canvas;
        this.board = board;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setController(DrawerController controller) {
        this.controller = controller;
    }

    // Minden képfrissítésnél lefut
    @Override
    public void handle(long l) {
        drawBoard();
        this.controller.refreshTimer();
        if (stop)
            this.stop();
    }

    private void drawBoard() {
        double rectangleWidth = this.canvas.getWidth() / this.board.getWidth();
        double rectangleHeight = (this.canvas.getHeight()) / this.board.getHeight() - 1;

        GraphicsContext gc = this.canvas.getGraphicsContext2D();

        Color fill = Color.rgb(108, 195, 172);
        Color stroke = Color.rgb(12, 74, 60);
        gc.setLineWidth(3);
        gc.setFill(fill);
        gc.setStroke(stroke);
        //board lenullázása
        for (int i = 0; i < this.board.getHeight(); i++) { //y
            for (int j = 0; j < this.board.getWidth(); j++) { //x
                gc.fillRect(j * rectangleWidth, i * rectangleHeight, rectangleWidth, rectangleHeight);
                gc.strokeRect(j * rectangleWidth, i * rectangleHeight, rectangleWidth, rectangleHeight);

            }
        }
        // Itt a Canvas-ra kirajzoljuk a Board-ot
        double ovalWidth = 0.8 * rectangleWidth;
        double ovalHeight = 0.8 * rectangleWidth;

        double xStart = (rectangleWidth - ovalWidth) / 2.0;
        double yStart = (rectangleHeight - ovalHeight) / 2.0;

        gc.setLineWidth(2);
        gc.setStroke(BLACK);
        for (int i = 0; i < this.board.getHeight(); i++) { //y
            for (int j = 0; j < this.board.getWidth(); j++) { //x
                this.board.getTile(new Coordinate(j, i));
                if (DARK == this.board.getTile(new Coordinate(j, i))) {
                    gc.setFill(BLACK);
                    gc.fillOval((j) * rectangleWidth + xStart, i * rectangleHeight + yStart, ovalWidth, ovalHeight);
                    gc.strokeOval((j) * rectangleWidth + xStart, i * rectangleHeight + yStart, ovalWidth, ovalHeight);
                } else if (LIGHT == this.board.getTile(new Coordinate(j, i))) {
                    gc.setFill(WHITE);
                    gc.fillOval((j) * rectangleWidth + xStart, i * rectangleHeight + yStart, ovalWidth, ovalHeight);
                    gc.strokeOval((j) * rectangleWidth + xStart, i * rectangleHeight + yStart, ovalWidth, ovalHeight);
                }

            }
        }
        fill = Color.rgb(115, 165, 195);
        stroke = Color.rgb(12, 74, 60);
        gc.setLineWidth(3);
        gc.setFill(fill);
        gc.setStroke(stroke);
        for (int i = 0; i < this.board.getValidCoordinates().size(); i++) {
            Coordinate cor = board.getValidCoordinates().get(i);
            gc.fillRect(cor.getX() * rectangleWidth, cor.getY() * rectangleHeight, rectangleWidth, rectangleHeight);
            gc.strokeRect(cor.getX() * rectangleWidth, cor.getY() * rectangleHeight, rectangleWidth, rectangleHeight);
        }
    }
}
