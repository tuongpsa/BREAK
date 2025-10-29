package game.ui;
import game.render.LevelRender;

import game.core.Game;
import game.objects.*;
import game.render.Background;
import game.render.GameOverRenderer;
import game.score.HighScoreManager;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Màn hình chơi game chính.
 */
public class GamePanel extends GameScreen {
    private Game game;
    private HighScoreManager highScoreManager;
    private GameOverRenderer gameOverRenderer;
    private MenuCallback menuCallback;

    private boolean highScoreProcessed = false;

    private Image ballImage;
    private Image paddleImage;
    private Image brickImage;

    private Background background;
    private LevelRender levelRender;
    private int lastLevel = -1;

    public GamePanel(double width, double height) {
        super(width, height);

        game = new Game(audioManager);
        highScoreManager = new HighScoreManager();

        levelRender = new LevelRender();
        game.getLevelManager().setLevelRender(levelRender);

        ballImage = new Image("file:assets/bóng.png");
        paddleImage = new Image("file:assets/haiz.png");
        brickImage = new Image("file:assets/brick1.jpg");

        background = new Background("assets/backgroundtest.jpeg");

        if (ballImage.isError() || paddleImage.isError() || brickImage.isError()) {
            System.out.println("Object game is error");
        }
    }

    @Override
    protected void updateGame(float deltaTime) {
        if (paused) return;
        
        if (!game.isGameOver()) {
            if (leftPressed) game.getPaddle().moveLeft(deltaTime, game.getWidth());
            if (rightPressed) game.getPaddle().moveRight(deltaTime, game.getWidth());
            game.update(deltaTime);
        }

        levelRender.update();

        // nếu level thay đổi , hiển thị thông báo
        int currentLevel = game.getLevelManager().getLevel();
        if (currentLevel != lastLevel) {
            levelRender.showLevel(currentLevel);
            lastLevel = currentLevel;
        }

    }

    @Override
    protected void renderGame() {
        GraphicsContext gc = getGraphicsContext2D();
        background.drawBackground(gc,getWidth(),getHeight());

        // Vẽ paddle
        Paddle paddle = game.getPaddle();
        double paddleCenterX = paddle.getX() + paddle.getWidth()/2;
        double paddleCenterY = paddle.getY() + paddle.getHeight()/2;

        gc.drawImage(
                paddleImage,
                paddleCenterX - paddle.getWidth()/2,
                paddleCenterY - paddle.getHeight()/2,
                paddle.getWidth(),
                paddle.getHeight()*5.5
        );

        // Vẽ bóng
        for (Ball ball : game.getBalls()) {
            gc.drawImage(ballImage, ball.getX(), ball.getY(), ball.getRadius()*2, ball.getRadius()*2);
        }

        // Vẽ gạch
        for (Brick brick : game.getBricks()) {
            if (!brick.isDestroyed()) {
                gc.drawImage(brickImage, brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
            }
        }

        // Vẽ điểm, level
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(18));
        gc.fillText("Score: " + game.getScore(),10,40);
        gc.fillText("Level: " + game.getLevelManager().getLevel(),10,20);

        levelRender.render(gc);

        // Khi game over
        if (game.isGameOver()) {
            if (audioManager != null) audioManager.playGameOver();

            if (!highScoreProcessed) {
                processHighScore();
                highScoreProcessed = true;
                // Đảm bảo có focus để nhận phím
                this.requestFocus();
            }

            gameOverRenderer = new GameOverRenderer();
            gameOverRenderer.render(gc,getWidth(),getHeight(),game.getScore());
        }

        // Vẽ paused overlay
        if (paused) {
            gc.setFill(Color.color(0,0,0,0.5));
            gc.fillRect(0,0,getWidth(),getHeight());
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(36));
            gc.fillText("PAUSED",getWidth()/2 - 60,getHeight()/2);
        }
    }

    // Xử lý high score
    private void processHighScore() {
        int currentScore = game.getScore();
        Platform.runLater(() -> {
            if (highScoreManager.isHighScore(currentScore)) {
                String playerName = NameInputDialog.showDialog(currentScore);
                highScoreManager.addScore(playerName,currentScore);
            } else {
                NameInputDialog.showNotHighScoreDialog(currentScore);
            }
        });
    }

    public Game getGame() {
        return game;
    }

    public void setMenuCallback(MenuCallback callback) {
        this.menuCallback = callback;
    }

    @Override
    protected void handleRestartKey() {
        // Chỉ restart khi game over
        if (game.isGameOver()) {
            System.out.println("Restarting game...");
            game.resetGame();
            highScoreProcessed = false;
        }
    }

    @Override
    protected void handleQuitKey() {
        // Chỉ quit khi game over
        if (game.isGameOver()) {
            System.out.println("Quitting to menu...");
            if (menuCallback != null) {
                menuCallback.returnToMenu();
            }
        }
    }
}
