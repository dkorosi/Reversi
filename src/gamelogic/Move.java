package gamelogic;

public class Move {
    Integer x;
    Integer y;

    public Move(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Move step(Direction dir) {
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
        return this;
    }
}
