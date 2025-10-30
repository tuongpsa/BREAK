package game.ui;

import game.audio.AudioManager;
import game.render.MenuRenderer;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

/**
 * MenuPanel class gộp tất cả logic - không cần kế thừa phức tạp
 * Có thể xử lý nhiều loại menu khác nhau
 */
public class MenuPanel extends Canvas {
    private MenuRenderer menuRenderer;
    private AudioManager audioManager;
    private boolean startGame = false;
    private boolean quitGame = false;
    private boolean showHighScore = false;
    private boolean resumeGame = false; // Cho pause menu

    public MenuPanel(double width, double height, AudioManager audioManager) {
        super(width, height);
        menuRenderer = new MenuRenderer();

        this.audioManager = audioManager;

        // Thiết lập để nhận sự kiện chuột
        this.setFocusTraversable(true);

        // Xử lý sự kiện click chuột
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double mouseX = event.getX();
                double mouseY = event.getY();
                handleMouseClick(mouseX, mouseY);
            }
        });

        // Xử lý sự kiện di chuyển chuột để highlight nút
        this.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMouseMove(event.getX(), event.getY());
            }
        });

        // Bắt đầu vòng lặp render menu
        startMenuLoop();

        // Bắt đầu nhạc menu
        if (this.audioManager != null) {
            this.audioManager.playMenuMusic();
        }
    }

    private void handleMouseClick(double x, double y) {
        // Kiểm tra click vào nút Start game
        if (isPointInStartButton(x, y)) {
            startGame = true;
        }
        // Kiểm tra click vào nút High Score
        else if (isPointInHighScoreButton(x, y)) {
            showHighScore = true;
        }
        // Kiểm tra click vào nút Resume (cho pause menu)
        else if (isPointInResumeButton(x, y)) {
            resumeGame = true;
        }
        // Kiểm tra click vào nút Quit
        else if (isPointInQuitButton(x, y)) {
            quitGame = true;
        }
    }

    private void handleMouseMove(double x, double y) {
        // Có thể thêm hiệu ứng highlight nút khi hover
    }

    private boolean isPointInStartButton(double x, double y) {
        double buttonWidth = 200;
        double buttonHeight = 50;
        double buttonX = (getWidth() - buttonWidth) / 2;
        double buttonY = getHeight() / 2 - 40;

        return x >= buttonX && x <= buttonX + buttonWidth &&
               y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInHighScoreButton(double x, double y) {
        double buttonWidth = 200;
        double buttonHeight = 50;
        double buttonX = (getWidth() - buttonWidth) / 2;
        double buttonY = getHeight() / 2 + 20;

        return x >= buttonX && x <= buttonX + buttonWidth &&
               y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInResumeButton(double x, double y) {
        double buttonWidth = 200;
        double buttonHeight = 50;
        double buttonX = (getWidth() - buttonWidth) / 2;
        double buttonY = getHeight() / 2 - 60;

        return x >= buttonX && x <= buttonX + buttonWidth &&
               y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInQuitButton(double x, double y) {
        double buttonWidth = 150;
        double buttonHeight = 40;
        double buttonX = (getWidth() - buttonWidth) / 2;
        double buttonY = getHeight() / 2 + 80;

        return x >= buttonX && x <= buttonX + buttonWidth &&
               y >= buttonY && y <= buttonY + buttonHeight;
    }

    private void startMenuLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
        timer.start();
    }

    private void render() {
        menuRenderer.render(getGraphicsContext2D(), getWidth(), getHeight());
    }

    // Getters
    public boolean isStartGame() {
        return startGame;
    }

    public boolean isQuitGame() {
        return quitGame;
    }

    public boolean isShowHighScore() {
        return showHighScore;
    }

    public boolean isResumeGame() {
        return resumeGame;
    }

    public void resetStartGame() {
        startGame = false;
    }

    public void resetQuitGame() {
        quitGame = false;
    }

    public void resetShowHighScore() {
        showHighScore = false;
    }

    public void resetResumeGame() {
        resumeGame = false;
    }

    public void stopMenuMusic() {
        if (audioManager != null) {
            audioManager.stopMenuMusic();
        }
    }
}
