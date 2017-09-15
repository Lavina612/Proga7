package forGUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MyImageView extends ImageView{
    public MyImageView(Image im) {
        super(im);
        super.setPreserveRatio(true);
        super.setFitHeight(400);
        super.setFitWidth(400);
    }
}
