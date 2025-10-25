package game.ui;

import game.audio.AudioManager;
import game.render.MenuRenderer;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * Class để quản lý menu chính và xử lý sự kiện chuột
 */
public class MenuPanel extends Canvas {
    private MenuRenderer menuRenderer;
    private AudioManager audioManager;
    private boolean startGame = false;
    private boolean quitGame = false;
    private boolean showHighScore = false;

    public MenuPanel(double width, double height) {
        super(width, height);
        menuRenderer = new MenuRenderer();
        audioManager = new AudioManager();

        // Thiết lập để nhận sự kiện chuột
        this.setFocusTraversable(true);

        // Xử lý sự kiện click chuột
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double mouseX = event.getX();
                double mouseY = event.getY();

                // Kiểm tra click vào nút Start game.core.Game
                if (isPointInStartButton(mouseX, mouseY)) {
                    startGame = true;
                }
                // Kiểm tra click vào nút High Score
                else if (isPointInHighScoreButton(mouseX, mouseY)) {
                    showHighScore = true;
                }
                // Kiểm tra click vào nút Quit
                else if (isPointInQuitButton(mouseX, mouseY)) {
                    quitGame = true;
                }
            }
        });

        // Xử lý sự kiện di chuyển chuột để highlight nút
        this.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Có thể thêm hiệu ứng highlight nút khi hover
            }
        });

        // Bắt đầu vòng lặp render menu
        startMenuLoop();

        // Bắt đầu nhạc menu
        if (audioManager != null) {
            audioManager.playMenuMusic();
        }
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
        GraphicsContext gc = getGraphicsContext2D();
        menuRenderer.render(gc, getWidth(), getHeight());
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

    public void resetStartGame() {
        startGame = false;
    }

    public void resetQuitGame() {
        quitGame = false;
    }

    public void resetShowHighScore() {
        showHighScore = false;
    }

    public void stopMenuMusic() {
        if (audioManager != null) {
            audioManager.stopMenuMusic();
        }
    }
}
