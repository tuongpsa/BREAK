package game.objects;

import java.awt.*;

public class Paddle extends GameObject {
    private float speed = 400.f; // tốc độ thanh đỡ ban đầu

    public Paddle(float startX, float startY, float width, float height) {
        super(startX, startY, width, height);
    }

    @Override
    public void update(float deltaTime) {
        // Paddle update được xử lý bởi moveLeft/moveRight
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)width, (int)height);
    }

    // Paddle-specific methods
    public void moveLeft(float deltaTime, float screenWidth) {
        x -= speed * deltaTime;
        if (x < 0) {
            x = 0; // không cho vượt ra ngoài bên trái
        }
    }

    public void moveRight(float deltaTime, float screenWidth) {
        x += speed * deltaTime;
        if (x + width > screenWidth) {
            x = screenWidth - width; // không cho vượt ra ngoài bên phải
        }
    }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
}
