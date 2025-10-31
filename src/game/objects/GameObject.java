package game.objects;

import java.awt.Rectangle;
public abstract class GameObject {

    protected float x, y;
    protected float width, height;

    public GameObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)width, (int)height);
    }



    /**
     * Cập nhật logic của đối tượng
     * @param deltaTime Thời gian giữa 2 frame
     */
    public abstract void update(float deltaTime);

    /**
     * Vẽ đối tượng lên màn hình
     * @param g Đối tượng GraphicsContext để vẽ
     */

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setWidth(float width) { this.width = width; }
    public void setHeight(float height) { this.height = height; }
}