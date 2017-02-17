
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
    private Dao <Alue, Integer> alueDao;

    public ViestiketjuDao(Database database, Dao<Alue, Integer> alueDao) {
        this.database = database;
        this.alueDao = alueDao;
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
    public Viestiketju create(Viestiketju vk) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("INSERT INTO Viestiketju VALUES (?, ?)");
        
        stmnt.setInt(1, vk.getId());
        stmnt.setString(2, vk.getAihe());
        stmnt.setInt(3, vk.getAlue().getId());
        
        stmnt.execute();
        
        stmnt.close();
        connection.close();
        
        return new Viestiketju(vk.getId(), vk.getAihe(), vk.getAlue());
    }

    
    
}
