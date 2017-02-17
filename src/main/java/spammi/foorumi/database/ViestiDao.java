
package spammi.foorumi.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import spammi.foorumi.domain.Viesti;
import spammi.foorumi.domain.Viestiketju;

/**
 *
 * @author mari
 */
public class ViestiDao implements Dao <Viesti, Integer> {
    
    private Database database;
    private Dao <Viestiketju, Integer> vkDao;

    public ViestiDao(Database database, Dao<Viestiketju, Integer> vkDao) {
        this.database = database;
        this.vkDao = vkDao;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        return null;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Viesti");
        
        ResultSet rs = stmnt.executeQuery();
        List<Viesti> viestit = new ArrayList();
        
        while(rs.next()){
        viestit.add(new Viesti(rs.getInt("id"),
                    rs.getString("nimimerkki"),
                    (vkDao.findOne(rs.getInt("id"))),
                    rs.getTimestamp("lahetysaika"),
                    rs.getString("sisalto")));
        }
        
        rs.close();
        stmnt.close();
        connection.close();
        
        return viestit;
    }

    @Override
    public Viesti create(Viesti t) throws SQLException {
        return null;
    }
    
}
