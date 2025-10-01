import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod; // chọn cách gradient lặp lại chính nó
import javafx.scene.paint.LinearGradient; //Tạo gradient
import javafx.scene.paint.Stop; // điểm trong gradient
import javafx.scene.text.Font;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 */
public class GameOverRenderer {
    public void render(GraphicsContext gc, double width, double height, int score) {

        //Set font chữ cho gameover
        String text = "GAME OVER";
        Font overFont = Font.loadFont("file:assets/upheavtt.ttf", 64);
        gc.setFont(overFont);
        gc.setFill(Color.color(0, 0, 0, 0.6)); // red = 0, green = 0, blue = 0, trong suốt = 0.6 / 1.0
        gc.fillText(text, width / 2 - 160, height / 2 + 6); // vẽ bóng mờ

        // Viền
        gc.setStroke(Color.WHITE); // viền trắng
        gc.setLineWidth(6); //độ dày viền 6px
        gc.strokeText(text, width / 2 - 160, height / 2); // vẽ viền xung quanh

        //giá trị nội suy màu theo hình sin theo chu kỳ 2.5 giây.
        double phase = 0.5 + 0.5 * Math.sin(System.nanoTime() / 2_500_000_000.0);
        //độ trong suốt dao động theo hình sin trên text
        double alpha = 0.7 + 0.3 * (0.5 + 0.5 * Math.sin(System.nanoTime() / 300_000_000.0));

        // Nội suy màu gradient
        //từ đỏ(1, 0, 0) -> xanh dương (0, 0, 1)
        Color startColor = Color.color(
                (1 - phase) * 1.0 + phase * 0.0,  // R giảm dần để chuyển qua B
                (1 - phase) * 0.0 + phase * 0.0,  // G = 0 vì ko phụ thuộc
                (1 - phase) * 0.0 + phase * 1.0,  // B rõ dần
                alpha
        );
        //từ vàng (1, 1, 0) → xanh lá (0, 1, 0)
        Color endColor = Color.color(
                (1 - phase) * 1.0 + phase * 0.0,  // R giảm dần từ 1 → 0 theo phase
                (1 - phase) * 1.0 + phase * 1.0,  // G luôn = 1 để tạo dc màu vàng
                (1 - phase) * 0.0 + phase * 0.0,  // B  ko cần tạo vàng qua  nên = 0
                alpha
        );

        // Gradient động
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 0, true,
                CycleMethod.NO_CYCLE, // không lặp lại gradient nếu quá vùng vẽ
                new Stop(0, startColor),  // gradient tại 0 dùng startColor
                new Stop(1, endColor)    // gradient tại 1 dùng endColor

        );
        gc.setFill(gradient);
        gc.fillText(text, width/ 2 - 160, height / 2);// tô text bằng gradient

        // Vẽ score khi gameover
        gc.setFill(Color.GREEN);
        Font scoreFont = Font.loadFont("file:assets/ari-w9500-display.ttf", 35);
        gc.setFont(scoreFont);
        gc.fillText("Score: " + score, width / 2 - 100, height / 2 + 60 );
    }
}
