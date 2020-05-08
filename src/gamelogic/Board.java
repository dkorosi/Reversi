package gamelogic;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Ez az osztály tartalmazza a játéktábla belső állapotát.
 * @author borszag
 */
public class Board {
    /**
     * A játéktábla magassága.
     */
    private int width;
    /**
     * A  játéktábla szélessége.
     */
    private int height;
    /**
     * Jelzi, ha vége a játéknak, azaz nem tehető több lépés.
     */
    private boolean active;
    /**
     * Az aktuális körben az ellenfél játékos szín.
     */
    private TileType opponent;
    /**
     * Az aktuális körben lépést végrehajtó játékos.
     */
    private TileType current;

    private ArrayList<ArrayList<TileType>> board;
    private ArrayList<PossibleMove> validMoves;
    /**
     * A lépési szabályokat ellenőrző osztály.
     * @author borszag
     */
    private class PossibleMove {
        private Coordinate pos;
        /**
         * A megadott koordinátához tartozó lépés érvényessége.
         */
        private boolean valid;
        /**
         * Az egyes irányokban közrefogható ellenséges korongok száma.
         */
        private EnumMap<Direction, Integer> tileCount = new EnumMap<>(Direction.class);

        public PossibleMove(int x, int y) {
            this.pos = new Coordinate(x,y);
            this.countReversible();
        }

        public PossibleMove(Coordinate pos) {
            this.pos = new Coordinate(pos);
            this.countReversible();
        }

        /**
         * Egy lehetséges lépési koordináta esetén meghatározza annak érvényességét, illetve meghatározza a közrefogható
         * ellenséges korongok számát az irány függvényében.
         */
        private void countReversible() {
            this.valid = false;
            if (!isValidPos(this.pos) || TileType.EMPTY != getTile(this.pos)) {
                return;
            }
            for (Direction dir : Direction.values()) {
                int oppCount = -1;
                Coordinate pos = new Coordinate(this.pos);

                do {
                    oppCount++;
                    pos.step(dir);
                } while (isValidPos(pos) && getTile(pos) == opponent);

                if (0 >= oppCount || !isValidPos(pos) || getTile(pos) != current) {
                    this.tileCount.put(dir,0);
                } else {
                    this.tileCount.put(dir,oppCount);
                    this.valid = true;
                }
            }
        }

        public boolean isValid() {
            return valid;
        }
    }

    /**
     * Inicializálja a táblát, ami áll a kezdő korongok elhelyezéséből, és a sötét korongokkal játszó játékos
     * kiválasztásából, ezzel eldöntve, hogy kié a kezdő lépés. Meghatározza továbbá az érvényes első lépéseket is.
     * @param width A játéktábla szélessége. (Páros szám)
     * @param height A játéktábla magassága (Páratlan szám)
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.active = true;
        this.current = TileType.DARK;
        this.opponent = TileType.LIGHT;
        this.board = new ArrayList<>();
        this.validMoves = new ArrayList<>();
        for (int y = 0; y < this.height; y++) {
            ArrayList<TileType> emptyRow = new ArrayList<>();
            for (int x = 0; x < this.width; x++) {
                emptyRow.add(TileType.EMPTY);
            }
            this.board.add(emptyRow);
        }
        setTile(TileType.LIGHT, this.width/2-1, this.height/2-1);
        setTile(TileType.LIGHT, this.width/2, this.height/2);
        setTile(TileType.DARK,  this.width/2-1, this.height/2);
        setTile(TileType.DARK,  this.width/2, this.height/2-1);

        getValidMoves();
    }

    /**
     * Ez a tagfüggvény megadja az érvényes lépések listáját. Ennek a használata ajánlott minden lépés előtt.
     * @return Az érvényes lépések koordinátáinak listája.
     */
    public ArrayList<Coordinate> getValidCoordinates() {
        ArrayList<Coordinate> validCoordinates = new ArrayList<>();
        for (PossibleMove move : this.validMoves) {
            if (move.isValid()) {
                validCoordinates.add(move.pos);
            }
        }
        return validCoordinates;
    }

    public boolean isValidMove(Coordinate pos) {
        boolean valid = false;
        for (PossibleMove move : this.validMoves) {
            if (pos.equals(move.pos)) {
                valid = true;
                break;
            }
        }
        return valid;
    }

    public ArrayList<ArrayList<TileType>> getBoard() {
        return board;
    }

    private void setTile(TileType newTileType, Integer x, Integer y) {
        this.board.get(x).set(y, newTileType);
    }

    private void setTile(TileType newTileType, Coordinate pos) {
        this.board.get(pos.getX()).set(pos.getY(), newTileType);
    }

    public TileType getTile(Coordinate move) {
        if (!isValidPos(move)) return TileType.EMPTY;
        return this.board.get(move.getX()).get(move.getY());
    }

    private boolean isValidPos(Coordinate pos) {
        return 0 <= pos.getX() && pos.getX() < width && 0 <= pos.getY() && pos.getY() < height;
    }

    private void getValidMoves() {
        this.validMoves.clear();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                PossibleMove move = new PossibleMove(i,j);
                if (move.isValid()) {
                    this.validMoves.add(move);
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TileType getOpponent() {
        return opponent;
    }

    public TileType getCurrent() {
        return current;
    }

    public void makeMoveAt(TileType player, Coordinate pos) {
        if (player != current) {
            return;
        }

        PossibleMove move = new PossibleMove(pos);
        if (!move.isValid()) {
            return;
        }

        for (Direction dir : Direction.values()) {
            Coordinate flipPos = new Coordinate(move.pos);
            for (int i = 0; i <= move.tileCount.get(dir); i++) {
                setTile(current, flipPos);
                flipPos.step(dir);
            }
        }

        TileType temp = current;
        current = opponent;
        opponent = temp;
        getValidMoves();
        if (0 == validMoves.size()) {
            temp = current;
            current = opponent;
            opponent = temp;
            getValidMoves();
            if (0 == validMoves.size()) {
                active = false;
            }
        }
    }
}

