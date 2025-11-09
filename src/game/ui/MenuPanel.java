package game.ui;

import game.audio.AudioManager;
import game.render.MenuRenderer;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

/**
 * MenuPanel class gộp tất cả logic - không cần kế thừa phức tạp
 *  Hỗ trợ hiệu ứng Hover
 */
public class MenuPanel extends Canvas {
    private MenuRenderer menuRenderer;
    private boolean startGame = false;
    private boolean quitGame = false;
    private boolean showHighScore = false;
    private boolean resumeGame = false;
    private boolean continueGame = false;

    // --- BIẾN MỚI CHO HIỆU ỨNG HOVER ---
    private String hoveredButton = ""; // Lưu tên nút đang được hover ("START", "QUIT", "")

    public MenuPanel(double width, double height) {
        super(width, height);
        menuRenderer = new MenuRenderer();
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

        // Xử lý sự kiện di chuyển chuột
        this.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleMouseMove(event.getX(), event.getY());
            }
        });

        startMenuLoop();

        AudioManager.getInstance().playMenuMusic();
    }

    private void handleMouseClick(double x, double y) {
        boolean hasSave = game.core.SaveManager.hasSessionSave();

        // Kiểm tra Continue TRƯỚC Start (vì Continue ở trên Start)
        if (hasSave && isPointInContinueButton(x, y)) {
            continueGame = true;
        }
        else if (isPointInStartButton(x, y)) {
            startGame = true;
        }
        else if (isPointInHighScoreButton(x, y)) {
            showHighScore = true;
        }
        else if (isPointInResumeButton(x, y)) { // (Dùng cho Pause Menu)
            resumeGame = true;
        }
        else if (isPointInQuitButton(x, y)) {
            quitGame = true;
        }
    }

    private void handleMouseMove(double x, double y) {
        String oldHover = hoveredButton;
        String newHover = ""; // Mặc định là không hover gì

        boolean hasSave = game.core.SaveManager.hasSessionSave();

        // Kiểm tra thứ tự (từ trên xuống dưới)
        if (hasSave && isPointInContinueButton(x, y)) {
            newHover = "CONTINUE";
        } else if (isPointInStartButton(x, y)) {
            newHover = "START";
        } else if (isPointInHighScoreButton(x, y)) {
            newHover = "HIGH_SCORE";
        } else if (isPointInQuitButton(x, y)) {
            newHover = "QUIT";
        } else if (isPointInResumeButton(x, y)) { // (Dùng cho Pause Menu)
            newHover = "RESUME";
        }

        hoveredButton = newHover;


    }

    private void render() {
        // Truyền trạng thái "hoveredButton" cho renderer
        menuRenderer.render(getGraphicsContext2D(), getWidth(), getHeight(), hoveredButton);
    }

    // --- CÁC HÀM TÍNH TOÁN TỌA ĐỘ ---
    // (Giả định MenuRenderer vẽ panel W-4.png ở giữa)

    private double getPanelY() {
        double panelHeight = 600; // Phải giống trong MenuRenderer
        return (getHeight() - panelHeight) / 2;
    }

    private double getBaseY() {
        double panelHeight = 600; // Phải giống trong MenuRenderer
        return getPanelY() + panelHeight / 2 - 100;
    }

    private boolean isPointInStartButton(double x, double y) {
        boolean hasSave = game.core.SaveManager.hasSessionSave();
        double buttonWidth = 250;
        double buttonHeight = 80;
        double buttonX = (getWidth() - buttonWidth) / 2 + 10;
        // Tọa độ Y thay đổi dựa trên việc có save hay không
        double buttonY = getBaseY() - 30; // Gốc Y là baseY

        return x >= buttonX && x <= buttonX + buttonWidth &&
                y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInHighScoreButton(double x, double y) {
        double buttonWidth = 250;
        double buttonHeight = 80;
        double buttonX = (getWidth() - buttonWidth) / 2 + 10;
        double buttonY = getBaseY() + 30;

        return x >= buttonX && x <= buttonX + buttonWidth &&
                y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInResumeButton(double x, double y) {
        // (Đây là nút cho Pause Menu, có thể bạn sẽ cần tọa độ khác)
        // (Tạm thời dùng tọa độ của Continue)
        double buttonWidth = 250;
        double buttonHeight = 80;
        double buttonX = (getWidth() - buttonWidth) / 2 + 10;
        double buttonY = getBaseY() - 90; // (baseY - 90)

        return x >= buttonX && x <= buttonX + buttonWidth &&
                y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInQuitButton(double x, double y) {
        double buttonWidth = 150;
        double buttonHeight = 80;
        double buttonX = (getWidth() - buttonWidth) / 2 +10;
        double buttonY = getBaseY() + 90; // (baseY + 90)

        return x >= buttonX && x <= buttonX + buttonWidth &&
                y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInContinueButton(double x, double y) {
        if (!game.core.SaveManager.hasSessionSave()) return false;
        double buttonWidth = 250;
        double buttonHeight = 80;
        double buttonX = (getWidth() - buttonWidth) / 2 + 10;
        double buttonY = getBaseY() - 90; // (baseY - 90)

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

    // --- Getters & Resets ---
    public boolean isStartGame() { return startGame; }
    public boolean isQuitGame() { return quitGame; }
    public boolean isShowHighScore() { return showHighScore; }
    public boolean isResumeGame() { return resumeGame; }
    public boolean isContinueGame() { return continueGame; }
    public void resetStartGame() { startGame = false; }
    public void resetQuitGame() { quitGame = false; }
    public void resetShowHighScore() { showHighScore = false; }
    public void resetResumeGame() { resumeGame = false; }
    public void resetContinueGame() { continueGame = false; }

    public void stopMenuMusic() {
        AudioManager.getInstance().stopMenuMusic();
    }
    public void playMenuMusic() {
        AudioManager.getInstance().playMenuMusic();
    }
}
