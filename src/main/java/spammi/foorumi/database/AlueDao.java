package spammi.foorumi.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import spammi.foorumi.domain.Alue;

/**
 *
 * @author mari
 */
public class AlueDao implements Dao<Alue, Integer> {

    Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Alue WHERE id = ?");

        stmnt.setInt(1, key);
        ResultSet rs = stmnt.executeQuery();

        boolean on = rs.next();
        if (!on) {
            return null;
        }
        
        rs.close();
        stmnt.close();
        connection.close();
        
        return new Alue(rs.getInt("id"), rs.getString("otsikko"));
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Alue");

        ResultSet rs = stmnt.executeQuery();
        List<Alue> alueet = new ArrayList();

        while (rs.next()) {
            alueet.add(new Alue(rs.getInt("id"), rs.getString("otsikko")));
        }

        rs.close();
        stmnt.close();
        connection.close();

        return alueet;
    }

    @Override
    public Alue create(Alue t) throws SQLException {
        return null;
    }

}
