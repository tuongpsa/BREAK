package game.render;

import game.score.HighScoreManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * Class để vẽ giao diện bảng xếp hạng điểm cao
 */
public class HighScoreRenderer {
    
    public void render(GraphicsContext gc, double width, double height, List<HighScoreManager.HighScoreEntry> highScores) {
        // Vẽ background gradient
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 0, 1, true,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.color(0.1, 0.1, 0.3)), // Xanh đậm
            new Stop(1, Color.color(0.0, 0.0, 0.1))  // Đen
        );
        gc.setFill(backgroundGradient);
        gc.fillRect(0, 0, width, height);
        
        // Vẽ tiêu đề
        String title = "HIGH SCORES";
        Font titleFont = Font.loadFont("file:assets/upheavtt.ttf", 36);
        gc.setFont(titleFont);
        gc.setTextAlign(TextAlignment.CENTER);
        
        // Vẽ bóng cho tiêu đề
        gc.setFill(Color.color(0, 0, 0, 0.5));
        gc.fillText(title, width / 2 + 2, 80 + 2);
        
        // Vẽ viền cho tiêu đề
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeText(title, width / 2, 80);
        
        // Vẽ tiêu đề với gradient
        double phase = 0.5 + 0.5 * Math.sin(System.nanoTime() / 1_000_000_000.0);
        Color titleColor = Color.color(
            0.2 + 0.8 * phase,  // R
            0.5 + 0.5 * phase,  // G  
            1.0,                 // B
            0.9
        );
        gc.setFill(titleColor);
        gc.fillText(title, width / 2, 80);
        
        // Vẽ bảng xếp hạng
        drawLeaderboard(gc, width, height, highScores);
        
        // Vẽ nút Back to Menu
        drawButton(gc, width, height, "BACK TO MENU", height - 100, 200, 50);
        
        // Vẽ hướng dẫn
        String instruction = "Click BACK TO MENU to return";
        Font instructionFont = Font.loadFont("file:assets/VSWISEC.ttf", 14);
        gc.setFont(instructionFont);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(instruction, width / 2, height - 40);
    }
    
    private void drawLeaderboard(GraphicsContext gc, double width, double height, List<HighScoreManager.HighScoreEntry> highScores) {
        double startY = 150;
        double rowHeight = 60;
        double tableWidth = width - 80;
        double tableX = (width - tableWidth) / 2;
        
        // Vẽ background cho bảng
        gc.setFill(Color.color(0, 0, 0, 0.3));
        gc.fillRoundRect(tableX, startY - 10, tableWidth, Math.max(5, highScores.size()) * rowHeight + 20, 15, 15);
        
        // Vẽ viền bảng
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRoundRect(tableX, startY - 10, tableWidth, Math.max(5, highScores.size()) * rowHeight + 20, 15, 15);
        
        // Vẽ header
        Font headerFont = Font.loadFont("file:assets/ari-w9500-display.ttf", 18);
        gc.setFont(headerFont);
        gc.setFill(Color.YELLOW);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("RANK", tableX + 50, startY - 20);
        gc.fillText("PLAYER", tableX + tableWidth / 2, startY - 20);
        gc.fillText("SCORE", tableX + tableWidth - 50, startY - 20);
        
        // Vẽ danh sách điểm
        Font scoreFont = Font.loadFont("file:assets/VSWISEC.ttf", 16);
        gc.setFont(scoreFont);
        
        if (highScores.isEmpty()) {
            // Hiển thị thông báo khi chưa có điểm
            gc.setFill(Color.GRAY);
            gc.fillText("No scores yet. Play the game to set a record!", width / 2, startY + 50);
        } else {
            for (int i = 0; i < highScores.size(); i++) {
                HighScoreManager.HighScoreEntry entry = highScores.get(i);
                double y = startY + i * rowHeight;
                
                // Màu sắc khác nhau cho top 3
                Color rankColor;
                if (i == 0) {
                    rankColor = Color.GOLD; // Hạng 1 - vàng
                } else if (i == 1) {
                    rankColor = Color.SILVER; // Hạng 2 - bạc
                } else if (i == 2) {
                    rankColor = Color.color(0.8, 0.5, 0.2); // Hạng 3 - đồng
                } else {
                    rankColor = Color.WHITE; // Các hạng khác - trắng
                }
                
                // Vẽ rank
                gc.setFill(rankColor);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText("#" + (i + 1), tableX + 50, y);
                
                // Vẽ tên người chơi
                gc.setFill(Color.WHITE);
                gc.fillText(entry.getPlayerName(), tableX + tableWidth / 2, y);
                
                // Vẽ điểm
                gc.setFill(rankColor);
                gc.fillText(String.valueOf(entry.getScore()), tableX + tableWidth - 50, y);
                
                // Vẽ ngày (nhỏ hơn)
                Font dateFont = Font.loadFont("file:assets/VSWISEC.ttf", 12);
                gc.setFont(dateFont);
                gc.setFill(Color.GRAY);
                gc.fillText(entry.getFormattedDate(), tableX + tableWidth / 2, y + 20);
                
                // Đặt lại font cho dòng tiếp theo
                gc.setFont(scoreFont);
            }
        }
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
        Font buttonFont = Font.loadFont("file:assets/ari-w9500-display.ttf", 18);
        gc.setFont(buttonFont);
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text, width / 2, y + buttonHeight / 2 + 6);
    }
}
