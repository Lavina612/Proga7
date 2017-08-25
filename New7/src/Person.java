/**
 * Created by Elizabeth on 20.11.2016.
 */
import java.io.Serializable;
import java.util.*;

public class Person implements Comparable <Person>, Serializable {
    private String name;
    private ArrayList<Phrase> phrases;

    public int compareTo(Person person1) {
       return this.name.length() == person1.name.length() ? this.name.compareTo(person1.name) : this.name.length()-person1.name.length();
    }
    public ArrayList<Phrase> getPhrases() {
        return phrases;
    }
    public void setPhrase(int i, Phrase ph) {
        phrases.get(i).setPhrase(ph.getPhrase());
    }
    public void addPhrase(Phrase phrs){
        phrases.add(phrs);}
   public String getPhrase (int i){
        return phrases.get(i).getPhrase();}
    public void deletePhrase (int i) {
        phrases.remove(i);}
    public void setName (String newName) {
        name = newName;
    }
    public String getName (){
        return name;
    }
    public Person (String name) {
        this.name = name;
        phrases = new ArrayList <> ();
    }
    public Person () {
        phrases = new ArrayList <> ();
    }
    public void say() {
        System.out.println(name + " says");
        for (int i=0; i<phrases.size(); i++) {
            System.out.println(" " + phrases.get(i));
            if (i!=phrases.size()) System.out.println(", ");
        }
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