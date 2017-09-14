import java.util.Comparator;

public class MyComparator implements Comparator <Person> {
    public int compare(Person person1, Person person2) {
        return person1.getId() - person2.getId();
    }
}
