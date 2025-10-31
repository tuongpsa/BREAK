package game.core;

import game.audio.AudioManager;
import game.objects.*;

import java.awt.*;
import java.util.List;

public class CollisionHandler {
    private final AudioManager audioManager;

    //khai báo LevelManager
    private final LevelManager levelManager;

    //Thêm LevelManager vào constructor
    public CollisionHandler(AudioManager audioManager, LevelManager levelManager) {
        this.audioManager = audioManager;
        this.levelManager = levelManager; // Lưu lại LevelManager được chia sẻ
    }


    
    public void handleBallBrickCollision(List<Ball> balls, float deltaTime, List<Brick> bricks, Game game, Paddle paddle, List<PowerUp> powerUps) {
        float screenWidth = game.getWidth();
        float screenHeight = game.getHeight();
        
        // Xử lý từng ball
        for (int ballIndex = balls.size() - 1; ballIndex >= 0; ballIndex--) {
            Ball ball = balls.get(ballIndex);
            handleSingleBallCollision(ball, deltaTime, bricks, game, paddle, powerUps, screenWidth, screenHeight);
            
            // Kiểm tra game over - chỉ khi tất cả balls đều rơi
            if (ball.getY() >= screenHeight) {
                // Nếu có khiên, cứu bóng và bật ngược lại
                if (game.consumeShieldIfAvailable(ball, screenHeight)) {
                    continue;
                }
                balls.remove(ballIndex);
                if (balls.isEmpty()) {
                    game.setGameOver(true);
                }
            }
        }
        
        // Xử lý va chạm power-up với paddle
        handlePowerUpCollision(powerUps, paddle, game);
    }

    
    
    private void handleSingleBallCollision(Ball ball, float deltaTime, List<Brick> bricks, Game game, Paddle paddle, List<PowerUp> powerUps, float screenWidth, float screenHeight) {
        float dx = ball.getVelX() * deltaTime;
        float dy = ball.getVelY() * deltaTime;
        float radius = ball.getRadius();

        float stepSize = radius / 2.0f;
        int steps = (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy)) / stepSize);
        if (steps < 1) steps = 1;

        float stepX = dx / steps;
        float stepY = dy / steps;

        // Trái
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.setVelX(-ball.getVelX());
        }

        // Phải
        if (ball.getX() + radius * 2 >= screenWidth) {
            ball.setX(screenWidth - radius * 2);
            ball.setVelX(-ball.getVelX());
        }

        // Trên
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setVelY(-ball.getVelY());
        }

        // game.objects.Paddle
        if (ball.getY() + radius * 2 >= paddle.getY() &&
                ball.getY() + radius <= paddle.getY() + paddle.getHeight() &&
                ball.getX() + radius * 2 >= paddle.getX() &&
                ball.getX() <= paddle.getX() + paddle.getWidth()) {

            if (ball.getVelY() > 0) {
                // 1. Lấy vị trí tâm bóng và tâm thanh đỡ
                float tamBongX = ball.getX() + ball.getRadius();
                float tamThanhDoX = paddle.getX() + paddle.getWidth() / 2;

                // 2. Tính vị trí va chạm tương đối
                // (Giá trị từ -1.0 (mép trái) đến +1.0 (mép phải))
                float viTriVaChamTuongDoiX = tamBongX - tamThanhDoX;
                float viTriVaChamChuanHoaX = viTriVaChamTuongDoiX / (paddle.getWidth() / 2);

                // 3. Giới hạn giá trị, đề phòng bóng đập vào cạnh
                viTriVaChamChuanHoaX = Math.max(-1.0f, Math.min(1.0f, viTriVaChamChuanHoaX));

                // 4. Lấy TỔNG tốc độ hiện tại của bóng (để bảo toàn tốc độ)
                float tongTocDoHienTai = (float)Math.sqrt(ball.getVelX() * ball.getVelX() +
                        ball.getVelY() * ball.getVelY());

                // 5. Tính góc nảy.
                // Chúng ta sẽ đổi giá trị -1.0 đến +1.0 thành một góc,
                // ví dụ: tối đa 75 độ (khoảng 1.3 radians)
                float gocNayToiDa = 1.3f; // 75 độ
                float gocNay = viTriVaChamChuanHoaX * gocNayToiDa;

                // 6. Dùng lượng giác (Sin và Cos) để tính vanTocX và vanTocY mới
                float vanTocMoiX = tongTocDoHienTai * (float)Math.sin(gocNay);

                // vanTocY phải là SỐ ÂM (để đi lên)
                float vanTocMoiY = -tongTocDoHienTai * (float)Math.cos(gocNay);

                // 7. Cập nhật vận tốc mới cho bóng
                ball.setVelX(vanTocMoiX);
                ball.setVelY(vanTocMoiY);
                
                if (audioManager != null) {
                    audioManager.playPaddleHit();
                }
            }
        }

        for (int i = 0; i < steps; i++) {
            float nextX = ball.getX() + stepX;
            float nextY = ball.getY() + stepY;

            boolean collided = false;

            for (int j = bricks.size() - 1; j >= 0; j--) {
                Brick brick = bricks.get(j);

                if (!brick.isDestroyed()) {
                    Rectangle rect = brick.getBounds();
                    boolean hitX = nextX + radius * 2 > rect.x && nextX < rect.x + rect.width;
                    boolean hitY = nextY + radius * 2 > rect.y && nextY < rect.y + rect.height;

                    if (hitX && hitY) {
                        brick.hit();
                        
                        if (audioManager != null) {
                            audioManager.playBrickHit();
                        }

                        float overlapLeft   = Math.abs((nextX + radius * 2) - rect.x);
                        float overlapRight  = Math.abs((rect.x + rect.width) - nextX);
                        float overlapTop    = Math.abs((nextY + radius * 2) - rect.y);
                        float overlapBottom = Math.abs((rect.y + rect.height) - nextY);

                        float minHorizontal = Math.min(overlapLeft, overlapRight);
                        float minVertical   = Math.min(overlapTop, overlapBottom);

                        if (minVertical < minHorizontal) {
                            ball.setVelY(-ball.getVelY());
                        } else {
                            ball.setVelX(-ball.getVelX());
                        }

                        if (brick.isDestroyed()) {
                            game.addScore(10);
                            // Có 20% chance drop power-up
                            if (Math.random() < 1) {
                                dropPowerUp(brick.getX(), brick.getY(), powerUps, game);
                            }


                            bricks.remove(j); // 'j' là chỉ số từ vòng lặp ngược

                            // Kiểm tra xem đã hết gạch chưa
                            if (bricks.isEmpty()) {
                                // In ra để debug
                                System.out.println("GẠCH CUỐI CÙNG ĐÃ BỊ PHÁ! GỌI LEVEL UP!");

                                // Gọi hàm level up
                                levelManager.levelUp(game);
                            }

                        }

                        collided = true;
                        break;
                    }
                }
            }
            if (collided) break;
        }
    }
    
    private void dropPowerUp(float x, float y, List<PowerUp> powerUps, Game game) {
        PowerUpType type = game.randomPowerUpType();
        powerUps.add(new PowerUp(x, y, type));
    }
    
    private void handlePowerUpCollision(List<PowerUp> powerUps, Paddle paddle, Game game) {
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = powerUps.get(i);
            
            // Kiểm tra va chạm với paddle
            if (powerUp.getY() + powerUp.getHeight() >= paddle.getY() &&
                powerUp.getY() <= paddle.getY() + paddle.getHeight() &&
                powerUp.getX() + powerUp.getWidth() >= paddle.getX() &&
                powerUp.getX() <= paddle.getX() + paddle.getWidth()) {
                System.out.println("Power-up " + powerUp.getType() + " đã được kích hoạt!");
                
                // Kích hoạt power-up: chỉ gọi 1 hàm, không viết logic ở đây
                game.activatePowerUp(powerUp.getType());
                
                powerUps.remove(i);
            }
        }
    }

}
