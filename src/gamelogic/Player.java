package gamelogic;

public class Player {
    private String name;
    private TileType color;
    private Integer timer;

    public Player(String name, TileType color, Integer timer) {
        this.name = name;
        this.color = color;
        this.timer = timer;
    }

    public Integer getTimer() {
        return timer;
    }

    public TileType getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
