package spammi.foorumi.database;

import java.sql.*;
import java.util.*;
import spammi.foorumi.domain.Viesti;

/**
 *
 * @author mari
 */
public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    private ViestiketjuDao vkDao;

    public ViestiDao(Database database) {
        this.database = database;
        this.vkDao = new ViestiketjuDao(database);
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

        while (rs.next()) {
            viestit.add(new Viesti(rs.getInt("id"),
                    rs.getString("nimimerkki"),
                    (vkDao.findOne(rs.getInt("viestiketju"))),
                    rs.getTimestamp("lahetysaika"),
                    rs.getString("sisalto")));
        }

        rs.close();
        stmnt.close();
        connection.close();

        return viestit;
    }

    @Override
    public Viesti create(Viesti v) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("INSERT INTO Viesti (nimimerkki, viestiketju, lahetysaika, sisalto) VALUES (?, ?, DATETIME('now', 'localtime'), ?)");

        //stmnt.setInt(1, v.getId());                   //ei tarvitse kun tulee automaattisesti
        stmnt.setString(2, v.getNimimerkki());
        stmnt.setInt(3, v.getViestiketju().getId());
        //stmnt.setTimestamp(4, v.getLahetysaika());    //ei tarvitse kun tulee automaattisesti
        stmnt.setString(4, v.getSisalto());

        stmnt.execute();
        connection.close();

        return new Viesti(v.getId(), v.getNimimerkki(), v.getViestiketju(), v.getLahetysaika(), v.getSisalto());
    }

}
