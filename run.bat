@echo off
echo Compiling Java files...
javac --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml,javafx.media -cp "src" -d "out/production/BREAK" src/*.java

echo Running with JavaFX...
java --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml,javafx.media --enable-native-access=javafx.graphics,javafx.media -cp "out/production/BREAK" Main

pause


