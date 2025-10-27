package game.objects;

/**
 * Lớp Ball (quả bóng) kế thừa từ GameObject.
 * Nó quản lý logic di chuyển và vận tốc của chính nó.
 */
public class Ball extends GameObject {

    // --- Thuộc tính riêng của Ball ---
    private float radius; // Bán kính (dùng để vẽ và tính toán)
    private float velX;   // Vận tốc ngang
    private float velY;   // Vận tốc dọc

    /**
     * Constructor (hàm dựng) cho Ball.
     * @param x Vị trí ban đầu X
     * @param y Vị trí ban đầu Y
     * @param radius Bán kính của bóng
     * @param initialSpeed Tốc độ gốc ban đầu (chỉ dùng cho Y)
     */
    public Ball(float x, float y, float radius, float initialSpeed) {

        // CONSTRUCTOR CỦA LỚP CHA (GameObject)
        super(x, y, radius * 2, radius * 2);

        this.radius = radius;

        this.velX = 50.0f;
        this.velY = -Math.abs(initialSpeed); // Đi LÊN
    }


    @Override
    public void update(float deltaTime) {
        this.x += this.velX * deltaTime;
        this.y += this.velY * deltaTime;

    }


    public float getRadius() {
        return this.radius;
    }

    public float getVelX() { return this.velX; }
    public void setVelX(float velX) { this.velX = velX; }

    public float getVelY() { return this.velY; }
    public void setVelY(float velY) { this.velY = velY; }

    /**
     * Đảo ngược vận tốc Y khi va chạm Paddle hoặc Tường trên/dưới
     */
    public void reverseY() {
        this.velY = -this.velY;
    }

    /**
     * Đảo ngược vận tốc X khi va chạm Tường trái/phải
     */
    public void reverseX() {
        this.velX = -this.velX;
    }

}