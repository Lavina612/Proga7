import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server {
    private static Vector<Person> vectorServer = new Vector<>();
    private static ServerSocket server;
    private static Socket socket;
    private ObjectOutputStream obOutput;
    private Server() {
        try {
            server = new ServerSocket(6666);
            System.out.println("Сервер запущен и ожидает клиента");
            socket = server.accept();
            System.out.println("Клиент найден");
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            obOutput = new ObjectOutputStream(out);
            System.out.println("Все потоки созданы");
        } catch (IOException e) {
            System.out.println("Ошибка с вводом/выводом данных");
            System.exit(10);
        }
    }

    /**
     * Метод считывает данные из файла и заполняет ими коллекцию
     */
    private static void load() {
        vectorServer.removeAllElements();
        InputStreamReader in = null;
        int c;
        String str = "";
        String[] strs;
        try {
            in = new InputStreamReader(new FileInputStream("../../../../../Proba.csv"));
            while ((c = in.read()) != -1) {
                if (c == '\n' || c == '\r') {
                    if (!str.trim().equals("")) {
                        Person person = new Person();
                        strs = str.split(";");
                        fillPerson(strs, person);
                        str = "";
                    }
                } else {
                    if (c != 65279) {
                        str = str + (char) c;
                    }
                }
            }
            if (!str.trim().equals("")) {
                Person person = new Person();
                strs = str.split(";");
                fillPerson(strs, person);
            }
        } catch (IOException ioe) {
            System.out.println("Неправильный путь к файлу");
            System.exit(1);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                System.out.println("Проблема с закрытием потока (1)");
            } catch (NullPointerException e) {
                System.out.println("Проблема с закрытием потока (2)");
            }
        }
   //     if (!vector.isEmpty())
   //         message.setText("Loading is done :)");
    }


    /**
     * Метод заполняет Person, беря данные из строки (строка состоит из имени и фраз)
     */
    private static void fillPerson(String[] strs, Person person) {
        if (strs.length != 0) {
            if (!strs[0].trim().equals("")) {
                person.setName(strs[0].trim());
                if (strs.length - 1 != 0) {
                    for (int j = 1; j < strs.length; j++) {
                        person.addPhrase(new Phrase(strs[j].trim()));
                    }
                }
                vectorServer.add(person);
      //      } else {
      //          message.setText("Sorry, but person must have a name");
            }
        }
    }


    public void sendToClient() {
        try {obOutput.writeObject(vectorServer);
            System.out.println("Данные отправлены клиенту");
        } catch (FileNotFoundException e) {
            System.out.println("Класс не найден");
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода");
        }
    }

    public static void main(String [] args) {
        load();
        Server serverServer = new Server();
        serverServer.sendToClient();
        try {
            socket.close();
            server.close();
        } catch (IOException e) {
            System.out.println("Ошибка закрытия серверских сокетов");
        }
        System.exit(0);
    }
}
