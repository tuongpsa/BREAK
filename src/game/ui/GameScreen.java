package game.ui;

import game.audio.AudioManager;
import game.core.PauseManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
/**
 * Lớp cơ sở cho mọi màn hình game.
 */
public abstract class GameScreen extends Canvas {
    protected AudioManager audioManager;
    protected PauseManager pauseManager;

    protected boolean leftPressed = false;
    protected boolean rightPressed = false;

    private AnimationTimer gameTimer;

    public GameScreen(double width, double height, AudioManager audioManager, PauseManager pauseManager) {
        super(width, height);
        this.audioManager = audioManager;
        this.pauseManager = pauseManager;

        this.setFocusTraversable(true);

        // Xử lý phím nhấn
        this.setOnKeyPressed(this::handleKeyPressed);
        this.setOnKeyReleased(this::handleKeyReleased);
    }

    // Abstract methods mà màn hình con phải implement
    protected abstract void updateGame(float deltaTime);
    protected abstract void renderGame();
    
    // Abstract methods cho xử lý phím game over
    protected abstract void handleRestartKey();
    protected abstract void handleQuitKey();

    // Start game loop
    public void startGameLoop() {
        if (gameTimer != null) gameTimer.stop();

        gameTimer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                float deltaTime = (now - lastTime) / 1_000_000_000f;
                lastTime = now; // Cập nhật lastTime ngay lập tức
                // Chỉ update logic nếu không pause
                if (!pauseManager.isPaused()) {
                    updateGame(deltaTime);
                }
                renderGame();
            }
        };
        gameTimer.start();

        if (audioManager != null) audioManager.playGameMusic();
    }

    public void stopGameLoop() {
        if (gameTimer != null) gameTimer.stop();
        if (audioManager != null) audioManager.stopGameMusic();
    }

    // Xử lý phím
    protected void handleKeyPressed(KeyEvent e) {
        System.out.println("Key pressed: " + e.getCode()); // Debug
        
        if (e.getCode() == KeyCode.LEFT) leftPressed = true;
        if (e.getCode() == KeyCode.RIGHT) rightPressed = true;

        
        // Xử lý phím khi game over
        if (e.getCode() == KeyCode.R) {
            System.out.println("R key pressed - calling handleRestartKey");
            handleRestartKey();
        }
        if (e.getCode() == KeyCode.ESCAPE) {
            System.out.println("ESC key pressed - calling handleQuitKey");
            handleQuitKey();
        }
    }

    protected void handleKeyReleased(KeyEvent e) {
        if (e.getCode() == KeyCode.LEFT) leftPressed = false;
        if (e.getCode() == KeyCode.RIGHT) rightPressed = false;
    }

}
