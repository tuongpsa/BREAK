package game.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class để hiển thị dialog nhập tên người chơi
 */
public class NameInputDialog {
    
    public static String showDialog(int score) {
        try {
            TextInputDialog dialog = new TextInputDialog("Player");
            dialog.setTitle("New High Score!");
            dialog.setHeaderText("Congratulations! You achieved a high score!");
            dialog.setContentText("Your score: " + score + "\nEnter your name:");
            
            // Làm cho dialog modal và đặt style
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setAlwaysOnTop(true);
            stage.toFront(); // Đảm bảo dialog ở trên cùng
            stage.requestFocus(); // Đảm bảo dialog có focus
            
            // Hiển thị dialog và chờ kết quả
            java.util.Optional<String> result = dialog.showAndWait();
            
            if (result.isPresent() && !result.get().trim().isEmpty()) {
                return result.get().trim();
            } else {
                return "Anonymous";
            }
        } catch (Exception e) {
            System.out.println("Error showing name input dialog: " + e.getMessage());
            return "Anonymous";
        }
    }
    
    public static void showNotHighScoreDialog(int score) {
        try {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Game Over!");
            alert.setContentText("Your score: " + score + "\n\nPress R to restart or ESC to return to menu");
            
            // Làm cho dialog modal và đặt style
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setAlwaysOnTop(true);
            stage.toFront(); // Đảm bảo dialog ở trên cùng
            stage.requestFocus(); // Đảm bảo dialog có focus
            
            alert.showAndWait();
        } catch (Exception e) {
            System.out.println("Error showing not high score dialog: " + e.getMessage());
        }
    }
}
