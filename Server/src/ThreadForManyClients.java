import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class ThreadForManyClients extends Thread {
    private Socket socket;
    private ForDataBase fdb;
    private InputStream in;
    private OutputStream out;
    private ObjectInputStream obInput;
    private ObjectOutputStream obOutput;
    private static Vector<Person> vectorServer;
    private DataVector dv;
    private int countClients;
    private String str;
    private ArrayList<ThreadForManyClients> listOfThreads;
    public ThreadForManyClients (Vector <Person> vector, int countClients, Socket socket, ArrayList<ThreadForManyClients> listOfThreads, ForDataBase fdb) {
        this.countClients = countClients;
        this.socket = socket;
        this.fdb = fdb;
        vectorServer = vector;
        this.listOfThreads = listOfThreads;
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
            obInput = null;
            System.out.println("Все потоки созданы для клиента №" +  countClients);
        } catch (IOException e) {
            System.out.println("Ошибка создания потоков у клиента №" + countClients);
        }
        start();
    }
    public void run() {
        try {
            vectorServer = fdb.loadFromDB();
            vectorServer.sort(new MyComparator());
            sendToClient();
            connectionWithClient();
            while (!str.equals("esc")) {
                System.out.println(str);
                switch (str) {
                    case "Update server":
                        loadDataVectorFromClient();
                        fdb.sendToBD(dv);
                        break;
                    default:
                        System.out.println("Какая-то непонятная другая команда");
                }
                connectionWithClient();
            }
            System.out.println("Клиент №" + countClients + "отключился");
            listOfThreads.remove(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public synchronized void sendToClient() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            obOutput = new ObjectOutputStream(baos);
            obOutput.writeObject(vectorServer);
            obOutput.flush();
            baos.flush();
            out.write((int)Math.ceil((double)baos.size()/(double)512.0));
            out.write(baos.toByteArray());
            out.flush();
            System.out.println("Данные отправлены клиенту №" + countClients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendDataVectorToClient(DataVector dv) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            obOutput = new ObjectOutputStream(baos);
            System.out.println(dv);
            obOutput.writeObject(dv);
            System.out.println(dv);
            obOutput.flush();
            baos.flush();
            out.write((int)Math.ceil((double)baos.size()/(double)512.0));
            out.write(baos.toByteArray());
            out.flush();
            System.out.println("Данные отправлены клиенту №" + countClients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataVectorFromClient() {
        try {
            dv =  (DataVector) obInput.readObject();
            System.out.println(dv);
            System.out.println("Данные приняты от клиента №" + countClients);
            if (!listOfThreads.isEmpty()) {
                for (ThreadForManyClients tfmc : listOfThreads) {
                    if (tfmc != this) {
                        tfmc.sendDataVectorToClient(dv);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(456);
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

}
