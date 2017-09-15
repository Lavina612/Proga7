import java.io.*;
import java.net.InetAddress;
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
import forGUI.*;

/**
 * @author Elisa, P3111
 * @version 1.0
 */

public class Main extends Application {
    private static Client client;
    private static Person minPerson = null;
    private static Root root = new Root();
    private static AnchorPane pane00 = new AnchorPane();
    private static AnchorPane pane01 = new AnchorPane();
    private static PaneDown pane10 = new PaneDown();
    private static PaneDown pane11 = new PaneDown();
    private static Message message = new Message("", 15, 420);
    private static Message messageForMin = new Message("min = " + minPerson, 15, 440);
    private ButtonAction updateButton = new ButtonAction("Обновить элемент", 15, 170);
    private ButtonAction removeLastButton = new ButtonAction("Удалить последний", 15, 210);
    private ButtonAction removeButton = new ButtonAction("Удалить (по индексу)", 15, 250);
    private ButtonAction addIfMinButton = new ButtonAction("Добавить, если меньше", 15, 290);
    private static TreeView<String> tvPerson = new TreeView<>();
    private static ButtonForUpdate nameRB;
    private static ButtonForUpdate phraseRB;
    private static InteractiveTextField interactiveTextField;
    private static FlowPane image = new FlowPane();
    private static ForButtonInteractive enter = new ForButtonInteractive("Ввод",460, 230);
    private static ForButtonInteractive closeInteractive = new ForButtonInteractive("Закрыть", 460, 270);
    private static Button closeImage = new Button("Закрыть");



    public void start(Stage stage) {
        stage.setTitle("Лабораторная 07");
        root.add(pane00, 0, 0);
        root.add(pane01, 1, 0);
        root.add(pane10, 0, 1);
        root.add(pane11, 1, 1);
        ButtonAction filterName = new ButtonAction("Отсортировать", 45, 420);
        MyImageView imv1 = new MyImageView(new Image("/images/БольИСтрадания.jpg"));
        MyImageView imv2 = new MyImageView(new Image("/images/Фичи.png"));
        MyImageView imv3 = new MyImageView(new Image("/images/МурМур.jpg"));
        MyImageView imv4 = new MyImageView(new Image("/images/СамыйЛучшийПреподавательНаСвете.jpg"));
        Label iWant = new Label("Я хочу увидеть...");
        iWant.setTextFill(Color.DARKVIOLET);
        ToggleGroup tg = new ToggleGroup();
        RBForImages rb1 = new RBForImages("сдачу лаб", tg, Color.DARKRED);
        RBForImages rb2 = new RBForImages("истину жизни", tg, Color.DARKGOLDENROD);
        RBForImages rb3 = new RBForImages("поздравление", tg, Color.DARKMAGENTA);
        RBForImages rb4 = new RBForImages("взгляд в будущее", tg, Color.DARKBLUE);

        updateButton.setOnAction(event -> {
            checking();
            if (pane00.getChildren().contains(nameRB)) pane00.getChildren().remove(nameRB);
            if (pane00.getChildren().contains(phraseRB)) pane00.getChildren().remove(phraseRB);
            interactiveTextField = new InteractiveTextField("Введите номер person");
            pane00.getChildren().addAll(interactiveTextField, enter, closeInteractive);
            enter.setOnAction(event1 -> updateAction());
            interactiveTextField.setOnAction(event1 -> updateAction());
            closeInteractive.setOnAction(event1 -> {
                pane00.getChildren().removeAll(interactiveTextField, enter, closeInteractive);
                if (pane00.getChildren().contains(nameRB)) pane00.getChildren().remove(nameRB);
                if (pane00.getChildren().contains(phraseRB)) pane00.getChildren().remove(phraseRB);
            });
        });
        removeLastButton.setOnAction(event -> {
            checking();
            if (!client.getVectorClient().isEmpty()) {
                client.connectionWithServer("Update server");
                client.sendDataVectorToServer(DataVector.DELETE, client.getVectorClient().lastElement());
                client.getVectorClient().remove(client.getVectorClient().lastElement());
                message.setText("Последний элемент удален");
                getMin();
                rewriting();
            } else message.setText("Коллекция пустая");
        });
        removeButton.setOnAction(event -> {
            checking();
            interactiveTextField = new InteractiveTextField("Введите номер person");
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
            message.setText("Коллекция отсортирована по имени");
        });

        rb1.setOnAction(event -> imagination(imv1, imv4, imv2, imv3));
        rb2.setOnAction(event -> imagination(imv2, imv1, imv4, imv3));
        rb3.setOnAction(event -> imagination(imv3, imv1, imv2, imv4));
        rb4.setOnAction(event -> imagination(imv4, imv1, imv2, imv3));
        pane00.getChildren().addAll(updateButton, removeLastButton, removeButton, addIfMinButton, message, messageForMin);
        pane01.getChildren().addAll(tvPerson, filterName);
        pane10.getChildren().addAll(iWant, rb1, rb2, rb3, rb4);
        pane11.getChildren().add(new Message(" У меня девиз один: \n Код из фич непобедим :)", 0,0));
        Scene scene = new Scene(root, 860, 550);
        stage.setScene(scene);
        stage.setMinWidth(874);
        stage.setMinHeight(589);
        stage.setMaxHeight(589);
        stage.setMaxWidth(874);
        stage.show();
    }


    /**
     * Метод находит минимальный элемент в коллекции
     */
    public static synchronized void getMin() {
        if (!client.getVectorClient().isEmpty()) {
            minPerson = client.getVectorClient().get(0);
            for (Person person : client.getVectorClient()) {
                if (minPerson.compareTo(person) > 0) minPerson = person;
            }
            messageForMin.setText("min = " + minPerson.toString());
        } else {
            message.setText("Коллекция пустая");
            minPerson = null;
            messageForMin.setText("min = null");
        }
    }

    /**
     * Метод переписывает дерево Person-ов для актуального отображения данных
     */
    public static synchronized void rewriting() {
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
                                interactiveTextField.setPromptText("Введите имя");
                                pane00.getChildren().add(interactiveTextField);
                                enter.setOnAction(event1 -> forNameRB(integerCopy));
                                interactiveTextField.setOnAction(event1 -> forNameRB(integerCopy));
                            }
                    );
                    phraseRB.setOnAction(event -> {
                                pane00.getChildren().removeAll(nameRB, phraseRB);
                                if (client.getVectorClient().get(integerCopy).getPhrases().isEmpty()) {
                                    message.setText("У этого элемента нет фраз");
                                } else {
                                    interactiveTextField.setPromptText("Введите номер фразы");
                                    pane00.getChildren().add(interactiveTextField);
                                    enter.setOnAction(event1 -> forPhraseRB(integerCopy)
                                    );
                                    interactiveTextField.setOnAction(event1 -> forPhraseRB(integerCopy)
                                    );
                                }
                            }
                    );
                } else {
                    message.setText("Номер не может быть не положительным");
                    interactiveTextField.setText("");
                }
            } else {
                message.setText("Слишком большой номер");
                interactiveTextField.setText("");
            }
        } catch (NumberFormatException e) {
            message.setText("Чёт бред какой-то. Цифру плиз");
            interactiveTextField.setText("");
        }
    }

    private static void forNameRB (int integerCopy) {
        if(interactiveTextField.getText().trim().isEmpty()) {
            message.setText("Имя не может быть пустым");
        } else {
            client.getVectorClient().get(integerCopy).setName(interactiveTextField.getText().trim());
            interactiveTextField.setText("");
            client.connectionWithServer("Update server");
            client.sendDataVectorToServer(DataVector.UPDATE, client.getVectorClient().get(integerCopy));
            getMin();
            rewriting();
            message.setText("Имя изменено");
        }
    }

    private static void forPhraseRB(int a) {
        try {
            Integer int2 = new Integer(interactiveTextField.getText());
            int b = --int2;
            interactiveTextField.setText("");
            if (int2 < 0)
                message.setText("Номер не может быть не положительным");
            else if (int2 >= client.getVectorClient().get(a).getPhrases().size())
                message.setText("Слишком большой номер");
            else {
                pane00.getChildren().remove(interactiveTextField);
                interactiveTextField.setPromptText("Введите фразу");
                pane00.getChildren().add(interactiveTextField);
                interactiveTextField.setOnAction(event2 -> forNewPhrase(a,b));
                enter.setOnAction(event2 -> forNewPhrase(a,b));
            }
        } catch (NumberFormatException e) {
            message.setText("Чёт вообще бред. Тут циферка должна быть");
            interactiveTextField.setText("");
        }
    }

    private static void forNewPhrase(int a, int b) {
        client.getVectorClient().get(a).setPhrase(b, new Phrase(interactiveTextField.getText().trim()));
        interactiveTextField.setText("");
        client.connectionWithServer("Update server");
        client.sendDataVectorToServer(DataVector.UPDATE, client.getVectorClient().get(a));
        getMin();
        rewriting();
        message.setText("Фраза обновлена");
    }

    /**
     * This command removes need element from the Collection.
     */
    private static void removeOnIndex(TextField indexField) {
        try {
            Integer index = new Integer(indexField.getText());
            if (client.getVectorClient().isEmpty()) {
                message.setText("Коллекция пустая");
            } else {
                if (index > client.getVectorClient().size()) {
                    message.setText("Слишком большой номер");
                } else {
                    if (index <= 0) {
                        message.setText("Номер не может быть не положительным");
                    } else {
                        client.connectionWithServer("Update server");
                        client.sendDataVectorToServer(DataVector.DELETE, client.getVectorClient().get(index-1));
                        client.getVectorClient().remove(index - 1);
                        getMin();
                        rewriting();
                        message.setText("Удаление прошло успешно");
                    }
                }
            }
            indexField.setText("");
        } catch (NumberFormatException e) {
            if (indexField.getText().equals("")) message.setText("Надо бы что-нибудь написать...");
            else message.setText("Мне кажется, стоит написать адекватную цифру");
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
                    person.setId(1);
                    client.connectionWithServer("Update server");
                    client.sendDataVectorToServer(DataVector.ADD, person);
                    element.setText("");
                    message.setText("Минимальный элемент добавлен");
                    minPerson = person;
                    messageForMin.setText("min = " + minPerson.getName());
                } else {
                    if (minPerson.compareTo(person) > 0) {
                        minPerson = person;
                        int j = -1;
                        for (int i=0; i<client.getVectorClient().size(); i++) {
                            if (client.getVectorClient().get(i).getId()>j) j = client.getVectorClient().get(i).getId();
                        }
                        person.setId(j+1);
                        client.getVectorClient().addElement(person);
                        rewriting();
                        client.connectionWithServer("Update server");
                        client.sendDataVectorToServer(DataVector.ADD, person);
                        element.setText("");
                        message.setText("Минимальный элемент добавлен");
                        messageForMin.setText("min = " + minPerson.getName());
                    } else {
                        message.setText("Этот элемент не меньше минимального");
                        element.setText("");
                    }
                }
            } else {
                message.setText("Имя не может быть пустым");
            }
        } catch (JsonSyntaxException e) {
            message.setText("Это не формат JSON");
        } catch (NullPointerException e) {
            message.setText("Sorry, null cannot be here");
            element.setText("");
        }
    }


    public static void main(String[] args) throws IOException{
        ThreadForClient tfc = null;
        try {
            System.out.println(InetAddress.getLocalHost());
            client = new Client();
            tfc = new ThreadForClient(client);
            tfc.start();
            tfc.setName("Thread For Client");
            message.setText("Коллекция обновлена по новым изменениям");
            launch(args);
            client.bool = true;
            client.connectionWithServer("esc");
            tfc.wakeup();
            System.exit(0);
        } catch (IOException e) {
            tfc.interrupt();
            System.out.println("Сервер отключился");
            System.exit(666);
        }
    }
}

//C:\Users\Elizabeth\Desktop\ИТМО\Программирование\Лабораторные\Лаба05\Proba.csv