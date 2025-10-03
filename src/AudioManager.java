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
    // JavaFX Media cho MP3
    private MediaPlayer menuMusic;
    private MediaPlayer gameMusic;
    
    // Java Sound API cho WAV
    private Clip brickHitClip;
    private Clip paddleHitClip;
    private Clip gameOverClip;
    
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private boolean initialized = false;
    
    public AudioManager() {
        if (initialized) {
            return; // Tránh khởi tạo nhiều lần
        }
        
        try {
            // Load MP3 music với JavaFX Media
            loadMP3Music();
            
            // Load WAV sound effects với Java Sound API
            loadWAVSounds();
            
            initialized = true;
            System.out.println("AudioManager initialized with hybrid approach (MP3 + WAV)");
            
        } catch (Exception e) {
            System.out.println("Không thể load âm thanh: " + e.getMessage());
            createDefaultSounds();
        }
    }
    
    private void loadMP3Music() {
        try {
            // Load menu music MP3
            String menuMusicPath = "file:///D:/BREAK/assets/menu_music.mp3";
            Media menuMedia = new Media(menuMusicPath);
            menuMusic = new MediaPlayer(menuMedia);
            menuMusic.setCycleCount(MediaPlayer.INDEFINITE);
            menuMusic.setVolume(0.3);
            
            // Load game music MP3
            String gameMusicPath = "file:///D:/BREAK/assets/game_music.mp3";
            Media gameMedia = new Media(gameMusicPath);
            gameMusic = new MediaPlayer(gameMedia);
            gameMusic.setCycleCount(MediaPlayer.INDEFINITE);
            gameMusic.setVolume(0.3);
            
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
            System.out.println("Game music không khả dụng");
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
}