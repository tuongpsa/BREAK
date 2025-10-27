package game.objects;

/**
 * Lớp Paddle (thanh đỡ) kế thừa từ GameObject.
 */
public class Paddle extends GameObject {

    private static final float MOVE_SPEED = 350f;

    /**
     * Constructor cho Paddle.
     */
    public Paddle(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    /**
     * Hàm update() để trống, vì gọi move() từ bên ngoài.
     */
    @Override
    public void update(float deltaTime) {
    }

    // --- SỬA ĐỔI HÀM NÀY ---
    /**
     * Di chuyển thanh đỡ sang trái.
     * @param deltaTime Thời gian giữa 2 frame
     * @param gameWidth Chiều rộng màn hình (được truyền vào nhưng không dùng)
     */
    public void moveLeft(float deltaTime, float gameWidth) {
        // di chuyển sang trái
        this.x -= MOVE_SPEED * deltaTime;

        // Kiểm tra va chạm với tường bên trái
        if (this.x < 0) {
            this.x = 0;
        }
    }

    /**
     * Di chuyển thanh đỡ sang phải, có kiểm tra va chạm tường.
     * @param deltaTime Thời gian giữa 2 frame
     * @param gameWidth Chiều rộng của màn hình game
     */
    public void moveRight(float deltaTime, float gameWidth) {
        // di chuyển sang phải
        this.x += MOVE_SPEED * deltaTime;

        // Kiểm tra va chạm với tường bên phải
        if (this.x + this.width > gameWidth) {
            this.x = gameWidth - this.width;
        }
    }
}