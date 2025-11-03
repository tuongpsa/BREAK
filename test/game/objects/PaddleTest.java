package game.objects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import game.objects.Paddle;

class PaddleTest {

    private final float GAME_WIDTH = 480f;

    @Test
    void testPaddleMoveRight_ShouldChangeX() {
        // Tạo paddle ở giữa (x=100), rộng 80
        Paddle paddle = new Paddle(100, 100, 80, 10);
        // Giả sử 1 giây trôi qua để di chuyển được nhiều
        float deltaTime = 1.0f;

        paddle.moveRight(deltaTime, GAME_WIDTH);
        paddle.getX();
        // Vị trí X mới phải LỚN HƠN 100
        // (Vì MOVE_SPEED là 350f, nên X mới sẽ là 100 + 350 = 450)
        assertEquals(400f, paddle.getX());
    }

    @Test
    void testPaddleMoveLeft_ShouldBeBlockedByLeftWall() {
        // Đặt paddle ở sát mép trái (x=5)
        Paddle paddle = new Paddle(5, 100, 80, 10);
        float deltaTime = 1.0f; // 1 giây

        paddle.moveLeft(deltaTime, GAME_WIDTH); // Cố gắng di chuyển sang trái

        // Vị trí X phải bằng 0 (vì bị tường chặn)
        assertEquals(0, paddle.getX());
    }

    @Test
    void testPaddleMoveRight_ShouldBeBlockedByRightWall() {
        // Màn hình rộng 480, paddle rộng 80.
        // Vị trí paddle tối đa là (480 - 80) = 400.
        // Đặt paddle ở sát mép phải (x=395)
        Paddle paddle = new Paddle(395, 100, 80, 10);
        float deltaTime = 1.0f; // 1 giây

        paddle.moveRight(deltaTime, GAME_WIDTH); // Cố gắng di chuyển sang phải

        // Vị trí X phải bằng 400 (bị tường phải chặn)
        assertEquals(400, paddle.getX());
    }
}