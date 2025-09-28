import java.awt.Rectangle;

public class CollisionHandler {
    public static void checkCollisions(Game game){
        Ball ball = game.getBall();
        Paddle paddle = game.getPaddle();
        float width = game.getWidth();
        float height = game.getHeight();

        // Va chạm tường ngang
        if(ball.getX() <=0 || ball.getX() >= width - ball.getRadius()*2){
            ball.reverseX();
        }

        // Va chạm trần
        if(ball.getY() <= 0){
            ball.reverseY();
        }

        // Va chạm paddle
        if(ball.getY() + ball.getRadius()*2 >= paddle.getY() &&
                ball.getX() + ball.getRadius() >= paddle.getX() &&
                ball.getX() <= paddle.getX() + paddle.getWidth()){
            ball.reverseY();
        }

        // Va chạm gạch
        for(Brick brick : game.getBricks()){
            if(!brick.isDestroyed()){
                Rectangle rect = brick.getBounds();
                if(ball.getX()+ball.getRadius()*2 > rect.x &&
                        ball.getX() < rect.x + rect.width &&
                        ball.getY()+ball.getRadius()*2 > rect.y &&
                        ball.getY() < rect.y + rect.height){
                    brick.hit();
                    game.addScore(10);
                    ball.reverseY();
                    break;
                }
            }
        }

        // Bóng ra khỏi đáy
        if(ball.getY() >= height){
            System.out.println("game over");
            game.setGameOver(true);
        }
    }
}
