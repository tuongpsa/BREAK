package game.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    // Khai báo một đối tượng Game để dùng chung
    private Game game;

    // Hàm này sẽ tự động chạy TRƯỚC MỖI @Test
    // để đảm bảo chúng ta luôn có 1 đối tượng Game mới tinh.
    @BeforeEach
    void setUp() {
        // Tạo một Game mới. (null là vì chúng ta không test âm thanh)
        game = new Game(null);
    }

    /**
     * Test case 1: Kiểm tra cộng điểm cơ bản.
     */
    @Test
    void testAddScore_ShouldIncreaseScore() {
        game.addScore(50); // Cộng 50 điểm

        // Kiểm tra xem điểm có ĐÚNG BẰNG 50 không
        assertEquals(50, game.getScore());

        game.addScore(20); // Cộng thêm 20 điểm

        // Kiểm tra xem điểm có ĐÚNG BẰNG 70 không (50 + 20)
        assertEquals(70, game.getScore());
    }

    /**
     * Test case 2: Kiểm tra logic nhân điểm (Score Multiplier).
     */
    @Test
    void testAddScore_WithScoreMultiplier() {
        game.activateScoreMultiplier(); // Kích hoạt power-up x2 điểm

        game.addScore(50); // Cộng 50 điểm

        // Điểm phải là 100 (vì 50 * 2)
        assertEquals(100, game.getScore());
    }
}