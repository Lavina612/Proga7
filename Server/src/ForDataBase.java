import org.postgresql.ds.PGConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class ForDataBase {
    private PGConnectionPoolDataSource pgConPoolDS = new PGConnectionPoolDataSource();
    private PooledConnection pooledConnection;
    private String username;
    private String password;
    public ForDataBase (String url, String username, String password, String driver) throws SQLException, ClassNotFoundException {
        this.username = username;
        this.password = password;
        pgConPoolDS.setUrl(url);
        pooledConnection = pgConPoolDS.getPooledConnection(username, password);
        Class.forName(driver);
    }
    public synchronized Vector<Person> loadFromDB() throws SQLException{
        Connection connection = pooledConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rsPerson = statement.executeQuery("select * from person;");
        Vector <Person> vector = new Vector<>();
        while (rsPerson.next()) {
            Statement statementNew = connection.createStatement();
            Person person = new Person();
            person.setName(rsPerson.getString("name"));
            person.setId(rsPerson.getInt("id_per"));
            ResultSet rsPhrase = statementNew.executeQuery("select * from phrase where phrase.id_per=" + person.getId() + ";");
            while (rsPhrase.next()) {
                person.addPhrase(new Phrase(rsPhrase.getString("phrase")));
            }
            vector.add(person);
            statementNew.close();
            rsPhrase.close();
        }
        rsPerson.close();
        statement.close();
        connection.close();
        return vector;
    }

    public void insert(String str, DataVector dv, Connection connectionU1) {
        try {
            for (Phrase phrase : dv.getPerson().getPhrases()) {
                Statement statementU3 = connectionU1.createStatement();
                str = "insert into phrase values ('" + phrase.getPhrase() + "'," + dv.getPerson().getId() + ");";
                statementU3.execute(str);
                statementU3.close();
            }
        } catch (SQLException e) {
            System.out.println("Ошибка с БД insert");
        }
    }

    public synchronized void sendToBD(DataVector dv) {
        Connection connection = null;
        try {
            String str;
            switch (dv.getInformation()) {
                case DataVector.DELETE:
                    connection = pooledConnection.getConnection();
                    Statement statementD = connection.createStatement();
                    str = "delete from person where id_per=" + dv.getPerson().getId() + ";";
                    statementD.execute(str);
                    statementD.close();
                    connection.close();
                    break;
                case DataVector.UPDATE:
                    connection = pooledConnection.getConnection();
                    connection.setAutoCommit(false);
                    Statement statementU1 = connection.createStatement();
                    str = "update person set name='" + dv.getPerson().getName() + "' where id_per=" + dv.getPerson().getId() + ";";
                    statementU1.execute(str);
                    statementU1.close();
                    Statement statementU2 = connection.createStatement();
                    str = "delete from phrase where id_per=" + dv.getPerson().getId() + ";";
                    statementU2.execute(str);
                    statementU2.close();
                    insert(str, dv, connection);
                    connection.commit();
                    connection.setAutoCommit(true);
                    connection.close();
                    break;
                case DataVector.ADD:
                    connection = pooledConnection.getConnection();
                    connection.setAutoCommit(false);
                    Statement statementA1 = connection.createStatement();
                    str = "insert into person values (" + dv.getPerson().getId() + ",'" + dv.getPerson().getName() + "');";
                    statementA1.execute(str);
                    statementA1.close();
                    insert(str, dv, connection);
                    connection.commit();
                    connection.setAutoCommit(true);
                    connection.close();
                    break;
                default:
                    System.out.println("Чё-то не то");
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                System.out.println("Очередная ошибка с БД");
            }
            e.printStackTrace();
        }
    }
}
