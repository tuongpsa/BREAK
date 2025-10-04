import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Background {

    // Chỉ sử dụng 1 ảnh background
    private Image background;

    /**
     * Constructor đơn giản chỉ cần 1 ảnh background.
     * @param backgroundLink link đến ảnh background chính.
     */
    public Background(String backgroundLink) {
        // Load ảnh background
        this.background = new Image("file:" + backgroundLink);

        // Check xem có load được ảnh không
        if (background.isError()) {
            System.out.println("Background image load error: " + backgroundLink);
        } else {
            System.out.println("Background image loaded successfully: " + backgroundLink);
        }
    }

    /**
     * Không cần update vì chỉ có 1 ảnh background tĩnh.
     */
    public void updateBackground() {
        // Không cần làm gì vì background tĩnh
    }

    /**
     * Vẽ background đơn giản.
     * @param gc đối tượng GraphicsContext.
     * @param width Chiều rộng màn hình.
     * @param height Chiều cao màn hình.
     */
    public void drawBackground(GraphicsContext gc, double width, double height) {
        // Chỉ vẽ 1 ảnh background
        if (background != null && !background.isError()) {
            gc.drawImage(background, 0, 0, width, height);
        } else {
            // Fallback: vẽ background màu đen nếu không load được ảnh
            gc.setFill(javafx.scene.paint.Color.BLACK);
            gc.fillRect(0, 0, width, height);
        }
    }
}
