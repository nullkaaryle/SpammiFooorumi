
package spammi.foorumi.database;

import java.sql.*;
import java.util.*;
import spammi.foorumi.domain.*;

/**
 *
 * @author mari
 */
public class ViestiketjuDao implements Dao <Viestiketju, Integer> {
    
    private Database database;
    private AlueDao alueDao;
    private ViestiDao viestiDao;

    public ViestiketjuDao(Database database) {
        this.database = database;
    }

    @Override
    public Viestiketju findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Viestiketju WHERE id = ?");
        
        stmnt.setInt(1, key);
        ResultSet rs = stmnt.executeQuery();
        
        boolean on = rs.next();
        if (!on){
            return null;
        }
        
        Viestiketju vk = new Viestiketju(rs.getInt("id"), rs.getString("aihe"), alueDao.findOne(rs.getInt("alue")));
        
        rs.close();
        stmnt.close();
        connection.close();
        
        return vk ;
    }
    
    public List<Viestiketju> findByAlue (Alue alue) throws SQLException{
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Viestiketju WHERE alue = ?");
        
        stmnt.setInt(1, alue.getId());
        ResultSet rs = stmnt.executeQuery();
        List<Viestiketju> ketjut = new ArrayList();
        
        while(rs.next()){
            ketjut.add(new Viestiketju(rs.getInt("id"),
                        rs.getString("aihe"), 
                        alueDao.findOne(rs.getInt("alue"))));
        }
        
        rs.close();
        stmnt.close();
        connection.close();
        
        return ketjut;
        
    }

    @Override
    public List<Viestiketju> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Viestiketju");
        
        ResultSet rs = stmnt.executeQuery();
        List<Viestiketju> ketjut = new ArrayList();
        
        while (rs.next()){
            ketjut.add(new Viestiketju(rs.getInt("id"), rs.getString("aihe"), alueDao.findOne(rs.getInt("alue"))));
        }
        
        rs.close();
        stmnt.close();
        connection.close();
        
        return ketjut;
    }

    @Override
    public void create(Viestiketju vk) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("INSERT INTO Viestiketju (aihe, alue) VALUES (?, ?)");
        
        Alue alue = vk.getAlue();
        alue.lisaaViestiketjujenMaaraa();
        
        stmnt.setString(1, vk.getAihe());
        stmnt.setInt(2, alue.getId());
        
        stmnt.execute();
        
        stmnt.close();
        connection.close();
    }
    
    public int countKetjujenViestit(Alue alue) throws SQLException {
        
        List <Viestiketju> ketjut = findByAlue(alue);
        
        int maara = 0;
        for (Viestiketju vk : ketjut){
            maara += viestiDao.countViestit(vk);
        }
        
//        Connection connection = database.getConnection();
//
//        PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Viestiketju WHERE alue = ?");
//        
//        stmnt.setInt(1, alue.getId());
//        ResultSet rs = stmnt.executeQuery();
//        
//        
//        
//        int maara = Integer.parseInt(rs.getString("maara"));
//
//        rs.close();
//        stmnt.close();
//        connection.close();

        return maara;
    }

    public void setAlueDao(AlueDao alueDao) {
        this.alueDao = alueDao;
    }

    public void setViestiDao(ViestiDao viestiDao) {
        this.viestiDao = viestiDao;
    }
    
    
    

    
    
}
