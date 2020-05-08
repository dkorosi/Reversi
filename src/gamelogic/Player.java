package gamelogic;

/**
 *
 */
public abstract class Player {
    private String name;
    private TileType color;
    private int timer;
    private Board board;
    private Coordinate nextMove;

    public Player(String name, Board board, TileType color, int timer) {
        this.name = name;
        this.board = board;
        this.color = color;
        this.timer = timer;
    }

    public Integer getTimer() {
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

    public Board getBoard() {
        return board;
    }

    public abstract void makeMove();
}
