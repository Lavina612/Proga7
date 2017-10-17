import java.io.*;
import java.net.InetAddress;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.time.zone.ZoneRules;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private ButtonAction updateButton = new ButtonAction(client.getResBun().getString("update"), 15, 155);
    private ButtonAction removeLastButton = new ButtonAction(client.getResBun().getString("remove_last"), 15, 195);
    private ButtonAction removeButton = new ButtonAction(client.getResBun().getString("remove"), 15, 235);
    private ButtonAction addIfMinButton = new ButtonAction(client.getResBun().getString("add_if_min"), 15, 275);
    private static TreeView<String> tvPerson = new TreeView<>();
    private static ButtonForUpdate nameRB;
    private static ButtonForUpdate phraseRB;
    private static InteractiveTextField interactiveTextField;
    private static FlowPane image = new FlowPane();
    private ForButtonInteractive enter = new ForButtonInteractive(client.getResBun().getString("enter"),460, 230);
    private ForButtonInteractive closeInteractive = new ForButtonInteractive(client.getResBun().getString("close"), 460, 270);
    private Button closeImage = new Button(client.getResBun().getString("close"));
    public static Message getMessage() { return message; }


    public void start(Stage stage) {
        stage.setTitle(client.getResBun().getString("title"));
        root.add(pane00, 0, 0);
        root.add(pane01, 1, 0);
        root.add(pane10, 0, 1);
        root.add(pane11, 1, 1);
        ButtonAction sortName = new ButtonAction(client.getResBun().getString("sort_name"), 45, 420);
        ButtonAction sortDate = new ButtonAction(client.getResBun().getString("sort_date"), 0, 0);
        MyImageView imv1 = new MyImageView(new Image("/images/deadline.jpg"));
        MyImageView imv2 = new MyImageView(new Image("/images/deadline2.jpg"));
        MyImageView imv3 = new MyImageView(new Image("/images/deadline3.jpg"));
        MyImageView imv4 = new MyImageView(new Image("/images/СамыйЛучшийПреподавательНаСвете.jpg"));
        Label iWant = new Label(client.getResBun().getString("i_want"));
        iWant.setTextFill(Color.DARKVIOLET);
        ToggleGroup tg = new ToggleGroup();
        RBForImages rb1 = new RBForImages(client.getResBun().getString("rb1"), tg, Color.DARKRED);
        RBForImages rb2 = new RBForImages(client.getResBun().getString("rb2"), tg, Color.DARKGOLDENROD);
        RBForImages rb3 = new RBForImages(client.getResBun().getString("rb3"), tg, Color.DARKMAGENTA);
        RBForImages rb4 = new RBForImages(client.getResBun().getString("rb4"), tg, Color.DARKBLUE);
        ButtonAction filter = new ButtonAction(client.getResBun().getString("filter"), 15, 315);
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("English", "Russian", "Slovenian", "Hungarian");
        comboBox.relocate(10, 10);
        comboBox.setPromptText(client.getResBun().getString("change_lan"));
        comboBox.valueProperty().addListener(event -> {
                    switch (comboBox.getValue().toString()) {
                        case "English":
                            System.out.println(comboBox.getValue());
                            client.setLocale(new Locale("en", "CA"));
                            client.setResBun(ResourceBundle.getBundle("texts/all_phrases", client.getLocale()));
                            client.setZoneId(ZoneId.of("Canada/Central"));
                            break;
                        case "Russian":
                            System.out.println(comboBox.getValue());
                            client.setLocale(new Locale("ru", "RU"));
                            client.setResBun(ResourceBundle.getBundle("texts/all_phrases", client.getLocale()));
                            client.setZoneId(ZoneId.of("Europe/Moscow"));
                            break;
                        case "Hungarian":
                            System.out.println(comboBox.getValue());
                            client.setLocale(new Locale("hu", "HU"));
                            client.setResBun(ResourceBundle.getBundle("texts/all_phrases", client.getLocale()));
                            client.setZoneId(ZoneId.of("Europe/Budapest"));
                            break;
                        case "Slovenian":
                            System.out.println(comboBox.getValue());
                            client.setLocale(new Locale("sl", "SI"));
                            client.setResBun(ResourceBundle.getBundle("texts/all_phrases", client.getLocale()));
                            client.setZoneId(ZoneId.of("Europe/Ljubljana"));
                            break;
                        default:
                            System.out.println("BAD!!!");
                    }

                    String temp = client.getResBun().getString("min");
                    String template = MessageFormat.format(temp, minPerson);
                    messageForMin.setText(template);
                    updateButton.setText(client.getResBun().getString("update"));
                    removeLastButton.setText(client.getResBun().getString("remove_last"));
                    removeButton.setText(client.getResBun().getString("remove"));
                    addIfMinButton.setText(client.getResBun().getString("add_if_min"));
                    enter.setText(client.getResBun().getString("enter"));
                    closeInteractive.setText(client.getResBun().getString("close"));
                    closeImage.setText(client.getResBun().getString("close"));
                    stage.setTitle(client.getResBun().getString("title"));
                    sortName.setText(client.getResBun().getString("sort_name"));
                    sortDate.setText(client.getResBun().getString("sort_date"));
                    filter.setText(client.getResBun().getString("filter"));
                    iWant.setText(client.getResBun().getString("i_want"));
                    rb1.setText(client.getResBun().getString("rb1"));
                    rb2.setText(client.getResBun().getString("rb2"));
                    rb3.setText(client.getResBun().getString("rb3"));
                    rb4.setText(client.getResBun().getString("rb4"));
                    comboBox.setPromptText(client.getResBun().getString("change_lan"));
                    rewriting();
        });

        updateButton.setOnAction(event -> {
            checking(enter, closeInteractive);
            if (pane00.getChildren().contains(nameRB)) pane00.getChildren().remove(nameRB);
            if (pane00.getChildren().contains(phraseRB)) pane00.getChildren().remove(phraseRB);
            interactiveTextField = new InteractiveTextField(client.getResBun().getString("enter_num_p"));
            pane00.getChildren().addAll(interactiveTextField, enter, closeInteractive);
            enter.setOnAction(event1 -> updateAction(enter));
            interactiveTextField.setOnAction(event1 -> updateAction(enter));
            closeInteractive.setOnAction(event1 -> {
                pane00.getChildren().removeAll(interactiveTextField, enter, closeInteractive);
                if (pane00.getChildren().contains(nameRB)) pane00.getChildren().remove(nameRB);
                if (pane00.getChildren().contains(phraseRB)) pane00.getChildren().remove(phraseRB);
            });
        });
        removeLastButton.setOnAction(event -> {
            checking(enter, closeInteractive);
            if (!client.getVectorClient().isEmpty()) {
                client.connectionWithServer("Update server");
                client.sendDataVectorToServer(DataVector.DELETE, client.getVectorClient().lastElement());
                client.getVectorClient().remove(client.getVectorClient().lastElement());
                message.setText(client.getResBun().getString("rl_done"));
                getMin();
                rewriting();
            } else message.setText(client.getResBun().getString("col_empty"));
        });
        removeButton.setOnAction(event -> {
            checking(enter, closeInteractive);
            interactiveTextField = new InteractiveTextField(client.getResBun().getString("enter_num_p"));
            pane00.getChildren().addAll(interactiveTextField, enter, closeInteractive);
            enter.setOnAction(event1 -> removeOnIndex(interactiveTextField));
            interactiveTextField.setOnAction(event1 -> removeOnIndex(interactiveTextField));
            closeInteractive.setOnAction(event1 -> pane00.getChildren().removeAll(interactiveTextField, enter, closeInteractive));
        });
        addIfMinButton.setOnAction(event -> {
            checking(enter, closeInteractive);
            interactiveTextField = new InteractiveTextField("{name:\"name\";phrases:[{phrase:\"phrase\"};{...}]}");
            interactiveTextField.relocate(190, 230);
            interactiveTextField.setPrefColumnCount(22);
            pane00.getChildren().addAll(interactiveTextField, enter, closeInteractive);
            enter.setOnAction(event1 -> addIfMin(interactiveTextField));
            interactiveTextField.setOnAction(event1 -> addIfMin(interactiveTextField));
            closeInteractive.setOnAction(event1 -> pane00.getChildren().removeAll(interactiveTextField, enter, closeInteractive));
        });
        sortName.setOnAction(event -> {
            List<Person> list = client.getVectorClient().stream().sorted().collect(Collectors.toList());
            Vector<Person> vector = new Vector<>();
            for (int i=0; i<list.size(); i++) {
                vector.add(list.get(i));
            }
            client.setVectorClient(vector);
            rewriting();
            message.setText(client.getResBun().getString("col_sort_n"));
        });
        sortDate.setOnAction(event -> {
            List<Person> list = client.getVectorClient().stream().sorted(new Person()).collect(Collectors.toList());
            Vector<Person> vector = new Vector<>();
            for (int i=0; i<list.size(); i++) {
                vector.add(list.get(i));
            }
            client.setVectorClient(vector);
            rewriting();
            message.setText(client.getResBun().getString("col_sort_d"));
        });
        filter.setOnAction(event -> {
            Vector<Person> vct = client.getVectorClient();
            checking(enter, closeInteractive);
            interactiveTextField = new InteractiveTextField("Filter");
            pane00.getChildren().addAll(interactiveTextField, enter, closeInteractive);
            enter.setOnAction(event1 -> {
                beforeFilter(updateButton, removeLastButton, removeButton, addIfMinButton, true);
                filter();
            });
            interactiveTextField.setOnAction(event1 -> {
                beforeFilter(updateButton, removeLastButton, removeButton, addIfMinButton, true);
                filter();
            });
            closeInteractive.setOnAction(event1 -> {
                beforeFilter(updateButton, removeLastButton, removeButton, addIfMinButton, false);
                pane00.getChildren().removeAll(interactiveTextField, enter, closeInteractive);
                client.setVectorClient(vct);
                getMin();
                rewriting();
            });
        });

        rb1.setOnAction(event -> imagination(imv1, imv4, imv2, imv3, closeImage));
        rb2.setOnAction(event -> imagination(imv2, imv1, imv4, imv3, closeImage));
        rb3.setOnAction(event -> imagination(imv3, imv1, imv2, imv4, closeImage));
        rb4.setOnAction(event -> imagination(imv4, imv1, imv2, imv3, closeImage));
        pane00.getChildren().addAll(comboBox, updateButton, removeLastButton, removeButton, addIfMinButton, filter, message, messageForMin);
        pane01.getChildren().addAll(tvPerson, sortName);
        pane10.getChildren().addAll(iWant, rb1, rb2, rb3, rb4);
        pane11.getChildren().addAll(sortDate);
        Scene scene = new Scene(root, 860, 550);
        stage.setScene(scene);
        stage.setMinWidth(874);
        stage.setMinHeight(589);
        stage.setMaxHeight(589);
        stage.setMaxWidth(874);
        stage.show();
    }

    public static void beforeFilter(ButtonAction b1, ButtonAction b2, ButtonAction b3, ButtonAction b4, boolean b) {
        b1.setDisable(b);
        b2.setDisable(b);
        b3.setDisable(b);
        b4.setDisable(b);
    }

    public static void filter() {
        List<Person> list = client.getVectorClient().stream().filter((s) -> s.getName().contains(interactiveTextField.getText())).collect(Collectors.toList());
        Vector<Person> vector = new Vector<>();
        for (int i=0; i<list.size(); i++) {
            vector.add(list.get(i));
        }
        client.setVectorClient(vector);
        rewriting();
        message.setText(client.getResBun().getString("col_filt"));
    }

    public static synchronized void getMin() {
        if (!client.getVectorClient().isEmpty()) {
            minPerson = client.getVectorClient().get(0);
            for (Person person : client.getVectorClient()) {
                if (minPerson.compareTo(person) > 0) minPerson = person;
            }
            String temp = client.getResBun().getString("min");
            String template = MessageFormat.format(temp, minPerson.toString());
            messageForMin.setText(template);
        } else {
            message.setText(client.getResBun().getString("col_empty"));
            minPerson = null;
            String temp = client.getResBun().getString("min");
            String template = MessageFormat.format(temp, "null");
            messageForMin.setText(template);
        }
    }

    /**
     * Метод переписывает дерево Person-ов для актуального отображения данных
     */
    public static synchronized void rewriting() {
        pane01.getChildren().remove(tvPerson);
        TreeItem<String> tiRoot = new TreeItem<>(client.getResBun().getString("persons"));
        for (int i = 0; i < client.getVectorClient().size(); i++) {
            String temp = client.getResBun().getString("person");
            String template = MessageFormat.format(temp, (i+1));
            TreeItem<String> tiPerson = new TreeItem<>(template);
            tiRoot.getChildren().add(tiPerson);
            TreeItem<String> tiName = new TreeItem<>(client.getResBun().getString("name"));
            tiPerson.getChildren().add(tiName);
            tiName.getChildren().add(new TreeItem<>(client.getVectorClient().get(i).getName()));
            TreeItem<String> tiTime = new TreeItem<>(client.getResBun().getString("date_time"));
            tiPerson.getChildren().add(tiTime);
            tiTime.getChildren().add(new TreeItem<>(client.getVectorClient().get(i).getTime(client.getLocale())));
            if (!client.getVectorClient().get(i).getPhrases().isEmpty()) {
                TreeItem<String> tiPhrases = new TreeItem<>(client.getResBun().getString("phrases"));
                tiPerson.getChildren().add(tiPhrases);
                for (int j = 0; j < client.getVectorClient().get(i).getPhrases().size(); j++) {
                    TreeItem<String> tiPhrase = new TreeItem<>(client.getResBun().getString("phr") + (j+1));
                    tiPhrases.getChildren().add(tiPhrase);
                    if (client.getVectorClient().get(i).getPhrases().get(j).getPhrase().isEmpty()) {
                        tiPhrase.getChildren().add(new TreeItem<>(client.getResBun().getString("silent")));
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
    private static void checking(ForButtonInteractive enter, ForButtonInteractive closeInteractive) {
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
    private static void imagination(ImageView a, ImageView b, ImageView c, ImageView d, Button closeImage) {
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
    private static void updateAction(ForButtonInteractive enter) {
        try {
            Integer integer = new Integer(interactiveTextField.getText());
            integer--;
            if (integer < client.getVectorClient().size()) {
                if (integer >= 0) {
                    int integerCopy = integer;
                    interactiveTextField.setText("");
                    ToggleGroup np = new ToggleGroup();
                    nameRB = new ButtonForUpdate(client.getResBun().getString("name"), 250, 235, np);
                    phraseRB = new ButtonForUpdate(client.getResBun().getString("phr"), 250, 270, np);
                    pane00.getChildren().remove(interactiveTextField);
                    pane00.getChildren().addAll(nameRB, phraseRB);
                    nameRB.setOnAction(event -> {
                                pane00.getChildren().removeAll(nameRB, phraseRB);
                                interactiveTextField.setPromptText(client.getResBun().getString("enter_name"));
                                pane00.getChildren().add(interactiveTextField);
                                enter.setOnAction(event1 -> forNameRB(integerCopy));
                                interactiveTextField.setOnAction(event1 -> forNameRB(integerCopy));
                            }
                    );
                    phraseRB.setOnAction(event -> {
                                pane00.getChildren().removeAll(nameRB, phraseRB);
                                if (client.getVectorClient().get(integerCopy).getPhrases().isEmpty()) {
                                    message.setText(client.getResBun().getString("no_phr"));
                                } else {
                                    interactiveTextField.setPromptText(client.getResBun().getString("enter_num_phr"));
                                    pane00.getChildren().add(interactiveTextField);
                                    enter.setOnAction(event1 -> forPhraseRB(integerCopy, enter)
                                    );
                                    interactiveTextField.setOnAction(event1 -> forPhraseRB(integerCopy, enter)
                                    );
                                }
                            }
                    );
                } else {
                    message.setText(client.getResBun().getString("num_pos"));
                    interactiveTextField.setText("");
                }
            } else {
                message.setText(client.getResBun().getString("num_big"));
                interactiveTextField.setText("");
            }
        } catch (NumberFormatException e) {
            message.setText(client.getResBun().getString("nonsense"));
            interactiveTextField.setText("");
        }
    }

    private static void forNameRB (int integerCopy) {
        if(interactiveTextField.getText().trim().isEmpty()) {
            message.setText(client.getResBun().getString("nam_emp"));
        } else {
            client.getVectorClient().get(integerCopy).setName(interactiveTextField.getText().trim());
            interactiveTextField.setText("");
            client.connectionWithServer("Update server");
            client.sendDataVectorToServer(DataVector.UPDATE, client.getVectorClient().get(integerCopy));
            getMin();
            rewriting();
            message.setText(client.getResBun().getString("nam_update"));
        }
    }

    private static void forPhraseRB(int a, ForButtonInteractive enter) {
        try {
            Integer int2 = new Integer(interactiveTextField.getText());
            int b = --int2;
            interactiveTextField.setText("");
            if (int2 < 0)
                message.setText(client.getResBun().getString("num_pos"));
            else if (int2 >= client.getVectorClient().get(a).getPhrases().size())
                message.setText(client.getResBun().getString("num_big"));
            else {
                pane00.getChildren().remove(interactiveTextField);
                interactiveTextField.setPromptText(client.getResBun().getString("enter_phr"));
                pane00.getChildren().add(interactiveTextField);
                interactiveTextField.setOnAction(event2 -> forNewPhrase(a,b));
                enter.setOnAction(event2 -> forNewPhrase(a,b));
            }
        } catch (NumberFormatException e) {
            message.setText(client.getResBun().getString("nonsense"));
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
        message.setText(client.getResBun().getString("phr_update"));
    }

    /**
     * This command removes need element from the Collection.
     */
    private static void removeOnIndex(TextField indexField) {
        try {
            Integer index = new Integer(indexField.getText());
            if (client.getVectorClient().isEmpty()) {
                message.setText(client.getResBun().getString("col_empty"));
            } else {
                if (index > client.getVectorClient().size()) {
                    message.setText(client.getResBun().getString("num_big"));
                } else {
                    if (index <= 0) {
                        message.setText(client.getResBun().getString("num_pos"));
                    } else {
                        client.connectionWithServer("Update server");
                        client.sendDataVectorToServer(DataVector.DELETE, client.getVectorClient().get(index-1));
                        client.getVectorClient().remove(index - 1);
                        getMin();
                        rewriting();
                        message.setText(client.getResBun().getString("num_success"));
                    }
                }
            }
            indexField.setText("");
        } catch (NumberFormatException e) {
            if (indexField.getText().equals("")) message.setText(client.getResBun().getString("write_anyth"));
            else message.setText(client.getResBun().getString("nonsense"));
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
                    person.setId_per(1);
                    client.connectionWithServer("Update server");
                    client.sendDataVectorToServer(DataVector.ADD, person);
                    element.setText("");
                    message.setText(client.getResBun().getString("min_add"));
                    minPerson = person;
                    String temp = client.getResBun().getString("min");
                    String template = MessageFormat.format(temp, minPerson);
                    messageForMin.setText(template);
                } else {
                    if (minPerson.compareTo(person) > 0) {
                        minPerson = person;
                        int j = -1;
                        for (int i=0; i<client.getVectorClient().size(); i++) {
                            if (client.getVectorClient().get(i).getId_per()>j) j = client.getVectorClient().get(i).getId_per();
                        }
                        person.setId_per(j+1);
                        client.getVectorClient().addElement(person);
                        rewriting();
                        client.connectionWithServer("Update server");
                        client.sendDataVectorToServer(DataVector.ADD, person);
                        element.setText("");
                        message.setText(client.getResBun().getString("min_add"));
                        messageForMin.setText("min = " + minPerson.toString());
                    } else {
                        message.setText(client.getResBun().getString("min_not_min"));
                        element.setText("");
                    }
                }
            } else {
                message.setText(client.getResBun().getString("nam_emp"));
            }
        } catch (JsonSyntaxException e) {
            message.setText(client.getResBun().getString("not_json"));
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
            launch(args);
            client.bool = true;
            client.connectionWithServer("esc");
            tfc.wakeup();
            System.exit(0);
        } catch (IOException e) {
            tfc.interrupt();
            System.out.println(client.getResBun().getString("serv_not_work"));
            System.exit(666);
        }
    }
}

//C:\Users\Elizabeth\Desktop\ИТМО\Программирование\Лабораторные\Лаба05\Proba.csv