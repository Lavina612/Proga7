import javafx.scene.layout.*;

public class Root extends GridPane{
    public Root () {
        this.setStyle("-fx-background-color: lightcyan");
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(600);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(250);
        RowConstraints row1 = new RowConstraints();
        row1.setPrefHeight(500);
        RowConstraints row2 = new RowConstraints();
        row2.setPrefHeight(50);
        this.getColumnConstraints().addAll(col1, col2);
        this.getRowConstraints().addAll(row1, row2);
        this.setGridLinesVisible(true);
    }

}
