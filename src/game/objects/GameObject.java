package game.objects;

import java.awt.*;

/**
 * Base class cho tất cả các object trong game
 * Chứa các thuộc tính và phương thức chung
 */
public abstract class GameObject {
    protected float x, y;
    protected float width, height;
    
    public GameObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    // Abstract methods - các class con phải implement
    public abstract void update(float deltaTime);
    public abstract Rectangle getBounds();
    
    // Common getters/setters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setWidth(float width) { this.width = width; }
    public void setHeight(float height) { this.height = height; }
}
