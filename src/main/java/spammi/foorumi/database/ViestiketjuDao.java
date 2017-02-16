
package spammi.foorumi.database;

import java.sql.SQLException;
import java.util.List;
import spammi.foorumi.domain.Viestiketju;

/**
 *
 * @author mari
 */
public class ViestiketjuDao implements Dao <Viestiketju, Integer> {

    @Override
    public Viestiketju findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Viestiketju> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
