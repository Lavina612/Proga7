import org.postgresql.ds.PGConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

public class ForDataBase {
    private PGConnectionPoolDataSource pgConPoolDS = new PGConnectionPoolDataSource();
    private PooledConnection pooledConnection;
    private ORM orm;
    private String username;
    private String password;
    public ForDataBase (String url, String username, String password, String driver) throws SQLException, ClassNotFoundException {
        this.username = username;
        this.password = password;
        pgConPoolDS.setUrl(url);
        pooledConnection = pgConPoolDS.getPooledConnection(username, password);
        Class.forName(driver);
        orm = new ORM(pooledConnection);
    }
    public synchronized Vector<Person> loadFromDB() throws SQLException{
        Vector<Person> person = null;
        try {
            person = orm.getAllObjects(Person.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(person);
        return person;
    }

    public void insert(String str, DataVector dv, Connection connectionU1) {
        try {
            orm.writeObject(dv.getPerson());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(dv.getPerson());
    }

    public synchronized void sendToBD(DataVector dv) {
        Connection connection = null;
        try {
            connection = pooledConnection.getConnection();
            connection.setAutoCommit(false);
            String str;
            switch (dv.getInformation()) {
                case DataVector.DELETE:
                    orm.deleteObject(dv.getPerson());
                    break;
                case DataVector.UPDATE:
                    orm.updateObject(dv.getPerson());
                    break;
                case DataVector.ADD:
                    orm.writeObject(dv.getPerson());
                    break;
                default:
                    System.out.println("Чё-то не то");
            }
        } catch (Exception e) {
            try {
                connection.rollback();
                System.out.println("Ошибка связи с бд");
            } catch (SQLException e1) {
                System.out.println("Невозможно откатнуться назад");
            }

        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Проблемы с закрытием соединения");
            }
        }
    }
}
