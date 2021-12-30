package game;

public class Pig {
    private int x;
    private int y;

    public Pig() {
        this.x = (int) (Math.random() * Room.game.getWidth());
        this.y = (int) (Math.random() * Room.game.getHeight());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
