package game.score;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class để quản lý điểm cao nhất (top 5)
 */
public class HighScoreManager {
    private static final String HIGH_SCORE_FILE = "highscores.dat";
    private static final int MAX_SCORES = 5;
    private List<HighScoreEntry> highScores;

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    /**
     * Thêm điểm mới và cập nhật danh sách top 5
     */
    public void addScore(String playerName, int score) {
        highScores.add(new HighScoreEntry(playerName, score));

        // Sắp xếp theo điểm giảm dần
        highScores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        // Chỉ giữ lại top 5
        if (highScores.size() > MAX_SCORES) {
            highScores = highScores.subList(0, MAX_SCORES);
        }

        saveHighScores();
    }

    /**
     * Lấy danh sách điểm cao nhất
     */
    public List<HighScoreEntry> getHighScores() {
        return new ArrayList<>(highScores);
    }

    /**
     * Kiểm tra xem điểm có đủ để vào top 5 không
     */
    public boolean isHighScore(int score) {
        if (highScores.size() < MAX_SCORES) {
            return true;
        }
        return score > highScores.get(highScores.size() - 1).getScore();
    }

    /**
     * Lưu điểm vào file
     */
    private void saveHighScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORE_FILE))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            System.err.println("Không thể lưu điểm cao: " + e.getMessage());
        }
    }

    /**
     * Đọc điểm từ file
     */
    @SuppressWarnings("unchecked")
    private void loadHighScores() {
        File file = new File(HIGH_SCORE_FILE);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGH_SCORE_FILE))) {
            highScores = (List<HighScoreEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Không thể đọc điểm cao: " + e.getMessage());
            highScores = new ArrayList<>();
        }
    }

}