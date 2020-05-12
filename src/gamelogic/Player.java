package gamelogic;


public abstract class Player {
    private String name;
    private TileType color;
    private double timer;
    private Coordinate nextMove;

    public Player(String name, TileType color, double timer) {
        this.name = name;
        this.color = color;
        this.timer = timer;
    }

    public double getTimer() {
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
}
