/**
 * Created by Elizabeth on 20.11.2016.
 */
import Annotations.*;

import java.io.Serializable;
import java.text.DateFormat;
import java.time.ZonedDateTime;
import java.util.*;

@Table (name="person")
public class Person implements Comparable <Person>, Comparator<Person>, Serializable {
    @Column(name="name")
    private String name;
    @DateTime
    @Column(name="time")
    private ZonedDateTime time;
    @Id
    @Column(name="id_per")
    private int id_per;
    @Property(type="Phrase", refColumn="id_per")
    private ArrayList<Phrase> phrases;
    //  public static final long serialVersionUID = 10L;

    public int compareTo(Person person1) {
        return this.name.length() == person1.name.length() ? this.name.compareTo(person1.name) : this.name.length()-person1.name.length();
    }
    public int compare(Person person1, Person person2) {
        return person1.getT().getYear() == person2.getT().getYear() ?
                person1.getT().getMonthValue() == person2.getT().getMonthValue() ?
                        person1.getT().getDayOfMonth() == person2.getT().getDayOfMonth() ?
                                person1.getT().getHour() == person2.getT().getHour() ?
                                        person1.getT().getMinute() == person2.getT().getMinute() ?
                                                person1.getT().getSecond() == person2.getT().getSecond() ?
                                                        1 :
                                                        person1.getT().getSecond()-person2.getT().getSecond():
                                                person1.getT().getMinute()-person2.getT().getMinute():
                                        person1.getT().getHour()-person2.getT().getHour():
                                person1.getT().getDayOfMonth()-person2.getT().getDayOfMonth():
                        person1.getT().getMonthValue()-person2.getT().getMonthValue():
                person1.getT().getYear()-person2.getT().getYear();
    }
    public ArrayList<Phrase> getPhrases() {
        return phrases;
    }
    public void setPhrase(int i, Phrase ph) {
        phrases.get(i).setPhrase(ph.getPhrase());
    }
    public void addPhrase(Phrase phrs){
        phrases.add(phrs);}
    public int getId_per() {
        return id_per;
    }
    public void setId_per(int id_per) {
        this.id_per = id_per;
    }
    public void setName (String newName) {
        name = newName;
    }
    public String getName (){
        return name;
    }
    public String getTime (Locale locale) {
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
        String result = formatter.format(Date.from(time.toInstant()));
        return result;
    }
    public ZonedDateTime getT() {  return time;  }
    public void setTime(ZonedDateTime zdt) {
        time = time.withZoneSameInstant(zdt.getZone());
    }
    public Person (String name) {
        this.name = name;
        time = ZonedDateTime.now();
        phrases = new ArrayList <> ();
    }
    public Person () {
        time = ZonedDateTime.now();
        phrases = new ArrayList <> ();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        if (id_per != person.id_per) return false;
        else return true;
    }

    @Override
    public int hashCode() {
        return id_per;
    }

    public String toString(){
        String str = "Name: " + name;
        for (int j = 0; j < phrases.size(); j++) {
            int g = j+1;
            str = str.concat(";  phrase" + g + ": " + phrases.get(j).getPhrase());
        }
        return str;
    }
}