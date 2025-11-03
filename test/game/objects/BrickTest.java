package game.objects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// Đảm bảo bạn import đúng class Brick của mình
import game.objects.Brick;

class BrickTest {

    // Test case 1: Kiểm tra xem gạch có bị trừ máu khi bị bắn
    @Test
    void testBrickHit_ShouldReduceHp() {
        // Tạo 1 viên gạch với 3 HP
        Brick brick = new Brick(0, 0, 3);

        // hit 1 hit
        brick.hit();

        // Kiểm tra xem HP CÓ BẰNG 2 không
        assertEquals(2, brick.getHp());
        // Kiểm tra xem gạch CHƯA bị phá hủy
        assertFalse(brick.isDestroyed());
    }

    // Test case 2: Kiểm tra xem gạch có bị phá hủy khi hết máu
    @Test
    void testBrickDestroyed_WhenHPIsZero() {
        // Tạo 1 viên gạch với 1 HP
        Brick brick = new Brick(0, 0, 1);

        // hit 1 hit (HP về 0)
        brick.hit();

        // Kiểm tra xem HP CÓ BẰNG 0 không
        assertEquals(0, brick.getHp());
        // Kiểm tra xem gạch ĐÃ bị phá hủy
        assertTrue(brick.isDestroyed());
    }
}