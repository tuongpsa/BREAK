package game.render;

import game.core.SaveManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Class để vẽ giao diện menu chính
 * Sử dụng tài sản hình ảnh và hiệu ứng HOVER
 * - Nền: background2.png
 * - Khung: W-4.png
 * - Icon: G-2.jpg
 * - Nút: choose.png (thường) và choose(click).png (hover)
 */
public class MenuRenderer {

    // --- Đường dẫn và Hình ảnh ---
    private final String ASSET_PATH = "assets/gui/gui/WindowPopUp/";

    // Các tài sản mới
    private Image backgroundImage;    // background2.png (nền đen)
    private Image menuPanelImage;     // W-4.png (khung menu xanh)
    private Image crownIcon;          // G-2.jpg (vương miện)

    // Các ảnh nút cho 2 trạng thái
    private Image buttonImage_Normal; // choose.png (nút thường)
    private Image buttonImage_Hover;  // choose(click).png (nút khi hover)

    // --- Fonts ---
    private Font titleFont;
    private Font buttonFont;
    private Font instructionFont;

    /**
     * Hàm khởi tạo (Constructor)
     * Load (tải) tất cả ảnh và font chữ 1 lần
     */
    public MenuRenderer() {
        try {
            // Load (tải) ảnh
            backgroundImage = loadImage("background2.png");
            menuPanelImage = loadImage("W-4.png");
            crownIcon = loadImage("G-1.png");
            buttonImage_Normal = loadImage("choose.png");
            buttonImage_Hover = loadImage("choose(click).png");

            // Load (tải) fonts (bạn có thể cần điều chỉnh đường dẫn)
            titleFont = loadFont("Spooky.ttf", 46);
            buttonFont = loadFont("Spooky.ttf", 20);
            instructionFont = loadFont("Spooky.ttf", 16);

        } catch (Exception e) {
            System.err.println("LỖI NGHIÊM TRỌNG: Không thể load assets cho MenuRenderer!");
            e.printStackTrace();
        }
    }

    /**
     * Hàm render (vẽ) chính - Đã được nâng cấp
     * @param gc GraphicsContext để vẽ
     * @param width Chiều rộng của Canvas
     * @param height Chiều cao của Canvas
     * @param hoveredButton Tên của nút đang được di chuột vào (ví dụ: "START", "QUIT"...)
     */
    public void render(GraphicsContext gc, double width, double height, String hoveredButton) {

        // --- 1. Vẽ Ảnh Nền (background2.png) ---
        if (backgroundImage != null) {
            // Vẽ ảnh nền, kéo dãn ra để vừa màn hình
            gc.drawImage(backgroundImage, 0, 0, width, height);
        } else {
            gc.setFill(Color.BLACK); // Dự phòng nếu load ảnh thất bại
            gc.fillRect(0, 0, width, height);
        }

        // --- 2. Tính toán tọa độ Khung Menu (W-4.png) ---
        // (Vẽ ở giữa màn hình)
        double panelWidth = 500;  // Chiều rộng mong muốn của khung
        double panelHeight = 600; // Chiều cao mong muốn của khung
        double panelX = (width - panelWidth) / 2;
        double panelY = (height - panelHeight) / 2;

        // Vẽ khung
        if (menuPanelImage != null) {
            gc.drawImage(menuPanelImage, panelX + 25, panelY, panelWidth, panelHeight);
        }

        // --- 3. Vẽ Icon Vương miện (G-2.jpg) ---
        // (Vẽ phía trên tiêu đề, bên trong khung)
        if (crownIcon != null) {
            gc.drawImage(crownIcon, width / 2 -250, panelY - 100, 500, 300);
        }

        // --- 4. Vẽ Chữ Tiêu đề (BRICK BREAKER) ---
        // (Vẽ bên trong khung, bên dưới vương miện)
        String title = "BRICK BREAKER";
        gc.setFont(titleFont);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.WHITE);
        gc.fillText(title, width / 2, panelY +50); // Tọa độ Y đã điều chỉnh

        // --- 5. Vẽ các nút ---
        // Tọa độ Y cơ sở (nằm giữa khung panel)
        double baseY = panelY + panelHeight / 2- 100;

        boolean hasSave = SaveManager.hasSessionSave();

        if (hasSave) {
            drawButton(gc, width, "CONTINUE", baseY-90, 250, 80, "CONTINUE", hoveredButton);
            drawButton(gc, width, "START GAME", baseY-30, 250, 80, "START", hoveredButton);
        } else {
            drawButton(gc, width, "START GAME", baseY-30, 250, 80, "START", hoveredButton);
        }

        // Các nút còn lại (High Score ở +60, Quit ở +120)
        drawButton(gc, width, "HIGH SCORE", baseY + 30, 250, 80, "HIGH_SCORE", hoveredButton);
        drawButton(gc, width, "QUIT", baseY + 90, 150, 80, "QUIT", hoveredButton);

        // --- 6. Vẽ Hướng dẫn ---
        String instruction = hasSave ? "Click CONTINUE or START" : "Click START GAME";
        gc.setFont(instructionFont);
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(instruction, width / 2 + 10, baseY + 180); // Bên dưới nút Quit
        String instructionPlay = "Use Arrow Keys to play";
        gc.fillText(instructionPlay, width / 2 + 10, baseY + 202); // (180 + 22)
        String instructionPause = "Press ESC to pause";
        gc.fillText(instructionPause, width / 2 + 10, baseY + 224); // (202 + 22)
    }

    /**
     * Hàm vẽ nút (Helper) - Đã được nâng cấp
     * Giờ đây nó vẽ ảnh dựa trên trạng thái 'hover'
     *
     * @param buttonIdentifier Tên định danh ("START", "QUIT"...) để so sánh
     * @param hoveredButton Tên nút mà chuột đang trỏ vào
     */
    private void drawButton(GraphicsContext gc, double width, String text, double y,
                            double buttonWidth, double buttonHeight,
                            String buttonIdentifier, String hoveredButton) {

        double x = (width - buttonWidth) / 2+10;

        // 1. CHỌN ảnh để vẽ (Normal hay Hover)
        Image imageToDraw;

        if (buttonIdentifier.equals(hoveredButton)) {
            imageToDraw = buttonImage_Hover; // Nếu đang hover, dùng ảnh hover
        } else {
            imageToDraw = buttonImage_Normal; // Mặc định là ảnh thường
        }

        // 2. Vẽ ảnh nút đã chọn
        if (imageToDraw != null) {
            gc.drawImage(imageToDraw, x, y, buttonWidth, buttonHeight);
        } else {
            // (Dự phòng nếu load ảnh thất bại)
            gc.setFill(buttonIdentifier.equals(hoveredButton) ? Color.color(0.4, 0.6, 1.0) : Color.color(0.2, 0.4, 0.8));
            gc.fillRoundRect(x, y, buttonWidth, buttonHeight, 10, 10);
            gc.setStroke(Color.WHITE);
            gc.strokeRoundRect(x, y, buttonWidth, buttonHeight, 10, 10);
        }

        // 3. Vẽ text trên nút
        gc.setFont(buttonFont);
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        // (Căn chỉnh Y của text cho khớp với ảnh nút)
        gc.fillText(text, width / 2 + 10, y + buttonHeight / 2 + 9);
    }

    // --- CÁC HÀM HELPER ĐỂ LOAD TÀI NGUYÊN ---

    /**
     * Helper để load ảnh từ thư mục assets
     */
    private Image loadImage(String fileName) {
        String path = ASSET_PATH + fileName;
        try {
            return new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            System.err.println("LỖI: Không thể tìm thấy file ảnh: " + path);
            return null;
        }
    }

    /**
     * Helper để load font một cách an toàn
     */
    private Font loadFont(String fileName, double size) {
        // (Giả sử font nằm ở assets/ chứ không phải assets/gui/...)
        String path = "assets/" + fileName;
        try {
            Font font = Font.loadFont(new FileInputStream(path), size);
            if (font != null) {
                return font;
            } else {
                // Nếu font load thất bại, dùng font hệ thống
                System.err.println("Font " + fileName + " load thất bại, dùng font Arial mặc định.");
                return Font.font("Arial", FontWeight.BOLD, size - 4);
            }
        } catch (FileNotFoundException e) {
            System.err.println("LỖI: Không tìm thấy file font: " + path + ". Dùng font Arial mặc định.");
            return Font.font("Arial", FontWeight.BOLD, size - 4);
        }
    }
}
