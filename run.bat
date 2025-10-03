@echo off
echo ========================================
echo    BRICK BREAKER GAME
echo ========================================
echo.

echo Compiling Java files...
javac --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml,javafx.media -cp "src" -d "out/production/BREAK" src/*.java

if %errorlevel% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo Starting game...
java --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml,javafx.media --enable-native-access=javafx.graphics,javafx.media --add-exports javafx.base/com.sun.javafx=ALL-UNNAMED -cp "out/production/BREAK" Main

echo.
echo Game ended. Press any key to exit...
pause >nul
