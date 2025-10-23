package game.core;

public class LevelManager {
    private int level = 1;            // cấp hiện tại
    private int baseBrickCount = 5;   // số brick cơ bản ban đầu
    private float ballSpeedMultiplier = 1.0f; // hệ số tốc độ bóng
    private float paddleWidthMultiplier = 1.0f; // hệ số chiều dài paddle

    public void levelUp(Game game) {
        level++;
        // Reset lại vị trí ball và paddle
        game.getBall().setX((game.getWidth() / 2) - game.getBall().getRadius());
        game.getBall().setY(game.getHeight() - 30);
        game.getBall().setVelY( Math.abs(game.getBall().getVelY())); // đảm bảo bóng đi lên

        // Đặt paddle về giữa
        game.getPaddle().setX((game.getWidth() - game.getPaddle().getWidth()) / 2);

        // xóa power-ups hiện có
        game.getPowerUps().clear();

        // Tăng số lượng gạch
        int newBrickCount = baseBrickCount + (level - 1) * 5;
        game.setMaxBrick(newBrickCount);
        game.createBricks();

        // Giảm kích thước paddle
        paddleWidthMultiplier *= 0.9f; // Mỗi level giảm 10%
        game.getPaddle().setWidth(game.getPaddle().getWidth() * paddleWidthMultiplier);

        // Tăng tốc độ bóng
        ballSpeedMultiplier *= 1.15f; // tăng 15%
        game.getBall().setVelY(game.getBall().getVelY() * ballSpeedMultiplier);

        System.out.println("Level up! → Level " + level);
    }

    public int getLevel() {
        return level;
    }
}
