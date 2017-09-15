package forGUI;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ButtonForUpdate extends RadioButton{
    public ButtonForUpdate(String str, int x, int y, ToggleGroup np){
        super(str);
        super.setToggleGroup(np);
        super.relocate(x, y);

    }
}
