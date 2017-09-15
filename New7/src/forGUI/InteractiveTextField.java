package forGUI;
import javafx.scene.control.TextField;

public class InteractiveTextField extends TextField {
    public InteractiveTextField (String str) {
        super.relocate(250, 230);
        super.setPromptText(str);
    }
}
