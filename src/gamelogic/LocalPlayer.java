package gamelogic;


public class LocalPlayer extends Player {
    private Coordinate nextMove;

    public LocalPlayer(String name, Board board, TileType color, int timer) {
        super(name, board, color, timer);
        nextMove = new Coordinate(-1, -1);
    }

    public Coordinate getNextMove() {
        return nextMove;
    }

    public void setNextMove(Coordinate nextStep) {
        this.nextMove = nextStep;
    }

    @Override
    public void makeMove() {
        this.getBoard().makeMoveAt(this.getColor(), this.nextMove);
    }
}
