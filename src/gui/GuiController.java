package gui;

import gamelogic.GameLoop;
import javafx.beans.property.BooleanPropertyBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static gui.GameType.*;

public class GuiController {
    private Stage stage;
    @FXML
    private Slider timerSliderSingle;
    @FXML
    private Slider timerSliderMulti;
    @FXML
    private ToggleGroup startingColorTG;
    @FXML
    private ToggleGroup difficultyTG;
    @FXML
    private TextField multiNameText;

    @FXML
    private TextField multiIPAddrText;


    DrawerController drawerController;

    public ToggleGroup getStartingColorTG() {

        return startingColorTG;
    }

    public ToggleGroup getDifficultyTG() {
        return difficultyTG;
    }

    @FXML
    void startSingleGame(ActionEvent event) throws IOException {
        changeSceneToCanvas(event,SINGLE,this.timerSliderSingle.getValue()); //Előbb change scene aztán init
        System.out.println("Start Single GAME");
    }
    @FXML
    void startLocalGame(ActionEvent event) throws IOException{
        String Name =   getMultiNameText();
        changeSceneToCanvas(event, LOCAL,this.timerSliderSingle.getValue()); //Előbb change scene aztán init
        System.out.println("Start Local Game");
        System.out.println("The name is:"+ Name);
    }
    @FXML
    void startOnlineGame(ActionEvent event) throws IOException{
        String IPAddr = getMultiIPAddrText();
        String Name =   getMultiNameText();
        changeSceneToCanvas(event, ONLINE,this.timerSliderSingle.getValue()); //Előbb change scene aztán init
        //GameLoop game = new GameLoop(canvas_sb);
        System.out.println("Start Multi Game");
        System.out.println("The name is: "+ Name + "\n" + "The IP Address is: " + IPAddr);
    }
    @FXML
    double getTimerSliderSingleVal(MouseEvent event) {
        System.out.println("Slide");
        return timerSliderSingle.getValue();
    }
    @FXML
    double getTimerSliderMultiVal(MouseEvent event) {
        System.out.println("Slide");
        return timerSliderMulti.getValue();
    }


    @FXML
    String getMultiNameText() {
        return multiNameText.getText();
    }
    @FXML
    String getMultiIPAddrText() {
        return multiIPAddrText.getText();
    }

    public void changeSceneToCanvas(ActionEvent event,GameType gameType,double singleTimer) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("canvasDrawer.fxml"));
        Parent canvasDrawerParent =loader.load();

        Scene canvasDrawerScene = new Scene(canvasDrawerParent, 700, 510);
        this.drawerController = loader.getController();
        //ha a fekete ki van jelölve
        boolean b = startingColorTG.getToggles().get(1).isSelected();

        int diff = 0;
        if(difficultyTG.getToggles().get(0).isSelected())
            diff = 0;
        else if (difficultyTG.getToggles().get(1).isSelected())
            diff = 1;
        else
            diff = 2;


        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        if(gameType == SINGLE)
            drawerController.initDrawerControllerSingle(window.getScene(),gameType,singleTimer,b,diff); //passing menu scene so drawer controller can change back
        else if(gameType == LOCAL)
            drawerController.initDrawerControllerLocal(window.getScene(),gameType,singleTimer,b); //passing menu scene so drawer controller can change back
        else
            drawerController.initDrawerControllerOnline(window.getScene(), gameType, singleTimer, b,getMultiIPAddrText(),getMultiNameText()); //passing menu scene so drawer controller can change back

        window.setScene((canvasDrawerScene));
        window.show();


    }
    public void changeSceneToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));

        Scene guiScene = new Scene(root, 654, 460);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene((guiScene));
        window.show();
    }
}


