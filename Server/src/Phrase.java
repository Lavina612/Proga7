import Annotations.Column;
import Annotations.Table;

import java.io.Serializable;

/**
 * Created by Elizabeth on 20.11.2016.
 */
@Table(name="phrase")
public class Phrase implements Serializable{
    @Column(name="phrase")
    private String phrase;
    public Phrase () {}
    public Phrase (String phrase) {
        this.phrase = phrase;
    }
    public void setPhrase (String newPhrase) {
        phrase = newPhrase;
    }
    public String getPhrase () {
        return phrase;
    }
}