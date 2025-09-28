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
    public void moveLeft(float deltaTime) {
        x -= speed * deltaTime;
    }

    public void moveRight(float deltaTime) {
        x += speed * deltaTime;
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
}
