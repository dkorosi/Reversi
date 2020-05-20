package gamelogic;

import javafx.scene.paint.Color;

/**
 * Ez az enumeráció a tábla egyes mezőinek állapotát, és a játékosok által használt korongok színét is jelzi.
 *
 * @author borszag
 */
public enum TileType {
    EMPTY(0, Color.GRAY),
    DARK(1, Color.BLACK),
    LIGHT(2, Color.WHITE);

    private final int index;
    private final Color color;

    TileType(int index, Color color) {
        this.index = index;
        this.color = color;
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

    public Color getColor() {
        return color;
    }
}