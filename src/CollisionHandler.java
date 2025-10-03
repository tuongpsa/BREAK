import java.awt.Rectangle;
import java.util.List;

public class CollisionHandler {
    public  void handleBallBrickCollision(Ball ball, float deltaTime, List<Brick> bricks,Game game ,Paddle paddle) {
        float dx = ball.getVelX() * deltaTime;
        float dy = ball.getVelY() * deltaTime;
        float radius = ball.getRadius();

        float screenWidth = game.getWidth();
        float screenHeight = game.getHeight();


        float stepSize = radius / 4.0f;
        int steps = (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy)) / stepSize);
        if (steps < 1) steps = 1;

        float stepX = dx / steps;
        float stepY = dy / steps;

// Trái
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.setVelX(-ball.getVelX());
        }

// Phải
        if (ball.getX() + radius * 2 >= screenWidth) {
            ball.setX(screenWidth - radius * 2);
            ball.setVelX(-ball.getVelX());
        }

// Trên
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setVelY(-ball.getVelY());
        }

// Paddle
        if (ball.getY() + radius * 2 >= paddle.getY() &&
                ball.getY() + radius <= paddle.getY() + paddle.getHeight() &&  // check chiều cao paddle
                ball.getX() + radius * 2 >= paddle.getX() &&
                ball.getX() <= paddle.getX() + paddle.getWidth()) {

            if (ball.getVelY() > 0) {  // chỉ nảy nếu bóng đang đi xuống
                ball.reverseY();
            }
        }


        // Dưới → GAME OVER
        if (ball.getY() + radius * 2 >= screenHeight + 25 ) {
            game.setGameOver(true);
        }

        for (int i = 0; i < steps; i++) {
            float nextX = ball.getX() + stepX;
            float nextY = ball.getY() + stepY;

            boolean collided = false;

            for (Brick brick : bricks) {
                if (!brick.isDestroyed()) {
                    Rectangle rect = brick.getBounds();
                    boolean hitX = nextX + radius * 2 > rect.x && nextX < rect.x + rect.width;
                    boolean hitY = nextY + radius * 2 > rect.y && nextY < rect.y + rect.height;

                    if (hitX && hitY) {
                        brick.hit();

                        float overlapLeft   = Math.abs((nextX + radius * 2) - rect.x);
                        float overlapRight  = Math.abs((rect.x + rect.width) - nextX);
                        float overlapTop    = Math.abs((nextY + radius * 2) - rect.y);
                        float overlapBottom = Math.abs((rect.y + rect.height) - nextY);

                        float minHorizontal = Math.min(overlapLeft, overlapRight);
                        float minVertical   = Math.min(overlapTop, overlapBottom);

                        if (minVertical < minHorizontal) {
                            ball.setVelY(-ball.getVelY());
                        } else {
                            ball.setVelX(-ball.getVelX());
                        }
                        if(brick.isDestroyed()==true) game.addScore(10);

                        collided = true;
                        break; // thoát vòng lặp bricks
                    }
                }
            }

            // luôn cập nhật vị trí bóng
           // ball.setX(nextX);
            //ball.setY(nextY);

            if (collided) break; // sau khi va chạm thì thoát bước này
        }

    }

}
