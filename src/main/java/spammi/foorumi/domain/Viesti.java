
package spammi.foorumi.domain;

import java.sql.Timestamp;


/**
 *
 * @author mari
 */
public class Viesti {
    private int id;
    private String nimimerkki;
    private Viestiketju viestiketju;

    private Timestamp lahetysaika;

    private String sisalto;

    public Viesti(int id, String nimimerkki, Viestiketju viestiketju, Timestamp lahetysaika, String sisalto) {
        this.id = id;
        this.nimimerkki = nimimerkki;
        this.viestiketju = viestiketju;
        this.lahetysaika = lahetysaika;
        this.sisalto = sisalto;
    }

    public Viesti(String nimimerkki, Viestiketju viestiketju, String sisalto) {
        this.nimimerkki = nimimerkki;
        this.viestiketju = viestiketju;
        this.sisalto = sisalto;
    }

    public int getId() {
        return id;
    }


    public Timestamp getLahetysaika() {


        return lahetysaika;
    }

    public String getNimimerkki() {
        return nimimerkki;
    }

    public String getSisalto() {
        return sisalto;
    }

    public Viestiketju getViestiketju() {
        return viestiketju;
    }
    
    public String toString() {
        return this.nimimerkki +" kirjoitti: \n" + this.sisalto +"\n"+ this.lahetysaika;
    }
    
    
    
}
