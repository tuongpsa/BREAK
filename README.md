# 🎮 BREAK - Brick Breaker Game

Một game Brick Breaker (phá gạch) được phát triển bằng Java và JavaFX với nhiều tính năng thú vị.

## 📋 Mô tả

BREAK là một game phá gạch cổ điển với đồ họa đẹp mắt, hệ thống power-up đa dạng, và nhiều level thử thách. Người chơi điều khiển paddle để đánh bóng phá các viên gạch, thu thập power-up và vượt qua các level.

## ✨ Tính năng

### Gameplay
- **Nhiều level**: Hơn 13 level với độ khó tăng dần
- **Hệ thống điểm số**: Ghi điểm khi phá gạch, có hệ thống high score
- **Power-ups đa dạng**:
  - 🎯 **Score Multiplier**: Tăng điểm số x2 trong 15 giây
  - ⚡ **Multi Ball**: Tạo thêm bóng để phá gạch nhanh hơn
  - 🚀 **Speed Boost**: Tăng tốc độ di chuyển paddle
  - 📏 **Big Paddle**: Tăng kích thước paddle trong 15 giây
  - 🐌 **Slow Ball**: Làm chậm tốc độ bóng trong 10 giây
  - 🛡️ **Shield**: Bảo vệ bóng không rơi xuống dưới (1 lần)
  - 🔫 **Laser**: Bắn đạn để phá gạch từ xa (6 lần bắn)

### Hệ thống
- **Menu chính**: Giao diện menu với nhạc nền
- **Pause/Resume**: Tạm dừng game bất cứ lúc nào
- **Save/Load**: Lưu tiến độ game và tiếp tục chơi
- **High Score**: Lưu và hiển thị điểm cao nhất
- **Audio**: Nhạc nền và hiệu ứng âm thanh
- **Điều khiển**: Hỗ trợ phím A/D hoặc mũi tên trái/phải

## 🛠️ Yêu cầu hệ thống

- **Java**: JDK 11 trở lên
- **JavaFX SDK**: Phiên bản 17 trở lên
- **Hệ điều hành**: Windows, macOS, hoặc Linux

## 📦 Cài đặt

### 1. Tải JavaFX SDK

Tải JavaFX SDK từ [OpenJFX](https://openjfx.io/) và giải nén vào thư mục trên máy của bạn.

### 2. Cấu hình đường dẫn

Mở file `run.bat` và chỉnh sửa đường dẫn JavaFX SDK:

```batch
--module-path "D:\javafx-sdk-25\lib"
```

Thay `D:\javafx-sdk-25\lib` bằng đường dẫn JavaFX SDK trên máy của bạn.

### 3. Chạy game

**Windows:**
```batch
run.bat
```

**Linux/macOS:**
```bash
# Biên dịch
javac --module-path "/path/to/javafx-sdk/lib" \
      --add-modules javafx.controls,javafx.fxml,javafx.media \
      -cp "src" -d "out/production/BREAK" \
      -sourcepath "src" src/game/main/Main.java \
      src/game/audio/*.java src/game/core/*.java \
      src/game/objects/*.java src/game/render/*.java \
      src/game/score/*.java src/game/ui/*.java

# Chạy
java --module-path "/path/to/javafx-sdk/lib" \
     --add-modules javafx.controls,javafx.fxml,javafx.media \
     --enable-native-access=javafx.graphics,javafx.media \
     -cp "out/production/BREAK" game.main.Main
```

## 🎮 Hướng dẫn chơi

### Điều khiển
- **A / ←**: Di chuyển paddle sang trái
- **D / →**: Di chuyển paddle sang phải
- **ESC**: Pause/Resume game
- **Space**: Bắn laser (khi có power-up Laser)

### Mục tiêu
- Phá tất cả các viên gạch trong level để chuyển level tiếp theo
- Thu thập power-up để tăng sức mạnh
- Đạt điểm cao nhất có thể
- Tránh để bóng rơi xuống dưới (trừ khi có Shield)

## 📁 Cấu trúc dự án

```
BREAK/
├── assets/              # Tài nguyên game (hình ảnh, âm thanh, font)
│   ├── gui/            # Giao diện người dùng
│   └── *.png, *.wav, *.mp3, *.ttf
├── levels/             # File level (level1.txt, level2.txt, ...)
├── src/                # Mã nguồn
│   └── game/
│       ├── audio/      # Quản lý âm thanh
│       ├── core/       # Logic game chính
│       ├── main/       # Entry point
│       ├── objects/    # Đối tượng game (Ball, Brick, Paddle, PowerUp)
│       ├── render/     # Renderer cho các màn hình
│       ├── score/      # Quản lý điểm số
│       └── ui/         # Giao diện người dùng
├── test/               # Unit tests
├── out/                # File biên dịch
├── run.bat             # Script chạy game (Windows)
└── README.md           # File này
```

## 🧪 Testing

Dự án sử dụng JUnit 4 và JUnit 5 cho unit testing. Chạy tests:

```bash
# Với JUnit
java -cp "out/test/BREAK:junit-4.13.1.jar:hamcrest-core-1.3.jar" \
     org.junit.runner.JUnitCore game.core.GameTest
```

## 🎨 Tùy chỉnh

### Thêm level mới

Tạo file mới trong thư mục `levels/` với format:
- Số `0`: Không có gạch
- Số `1-3`: Gạch với HP tương ứng

Ví dụ `level14.txt`:
```
1 1 1 1 1
2 2 2 2 2
3 3 3 3 3
```

### Điều chỉnh cài đặt

Chỉnh sửa `GameSettings.java` để thay đổi:
- Âm lượng nhạc nền
- Âm lượng hiệu ứng
- Các cài đặt game khác

## 🐛 Xử lý lỗi

### Lỗi "JavaFX runtime components are missing"
- Đảm bảo đã tải và cấu hình đúng đường dẫn JavaFX SDK
- Kiểm tra lại đường dẫn trong `run.bat`

### Lỗi "Cannot find module"
- Kiểm tra các module JavaFX đã được thêm đúng: `javafx.controls`, `javafx.fxml`, `javafx.media`

### Game không chạy
- Đảm bảo JDK 11+ đã được cài đặt
- Kiểm tra các file assets có trong thư mục `assets/`

## 📝 License

Dự án này được phát triển cho mục đích học tập và giải trí.

## 👨‍💻 Tác giả

Phát triển bởi nhóm 10

## 🙏 Lời cảm ơn

- JavaFX team cho framework tuyệt vời
- Cộng đồng Java game development

---

**Chúc bạn chơi game vui vẻ! 🎮**

