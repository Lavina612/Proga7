import java.util.ListResourceBundle;

public class MyLoc extends ListResourceBundle {
    public Object[][] getContents() {
        return new Object[][] {
                {"greeting", "Hello!"},
                {"message", "How are you?"},
                {"farewell", "Goodbye!"}
        };
    }
}
