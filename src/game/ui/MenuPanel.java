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
    private AudioManager audioManager;
    private boolean startGame = false;
    private boolean quitGame = false;
    private boolean showHighScore = false;
    private boolean resumeGame = false;
    private boolean continueGame = false;

    // --- BIẾN MỚI CHO HIỆU ỨNG HOVER ---
    private String hoveredButton = ""; // Lưu tên nút đang được hover ("START", "QUIT", "")

    public MenuPanel(double width, double height, AudioManager audioManager) {
        super(width, height);
        menuRenderer = new MenuRenderer();
        this.audioManager = audioManager;

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
                // --- ĐÃ CẬP NHẬT ---
                handleMouseMove(event.getX(), event.getY());
            }
        });

        startMenuLoop();

        if (this.audioManager != null) {
            this.audioManager.playMenuMusic();
        }
    }

    private void handleMouseClick(double x, double y) {
        // Biến kiểm tra xem có click vào nút nào không để phát âm thanh
        boolean buttonClicked = false;

        if (isPointInStartButton(x, y)) {
            startGame = true;
            buttonClicked = true;
        }
        else if (isPointInHighScoreButton(x, y)) {
            showHighScore = true;
            buttonClicked = true;
        }
        else if (isPointInResumeButton(x, y)) { // (Dùng cho Pause Menu)
            resumeGame = true;
            buttonClicked = true;
        }
        else if (isPointInQuitButton(x, y)) {
            quitGame = true;
            buttonClicked = true;
        }
        else if (isPointInContinueButton(x, y) && game.core.SaveManager.hasSessionSave()) {
            continueGame = true;
            buttonClicked = true;
        }

        // Phát âm thanh click nếu click trúng một nút
        if (buttonClicked && audioManager != null) {
            // (Giả sử bạn có một âm thanh "click" trong AudioManager)
            // audioManager.playClickSound();
        }
    }

    // --- HÀM MỚI ĐƯỢC VIẾT LẠI ---
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

        // Chỉ phát âm thanh *một lần* khi trạng thái hover thay đổi
        if (!oldHover.equals(newHover) && !newHover.isEmpty()) {
            if (audioManager != null) {
                //audioManager.playHoverSound();
            }
        }

    }

    private void render() {
        // Truyền trạng thái "hoveredButton" cho renderer
        menuRenderer.render(getGraphicsContext2D(), getWidth(), getHeight(), hoveredButton);
    }

    // --- CÁC HÀM TÍNH TOÁN TỌA ĐỘ ---
    // Các hàm này PHẢI KHỚP với tọa độ trong MenuRenderer
    // (Giả định MenuRenderer vẽ panel W-4.png ở giữa)

    private double getPanelY() {
        double panelHeight = 500; // Phải giống trong MenuRenderer
        return (getHeight() - panelHeight) / 2;
    }

    private double getBaseY() {
        double panelHeight = 500; // Phải giống trong MenuRenderer
        return getPanelY() + panelHeight / 2;
    }

    private boolean isPointInStartButton(double x, double y) {
        boolean hasSave = game.core.SaveManager.hasSessionSave();
        double buttonWidth = 200;
        double buttonHeight = 50;
        double buttonX = (getWidth() - buttonWidth) / 2;
        // Tọa độ Y thay đổi dựa trên việc có save hay không
        double buttonY = hasSave ? getBaseY() : getBaseY(); // Gốc Y là baseY

        return x >= buttonX && x <= buttonX + buttonWidth &&
                y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInHighScoreButton(double x, double y) {
        double buttonWidth = 200;
        double buttonHeight = 50;
        double buttonX = (getWidth() - buttonWidth) / 2;
        double buttonY = getBaseY() + 60; // (baseY + 60)

        return x >= buttonX && x <= buttonX + buttonWidth &&
                y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInResumeButton(double x, double y) {
        // (Đây là nút cho Pause Menu, có thể bạn sẽ cần tọa độ khác)
        // (Tạm thời dùng tọa độ của Continue)
        double buttonWidth = 200;
        double buttonHeight = 50;
        double buttonX = (getWidth() - buttonWidth) / 2;
        double buttonY = getBaseY() - 60; // (baseY - 60)

        return x >= buttonX && x <= buttonX + buttonWidth &&
                y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInQuitButton(double x, double y) {
        double buttonWidth = 150;
        double buttonHeight = 40;
        double buttonX = (getWidth() - buttonWidth) / 2;
        double buttonY = getBaseY() + 120; // (baseY + 120)

        return x >= buttonX && x <= buttonX + buttonWidth &&
                y >= buttonY && y <= buttonY + buttonHeight;
    }

    private boolean isPointInContinueButton(double x, double y) {
        if (!game.core.SaveManager.hasSessionSave()) return false;
        double buttonWidth = 200;
        double buttonHeight = 50;
        double buttonX = (getWidth() - buttonWidth) / 2;
        double buttonY = getBaseY() - 60; // (baseY - 60)

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
        if (audioManager != null) {
            audioManager.stopMenuMusic();
        }
    }
}
