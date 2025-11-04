package game.core;

import game.objects.Ball;
import game.render.LevelRender;

public class LevelManager {
    private int level = 1;            // cấp hiện tại
    private float ballSpeedMultiplier = 1.07f; // hệ số tốc độ bóng
    private LevelRender levelRender;

    public void levelUp(Game game) {
        level++;
        if (levelRender != null) {
            levelRender.showLevel(level);
        }

        // Xóa mọi hiệu ứng power-up đang active trước khi chuyển level
        game.clearPowerUpsOnLevelUp();

        // Reset lại vị trí ball và paddle
        game.getBall().setX((game.getWidth() / 2) - game.getBall().getRadius());
        game.getBall().setY(game.getHeight() - 30);
        game.getBall().setVelY(-Math.abs(game.getBall().getVelY())); // đảm bảo bóng đi lên

        // Đặt paddle về giữa
        game.getPaddle().setX((game.getWidth() - game.getPaddle().getWidth()) / 2);



        // (đã clear trong clearPowerUpsOnLevelUp)

        game.createBricks(level);
        System.out.println("DEBUG: getBallSpeed() trả về: " + game.getBallSpeed());
        System.out.println("DEBUG: ballSpeedMultiplier là: " + ballSpeedMultiplier);

        // Tăng tốc độ bóng
        game.getBalls().clear();
        float tocdobandau = game.getBallSpeed(); // Lấy tốc độ GỐC (ví dụ: 250.f)
        float tocdomoi = tocdobandau * ballSpeedMultiplier;
        float ballRadius = game.getBallRadius();
        game.setBallSpeed(tocdomoi);
        float initX = (game.getWidth() / 2) - ballRadius;
        float initY = game.getHeight() - 30;
        Ball newBall = new Ball(initX, initY, ballRadius, tocdomoi);
        newBall.setVelX(1);
        newBall.setVelY(-Math.abs(tocdomoi));
        game.getBalls().add(newBall);

        System.out.println("Level up! → Level " + level);
        System.out.println("New ball speed: " + tocdomoi);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public void setLevelRender(LevelRender levelRender) {
        this.levelRender = levelRender;
    }

}
