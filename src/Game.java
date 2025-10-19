import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final float width = 480;
    private final float height = 720;

    private List<Ball> balls = new ArrayList<>();
    private final float ballRadius = 10;
    private final float ballSpeed = 250.f;

    private Paddle paddle;
    private final float paddleWidth = width*1/2 ;
    private final float paddleHeight = 10;

    private List<Brick> bricks = new ArrayList<>();
    private int maxBrick = 5;

    private List<PowerUp> powerUps = new ArrayList<>();
    private int score = 0;
    private boolean gameOver = false;
    private int scoreMultiplier = 1;

    private Random rand = new Random();
    private AudioManager audioManager;

    public Game(AudioManager audioManager) {
        this.audioManager = audioManager;
        balls.add(new Ball((width/2)-ballRadius, height-20, ballRadius, ballSpeed));
        paddle = new Paddle((width-paddleWidth)/2, height-20, paddleWidth, paddleHeight);
        createBricks();
    }

    public void createBricks() {
        bricks.clear();
        for (int i = 0; i < maxBrick; i++) {
            boolean valid = false;
            Brick brick = null;
            while(!valid){
                float x = 30 + rand.nextInt((int)(width - 60 - 60));
                float y = 30 + rand.nextInt((int)(height/2));
                int hp = 1 + rand.nextInt(3);
                brick = new Brick(x, y, hp);
                valid = true;
                for (Brick other : bricks){
                    if(brick.getBounds().intersects(other.getBounds())){
                        valid = false; break;
                    }
                }
            }
            bricks.add(brick);
        }
    }

    public void update(float deltaTime) {
        if (gameOver) return; // dừng update nếu game over

        // Update balls
        for (Ball ball : balls) {
            ball.move(deltaTime);
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

        CollisionHandler handler = new CollisionHandler(audioManager);
        handler.handleBallBrickCollision(balls, deltaTime, bricks, this, paddle, powerUps);
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
    public void setMaxBrick(int maxBrick) {
        this.maxBrick = maxBrick;
    }


    // Game over
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
        createBricks();
    }
    
    // Constructor mặc định cho tương thích ngược
    public Game() {
        this(null);
    }
    
    // Getter cho ballSpeed
    public float getBallSpeed() { return ballSpeed; }
    
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
}
