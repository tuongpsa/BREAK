package game.core; // Bạn có thể đặt ở package khác nếu muốn

// Class để lưu trữ cài đặt
public class GameSettings {
    // Âm lượng nhạc, giá trị từ 0.0 đến 1.0
    private double musicVolume = 0.5; // Mặc định 50%

    // Âm lượng hiệu ứng, giá trị từ 0.0 đến 1.0
    private double sfxVolume = 1.0;   // Mặc định 100%

    private ControlScheme controlScheme = ControlScheme.ARROW_KEYS; // Mặc định

    // Getters
    public double getMusicVolume() { return musicVolume; }
    public double getSfxVolume() { return sfxVolume; }
    public ControlScheme getControlScheme() { return controlScheme; }

    // Setters (UI sẽ gọi các hàm này)
    public void setMusicVolume(double volume) {
        // Kẹp giá trị trong khoảng [0, 1]
        this.musicVolume = Math.max(0, Math.min(1, volume));
    }

    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0, Math.min(1, volume));
    }

    public void setControlScheme(ControlScheme controlScheme) {
        this.controlScheme = controlScheme;
    }
}