public class LevelManager {
    private int level = 1;            // cấp hiện tại
    private int baseBrickCount = 5;   // số brick cơ bản ban đầu
    private float ballSpeedMultiplier = 1.0f; // hệ số tốc độ bóng
    private float paddleWidthMultiplier = 1.0f; // hệ số chiều dài paddle

    public void levelUp(Game game) {
        level++;

        // Tăng số lượng gạch
        int newBrickCount = baseBrickCount + (level - 1) * 5;
        game.setMaxBrick(newBrickCount);
        game.createBricks();

        /* Giảm kích thước paddle
        paddleWidthMultiplier *= 0.9f; // Mỗi level giảm 10%
        game.getPaddle().setWidth(game.getPaddle().getWidth() * paddleWidthMultiplier);

        // Tăng tốc độ bóng
        ballSpeedMultiplier *= 1.15f; // tăng 15%
        game.getBall().setVelY(game.getBall().getVelY() * ballSpeedMultiplier);*/

        System.out.println("Level up! → Level " + level);
    }

    public int getLevel() {
        return level;
    }
}
