package game.core;

/**
 * Base class cho tất cả các manager trong game
 * Chứa các phương thức chung cho việc quản lý
 */
public abstract class Manager {
    protected boolean active;
    
    public Manager() {
        this.active = true;
    }
    
    // Common manager methods
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    // Abstract methods - các manager con phải implement
    public abstract void update(float deltaTime);
    public abstract void reset();
}
