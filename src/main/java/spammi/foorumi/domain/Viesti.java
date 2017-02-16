
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
    private Timestamp lahetysika;
    private String sisalto;

    public Viesti(int id, String nimimerkki, Viestiketju viestiketju, Timestamp lahetysika, String sisalto) {
        this.id = id;
        this.nimimerkki = nimimerkki;
        this.viestiketju = viestiketju;
        this.lahetysika = lahetysika;
        this.sisalto = sisalto;
    }

    public int getId() {
        return id;
    }

    public Timestamp getLahetysika() {
        return lahetysika;
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
    
    
}
