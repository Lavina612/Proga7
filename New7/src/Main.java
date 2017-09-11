import java.io.*;
import java.net.InetAddress;
import java.util.Vector;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.application.Application;
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
    private static int count;
    private static Client client;
//    private static Vector<Person> vectorOld;
    private static Person minPerson = null;
    private static Root root = new Root();
    private static AnchorPane pane00 = new AnchorPane();
    private static AnchorPane pane01 = new AnchorPane();
    private static PaneDown pane10 = new PaneDown();
    private static PaneDown pane11 = new PaneDown();
    private static Message message = new Message("", 15, 450);
    private static Message messageForMin = new Message("min = " + minPerson, 15, 470);
    private ButtonAction loadButton = new ButtonAction("Перечитать из файла", 15, 130);
    private ButtonAction updateButton = new ButtonAction("Обновить", 15, 170);
    private ButtonAction saveButton = new ButtonAction("Сохранить в файл", 15, 210);
    private ButtonAction removeLastButton = new ButtonAction("Удалить последний элемент", 15, 250);
    private ButtonAction removeButton = new ButtonAction("Удалить (по индексу)", 15, 290);
    private ButtonAction addIfMinButton = new ButtonAction("Добавить, если меньше", 15, 330);
    private static TreeView<String> tvPerson = new TreeView<>();
    private static ButtonForUpdate nameRB;
    private static ButtonForUpdate phraseRB;
    private static InteractiveTextField interactiveTextField;
    private static FlowPane image = new FlowPane();
    private static AnchorPane exit = new AnchorPane();
    private static ForButtonInteractive enter = new ForButtonInteractive("Enter",460, 230);
    private static ForButtonInteractive closeInteractive = new ForButtonInteractive("Close", 460, 270);

    private static Button closeImage = new Button("Close");



    public void start(Stage stage) {
        stage.setTitle("Laba07");
        root.add(pane00, 0, 0);
        root.add(pane01, 1, 0);
        root.add(pane10, 0, 1);
        root.add(pane11, 1, 1);
        ButtonAction filterName = new ButtonAction("Sort on name", 45, 430);
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
        RBForImages rb4 = new RBForImages("взгляд в будущее", tg, Color.DARKBLUE);


        loadButton.setOnAction(event -> {
            checking();
            client.connectionWithServer("Load");
            client.loadVectorFromServer();
            try {
                count = client.getDataInput().readInt();
            } catch (IOException e) {
                System.out.println("Не удалось принять count");
            }
            if (count != -1) {
                Vector <Person> vector = client.getVectorClient();
                vector.removeAllElements();
                client.setVectorClient(vector);
                message.setText("Person" + count + " must have a name. Correct it and try again");
            } else message.setText("Коллекция успешно считана из файла");
            getMin();
            rewriting();
        });
        updateButton.setOnAction(event -> {
            checking();
            if (pane00.getChildren().contains(nameRB)) pane00.getChildren().remove(nameRB);
            if (pane00.getChildren().contains(phraseRB)) pane00.getChildren().remove(phraseRB);
            interactiveTextField = new InteractiveTextField("Enter number of person");
            pane00.getChildren().addAll(interactiveTextField, enter, closeInteractive);
            enter.setOnAction(event1 -> updateAction());
            interactiveTextField.setOnAction(event1 -> updateAction());
            closeInteractive.setOnAction(event1 -> {
                pane00.getChildren().removeAll(interactiveTextField, enter, closeInteractive);
                if (pane00.getChildren().contains(nameRB)) pane00.getChildren().remove(nameRB);
                if (pane00.getChildren().contains(phraseRB)) pane00.getChildren().remove(phraseRB);
            });
        });
        saveButton.setOnAction(event -> {
            checking();
            client.connectionWithServer("Save");
            client.sendVectorToServer();
            message.setText("Save is done :)");
        });
        removeLastButton.setOnAction(event -> {
            checking();
            if (!client.getVectorClient().isEmpty()) {
                client.getVectorClient().remove(client.getVectorClient().lastElement());
                message.setText("Remove last is done :)");
                getMin();
                rewriting();
                client.connectionWithServer("Remove last");
                client.sendVectorToServer();
            } else message.setText("Vector is empty");
        });
        removeButton.setOnAction(event -> {
            checking();
            interactiveTextField = new InteractiveTextField("Write number of element");
            pane00.getChildren().addAll(interactiveTextField, enter, closeInteractive);
            enter.setOnAction(event1 -> removeOnIndex(interactiveTextField));
            interactiveTextField.setOnAction(event1 -> removeOnIndex(interactiveTextField));
            closeInteractive.setOnAction(event1 -> pane00.getChildren().removeAll(interactiveTextField, enter, closeInteractive));
        });
        addIfMinButton.setOnAction(event -> {
            checking();
            interactiveTextField = new InteractiveTextField("{name:\"name\";phrases:[{phrase:\"phrase\"};{...}]}");
            interactiveTextField.relocate(190, 230);
            interactiveTextField.setPrefColumnCount(22);
            pane00.getChildren().addAll(interactiveTextField, enter, closeInteractive);
            enter.setOnAction(event1 -> addIfMin(interactiveTextField));
            interactiveTextField.setOnAction(event1 -> addIfMin(interactiveTextField));
            closeInteractive.setOnAction(event1 -> pane00.getChildren().removeAll(interactiveTextField, enter, closeInteractive));
        });
        filterName.setOnAction(event -> {
            for (int i = 0; i < client.getVectorClient().size()-1; i++) {
                for (int j = i + 1; j < client.getVectorClient().size(); j++) {
                    if (client.getVectorClient().get(i).compareTo(client.getVectorClient().get(j)) > 0) {
                        Person ch1 = client.getVectorClient().get(i);
                        client.getVectorClient().set(i, client.getVectorClient().get(j));
                        client.getVectorClient().set(j, ch1);
                    }
                }
            }
            rewriting();
            message.setText("Collection sorted on names");
        });
        stage.setOnCloseRequest(event -> {
          //  if (!vectorOld.equals(client.getVectorClient())) {
                exit.relocate(375, 250);
                exit.setStyle("-fx-background-color: lightblue");
                exit.setPrefWidth(100);
                exit.setPrefHeight(50);
                root.add(exit, 0, 0);
                Message exitQuestion = new Message("Хотите сохранить изменения?", 20, 10);
                ButtonExit yes = new ButtonExit("Yes", 10, 35);
                ButtonExit no = new ButtonExit("No", 40, 35);
                ButtonExit cancel = new ButtonExit("Cancel", 70, 35);
                yes.setStyle("-fx-background-color: aqua");
                exit.getChildren().addAll(exitQuestion, yes, no, cancel);
                yes.setOnAction(event1 -> {
                    client.connectionWithServer("Save");
                    client.sendVectorToServer();
                });
                no.setOnAction(event2 -> {
                    System.exit(0);
                });
                cancel.setOnAction(event3 -> {
                    pane00.getChildren().remove(exit);
                });
         //   }
        });

        rb1.setOnAction(event -> imagination(imv1, imv4, imv2, imv3));
        rb2.setOnAction(event -> imagination(imv2, imv1, imv4, imv3));
        rb3.setOnAction(event -> imagination(imv3, imv1, imv2, imv4));
        rb4.setOnAction(event -> imagination(imv4, imv1, imv2, imv3));
        pane00.getChildren().addAll(loadButton, updateButton, saveButton, removeLastButton, removeButton, addIfMinButton, message, messageForMin);
        pane01.getChildren().add(filterName);
        pane10.getChildren().addAll(iWant, rb1, rb2, rb3, rb4);
        getMin();
        rewriting();
        Scene scene = new Scene(root, 850, 550);
        stage.setScene(scene);
        stage.show();
    }




    /**
     * Метод находит минимальный элемент в коллекции
     */
    private static void getMin() {
        if (!client.getVectorClient().isEmpty()) {
            minPerson = client.getVectorClient().get(0);
            for (Person person : client.getVectorClient()) {
                if (minPerson.compareTo(person) > 0) minPerson = person;
            }
            messageForMin.setText("min = " + minPerson);
        } else {
            if (!message.getText().substring(0,6).equals("Person"))
            message.setText("No elements in Collection");
            minPerson = null;
            messageForMin.setText("min = null");
        }
    }

    /**
     * Метод переписывает дерево Person-ов для актуального отображения данных
     */
    private static void rewriting() {
        pane01.getChildren().remove(tvPerson);
        TreeItem<String> tiRoot = new TreeItem<>("Persons");
        for (int i = 0; i < client.getVectorClient().size(); i++) {
            TreeItem<String> tiPerson = new TreeItem<>("Person " + (i + 1));
            tiRoot.getChildren().add(tiPerson);
            TreeItem<String> tiName = new TreeItem<>("Name");
            tiPerson.getChildren().add(tiName);
            tiName.getChildren().add(new TreeItem<>(client.getVectorClient().get(i).getName()));
            if (!client.getVectorClient().get(i).getPhrases().isEmpty()) {
                TreeItem<String> tiPhrases = new TreeItem<>("Phrases");
                tiPerson.getChildren().add(tiPhrases);
                for (int j = 0; j < client.getVectorClient().get(i).getPhrases().size(); j++) {
                    TreeItem<String> tiPhrase = new TreeItem<>("Phrase " + (j + 1));
                    tiPhrases.getChildren().add(tiPhrase);
                    if (client.getVectorClient().get(i).getPhrases().get(j).getPhrase().isEmpty()) {
                        tiPhrase.getChildren().add(new TreeItem<>("I was silent!"));
                    } else
                        tiPhrase.getChildren().add(new TreeItem<>(client.getVectorClient().get(i).getPhrases().get(j).getPhrase()));
                }
            }
        }
        tvPerson = new TreeView<>(tiRoot);
        tvPerson.relocate(0, 0);
        pane01.getChildren().add(tvPerson);
    }

    /**
     * This command does checking.
     */
    private static void checking() {
        if (pane00.getChildren().contains(interactiveTextField)) pane00.getChildren().remove(interactiveTextField);
        if (pane00.getChildren().contains(enter)) pane00.getChildren().remove(enter);
        if (pane00.getChildren().contains(closeInteractive)) pane00.getChildren().remove(closeInteractive);
        if (pane00.getChildren().contains(nameRB)) pane00.getChildren().remove(nameRB);
        if (pane00.getChildren().contains(phraseRB)) pane00.getChildren().remove(phraseRB);
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
        pane10.getChildren().remove(closeImage);
        image.setAlignment(Pos.CENTER);
        image.getChildren().add(a);
        root.add(image, 0, 0);
        pane10.getChildren().add(closeImage);
        closeImage.setOnAction(event -> {
            root.getChildren().remove(image);
            pane10.getChildren().remove(closeImage);
        });
    }

    private static void load() {
        if (!client.getVectorClient().isEmpty()) {
            int count = -1;
            for (int i = 0; i<client.getVectorClient().size(); i++) {
                if ("".equals(client.getVectorClient().get(i).getName())) count = i;
                System.out.println(i);
            }
            if(count == -1) {
                message.setText("Loading is done :)");
                getMin();
                rewriting();
            } else {
                message.setText("Person" + count + " must have a name. Correct this and try again");
                Vector <Person> vector = client.getVectorClient();
                vector.removeAllElements();
                client.setVectorClient(vector);
            }
        } else message.setText("Коллекция пустая");
    }

    /**
     * Метод предназначен для обновления какого-либо элемента коллекции, использую кнопку updateButton
     * Последующие три метода используются для недопущения повторного кода
     */
    private static void updateAction() {
        try {
            Integer integer = new Integer(interactiveTextField.getText());
            integer--;
            if (integer < client.getVectorClient().size()) {
                if (integer >= 0) {
                    int integerCopy = integer;
                    interactiveTextField.setText("");
                    pane00.getChildren().remove(interactiveTextField);
                    ToggleGroup np = new ToggleGroup();
                    nameRB = new ButtonForUpdate("Name", 250, 235, np);
                    phraseRB = new ButtonForUpdate("Phrase", 250, 270, np);
                    pane00.getChildren().addAll(nameRB, phraseRB);
                    nameRB.setOnAction(event -> {
                                pane00.getChildren().removeAll(nameRB, phraseRB);
                                interactiveTextField.setPromptText("Enter name");
                                pane00.getChildren().add(interactiveTextField);
                                enter.setOnAction(event1 -> forNameRB(integerCopy));
                                interactiveTextField.setOnAction(event1 -> forNameRB(integerCopy));
                            }
                    );
                    phraseRB.setOnAction(event -> {
                                pane00.getChildren().removeAll(nameRB, phraseRB);
                                if (client.getVectorClient().get(integerCopy).getPhrases().isEmpty()) {
                                    message.setText("There are not phrases");
                                } else {
                                    interactiveTextField.setPromptText("Enter number of phrase");
                                    pane00.getChildren().add(interactiveTextField);
                                    enter.setOnAction(event1 -> forPhraseRB(integerCopy)
                                    );
                                    interactiveTextField.setOnAction(event1 -> forPhraseRB(integerCopy)
                                    );
                                }
                            }
                    );
                } else {
                    message.setText("Number can't be negative");
                    interactiveTextField.setText("");
                }
            } else {
                message.setText("The number more than elements in Collection");
                interactiveTextField.setText("");
            }
        } catch (NumberFormatException e) {
            message.setText("Sorry, but you wrote a nonsense");
            interactiveTextField.setText("");
        }
    }

    private static void forNameRB (int integerCopy) {
        if(interactiveTextField.getText().trim().isEmpty()) {
            message.setText("Name can't be empty");
        } else {
            client.getVectorClient().get(integerCopy).setName(interactiveTextField.getText().trim());
            interactiveTextField.setText("");
            client.connectionWithServer("Update");
            client.sendVectorToServer();
            getMin();
            rewriting();
            message.setText("Name is updated");
        }
    }

    private static void forPhraseRB(int a) {
        try {
            Integer int2 = new Integer(interactiveTextField.getText());
            int b = --int2;
            interactiveTextField.setText("");
            if (int2 < 0)
                message.setText("The number can't be negative");
            else if (int2 >= client.getVectorClient().get(a).getPhrases().size())
                message.setText("The number more than elements in Collection");
            else {
                pane00.getChildren().remove(interactiveTextField);
                interactiveTextField.setPromptText("Enter a phrase");
                pane00.getChildren().add(interactiveTextField);
                interactiveTextField.setOnAction(event2 -> forNewPhrase(a,b));
                enter.setOnAction(event2 -> forNewPhrase(a,b));
            }
        } catch (NumberFormatException e) {
            message.setText("Sorry, but you wrote a nonsense");
            interactiveTextField.setText("");
        }
    }

    private static void forNewPhrase(int a, int b) {
        client.getVectorClient().get(a).setPhrase(b, new Phrase(interactiveTextField.getText().trim()));
        interactiveTextField.setText("");
        client.connectionWithServer("Update");
        client.sendVectorToServer();
        getMin();
        rewriting();
        message.setText("Phrase is updated");
    }

    /**
     * This command removes need element from the Collection.
     */
    private static void removeOnIndex(TextField indexField) {
        try {
            Integer index = new Integer(indexField.getText());
            if (client.getVectorClient().isEmpty()) {
                message.setText("Vector is empty");
            } else {
                if (index > client.getVectorClient().size()) {
                    message.setText("The number more than elements in Collection");
                } else {
                    if (index <= 0) {
                        message.setText("The number can't be negative");
                    } else {
                        client.getVectorClient().remove(index - 1);
                        getMin();
                        rewriting();
                        client.connectionWithServer("Remove");
                        client.sendVectorToServer();
                        message.setText("Remove is done :)");
                    }
                }
            }
            indexField.setText("");
        } catch (NumberFormatException e) {
            if (indexField.getText().equals("")) message.setText("You didn't write anything");
            else message.setText("Sorry, but you wrote a nonsense. Try again!");
            indexField.setText("");
        }
    }

    /**
     * This command adds the element into Collection if this element less than min element of this Collection.
     */
    private static void addIfMin(TextField element) {
        try {
            Gson gson = new Gson();
            Person person = gson.fromJson(element.getText(), Person.class);
            if (!person.getName().trim().equals("")) {
                if (minPerson == null) {
                    client.getVectorClient().addElement(person);
                    rewriting();
                    client.connectionWithServer("Add if min");
                    client.sendVectorToServer();
                    element.setText("");
                    message.setText("Add if min is done :)");
                    minPerson = person;
                    messageForMin.setText("min = " + minPerson.getName());
                } else {
                    if (minPerson.compareTo(person) > 0) {
                        minPerson = person;
                        client.getVectorClient().addElement(person);
                        rewriting();
                        client.connectionWithServer("Add if min");
                        client.sendVectorToServer();
                        element.setText("");
                        message.setText("Add_if_min is done :)");
                        messageForMin.setText("min = " + minPerson.getName());
                    } else {
                        message.setText("Element is more than min");
                        element.setText("");
                    }
                }
            } else {
                message.setText("Sorry, but person must have a name");
            }
        } catch (JsonSyntaxException e) {
            message.setText("Sorry, it's not JSON");
        } catch (NullPointerException e) {
            message.setText("Sorry, null cannot be here");
            element.setText("");
        }
    }


    public static void main(String[] args) throws IOException{
        System.out.println(InetAddress.getLocalHost());
        client = new Client();
        client.loadVectorFromServer();
        count = client.getDataInput().readInt();
        if (count != -1) {
            Vector <Person> vector = client.getVectorClient();
            vector.removeAllElements();
            client.setVectorClient(vector);
            System.out.println("Person" + count + " must have a name. Correct it and try again");
            client.connectionWithServer("esc");
            System.exit(1);
        } else message.setText("Коллекция успешно загружена");
   //     vectorOld = client.getVectorClient();
        System.out.println(client.getVectorClient().size());
        launch(args);
        client.connectionWithServer("esc");
        client.getDataOutput().close();
        client.getSocket().close();
        System.exit(0);
    }
}

//C:\Users\Elizabeth\Desktop\ИТМО\Программирование\Лабораторные\Лаба05\Proba.csv