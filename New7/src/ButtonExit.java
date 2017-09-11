import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ButtonExit extends Button {
    public ButtonExit(String str, int x, int y) {
        super.setText(str);
        super.setTextFill(Color.DARKBLUE);
        super.setStyle("-fx-background-color: paleturquoise");
        super.setPrefSize(20, 10);
        super.setFont(new Font(13));
        super.setAlignment(Pos.CENTER);
        super.relocate(x, y);
    }
}
