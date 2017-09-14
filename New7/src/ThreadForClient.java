import javafx.application.Platform;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

public class ThreadForClient extends Thread{
    private SocketChannel socketChannel = null;
    private Selector selector = null;
    private Client client = null;
    private long lastSend = 0;
    private boolean first = true;
    public ThreadForClient(Client client) {
        socketChannel = client.getSocketChannel();
        this.client = client;
    }
    public void wakeup(){
        selector.wakeup();
    }
    public void run() {
        try {
            selector = SelectorProvider.provider().openSelector();
            socketChannel.register(selector, SelectionKey.OP_READ);
            while(!client.bool) {
                System.out.println("Жду");
                selector.select();
                System.out.println("Сработал селектор");
                if(client.bool) break;
                System.out.println("Количество ключей: " + selector.selectedKeys().size());
                Iterator it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey selKey = (SelectionKey) it.next();
                    it.remove();
                    if(!selKey.isValid()) {System.out.println("Ilya ne pidr");continue;}
                    if (selKey.isReadable()) {
                        if((System.currentTimeMillis()-lastSend)>1000) {
                            if (first) {
                                lastSend = System.currentTimeMillis();
                                client.loadVectorFromServer();
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        Main.getMin();
                                        Main.rewriting();
                                    }
                                });
                                first = false;
                            } else {
                                lastSend = System.currentTimeMillis();
                                client.loadDataVectorFromServer();
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        Main.getMin();
                                        Main.rewriting();
                                    }});
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Ошибка создания канала или селектора");
        } finally {
            try {
                selector.close();
                socketChannel.close();
            } catch (IOException e) {
                System.out.println("Не получилось закрыть канал или селектор");
            }
        }
    }
}
