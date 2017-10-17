import javafx.application.Platform;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.text.MessageFormat;
import java.util.Iterator;

public class ThreadForClient extends Thread{
    private SocketChannel socketChannel = null;
    private Selector selector = null;
    private Client client = null;
  //  private long lastSend = 0;
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
                System.out.println(client.getResBun().getString("wait"));
                selector.select();
                System.out.println(client.getResBun().getString("work_sel"));
                if(client.bool) break;
                String temp = client.getResBun().getString("key_am");
                String template = MessageFormat.format(temp, selector.selectedKeys().size());
                System.out.println(template);
                Iterator it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey selKey = (SelectionKey) it.next();
                    it.remove();
                    if(!selKey.isValid()) continue;
                    if (selKey.isReadable()) {
                  //      if((System.currentTimeMillis()-lastSend)>1000) {
                            if (first) {
                         //       lastSend = System.currentTimeMillis();
                                client.loadVectorFromServer();
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        Main.getMin();
                                        Main.rewriting();
                                    }
                                });
                                first = false;
                            } else {
                    //            lastSend = System.currentTimeMillis();
                                client.loadDataVectorFromServer();
                                Platform.runLater(new Runnable() {
                                    public void run() {
                                        Main.getMin();
                                        Main.rewriting();
                                        Main.getMessage().setText(client.getResBun().getString("change_col"));
                                    }});
                            }
                   //     }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(client.getResBun().getString("er1"));
        } finally {
            try {
                selector.close();
                socketChannel.close();
            } catch (IOException e) {
                System.out.println(client.getResBun().getString("er_cl_1"));
            }
        }
    }
}
