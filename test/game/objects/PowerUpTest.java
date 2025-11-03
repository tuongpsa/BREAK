package game.objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import game.objects.PowerUp;
import game.objects.PowerUpType;

class PowerUpTest {

    @Test
    void testPowerUpUpdate_ShouldMoveDown() {
        // Tạo power-up ở y = 100
        PowerUp powerUp = new PowerUp(50, 100, PowerUpType.MULTI_BALL);
        float deltaTime = 0.5f; // Giả sử 0.5 giây trôi qua
        // Tốc độ rơi là 100f

        powerUp.update(deltaTime); // Cập nhật vị trí

        // Y_mới = Y_cũ + (tốc độ * thời gian)
        // Y_mới = 100 + (100f * 0.5f) = 100 + 50 = 150
        assertEquals(150.0f, powerUp.getY());
    }

    @Test
    void testPowerUpDuration_ShouldExpireAfterTime() {
        // Loại này có duration = 15 giây
        PowerUp powerUp = new PowerUp(0, 0, PowerUpType.SCORE_MULTIPLIER);

        powerUp.updateDuration(10.0f); // Trôi qua 10 giây

        // Kiểm tra chưa hết hạn
        assertFalse(powerUp.isExpired(), "trôi qua 10s,chưa thể hết hạn");

        powerUp.updateDuration(5.0f); // Trôi qua thêm 5 giây (tổng 15s)

        // Kiểm tra đã hết hạn
        assertTrue(powerUp.isExpired(), "trôi qua tổng 15s,đã hết hạn");
    }

    @Test
    void testConstructor_ShouldSetCorrectProperties_ForScoreMultiplier() {
        // Tạo 1 power-up loại SCORE_MULTIPLIER
        PowerUp powerUp = new PowerUp(0, 0, PowerUpType.SCORE_MULTIPLIER);

        // Duration của loại này là 15f
        assertEquals(15.0f, powerUp.getTimeRemaining());
        // Multiplier của loại này là 2.0f
        assertEquals(2.0f, powerUp.getMultiplier());
    }

    @Test
    void testConstructor_ShouldSetCorrectProperties_ForMultiBall() {
        // Tạo 1 power-up loại MULTI_BALL
        PowerUp powerUp = new PowerUp(0, 0, PowerUpType.MULTI_BALL);

        assertEquals(20.0f, powerUp.getTimeRemaining());
        // Số bóng thêm là 2
        assertEquals(2, powerUp.getBallCount());
    }

    @Test
    void testConstructor_ShouldSetCorrectProperties_ForShield() {
        // Tạo 1 power-up loại SHIELD
        PowerUp powerUp = new PowerUp(0, 0, PowerUpType.SHIELD);

        // Duration của loại này là 0f (hiệu ứng tức thời)
        assertEquals(0f, powerUp.getTimeRemaining());
        // Số mạng là 1
        assertEquals(1, powerUp.getShieldLives());
    }
}