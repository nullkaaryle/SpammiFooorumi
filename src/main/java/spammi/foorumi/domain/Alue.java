
package spammi.foorumi.domain;

/**
 *
 * @author mari
 */
public class Alue {
    private int id;
    private String otsikko;

    public Alue(int id, String otsikko) {
        this.id = id;
        this.otsikko = otsikko;
    }

    public int getId() {
        return id;
    }

    public String getOtsikko() {
        return otsikko;
    }
    
}
