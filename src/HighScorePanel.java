import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

/**
 * Class để hiển thị bảng xếp hạng điểm cao
 */
public class HighScorePanel extends Canvas {
    private HighScoreRenderer highScoreRenderer;
    private HighScoreManager highScoreManager;
    private boolean backToMenu = false;
    
    public HighScorePanel(double width, double height) {
        super(width, height);
        highScoreRenderer = new HighScoreRenderer();
        highScoreManager = new HighScoreManager();
        
        // Thiết lập để nhận sự kiện chuột
        this.setFocusTraversable(true);
        
        // Xử lý sự kiện click chuột
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double mouseX = event.getX();
                double mouseY = event.getY();
                
                // Kiểm tra click vào nút Back to Menu
                if (isPointInBackButton(mouseX, mouseY)) {
                    backToMenu = true;
                }
            }
        });
        
        // Bắt đầu vòng lặp render
        startRenderLoop();
    }
    
    private boolean isPointInBackButton(double x, double y) {
        double buttonWidth = 200;
        double buttonHeight = 50;
        double buttonX = (getWidth() - buttonWidth) / 2;
        double buttonY = getHeight() - 100;
        
        return x >= buttonX && x <= buttonX + buttonWidth &&
               y >= buttonY && y <= buttonY + buttonHeight;
    }
    
    private void startRenderLoop() {
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
        highScoreRenderer.render(gc, getWidth(), getHeight(), highScoreManager.getHighScores());
    }
    
    // Getters
    public boolean isBackToMenu() {
        return backToMenu;
    }
    
    public void resetBackToMenu() {
        backToMenu = false;
    }
    
    public HighScoreManager getHighScoreManager() {
        return highScoreManager;
    }
}
