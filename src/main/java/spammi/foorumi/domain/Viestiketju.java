
package spammi.foorumi.domain;

import java.sql.Timestamp;

/**
 *
 * @author mari
 */
public class Viestiketju {
    private int id;
    private String aihe;
    private Alue alue;
    private int viestimaara;
    private Timestamp viimeisinViesti;

    public Viestiketju(int id, String aihe, Alue alue, int viestimaara, Timestamp viimeisinViesti) {
        this.id = id;
        this.aihe = aihe;
        this.alue = alue;
        this.viestimaara = viestimaara;
        this.viimeisinViesti = viimeisinViesti;
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
    
   // public void lisaaViestienMaaraa(){
   //     this.viestimaara++;
   // }

    public void setvMaara(int vMaara) {
        this.viestimaara = vMaara;
    }

    public void setViimeisinViesti(Timestamp viimeisinViesti) {
        this.viimeisinViesti = viimeisinViesti;
    }

    public int getvMaara() {
        return viestimaara;
    }

    public Timestamp getViimeisinViesti() {
        return viimeisinViesti;
    }
    
    
    
}
