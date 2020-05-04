package gamelogic;

public class Player {
    private String name;
    private TileType color;
    private int timer;

    public Player(String name, TileType color, int timer) {
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
