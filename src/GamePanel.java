import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private Game game;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public GamePanel() {
        game = new Game();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = true;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) leftPressed = false;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) rightPressed = false;
            }
        });
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
        g.fillRect((int)paddle.getX(), (int)paddle.getY(), (int)paddle.getWidth(), (int)paddle.getHeight());

        // Vẽ bóng
        Ball ball = game.getBall();
        g.setColor(Color.WHITE);
        g.fillOval((int)ball.getX(), (int)ball.getY(), (int)(ball.getRadius()*2), (int)(ball.getRadius()*2));

        // Vẽ gạch
        g.setColor(Color.RED);
        for (Brick brick : game.getBricks()) {
            if (!brick.isDestroyed()) {
                g.fillRect((int)brick.getX(), (int)brick.getY(), brick.getWidth(), brick.getHeight());
            }
        }

        // Hiển thị score
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + game.getScore(), 10, 20);
        if (game.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER", getWidth()/2 - 100, getHeight()/2);
        }
    }
}
