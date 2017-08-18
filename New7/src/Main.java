import java.io.*;
import java.time.LocalDate;
import java.util.Vector;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.application.Application;
import javafx.event.*;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 * @author Elizabeth, P3111 :)
 */

public class Main extends Application {
    private static Vector<Person> vector = new Vector<>();
    private static Person min = null;
    private static Label answer;
    private static Label message;
    private static Label message2;
    private static CheckBox chb1;
    private static CheckBox chb2;
    private static CheckBox chb3;
    private static Button close;
    private static Button enter;
    private static Button closeInteractive;
    private static RadioButton nameRB;
    private static RadioButton phraseRB;
    private static FlowPane image;
    private static GridPane root;
    private static AnchorPane pane10;
    private static AnchorPane pane11;
    private static FlowPane pane20;
    private static TreeView<String> tvPerson = new TreeView<>();
    private static TextField add;
    private static TextField indexField;
    private static TextField change;
    private static TextField phrase;
    private static int i = 0;
    private static Person ch1;
    private static String ch2;

    public void start(Stage stage) {
        stage.setTitle("Laba06");
        root = new GridPane();
        root.setStyle("-fx-background-color: lightcyan");
        HBox hbox00 = new HBox(10);
        FlowPane pane01 = new FlowPane(10, 10);
        pane10 = new AnchorPane();
        pane11 = new AnchorPane();
        pane11.getChildren().add(tvPerson);
        pane20 = new FlowPane(20, 15);
        FlowPane pane21 = new FlowPane(10, 10);
        image = new FlowPane();
        message = new Label("");
        message2 = new Label("min = " + min);
        message.setTextFill(Color.DARKBLUE);
        message.relocate(15, 450);
        message.setFont(new Font(13));
        message2.setTextFill(Color.DARKBLUE);
        message2.relocate(15, 470);
        message2.setFont(new Font(13));

        hbox00.setAlignment(Pos.CENTER);
        pane01.setAlignment(Pos.CENTER);
        pane20.setAlignment(Pos.CENTER);
        pane21.setAlignment(Pos.CENTER);
        image.setAlignment(Pos.CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(600);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(250);
        RowConstraints row1 = new RowConstraints();
        row1.setPrefHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPrefHeight(500);
        RowConstraints row3 = new RowConstraints();
        row3.setPrefHeight(50);
        root.getColumnConstraints().addAll(col1, col2);
        root.getRowConstraints().addAll(row1, row2, row3);
        root.setGridLinesVisible(true);

        answer = new Label("");
        Label question = new Label("Will you give me a variant for laba 8?");
        chb1 = new CheckBox("Yes");
        chb2 = new CheckBox("Of course");
        chb3 = new CheckBox("Absolutely");
        chb1.setOnAction(event -> {
                    if (chb1.isSelected())
                        answer.setText("Thank you!");
                    else answer.setText("Don't remove this stick");
                }
        );
        chb2.setOnAction(event -> {
                    if (chb2.isSelected())
                        answer.setText("Thank you very much!");
                    else answer.setText("Don't remove this stick!");
                }
        );
        chb3.setOnAction(event -> {
                    if (chb3.isSelected())
                        answer.setText("Ooh, it's so nice! You're so kind!");
                    else answer.setText("Don't remove this stick!!!");
                }
        );

        ProgressBar bar = new ProgressBar();

        DatePicker datePick = new DatePicker();
        datePick.setValue(LocalDate.of(2017, 6, 20));
        datePick.setShowWeekNumbers(true);
        datePick.relocate(350, 10);
        Button load = new Button("load");
        Button remove_last = new Button("remove last element");
        Button remove = new Button("remove (index)");
        Button add_if_min = new Button("add if min");
        Button save = new Button("save");
        Button update = new Button("update");
        load.setTextFill(Color.DARKBLUE);
        load.setStyle("-fx-background-color: paleturquoise");
        load.setPrefSize(140, 30);
        load.setFont(new Font(13));
        load.setAlignment(Pos.CENTER);
        update.setTextFill(Color.DARKBLUE);
        update.setStyle("-fx-background-color: paleturquoise");
        update.setPrefSize(140, 30);
        update.setFont(new Font(13));
        update.setAlignment(Pos.CENTER);
        save.setTextFill(Color.DARKBLUE);
        save.setStyle("-fx-background-color: paleturquoise");
        save.setPrefSize(140, 30);
        save.setFont(new Font(13));
        save.setAlignment(Pos.CENTER);
        remove.setTextFill(Color.DARKBLUE);
        remove.setStyle("-fx-background-color: paleturquoise");
        remove.setPrefSize(140, 30);
        remove.setFont(new Font(13));
        remove.setAlignment(Pos.CENTER);
        remove_last.setTextFill(Color.DARKBLUE);
        remove_last.setStyle("-fx-background-color: paleturquoise");
        remove_last.setPrefSize(140, 30);
        remove_last.setFont(new Font(13));
        remove_last.setAlignment(Pos.CENTER);
        add_if_min.setTextFill(Color.DARKBLUE);
        add_if_min.setStyle("-fx-background-color: paleturquoise");
        add_if_min.setPrefSize(140, 30);
        add_if_min.setFont(new Font(13));
        add_if_min.setAlignment(Pos.CENTER);
        load.relocate(15, 130);
        update.relocate(15, 170);
        save.relocate(15, 210);
        remove_last.relocate(15, 250);
        remove.relocate(15, 290);
        add_if_min.relocate(15, 330);
        enter = new Button("Enter");
        enter.relocate(440, 230);
        enter.setPrefSize(70, 20);
        enter.setAlignment(Pos.CENTER);
        closeInteractive = new Button("Close");
        closeInteractive.relocate(440, 270);
        closeInteractive.setPrefSize(70, 20);
        closeInteractive.setAlignment(Pos.CENTER);
        load.setOnAction(event -> {
            load();
            getMin();
            rewriting();
        });
        remove_last.setOnAction(event -> {
            remove_last();
            getMin();
            rewriting();
        });
        save.setOnAction(event -> save());
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checking();
                indexField = new TextField();
                indexField.relocate(250, 230);
                indexField.setPromptText("Write number of element");
                pane10.getChildren().addAll(indexField, enter, closeInteractive);
                enter.setOnAction(event1 -> index(indexField));
                indexField.setOnAction(event1 -> index(indexField));
                closeInteractive.setOnAction(event1 -> pane10.getChildren().removeAll(indexField, enter, closeInteractive));
            }
        });
        add_if_min.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checking();
                add = new TextField();
                add.relocate(220, 230);
                add.setPrefColumnCount(16);
                add.setPromptText("Enter JSON-person");
                pane10.getChildren().addAll(add, enter, closeInteractive);
                enter.setOnAction(event1 -> add_if_min(add));
                add.setOnAction(event1 -> add_if_min(add));
                closeInteractive.setOnAction(event1 -> pane10.getChildren().removeAll(add, phrase, enter, closeInteractive));
            }
        });
        update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checking();
                if (pane10.getChildren().contains(nameRB)) pane10.getChildren().remove(nameRB);
                if (pane10.getChildren().contains(phraseRB)) pane10.getChildren().remove(phraseRB);
                change = new TextField();
                change.relocate(250, 230);
                change.setPromptText("Enter number of person");
                pane10.getChildren().addAll(change, enter, closeInteractive);
                enter.setOnAction(event1 -> method2());
                change.setOnAction(event1 -> method2());
                closeInteractive.setOnAction(event1 -> {
                    pane10.getChildren().removeAll(change, enter, closeInteractive);
                    if (pane10.getChildren().contains(nameRB)) pane10.getChildren().remove(nameRB);
                    if (pane10.getChildren().contains(phraseRB)) pane10.getChildren().remove(phraseRB);
                });
            }
        });

        Image im1 = new Image("/images/123.jpg");
        ImageView imv1 = new ImageView(im1);
        imv1.setPreserveRatio(true);
        imv1.setFitHeight(400);
        imv1.setFitWidth(400);
        Image im2 = new Image("/images/вт2.jpg");
        ImageView imv2 = new ImageView(im2);
        imv2.setPreserveRatio(true);
        imv2.setFitHeight(400);
        imv2.setFitWidth(400);
        Image im3 = new Image("/images/778.jpg");
        ImageView imv3 = new ImageView(im3);
        imv3.setPreserveRatio(true);
        imv3.setFitHeight(400);
        imv3.setFitWidth(400);
        Image im4 = new Image("/images/Письмак.jpg");
        ImageView imv4 = new ImageView(im4);
        imv4.setPreserveRatio(true);
        imv4.setFitHeight(400);
        imv4.setFitWidth(400);
        close = new Button("Close");
        close.setOnAction(event -> {
            pane20.getChildren().remove(close);
            root.getChildren().remove(image);
        });
        Label iWant = new Label("I want to see...");
        RadioButton rb1 = new RadioButton("VT");
        RadioButton rb2 = new RadioButton("duck");
        RadioButton rb3 = new RadioButton("our dreams");
        RadioButton rb4 = new RadioButton("my photo");
        ToggleGroup tg = new ToggleGroup();
        iWant.setTextFill(Color.DARKVIOLET);
        rb1.setTextFill(Color.DARKRED);
        rb2.setTextFill(Color.DARKGOLDENROD);
        rb3.setTextFill(Color.DARKMAGENTA);
        rb4.setTextFill(Color.DARKBLUE);
        rb1.setToggleGroup(tg);
        rb2.setToggleGroup(tg);
        rb3.setToggleGroup(tg);
        rb4.setToggleGroup(tg);
        rb1.setOnAction(event -> imagination(imv1, imv4, imv2, imv3));
        rb2.setOnAction(event -> imagination(imv2, imv1, imv4, imv3));
        rb3.setOnAction(event -> imagination(imv3, imv1, imv2, imv4));
        rb4.setOnAction(event -> imagination(imv4, imv1, imv2, imv3));

        Button filterName = new Button("Sort on name");
        Button filterPhr = new Button("Sort on phrase");
        filterName.setOnAction(event -> {
            for (int i = 0; i < vector.size(); i++) {
                for (int j = i + 1; j < vector.size(); j++) {
                    if (vector.get(i).compareTo(vector.get(j)) > 0) {
                        ch1 = vector.get(i);
                        vector.set(i, vector.get(j));
                        vector.set(j, ch1);
                    }
                }
            }
            rewriting();
            message.setText("Collection sorted on names");
        });
        filterPhr.setOnAction(event -> {
            for (Person person : vector) {
                for (int j = 0; j < person.getPhrases().size(); j++) {
                    for (int k = j + 1; k < person.getPhrases().size(); k++) {
                        if (person.getPhrases().get(j).getPhrase().compareTo(person.getPhrases().get(k).getPhrase()) < 0) {
                            ch2 = person.getPhrases().get(j).getPhrase();
                            person.getPhrases().get(j).setPhrase(person.getPhrases().get(k).getPhrase());
                            person.getPhrases().get(k).setPhrase(ch2);
                        }
                    }
                }
            }
            rewriting();
            message.setText("Collection sorted on phrases");
        });
        filterName.relocate(60, 415);
        filterPhr.relocate(60, 455);
        filterName.setTextFill(Color.DARKBLUE);
        filterName.setStyle("-fx-background-color: paleturquoise");
        filterName.setPrefSize(120, 25);
        filterName.setFont(new Font(13));
        filterName.setAlignment(Pos.CENTER);
        filterPhr.setTextFill(Color.DARKBLUE);
        filterPhr.setStyle("-fx-background-color: paleturquoise");
        filterPhr.setPrefSize(120, 25);
        filterPhr.setFont(new Font(13));
        filterPhr.setAlignment(Pos.CENTER);
        pane11.getChildren().addAll(filterName, filterPhr);

        Button exit = new Button("Exit");
        exit.setOnAction(event -> exit());
        load();
        getMin();
        rewriting();
        hbox00.getChildren().addAll(question, chb1, chb2, chb3, answer);
        pane01.getChildren().addAll(bar);
        pane10.getChildren().addAll(datePick, load, update, save, remove_last, remove, add_if_min, message, message2);
        pane20.getChildren().addAll(iWant, rb1, rb2, rb3, rb4);
        pane21.getChildren().addAll(exit);
        root.add(hbox00, 0, 0);
        root.add(pane01, 1, 0);
        root.add(pane10, 0, 1);
        root.add(pane11, 1, 1);
        root.add(pane20, 0, 2);
        root.add(pane21, 1, 2);
        Scene scene = new Scene(root, 850, 600);
        stage.setScene(scene);
/*        load.setId("1");
        remove_last.setId("1");
        exit.getStyleClass().add("exit");
        chb1.getStyleClass().add("chb1");
        scene.getStylesheets().add((getClass().getResource("/css/styles.css")).toExternalForm());
 */       root.setStyle("-fx-background-color: snow");
        stage.show();
        stage.setOnCloseRequest(event -> {
            save();
            System.exit(0);
                }
        );
    }


    private static void method0(int a, int b) {
        vector.get(a).setPhrase(b, new Phrase(change.getText()));
        change.setText("");
        getMin();
        rewriting();
        message.setText("Phrase is updated");
    }

    private static void method1(int a) {
        try {
            Integer int2 = new Integer(change.getText());
            int b = --int2;
            change.setText("");
            if (int2 < 0)
                message.setText("The number can't be negative");
            else if (int2 >= vector.get(a).getPhrases().size())
                message.setText("The number more than elements in Collection");
            else {
                pane10.getChildren().remove(change);
                change.setPromptText("Enter a phrase");
                pane10.getChildren().add(change);
                change.setOnAction(event2 -> method0(a,b));
                enter.setOnAction(event2 -> method0(a,b));
            }
        } catch (NumberFormatException e) {
            message.setText("Sorry, but you wrote a nonsense");
            change.setText("");
        }
    }

    private static void method2() {
        try {
            Integer integer = new Integer(change.getText());
            integer--;
            if (integer < vector.size()) {
                if (integer >= 0) {
                    int a = integer;
                    change.setText("");
                    pane10.getChildren().remove(change);
                    nameRB = new RadioButton("Name");
                    phraseRB = new RadioButton("Phrase");
                    ToggleGroup np = new ToggleGroup();
                    nameRB.setToggleGroup(np);
                    phraseRB.setToggleGroup(np);
                    nameRB.relocate(250, 235);
                    phraseRB.relocate(250, 270);
                    pane10.getChildren().addAll(nameRB, phraseRB);
                    nameRB.setOnAction(event -> {
                            pane10.getChildren().removeAll(nameRB, phraseRB);
                            change.setPromptText("Enter name");
                            pane10.getChildren().add(change);
                            enter.setOnAction(event1 -> method3(a));
                            change.setOnAction(event1 -> method3(a));
                        }
                    );
                    phraseRB.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            pane10.getChildren().removeAll(nameRB, phraseRB);
                            if(vector.get(a).getPhrases().isEmpty()) {
                                message.setText("There are not phrases");
                            }
                            else {
                                change.setPromptText("Enter number of phrase");
                                pane10.getChildren().add(change);
                                enter.setOnAction(event1 -> {
                                    method1(a);
                                });
                                change.setOnAction(event1 -> {
                                    method1(a);
                                });
                            }
                        }
                    });
                } else {
                    message.setText("Number can't be negative");
                    change.setText("");
                }
            } else {
                message.setText("The number more than elements in Collection");
                change.setText("");
            }
        } catch (NumberFormatException e) {
            message.setText("Sorry, but you wrote a nonsense");
            change.setText("");
        }
    }

    private static void method3 (int a) {
        if(change.getText().trim().isEmpty()) {
            return;
        }
        vector.get(a).setName(change.getText());
        change.setText("");
        getMin();
        rewriting();
        message.setText("Name is updated");
    }

    /**
     * This command does checking.
     */
    private static void checking() {
        if (pane10.getChildren().contains(indexField)) pane10.getChildren().remove(indexField);
        if (pane10.getChildren().contains(enter)) pane10.getChildren().remove(enter);
        if (pane10.getChildren().contains(closeInteractive)) pane10.getChildren().remove(closeInteractive);
        if (pane10.getChildren().contains(add)) pane10.getChildren().remove(add);
        if (pane10.getChildren().contains(change)) pane10.getChildren().remove(change);
    }

    /**
     * This command removes need element from the Collection.
     */
    private static void index(TextField indexField) {
        try {
            Integer integer = new Integer(indexField.getText());
            remove(integer);
            indexField.setText("");
        } catch (NumberFormatException e) {
            if (indexField.getText().equals("")) message.setText("You didn't write anything");
            else message.setText("Sorry, but you wrote a nonsense. Try again!");
            indexField.setText("");
        }
    }

    /**
     * This command works with pictures.
     */
    private static void imagination(ImageView a, ImageView b, ImageView c, ImageView d) {
        image.getChildren().removeAll(b, c, d);
        root.getChildren().remove(image);
        pane20.getChildren().remove(close);
        image.getChildren().add(a);
        root.add(image, 0, 1);
        pane20.getChildren().add(close);
    }

    /**
     * This command rewrites the Collection.
     */
    private static void rewriting() {
        pane11.getChildren().remove(tvPerson);
        TreeItem<String> tiRoot = new TreeItem<>("Persons");
        for (int i = 0; i < vector.size(); i++) {
            TreeItem<String> tiPerson = new TreeItem<>("Person " + (i + 1));
            tiRoot.getChildren().add(tiPerson);
            TreeItem<String> tiName = new TreeItem<>("Name");
            tiPerson.getChildren().add(tiName);
            tiName.getChildren().add(new TreeItem<>(vector.get(i).getName()));
            if (!vector.get(i).getPhrases().isEmpty()) {
                TreeItem<String> tiPhrases = new TreeItem<>("Phrases");
                tiPerson.getChildren().add(tiPhrases);
                for (int j = 0; j < vector.get(i).getPhrases().size(); j++) {
                    TreeItem<String> tiPhrase = new TreeItem<>("Phrase " + (j + 1));
                    tiPhrases.getChildren().add(tiPhrase);
                    if (vector.get(i).getPhrases().get(j).getPhrase().isEmpty()) {
                        tiPhrase.getChildren().add(new TreeItem<>("I was silent!"));
                    } else
                        tiPhrase.getChildren().add(new TreeItem<>(vector.get(i).getPhrases().get(j).getPhrase()));
                }
            }
        }
        tvPerson = new TreeView<>(tiRoot);
        tvPerson.relocate(0, 0);
        pane11.getChildren().add(tvPerson);
    }

    /**
     * This command does exit from programme.
     */
    private static void exit() {
        message.setText("Bye!");
        System.exit(0);
    }

    /**
     * This command finds the minimum from the Collection.
     */
    private static void getMin() {
        if (!vector.isEmpty()) {
            min = vector.get(0);
            for (Person person : vector) {
                if (min.compareTo(person) > 0) min = person;
            }
            message2.setText("min = " + min);
        } else {
            message.setText("No elements in Collection");
            min = null;
            message2.setText("min = null");
        }
    }

    /**
     * This command removes the last element from the Collection.
     */
    private static void remove_last() {
        if (!vector.isEmpty()) {
            vector.remove(vector.lastElement());
            message.setText("Remove_last is done :)");
        } else message.setText("Vector is empty");
    }

    /**
     * This command removes the element from the Collection, which have this index.
     */
    private static void remove(int index) {
        if (vector.isEmpty()) {
            message.setText("Vector is empty");
        } else {
            if (index > vector.size()) {
                message.setText("The number more than elements in Collection");
            } else {
                if (index < 0) {
                    message.setText("The number can't be negative");
                } else {
                    vector.remove(index - 1);
                    getMin();
                    rewriting();
                    message.setText("Remove is done :)");
                }
            }
        }
    }


    /**
     * This command reads all data from the file into Collection.
     */
    private static void load() {
        vector.removeAllElements();
        InputStreamReader in = null;
        int c;
        String str = "";
        String[] strs;
        try {
            in = new InputStreamReader(new FileInputStream("../../Proba.csv"));
            while ((c = in.read()) != -1) {
                if (c == '\n' || c == '\r') {
                    if (!str.trim().equals("")) {
                        Person person = new Person();
                        strs = str.split(";");
                        m(strs, person);
                        str = "";
                    }
                } else {
                    str = str + (char) c;
                }
            }
            if (!str.trim().equals("")) {
                Person person = new Person();
                strs = str.split(";");
                m(strs, person);
            }
        } catch (IOException ioe) {
            System.out.println("Error with path to the file (input)");
            System.exit(1);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                System.out.println("Error with close in 1");
            } catch (NullPointerException e) {
                System.out.println("Error with close in 2");
            }
        }
        if (!vector.isEmpty())
            message.setText("Loading is done :)");
    }

    private static void m(String[] strs, Person person) {
        if (strs.length != 0) {
            if (!strs[0].trim().equals("")) {
                person.setName(strs[0].trim());
                if (strs.length - 1 != 0) {
                    for (int j = 1; j < strs.length; j++) {
                        person.addPhrase(new Phrase(strs[j].trim()));
                    }
                }
                vector.add(person);
            } else {
                message.setText("Sorry, but person must have a name");
            }
        }
    }

    /**
     * This command saves all elements in file.
     */
    private static void save() {
        String str = "";
        FileWriter out = null;
        try {
            out = new FileWriter("../../Proba.csv", false);
            for (Person person : vector) {
                for (int n = 0; n < person.getPhrases().size(); n++) {
                    str = str.concat(";" + person.getPhrase(n));
                }
                str = person.getName().concat(str + '\r' + '\n');
                out.write(str);
                str = "";
            }
            message.setText("Save is done :)");
        } catch (IOException e) {
            System.out.println("Error with access to the file");
        } finally {
            try {
                out.close();
            } catch (IOException ioe) {
                System.out.println("Error with close out");
            } catch (NullPointerException e) {
                System.out.println("Error with path to the file (output)");
            }
        }
    }

    /**
     * This command adds the element into Collection if this element less than min element of this Collection.
     */
    private static void add_if_min(TextField element) {
        try {
            Gson gson = new Gson();
            Person person = gson.fromJson(element.getText(), Person.class);
            if (!person.getName().trim().equals("")) {
    /*            if (min == null) {
                    vector.addElement(person);
                rewriting();
                element.setText("");
                message.setText("Add_if_min is done :)");
                message2.setText("min = " + min.getName()); */
                if (min.compareTo(person) > 0) {
                    min = person;
                    vector.addElement(person);
                    rewriting();
                    element.setText("");
                    message.setText("Add_if_min is done :)");
                    message2.setText("min = " + min.getName());
                } else {
                    message.setText("Element is more than min");
                    element.setText("");
                }
            } else {
                message.setText("Sorry, but person must have a name");
            }
        } catch (JsonSyntaxException e) {
     /*       if (!"".equals(element.getText())) {
                Person person = new Person(element.getText());
                if (min.compareTo(person) > 0) {
                    min = person;
                    vector.addElement(person);
                    rewriting();
                    pane10.getChildren().remove(element);
                    message.setText("Add_if_min is done :)");
                    message2.setText("min = " + min.getName());
                    phrase = new TextField();
                    phrase.relocate(220, 230);
                    phrase.setPrefColumnCount(16);
                    phrase.setPromptText("Enter phrase " + (i + 1));
                    pane10.getChildren().add(phrase);
                    phrase.setOnAction(event -> {
                        if (!vector.isEmpty()) {
                            vector.get(vector.size()-1).setPhrase(i, new Phrase(phrase.getText()));
                            i++;
                            phrase.setText("");
                            rewriting();
                        } else message.setText("Something");
                    });
                } else {
                    System.out.println("Element is more than min");
                    element.setText("");
                }
            } else */
                message.setText("Sorry, it's not JSON");
        } catch (NullPointerException e) {
            message.setText("Sorry, null cannot be here");
            element.setText("");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//C:\Users\Elizabeth\Desktop\ИТМО\Программирование\Лабораторные\Лаба05\Proba.csv

/* Посмотреть imagine
add_if_min
целиком код
комментарии
 */