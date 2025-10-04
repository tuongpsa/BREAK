import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;

/**
 * Class để vẽ giao diện menu chính
 */
public class MenuRenderer {
    private Image menuBackground;
    private Image titleImage;
    private Image chooseButton;
    private Image chooseButtonClicked;
    private boolean startButtonHovered = false;
    private boolean quitButtonHovered = false;
    
    public MenuRenderer() {
        // Load các ảnh
        try {
            menuBackground = new Image("file:assets/menu start.png");
            titleImage = new Image("file:assets/title.png");
            chooseButton = new Image("file:assets/choose.png");
            chooseButtonClicked = new Image("file:assets/choose(click).png");
        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh menu: " + e.getMessage());
        }
    }
    
    public void render(GraphicsContext gc, double width, double height) {
        // Vẽ background menu - nhỏ hơn
        if (menuBackground != null) {
            double menuWidth = width * 0.6; // 60% chiều rộng màn hình
            double menuHeight = menuWidth * (menuBackground.getHeight() / menuBackground.getWidth());
            double menuX = (width - menuWidth) / 2;
            double menuY = height * 0.3; // 30% từ trên xuống
            
            gc.drawImage(menuBackground, menuX, menuY, menuWidth, menuHeight);
        } else {
            // Fallback nếu không load được ảnh
            gc.setFill(Color.color(0.1, 0.1, 0.3));
            gc.fillRect(0, 0, width, height);
        }
        
        // Vẽ ảnh title ở đỉnh - to hơn menu start
        if (titleImage != null) {
            double titleWidth = width * 0.8; // 80% chiều rộng màn hình - to hơn menu start
            double titleHeight = titleWidth * (titleImage.getHeight() / titleImage.getWidth());
            double titleX = (width - titleWidth) / 2;
            double titleY = height * 0.05; // 5% từ trên xuống - ở đỉnh
            
            gc.drawImage(titleImage, titleX, titleY, titleWidth, titleHeight);
            
            // Thêm tên game vào bên trong ảnh title
            String gameTitle = "BRICK BREAKER";
            Font titleFont = Font.loadFont("file:assets/October Crow.ttf", 32);
            gc.setFont(titleFont);
            gc.setTextAlign(TextAlignment.CENTER);
            
            // Vẽ bóng cho text
            gc.setFill(Color.color(0, 0, 0, 0.7));
            gc.fillText(gameTitle, width / 2 + 2, titleY + titleHeight * 0.6 + 2);
            
            // Vẽ text chính với màu sáng
            gc.setFill(Color.WHITE);
            gc.fillText(gameTitle, width / 2, titleY + titleHeight * 0.6);
        } else {
            // Fallback: vẽ text title với font October Crow
            String title = "BRICK BREAKER";
            Font titleFont = Font.loadFont("file:assets/October Crow.ttf", 48);
            gc.setFont(titleFont);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFill(Color.WHITE);
            gc.fillText(title, width / 2, height * 0.15);
        }
        
        // Vẽ nút Start Game - đặt trong vùng menu start
        double menuStartY = height * 0.3; // Vị trí Y của menu start
        double menuStartHeight = width * 0.6 * (menuBackground.getHeight() / menuBackground.getWidth()); // Chiều cao menu start
        double buttonY1 = menuStartY + menuStartHeight * 0.3; // 30% từ trên menu start
        double buttonY2 = menuStartY + menuStartHeight * 0.6; // 60% từ trên menu start
        
        drawImageButton(gc, width, height, "START GAME", buttonY1, 200, 50, startButtonHovered);
        
        // Vẽ nút Quit
        drawImageButton(gc, width, height, "QUIT", buttonY2, 150, 40, quitButtonHovered);
        
        // Vẽ hướng dẫn - đặt dưới menu start
        String instruction = "Click START GAME to begin";
        Font instructionFont = Font.loadFont("file:assets/October Crow.ttf", 16);
        gc.setFont(instructionFont);
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        double instructionY = menuStartY + menuStartHeight + 30; // Dưới menu start
        gc.fillText(instruction, width / 2, instructionY);
        
        // Vẽ thông tin game
        String info = "Use LEFT/RIGHT arrow keys to move paddle";
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(info, width / 2, instructionY + 30);
    }
    
    private void drawImageButton(GraphicsContext gc, double width, double height, String text, double y, double buttonWidth, double buttonHeight, boolean isHovered) {
        double x = (width - buttonWidth) / 2;
        
        // Chọn ảnh nút dựa trên trạng thái hover
        Image buttonImage = isHovered ? chooseButtonClicked : chooseButton;
        
        if (buttonImage != null) {
            // Vẽ ảnh nút
            gc.drawImage(buttonImage, x, y, buttonWidth, buttonHeight);
        } else {
            // Fallback: vẽ nút bằng code cũ
            gc.setFill(Color.color(0.2, 0.4, 0.8));
            gc.fillRoundRect(x, y, buttonWidth, buttonHeight, 10, 10);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeRoundRect(x, y, buttonWidth, buttonHeight, 10, 10);
        }
        
        // Vẽ text trên nút
        Font buttonFont = Font.loadFont("file:assets/October Crow.ttf", 18);
        gc.setFont(buttonFont);
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text, width / 2, y + buttonHeight / 2 + 6);
    }
    
    // Methods để cập nhật trạng thái hover
    public void setStartButtonHovered(boolean hovered) {
        this.startButtonHovered = hovered;
    }
    
    public void setQuitButtonHovered(boolean hovered) {
        this.quitButtonHovered = hovered;
    }
}
