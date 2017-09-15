package forGUI;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Message extends Label {
    public Message (String str, int x, int y) {
        super(str);
        super.setTextFill(Color.DARKBLUE);
        super.relocate(x, y);
        super.setFont(new Font(13));
    }
}
