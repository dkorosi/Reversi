package gamelogic;

/**
 * Ez az enumeráció egy mező szomszédainak égtájak szerinti megadásához szükséges. A tábla (0,0) mezője a bal felső
 * sarokban van.
 *
 * @author borszag
 */
public enum Direction {
    N(0, -1),
    NE(1, -1),
    E(1, 0),
    SE(1, 1),
    S(0, 1),
    SW(-1, 1),
    W(-1, 0),
    NW(-1, -1);

    private final int shiftX, shiftY;

    Direction(int xShift, int yShift) {
        this.shiftX = xShift;
        this.shiftY = yShift;
    }

    public int getShiftX() {
        return shiftX;
    }

    public int getShiftY() {
        return shiftY;
    }
}