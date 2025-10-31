package game.ui;

import game.audio.AudioManager;
import game.core.ControlScheme;
import game.core.Game;
import game.core.GameSettings;
import game.core.PauseManager;
import game.objects.Ball;
import game.objects.Brick;
import game.objects.Paddle;
import game.objects.PowerUp;
import game.objects.PowerUpType;
import game.objects.Bullet;
import game.render.Background;
import game.render.GameOverRenderer;
import game.render.LevelRender;
import game.score.HighScoreManager;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Image ballShieldImage;
    private Map<Integer, Image> brickImages;
    private Map<PowerUpType, Image> powerUpImages;

    private Background background;
    private LevelRender levelRender;
    private int lastLevel = -1;
    private final GameSettings gameSettings;

    public GamePanel(double width, double height, GameSettings settings, AudioManager audioMgr, PauseManager pauseMgr) {
        // 1. Truyền audioManager và pauseManager lên LỚP CHA (GameScreen)
        super(width, height, audioMgr, pauseMgr);

        // 2. Lưu lại GameSettings
        this.gameSettings = settings;
        // (this.audioManager và this.pauseManager đã được lớp cha lưu)

        // 3. Truyền audioManager (đã được tiêm vào) cho Game
        game = new Game(this.audioManager);
        highScoreManager = new HighScoreManager();

        levelRender = new LevelRender();
        game.getLevelManager().setLevelRender(levelRender);

        ballImage = new Image("file:assets/ball.png");
        ballShieldImage = new Image("file:assets/shield_active.png");
        paddleImage = new Image("file:assets/paddle1.png");

        brickImages = new HashMap<>();
        Image brick_hp3 = new Image("file:assets/brick3.png"); // Gạch 3 HP
        Image brick_hp2 = new Image("file:assets/brick2.png"); // Gạch 2 HP
        Image brick_hp1 = new Image("file:assets/brick.png"); // Gạch 1 HP

        // Đưa ảnh vào Map với "key" là số HP
        brickImages.put(3, brick_hp3);
        brickImages.put(2, brick_hp2);
        brickImages.put(1, brick_hp1);

        background = new Background("assets/background2.png");

        // Power-up images (dùng ảnh sẵn có trong assets làm placeholder)
        powerUpImages = new HashMap<>();
        try {
            powerUpImages.put(PowerUpType.SCORE_MULTIPLIER, new Image("file:assets/SCORE_MULTIPLIER.png"));
            powerUpImages.put(PowerUpType.MULTI_BALL, new Image("file:assets/MULTI_BALL.png"));
            powerUpImages.put(PowerUpType.SPEED_BOOST, new Image("file:assets/SPEED_BOOST.png"));
            powerUpImages.put(PowerUpType.BIG_PADDLE, new Image("file:assets/BIG_PADDLE.png"));
            powerUpImages.put(PowerUpType.SLOW_BALL, new Image("file:assets/SLOW_BALL.png"));
            powerUpImages.put(PowerUpType.SHIELD, new Image("file:assets/SHIELD.png"));
            powerUpImages.put(PowerUpType.LASER, new Image("file:assets/LASER.png"));
        } catch (Exception e) {
            System.out.println("Lỗi tải ảnh power-up: " + e.getMessage());
        }

        if (ballImage.isError() || paddleImage.isError() ||
                brick_hp3.isError() || brick_hp2.isError() || brick_hp1.isError()) {
            System.out.println("Lỗi: Không thể tải một hoặc nhiều file ảnh (ball, paddle, hoặc bricks).");
        }

    }

    @Override
    protected void updateGame(float deltaTime) {
        //if (paused) return;

        if (!game.isGameOver()) {
            // Cập nhật input (logic này chạy đúng vì handleKeyPressed cập nhật left/rightPressed)
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
            double w = ball.getRadius()*2+10;
            double h = w;
            boolean shieldActive = game.getShieldLives() > 0;
            if (shieldActive && ballShieldImage != null && !ballShieldImage.isError()) {
                gc.drawImage(ballShieldImage, ball.getX(), ball.getY(), w, h);
            } else {
                gc.drawImage(ballImage, ball.getX(), ball.getY(), w, h);
            }
        }

        // Vẽ đạn laser
        gc.setFill(Color.RED);
        for (Bullet b : game.getBullets()) {
            gc.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }

        // Vẽ power-ups đang rơi
        for (PowerUp powerUp : game.getPowerUps()) {
            renderPowerUp(gc, powerUp);
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
        
        // Vẽ thời gian còn lại của các power-up đang active
        renderActivePowerUps(gc, game);

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

    // Ghi đè (Override) hàm của GameScreen
    @Override
    protected void handleKeyPressed(KeyEvent e) {
        // Phím ESC đã được xử lý bởi Main.java (trong setupInputHandlers)

        // Nếu đang pause, không nhận phím game
        if (pauseManager.isPaused()) return;

        if (game.isGameOver()) {
            if (e.getCode() == KeyCode.R) {
                handleRestartKey();
                return; // Đã xử lý, không làm gì nữa
            }
            if (e.getCode() == KeyCode.ESCAPE) {
                handleQuitKey();
                return; // Đã xử lý, không làm gì nữa
            }
        }

        // Lấy kiểu điều khiển từ Settings
        ControlScheme controls = gameSettings.getControlScheme();

        if (controls == ControlScheme.ARROW_KEYS) {
            if (e.getCode() == KeyCode.LEFT) leftPressed = true;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = true;
        } else { // (controls == ControlScheme.AD_KEYS)
            if (e.getCode() == KeyCode.A) leftPressed = true;
            if (e.getCode() == KeyCode.D) rightPressed = true;
        }

        // Bắn laser
        if (e.getCode() == KeyCode.SPACE) {
            game.fireLaser();
        }
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
    @Override
    protected void handleKeyReleased(KeyEvent e) {
        ControlScheme controls = gameSettings.getControlScheme();

        if (controls == ControlScheme.ARROW_KEYS) {
            if (e.getCode() == KeyCode.LEFT) leftPressed = false;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = false;
        } else { // (controls == ControlScheme.AD_KEYS)
            if (e.getCode() == KeyCode.A) leftPressed = false;
            if (e.getCode() == KeyCode.D) rightPressed = false;
        }
    }
    
    private void renderPowerUp(GraphicsContext gc, PowerUp powerUp) {
        float x = powerUp.getX();
        float y = powerUp.getY();
        float width = powerUp.getWidth();
        float height = powerUp.getHeight();
        Image img = powerUpImages != null ? powerUpImages.get(powerUp.getType()) : null;
        if (img != null && !img.isError()) {
            gc.drawImage(img, x, y, width, height);
        } else {
            // Fallback vẽ hình nếu ảnh không có
            Color color = getPowerUpColor(powerUp.getType());
            switch (powerUp.getType()) {
                case SCORE_MULTIPLIER:
                case MULTI_BALL:
                case SHIELD:
                case LASER:
                    gc.setFill(color);
                    gc.fillOval(x, y, width, height);
                    gc.setStroke(Color.WHITE);
                    gc.setLineWidth(2);
                    gc.strokeOval(x, y, width, height);
                    break;
                case SPEED_BOOST:
                case BIG_PADDLE:
                case SLOW_BALL:
                    gc.setFill(color);
                    gc.fillRect(x, y, width, height);
                    gc.setStroke(Color.WHITE);
                    gc.setLineWidth(2);
                    gc.strokeRect(x, y, width, height);
                    break;
            }
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(12));
            String symbol = getPowerUpSymbol(powerUp.getType());
            gc.fillText(symbol, x + width/2 - 5, y + height/2 + 5);
        }
    }
    
    private Color getPowerUpColor(PowerUpType type) {
        switch (type) {
            case SCORE_MULTIPLIER: return Color.GOLD;
            case MULTI_BALL: return Color.CYAN;
            case SPEED_BOOST: return Color.LIME;
            case BIG_PADDLE: return Color.ORANGE;
            case SLOW_BALL: return Color.PURPLE;
            case SHIELD: return Color.BLUE;
            case LASER: return Color.RED;
            default: return Color.GRAY;
        }
    }
    
    private String getPowerUpSymbol(PowerUpType type) {
        switch (type) {
            case SCORE_MULTIPLIER: return "x2";
            case MULTI_BALL: return "MB";
            case SPEED_BOOST: return "S";
            case BIG_PADDLE: return "B";
            case SLOW_BALL: return "SL";
            case SHIELD: return "S";
            case LASER: return "L";
            default: return "?";
        }
    }
    
    private void renderActivePowerUps(GraphicsContext gc, Game game) {
        Map<PowerUpType, Float> activePowerUps = game.getActivePowerUps();
        if (activePowerUps == null || activePowerUps.isEmpty()) return;
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font(14));
        float yOffset = 60;
        int index = 0;
        for (Map.Entry<PowerUpType, Float> entry : activePowerUps.entrySet()) {
            PowerUpType type = entry.getKey();
            Float timeLeft = entry.getValue();
            if (timeLeft != null && timeLeft > 0) {
                String typeName = getPowerUpName(type);
                String timeText = String.format("%s: %.1fs", typeName, timeLeft);
                gc.fillText(timeText, 10, yOffset + index * 20);
                index++;
            }
        }
    }
    
    private String getPowerUpName(PowerUpType type) {
        switch (type) {
            case SCORE_MULTIPLIER: return "Score x2";
            case MULTI_BALL: return "Multi Ball";
            case SPEED_BOOST: return "Speed Boost";
            case BIG_PADDLE: return "Big Paddle";
            case SLOW_BALL: return "Slow Ball";
            case SHIELD: return "Shield";
            case LASER: return "Laser";
            default: return "Unknown";
        }
    }
}
