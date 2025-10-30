package game.ui;

import game.audio.AudioManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Lớp cơ sở cho mọi màn hình game.
 */
public abstract class GameScreen extends Canvas {
    protected AudioManager audioManager;
    protected boolean leftPressed = false;
    protected boolean rightPressed = false;
    protected boolean paused = false;

    private AnimationTimer gameTimer;

    public GameScreen(double width, double height) {
        super(width, height);
        audioManager = new AudioManager();
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
                if (lastTime > 0) {
                    float deltaTime = (now - lastTime) / 1_000_000_000f;
                    if (!paused) updateGame(deltaTime);
                    renderGame();
                }
                lastTime = now;
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

        // ✅ Hỗ trợ cả mũi tên + WASD
        if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A)
            leftPressed = true;

        if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D)
            rightPressed = true;

        // ✅ Nhấn SPACE để pause menu
        if (e.getCode() == KeyCode.SPACE) {
            paused = !paused;
            if (paused) {
                Platform.runLater(() -> {
                    PauseMenu pauseMenu = new PauseMenu();
                    pauseMenu.show(
                            (Stage) this.getScene().getWindow(),
                            () -> paused = false,      // Resume
                            () -> {                    // Replay
                                paused = false;
                                handleRestartKey();
                            },
                            () -> {                    // Exit
                                paused = false;
                                handleQuitKey();
                            }
                    );
                });
            }
        }

        // Key khi game over
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

        // ✅ Nhả phím A/LEFT
        if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A)
            leftPressed = false;

        // ✅ Nhả phím D/RIGHT
        if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D)
            rightPressed = false;
    }

    public boolean isPaused() {
        return paused;
    }
}
