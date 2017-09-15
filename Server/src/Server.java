import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class Server {
    private static Vector<Person> vectorServer = new Vector<>();
    private static ServerSocket server;
    private static Socket socket;
    private int countClients = 0;
    private static ThreadForManyClients tfmc;
    private ArrayList <ThreadForManyClients> listOfThreads = new ArrayList <>();
    private Server() {
        try {
            ForDataBase fdb = new ForDataBase("jdbc:postgresql://localhost:5432/postgres", "postgres", "qwerty", "org.postgresql.Driver");
            System.out.println("Подключено к БД");
            server = new ServerSocket(6666);
            System.out.println("Сервер запущен и ожидает клиентов");
            while (true) {
                socket = server.accept();
                countClients++;
                tfmc = new ThreadForManyClients(vectorServer, countClients, socket, listOfThreads, fdb);
                listOfThreads.add(tfmc);
                System.out.println(listOfThreads.size());
                System.out.println("Клиент №" + countClients + " найден");
            }
        } catch (BindException e) {
            System.out.println("Сервер уже подключен");
            System.exit(10);
        } catch (IOException e) {
            System.out.println("Ошибка с вводом/выводом данных");
            System.exit(11);
        } catch (SQLException e) {
            System.out.println("Не удаётся подключиться к БД");
            System.exit(12);
        } catch (ClassNotFoundException e) {
            System.out.println("Класс не найден");
            System.exit(13);
        }
    }


    public static void main(String [] args) {
        Server serverServer = new Server();
        try {
            socket.close();
            server.close();
        } catch (IOException e) {
            System.out.println("Ошибка закрытия серверских сокетов");
        }
        System.exit(0);
    }
}
