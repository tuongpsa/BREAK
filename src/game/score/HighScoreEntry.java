package game.score;

import java.io.Serializable;

/**
 * Class để lưu trữ thông tin một điểm cao
 */
// Bỏ từ khóa "static" và giữ "public"
public class HighScoreEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private String playerName;
    private int score;
    private long timestamp;

    public HighScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFormattedDate() {
        return new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(timestamp));
    }
}