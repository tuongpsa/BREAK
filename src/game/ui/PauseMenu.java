package game.ui;

import game.audio.AudioManager;
import game.core.ControlScheme;
import game.core.GameSettings;
import game.core.PauseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Lớp PauseMenu được thiết kế lại hoàn toàn
 * để sử dụng tài sản (assets) hình ảnh tùy chỉnh theo mockup.
 * Đã cập nhật để chứa 4 nút và co giãn khung nền.
 */
public class PauseMenu {

    // --- Đường dẫn tới thư mục assets ---
    private final String ASSET_PATH = "assets/gui/gui/WindowPopUp/";

    // --- Các thành phần UI ---
    private final StackPane rootPane;       // Lớp nền đen mờ
    private final StackPane mainPausePane;  // Pane chứa menu "Tạm dừng"
    private final StackPane settingsPane;   // Pane chứa menu "Cài đặt"

    // --- Các ảnh đã load ---
    private final Image frameImage;       // W-4.png (Khung chính)
    private final Image buttonImage;      // choose.png (Nút bấm)
    private final Image buttonClickImage; // choose(click).png (Nút bấm khi nhấn)
    private final Image crownImage;       // G-2.jpg (Vương miện)
    private final Image closeImage;       // 2-2.png (Nút X)

    // --- Tham chiếu hệ thống ---
    private final PauseManager pauseManager;

    /**
     * Tạo giao diện menu pause ĐẸP
     */
    public PauseMenu(PauseManager pauseManager, GameSettings gameSettings, AudioManager audioManager, Runnable onRestart, Runnable onExit) {
        this.pauseManager = pauseManager;

        // === 1. LOAD HÌNH ẢNH ===
        frameImage = loadImage("W-4.png");
        buttonImage = loadImage("choose.png");
        buttonClickImage = loadImage("choose(click).png");
        crownImage = loadImage("G-2.png");
        closeImage = loadImage("2-2.png");

        // === 2. LỚP NỀN (ROOT) ===
        rootPane = new StackPane();
        rootPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);"); // Nền mờ
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setVisible(false); // Ẩn lúc đầu

        // === 3. XÂY DỰNG PANE 1: MENU PAUSE CHÍNH ===
        mainPausePane = buildMainPausePane(onRestart, onExit);

        // === 4. XÂY DỰNG PANE 2: MENU CÀI ĐẶT ===
        settingsPane = buildSettingsPane(gameSettings, audioManager);

        // Thêm cả 2 pane vào root (pane chính ở trên)
        rootPane.getChildren().addAll(settingsPane, mainPausePane);
    }

    /**
     * Xây dựng giao diện cho Menu Pause chính (Đủ 4 nút)
     */
    private StackPane buildMainPausePane(Runnable onRestart, Runnable onExit) {
        // 1. Khung nền (W-4.png)
        ImageView frameView = new ImageView(frameImage);
        // Đặt chiều cao cố định để đủ chứa 4 nút
        frameView.setFitHeight(650);
        frameView.setPreserveRatio(true);

        // 2. Tiêu đề (Vương miện + Chữ "PAUSED")
        ImageView crownView = new ImageView(crownImage);
        crownView.setFitWidth(400);
        crownView.setPreserveRatio(true);

        Label titleLabel = createLabel("PAUSED", 36, "#FFFFFF");
        StackPane titlePane = new StackPane(crownView, titleLabel);
        titlePane.setAlignment(Pos.CENTER);
        StackPane.setMargin(titleLabel, new Insets(50, 0, 0, 0));

        // 3. Tạo 4 nút bấm
        StackPane resumeButton = createStyledButton("CONTINUE");
        StackPane restartButton = createStyledButton("RESTART");
        StackPane settingsButton = createStyledButton("SETTING");
        StackPane exitButton = createStyledButton("MAIN MENU");

        // Gán hành động cho 4 nút
        resumeButton.setOnMouseClicked(e -> pauseManager.resume());
        restartButton.setOnMouseClicked(e -> {
            pauseManager.resume();
            onRestart.run();
        });
        settingsButton.setOnMouseClicked(e -> showSettingsPane(true)); // <<< ĐÃ SỬA LỖI
        exitButton.setOnMouseClicked(e -> {
            pauseManager.resume();
            onExit.run();
        });

        // 4. Nút 'X' (Đóng)
        ImageView closeButton = createClickableIcon(closeImage, 120);
        closeButton.setOnMouseClicked(e -> pauseManager.resume());

        // 5. Container (VBox) trong suốt cho nội dung
        VBox contentBox = new VBox(-50, titlePane, resumeButton, restartButton, settingsButton, exitButton);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(-180, 0, 10, 0)); // Căn đều

        // 6. Xếp chồng mọi thứ
        StackPane pane = new StackPane(frameView, contentBox, closeButton);
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        // Chỉnh lại vị trí nút X cho khớp với khung 550px
        StackPane.setMargin(closeButton, new Insets(70, 70, 0, 0));

        return pane;
    }

    /**
     * Xây dựng giao diện cho Menu Cài đặt
     */
    private StackPane buildSettingsPane(GameSettings gameSettings, AudioManager audioManager) {
        // === 1. Khung nền ===
        ImageView frameView = new ImageView(frameImage);
        frameView.setFitHeight(650);
        frameView.setPreserveRatio(true);

        // === 2. Tiêu đề (vương miện + chữ) ===
        ImageView crownView = new ImageView(crownImage);
        crownView.setFitWidth(400);
        crownView.setPreserveRatio(true);

        Label titleLabel = createLabel("SETTING", 36, "#FFFFFF");
        StackPane titlePane = new StackPane(crownView, titleLabel);
        titlePane.setAlignment(Pos.CENTER);
        StackPane.setMargin(titleLabel, new Insets(50, 0, 0, 0)); // đẩy chữ xuống một chút

        // === 3. Kích thước tiêu chuẩn cho control ===
        final double controlWidth = 280.0; // vừa khít khung hơn
        final double boxWidth = controlWidth + 100; // tổng vùng content

        // === 4. Nhãn và thanh điều khiển ===
        Label musicLabel = createLabel("MUSIC VOLUME:", 16, "#FFFFFF");
        Slider musicSlider = createStyledSlider(gameSettings.getMusicVolume());
        musicSlider.valueProperty().addListener((obs, o, n) -> audioManager.setMusicVolume(n.doubleValue()));
        musicSlider.setPrefWidth(controlWidth);
        musicSlider.setMaxWidth(controlWidth);

        Label sfxLabel = createLabel("SFX VOLUME:", 16, "#FFFFFF");
        Slider sfxSlider = createStyledSlider(gameSettings.getSfxVolume());
        sfxSlider.valueProperty().addListener((obs, o, n) -> audioManager.setSoundEffectsVolume(n.doubleValue()));
        sfxSlider.setPrefWidth(controlWidth);
        sfxSlider.setMaxWidth(controlWidth);

        Label controlLabel = createLabel("CONTROL:", 16, "#FFFFFF");
        ChoiceBox<ControlScheme> controlChoice = createStyledChoiceBox(gameSettings.getControlScheme());
        controlChoice.valueProperty().addListener((obs, o, n) -> gameSettings.setControlScheme(n));
        controlChoice.setPrefWidth(controlWidth);
        controlChoice.setMaxWidth(controlWidth);

        // === 5. Nút quay lại ===
        StackPane backButton = createStyledButton("BACK");
        backButton.setOnMouseClicked(e -> showSettingsPane(false));

        // === 6. Bố cục chính ===
        VBox contentBox = new VBox(
                10, // khoảng cách giữa các dòng
                titlePane,
                musicLabel, musicSlider,
                sfxLabel, sfxSlider,
                controlLabel, controlChoice,
                backButton
        );

        contentBox.setAlignment(Pos.CENTER); // căn giữa toàn bộ
        contentBox.setPadding(new Insets(-255, 0, 0, 0));
        contentBox.setMaxWidth(boxWidth);

        // === 7. Xếp chồng khung nền và nội dung ===
        StackPane pane = new StackPane(frameView, contentBox);
        pane.setAlignment(Pos.CENTER);
        pane.setVisible(false); // ẩn lúc đầu

        return pane;
    }

    // === CÁC HÀM HELPER (TRỢ GIÚP) ===

    /**
     * Helper để load ảnh từ thư mục assets
     */
    private Image loadImage(String fileName) {
        String path = null;
        try {
            path = ASSET_PATH + fileName;
            return new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            System.err.println("LỖI NGHIÊM TRỌNG: Không thể tìm thấy file ảnh: " + path);
            throw new RuntimeException("Không thể load ảnh: " + fileName, e);
        }
    }

    /**
     * Helper tạo một "Nút bấm" bằng hình ảnh (choose.png)
     */
    private StackPane createStyledButton(String text) {
        StackPane buttonPane = new StackPane();
        buttonPane.setAlignment(Pos.CENTER);

        ImageView buttonView = new ImageView(buttonImage);
        buttonView.setFitWidth(200);
        buttonView.setPreserveRatio(true);

        ImageView buttonClickView = new ImageView(buttonClickImage);
        buttonClickView.setFitWidth(200);
        buttonClickView.setPreserveRatio(true);
        buttonClickView.setVisible(false);

        Label buttonText = createLabel(text, 24, "#FFFFFF");
        buttonText.setTextAlignment(TextAlignment.CENTER);

        buttonPane.getChildren().addAll(buttonView, buttonClickView, buttonText);
        buttonPane.setPrefHeight(60);

        // Hiệu ứng
        buttonPane.setOnMousePressed(e -> buttonClickView.setVisible(true));
        buttonPane.setOnMouseReleased(e -> buttonClickView.setVisible(false));
        buttonPane.setOnMouseEntered(e -> buttonPane.setScaleX(1.05));
        buttonPane.setOnMouseExited(e -> buttonPane.setScaleX(1.0));

        return buttonPane;
    }

    /**
     * Helper tạo một Icon (như nút X) có thể click
     */
    private ImageView createClickableIcon(Image iconImage, double size) {
        ImageView iconView = new ImageView(iconImage);
        iconView.setFitWidth(size);
        iconView.setFitHeight(size);

        iconView.setOnMousePressed(e -> iconView.setScaleX(0.9));
        iconView.setOnMouseReleased(e -> iconView.setScaleX(1.0));
        iconView.setOnMouseEntered(e -> iconView.setOpacity(0.8));
        iconView.setOnMouseExited(e -> iconView.setOpacity(1.0));

        return iconView;
    }

    /** Helper để tạo nhãn (Label) chuẩn */
    private Label createLabel(String text, int fontSize, String color) {
        Label label = new Label(text);
        label.setFont(Font.loadFont("file:assets/Spooky.ttf", fontSize));
        label.setTextFill(Color.web(color));
        return label;
    }

    /** Helper tạo Slider (Thanh trượt) với style màu tím/xanh */
    private Slider createStyledSlider(double defaultValue) {
        Slider slider = new Slider(0, 1, defaultValue);
        slider.setStyle(
                "-fx-control-inner-background: #8A2BE2; " + // Tím
                        "-fx-accent: #00FFEF;" // Xanh Cyan
        );
        return slider;
    }

    /** Helper tạo ChoiceBox (Hộp chọn) với style */
    private ChoiceBox<ControlScheme> createStyledChoiceBox(ControlScheme defaultScheme) {
        ChoiceBox<ControlScheme> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(ControlScheme.ARROW_KEYS, ControlScheme.AD_KEYS);
        choiceBox.setValue(defaultScheme);
        choiceBox.setMaxWidth(Double.MAX_VALUE);

        choiceBox.setStyle(
                "-fx-background-color: #8A2BE2; " + // Tím
                        "-fx-mark-color: #00FFEF; " + // Xanh Cyan
                        "-fx-text-fill: #FFFFFF;" + // Chữ trắng
                        "-fx-font-weight: bold;"
        );
        return choiceBox;
    }

    /** Chuyển đổi giữa menu chính và menu cài đặt */
    private void showSettingsPane(boolean show) {
        settingsPane.setVisible(show);
        mainPausePane.setVisible(!show);
    }

    // === CÁC PHƯƠNG THỨC PUBLIC (cho MainGame gọi) ===

    public StackPane getRootNode() {
        return rootPane;
    }

    public void show() {
        rootPane.setVisible(true);
        showSettingsPane(false); // Luôn mở ở menu chính
    }

    public void hide() {
        rootPane.setVisible(false);
    }
}
