package game.render;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LevelRender extends BaseRenderer {

    private long startTime = 0; // thời điểm bắt đầu hiển thị
    private boolean showing = false;
    private final long displayDuration = 3000; // 3 giây

    public LevelRender() {
        this.font = Font.loadFont("file:assets/HALLOWEEN.ttf", 48);
        this.textColor = Color.YELLOW;
    }

    /**
     * Bắt đầu hiển thị level mới
     */
    public void showLevel(int level) {
        this.text = "Level " + level;
        this.showing = true;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * thoi gian hiển thị
     * dc 3 giây thì kết thúc hiển thị
     */
    @Override
    public void update() {
        if (showing) {
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed >= displayDuration) {
                showing = false;
            }
        }
    }

    /**
     * Chỉ vẽ khi đang hiển thị
     */
    @Override
    public void render(GraphicsContext gc) {
        if (showing && text != null) {
            gc.setFill(textColor);
            gc.setFont(font);

            // Vẽ text giữa màn hình
            double x = (gc.getCanvas().getWidth() - gc.getFont().getSize() * text.length() / 2) / 2;
            double y = gc.getCanvas().getHeight() / 2;
            gc.fillText(text, x, y);
        }
    }

    /**
     * check xem level đang được hiển thị hay không
     */
    public boolean isShowing() {
        return showing;
    }
}
