import java.awt.Rectangle;

public class Brick {
    private float x, y;
    private int hp;
    private boolean destroyed;
    private int width = 60;
    private int height = 20;

    public Brick(float x, float y, int hp) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.destroyed = false;
    }

    public void hit() {
        hp--;
        if (hp <= 0) destroyed = true;
    }

    public boolean isDestroyed() { return destroyed; }

    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
