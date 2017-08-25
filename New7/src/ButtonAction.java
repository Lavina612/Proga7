import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ButtonAction extends Button {
    public ButtonAction(String s, int x, int y) {
        super.setText(s);
        super.setTextFill(Color.DARKBLUE);
        super.setStyle("-fx-background-color: paleturquoise");
        super.setPrefSize(160, 30);
        super.setFont(new Font(13));
        super.setAlignment(Pos.CENTER);
        super.relocate(x, y);
    }
}
