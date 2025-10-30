package game.main;

import game.audio.AudioManager;
import game.core.GameSettings;
import game.core.PauseListener;
import game.core.PauseManager;
import game.ui.GamePanel;
import game.ui.HighScorePanel;
import game.ui.MenuPanel;
import game.ui.PauseMenu;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    private GameSettings gameSettings;
    private AudioManager audioManager;
    private PauseManager pauseManager;
    private PauseMenu pauseMenu;

    private Stage primaryStage;
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    private HighScorePanel highScorePanel;
    private Scene gameScene;
    private Scene menuScene;
    private Scene highScoreScene;
    private AnimationTimer gameOverTimer;


    @Override // ghi đè lên phg thức start của lớp Application
    public void start(Stage stage) {
        this.primaryStage = stage;

        gameSettings = new GameSettings();
        // Truyền settings vào AudioManager
        audioManager = new AudioManager(gameSettings);
        pauseManager = new PauseManager();
        
        // Tạo menu panel
        menuPanel = new MenuPanel(480, 820, audioManager);
        StackPane menuRoot = new StackPane(menuPanel);
        menuScene = new Scene(menuRoot, 480, 820);
        
        // Tạo high score panel
        highScorePanel = new HighScorePanel(480, 820);
        StackPane highScoreRoot = new StackPane(highScorePanel);
        highScoreScene = new Scene(highScoreRoot, 480, 820);
        
        // Tạo game panel
        // 1. Tạo GamePanel và TRUYỀN các hệ thống vào
        // (Bạn cần sửa constructor của GamePanel.java để nhận chúng)
        gamePanel = new GamePanel(480, 820, gameSettings, audioManager, pauseManager);

        // 2. Tạo PauseMenu (UI)
        pauseMenu = new PauseMenu(
                pauseManager,
                gameSettings,
                audioManager,
                () -> gamePanel.getGame().resetGame(), // Hàm Restart
                this::switchToMenu                      // Hàm Exit (quay về Menu)
        );

        // 3. Dùng StackPane để xếp chồng PauseMenu LÊN TRÊN GamePanel
        StackPane gameRoot = new StackPane(gamePanel, pauseMenu.getRootNode());
        gameScene = new Scene(gameRoot, 480, 820);

        stage.setTitle("Brick Breaker Demo");
        stage.setScene(menuScene);
        stage.show();

        // 4. Thiết lập Input và Listener (Rất quan trọng)
        setupGameInput(gameScene);
        setupPauseListeners();

        menuPanel.requestFocus();
        startMenuLoop();
    }
    // <<< THÊM HÀM 1: Hàm setupPauseListeners() bị thiếu
    /**
     * Kết nối PauseManager với PauseMenu
     * để tự động ẨN/HIỆN menu khi pause/resume.
     */
    private void setupPauseListeners() {
        pauseManager.addListener(new PauseListener() {
            @Override
            public void onGamePaused() {
                Platform.runLater(() -> pauseMenu.show());
            }

            @Override
            public void onGameResumed() {
                Platform.runLater(() -> {
                    pauseMenu.hide();
                    gamePanel.requestFocus(); // Lấy lại focus cho game
                });
            }
        });
    }

    // <<< THÊM HÀM 2: Hàm setupInputHandlers() bị thiếu
    /**
     * Thiết lập xử lý input (phím ESC) cho GameScene.
     * Các phím di chuyển (A/D, Mũi tên) đã được GamePanel tự xử lý.
     */
    private void setupGameInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            // Luôn bắt phím ESC để pause/toggle
            if (e.getCode() == KeyCode.ESCAPE) {
                // Nếu game over, phím ESC sẽ do GamePanel xử lý (để quit)
                if (gamePanel != null && !gamePanel.getGame().isGameOver()) {
                    pauseManager.toggle();
                }
            }

            // Các phím khác (R, A, D, Left, Right)
            // sẽ tự động được GamePanel xử lý
            // vì GamePanel (GameScreen) cũng có listener riêng.
        });
    }
    private void startMenuLoop() {
        AnimationTimer menuTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (menuPanel.isStartGame()) {
                    // Chuyển sang game
                    switchToGame();
                    this.stop();
                } else if (menuPanel.isShowHighScore()) {
                    // Chuyển sang high score
                    switchToHighScore();
                    this.stop();
                } else if (menuPanel.isQuitGame()) {
                    // Thoát game
                    primaryStage.close();
                    this.stop();
                }
            }
        };
        menuTimer.start();
    }
    
    private void switchToGame() {
        // Dừng nhạc menu
        if (menuPanel != null) {
            menuPanel.stopMenuMusic();
        }
// <<< SỬA LỖI 2: Xóa khối "else"
        // GamePanel đã được khởi tạo 1 lần (và duy nhất) trong hàm start().
        // Chúng ta chỉ cần reset game.
        gamePanel.getGame().resetGame();

        primaryStage.setScene(gameScene);
        gamePanel.setMenuCallback(this::switchToMenu);
        gamePanel.startGameLoop();
        gamePanel.requestFocus();

        // Gắn input (phím ESC) cho GameScene
        setupGameInput(gameScene);

        startGameOverLoop();
    }
    
    private void startGameOverLoop() {
        // Dừng timer cũ nếu có
        if (gameOverTimer != null) {
            gameOverTimer.stop();
        }
        gameOverTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // (Không cần làm gì, GamePanel tự xử lý input R/ESC)
            }
        };
        gameOverTimer.start();
    }
    
    private void switchToMenu() {
        // Dừng game loop cũ nếu có
        if (gamePanel != null) {
            gamePanel.stopGameLoop();
        }
        
        primaryStage.setScene(menuScene);
        menuPanel.resetStartGame();
        menuPanel.resetQuitGame();
        menuPanel.resetShowHighScore();
        menuPanel.requestFocus();
        
        // Bắt đầu lại nhạc menu
// <<< SỬA LỖI 3: Khởi động lại nhạc menu
        // Dùng AudioManager trung tâm
        if (audioManager != null) {
            audioManager.playMenuMusic();
        }

        startMenuLoop();
    }
    
    private void switchToHighScore() {
        primaryStage.setScene(highScoreScene);
        highScorePanel.resetBackToMenu();
        highScorePanel.requestFocus();
        
        // Bắt đầu vòng lặp kiểm tra nút Backl
        startHighScoreLoop();
    }
    
    private void startHighScoreLoop() {
        AnimationTimer highScoreTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (highScorePanel.isBackToMenu()) {
                    // Quay về menu
                    switchToMenu();
                    this.stop();
                }
            }
        };
        highScoreTimer.start();
    }

    public static void main(String[] args) {
        launch(args); // chạy JavaFX
    }
}
