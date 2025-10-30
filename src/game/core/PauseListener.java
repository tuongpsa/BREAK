package game.core;

/**
 * Interface cho các đối tượng muốn "lắng nghe" sự kiện pause/resume.
 * (Tách ra từ PauseManager.java để sửa lỗi truy cập)
 */
public interface PauseListener {
    void onGamePaused();
    void onGameResumed();
}