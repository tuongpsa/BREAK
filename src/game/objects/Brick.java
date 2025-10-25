package game.objects;

import java.awt.*;

public class Brick extends GameObject {
    private int hp;
    private boolean destroyed;
    private static final int DEFAULT_WIDTH = 42;
    private static final int DEFAULT_HEIGHT = 20;

    public Brick(float x, float y, int hp) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.hp = hp;
        this.destroyed = false;
    }

    @Override
    public void update(float deltaTime) {
        // Brick không cần update gì
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)width, (int)height);
    }

    // Brick-specific methods
    public void hit() {
        hp--;
        if (hp <= 0) destroyed = true;
    }

    public boolean isDestroyed() { return destroyed; }
    public int getHp() { return hp; }
}
