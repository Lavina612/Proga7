import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server {
    private static Vector<Person> vectorServer = new Vector<>();
    private static ServerSocket server;
    private static Socket socket;
    private ObjectOutputStream obOutput;
    private ObjectInputStream obInput;
    private static DataInputStream dataInput;
    private static DataOutputStream dataOutput;
    private static String str;
    private static int count = -1;
    private Server() {
        try {
            server = new ServerSocket(6666);
            System.out.println("Сервер запущен и ожидает клиентов");
            socket = server.accept();
            System.out.println("Клиент найден");
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            dataInput = new DataInputStream(in);
            dataOutput = new DataOutputStream(out);
            obOutput = new ObjectOutputStream(out);
            obInput = new ObjectInputStream(in);
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
        count = -1;
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
            if (!strs[0].trim().equals("")) {
                person.setName(strs[0].trim());
                if (strs.length - 1 != 0) {
                    for (int j = 1; j < strs.length; j++) {
                        person.addPhrase(new Phrase(strs[j].trim()));
                    }
                }
                vectorServer.add(person);
            } else {
                int i = 0;
                for (int j = 1; j < strs.length; j++) {
                    if (!strs[j].trim().equals("")) i++;
                }
                if ((count == -1) && (i!=0)) count = vectorServer.size()+1;
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
        try {obOutput.reset();
            obOutput.writeObject(vectorServer);
            obOutput.flush();
            System.out.println("Данные отправлены клиенту");
        } catch (FileNotFoundException e) {
            System.out.println("Класс не найден");
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода");
        }
    }

    public void loadVectorFromClient() {
        try {vectorServer =  (Vector<Person>) obInput.readObject();
            System.out.println("Данные приняты от клиента");
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
        } catch (IOException e) {
            System.out.println("Ошибка чтения вектора с клиента");
        }
    }

    public void connectionWithClient() {
        try {str = dataInput.readUTF();
            System.out.println(str);
        } catch (IOException e) {
            System.out.println("Данные не отправлены на сервер либо не приняты");
        }

    }


    public static void main(String [] args) {
        load();
        Server serverServer = new Server();
        serverServer.sendToClient();
        try {
            dataOutput.writeInt(count);
            serverServer.connectionWithClient();
            while (!str.equals("esc")) {
                switch (str) {
                    case "Load":
                        load();
                        serverServer.sendToClient();
                        dataOutput.writeInt(count);
                        break;
                    case "Update":
                        serverServer.loadVectorFromClient();
                        break;
                    case "Save":
                        serverServer.loadVectorFromClient();
                        save();
                        break;
                    case "Remove last":
                        serverServer.loadVectorFromClient();
                        System.out.println(vectorServer.size());
                        break;
                    case "Remove":
                        serverServer.loadVectorFromClient();
                        System.out.println(vectorServer.size());
                        break;
                    case "Add if min":
                        serverServer.loadVectorFromClient();
                        System.out.println(vectorServer.size());
                        break;
                    default:
                        System.out.println("Какая-то непонятная другая команда");
                }
                serverServer.connectionWithClient();
            }
        }catch (IOException e) {
            System.out.println("Не получилось отправить count");
        }
        try {
            dataInput.close();
            socket.close();
            server.close();
        } catch (IOException e) {
            System.out.println("Ошибка закрытия серверских сокетов");
        }
        System.exit(0);
    }
}
