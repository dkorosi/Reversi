package gamelogic;


public abstract class Player {
    private String name;
    private boolean isHuman; // Külső játékos-e vagy mi hívjuk a move függvényt
    private TileType color;
    private long timer;
    private Coordinate nextMove;

    public Player(String name, TileType color, long timer, boolean isHuman) {
        if (timer == 0) {
            timer = Long.MAX_VALUE;
        }
        this.name = name;
        this.color = color;
        this.timer = timer;
        this.isHuman = isHuman;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long absTime) {
        this.timer = absTime;
    }

    public void incrementTimer(long relTime) {
        this.timer += relTime;
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

    public void setNextMove(Coordinate nextMove) {
        this.nextMove = nextMove;
    }

    public abstract void makeMove(Board board);

    public boolean isHuman() {
        return isHuman;
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
}
