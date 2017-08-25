import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class RBForImages extends RadioButton{
    public RBForImages (String s, ToggleGroup tg, Color col) {
        super(s);
        super.setToggleGroup(tg);
        super.setTextFill(col);
    }
}
