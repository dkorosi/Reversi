package gamelogic;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Ez az osztály tartalmazza a játéktábla belső állapotát.
 *
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
    private boolean active = true;
    /**
     * Az aktuális körben lépést végrehajtó játékos.
     */
    private TileType current = TileType.DARK;

    private List<List<TileType>> board;
    private List<PossibleMove> validMoves = new ArrayList<>();

    /**
     * Inicializálja a táblát, ami áll a kezdő korongok elhelyezéséből, és a sötét korongokkal játszó játékos
     * kiválasztásából, ezzel eldöntve, hogy kié a kezdő lépés. Meghatározza továbbá az érvényes első lépéseket is.
     *
     * @param width  A játéktábla szélessége. (Páros szám)
     * @param height A játéktábla magassága (Páratlan szám)
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new ArrayList<>();
        for (int y = 0; y < this.height; y++) {
            List<TileType> emptyRow = new ArrayList<>();
            for (int x = 0; x < this.width; x++) {
                emptyRow.add(TileType.EMPTY);
            }
            this.board.add(emptyRow);
        }
        setTile(TileType.LIGHT, this.width / 2 - 1, this.height / 2 - 1);
        setTile(TileType.LIGHT, this.width / 2, this.height / 2);
        setTile(TileType.DARK, this.width / 2 - 1, this.height / 2);
        setTile(TileType.DARK, this.width / 2, this.height / 2 - 1);

        calculateValidMoves(current);
    }

    /**
     * Lemásol egy játékállapotot
     *
     * @param board   másik tábla
     * @param current következő játékos
     */
    public Board(List<List<TileType>> board, TileType current) {
        this.current = current;
        this.height = board.size();
        this.width = board.get(0).size();

        this.board = new ArrayList<>();
        for (int y = 0; y < this.height; y++) {
            List<TileType> row = new ArrayList<>();
            for (int x = 0; x < this.width; x++) {
                row.add(board.get(y).get(x));
            }
            this.board.add(row);
        }
        calculateValidMoves(current);
    }

    /**
     * A lépési szabályokat ellenőrző osztály.
     *
     * @author borszag
     */
    private class PossibleMove {
        private Coordinate pos;
        /**
         * A megadott koordinátához tartozó lépés érvényessége.
         */
        private boolean valid;
        /**
         * Az egyes irányokban közrefogható ellenséges korongok.
         */
        private EnumMap<Direction, List<Coordinate>> enemyTiles = new EnumMap<>(Direction.class);

        public PossibleMove(int x, int y) {
            this.pos = new Coordinate(x, y);
        }

        /**
         * Egy lehetséges lépési koordináta esetén meghatározza annak érvényességét, illetve meghatározza a közrefogható
         * ellenséges korongok számát az irány függvényében.
         * @param current
         */
        private void countReversible(TileType current) {
            this.valid = false;
            if (!isInBounds(pos) || TileType.EMPTY != getTile(pos)) {
                return;
            }
            for (Direction dir : Direction.values()) {
                Coordinate temp = pos.step(dir);

                List<Coordinate> enemyCoordinates = new ArrayList<>();

                while (isInBounds(temp) && getTile(temp) == current.enemyTileType()) {
                    enemyCoordinates.add(temp);
                    temp = temp.step(dir);
                }

                if (enemyCoordinates.isEmpty() || !isInBounds(temp) || getTile(temp) != current) {
                    this.enemyTiles.put(dir, new ArrayList<>());
                } else {
                    this.enemyTiles.put(dir, enemyCoordinates);
                    this.valid = true;
                }
            }
        }

        public boolean isValid() {
            return valid;
        }
    }

    /**
     * Ez a tagfüggvény megadja az érvényes lépések koordinátáinak a listáját.
     *
     * @return Az érvényes lépések koordinátáinak listája.
     */
    public List<Coordinate> getValidCoordinates() {
        return validMoves.stream().map((move) -> move.pos).collect(Collectors.toList());
    }

    public boolean isValidMove(Coordinate pos) {
        for (PossibleMove move : this.validMoves) {
            if (pos.equals(move.pos)) {
                return true;
            }
        }

        return false;
    }

    public List<List<TileType>> getBoard() {
        return board;
    }

    private void setTile(TileType newTileType, int x, int y) {
        this.board.get(x).set(y, newTileType);
    }

    private void setTile(TileType newTileType, Coordinate pos) {
        this.board.get(pos.getX()).set(pos.getY(), newTileType);
    }

    public TileType getTile(Coordinate pos) {
        if (!isInBounds(pos)) return TileType.EMPTY;
        return this.board.get(pos.getX()).get(pos.getY());
    }

    private boolean isInBounds(Coordinate pos) {
        return 0 <= pos.getX() && pos.getX() < width && 0 <= pos.getY() && pos.getY() < height;
    }

    private void calculateValidMoves(TileType current) {
        this.validMoves.clear();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                PossibleMove move = new PossibleMove(i, j);
                move.countReversible(current);
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

    public TileType getCurrent() {
        return current;
    }

    public void makeMoveAt(TileType player, Coordinate pos) {
        if (player != current) {
            return;
        }

        PossibleMove currentMove = getPossibleMoveByCoordinates(pos);
        if (null == currentMove)
            return;

        setTile(player, pos);
        for (Direction dir : Direction.values()) {
            List<Coordinate> flipPositions = currentMove.enemyTiles.get(dir);
            for (Coordinate coordinate : flipPositions) {
                setTile(player, coordinate);
            }
        }

        TileType temp = current.enemyTileType();
        calculateValidMoves(temp);
        if (validMoves.isEmpty()) {
            temp = temp.enemyTileType();
            calculateValidMoves(temp);
            if (validMoves.isEmpty()) {
                active = false;
                TileType winner = getWinning();
            }
        }
        current = temp;
    }

    private PossibleMove getPossibleMoveByCoordinates(Coordinate pos) {
        for (PossibleMove move : validMoves) {
            if (pos.equals(move.pos)) {
                return move;
            }
        }
        return null;
    }

    /**
     * Megállapítja ki áll nyerésre
     *
     * @return
     */
    public TileType getWinning() {
        int lightCount = 0;
        int darkCount = 0;
        for (List<TileType> row : board) {
            for (TileType tile : row) {
                switch (tile) {
                    case DARK:
                        ++darkCount;
                        break;
                    case LIGHT:
                        ++lightCount;
                        break;
                }
            }
        }
        if (lightCount > darkCount)
            return TileType.LIGHT;
        else if (darkCount > lightCount)
            return TileType.DARK;
        else
            return TileType.EMPTY;
    }
}

