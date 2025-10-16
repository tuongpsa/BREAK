public class Pause {
    private boolean paused = false;
    private long lastToggleTime = 0;
    private static final long DEBOUNCE_MS = 200; // chống spam phím

    public void togglePause() {
        long now = System.currentTimeMillis();
        if (now - lastToggleTime < DEBOUNCE_MS) return; // bấm quá nhanh thì bỏ qua
        paused = !paused;
        lastToggleTime = now;
        System.out.println(paused ? "Game paused" : "Game resumed");
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean value) {
        paused = value;
    }
}
