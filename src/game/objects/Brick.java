package game.objects;

/**
 * Lớp Brick (viên gạch) kế thừa từ GameObject.
 * Nó chỉ quản lý các thuộc tính riêng của mình là Máu (hp)
 * và Trạng thái (destroyed).
 */
public class Brick extends GameObject {
    //  kích thước mặc định của gạch
    public static final int DEFAULT_WIDTH = 42;
    public static final int DEFAULT_HEIGHT = 20;

    private int hp; // Máu của gạch
    private boolean destroyed; // Trạng thái đã bị phá hủy hay chưa

    /**
     * Constructor (hàm dựng) cho Brick.
     * Nhận vào vị trí (x, y) và máu (hp).
     */
    public Brick(float x, float y, int hp) {

        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        this.hp = hp;
        this.destroyed = false; // Gạch mới tạo chưa bị phá hủy
    }

    @Override
    public void update(float deltaTime) {}

    public void hit() {
        if (destroyed) return;

        this.hp--;
        if (this.hp <= 0) {
            this.destroyed = true;
        }
    }

    public boolean isDestroyed() {
        return this.destroyed;
    }

}