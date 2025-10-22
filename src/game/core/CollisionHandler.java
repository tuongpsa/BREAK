package game.core;

import game.audio.AudioManager;
import game.objects.*;

import java.awt.*;
import java.util.List;

public class CollisionHandler {
    private final AudioManager audioManager;
    
    public CollisionHandler(AudioManager audioManager) {
        this.audioManager = audioManager;
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
                ball.reverseY();
                if (audioManager != null) {
                    audioManager.playPaddleHit();
                }
            }
        }

        for (int i = 0; i < steps; i++) {
            float nextX = ball.getX() + stepX;
            float nextY = ball.getY() + stepY;

            boolean collided = false;

            for (Brick brick : bricks) {
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
                            if (Math.random() < 1.0) {
                                dropPowerUp(brick.getX(), brick.getY(), powerUps);
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
    
    private void dropPowerUp(float x, float y, List<PowerUp> powerUps) {
        PowerUpType type = Math.random() < 0.5 ? PowerUpType.SCORE_MULTIPLIER : PowerUpType.MULTI_BALL;
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
                
                // Kích hoạt power-up
                switch (powerUp.getType()) {
                    case PowerUpType.SCORE_MULTIPLIER:
                        game.activateScoreMultiplier();
                        break;
                    case PowerUpType.MULTI_BALL:
                        game.activateMultiBall();
                        break;
                }
                
                powerUps.remove(i);
            }
        }
    }

}
