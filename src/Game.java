import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final float width = 480;
    private final float height = 720;

    private Ball ball;
    private final float ballRadius = 10;
    private final float ballSpeed = 400.f;

    private Paddle paddle;
    private final float paddleWidth = width / 2;
    private final float paddleHeight = 15;

    private List<Brick> bricks = new ArrayList<>();
    private final int maxBrick = 5;

    private int score = 0;
    private boolean gameOver = false;

    private Random rand = new Random();

    public Game() {
        ball = new Ball((width/2)-ballRadius, height-20.01f-(2*ballRadius), ballRadius, ballSpeed);
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

    public void update(float deltaTime){
        if(gameOver) return; // dừng update nếu game over
        ball.move(deltaTime);
        CollisionHandler.checkCollisions(this); // gọi method đã truyền Game
    }

    // Getters
    public Ball getBall(){ return ball; }
    public Paddle getPaddle(){ return paddle; }
    public List<Brick> getBricks(){ return bricks; }
    public int getScore(){ return score; }
    public void addScore(int s){ score += s; }
    public float getWidth(){ return width; }
    public float getHeight(){ return height; }

    // Game over
    public boolean isGameOver(){ return gameOver; }
    public void setGameOver(boolean b){ gameOver = b; }
}
