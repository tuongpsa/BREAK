package game.objects;

import java.awt.*;

public class PowerUp extends MovableObject {
    private PowerUpType type;
    private boolean active;
    private float fallSpeed = 100f;
    private float duration = 10f; // thời gian hiệu lực (giây)
    private float timeRemaining;
    private static final float DEFAULT_SIZE = 30;

    public PowerUp(float x, float y, PowerUpType type) {
        super(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
        this.type = type;
        this.velY = fallSpeed;
        this.active = true;
        this.timeRemaining = duration;
    }

    @Override
    public void update(float deltaTime) {
        if (active) {
            move(deltaTime);
        }
        updateDuration(deltaTime);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)width, (int)height);
    }

    // PowerUp-specific methods
    public void activate() {
        active = false;
    }

    public void updateDuration(float deltaTime) {
        if (timeRemaining > 0) {
            timeRemaining -= deltaTime;
        }
    }

    public boolean isExpired() {
        return timeRemaining <= 0;
    }

    public boolean isActive() {
        return active;
    }

    public PowerUpType getType() {
        return type;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }
}
