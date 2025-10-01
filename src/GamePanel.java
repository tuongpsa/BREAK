import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
import javafx.animation.AnimationTimer; // tạo vòng lặp
import javafx.animation.AnimationTimer; // cho phép chạy một hàm handle(long now) mỗi frame
import javafx.scene.canvas.Canvas; // một node để vẽ 2D
import javafx.scene.canvas.GraphicsContext; // như bút vẽ lên canvas
import javafx.scene.input.KeyCode; // cung cấp các phím
import javafx.scene.paint.Color; // cung cấp màu vẽ
import javafx.scene.text.Font; // setFont
import javafx.scene.image.Image;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

// GamePanel kế thừa Canvas -> Node của scene graph
public class GamePanel extends Canvas {
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

    /**
     * Use AnimationTimer để gọi liên tục.
     * Use timeDistance cho chuyển động frame.
     * update lại logic và render.
     */
    public void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0; // thời gian cập nhật trước

    private void updateGame() {
        float deltaTime = 0.016f;
            @Override
            public void handle(long now) {
                if (lastTime > 0) {
                    // khoảng thời gian trôi qua giữa 2 frame
                    float timeDistance = (now - lastTime) / 1_000_000_000f;
                    updateGame(timeDistance); // update logic
                    render();
                }
                lastTime = now;
            }
        };
        timer.start(); // bắt đầu vòng lặp
    }

    /**
     * update logic game trong mỗi frame dc tạo.
     * xử lý di chuyển paddle dựa vào phím.
     * @param timeDistance khoảng tg giữa 2 frame.
     */
    private void updateGame(float timeDistance) {
        background.updateBackground();
        if (!game.isGameOver()) {
            // Di chuyển paddle
            if (leftPressed) game.getPaddle().moveLeft(deltaTime);
            if (rightPressed) game.getPaddle().moveRight(deltaTime);

            // Cập nhật bóng và va chạm
            game.update(deltaTime);
            if (leftPressed) game.getPaddle().moveLeft(timeDistance); // gọi method move
            if (rightPressed) game.getPaddle().moveRight(timeDistance);
            game.update(timeDistance);
        }
    }

    /**
     * Vẽ paddel, ball, brick, hiện score điểm.
     * Vẽ text khi gameover : score, gameover.
     */
    private void render() {
        GraphicsContext gc = getGraphicsContext2D();
        background.drawBackground(gc, getWidth(), getHeight());

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
        // tọa độ tâm paddle
        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        double paddleCenterY = paddle.getY() + paddle.getHeight() / 2;
        // vẽ paddle
        gc.drawImage(
                paddleImage,
                paddleCenterX - paddle.getWidth() / 2,   // tọa độ x
                paddleCenterY - paddle.getHeight() / 2,  // tọa độ y
                paddle.getWidth(),                 // width
                paddle.getHeight()*5.5             // height
        );
        // Debug paddle
        System.out.println("Paddle: x=" + paddle.getX());

        // Vẽ bóng
        Ball ball = game.getBall();
        g.setColor(Color.WHITE);
        g.fillOval((int) ball.getX(), (int) ball.getY(), (int) (ball.getRadius() * 2), (int) (ball.getRadius() * 2));
        gc.drawImage(ballImage, ball.getX(), ball.getY(), ball.getRadius() * 2, ball.getRadius() * 2);

        // Vẽ gạch
        g.setColor(Color.RED);
        for (Brick brick : game.getBricks()) {
            if (!brick.isDestroyed()) {
                g.fillRect((int) brick.getX(), (int) brick.getY(), brick.getWidth(), brick.getHeight());
                gc.drawImage(brickImage, brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
            }
        }

        // Hiển thị score
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + game.getScore(), 10, 20);
        // Vẽ điểm
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(18));
        gc.fillText("Score: " + game.getScore(), 10, 20);

        // Khi gameover
        if (game.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER", getWidth() / 2 - 100, getHeight() / 2);

            gameOverRenderer = new GameOverRenderer();
            gameOverRenderer  .render(gc, getWidth(), getHeight(), game.getScore());
        }
    }
}

