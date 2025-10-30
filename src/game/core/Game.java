package game.core;

import game.audio.AudioManager;
import game.objects.Ball;
import game.objects.Brick;
import game.objects.Paddle;
import game.objects.PowerUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final float width = 480;
    private final float height = 720;

    private List<Ball> balls = new ArrayList<>();
    private final float ballRadius = 15;
    private final float ballSpeed = 500.f;

    private Paddle paddle;
    private final float paddleWidth = width ;
    private final float paddleHeight = 7;

    private List<Brick> bricks = new ArrayList<>();
    private int maxBrick = 5;

    private List<PowerUp> powerUps = new ArrayList<>();
    private int score = 0;
    private boolean gameOver = false;
    private int scoreMultiplier = 1;

    private Random rand = new Random();
    private AudioManager audioManager;

    private LevelManager levelManager = new LevelManager();
    private final CollisionHandler collisionHandler;

    public Game(AudioManager audioManager) {
        this.collisionHandler = new CollisionHandler(audioManager, this.levelManager);
        this.audioManager = audioManager;
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

        // Update power-ups
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = powerUps.get(i);
            powerUp.update(deltaTime);

            // Remove power-ups that fell off screen or expired
            if (powerUp.getY() > height || powerUp.isExpired()) {
                powerUps.remove(i);
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

    //setter cho maxBrick
   /* public void setMaxBrick(int maxBrick) {
        this.maxBrick = maxBrick;
    }*/


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
}
