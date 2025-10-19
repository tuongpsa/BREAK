import java.awt.Rectangle;

public class PowerUp {
    private float x, y;
    private float velY;
    private PowerUpType type;
    private boolean active;
    private float width = 30;
    private float height = 30;
    private float fallSpeed = 100f;
    private float duration = 10f; // thời gian hiệu lực (giây)
    private float timeRemaining;

    public PowerUp(float x, float y, PowerUpType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.velY = fallSpeed;
        this.active = true;
        this.timeRemaining = duration;
    }

    public void update(float deltaTime) {
        if (active) {
            y += velY * deltaTime;
        }
    }

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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)width, (int)height);
    }
}
