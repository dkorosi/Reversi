package gui;

import gamelogic.Coordinate;
import gamelogic.GameLoop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Timer;
import java.io.IOException;



public class DrawerController {

    GameType gameType;
    Scene MenuScene;
    double singleTimer;
    GameLoop gameLoop;


    @FXML
    private Canvas canvas_sb;

    @FXML
    private Text timerCountDown;

    @FXML
    private Text nameText;

    @FXML
    void changeSceneToMenu(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("gui.fxml"));
        Parent root =loader.load();


        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        if(gameLoop.getTimerVal() != 0)
        this.gameLoop.stopTimer();
        window.setScene((this.MenuScene));
        window.show();

    }

    void initDrawerControllerSingle(Scene Menu,GameType type, double singleTimer,boolean black,int difficulty){
        this.MenuScene = Menu;
        this.gameType = type;
        this.singleTimer = singleTimer;

        int min = (int)(this.singleTimer);
        int sec = (int)((this.singleTimer-min)*60);
        int whole = 60 * min + sec;

        GameLoop game = new GameLoop(this.canvas_sb,difficulty,black,whole);
        this.gameLoop = game;
        Thread th = new Thread(game);
        th.start();
        nameText.setText(gameLoop.getCurrentPlayer().getName());
        System.out.println("Start Single GAME DC");

    }

    public void refreshTimer(){

        int min = (int)(gameLoop.getTimerVal()/60);
        int sec = (int)(gameLoop.getTimerVal()-min*60);

        String plus_sec;
        String plus_min;

        if(sec < 10)
            plus_sec = "0";
        else
            plus_sec = "";

        if(min < 10)
            plus_min = "0";
        else
            plus_min = "";

        String timer_string = plus_min + String.valueOf(min) + ":" + plus_sec +  String.valueOf(sec);
        this.timerCountDown.setText(timer_string);
    }


    void initDrawerControllerLocal(Scene Menu,GameType type, double singleTimer,boolean black){
        this.MenuScene = Menu;
        this.gameType = type;
        this.singleTimer = singleTimer;

        int min = (int)(this.singleTimer);
        int sec = (int)((this.singleTimer-min)*60);

        int whole = 60 * min + sec;

        GameLoop game = new GameLoop(this.canvas_sb,black,whole);
        this.gameLoop = game;
        Thread th = new Thread(game);
        th.start();
        nameText.setText(gameLoop.getCurrentPlayer().getName());
        System.out.println("Start Single GAME DC");

    }


    void initDrawerControllerOnline(Scene Menu,GameType type, double singleTimer,boolean black,String ip,String name){
        this.MenuScene = Menu;
        this.gameType = type;
        this.singleTimer = singleTimer;


        int min = (int)(this.singleTimer);
        int sec = (int)((this.singleTimer-min)*60);
        int whole = 60 * min + sec;


        GameLoop game = new GameLoop(this.canvas_sb,black,ip,name,whole);
        this.gameLoop = game;
        Thread th = new Thread(game);
        th.start();
        nameText.setText(gameLoop.getCurrentPlayer().getName());
        System.out.println("Start Single GAME DC");

    }




    Canvas getCanvas(){

        return this.canvas_sb;
    }





    @FXML
    void canvasClicked(MouseEvent event) {
        double boardHeight = this.gameLoop.getBoard().getHeight();
        double boardWidth = this.gameLoop.getBoard().getWidth();
        double x_pos = event.getX();
        double y_pos = event.getY();
        double rectangleWidth = this.canvas_sb.getWidth()/boardWidth; // getter -el kell lekérdezni a board méretét ( 8-as)
        double rectangleHeight = (this.canvas_sb.getHeight())/boardHeight-1;
        int x = (int)(x_pos/rectangleWidth);
        int y = (int)(y_pos/rectangleHeight);
        Coordinate cor = new Coordinate(x,y);
        gameLoop.move(cor);
        refreshTimer();


        nameText.setText(gameLoop.getCurrentPlayer().getName());
    }


}
