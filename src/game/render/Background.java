package game.render;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Background {

    private Image background;

    public Background(String backgroundLink) {
        this.background = new Image("file:" + backgroundLink);
        if (background.isError()) {
            System.out.println("Background image load error");
        }
    }

    public void drawBackground(GraphicsContext gc, double width, double height) {
        gc.drawImage(background, 0, 0, width, height);
    }
}
