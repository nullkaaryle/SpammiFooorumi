
package spammi.foorumi.domain;

import java.sql.Timestamp;

/**
 *
 * @author mari
 */
public class Alue {
    private int id;
    private String otsikko;
    private int vkMaara;
    private Timestamp viimeisinViesti;

    public Alue(int id, String otsikko) {
        this.id = id;
        this.otsikko = otsikko;
        this.vkMaara = 0;
    }

    public Alue(String otsikko) {
        this.otsikko = otsikko;
        this.vkMaara = 0;
    }
    

    public int getId() {
        return id;
    }

    public String getOtsikko() {
        return otsikko;
    }

    @Override
    public String toString() {
        return this.otsikko;
    }

   // public void lisaaViestiketjujenMaaraa() {
   //     this.vkMaara++;
   // }

    public void setVkMaara(int vkMaara) {
        this.vkMaara = vkMaara;
    }

    public void setViimeisinViesti(Timestamp viimeisinViesti) {
        this.viimeisinViesti = viimeisinViesti;
    }

    public int getVkMaara() {
        return vkMaara;
    }

    public Timestamp getViimeisinViesti() {
        return viimeisinViesti;
    }
    
    
    
    
}
