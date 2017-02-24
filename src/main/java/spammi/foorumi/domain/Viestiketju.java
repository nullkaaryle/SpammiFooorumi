
package spammi.foorumi.domain;

/**
 *
 * @author mari
 */
public class Viestiketju {
    private int id;
    private String aihe;
    private Alue alue;
    private int vMaara;

    public Viestiketju(int id, String aihe, Alue alue) {
        this.id = id;
        this.aihe = aihe;
        this.alue = alue;
        this.vMaara = 0;
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

    public int getvMaara() {
        return vMaara;
    }

    public void setvMaara(int vMaara) {
        this.vMaara = vMaara;
    }
    
    
    public void lisaaViestienMaaraa(){
        this.vMaara++;
    }
    
}
