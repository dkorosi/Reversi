package gui;

import gamelogic.Board;
import gamelogic.Coordinate;
import gamelogic.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

import static gamelogic.TileType.DARK;
import static gamelogic.TileType.LIGHT;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Drawer extends AnimationTimer {

    // Ha vége a játéknak ezt állítjuk be
    private boolean stop = false;
    private Canvas canvas;
    private Board board;
    private GameController controller;
    private long lastTime = 0;

    private Player lastCurrentPlayer;

    public Drawer(Canvas canvas, Board board) {
        this.canvas = canvas;
        this.board = board;
    }

    private void refreshTimer() {
        Player currentPlayer = controller.getGameLoop().getCurrentPlayer();
        if (lastCurrentPlayer == currentPlayer && lastTime != 0) {
            long sysTimer = System.currentTimeMillis();
            currentPlayer.decrementTimer(sysTimer - lastTime);
            lastTime = sysTimer;
            if (currentPlayer.getColor() == DARK)
                controller.getDarkTimer().setText(currentPlayer.getTimerString());
            else
                controller.getLightTimer().setText(currentPlayer.getTimerString());
        } else {
            lastTime = System.currentTimeMillis();
            lastCurrentPlayer = currentPlayer;
        }
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    // Minden képfrissítésnél lefut
    @Override
    public void handle(long l) {
        drawBoard();
        refreshTimer();
        updateFields();
        if (stop) {
            drawBoard();
            GraphicsContext gc = this.canvas.getGraphicsContext2D();
            gc.setFill(board.getWinning() == LIGHT ? WHITE : BLACK);
            gc.fillOval(10, 10, 200, 200);
            this.stop();
        }
    }

    private void updateFields() {
        if (controller.getGameLoop().getCurrentPlayer().getColor() == DARK) {
            controller.getDarkRect().setVisible(true);
            controller.getLightRect().setVisible(false);
        } else {
            controller.getDarkRect().setVisible(false);
            controller.getLightRect().setVisible(true);
        }

        controller.getDarkTiles().setText(String.valueOf(controller.getGameLoop().getBoard().getNumberOfTiles(DARK)));
        controller.getLightTiles().setText(String.valueOf(controller.getGameLoop().getBoard().getNumberOfTiles(LIGHT)));
    }

    private void drawBoard() {
        double tileWidth = this.canvas.getWidth() / this.board.getWidth();
        double tileHeight = (this.canvas.getHeight()) / this.board.getHeight() - 1;

        GraphicsContext gc = this.canvas.getGraphicsContext2D();

        // Board lenullázása
        drawBase(gc, tileWidth, tileHeight);

        // Itt a Canvas-ra kirajzoljuk a Board-ot
        drawPlayers(gc, tileWidth, tileHeight);

        // Lehetséges lépések rajzolása
        if (controller.getGameLoop().getCurrentPlayer().isGuiPlayer())
            drawValidMoves(gc, tileWidth, tileHeight);
    }

    private void drawBase(GraphicsContext gc, double tileWidth, double tileHeight) {
        Color fill = Color.rgb(108, 195, 172);
        Color stroke = Color.rgb(12, 74, 60);
        gc.setLineWidth(3);
        gc.setFill(fill);
        gc.setStroke(stroke);

        for (int i = 0; i < this.board.getHeight(); i++) { //y
            for (int j = 0; j < this.board.getWidth(); j++) { //x
                gc.fillRect(j * tileWidth, i * tileHeight, tileWidth, tileHeight);
                gc.strokeRect(j * tileWidth, i * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    private void drawPlayers(GraphicsContext gc, double tileWidth, double tileHeight) {
        double ovalWidth = 0.8 * tileWidth;
        double ovalHeight = 0.8 * tileWidth;

        double xStart = (tileWidth - ovalWidth) / 2;
        double yStart = (tileHeight - ovalHeight) / 2;

        gc.setLineWidth(2);
        gc.setStroke(BLACK);
        for (int i = 0; i < this.board.getHeight(); i++) { // y
            for (int j = 0; j < this.board.getWidth(); j++) { // x
                this.board.getTile(new Coordinate(j, i));
                if (DARK == this.board.getTile(new Coordinate(j, i))) {
                    gc.setFill(BLACK);
                    gc.fillOval(j * tileWidth + xStart, i * tileHeight + yStart, ovalWidth, ovalHeight);
                    gc.strokeOval(j * tileWidth + xStart, i * tileHeight + yStart, ovalWidth, ovalHeight);
                } else if (LIGHT == this.board.getTile(new Coordinate(j, i))) {
                    gc.setFill(WHITE);
                    gc.fillOval(j * tileWidth + xStart, i * tileHeight + yStart, ovalWidth, ovalHeight);
                    gc.strokeOval(j * tileWidth + xStart, i * tileHeight + yStart, ovalWidth, ovalHeight);
                }
            }
        }
    }

    private void drawValidMoves(GraphicsContext gc, double tileWidth, double tileHeight) {
        Color fill = Color.rgb(115, 165, 195);
        Color stroke = Color.rgb(12, 74, 60);
        gc.setLineWidth(3);
        gc.setFill(fill);
        gc.setStroke(stroke);
        List<Coordinate> validCoordinates = this.board.getValidCoordinates();
        for (Coordinate cor : validCoordinates) {
            gc.fillRect(cor.getX() * tileWidth, cor.getY() * tileHeight, tileWidth, tileHeight);
            gc.strokeRect(cor.getX() * tileWidth, cor.getY() * tileHeight, tileWidth, tileHeight);
        }
    }

    /**
     * Ha vége a játéknak, beállítjuk, hogy állítsuk le a thread-et
     */
    public void setStop() {
        this.stop = true;
    }
}
