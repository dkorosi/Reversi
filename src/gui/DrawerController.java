package gui;

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
import javafx.stage.Stage;

import java.io.IOException;

public class DrawerController {

    GameType gameType;
    Scene MenuScene;
    double singleTimer;

    @FXML
    private Canvas canvas_sb;

    @FXML
    private TextField timerCountDown;

    @FXML
    void changeSceneToMenu(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("gui.fxml"));
        Parent root =loader.load();


        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene((this.MenuScene));
        window.show();

    }
    void initDrawerController(Scene Menu,GameType type, double singleTimer){
        this.MenuScene = Menu;
        this.gameType = type;
        this.singleTimer = singleTimer;
        this.timerCountDown.setText(String.valueOf(this.singleTimer));

    }
    void startDrawerController(){
        switch(this.gameType){
            case SINGLE:
                startSingleGame();
                break;
            case LOCAL:
                startLocalGame();
                break;
            case ONLINE:
                startOnlineGame();
                break;

        }


    }
    Canvas getCanvas(){
        return this.canvas_sb;
    }



    public void startSingleGame() {
        GameLoop game = new GameLoop(this.canvas_sb);
        Thread th = new Thread(game);
        th.start();

        System.out.println("Start Single GAME DC");
    }
    public void startLocalGame() {
       // String Name =   getMultiNameText();
       // GameLoop game = new GameLoop(canvas_sb);
        System.out.println("Start Local Game DC");
       // System.out.println("The name is:"+ Name);
    }

    public void startOnlineGame() {
      //  String IPAddr = getMultiIPAddrText();
       // String Name =   getMultiNameText();
       // GameLoop game = new GameLoop(canvas_sb);
        System.out.println("Start Multi Game DC");
       // System.out.println("The name is: "+ Name + "\n" + "The IP Address is: " + IPAddr);
    }
}
