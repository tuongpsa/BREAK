public class Ball {
    private float x, y;       // vị trí
    private float velX, velY; // vận tốc
    private float radius; // bán kính

    public Ball(float startX, float startY, float radius, float speed) {
        this.x = startX;
        this.y = startY;
        this.radius = radius;
        this.velX = speed;
        this.velY = -speed;
    }

    public void move(float deltaTime) {
        // deltaTime là thời gian giữa các frame (giây)
        x += velX * deltaTime;
        y += velY * deltaTime;
    }

    public void reverseX() {
        velX = -velX;
    }

    public void reverseY() {
        velY = -velY;
    }

    // getter,setter
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
