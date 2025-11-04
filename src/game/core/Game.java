package game.core;

import game.audio.AudioManager;
import game.objects.Ball;
import game.objects.Bullet;
import game.objects.Brick;
import game.objects.Paddle;
import game.objects.PowerUp;
import game.objects.PowerUpType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {
    private final float width = 480;
    private final float height = 720;

    private List<Ball> balls = new ArrayList<>();
    private final float ballRadius = 15;
    private  float ballSpeed = 500.f;

    private Paddle paddle;
    private final float paddleWidth = width/2 ;
    private final float paddleHeight = 7;

    private List<Brick> bricks = new ArrayList<>();

    private List<PowerUp> powerUps = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private Map<PowerUpType, Float> activePowerUps = new HashMap<>(); // Track active power-ups và thời gian còn lại
    private int score = 0;
    private boolean gameOver = false;
    private int scoreMultiplier = 1;
    // Power-up state
    private boolean speedBoostActive = false;
    private float speedBoostMultiplier = 1.5f;
    private boolean bigPaddleActive = false;
    private float bigPaddleMultiplier = 1.5f;
    private boolean slowBallActive = false;
    private float slowBallMultiplier = 0.7f;
    private int shieldLives = 0;
    private int laserShots = 0;

    private Random rand = new Random();

    private LevelManager levelManager = new LevelManager();
    private final CollisionHandler collisionHandler;

    public Game(AudioManager audioManager) {
        this.collisionHandler = new CollisionHandler(audioManager, this.levelManager);
        balls.add(new Ball((width/2)-ballRadius, height-20, ballRadius, ballSpeed));
        paddle = new Paddle((width-paddleWidth)/2, height-20, paddleWidth, paddleHeight);
        createBricks(levelManager.getLevel());
    }

    public void createBricks(int currentLevel) {
        System.out.println("Đang bắt đầu tạo gạch cho level: " + currentLevel);
        bricks.clear();

        // Tạm thời tôi vẫn dùng đường dẫn tuyệt đối của bạn
        String filePath = "levels/level" + currentLevel + ".txt";

        int[][] layout = LevelLoader.loadLayoutFromFile(filePath);

        if (layout == null || layout.length == 0) {
            System.err.println("Không thể tải level " + currentLevel + " hoặc layout rỗng.");
            gameOver = true; // Nếu không tải được level, nên cho game over
            return;
        }


        float BRICK_WIDTH = 42;
        float BRICK_HEIGHT = 20;
        float PADDING = 5;
        float OFFSET_TOP = 50;

        for (int row = 0; row < layout.length; row++) {

            // CĂN GIỮA
            // Lấy số cột của hàng hiện tại
            int numCols = layout[row].length;

            // Tính tổng chiều rộng của layout này (N gạch và N-1 khoảng cách)
            float totalLayoutWidth = (numCols * BRICK_WIDTH) + ((numCols - 1) * PADDING);

            // Tính lề trái mới để căn giữa
            float dynamicOffsetLeft = (width - totalLayoutWidth) / 2;
            // --- CĂN GIỮA ---

            for (int col = 0; col < numCols; col++) {
                int hp = layout[row][col];

                if (hp > 0) {
                    // Dùng lề trái động, và công thức tính x ĐÚNG
                    float x = dynamicOffsetLeft + col * (BRICK_WIDTH + PADDING);
                    float y = OFFSET_TOP + row * (BRICK_HEIGHT + PADDING);

                    Brick newBrick = new Brick(x, y, hp);
                    bricks.add(newBrick);
                }
            }
        }

        System.out.println("Đã tạo thành công " + bricks.size() + " viên gạch.");
    }

    public void update(float deltaTime) {
        if (gameOver) return; // dừng update nếu game over

        // Update balls
        for (Ball ball : balls) {
            ball.update(deltaTime);
        }

        // Update power-ups đang rơi
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = powerUps.get(i);
            powerUp.update(deltaTime);

            // Chỉ loại bỏ khi rơi khỏi màn hình; không dùng isExpired cho vật phẩm đang rơi
            if (powerUp.getY() > height) {
                powerUps.remove(i);
            }
        }

        // Update bullets
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(deltaTime);
            if (bullet.getY() + bullet.getHeight() < 0) {
                bullets.remove(i);
                continue;
            }
            // Va chạm với gạch
            for (int j = bricks.size() - 1; j >= 0; j--) {
                Brick brick = bricks.get(j);
                if (!brick.isDestroyed()) {
                    java.awt.Rectangle b1 = new java.awt.Rectangle((int)brick.getX(), (int)brick.getY(), (int)brick.getWidth(), (int)brick.getHeight());
                    java.awt.Rectangle b2 = bullet.getBounds();
                    if (b1.intersects(b2)) {
                        brick.hit();
                        bullets.remove(i);
                        if (brick.isDestroyed()) {
                            addScore(10);
                            // Có 20% cơ hội rơi power-up khi phá gạch bằng laser
                            if (Math.random() < 0.2) {
                                powerUps.add(new PowerUp(brick.getX(), brick.getY(), randomPowerUpType()));
                            }
                            bricks.remove(j);
                        }
                        break;
                    }
                }
            }
        }

        // Sau khi xử lý đạn, nếu hết gạch thì chuyển level
        if (bricks.isEmpty() && !gameOver) {
            levelManager.levelUp(this);
        }
        
        // Update thời gian của các power-up đang active
        List<PowerUpType> toRemove = new ArrayList<>();
        for (Map.Entry<PowerUpType, Float> entry : activePowerUps.entrySet()) {
            float timeLeft = entry.getValue();
            timeLeft -= deltaTime;
            if (timeLeft <= 0) {
                toRemove.add(entry.getKey());
            } else {
                entry.setValue(timeLeft);
            }
        }
        for (PowerUpType type : toRemove) {
            activePowerUps.remove(type);
            // Remove effect khi hết thời gian
            if (type == PowerUpType.SCORE_MULTIPLIER) {
                scoreMultiplier = 1;
            } else if (type == PowerUpType.SPEED_BOOST) {
                if (speedBoostActive) {
                    Paddle p = getPaddle();
                    if (p != null) {
                        p.setMoveSpeed(p.getMoveSpeed() / speedBoostMultiplier);
                    }
                    speedBoostActive = false;
                }
            } else if (type == PowerUpType.BIG_PADDLE) {
                if (bigPaddleActive) {
                    Paddle p = getPaddle();
                    if (p != null) {
                        p.setWidth(p.getWidth() / bigPaddleMultiplier);
                        if (p.getX() + p.getWidth() > getWidth()) {
                            p.setX(getWidth() - p.getWidth());
                        }
                        if (p.getX() < 0) p.setX(0);
                    }
                    bigPaddleActive = false;
                }
            } else if (type == PowerUpType.SLOW_BALL) {
                if (slowBallActive) {
                    for (Ball b : balls) {
                        b.setVelX(b.getVelX() / slowBallMultiplier);
                        b.setVelY(b.getVelY() / slowBallMultiplier);
                    }
                    slowBallActive = false;
                }
            }
        }

        collisionHandler.handleBallBrickCollision(balls, deltaTime, bricks, this, paddle, powerUps);
    }


    // Getters
    public List<Ball> getBalls(){ return balls; }
    public Ball getBall(){ return balls.isEmpty() ? null : balls.get(0); }
    public Paddle getPaddle(){ return paddle; }
    public List<Brick> getBricks(){ return bricks; }
    public List<PowerUp> getPowerUps(){ return powerUps; }
    public int getScore(){ return score; }
    public void addScore(int s){ score += s * scoreMultiplier; }
    public float getWidth(){ return width; }
    public float getHeight(){ return height; }
    public int getScoreMultiplier(){ return scoreMultiplier; }

    // game.core.Game over
    public boolean isGameOver(){ return gameOver; }
    public void setGameOver(boolean b){ gameOver = b; }

    // Thêm method để reset game
    public void resetGame() {
        gameOver = false;
        score = 0;
        scoreMultiplier = 1;
        balls.clear();
        balls.add(new Ball((width/2)-ballRadius, height-150, ballRadius, ballSpeed));
        paddle = new Paddle((width-paddleWidth)/2, height-20, paddleWidth, paddleHeight);
        powerUps.clear();
        activePowerUps.clear(); // Xóa active power-ups khi reset
        createBricks(levelManager.getLevel());
    }

    // Constructor mặc định cho tương thích ngược
    public Game() {
        this(null);
    }

    // Getter cho ballSpeed
    public float getBallSpeed() { return ballSpeed; }
    public LevelManager getLevelManager() {
        return this.levelManager;
    }
    
    // Getter cho CollisionHandler để có thể shutdown khi cần
    public CollisionHandler getCollisionHandler() {
        return this.collisionHandler;
    }

    // Setter cho ballSpeed
    public void setBallSpeed(float newSpeed) {
        this.ballSpeed = newSpeed;
    }
    // Power-up methods
    public boolean consumeShieldIfAvailable(Ball ball, float screenHeight) {
        if (shieldLives > 0 && ball != null) {
            shieldLives--;
            // Đặt bóng lên trên mép dưới một chút và bật ngược lên
            ball.setY(screenHeight - 40);
            float vy = ball.getVelY();
            if (vy > 0) {
                ball.setVelY(-vy);
            } else if (vy == 0) {
                ball.setVelY(-Math.abs(getBallSpeed()));
            }
            return true;
        }
        return false;
    }

    // Power-up methods
    public void activateScoreMultiplier() {
        scoreMultiplier = 2;
    }

    public void activateMultiBall() {
        if (balls.size() < 5) { // Giới hạn tối đa 5 bóng
            Ball originalBall = balls.get(0);
            for (int i = 0; i < 2; i++) {
                Ball newBall = new Ball(
                    originalBall.getX() + (i - 1) * 20,
                    originalBall.getY(),
                    ballRadius,
                    ballSpeed
                );
                // Tạo góc khác nhau cho các bóng
                newBall.setVelX(originalBall.getVelX() + (i - 1) * 100);
                newBall.setVelY(originalBall.getVelY());
                balls.add(newBall);
            }
        }
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }

    public void removePowerUp(PowerUp powerUp) {
        powerUps.remove(powerUp);
    }

    public float getBallRadius() {
        return ballRadius;
    }
    
    // Method để random loại power-up
    public PowerUpType randomPowerUpType() {
        PowerUpType[] types = PowerUpType.values();
        return types[rand.nextInt(types.length)];
    }
    
    // Method tổng hợp để kích hoạt power-up dựa trên type
    public void activatePowerUp(PowerUpType type) {
        // Lấy duration từ PowerUpType (tạm thời hardcode, có thể lưu trong PowerUp object)
        float duration = 10f;
        switch (type) {
            case SCORE_MULTIPLIER: duration = 15f; break;
            case MULTI_BALL: duration = 0f; break; // Instant
            case SPEED_BOOST: duration = 12f; break;
            case BIG_PADDLE: duration = 15f; break;
            case SLOW_BALL: duration = 10f; break;
            case SHIELD: duration = 0f; break; // Instant
            case LASER: duration = 0f; break; // Instant
        }
        
        // Nếu power-up có duration, thêm vào activePowerUps
        if (duration > 0) {
            activePowerUps.put(type, duration);
        }
        
        switch (type) {
            case SCORE_MULTIPLIER:
                activateScoreMultiplier();
                break;
            case MULTI_BALL:
                activateMultiBall();
                break;
            case SPEED_BOOST:
                activateSpeedBoost();
                break;
            case BIG_PADDLE:
                activateBigPaddle();
                break;
            case SLOW_BALL:
                activateSlowBall();
                break;
            case SHIELD:
                activateShield();
                break;
            case LASER:
                activateLaser();
                break;
        }
    }
    
    // Các method kích hoạt cho power-up mới
    public void activateSpeedBoost() {
        if (speedBoostActive) return;
        speedBoostActive = true;
        Paddle p = getPaddle();
        if (p != null) {
            p.setMoveSpeed(p.getMoveSpeed() * speedBoostMultiplier);
        }
    }
    
    public void activateBigPaddle() {
        if (bigPaddleActive) return;
        bigPaddleActive = true;
        Paddle p = getPaddle();
        if (p != null) {
            float newWidth = p.getWidth() * bigPaddleMultiplier;
            float overflow = (p.getX() + newWidth) - getWidth();
            p.setWidth(newWidth);
            if (overflow > 0) {
                p.setX(Math.max(0, p.getX() - overflow));
            }
        }
    }
    
    public void activateSlowBall() {
        if (slowBallActive) return;
        slowBallActive = true;
        for (Ball b : balls) {
            b.setVelX(b.getVelX() * slowBallMultiplier);
            b.setVelY(b.getVelY() * slowBallMultiplier);
        }
    }
    
    public void activateShield() {
        shieldLives += 1;
    }
    
    public void activateLaser() {
        laserShots += 6; // cấp số lần bắn
    }
    
    // Getter cho active power-ups
    public Map<PowerUpType, Float> getActivePowerUps() {
        return activePowerUps;
    }

    // Laser API
    public void fireLaser() {
        if (laserShots <= 0) return;
        Paddle p = getPaddle();
        if (p == null) return;
        float bulletWidth = 6;
        float bulletHeight = 16;
        float speed = 800f;
        float leftX = p.getX() + p.getWidth() * 0.25f - bulletWidth / 2f;
        float rightX = p.getX() + p.getWidth() * 0.75f - bulletWidth / 2f;
        float y = p.getY() - bulletHeight - 5;
        bullets.add(new Bullet(leftX, y, bulletWidth, bulletHeight, speed));
        bullets.add(new Bullet(rightX, y, bulletWidth, bulletHeight, speed));
        laserShots--;
    }

    public List<Bullet> getBullets() { return bullets; }
    public int getLaserShots() { return laserShots; }

    public int getShieldLives() { return shieldLives; }

    // Reset all power-up effects when changing level
    public void clearPowerUpsOnLevelUp() {
        // Clear active timers
        activePowerUps.clear();
        // Reset score multiplier
        scoreMultiplier = 1;
        // Reset paddle speed
        if (paddle != null) {
            paddle.setMoveSpeed(350f);
            // Reset paddle width to default
            paddle.setWidth(paddleWidth);
            if (paddle.getX() + paddle.getWidth() > width) {
                paddle.setX(width - paddle.getWidth());
            }
            if (paddle.getX() < 0) paddle.setX(0);
        }
        speedBoostActive = false;
        bigPaddleActive = false;
        slowBallActive = false;
        // Clear shield and laser
        shieldLives = 0;
        laserShots = 0;
        // Remove bullets and falling powerups
        bullets.clear();
        powerUps.clear();
    }
}
