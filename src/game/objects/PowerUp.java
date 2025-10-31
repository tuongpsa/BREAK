package game.objects;

/**
 * PowerUp class - gộp tất cả logic của các loại power-up vào 1 class
 */
public class PowerUp extends GameObject {
    private float velY;
    private PowerUpType type;
    private boolean active;
    private float fallSpeed = 100f;
    private float duration = 10f; // thời gian hiệu lực (giây)
    private float timeRemaining;
    
    // Các thuộc tính riêng cho từng loại power-up
    private float multiplier = 2.0f; // Cho SCORE_MULTIPLIER
    private int ballCount = 2; // Cho MULTI_BALL
    private float speedMultiplier = 1.5f; // Cho SPEED_BOOST
    private float sizeMultiplier = 1.5f; // Cho BIG_PADDLE
    private float slowMultiplier = 0.7f; // Cho SLOW_BALL
    private int shieldLives = 1; // Cho SHIELD
    private int laserShots = 5; // Cho LASER

    public PowerUp(float x, float y, PowerUpType type) {
        super(x, y, 30, 30); // width = 30, height = 30
        this.type = type;
        this.velY = fallSpeed;
        this.active = true;
        
        // Thiết lập duration và các thuộc tính khác nhau cho từng loại
        switch (type) {
            case SCORE_MULTIPLIER:
                this.duration = 15f;
                this.multiplier = 2.0f;
                break;
            case MULTI_BALL:
                this.duration = 20f;
                this.ballCount = 2;
                break;
            case SPEED_BOOST:
                this.duration = 12f;
                this.speedMultiplier = 1.5f;
                break;
            case BIG_PADDLE:
                this.duration = 15f;
                this.sizeMultiplier = 1.5f;
                break;
            case SLOW_BALL:
                this.duration = 10f;
                this.slowMultiplier = 0.7f;
                break;
            case SHIELD:
                this.duration = 0f; // Instant effect, không có duration
                this.shieldLives = 1;
                break;
            case LASER:
                this.duration = 0f; // Instant effect, không có duration
                this.laserShots = 5;
                break;
            default:
                this.duration = 10f;
                break;
        }
        this.timeRemaining = duration;
    }

    @Override
    public void update(float deltaTime) {
        if (active) {
            y += velY * deltaTime;
        }
    }

    public void activate() {
        active = false;
    }

    public void updateDuration(float deltaTime) {
        if (timeRemaining > 0) {
            timeRemaining -= deltaTime;
        }
    }

    public boolean isExpired() {
        return timeRemaining <= 0;
    }

    public boolean isActive() {
        return active;
    }

    public PowerUpType getType() {
        return type;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public void applyEffect() {
        // Logic áp dụng hiệu ứng dựa trên type
        switch (type) {
            case SCORE_MULTIPLIER:
                System.out.println("Score Multiplier activated! Points will be doubled for " + duration + " seconds");
                break;
            case MULTI_BALL:
                System.out.println("Multi Ball activated! " + ballCount + " additional balls will be created");
                break;
            case SPEED_BOOST:
                System.out.println("Speed Boost activated! Paddle speed increased by " + ((speedMultiplier * 100 - 100)) + "%");
                break;
            case BIG_PADDLE:
                System.out.println("Big Paddle activated! Paddle size increased by " + ((sizeMultiplier * 100 - 100)) + "%");
                break;
            case SLOW_BALL:
                System.out.println("Slow Ball activated! Ball speed reduced by " + ((1 - slowMultiplier) * 100) + "%");
                break;
            case SHIELD:
                System.out.println("Shield activated! You gained " + shieldLives + " extra life!");
                break;
            case LASER:
                System.out.println("Laser activated! You can shoot " + laserShots + " laser shots!");
                break;
            default:
                System.out.println("Unknown power-up type activated!");
                break;
        }
    }

    public void removeEffect() {
        // Logic loại bỏ hiệu ứng dựa trên type
        switch (type) {
            case SCORE_MULTIPLIER:
                System.out.println("Score Multiplier effect ended");
                break;
            case MULTI_BALL:
                System.out.println("Multi Ball effect ended");
                break;
            case SPEED_BOOST:
                System.out.println("Speed Boost effect ended");
                break;
            case BIG_PADDLE:
                System.out.println("Big Paddle effect ended");
                break;
            case SLOW_BALL:
                System.out.println("Slow Ball effect ended");
                break;
            case SHIELD:
                System.out.println("Shield effect ended");
                break;
            case LASER:
                System.out.println("Laser effect ended");
                break;
            default:
                System.out.println("Unknown power-up effect ended");
                break;
        }
    }

    // Getters cho các thuộc tính riêng
    public float getMultiplier() {
        return multiplier;
    }

    public int getBallCount() {
        return ballCount;
    }
    
    public float getSpeedMultiplier() {
        return speedMultiplier;
    }
    
    public float getSizeMultiplier() {
        return sizeMultiplier;
    }
    
    public float getSlowMultiplier() {
        return slowMultiplier;
    }
    
    public int getShieldLives() {
        return shieldLives;
    }
    
    public int getLaserShots() {
        return laserShots;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    public void setBallCount(int ballCount) {
        this.ballCount = ballCount;
    }
}
