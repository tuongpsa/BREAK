import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod; // chọn cách gradient lặp lại chính nó
import javafx.scene.paint.LinearGradient; //Tạo gradient
import javafx.scene.paint.Stop; // điểm trong gradient
import javafx.scene.text.Font;
import javafx.scene.canvas.GraphicsContext;

/**
 * effects cho "GAME OVER".
 * show score.
 */
public class GameOverRenderer {
    public void render(GraphicsContext gc, double width, double height, int score) {

        //Set font chữ cho gameover
        String gameOver = "GAME OVER";;
        Font gameOverFont = Font.loadFont("file:assets/upheavtt.ttf", 64);
            //Vẽ bóng
        gc.setFont(gameOverFont);
        gc.setFill(Color.color(0, 0, 0, 0.6)); // red = 0, green = 0, blue = 0, trong suốt = 0.6 / 1.0
        gc.fillText(gameOver, width / 2 - 158, height / 2 - 64);
            // Viền
        gc.setStroke(Color.WHITE); // viền trắng
        gc.setLineWidth(6); //độ dày viền 6px
        gc.strokeText(gameOver, width / 2 - 160, height / 2 - 70); // vẽ viền xung quanh
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
        gc.fillText(gameOver, width/ 2 - 160, height / 2 - 70);// tô text bằng gradient

        // Vẽ score
        String scoreText = "Score: ";
        Font scoreFont = Font.loadFont("file:assets/ari-w9500-display.ttf", 35);
        gc.setFont(scoreFont);
            // vẽ viền
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(4); //độ dày viền 2px
        gc.strokeText(scoreText + score, width / 2 - 85, height / 2 - 10); // vẽ viền xung quanh

        gc.setFill(Color.GREEN);
        gc.fillText(scoreText + score, width / 2 - 85, height / 2 - 10);

        // Vẽ message
        String message = "You Are Chicken!!!";
        Font messageFont = Font.loadFont("file:assets/PixelWarden.ttf", 33);
        gc.setFont(messageFont);
            // vẽ viền
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(4); //độ dày viền 2px
        gc.strokeText(message, width / 2 - 125, height / 2 - 180); // vẽ viền xung quanh

        gc.setFill(Color.CRIMSON);
        gc.fillText(message, width / 2 - 125, height / 2 - 180);

        // Vẽ instruction
        String gameOverInstruction = "Press R to RESTART or ESC to QUIT";
        Font gameOverInstructionFont = Font.loadFont("file:assets/VSWISEC.ttf", 22);
            // Vẽ bóng
        gc.setFont(gameOverInstructionFont);
        gc.setFill(Color.PURPLE);
        gc.fillText(gameOverInstruction,  width / 2 - 127, height / 2 + 42);
            // Viền
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2); //độ dày viền 2px
        gc.strokeText(gameOverInstruction, width / 2 - 130, height / 2 + 40); // vẽ viền xung quanh

        gc.setFill(Color.WHITE);// nền chữ màu trắng
        gc.fillText(gameOverInstruction, width / 2 - 130, height / 2 + 40);
    }
}
