public class Ball {
    private float x, y;
    private float velX, velY;
    private float radius;

    public Ball(float x, float y, float radius, float speed) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velX = speed;
        this.velY = -speed;
    }

    public void move(float deltaTime) {
        x += velX * deltaTime;
        y += velY * deltaTime;
    }

    public void reverseX() { velX = -velX; }
    public void reverseY() { velY = -velY; }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getVelX() { return velX; }
    public float getVelY() { return velY; }
    public float getRadius() { return radius; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setVelX(float velX) { this.velX = velX; }
    public void setVelY(float velY) { this.velY = velY; }
}
