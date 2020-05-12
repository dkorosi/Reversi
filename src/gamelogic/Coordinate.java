package gamelogic;

/**
 * A lépések megadásához szükséges koordinátákat megvalósító osztály.
 *
 * @author borszag
 */
public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * A megadott koordinátát egy szomszédos mezőre léptető tagfüggvény.
     *
     * @param dir A lépés iránya
     * @return a lépés során keletkezett koordinátapár
     */
    public Coordinate step(Direction dir) {
        return new Coordinate(x + dir.getShiftX(), y + dir.getShiftY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;
    }
}
