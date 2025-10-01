import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
import javafx.animation.AnimationTimer; // tạo vòng lặp
import javafx.scene.canvas.Canvas; // một node để vẽ 2D
import javafx.scene.canvas.GraphicsContext; // như bút vẽ lên canvas
import javafx.scene.input.KeyCode; // cung cấp các phím
import javafx.scene.image.Image;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
    private Game game;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public GamePanel() {
    private boolean leftPressed = false; // Biến quản lí nút trái
    private boolean rightPressed = false; // Biến quản lí nút phải

    private Image ballImage;
    private Image paddleImage;
    private Image brickImage;

    private Background background;
    private GameOverRenderer gameOverRenderer;

    /**
     * Constructor có tham số.
     * @param width chiều dài
     * @param height chiều rộng
     */
    public GamePanel(double width, double height) {
        super(width, height); // gọi constructor của Canvas
        game = new Game();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;
            }
        // Load ảnh cho object game
        ballImage = new Image("file:assets/ball.panda.png");// ảnh ball
        paddleImage = new Image("file:assets/sword.png"); // ảnh paddle
        brickImage = new Image("file:assets/thanh2.png"); // ảnh brick
        background = new Background("assets/sky.png", "assets/mountain.png", "assets/cloud1.png", 0.3, 0.6);

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
            }
        });
        //Check lỗi load ảnh
        if (ballImage.isError() || paddleImage.isError() || brickImage.isError()) {
            System.out.println("Object game is error");
        }

        this.setFocusTraversable(true); // Canvas nhận lệnh từ keyboard
        this.setOnKeyPressed( // ghi nhận sự kiện khi nhấn phím
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent e) {
                        if (e.getCode() == KeyCode.LEFT) leftPressed = true;
                        if (e.getCode() == KeyCode.RIGHT) rightPressed = true;
                    }
                }
        );
        this.setOnKeyReleased( // ghi nhận sự kiện nhả phím
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent e) {
                        if (e.getCode() == KeyCode.LEFT) {
                            leftPressed = false;
                        }
                        if (e.getCode() == KeyCode.RIGHT) {
                            rightPressed = false;
                        }
                    }
                }
        );
    }

    public void startGameLoop() {
        Timer timer = new Timer(16, e -> { // khoảng 60 FPS
            updateGame();
            repaint();
        });
        timer.start();
    }

    private void updateGame() {
        float deltaTime = 0.016f;

        if (!game.isGameOver()) {
            // Di chuyển paddle
            if (leftPressed) game.getPaddle().moveLeft(deltaTime);
            if (rightPressed) game.getPaddle().moveRight(deltaTime);

            // Cập nhật bóng và va chạm
            game.update(deltaTime);
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Vẽ background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Vẽ paddle
        Paddle paddle = game.getPaddle();
        g.setColor(Color.GREEN);
        g.fillRect((int) paddle.getX(), (int) paddle.getY(), (int) paddle.getWidth(), (int) paddle.getHeight());

        //debug paddle
        System.out.println("Paddle: x=" + paddle.getX() + " y=" + paddle.getY() +
                " w=" + paddle.getWidth() + " h=" + paddle.getHeight());

        // Vẽ bóng
        Ball ball = game.getBall();
        g.setColor(Color.WHITE);
        g.fillOval((int) ball.getX(), (int) ball.getY(), (int) (ball.getRadius() * 2), (int) (ball.getRadius() * 2));

        // Vẽ gạch
        g.setColor(Color.RED);
        for (Brick brick : game.getBricks()) {
            if (!brick.isDestroyed()) {
                g.fillRect((int) brick.getX(), (int) brick.getY(), brick.getWidth(), brick.getHeight());
            }
        }

        // Hiển thị score
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + game.getScore(), 10, 20);
        if (game.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER", getWidth() / 2 - 100, getHeight() / 2);
        }
    }
}
