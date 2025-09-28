import javax.swing.*;

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

        // Hiển thị JFrame
        frame.setVisible(true);

        // Bắt panel nhận focus để nhận bàn phím
        panel.requestFocusInWindow();

        // Bắt đầu vòng lặp game
        panel.startGameLoop();
    }
}
