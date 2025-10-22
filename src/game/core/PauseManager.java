package game.core;

// java
public class PauseManager {
    private volatile boolean paused = false;
    private final Object lock = new Object();
    private long pauseStartMillis = 0;
    private long accumulatedPausedMillis = 0;

    // Enter pause state
    public void pause() {
        if (!paused) {
            paused = true;
            pauseStartMillis = System.currentTimeMillis();
        }
    }

    // Exit pause state
    public void resume() {
        if (paused) {
            long now = System.currentTimeMillis();
            accumulatedPausedMillis += now - pauseStartMillis;
            paused = false;
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }

    // Toggle pause/resume
    public void toggle() {
        if (paused) resume(); else pause();
    }

    // Query pause state
    public boolean isPaused() {
        return paused;
    }

    // Blocking helper for game loop: call at start of each frame/update
    // This will pause the calling thread until resume() is called.
    public void waitWhilePaused() {
        synchronized (lock) {
            while (paused) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // Total time spent paused (millis). If currently paused, includes current pause interval.
    public long getTotalPausedMillis() {
        if (paused) {
            return accumulatedPausedMillis + (System.currentTimeMillis() - pauseStartMillis);
        } else {
            return accumulatedPausedMillis;
        }
    }

    // Reset accumulated paused time (does not change current pause state)
    public void resetAccumulatedPausedMillis() {
        accumulatedPausedMillis = 0;
        if (paused) {
            pauseStartMillis = System.currentTimeMillis();
        }
    }
}

