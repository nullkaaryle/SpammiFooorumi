package spammi.foorumi.database;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import spammi.foorumi.domain.*;

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
<<<<<<< HEAD

        while (rs.next()) {
            viestit.add(new Viesti(rs.getInt("id"),
                    rs.getString("nimimerkki"),
                    (vkDao.findOne(rs.getInt("viestiketju"))),
                    rs.getTimestamp("lahetysaika"),
                    rs.getString("sisalto")));
=======
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while (rs.next()) {
            try {
                viestit.add(new Viesti(rs.getInt("id"),
                        rs.getString("nimimerkki"),
                        (vkDao.findOne(rs.getInt("viestiketju"))),
                        formatter.parse(rs.getString("lahetysaika")),
                        rs.getString("sisalto")));
            } catch (ParseException ex) {
                Logger.getLogger(ViestiDao.class.getName()).log(Level.SEVERE, null, ex);
            }
>>>>>>> b2a987cd35a6c74cb6bcaefda6ae51db1569755e
        }

        rs.close();
        stmnt.close();
        connection.close();

        return viestit;
    }
    
    public List<Viesti> findByViestiketju(Viestiketju ketju) throws SQLException{
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Viesti WHERE Viestiketju = ?");
        
        stmnt.setInt(1, ketju.getId());
        ResultSet rs = stmnt.executeQuery();
        List<Viesti> viestit = new ArrayList();
        
        while(rs.next()){
            viestit.add(new Viesti(rs.getInt("id"),
                        rs.getString("nimimerkki"),
                        vkDao.findOne(rs.getInt("viestiketju")),
                        rs.getTimestamp("lahetysaika"),
                        rs.getString("sisalto")));
        }
        return viestit;
    }

    @Override
    public Viesti create(Viesti v) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("INSERT INTO Viesti (nimimerkki, viestiketju, lahetysaika, sisalto) VALUES (?, ?, DATETIME('now', 'localtime'), ?)");

        stmnt.setString(1, v.getNimimerkki());
        stmnt.setInt(2, v.getViestiketju().getId());
        stmnt.setString(3, v.getSisalto());

        stmnt.execute();
        connection.close();

        return new Viesti(v.getNimimerkki(), v.getViestiketju(), v.getSisalto());
    }

    public int countViestit(Viestiketju viestiketju) throws SQLException {
        Connection connection = database.getConnection();

        PreparedStatement stmnt = connection.prepareStatement("SELECT COUNT(nimimerkki) AS maara FROM Viesti WHERE viestiketju= '" + viestiketju.getId() + "'");
        ResultSet rs = stmnt.executeQuery();
        int maara = Integer.parseInt(rs.getString("maara"));

        rs.close();
        stmnt.close();
        connection.close();

        return maara;
    }

}
