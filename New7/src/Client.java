import java.io.*;
import java.net.*;
import java.util.Vector;

public class Client {
    private Vector <Person> vectorClient = new Vector<>();
    private Socket socket;
    private ObjectInputStream obInput;
    private ObjectOutputStream obOutput;
    private DataOutputStream dataOutput;
    private DataInputStream dataInput;
    public DataOutputStream getDataOutput() {return dataOutput;}
    public DataInputStream getDataInput() {return dataInput;}
    public Socket getSocket () {
        return socket;
    }
    public Vector<Person> getVectorClient() {
        return vectorClient;
    }
    public void setVectorClient(Vector<Person> vector) {
        vectorClient = vector;
    }
    public Client () {
        try {
            InetAddress address = InetAddress.getByName("DESKTOP-KANTA7F");
            socket = new Socket(address, 6666);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            dataOutput = new DataOutputStream(out);
            dataInput = new DataInputStream(in);
            obInput = new ObjectInputStream(in);
            obOutput = new ObjectOutputStream(out);
            System.out.println("Все потоки созданы");
        } catch (UnknownHostException e) {
            System.out.println("Не удалось установить соединение");
            System.exit(1);
        } catch (ConnectException e) {
            System.out.println("Сервер ещё не работает, поэтому соединение установить не получилось");
            System.exit(2);
        } catch (IOException e) {
            System.out.println("Не удалось создать сокет");
            e.printStackTrace();
            System.exit(3);
        }
    }

    public void loadVectorFromServer() {
        try {vectorClient =  (Vector<Person>) obInput.readObject();
            System.out.println("Данные приняты с сервера");
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
        } catch (IOException e) {
            System.out.println("Ошибка чтения вектора с сервера");
        }
    }

    public void sendVectorToServer() {
        try {obOutput.reset();
            obOutput.writeObject(vectorClient);
            System.out.println("Данные отправлены на сервер");
        } catch (IOException e) {
            System.out.println("Ошибка отправки вектора на сервер");
        }
    }

    public void connectionWithServer(String str) {
        try {
            dataOutput.writeUTF(str);
            dataOutput.flush();
        } catch (IOException e) {
            System.out.println("Данные не отправлены на сервер либо не приняты");
        }

    }

}
