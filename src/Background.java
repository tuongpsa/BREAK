import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Background {

    // Khai báo các field
    private Image sky;
    private Image mountain;
    private Image cloud;

    // Tọa độ X mặc định của mountain và cloud
    private double mountainX = 0;
    private double cloudX = 0;
    private double[] cloudYs = new double[2]; // mảng 2 đám mây lưu tạo độ dọc

    // Scroll speed của mountain và cloud
    private double mountainSpeed = 0.3;// mo frame di chuyển 0.3 px
    private double cloudSpeed = 0.6;

    /**
     * Taoj constructor có tham số.
     * @param skyLink link dan den sky.
     * @param mountainLink link dan mountain.
     * @param cloudLink link dan cloud.
     * @param mountainSpeed mountain scroll speed.
     * @param cloudSpeed clound scroll speed.
     */
    public Background(String skyLink, String mountainLink, String cloudLink, double mountainSpeed, double cloudSpeed) {
        // Load anh từ link truyền vào
        this.sky = new Image("file:" + skyLink);
        this.mountain = new Image("file:" + mountainLink);
        this.cloud = new Image("file:" + cloudLink);

        // Check xem co load dc anh hk
        if (sky.isError() || mountain.isError() || cloud.isError()) {
            System.out.println("Background image load error");
        }

        // Gán tham số truyền vao cho this
        this.mountainSpeed = mountainSpeed;
        this.cloudSpeed = cloudSpeed;
    }

    /**
     * update vị trí cuộn.
     * Khi mà vị trí trái của núi và mây cuộn ra ngoài màn hình
     * thì reset lại objectX = 0.
     */
    public void updateBackground() {
        mountainX -= mountainSpeed; // cuộn núi
        cloudX -= cloudSpeed;

        if (mountainX < -mountain.getWidth()) mountainX = 0;
        if (cloudX < -cloud.getWidth()) cloudX = 0;
    }

    /**
     * Vẽ sky hk di chuyển.
     * vẽ 2 cloud liền mạch vs nhau.
     * @param gc đối tượng trong method graphicsContext.
     * @param width Chiều rộng.
     * @param height chiều dài.
     */
    public void drawBackground(GraphicsContext gc, double width, double height) {

        gc.drawImage(sky, 0, 0, width, height);

        //Chạy for để vẽ clouds
        for (int i = 0; i < cloudYs.length; i++) {
            gc.drawImage(cloud, cloudX, cloudYs[i]);
            gc.drawImage(cloud, cloudX + cloud.getWidth(), cloudYs[i]);// tạo sự liền mạch khi cloud1
            // di chuyển hết màn hình.

        }

        //Tương tự cloud
        double mountainY = height - mountain.getHeight();
        gc.drawImage(mountain, mountainX, mountainY);
        gc.drawImage(mountain, mountainX + mountain.getWidth(), mountainY);

    }
}
