package game.objects;

import java.awt.*;

public class Ball extends MovableObject {
    private float radius;
    private int n;

    public Ball(float x, float y, float radius, float speed) {
        super(x, y, radius * 2, radius * 2); // width = height = diameter
        this.radius = radius;
        this.velX = speed;
        this.velY = speed;
    }

    @Override
    public void update(float deltaTime) {
        move(deltaTime);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));
    }

    // Ball-specific methods
    public float getRadius() { return radius; }
    public void setN(int n) { this.n = n; }
    public int getN() { return n; }
}
