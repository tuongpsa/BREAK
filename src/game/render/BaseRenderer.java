package game.render;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class BaseRenderer {
    protected Image image;
    protected String text;
    protected double x, y; // vị trí
    protected Font font = Font.loadFont("file:assets/HALLOWEEN.ttf", 24); // ti le font
    protected Color textColor = Color.WHITE; // mau sac font


    public void setImage(Image img) {
        this.image = img;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setTextColor(Color color) {
        this.textColor = color;
    }

    //Ve len man hinh
    public void render(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x, y);
        }
        if (text != null && !text.isEmpty()) {
            gc.setFill(textColor);
            gc.setFont(font);
            gc.fillText(text, x, y);
        }
    }

    // logic rieng
    public void update() {}
}
