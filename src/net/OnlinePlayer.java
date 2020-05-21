package net;

import gamelogic.Board;
import gamelogic.Player;
import gamelogic.TileType;

public class OnlinePlayer extends Player {

    public OnlinePlayer(String name, TileType color, int timerSeconds) {
        super(name, color, timerSeconds, false);
    }

    @Override
    public void makeMove(Board board) {
        Object changedTiles = board.makeMoveAt(getColor(), getNextMove());
        // Ha helyes helyre léptünk, elindítjuk a timert (csak az első lépésnél kötelező)
        if (changedTiles != null)
            setMoved();
    }
}
