package gamelogic;


import java.io.Serializable;

public abstract class Player implements Serializable {
    private String name;
    private boolean isLocal; // Külső játékos-e vagy mi hívjuk a move függvényt
    private TileType color;
    private long timer;
    private Coordinate nextMove;
    private boolean moved = false;

    public Player(String name, TileType color, long timer, boolean isLocal) {
        if (timer == 0) {
            this.timer = Long.MAX_VALUE;
        } else {
            this.timer = timer * 1000;
        }
        this.name = name;
        this.color = color;
        this.isLocal = isLocal;
        nextMove = new Coordinate(-1,-1);
    }

    public boolean isTimeRunOut() {
        return timer <= 0;
    }

    public void decrementTimer(long relTime) {
        this.timer -= relTime;
    }

    public TileType getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public Coordinate getNextMove() {
        return nextMove;
    }

    /**
     * Üzenet string alapján beállítja a következő lépést, lokális és online játékosnál is használjuk
     * @param nextMove üzenet (move;x;y)
     * @return mindig false-t ad vissza, mert a broker event listener-jéből nem vesszük ki
     */
    public Boolean setNextMove(String nextMove) {
        String[] split = nextMove.split(";");
        this.nextMove = new Coordinate(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        return false;
    }

    public abstract void makeMove(Board board);

    public boolean isLocal() {
        return isLocal;
    }

    public String getTimerString() {
        if (timer > Long.MAX_VALUE / 2) {
            return "\u221E";
        }

        int timerInSeconds = (int) (timer / 1000);
        int min = timerInSeconds / 60;
        int sec = timerInSeconds - min * 60;


        String plusSec;
        String plusMin;

        if (sec < 10)
            plusSec = "0";
        else
            plusSec = "";

        if (min < 10)
            plusMin = "0";
        else
            plusMin = "";

        return plusMin + min + ":" + plusSec + sec;
    }

    /**
     * Ha már lépett akkor igaz, ha nem, akkor hamis
     * @return lépett-e már
     */
    public boolean hasMoved(){
        return moved;
    }

    public void setMoved() {
        this.moved = true;
    }
}
