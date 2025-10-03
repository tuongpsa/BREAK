@echo off
echo ========================================
echo    BRICK BREAKER GAME - JavaFX
echo ========================================
echo.

echo [1/3] Compiling Java files...
javac -cp "src" -d "out/production/BREAK" src/*.java
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)
echo ✓ Compilation successful!

echo.
echo [2/3] Checking JavaFX SDK...
if not exist "D:\javafx-sdk-25\lib" (
    echo ERROR: JavaFX SDK not found at D:\javafx-sdk-25\lib
    echo Please check if JavaFX SDK is installed correctly
    pause
    exit /b 1
)
echo ✓ JavaFX SDK found!

echo.
echo [3/3] Starting game...
java --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml -cp "out/production/BREAK" Main

echo.
echo Game ended. Press any key to exit...
pause >nul
