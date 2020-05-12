package gamelogic;

/**
 * Ez az enumeráció a tábla egyes mezőinek állapotát, és a játékosok által használt korongok színét is jelzi.
 *
 * @author borszag
 */
public enum TileType {
    EMPTY,
    DARK,
    LIGHT;

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
}