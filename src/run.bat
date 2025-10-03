@echo off
echo Compiling and running Brick Breaker Game...
javac --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml,javafx.media -cp "." -d "../out/production/BREAK" *.java
cd ..
java --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml,javafx.media --enable-native-access=javafx.graphics,javafx.media --add-exports javafx.base/com.sun.javafx=ALL-UNNAMED -cp "out/production/BREAK" Main
pause
