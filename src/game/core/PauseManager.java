package game.core; // Giữ nguyên package của bạn

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; // Dùng class này để an toàn khi duyệt và sửa list cùng lúc

/**
 * Quản lý trạng thái pause/resume của game,
 * chặn luồng game loop và thông báo cho các hệ thống khác.
 */
public class PauseManager {
    private volatile boolean paused = false;
    private final Object lock = new Object();
    private long pauseStartMillis = 0;
    private long accumulatedPausedMillis = 0;

    // Danh sách các "listener" sẽ được thông báo khi có sự kiện
    private final List<PauseListener> listeners = new CopyOnWriteArrayList<>();

    // --- Quản lý Listener ---

    /**
     * Đăng ký một đối tượng để nhận thông báo khi game pause/resume.
     * @param listener Đối tượng (ví dụ: PauseMenu)
     */
    public void addListener(PauseListener listener) {
        listeners.add(listener);
    }

    /**
     * Hủy đăng ký nhận thông báo.
     * @param listener Đối tượng đã đăng ký
     */
    public void removeListener(PauseListener listener) {
        listeners.remove(listener);
    }

    /**
     * Thông báo cho tất cả listener rằng game đã pause.
     */
    private void notifyPaused() {
        for (PauseListener listener : listeners) {
            listener.onGamePaused();
        }
    }

    /**
     * Thông báo cho tất cả listener rằng game đã resume.
     */
    private void notifyResumed() {
        for (PauseListener listener : listeners) {
            listener.onGameResumed();
        }
    }

    // --- Điều khiển Trạng thái ---

    /**
     * Đưa game vào trạng thái pause.
     * Sẽ thông báo cho tất cả listener.
     */
    public void pause() {
        if (!paused) {
            paused = true;
            pauseStartMillis = System.currentTimeMillis();
            // Thông báo sự kiện
            notifyPaused();
        }
    }

    /**
     * Đưa game ra khỏi trạng thái pause.
     * Sẽ thông báo cho tất cả listener và "mở khóa" luồng game loop.
     */
    public void resume() {
        if (paused) {
            long now = System.currentTimeMillis();
            accumulatedPausedMillis += now - pauseStartMillis;
            paused = false;

            // Thông báo sự kiện *trước* khi mở khóa
            notifyResumed();

            // Mở khóa luồng game loop (đang bị kẹt ở waitWhilePaused)
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }

    /**
     * Lật ngược trạng thái pause/resume.
     */
    public void toggle() {
        if (paused) {
            resume();
        } else {
            pause();
        }
    }

    /**
     * Kiểm tra xem game có đang pause hay không.
     * @return true nếu game đang pause, ngược lại là false.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Hàm này được gọi ở đầu game loop.
     * Nó sẽ "đóng băng" luồng gọi nó (ví dụ: luồng AnimationTimer)
     * cho đến khi resume() được gọi.
     */


    // --- Quản lý Thời gian ---

    /**
     * Lấy tổng thời gian (milliseconds) mà game đã bị pause.
     * Nếu game đang pause, nó sẽ bao gồm cả thời gian pause hiện tại.
     * @return Tổng thời gian đã pause (ms).
     */
    public long getTotalPausedMillis() {
        if (paused) {
            // Trả về thời gian đã tích lũy + thời gian đang pause hiện tại
            return accumulatedPausedMillis + (System.currentTimeMillis() - pauseStartMillis);
        } else {
            // Chỉ trả về thời gian đã tích lũy
            return accumulatedPausedMillis;
        }
    }

    /**
     * Reset lại bộ đếm thời gian đã pause về 0.
     * Thường dùng khi bắt đầu một màn chơi mới.
     * (Không thay đổi trạng thái pause hiện tại).
     */
    public void resetAccumulatedPausedMillis() {
        accumulatedPausedMillis = 0;

        // Nếu đang pause, reset lại mốc thời gian bắt đầu
        if (paused) {
            pauseStartMillis = System.currentTimeMillis();
        }
    }
}