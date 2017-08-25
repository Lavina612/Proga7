import java.io.*;
import java.net.InetAddress;
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
import javafx.stage.*;

/**
 * @author Elisa, P3111
 * @version 1.0
 */

public class Main extends Application {
    private static Vector<Person> vector = new Vector<>();
    private static Root root = new Root();
    private static PaneUp pane00 = new PaneUp();
    private static PaneUp pane01 = new PaneUp();
    private static PaneDown pane10 = new PaneDown();
    private static PaneDown pane11 = new PaneDown();
    private ButtonAction loadButton = new ButtonAction("Перечитать из файла", 15, 130);
    private ButtonAction updateButton = new ButtonAction("Обновить", 15, 170);
    private ButtonAction saveButton = new ButtonAction("Сохранить в файл", 15, 210);
    private ButtonAction removeLastButton = new ButtonAction("Удалить последний элемент", 15, 250);
    private ButtonAction removeButton = new ButtonAction("Удалить (по индексу)", 15, 290);
    private ButtonAction addIfMinButton = new ButtonAction("Добавить, если меньше", 15, 330);






    private static Person min = null;
    private static Label message = new Label("");
    private static Label messageForMin = new Label("min = " + min);
    private static Button close = new Button("Close");
    private static Button enter = new Button("Enter");
    private static Button closeInteractive = new Button("Close");
    private static RadioButton nameRB;
    private static RadioButton phraseRB;
    private static FlowPane image = new FlowPane();
    private static TreeView<String> tvPerson = new TreeView<>();
    private static TextField add;
    private static TextField indexField;
    private static TextField change;
    private static TextField phrase;
    private static Person ch1;
    private static String ch2;



    public void start(Stage stage) {
        stage.setTitle("Laba07");
        root.add(pane00, 0, 0);
        root.add(pane01, 1, 0);
        root.add(pane10, 0, 1);
        root.add(pane11, 1, 1);
        MyImageView imv1 = new MyImageView(new Image("/images/БольИСтрадания.jpg"));
        MyImageView imv2 = new MyImageView(new Image("/images/Утка.jpg"));
        MyImageView imv3 = new MyImageView(new Image("/images/Мечты.jpg"));
        MyImageView imv4 = new MyImageView(new Image("/images/СамыйЛучшийПреподавательНаСвете.jpg"));
        Label iWant = new Label("Я хочу увидеть...");
        iWant.setTextFill(Color.DARKVIOLET);
        ToggleGroup tg = new ToggleGroup();
        RBForImages rb1 = new RBForImages("ВТ", tg, Color.DARKRED);
        RBForImages rb2 = new RBForImages("уточку", tg, Color.DARKGOLDENROD);
        RBForImages rb3 = new RBForImages("наши сны", tg, Color.DARKMAGENTA);
        RBForImages rb4 = new RBForImages("моё фото", tg, Color.DARKBLUE);


        rb1.setOnAction(event -> imagination(imv1, imv4, imv2, imv3));
        rb2.setOnAction(event -> imagination(imv2, imv1, imv4, imv3));
        rb3.setOnAction(event -> imagination(imv3, imv1, imv2, imv4));
        rb4.setOnAction(event -> imagination(imv4, imv1, imv2, imv3));

        pane00.getChildren().addAll(loadButton, updateButton, saveButton, removeLastButton, removeButton, addIfMinButton);
        pane10.getChildren().addAll(iWant, rb1, rb2, rb3, rb4);
 //       load();
 //       getMin();
//        rewriting();
        Scene scene = new Scene(root, 850, 550);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Метод переписывает дерево Person-ов для актуального отображения данных
     */
    private static void rewriting() {
        pane01.getChildren().remove(tvPerson);
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
        pane01.getChildren().add(tvPerson);
    }

    /**
     * Метод добавляет нужную картинку в окошко
     * @param a картинка, которую нужно добавить
     * @param b картинка, которую нужно удалить
     * @param c картинка, которую нужно удалить
     * @param d картинка, которую нужно удалить
     */
    private static void imagination(ImageView a, ImageView b, ImageView c, ImageView d) {
        image.getChildren().removeAll(b, c, d);
        root.getChildren().remove(image);
        pane10.getChildren().remove(close);
        image.setAlignment(Pos.CENTER);
        image.getChildren().add(a);
        root.add(image, 0, 0);
        pane10.getChildren().add(close);
    }


   /*   pane01.getChildren().add(tvPerson);
        message.setTextFill(Color.DARKBLUE);
        message.relocate(15, 450);
        message.setFont(new Font(13));
        messageForMin.setTextFill(Color.DARKBLUE);
        messageForMin.relocate(15, 470);
        messageForMin.setFont(new Font(13));

        enter.relocate(440, 230);
        enter.setPrefSize(70, 20);
        enter.setAlignment(Pos.CENTER);
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
        remove.setOnAction(event -> {
                checking();
                indexField = new TextField();
                indexField.relocate(250, 230);
                indexField.setPromptText("Write number of element");
                pane00.getChildren().addAll(indexField, enter, closeInteractive);
                enter.setOnAction(event1 -> index(indexField));
                indexField.setOnAction(event1 -> index(indexField));
                closeInteractive.setOnAction(event1 -> pane00.getChildren().removeAll(indexField, enter, closeInteractive));
        });
        addIfMin.setOnAction(event -> {
                checking();
                add = new TextField();
                add.relocate(220, 230);
                add.setPrefColumnCount(16);
                add.setPromptText("Enter JSON-person");
                pane00.getChildren().addAll(add, enter, closeInteractive);
                enter.setOnAction(event1 -> add_if_min(add));
                add.setOnAction(event1 -> add_if_min(add));
                closeInteractive.setOnAction(event1 -> pane00.getChildren().removeAll(add, phrase, enter, closeInteractive));
        });
        update.setOnAction(event -> {
                checking();
                if (pane00.getChildren().contains(nameRB)) pane00.getChildren().remove(nameRB);
                if (pane00.getChildren().contains(phraseRB)) pane00.getChildren().remove(phraseRB);
                change = new TextField();
                change.relocate(250, 230);
                change.setPromptText("Enter number of person");
                pane00.getChildren().addAll(change, enter, closeInteractive);
                enter.setOnAction(event1 -> method2());
                change.setOnAction(event1 -> method2());
                closeInteractive.setOnAction(event1 -> {
                    pane00.getChildren().removeAll(change, enter, closeInteractive);
                    if (pane00.getChildren().contains(nameRB)) pane00.getChildren().remove(nameRB);
                    if (pane00.getChildren().contains(phraseRB)) pane00.getChildren().remove(phraseRB);
                });
        });

        close.setOnAction(event -> {
            pane10.getChildren().remove(close);
            root.getChildren().remove(image);
        });

        Button filterName = new Button("Sort on name");
        Button filterPhr = new Button("Sort on phrase");
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
        pane01.getChildren().addAll(filterName, filterPhr);
        filterName.setOnAction(event -> {
            for (int i = 0; i < vector.size()-1; i++) {
                for (int j = i + 1; j < vector.size(); j++) {
                    if (vector.get(i).compareTo(vector.get(j)) > 0) {
                        ch1 = vector.get(i);
                        vector.set(i, vector.get(j));
                        vector.set(j, ch1);
                    }
                }
            }
            for (Person p : vector) {
                System.out.println(p.getName() + ", ");
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

        Button exit = new Button("Exit");
        exit.setOnAction(event -> exit());
        load();
        getMin();
        rewriting();
        pane00.getChildren().addAll( message, messageForMin);
        pane11.getChildren().addAll(exit);

        stage.setOnCloseRequest(event -> {
            save();
            System.exit(0);
                }
        ); */


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
                pane00.getChildren().remove(change);
                change.setPromptText("Enter a phrase");
                pane00.getChildren().add(change);
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
                    pane00.getChildren().remove(change);
                    nameRB = new RadioButton("Name");
                    phraseRB = new RadioButton("Phrase");
                    ToggleGroup np = new ToggleGroup();
                    nameRB.setToggleGroup(np);
                    phraseRB.setToggleGroup(np);
                    nameRB.relocate(250, 235);
                    phraseRB.relocate(250, 270);
                    pane00.getChildren().addAll(nameRB, phraseRB);
                    nameRB.setOnAction(event -> {
                            pane00.getChildren().removeAll(nameRB, phraseRB);
                            change.setPromptText("Enter name");
                            pane00.getChildren().add(change);
                            enter.setOnAction(event1 -> method3(a));
                            change.setOnAction(event1 -> method3(a));
                        }
                    );
                    phraseRB.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            pane00.getChildren().removeAll(nameRB, phraseRB);
                            if(vector.get(a).getPhrases().isEmpty()) {
                                message.setText("There are not phrases");
                            }
                            else {
                                change.setPromptText("Enter number of phrase");
                                pane00.getChildren().add(change);
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
        if (pane00.getChildren().contains(indexField)) pane00.getChildren().remove(indexField);
        if (pane00.getChildren().contains(enter)) pane00.getChildren().remove(enter);
        if (pane00.getChildren().contains(closeInteractive)) pane00.getChildren().remove(closeInteractive);
        if (pane00.getChildren().contains(add)) pane00.getChildren().remove(add);
        if (pane00.getChildren().contains(change)) pane00.getChildren().remove(change);
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
     * This command finds the minimum from the Collection.
     */
    private static void getMin() {
        if (!vector.isEmpty()) {
            min = vector.get(0);
            for (Person person : vector) {
                if (min.compareTo(person) > 0) min = person;
            }
            messageForMin.setText("min = " + min);
        } else {
            message.setText("No elements in Collection");
            min = null;
            messageForMin.setText("min = null");
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
                messageForMin.setText("min = " + min.getName()); */
                if (min.compareTo(person) > 0) {
                    min = person;
                    vector.addElement(person);
                    rewriting();
                    element.setText("");
                    message.setText("Add_if_min is done :)");
                    messageForMin.setText("min = " + min.getName());
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
                    pane00.getChildren().remove(element);
                    message.setText("Add_if_min is done :)");
                    messageForMin.setText("min = " + min.getName());
                    phrase = new TextField();
                    phrase.relocate(220, 230);
                    phrase.setPrefColumnCount(16);
                    phrase.setPromptText("Enter phrase " + (i + 1));
                    pane00.getChildren().add(phrase);
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

    public static void main(String[] args) throws IOException{
        System.out.println(InetAddress.getLocalHost());
        Client client = new Client();
        client.loadFromServer();
        System.out.println(client.getVectorClient().size());
        client.getSocket().close();
   //     System.out.println(client.vectorClient.size());
   //     launch(args);
        System.exit(0);
    }
}

//C:\Users\Elizabeth\Desktop\ИТМО\Программирование\Лабораторные\Лаба05\Proba.csv