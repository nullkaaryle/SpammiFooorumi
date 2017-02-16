
package spammi.foorumi.database;

import java.sql.*;
import java.util.List;
import spammi.foorumi.domain.Alue;
import spammi.foorumi.domain.Viestiketju;

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
        
        Viestiketju vk = new Viestiketju(rs.getInt("id"), rs.getString("aihe"), alueDao.findOne(rs.getInt("id")));
        
        rs.close();
        stmnt.close();
        connection.close();
        
        return vk ;
    }

    @Override
    public List<Viestiketju> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
    }
    
}
