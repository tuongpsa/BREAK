import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane; // Canvas or Image
import javafx.stage.Stage;

public class Main extends Application {
    @Override // ghi đè lên phg thức start của lớp Application
    public void start(Stage stage) {

        GamePanel panel = new GamePanel(480, 820); // width, height
        StackPane root = new StackPane(panel);//Scene chỉ nhận Parent -> cần tạo root
        // panel là 1 Node con của root
        Scene scene = new Scene(root, 480, 820);

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
