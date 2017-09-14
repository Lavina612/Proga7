import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server {
    private static Vector<Person> vectorServer = new Vector<>();
    private static ServerSocket server;
    private static Socket socket;
    private InputStream in;
    private static OutputStream out;
    private ObjectOutputStream obOutput;
    private static ObjectInputStream obInput;
    private static String str;
    private Server() {
        try {
            server = new ServerSocket(6666);
            System.out.println("Сервер запущен и ожидает клиентов");
            socket = server.accept();
            System.out.println("Клиент найден");
            out = socket.getOutputStream();
            in = socket.getInputStream();
            obInput = null;
            System.out.println("Все потоки созданы");
        } catch (IOException e) {
            e.printStackTrace();
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

    }


    /**
     * Метод заполняет Person, беря данные из строки (строка состоит из имени и фраз)
     */
    private static void fillPerson(String[] strs, Person person) {
        if (strs.length != 0) {
            person.setName(strs[0].trim());
            if (strs.length - 1 != 0) {
                for (int j = 1; j < strs.length; j++) {
                    person.addPhrase(new Phrase(strs[j].trim()));
                }
            }
            vectorServer.add(person);
        }
    }

    /**
     * This command saves all elements in file.
     */
    private static void save() {
        String str = "";
        FileWriter out = null;
        try {
            out = new FileWriter("../../../../../Proba.csv", false);
            for (Person person : vectorServer) {
                for (int n = 0; n < person.getPhrases().size(); n++) {
                    str = str.concat(";" + person.getPhrase(n).trim());
                }
                str = person.getName().trim().concat(str + '\r' + '\n');
                out.write(str);
                str = "";
            }
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


    public void sendToClient() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            obOutput = new ObjectOutputStream(baos);
            obOutput.writeObject(vectorServer);
            obOutput.flush();
            baos.flush();
            out.write((int)Math.ceil((double)baos.size()/(double)512.0));
            out.write(baos.toByteArray());
            out.flush();
            System.out.println("Данные отправлены клиенту");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadVectorFromClient() {
        try {
            System.out.println(obInput);
            vectorServer =  (Vector<Person>) obInput.readObject();
            System.out.println("Данные приняты от клиента");
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
        } catch (IOException e) {
            System.out.println("Ошибка чтения вектора с клиента");
        }
    }

    public void connectionWithClient() {
        try  {
            if (obInput == null) {
                obInput = new ObjectInputStream(in);
            }
            str = (String) obInput.readObject();
        } catch (IOException e) {
            System.out.println("Ошибка с созданием ObjectInputStream");
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
        }

    }


    public static void main(String [] args) {
        load();
        Server serverServer = new Server();
        serverServer.sendToClient();
        serverServer.connectionWithClient();
        while (!str.equals("esc")) {
            System.out.println(str);
            switch (str) {
                case "Load":
                    load();
                    serverServer.sendToClient();
                    break;
                case "Update server":
                    serverServer.loadVectorFromClient();
                    System.out.println(vectorServer.size());
                    break;
                case "Save":
                    serverServer.loadVectorFromClient();
                    save();
                    break;
                default:
                    System.out.println("Какая-то непонятная другая команда");
            }
            serverServer.connectionWithClient();
        }
        System.out.println("Клиент отключился");
        try {
            obInput.close();
            socket.close();
            server.close();
        } catch (IOException e) {
            System.out.println("Ошибка закрытия серверских сокетов");
        }
        System.exit(0);
    }
}
