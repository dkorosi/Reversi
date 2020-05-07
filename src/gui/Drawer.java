package gui;

import gamelogic.Board;
import gamelogic.Coordinate;
import gamelogic.TileType;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import static gamelogic.TileType.DARK;
import static gamelogic.TileType.LIGHT;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class Drawer extends AnimationTimer {
    private static final int boardSize = 8;
    // Ha vége a játéknak ezt állítjuk be
    private boolean stop = false;

    private Canvas canvas;
    private Board board;



    public Drawer(Canvas canvas, Board board) {
        this.canvas = canvas;
        this.board = board;

    }

    public void setCanvas(Canvas canvas_new){
        this.canvas =  canvas_new;
    }


    // Minden képfrissítésnél lefut
    @Override
    public void handle(long l) {
        drawBoard();

        if (stop)
            this.stop();
    }

    private void drawBoard() {

            double rectangleWidth = this.canvas.getWidth()/boardSize;
            double rectangleHeight = (this.canvas.getHeight())/boardSize-1;

       GraphicsContext gc = this.canvas.getGraphicsContext2D();


        Color fill = Color.rgb(108,195,172);
        Color stroke = Color.rgb(12,74,60);
        gc.setLineWidth(3);
        gc.setFill(fill);
        gc.setStroke(stroke);
    //board lenullázása
        for(int i =0; i < boardSize ; i ++){ //y
           for(int j = 0 ; j< boardSize;j++) { //x
               gc.fillRect(j*rectangleWidth , i*rectangleHeight , rectangleWidth, rectangleHeight);
               gc.strokeRect(j*rectangleWidth , i*rectangleHeight , rectangleWidth, rectangleHeight);

           }
       }
        // Itt a Canvas-ra kirajzoljuk a Board-ot
        double ovalWidth = 0.8*rectangleWidth;
        double ovalHeight = 0.8*rectangleWidth;

        double x_start = (rectangleWidth-ovalWidth)/2.0;
        double y_start = (rectangleHeight-ovalHeight)/2.0;

        gc.setLineWidth(2);
        gc.setStroke(BLACK);
        for(int i =0; i < boardSize ; i ++) { //y
            for (int j = 0; j < boardSize; j++) { //x
                this.board.getTile(new Coordinate(j,i));
                if(this.board.getTile(new Coordinate(j,i)) == DARK){
                    gc.setFill(BLACK);
                    gc.fillOval((j)*rectangleWidth + x_start,i  * rectangleHeight + y_start,ovalWidth,ovalHeight);
                    gc.strokeOval((j)*rectangleWidth + x_start,i  * rectangleHeight + y_start,ovalWidth,ovalHeight);
                }
                else if(this.board.getTile(new Coordinate(j,i)) == LIGHT){
                    gc.setFill(WHITE);
                    gc.fillOval((j)*rectangleWidth + x_start,i  * rectangleHeight + y_start,ovalWidth,ovalHeight);
                    gc.strokeOval((j)*rectangleWidth + x_start,i  * rectangleHeight + y_start,ovalWidth,ovalHeight);
                }


            }
        }


    }
}
