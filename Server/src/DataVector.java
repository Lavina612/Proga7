import java.io.Serializable;

public class DataVector implements Serializable{
    private byte information;
    private Person person = null;
    public static final byte DELETE = 0, UPDATE = 1, ADD = 2;
    public byte getInformation() { return information;}
    public Person getPerson() {
        return person;
    }
    public DataVector () {}
    public DataVector (byte inform, Person person) {
        information = inform;
        this.person = person;
    }
}
