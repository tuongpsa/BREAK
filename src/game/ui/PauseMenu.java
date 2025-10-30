package game.ui; // Bạn có thể đặt tên package là game.core nếu muốn

import game.audio.AudioManager;
import game.core.ControlScheme;
import game.core.GameSettings;
import game.core.PauseManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Lớp này quản lý toàn bộ giao diện (UI) của menu pause.
 * Nó chứa 2 "màn hình": menu chính và menu cài đặt.
 */
public class PauseMenu {

    // --- Các thành phần UI chính ---
    private final StackPane rootPane;       // Lớp nền đen mờ
    private final VBox mainPausePane;       // Hộp chứa menu "Tạm dừng"
    private final VBox settingsPane;        // Hộp chứa menu "Cài đặt"

    // --- Tham chiếu đến các hệ thống khác ---
    private final PauseManager pauseManager;
    private final GameSettings gameSettings;
    private final AudioManager audioManager;

    /**
     * Tạo giao diện menu pause.
     * @param pauseManager     Để điều khiển pause/resume.
     * @param gameSettings     Để đọc/ghi cài đặt.
     * @param audioManager     Để áp dụng cài đặt âm thanh.
     * @param onRestart        Hàm (Runnable) được gọi khi bấm "Restart".
     * @param onExit           Hàm (Runnable) được gọi khi bấm "Exit" (về Menu chính).
     */
    public PauseMenu(PauseManager pauseManager, GameSettings gameSettings, AudioManager audioManager, Runnable onRestart, Runnable onExit) {
        this.pauseManager = pauseManager;
        this.gameSettings = gameSettings;
        this.audioManager = audioManager;

        // === 1. Lớp Nền (Root) ===
        // StackPane để xếp chồng các menu lên nhau
        rootPane = new StackPane();
        // CSS để làm nền đen mờ 60%
        rootPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");
        rootPane.setAlignment(Pos.CENTER);
        rootPane.setVisible(false); // Ẩn lúc đầu

        // === 2. Pane 1: Menu Pause Chính ===
        mainPausePane = createMenuVBox(); // Dùng hàm helper

        Label title = createLabel("TẠM DỪNG", 40);

        Button resumeButton = createMenuButton("TIẾP TỤC");
        Button restartButton = createMenuButton("CHƠI LẠI");
        Button settingsButton = createMenuButton("CÀI ĐẶT");
        Button exitButton = createMenuButton("VỀ MENU CHÍNH");

        // Gán hành động cho các nút
        resumeButton.setOnAction(e -> pauseManager.resume());

        restartButton.setOnAction(e -> {
            pauseManager.resume(); // Quan trọng: Luôn resume trước khi làm việc khác
            onRestart.run();
        });

        exitButton.setOnAction(e -> {
            pauseManager.resume(); // Quan trọng: Luôn resume
            onExit.run();
        });

        settingsButton.setOnAction(e -> showSettingsPane(true)); // Chuyển sang menu Cài đặt

        mainPausePane.getChildren().addAll(title, resumeButton, restartButton, settingsButton, exitButton);

        // === 3. Pane 2: Menu Cài đặt ===
        settingsPane = createMenuVBox();
        settingsPane.setVisible(false); // Ẩn lúc đầu

        Label settingsTitle = createLabel("CÀI ĐẶT", 30);
        settingsTitle.setAlignment(Pos.CENTER);
        settingsTitle.setMaxWidth(Double.MAX_VALUE);

        // --- Thanh trượt âm lượng nhạc ---
        Label musicLabel = createLabel("Âm lượng Nhạc:", 18);
        Slider musicSlider = new Slider(0, 1, gameSettings.getMusicVolume()); // Min=0, Max=1, Default=giá trị hiện tại
        // Thêm listener: Khi kéo thanh trượt...
        musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            // ...gọi AudioManager, nó sẽ tự cập nhật GameSettings
            audioManager.setMusicVolume(newVal.doubleValue());
        });

        // --- Thanh trượt âm lượng hiệu ứng (SFX) ---
        Label sfxLabel = createLabel("Âm lượng Hiệu ứng:", 18);
        Slider sfxSlider = new Slider(0, 1, gameSettings.getSfxVolume());
        sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audioManager.setSoundEffectsVolume(newVal.doubleValue());
        });

        // --- Chọn kiểu điều khiển ---
        Label controlLabel = createLabel("Điều khiển:", 18);
        ChoiceBox<ControlScheme> controlChoice = new ChoiceBox<>();
        controlChoice.getItems().addAll(ControlScheme.ARROW_KEYS, ControlScheme.AD_KEYS);
        controlChoice.setValue(gameSettings.getControlScheme()); // Đặt giá trị mặc định
        controlChoice.setMaxWidth(Double.MAX_VALUE);
        // Khi người chơi chọn...
        controlChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            gameSettings.setControlScheme(newVal); // ...lưu cài đặt
        });

        Button backButton = createMenuButton("QUAY LẠI");
        backButton.setOnAction(e -> showSettingsPane(false)); // Quay về menu chính

        settingsPane.getChildren().addAll(
                settingsTitle,
                musicLabel, musicSlider,
                sfxLabel, sfxSlider,
                controlLabel, controlChoice,
                backButton
        );

        // Thêm cả 2 pane vào root (thứ tự quan trọng, mainPausePane ở trên)
        rootPane.getChildren().addAll(settingsPane, mainPausePane);
    }

    // === 4. Các hàm Helper (Trợ giúp) ===

    /** Chuyển đổi giữa menu chính và menu cài đặt */
    private void showSettingsPane(boolean show) {
        settingsPane.setVisible(show);
        mainPausePane.setVisible(!show);
    }

    /** Helper để tạo VBox menu chuẩn */
    private VBox createMenuVBox() {
        VBox vbox = new VBox(20); // Khoảng cách 20px
        vbox.setAlignment(Pos.CENTER);
        vbox.setMaxWidth(300);
        // CSS cho hộp menu
        vbox.setStyle("-fx-background-color: #222; -fx-padding: 30; -fx-border-color: #CCC; -fx-border-width: 2;");
        return vbox;
    }

    /** Helper để tạo nút bấm chuẩn */
    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setFont(new Font("Arial", 20));
        btn.setMaxWidth(Double.MAX_VALUE); // Cho nút rộng tối đa
        return btn;
    }

    /** Helper để tạo nhãn (Label) chuẩn */
    private Label createLabel(String text, int fontSize) {
        Label label = new Label(text);
        label.setFont(new Font("Arial", fontSize));
        label.setTextFill(Color.WHITE);
        return label;
    }

    // === 5. Phương thức Public (cho class MainGame gọi) ===

    /**
     * Trả về Node UI (StackPane) để thêm vào Scene chính của game.
     */
    public StackPane getRootNode() {
        return rootPane;
    }

    /**
     * Hiển thị menu pause.
     * Được gọi bởi PauseListener.
     */
    public void show() {
        rootPane.setVisible(true);
        // Đảm bảo menu chính luôn hiển thị khi mở, chứ không phải menu setting
        showSettingsPane(false);
    }

    /**
     * Ẩn menu pause.
     * Được gọi bởi PauseListener.
     */
    public void hide() {
        rootPane.setVisible(false);
    }
}