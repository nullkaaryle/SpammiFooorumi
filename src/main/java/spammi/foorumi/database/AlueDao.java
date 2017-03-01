package spammi.foorumi.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import spammi.foorumi.domain.Alue;

/**
 * @author mari, ninna, pinni
 */
public class AlueDao implements Dao<Alue, Integer> {

    Database database;
    ViestiketjuDao vkDao;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("SELECT a.id, a.otsikko, COUNT(vk.id) AS viestiketjujenMaara FROM Alue a LEFT JOIN Viestiketju vk ON a.id = vk.alue WHERE a.id = ? GROUP BY a.id");

        stmnt.setInt(1, key);
        ResultSet rs = stmnt.executeQuery();

        boolean on = rs.next();
        if (!on) {
            return null;
        }
        
        Alue alue = new Alue(
                rs.getInt("id"), 
                rs.getString("otsikko"),
                rs.getInt("viestiketjujenMaara"));
        
        rs.close();
        stmnt.close();
        connection.close();
        
        return alue;
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("SELECT a.id, a.otsikko, COUNT(vk.id) AS viestiketjujenMaara FROM Alue a LEFT JOIN Viestiketju vk ON a.id = vk.alue GROUP BY a.id ORDER BY otsikko");

        ResultSet rs = stmnt.executeQuery();
        List<Alue> alueet = new ArrayList();

        while (rs.next()) {
            Alue alue = new Alue(
                rs.getInt("id"), 
                rs.getString("otsikko"),
                rs.getInt("viestiketjujenMaara"));
            
            alue.setVkMaara(vkDao.countKetjujenViestit(alue));
            alue.setViimeisinViesti(vkDao.findLatestLahetysaika(alue));
            alueet.add(alue);
        }

        rs.close();
        stmnt.close();
        connection.close();

        return alueet;
    }

    @Override
    public void create(Alue t) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("INSERT INTO Alue (otsikko) VALUES (?)");
        
        stmnt.setString(1, t.getOtsikko());
        stmnt.execute();
        
        stmnt.close();
        connection.close();
        
    }

    public void setVkDao(ViestiketjuDao vkDao) {
        this.vkDao = vkDao;
    }
    
    

}
