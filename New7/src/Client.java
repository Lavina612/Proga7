import java.io.*;
import java.net.*;
import java.util.Vector;

public class Client {
    private Vector <Person> vectorClient = new Vector<>();
    private Socket socket;
    private ObjectInputStream obInput;
    public Socket getSocket () {
        return socket;
    }
    public Vector<Person> getVectorClient() {
        return vectorClient;
    }
    public Client () {
        try {
            InetAddress address = InetAddress.getByName("DESKTOP-KANTA7F");
            socket = new Socket(address, 6666);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            obInput = new ObjectInputStream(in);
            System.out.println("Все потоки созданы");
        } catch (UnknownHostException e) {
            System.out.println("Не удалось установить соединение");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Не удалось создать сокет");
            e.printStackTrace();
            System.exit(2);
        }
    }

    public void loadFromServer() {
        try {vectorClient = (Vector<Person>) obInput.readObject();
            System.out.println("Данные приняты с сервера");
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
        } catch (IOException e) {
            System.out.println("Ошибка чтения объекта");
        }
    }
}
