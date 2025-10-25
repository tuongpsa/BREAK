package game.core;

import game.objects.Ball;

public class LevelManager extends Manager {
    private int level = 1;            // cấp hiện tại
    private int baseBrickCount = 5;   // số brick cơ bản ban đầu
    private float ballSpeedMultiplier = 1.0f; // hệ số tốc độ bóng
    private float paddleWidthMultiplier = 1.0f; // hệ số chiều dài paddle

    @Override
    public void update(float deltaTime) {
        // LevelManager không cần update liên tục
    }

    @Override
    public void reset() {
        level = 1;
        ballSpeedMultiplier = 1.0f;
        paddleWidthMultiplier = 1.0f;
    }

    public void levelUp(Game game) {
        level++;
        // Reset lại vị trí ball và paddle
        game.getBall().setX((game.getWidth() / 2) - game.getBall().getRadius());
        game.getBall().setY(game.getHeight() - 30);
        game.getBall().setVelY(-Math.abs(game.getBall().getVelY())); // đảm bảo bóng đi lên

        // Đặt paddle về giữa
        game.getPaddle().setX((game.getWidth() - game.getPaddle().getWidth()) / 2);

        // xóa power-ups hiện có
        game.getPowerUps().clear();

        // Tăng số lượng gạch
        int newBrickCount = baseBrickCount + (level - 1) * 5;
        game.createBricks(level);

        // Tăng tốc độ bóng
        game.getBalls().clear();
        float tocdobandau = game.getBallSpeed(); // Lấy tốc độ GỐC (ví dụ: 250.f)
        float tocdomoi = tocdobandau * ballSpeedMultiplier;
        float ballRadius = game.getBallRadius();
        float initX = (game.getWidth() / 2) - ballRadius;
        float initY = game.getHeight() - 30;
        Ball newBall = new Ball(initX, initY, ballRadius,tocdobandau);
        newBall.setVelX(1);
        newBall.setVelY(-Math.abs(tocdomoi));
        game.getBalls().add(newBall);

        System.out.println("Level up! → Level " + level);
    }

    public int getLevel() {
        return level;
    }
}
