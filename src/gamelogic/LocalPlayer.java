package gamelogic;


public class LocalPlayer extends Player {
    private Coordinate nextMove;

    public LocalPlayer(String name, TileType color, int timer) {
        super(name, color, timer, true);
        nextMove = new Coordinate(-1, -1);
    }

    public Coordinate getNextMove() {
        return nextMove;
    }

    public void setNextMove(Coordinate nextMove) {
        this.nextMove = nextMove;
    }

    @Override
    public void makeMove(Board board) {
        board.makeMoveAt(this.getColor(), this.nextMove);
    }
}
