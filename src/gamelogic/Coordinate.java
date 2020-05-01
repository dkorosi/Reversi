package gamelogic;

/**
 * A lépések megadásához szükséges koordinátákat megvalósító osztály.
 * @author borszag
 */
public class Coordinate {
    Integer x;
    Integer y;

    public Coordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    /**
     * A megadott koordinátát egy szomszédos mezőre léptető tagfüggvény.
     * @param dir A lépés iránya
     */
    public void step(Direction dir) {
        switch (dir) {
            case N:
                this.y += 1;
                break;
            case NE:
                this.x += 1;
                this.y += 1;
                break;
            case E:
                this.x += 1;
                break;
            case SE:
                this.x += 1;
                this.y += -1;
                break;
            case S:
                this.y += -1;
                break;
            case SW:
                this.x += -1;
                this.y += -1;
                break;
            case W:
                this.x += -1;
                break;
            case NW:
                this.x += -1;
                this.y += 1;
                break;
            default:
                break;
        }
    }
}
