package game.objects;

public class Paddle {
    private float x, y; //vị trí ban đầu
    private float width, height; //chiều dài,chiều cao ban đầu
    private float speed = 400.f; // tốc độ thanh đỡ ban đầu

    public Paddle(float startX, float startY, float width, float height) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
    }

    // paddle di chuyển trái phải
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


    // getter,setter
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

    public void setX(float x) {
        this.x = x;
    }

    public void setWidth(float v) {
        this.width = v;
    }
}
