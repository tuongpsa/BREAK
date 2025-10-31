package game.render;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Class để vẽ giao diện menu chính
 */
public class MenuRenderer {
    
    public void render(GraphicsContext gc, double width, double height) {
        // Vẽ background gradient
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 0, 1, true,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.color(0.1, 0.1, 0.3)), // Xanh đậm
            new Stop(1, Color.color(0.0, 0.0, 0.1))  // Đen
        );
        gc.setFill(backgroundGradient);
        gc.fillRect(0, 0, width, height);
        
        // Vẽ tiêu đề game
        String title = "BRICK BREAKER";
        Font titleFont = Font.loadFont("file:assets/upheavtt.ttf", 48);
        gc.setFont(titleFont);
        gc.setTextAlign(TextAlignment.CENTER);
        
        // Vẽ bóng cho tiêu đề
        gc.setFill(Color.color(0, 0, 0, 0.5));
        gc.fillText(title, width / 2 + 2, height / 2 - 100 + 2);
        
        // Vẽ viền cho tiêu đề
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.strokeText(title, width / 2, height / 2 - 100);
        
        // Vẽ tiêu đề với gradient
        double phase = 0.5 + 0.5 * Math.sin(System.nanoTime() / 1_000_000_000.0);
        Color titleColor = Color.color(
            0.2 + 0.8 * phase,  // R
            0.5 + 0.5 * phase,  // G  
            1.0,                 // B
            0.9
        );
        gc.setFill(titleColor);
        gc.fillText(title, width / 2, height / 2 - 100);
        
        // Vẽ nút Continue (chỉ khi có save trong phiên hiện tại)
        boolean hasSave = game.core.SaveManager.hasSessionSave();
        if (hasSave) {
            drawButton(gc, width, height, "CONTINUE", height / 2 - 100, 200, 50);
            // Vẽ nút Start ngay bên dưới
            drawButton(gc, width, height, "START GAME", height / 2 - 40, 200, 50);
        } else {
            // Không có save: chỉ vẽ Start ở vị trí mặc định
            drawButton(gc, width, height, "START GAME", height / 2 - 40, 200, 50);
        }
        
        // Vẽ nút High Score
        drawButton(gc, width, height, "HIGH SCORE", height / 2 + 20, 200, 50);
        
        // Vẽ nút Quit
        drawButton(gc, width, height, "QUIT", height / 2 + 80, 150, 40);
        
        // Vẽ hướng dẫn
        String instruction = hasSave ? "Click CONTINUE or START" : "Click START GAME to begin";
        Font instructionFont = Font.loadFont("file:assets/VSWISEC.ttf", 16);
        gc.setFont(instructionFont);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(instruction, width / 2, height / 2 + 140);
        
        // Vẽ thông tin game
        String info = "Use LEFT/RIGHT arrow keys to move paddle";
        gc.setFont(Font.font(14));
        gc.setFill(Color.GRAY);
        gc.fillText(info, width / 2, height / 2 + 170);
    }
    
    private void drawButton(GraphicsContext gc, double width, double height, String text, double y, double buttonWidth, double buttonHeight) {
        double x = (width - buttonWidth) / 2;
        
        // Vẽ bóng nút
        gc.setFill(Color.color(0, 0, 0, 0.3));
        gc.fillRoundRect(x + 2, y + 2, buttonWidth, buttonHeight, 10, 10);
        
        // Vẽ nút với gradient
        LinearGradient buttonGradient = new LinearGradient(
            0, 0, 0, 1, true,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.color(0.2, 0.4, 0.8)),  // Xanh dương
            new Stop(1, Color.color(0.1, 0.2, 0.5))   // Xanh đậm
        );
        gc.setFill(buttonGradient);
        gc.fillRoundRect(x, y, buttonWidth, buttonHeight, 10, 10);
        
        // Vẽ viền nút
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRoundRect(x, y, buttonWidth, buttonHeight, 10, 10);
        
        // Vẽ text trên nút
        Font buttonFont = Font.loadFont("file:assets/ari-w9500-display.ttf", 20);
        gc.setFont(buttonFont);
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text, width / 2, y + buttonHeight / 2 + 7);
    }
}
