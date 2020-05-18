package gamelogic;

/**
 * Ez az enumeráció a tábla egyes mezőinek állapotát, és a játékosok által használt korongok színét is jelzi.
 *
 * @author borszag
 */
public enum TileType {
    EMPTY(0),
    DARK(1),
    LIGHT(2);

    private final int index;

    TileType(int index) {
        this.index = index;
    }

    public TileType enemyTileType() {
        switch (this) {
            case DARK:
                return LIGHT;
            case LIGHT:
                return DARK;
            default:
                return EMPTY;
        }
    }

    public int getIndex() {
        return index;
    }
}