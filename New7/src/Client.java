import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Vector;

public class Client {
    public boolean bool;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private ObjectOutputStream obOutput;
    private Vector <Person> vectorClient = new Vector<>();
    private DataVector dataVector;
    private SocketChannel sc;
    public SocketChannel getSocketChannel() {return sc;}
    public Vector<Person> getVectorClient() {
        return vectorClient;
    }
    public Client () {
        try {
            sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("DESKTOP-KANTA7F", 6666));
            sc.configureBlocking(false);
            obOutput = new ObjectOutputStream(baos);
            System.out.println("Все потоки созданы");
        } catch (UnknownHostException e) {
            System.out.println("Не удалось установить соединение");
            System.exit(1);
        } catch (ConnectException e) {
            System.out.println("Сервер ещё не работает, поэтому соединение установить не получилось");
            System.exit(2);
        } catch (IOException e) {
            System.out.println("Не удалось создать сокет");
            System.exit(3);
        }
    }

    public void loadVectorFromServer() {
        try {
            ByteBuffer size = ByteBuffer.allocate(1);
            sc.read(size);
            ByteBuffer bbvector = ByteBuffer.allocate(512* (int)size.get(0));
            sc.read(bbvector);
            ByteArrayInputStream bais = new ByteArrayInputStream(bbvector.array());
            ObjectInputStream obInput = new ObjectInputStream(bais);
            vectorClient = (Vector<Person>) obInput.readObject();
            bais.close();
            obInput.close();
            System.out.println("Данные приняты с сервера");
        } catch (IOException e) {
            System.out.println("Не удалось принять данные с сервера");
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
        }
    }

    public void loadDataVectorFromServer() {
        try {
            ByteBuffer size = ByteBuffer.allocate(1);
            sc.read(size);
            ByteBuffer bbvector = ByteBuffer.allocate(512* (int)size.get(0));
            sc.read(bbvector);
            ByteArrayInputStream bais = new ByteArrayInputStream(bbvector.array());
            ObjectInputStream obInput = new ObjectInputStream(bais);
            dataVector = (DataVector) obInput.readObject();
            switch (dataVector.getInformation()) {
                case DataVector.DELETE:
                    vectorClient.remove(dataVector.getPerson());
                    break;
                case DataVector.UPDATE:
                    vectorClient.set(vectorClient.indexOf(dataVector.getPerson()), dataVector.getPerson());
                    break;
                case DataVector.ADD:
                    vectorClient.add(dataVector.getPerson());
                    break;
                default:
                    System.out.println("Что-то другое");
            }
            bais.close();
            obInput.close();
            System.out.println("Данные приняты с сервера");
        } catch (IOException e) {
            System.out.println("Сервер отключился, поэтому клиент вынужден прекратить работу");
            System.exit(20);
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
        }
    }


    public void sendDataVectorToServer(byte inform, Person person) {
        try {
            DataVector dv = new DataVector(inform, person);
            obOutput.writeObject(dv);
            obOutput.flush();
            baos.flush();
            sc.write(ByteBuffer.wrap(baos.toByteArray()));
            baos.reset();
            obOutput.reset();
            System.out.println("Данные отправлены на сервер");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectionWithServer(String str) {
        try {
            obOutput.writeObject(str);
            obOutput.flush();
            baos.flush();
            sc.write(ByteBuffer.wrap(baos.toByteArray()));
            baos.reset();
            obOutput.reset();
            System.out.println("Команда отправлена на сервер");
        } catch (IOException e) {
            System.out.println("Данные не отправлены на сервер либо не приняты");
        }

    }

}
