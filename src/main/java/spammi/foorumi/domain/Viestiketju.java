package spammi.foorumi.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author mari, ninna, pinni
 */
public class Viestiketju {

    private int id;
    private String aihe;
    private Alue alue;
    private int viestimaara;
    private String viimeisin;

    public Viestiketju(int id, String aihe, Alue alue, int viestimaara, Timestamp viimeisinViesti) {
        this.id = id;
        this.aihe = aihe;
        this.alue = alue;
        this.viestimaara = viestimaara;
        setViimeisin(viimeisinViesti);
    }

    private void setViimeisin(Timestamp aika) {
        if (aika == null) {
            this.viimeisin = " ";
        } else {
            this.viimeisin = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(aika);
        }
    }

    public Viestiketju(String aihe, Alue alue) {
        this.aihe = aihe;
        this.alue = alue;
    }

    public Viestiketju(int id, String aihe, Alue alue) {
        this.id = id;
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

//    public void setvMaara(int vMaara) {
//        this.viestimaara = vMaara;
//    }
    public int getvMaara() {
        return viestimaara;
    }

    public String getViimeisin() {
        return viimeisin;
    }

}
