# 🎮 Brick Breaker Game

## 📖 Giới thiệu

**Brick Breaker** là một game arcade cổ điển được phát triển bằng Java và JavaFX. Người chơi điều khiển một thanh paddle để đánh bóng phá vỡ các viên gạch, mục tiêu là đạt được điểm số cao nhất có thể.

## ✨ Tính năng chính

### 🎯 Gameplay
- **Điều khiển đơn giản**: Sử dụng phím mũi tên trái/phải để di chuyển paddle
- **Vật lý thực tế**: Bóng có chuyển động tự nhiên với va chạm và phản xạ
- **Hệ thống điểm**: Ghi điểm khi phá vỡ gạch, mỗi viên gạch có độ bền khác nhau
- **Độ khó tăng dần**: Gạch được tạo ngẫu nhiên với vị trí và độ bền khác nhau

### 🎵 Âm thanh & Âm nhạc
- **Nhạc nền**: Nhạc menu và nhạc game riêng biệt
- **Hiệu ứng âm thanh**: 
  - Âm thanh khi bóng chạm paddle
  - Âm thanh khi phá vỡ gạch
  - Âm thanh game over
- **Hỗ trợ định dạng**: MP3 cho nhạc nền, WAV cho hiệu ứng

### 🏆 Hệ thống High Score
- **Top 5 điểm cao nhất**: Lưu trữ và hiển thị 5 điểm cao nhất
- **Lưu trữ bền vững**: Dữ liệu được lưu vào file `highscores.dat`
- **Nhập tên người chơi**: Khi đạt điểm cao, có thể nhập tên để lưu kỷ lục
- **Hiển thị ngày tháng**: Ghi nhận thời gian đạt điểm cao

### 🎨 Giao diện
- **Menu chính**: Giao diện đẹp mắt với các nút Start Game, High Score, Quit
- **Background động**: Hình nền với hiệu ứng chuyển động
- **Đồ họa 2D**: Sử dụng hình ảnh PNG cho các đối tượng game
- **Responsive**: Giao diện thích ứng với kích thước màn hình

## 🎮 Cách chơi

### Điều khiển
- **Phím mũi tên trái (←)**: Di chuyển paddle sang trái
- **Phím mũi tên phải (→)**: Di chuyển paddle sang phải
- **Phím R**: Restart game khi game over
- **Phím ESC**: Quay về menu chính khi game over

### Mục tiêu
1. Sử dụng paddle để đánh bóng lên phía trên
2. Phá vỡ tất cả các viên gạch để ghi điểm
3. Không để bóng rơi xuống dưới paddle
4. Đạt điểm số cao nhất có thể

### Cơ chế điểm
- Mỗi viên gạch có độ bền từ 1-3 HP
- Ghi điểm khi phá vỡ gạch
- Game over khi bóng rơi xuống dưới paddle

## 🛠️ Cài đặt và Chạy game

### Yêu cầu hệ thống
- **Java**: JDK 11 trở lên
- **JavaFX**: JavaFX SDK 17 trở lên
- **Hệ điều hành**: Windows, macOS, hoặc Linux

### Cài đặt JavaFX
1. Tải JavaFX SDK từ [OpenJFX](https://openjfx.io/)
2. Giải nén vào thư mục (ví dụ: `D:\javafx-sdk-25\`)
3. Cập nhật đường dẫn trong file `run.bat`

### Chạy game
1. **Cách 1 - Sử dụng batch file**:
   ```bash
   run.bat
   ```

2. **Cách 2 - Chạy thủ công**:
   ```bash
   # Compile
   javac --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml,javafx.media -cp "src" -d "out/production/BREAK" src/*.java
   
   # Run
   java --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml,javafx.media --enable-native-access=javafx.graphics,javafx.media -cp "out/production/BREAK" Main
   ```

## 📁 Cấu trúc dự án

```
BREAK/
├── src/                    # Mã nguồn Java
│   ├── Main.java          # Entry point của ứng dụng
│   ├── Game.java          # Logic game chính
│   ├── GamePanel.java     # Panel hiển thị game
│   ├── MenuPanel.java     # Panel menu chính
│   ├── HighScorePanel.java # Panel hiển thị điểm cao
│   ├── HighScoreManager.java # Quản lý điểm cao
│   ├── AudioManager.java  # Quản lý âm thanh
│   ├── Ball.java          # Class bóng
│   ├── Paddle.java        # Class paddle
│   ├── Brick.java         # Class gạch
│   ├── CollisionHandler.java # Xử lý va chạm
│   ├── Background.java    # Background động
│   └── ...               # Các class khác
├── assets/                # Tài nguyên game
│   ├── *.png             # Hình ảnh game
│   ├── *.mp3             # Nhạc nền
│   ├── *.wav             # Hiệu ứng âm thanh
│   └── *.ttf             # Font chữ
├── out/                   # File compiled
├── run.bat               # Script chạy game
└── README.md             # File này
```

## 🎨 Tài nguyên

### Hình ảnh
- **Ball**: `ball.panda.png` - Hình ảnh bóng
- **Paddle**: `sword.png` - Hình ảnh paddle (thanh kiếm)
- **Brick**: `thanh2.png` - Hình ảnh gạch
- **Background**: `sky.png`, `mountain.png`, `cloud1.png` - Hình nền

### Âm thanh
- **Menu Music**: `menu_music.mp3` - Nhạc nền menu
- **Game Music**: `game_music.mp3` - Nhạc nền game
- **Brick Hit**: `brick_hit.wav` - Âm thanh phá gạch
- **Paddle Hit**: `paddle_hit.wav` - Âm thanh chạm paddle
- **Game Over**: `game_over.wav` - Âm thanh game over

### Font chữ
- Nhiều font chữ đẹp mắt cho giao diện game

## 🔧 Tính năng kỹ thuật

### Kiến trúc
- **MVC Pattern**: Tách biệt logic game và giao diện
- **Event-driven**: Sử dụng JavaFX Event System
- **Animation Timer**: Vòng lặp game mượt mà 60 FPS

### Xử lý va chạm
- **Collision Detection**: Thuật toán va chạm chính xác
- **Physics**: Mô phỏng vật lý thực tế cho bóng
- **Boundary Detection**: Xử lý va chạm với biên màn hình

### Quản lý trạng thái
- **Game States**: Menu, Playing, Game Over, High Score
- **State Transitions**: Chuyển đổi trạng thái mượt mà
- **Data Persistence**: Lưu trữ điểm cao bền vững

## 🐛 Xử lý lỗi

### Lỗi thường gặp
1. **JavaFX không tìm thấy**: Kiểm tra đường dẫn JavaFX trong `run.bat`
2. **File âm thanh không load**: Đảm bảo file trong thư mục `assets/`
3. **Font không hiển thị**: Kiểm tra file font trong thư mục `assets/`

### Debug
- Game hiển thị thông báo lỗi trong console
- Kiểm tra file log để debug âm thanh
- Sử dụng try-catch để xử lý lỗi gracefully

## 🚀 Phát triển tương lai

### Tính năng có thể thêm
- [ ] Nhiều level với độ khó khác nhau
- [ ] Power-ups (tăng kích thước paddle, nhiều bóng, v.v.)
- [ ] Multiplayer mode
- [ ] Settings menu (điều chỉnh âm lượng, độ khó)
- [ ] Animation effects cho gạch khi bị phá
- [ ] Particle effects
- [ ] Online leaderboard

### Cải tiến kỹ thuật
- [ ] Unit tests
- [ ] Code documentation
- [ ] Performance optimization
- [ ] Cross-platform build scripts
- [ ] CI/CD pipeline

## 📝 Giấy phép

Dự án này được phát triển cho mục đích học tập và giải trí.

## 👨‍💻 Tác giả

Game được phát triển bằng Java và JavaFX với tình yêu dành cho game development.

---

**Chúc bạn chơi game vui vẻ! 🎮✨**
