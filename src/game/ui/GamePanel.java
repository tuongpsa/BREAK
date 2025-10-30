package game.ui;

import game.core.Game;
import game.objects.Ball;
import game.objects.Brick;
import game.objects.Paddle;
import game.render.Background;
import game.render.GameOverRenderer;
import game.render.LevelRender;
import game.score.HighScoreManager;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

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
    private Map<Integer, Image> brickImages;

    private Background background;
    private LevelRender levelRender;
    private int lastLevel = -1;

    public GamePanel(double width, double height) {
        super(width, height);

        game = new Game(audioManager);
        highScoreManager = new HighScoreManager();

        levelRender = new LevelRender();
        game.getLevelManager().setLevelRender(levelRender);

        ballImage = new Image("file:assets/ball.png");
        paddleImage = new Image("file:assets/paddle1.png");

        brickImages = new HashMap<>();
        Image brick_hp3 = new Image("file:assets/brick3.png"); // Gạch 3 HP
        Image brick_hp2 = new Image("file:assets/brick2.png"); // Gạch 2 HP
        Image brick_hp1 = new Image("file:assets/brick.png"); // Gạch 1 HP

        // Đưa ảnh vào Map với "key" là số HP
        brickImages.put(3, brick_hp3);
        brickImages.put(2, brick_hp2);
        brickImages.put(1, brick_hp1);

        background = new Background("assets/background.png");

        if (ballImage.isError() || paddleImage.isError() ||
                brick_hp3.isError() || brick_hp2.isError() || brick_hp1.isError()) {
            System.out.println("Lỗi: Không thể tải một hoặc nhiều file ảnh (ball, paddle, hoặc bricks).");
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
                paddleCenterY - paddle.getHeight()/2-90,
                paddle.getWidth(),
                paddle.getHeight()*30
        );

        // Vẽ bóng
        for (Ball ball : game.getBalls()) {
            gc.drawImage(ballImage, ball.getX(), ball.getY(), ball.getRadius()*2+10, ball.getRadius()*2+10);
        }

        // Vẽ gạch

        for (Brick brick : game.getBricks()) {
            if (!brick.isDestroyed()) {

                // Lấy HP hiện tại của gạch
                int currentHp = brick.getHp();

                //Lấy ảnh tương ứng từ Map
                Image imageToDraw = brickImages.get(currentHp);

                //Nếu không tìm thấy ảnh thì dùng ảnh HP thấp nhất
                if (imageToDraw == null) {
                    imageToDraw = brickImages.get(3); // Ảnh mặc định
                }


                if (imageToDraw != null) {
                    gc.drawImage(imageToDraw, brick.getX(), brick.getY(), brick.getWidth()+10, brick.getHeight()+10);
                }
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
