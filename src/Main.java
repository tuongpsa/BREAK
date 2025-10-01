import javax.swing.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane; // Canvas or Image
import javafx.stage.Stage;

public class Main {
    public static void main(String[] args) {
        // Tạo JFrame
        JFrame frame = new JFrame("Brick Breaker Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 820); // khớp với tọa độ game
        frame.setResizable(false);

        // Tạo GamePanel
        GamePanel panel = new GamePanel();
        frame.add(panel);
public class Main extends Application {
    @Override // ghi đè lên phg thức start của lớp Application
    public void start(Stage stage) {

        // Hiển thị JFrame
        frame.setVisible(true);
        GamePanel panel = new GamePanel(480, 820); // width, height
        StackPane root = new StackPane(panel);//Scene chỉ nhận Parent -> cần tạo root
        // panel là 1 Node con của root
        Scene scene = new Scene(root, 480, 820);

        // Bắt panel nhận focus để nhận bàn phím
        panel.requestFocusInWindow();
        stage.setTitle("Brick Breaker Demo"); // tên tiêu đề
        stage.setScene(scene); // hiển thị UI
        stage.show(); // show cửa sổ game

        // Bắt đầu vòng lặp game
        panel.startGameLoop();
        // Đảm bảo panel nhận focus để bắt phím
        panel.requestFocus();
    }

    public static void main(String[] args) {
        launch(args); // chạy JavaFX
    }
}
