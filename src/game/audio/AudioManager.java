package game.audio;

import game.core.GameSettings;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Class quản lý âm thanh cho game - Hybrid approach
 * Sử dụng JavaFX Media cho MP3 và Java Sound API cho WAV
 */
public class AudioManager {
    private static AudioManager instance;

    // JavaFX Media cho MP3
    private MediaPlayer menuMusic;
    private MediaPlayer gameMusic;

    /**
     *
     * @param settings
     */
    private AudioManager(GameSettings settings) {
        if (initialized) {
            return; // Tránh khởi tạo nhiều lần
        }

        this.gameSettings = settings;

        try {
            // Load MP3 music với JavaFX Media
            loadMP3Music();

            // Load WAV sound effects với Java Sound API
            loadWAVSounds();

            initialized = true;
            System.out.println("game.audio.AudioManager initialized with hybrid approach (MP3 + WAV)");

        } catch (Exception e) {
            System.out.println("Không thể load âm thanh: " + e.getMessage());
            createDefaultSounds();
        }
    }
    /**
     * 3. Phương thức public static để "khởi tạo" (initialize) Singleton.
     * Phải được gọi một lần duy nhất khi game bắt đầu.
     * @param settings Đối tượng GameSettings cần thiết.
     */
    public static void initialize(GameSettings settings) {
        if (instance == null) {
            instance = new AudioManager(settings);
        } else {
            System.out.println("AudioManager đã được khởi tạo rồi.");
        }
    }

    /**
     * 4. Phương thức public static để get thể hiện duy nhất.
     * Mọi lớp khác trong game sẽ dùng hàm này.
     * @return Thể hiện (instance) duy nhất của AudioManager.
     */
    public static AudioManager getInstance() {
        if (instance == null) {
            // Lỗi này xảy ra nếu ai đó gọi getInstance() trước khi gọi initialize()
            throw new IllegalStateException("AudioManager chưa được khởi tạo! " +
                    "Hãy gọi AudioManager.initialize(settings) trước.");
        }
        return instance;
    }
    // Java Sound API cho WAV
    private Clip brickHitClip;
    private Clip paddleHitClip;
    private Clip gameOverClip;

    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private boolean initialized = false;

    private GameSettings gameSettings;
    
    private void loadMP3Music() {
        try {
            // Load menu music MP3
            File menuMusicFile = new File("assets/menu_music.mp3");
            String menuMusicPath = menuMusicFile.toURI().toString();
            Media menuMedia = new Media(menuMusicPath);
            menuMusic = new MediaPlayer(menuMedia);
            menuMusic.setCycleCount(MediaPlayer.INDEFINITE);
            menuMusic.setVolume(gameSettings.getMusicVolume());
            
            // Load game music MP3
            File gameMusicFile = new File("assets/game_music.mp3");
            String gameMusicPath = gameMusicFile.toURI().toString();
            Media gameMedia = new Media(gameMusicPath);
            gameMusic = new MediaPlayer(gameMedia);
            gameMusic.setCycleCount(MediaPlayer.INDEFINITE);
            menuMusic.setVolume(gameSettings.getMusicVolume());
            
            System.out.println("MP3 music loaded successfully");
            
        } catch (Exception e) {
            System.out.println("Không thể load MP3 music: " + e.getMessage());
            menuMusic = null;
            gameMusic = null;
        }
    }
    
    private void loadWAVSounds() {
        try {
            // Load WAV sound effects
            loadAudioClip("assets/brick_hit.wav", "brick");
            loadAudioClip("assets/paddle_hit.wav", "paddle");
            loadAudioClip("assets/game_over.wav", "gameover");
            
            System.out.println("WAV sound effects loaded successfully");
            
        } catch (Exception e) {
            System.out.println("Không thể load WAV sounds: " + e.getMessage());
        }
    }
    
    private void loadAudioClip(String filePath, String type) {
        try {
            File audioFile = new File(filePath);
            if (!audioFile.exists()) {
                System.out.println("File không tồn tại: " + filePath);
                return;
            }
            
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            switch (type) {
                case "brick":
                    brickHitClip = clip;
                    break;
                case "paddle":
                    paddleHitClip = clip;
                    break;
                case "gameover":
                    gameOverClip = clip;
                    break;
            }
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Lỗi load file âm thanh " + filePath + ": " + e.getMessage());
        }
    }
    
    private void createDefaultSounds() {
        System.out.println("Sử dụng âm thanh mặc định");
        initialized = true;
    }
    
    public void playMenuMusic() {
        if (musicEnabled && menuMusic != null) {
            stopAllMusic();
            menuMusic.play();
        } else if (musicEnabled) {
            System.out.println("Menu music không khả dụng");
        }
    }
    
    public void playGameMusic() {
        if (musicEnabled && gameMusic != null) {
            stopAllMusic();
            gameMusic.play();
        } else if (musicEnabled) {
            System.out.println("game.core.Game music không khả dụng");
        }
    }
    
    public void stopAllMusic() {
        if (menuMusic != null) {
            menuMusic.stop();
        }
        if (gameMusic != null) {
            gameMusic.stop();
        }
    }
    
    public void stopMenuMusic() {
        if (menuMusic != null) {
            menuMusic.stop();
        }
    }
    
    public void stopGameMusic() {
        if (gameMusic != null) {
            gameMusic.stop();
        }
    }
    
    public void playBrickHit() {
        if (soundEnabled && brickHitClip != null) {
            brickHitClip.setFramePosition(0);
            brickHitClip.start();
        }
    }
    
    public void playPaddleHit() {
        if (soundEnabled && paddleHitClip != null) {
            paddleHitClip.setFramePosition(0);
            paddleHitClip.start();
        }
    }
    
    public void playGameOver() {
        if (soundEnabled && gameOverClip != null) {
            gameOverClip.setFramePosition(0);
            gameOverClip.start();
        }
    }
    
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }
    
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopAllMusic();
        }
    }
    
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    public boolean isMusicEnabled() {
        return musicEnabled;
    }
    
    public void dispose() {
        if (menuMusic != null) menuMusic.dispose();
        if (gameMusic != null) gameMusic.dispose();
        if (brickHitClip != null) brickHitClip.close();
        if (paddleHitClip != null) paddleHitClip.close();
        if (gameOverClip != null) gameOverClip.close();
    }
    /**
     * Cập nhật âm lượng nhạc (MP3) ngay lập tức.
     * Được gọi bởi thanh trượt (slider) trong PauseMenu.
     * @param volume Giá trị từ 0.0 đến 1.0
     */
    public void setMusicVolume(double volume) {
        // 1. Lưu cài đặt mới
        gameSettings.setMusicVolume(volume);

        // 2. Áp dụng ngay cho nhạc đang chạy (nếu có)
        if (menuMusic != null) {
            menuMusic.setVolume(volume);
        }
        if (gameMusic != null) {
            gameMusic.setVolume(volume);
        }
    }

    /**
     * Cập nhật âm lượng hiệu ứng (WAV).
     * @param volume Giá trị từ 0.0 đến 1.0
     */
    public void setSoundEffectsVolume(double volume) {
        // 1. Lưu cài đặt mới
        gameSettings.setSfxVolume(volume);

        // 2. Áp dụng cho các Clip (phức tạp hơn, cần dùng FloatControl)
        setClipVolume(brickHitClip, volume);
        setClipVolume(paddleHitClip, volume);
        setClipVolume(gameOverClip, volume);

        System.out.println("SFX Volume đã được lưu: " + volume);
    }
    /**
     * Helper để đặt âm lượng cho một đối tượng Clip (WAV).
     * @param clip Đối tượng Clip (hiệu ứng âm thanh).
     * @param volume Âm lượng (từ 0.0 đến 1.0).
     */
    private void setClipVolume(Clip clip, double volume) {
        if (clip == null) {
            return;
        }

        // Đảm bảo âm lượng nằm trong khoảng [0.0, 1.0]
        volume = Math.max(0.0, Math.min(1.0, volume));

        try {
            // Lấy bộ điều khiển Âm lượng (MASTER_GAIN)
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Tính toán giá trị dB
            // Chúng ta sẽ nội suy tuyến tính giá trị từ min đến max
            float min = gainControl.getMinimum(); // Thường là -80.0
            float max = gainControl.getMaximum(); // Thường là 6.02

            // Nếu volume = 0, đặt giá trị nhỏ nhất (tắt tiếng)
            if (volume == 0.0) {
                gainControl.setValue(min);
            } else {
                // Công thức nội suy (Mapping 0.0-1.0 to min-max)
                float dB = (float) (Math.log10(volume) * 20.0);

                // Đảm bảo giá trị dB không vượt quá ngưỡng
                gainControl.setValue(Math.max(min, Math.min(dB, max)));
            }

        } catch (Exception e) {
            // Lỗi này xảy ra nếu hệ thống âm thanh không hỗ trợ MASTER_GAIN
            System.out.println("Không thể đặt âm lượng cho Clip: " + e.getMessage());
        }
    }
}