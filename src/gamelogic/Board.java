package gamelogic;

import javax.swing.*;
import java.util.ArrayList;
import java.util.EnumMap;


enum Color {
    NONE,
    DARK,
    LIGHT
}

enum Direction {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW
}

public class Board {
    Integer width;
    Integer height;

    ArrayList<ArrayList<Color>> board;

    private class PossibleMove {
        Move pos;
        EnumMap<Direction, Integer> tileCounts = new EnumMap<>(Direction.class);
        public PossibleMove(int x, int y) {
            this.pos = new Move(x,y);
            for (Direction dir : Direction.values()) {
                tileCounts.put(dir, 0);
            }
        }
    }

    public Board() {
        this.width = 8;
        this.height = 8;
        this.board = new ArrayList<>();
        ArrayList<Color> empty_row = new ArrayList<>();
        for (int x = 0; x < this.width; x++) {
            empty_row.add(Color.NONE);
        }
        for (int y = 0; y < this.height; y++) {
            this.board.add(empty_row);
        }
    }

    private void setTile (Color newColor, Integer x, Integer y) {
        this.board.get(x).set(y, newColor);
    }

    private PossibleMove isPossible(Move move) {
        PossibleMove ret = new PossibleMove(move.x, move.y);
        //TODO
        return ret;
    }


}
