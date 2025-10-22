package game.ui;

/**
 * Interface callback để game.ui.GamePanel có thể thông báo cho game.main.Main khi muốn quay về menu
 */
public interface MenuCallback {
    void returnToMenu();
}
