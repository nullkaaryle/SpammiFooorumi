
package spammi.foorumi.domain;

import java.sql.Timestamp;
import java.util.Calendar;

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
        //asetaLahetysaika();
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
    
//    public void asetaLahetysaika(){
//        Calendar calendar = Calendar.getInstance();
//        this.lahetysaika = new java.sql.Timestamp(calendar.getTime().getTime());
//    }

    //tulostukseen myös lähetysaika
    @Override
    public String toString() {
        return this.nimimerkki +" kirjoitti: \n" + this.sisalto +"\n"+ this.lahetysaika;
    }
    
    
    
}
