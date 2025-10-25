package game.objects;

/**
 * Base class cho các object có thể di chuyển
 * Kế thừa từ GameObject và thêm velocity
 */
public abstract class MovableObject extends GameObject {
    protected float velX, velY;
    
    public MovableObject(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.velX = 0;
        this.velY = 0;
    }
    
    public MovableObject(float x, float y, float width, float height, float velX, float velY) {
        super(x, y, width, height);
        this.velX = velX;
        this.velY = velY;
    }
    
    // Common movement methods
    public void move(float deltaTime) {
        x += velX * deltaTime;
        y += velY * deltaTime;
    }
    
    public void reverseX() { velX = -velX; }
    public void reverseY() { velY = -velY; }
    
    // Velocity getters/setters
    public float getVelX() { return velX; }
    public float getVelY() { return velY; }
    public void setVelX(float velX) { this.velX = velX; }
    public void setVelY(float velY) { this.velY = velY; }
}
