package gamelogic;


public abstract class Player {
    private String name;
    private boolean outside; // Külső játékos-e vagy mi hívjuk a move függvényt
    private TileType color;
    private int timer;
    private Coordinate nextMove;

    public Player(String name, TileType color, int timer, boolean outside) {
        this.name = name;
        this.color = color;
        this.timer = timer;
        this.outside = outside;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int absTime) {
        this.timer = absTime;
    }

    public void incrementTimer(int relTime) {
        this.timer += relTime;
    }

    public void decrementTimer(int relTime) {
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

    public boolean isOutside() {
        return outside;
    }
}
