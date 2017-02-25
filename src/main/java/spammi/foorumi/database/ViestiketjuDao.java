package spammi.foorumi.database;

import java.sql.*;
import java.util.*;
import spammi.foorumi.domain.*;

/**
 *
 * @author mari
 */
public class ViestiketjuDao implements Dao<Viestiketju, Integer> {

    private Database database;
    private AlueDao alueDao;
    private ViestiDao viestiDao;

    public ViestiketjuDao(Database database) {
        this.database = database;
    }

    @Override
    public Viestiketju findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement(
                "SELECT vk.id, vk.alue, vk.aihe, COUNT(v.id) AS viestienMaara, MAX(v.lahetysaika) AS viimeisinLahetysaika\n"
                + "FROM Viestiketju vk\n"
                + "LEFT JOIN Viesti v ON vk.id = v.viestiketju\n"
                + "WHERE vk.id = ?\n"
                + "GROUP BY vk.id, vk.alue, vk.aihe;");

        stmnt.setInt(1, key);
        ResultSet rs = stmnt.executeQuery();

        boolean on = rs.next();
        if (!on) {
            return null;
        }

        Viestiketju vk = new Viestiketju(
                rs.getInt("id"),
                rs.getString("aihe"),
                alueDao.findOne(rs.getInt("alue")),
                rs.getInt("viestienMaara"),
                rs.getTimestamp("viimeisinLahetysaika")
        );

        rs.close();
        stmnt.close();
        connection.close();

        return vk;
    }

    public List<Viestiketju> findByAlue(Alue alue) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement(
                "SELECT vk.id, vk.alue, vk.aihe, COUNT(v.id) AS viestienMaara, MAX(v.lahetysaika) AS viimeisinLahetysaika\n"
                + "FROM Viestiketju vk\n"
                + "LEFT JOIN Viesti v ON vk.id = v.viestiketju\n"
                + "WHERE vk.alue = ?\n"
                + "GROUP BY vk.id, vk.alue, vk.aihe;");

        stmnt.setInt(1, alue.getId());
        ResultSet rs = stmnt.executeQuery();
        List<Viestiketju> ketjut = new ArrayList();

        while (rs.next()) {
            ketjut.add(
                    new Viestiketju(
                            rs.getInt("id"),
                            rs.getString("aihe"),
                            alueDao.findOne(rs.getInt("alue")),
                            rs.getInt("viestienMaara"),
                            rs.getTimestamp("viimeisinLahetysaika")
                    )
            );
        }

        rs.close();
        stmnt.close();
        connection.close();

        return ketjut;

    }

    @Override
    public List<Viestiketju> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement(
                "SELECT vk.id, vk.alue, vk.aihe, COUNT(v.id) AS viestienMaara, MAX(v.lahetysaika) AS viimeisinLahetysaika\n"
                + "FROM Viestiketju vk\n"
                + "LEFT JOIN Viesti v ON vk.id = v.viestiketju\n"
                + "GROUP BY vk.id, vk.alue, vk.aihe;");

        ResultSet rs = stmnt.executeQuery();
        List<Viestiketju> ketjut = new ArrayList();

        while (rs.next()) {
            ketjut.add(
                    new Viestiketju(
                            rs.getInt("id"),
                            rs.getString("aihe"),
                            alueDao.findOne(rs.getInt("alue")),
                            rs.getInt("viestienMaara"),
                            rs.getTimestamp("viimeisinLahetysaika")
                    )
            );
        }

        rs.close();
        stmnt.close();
        connection.close();

        return ketjut;
    }

    public Timestamp findLatestLahetysaika(Alue alue) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Viestiketju vk, Viesti v ON v.viestiketju = vk.id WHERE vk.alue = ? ORDER BY v.lahetysaika DESC LIMIT 1");

        stmnt.setInt(1, alue.getId());
        ResultSet rs = stmnt.executeQuery();

        Timestamp viimeisin = null;

        while (rs.next()) {
            viimeisin = rs.getTimestamp("lahetysaika");
        }

        rs.close();
        stmnt.close();
        connection.close();

        return viimeisin;
    }

    @Override
    public void create(Viestiketju vk) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmnt = connection.prepareStatement("INSERT INTO Viestiketju (aihe, alue) VALUES (?, ?)");

//        Alue alue = vk.getAlue();
//        alue.lisaaViestiketjujenMaaraa();
        stmnt.setString(1, vk.getAihe());
        stmnt.setInt(2, vk.getAlue().getId());

        stmnt.execute();

        stmnt.close();
        connection.close();
    }

    public int countKetjujenViestit(Alue alue) throws SQLException {

        List<Viestiketju> ketjut = findByAlue(alue);

        int maara = 0;
        for (Viestiketju vk : ketjut) {
            maara += viestiDao.countViestit(vk);
        }

        return maara;
    }

    public void setAlueDao(AlueDao alueDao) {
        this.alueDao = alueDao;
    }

    public void setViestiDao(ViestiDao viestiDao) {
        this.viestiDao = viestiDao;
    }

}
