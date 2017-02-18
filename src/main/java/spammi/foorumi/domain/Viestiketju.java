
package spammi.foorumi.domain;

/**
 *
 * @author mari
 */
public class Viestiketju {
    private int id;
    private String aihe;
    private Alue alue;

    public Viestiketju(int id, String aihe, Alue alue) {
        this.id = id;
        this.aihe = aihe;
        this.alue = alue;
    }

    public Viestiketju(String aihe, Alue alue) {
        this.aihe = aihe;
        this.alue = alue;
    }
    
    

    public int getId() {
        return id;
    }

    public String getAihe() {
        return aihe;
    }

    public Alue getAlue() {
        return alue;
    }

    @Override
    public String toString() {
        return this.aihe;
    }
    
    
}
