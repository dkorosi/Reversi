package gamelogic;

import gui.Drawer;
import gui.MyTimer;
import javafx.scene.canvas.Canvas;

public class GameLoop implements Runnable {

    private Drawer drawer;
    private Board board;
    private Player currentPlayer;
    private Player opponentPlayer;
    private MyTimer timer;

    public GameLoop(Canvas canvas, int difficulty, boolean black,int time) { //0 -1 -2 a nehézség, melyik színel akar kezdeni
        this.board = new Board(8, 8);
        Player one = new LocalPlayer("Black",this.board , TileType.DARK, 0);
        Player two = new LocalPlayer("White", this.board, TileType.LIGHT, 0);

        currentPlayer = one;
        opponentPlayer = two;
        this.drawer = new Drawer(canvas, board);



            this.timer = new MyTimer(time);

            Thread timer_th = new Thread(this.timer);
            timer_th.start();

    }

    public GameLoop(Canvas canvas,boolean black,int time) { //Lokális játékokhoz
        this.board = new Board(8, 8);
        Player one = new LocalPlayer("Black",this.board , TileType.DARK, 0);
        Player two = new LocalPlayer("White", this.board, TileType.LIGHT, 0);

        currentPlayer = one;
        opponentPlayer = two;
        this.drawer = new Drawer(canvas, board);



            this.timer = new MyTimer(time);

            Thread timer_th = new Thread(this.timer);
            timer_th.start();

    }


    public GameLoop(Canvas canvas,boolean black, String name,String IP,int time) { //Online
        this.board = new Board(8, 8);
        Player one = new LocalPlayer("Black",this.board , TileType.DARK, 0);
        Player two = new LocalPlayer("White", this.board, TileType.LIGHT, 0);

        currentPlayer = one;
        opponentPlayer = two;
        this.drawer = new Drawer(canvas, board);



            this.timer = new MyTimer(time);

            Thread timer_th = new Thread(this.timer);
            timer_th.start();


    }

    public Drawer getDrawer() {
        return drawer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
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


    public int getTimerVal() {
        return timer.getTime();
    }

    public void setTimerVal(int time) {
        this.timer.setTime(time);
    }
    public void stopTimer() {
        this.timer.stop();
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
