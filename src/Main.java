import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane; // Canvas or Image
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Main extends Application {
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
        
        // Tạo menu panel
        menuPanel = new MenuPanel(480, 820);
        StackPane menuRoot = new StackPane(menuPanel);
        menuScene = new Scene(menuRoot, 480, 820);
        
        // Tạo high score panel
        highScorePanel = new HighScorePanel(480, 820);
        StackPane highScoreRoot = new StackPane(highScorePanel);
        highScoreScene = new Scene(highScoreRoot, 480, 820);
        
        // Tạo game panel
        gamePanel = new GamePanel(480, 820);
        StackPane gameRoot = new StackPane(gamePanel);
        gameScene = new Scene(gameRoot, 480, 820);

        stage.setTitle("Brick Breaker Demo"); // tên tiêu đề
        stage.setScene(menuScene); // hiển thị menu trước
        stage.show(); // show cửa sổ game
        
        // Đảm bảo menu panel nhận focus
        menuPanel.requestFocus();
        
        // Bắt đầu vòng lặp kiểm tra menu
        startMenuLoop();
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
        
        // Reset game nếu gamePanel đã tồn tại
        if (gamePanel != null) {
            gamePanel.getGame().resetGame();
        } else {
            // Tạo gamePanel mới chỉ lần đầu
            gamePanel = new GamePanel(480, 820);
            StackPane gameRoot = new StackPane(gamePanel);
            gameScene = new Scene(gameRoot, 480, 820);
        }
        
        primaryStage.setScene(gameScene);
        gamePanel.setMenuCallback(() -> switchToMenu());
        gamePanel.startGameLoop();
        gamePanel.requestFocus();
        
        // Thêm vòng lặp kiểm tra game over để quay về menu
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
                if (gamePanel.getGame().isGameOver()) {
                    // Không cần xử lý phím ở đây, GamePanel sẽ tự xử lý
                }
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
        if (menuPanel != null) {
            menuPanel.stopMenuMusic(); // Dừng nhạc cũ trước
            // Nhạc menu sẽ tự động bắt đầu lại trong MenuPanel constructor
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
