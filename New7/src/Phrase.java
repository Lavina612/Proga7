/**
 * Created by Elizabeth on 20.11.2016.
 */
public class Phrase {
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