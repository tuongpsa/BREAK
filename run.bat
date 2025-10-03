@echo off
echo Compiling Java files...
javac --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml -cp "src" -d "out/production/BREAK" src/*.java

echo Running with JavaFX...
java --module-path "D:\javafx-sdk-25\lib" --add-modules javafx.controls,javafx.fxml -cp "out/production/BREAK" Main

pause


