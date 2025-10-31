package game.objects;

import java.awt.Rectangle;

public class Bullet extends GameObject {
    private float velY;

    public Bullet(float x, float y, float width, float height, float speedY) {
        super(x, y, width, height);
        this.velY = -Math.abs(speedY);
    }

    @Override
    public void update(float deltaTime) {
        this.y += velY * deltaTime;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)width, (int)height);
    }
}


