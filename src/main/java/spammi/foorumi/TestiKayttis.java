
package spammi.foorumi;

import java.sql.SQLException;
import spammi.foorumi.database.*;
import spammi.foorumi.domain.*;

/**
 *
 * @author mari
 */
public class TestiKayttis {
    private Database database;
    private AlueDao alueDao;
    private ViestiketjuDao vkDao;
    private ViestiDao viestiDao;
    

    public TestiKayttis(Database database) {
        this.alueDao = new AlueDao(database);
        this.vkDao = new ViestiketjuDao(database);
        this.viestiDao = new ViestiDao(database);
    }
    
    //tulostaa kaikki alueet
    public void naytaAlueet() throws SQLException {
        for (Alue alue : alueDao.findAll()) {
            System.out.println(alue.toString());
        }
    }
    
    //tulostaa kaikki viestiketjut
    public void naytaViestiketjut() throws SQLException {
        for (Viestiketju ketju : vkDao.findAll()){
            System.out.println(ketju.toString());
        }
    }
    
    public void viestiketjutAlueittain() throws SQLException{
        for (Alue alue : alueDao.findAll()){
            System.out.println(alue.toString() + "\n- - - - - - - -");
            for (Viestiketju ketju : vkDao.findByAlue(alue)){
                System.out.println(ketju.toString());
            }
            System.out.println("* * * * * * * *\n");
        }
    }
    
    //tulostaa kaikki viestit, toimii kunhan timestamp toimii
    public void naytaViestit () throws SQLException {
        for (Viesti viesti : viestiDao.findAll()){
            System.out.println(viesti.toString());
        }
    }
    
    //hae kaikki viestiketjut (alueittain?)
    
    //hae kaikki viestit (ketjuittain?)
    
}
