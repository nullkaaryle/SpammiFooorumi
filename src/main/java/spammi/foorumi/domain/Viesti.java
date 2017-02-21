
package spammi.foorumi.domain;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author mari
 */
public class Viesti {
    private int id;
    private String nimimerkki;
    private Viestiketju viestiketju;
    private Date lahetysaika;
    private String sisalto;

    public Viesti(int id, String nimimerkki, Viestiketju viestiketju, Date lahetysaika, String sisalto) {
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

    public Date getLahetysaika() {
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
    

    //tulostukseen myös lähetysaika
    @Override
    public String toString() {
        return this.nimimerkki +" kirjoitti: \n" + this.sisalto +"\n"+ this.lahetysaika;
    }
    
    
    
}
