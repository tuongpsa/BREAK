@echo off
echo Compiling Java files...
javac -cp "src" -d "out/production/BREAK" src/*.java

echo Running with JavaFX...
java --module-path "C:\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp "out/production/BREAK" Main

pause


