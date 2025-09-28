package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label myLabel;

    @FXML
    protected void onHelloButtonClick() {
        myLabel.setText("Bạn vừa bấm nút!");
    }
}
