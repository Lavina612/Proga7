import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class ForButtonInteractive extends Button {
    public ForButtonInteractive (String str, int x, int y) {
        super(str);
        super.relocate(x, y);
        super.setPrefSize(70, 20);
        super.setAlignment(Pos.CENTER);
    }
}
