package spammi.foorumi.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author mari
 */
public class Alue {

    private int id;
    private String otsikko;
    private int vkMaara;
    private String viimeisin;

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

    public void setVkMaara(int vkMaara) {
        this.vkMaara = vkMaara;
    }

    public void setViimeisinViesti(Timestamp viimeisinViesti) {
        if (viimeisinViesti == null){
            this.viimeisin = " ";
        } else {
            this.viimeisin = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(viimeisinViesti);
        }

    }

    public int getVkMaara() {
        return vkMaara;
    }

    public String getViimeisin() {
        return viimeisin;
    }

}
