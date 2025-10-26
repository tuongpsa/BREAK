package game.objects;

import java.awt.*;

/**
 * PowerUp class gộp tất cả logic - không cần kế thừa phức tạp
 */
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
    
    // Các thuộc tính riêng cho từng loại power-up
    private float multiplier = 2.0f; // Cho SCORE_MULTIPLIER
    private int ballCount = 2; // Cho MULTI_BALL

    public PowerUp(float x, float y, PowerUpType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.velY = fallSpeed;
        this.active = true;
        
        // Thiết lập duration khác nhau cho từng loại
        switch (type) {
            case SCORE_MULTIPLIER:
                this.duration = 15f;
                break;
            case MULTI_BALL:
                this.duration = 20f;
                break;
            default:
                this.duration = 10f;
                break;
        }
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

    public void applyEffect() {
        // Logic áp dụng hiệu ứng dựa trên type
        switch (type) {
            case SCORE_MULTIPLIER:
                System.out.println("Score Multiplier activated! Points will be doubled for " + duration + " seconds");
                break;
            case MULTI_BALL:
                System.out.println("Multi Ball activated! " + ballCount + " additional balls will be created");
                break;
            default:
                System.out.println("Unknown power-up type activated!");
                break;
        }
    }

    public void removeEffect() {
        // Logic loại bỏ hiệu ứng dựa trên type
        switch (type) {
            case SCORE_MULTIPLIER:
                System.out.println("Score Multiplier effect ended");
                break;
            case MULTI_BALL:
                System.out.println("Multi Ball effect ended");
                break;
            default:
                System.out.println("Unknown power-up effect ended");
                break;
        }
    }

    // Getters cho các thuộc tính riêng
    public float getMultiplier() {
        return multiplier;
    }

    public int getBallCount() {
        return ballCount;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    public void setBallCount(int ballCount) {
        this.ballCount = ballCount;
    }
}
