import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

public class Client {
    private static Locale locale = new Locale("en", "CA");
    private static ResourceBundle resBun = ResourceBundle.getBundle("texts/all_phrases", locale);
    private static ZoneId zoneId = ZoneId.of("Canada/Central");
    public boolean bool;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private ObjectOutputStream obOutput;
    private Vector <Person> vectorClient = new Vector<>();
    private DataVector dataVector;
    private SocketChannel sc;
    public Locale getLocale() { return locale; }
    public ResourceBundle getResBun() {
        return resBun;
    }
    public ZoneId getZoneId() {
        return zoneId;
    }
    public void setLocale(Locale loc) { locale = loc; }
    public void setResBun(ResourceBundle rb) {
        resBun = rb;
    }
    public void setZoneId(ZoneId zi) {
        zoneId = zi;
    }
    public SocketChannel getSocketChannel() {return sc;}
    public Vector<Person> getVectorClient() {
        return vectorClient;
    }
    public void setVectorClient (Vector <Person> vectorClient) {this.vectorClient = vectorClient;}
    public Client () {
        try {
            sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("DESKTOP-KANTA7F", 6666));
            sc.configureBlocking(false);
            obOutput = new ObjectOutputStream(baos);
            System.out.println(resBun.getString("all_thr"));
        } catch (UnknownHostException e) {
            System.out.println(resBun.getString("er2"));
            System.exit(1);
        } catch (ConnectException e) {
            System.out.println(resBun.getString("er3"));
            System.exit(2);
        } catch (IOException e) {
            System.out.println(resBun.getString("er4"));
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
            System.out.println(resBun.getString("d_f_s"));
        } catch (IOException e) {
            System.out.println(resBun.getString("er5"));
        } catch (ClassNotFoundException e) {
            System.out.println(resBun.getString("er6"));
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
                    System.out.println(resBun.getString("som_else"));
            }
            bais.close();
            obInput.close();
            System.out.println(resBun.getString("d_f_s"));
        } catch (IOException e) {
            System.out.println(resBun.getString("er7"));
            System.exit(20);
        } catch (ClassNotFoundException e) {
            System.out.println(resBun.getString("er6"));
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
            System.out.println(resBun.getString("d_t_s"));
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
            System.out.println(resBun.getString("c_t_s"));
        } catch (IOException e) {
            System.out.println(resBun.getString("er8"));
        }

    }

}
