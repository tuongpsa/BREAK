@echo off
echo ========================================
echo    JavaFX Setup for Brick Breaker
echo ========================================
echo.

echo This script will help you set up JavaFX for your project.
echo.

echo [1/4] Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java not found! Please install Java 11 or higher.
    pause
    exit /b 1
)
echo ✓ Java is installed!

echo.
echo [2/4] Checking if JavaFX SDK exists...
if exist "C:\javafx-sdk-17.0.2\lib" (
    echo ✓ JavaFX SDK already exists!
    goto :run_game
)

echo.
echo [3/4] JavaFX SDK not found. Please follow these steps:
echo.
echo 1. Go to: https://openjfx.io/
echo 2. Download JavaFX SDK for Windows
echo 3. Extract to: C:\javafx-sdk-17.0.2\
echo 4. Run this script again
echo.
echo Alternative: Download from:
echo https://gluonhq.com/products/javafx/
echo.
pause
exit /b 0

:run_game
echo.
echo [4/4] Running the game...
call run-javafx.bat


