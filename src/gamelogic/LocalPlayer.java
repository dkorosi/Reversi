package gamelogic;


public class LocalPlayer extends Player {

    public LocalPlayer(String name, TileType color, int timerSeconds) {
        super(name, color, timerSeconds, true);

    }

    @Override
    public void makeMove(Board board) {
        Object changedTiles = board.makeMoveAt(this.getColor(), getNextMove());
        // Ha helyes helyre léptünk, elindítjuk a timert (csak az első lépésnél kötelező)
        if (changedTiles != null)
            setMoved();
    }
}
