package spammi.foorumi.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author mari, ninna, pinni
 */
public class Alue {

    private int id;
    private String otsikko;
    private int vMaara;
    private String viimeisin;
    private int viestiketjujenMaara;

    public Alue(int id, String otsikko, int viestiketjujenMaara) {
        this.id = id;
        this.otsikko = otsikko;
        this.vMaara = 0;
        this.viestiketjujenMaara = viestiketjujenMaara;
    }

    public Alue(String otsikko) {
        this.otsikko = otsikko;
        this.vMaara = 0;
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

    public void setVkMaara(int vkMaara) {
        this.vMaara = vkMaara;
    }

    public void setViimeisinViesti(Timestamp viimeisinViesti) {
        if (viimeisinViesti == null){
            this.viimeisin = " ";
        } else {
            this.viimeisin = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(viimeisinViesti);
        }

    }

    public int getVMaara() {
        return vMaara;
    }

    public String getViimeisin() {
        return viimeisin;
    }

    public int getVkMaara() {
        return viestiketjujenMaara;
    }
    
    

}
