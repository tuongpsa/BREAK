@echo off
echo Compiling Java files...
javac --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml,javafx.media -cp "src" -d "out/production/BREAK" -sourcepath "src" src\game\main\Main.java src\game\audio\*.java src\game\core\*.java src\game\objects\*.java src\game\render\*.java src\game\score\*.java src\game\ui\*.java

echo Running with JavaFX...
java --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml,javafx.media --enable-native-access=javafx.graphics,javafx.media -cp "out/production/BREAK" game.main.Main

pause


