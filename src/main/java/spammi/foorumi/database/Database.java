package spammi.foorumi.database;

import java.sql.*;
import java.util.*;
import org.sqlite.SQLiteConfig;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        SQLiteConfig sqLiteConfig = new SQLiteConfig();
        Properties properties = sqLiteConfig.toProperties();
        properties.setProperty(SQLiteConfig.Pragma.DATE_STRING_FORMAT.pragmaName, "yyyy-MM-dd HH:mm:ss");
        return DriverManager.getConnection(databaseAddress, properties);
    }
    
    
    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }
    

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiskomennot
        lista.add("CREATE TABLE Alue (id integer PRIMARY KEY, otsikko varchar(50) NOT NULL);");
        lista.add("CREATE TABLE Viestiketju (id integer PRIMARY KEY, alue integer NOT NULL, "
                                + "aihe varchar(100) NOT NULL, FOREIGN KEY(alue) REFERENCES Alue(id));");
        lista.add("CREATE TABLE Viesti (id integer PRIMARY KEY, nimimerkki varchar(10), "
                                + "viestiketju integer NOT NULL,lahetysaika timestamp NOT NULL, "
                                + "sisalto varchar(250) NOT NULL, FOREIGN KEY(viestiketju) REFERENCES Viestiketju(id));");
        
        //testidataa tauluihin
        
        //Alueet
        lista.add("INSERT INTO Alue VALUES(1,'Preeriakoirat');");
        lista.add("INSERT INTO Alue VALUES(2,'Lasiesineet');");
        lista.add("INSERT INTO Alue VALUES(3,'Kaktukset');");
        lista.add("INSERT INTO Alue VALUES(4,'Rasvahapot');");
        lista.add("INSERT INTO Alue VALUES(5,'Aikuisten muskari');");
        
        //Viestiketjut
        lista.add("INSERT INTO Viestiketju VALUES(1,1,'Preeriakoira lemmikkinä');");
        lista.add("INSERT INTO Viestiketju VALUES(2,2,'toikalta katkesi nokka');");
        lista.add("INSERT INTO Viestiketju VALUES(3,1,'kellä muilla dramaattinen preeriakoira?');");
        lista.add("INSERT INTO Viestiketju VALUES(4,4,'Ovatko rasvahapot syövyttäviä?');");
        lista.add("INSERT INTO Viestiketju VALUES(5,5,'Marakassi kadonnut');");
        lista.add("INSERT INTO Viestiketju VALUES(6,1,'kellä muilla dramaattinen preeriakoira osa 2');");
        lista.add("INSERT INTO Viestiketju VALUES(7,1,'Kissa söi Viljon');");
        lista.add("INSERT INTO Viestiketju VALUES(8,1,'Liikaa karvaneritystä');");
        lista.add("INSERT INTO Viestiketju VALUES(9,1,'persut ei ole marsuja!!!');");
        lista.add("INSERT INTO Viestiketju VALUES(10,1,'Parhaat purut');");
        lista.add("INSERT INTO Viestiketju VALUES(11,1,'Valkohäntäpreeriakoira (Cynomys leucurus)');");
        lista.add("INSERT INTO Viestiketju VALUES(12,1,'Kokemuksia hukunestopannoista');");
        lista.add("INSERT INTO Viestiketju VALUES(13,1,'Villien ja vapaiden preeriakoirien puolesta!');");
        lista.add("INSERT INTO Viestiketju VALUES(14,1,'Erilaiset pesärakennelmat');");
        lista.add("INSERT INTO Viestiketju VALUES(15,3,'Onko vaarallista jos piikkejä irtoaa paljon?');");
        
        //viestit
        lista.add("INSERT INTO Viesti VALUES(1,'Erkki',4,'2017-02-19 00:20:54','Uskoakseni se riippuu, onko rasvahappoa kuumennettu ja minkälaisessa astiassa');");
        lista.add("INSERT INTO Viesti VALUES(2,'Kerttu',15,'2017-02-01 10:12:08','Jos kaktuksella ei ole yhtään piikkejä, niin sillä voi olla kylmä.');");
        lista.add("INSERT INTO Viesti VALUES(3,'Anna',15,'2017-02-01 13:13:13','Kaktukselle voi kääriä esim. vähän foliota ympärille.');");
        lista.add("INSERT INTO Viesti VALUES(4,'hagrid',14,'2017-02-08 14:31:02','Ikean huonekalut on hyviä pesiä varten.');");
        lista.add("INSERT INTO Viesti VALUES(5,'pirkitta',9,'2017-02-15 09:20:37','ärsyttää kun jotkut luulee mun preeriakoiraa marsuksi');");
        lista.add("INSERT INTO Viesti VALUES(6,'GumBula',9,'2017-02-16 10:20:30','eiks ne oo marsuja?');");
        lista.add("INSERT INTO Viesti VALUES(7,'dramatic',3,'2017-02-18 13:34:05','se chipmunk on oikeesti prairie dog!');");
        lista.add("INSERT INTO Viesti VALUES(8,'Sale',15,'2017-03-18 12:53:47','voihan se olla vaarallista');");
        lista.add("INSERT INTO Viesti VALUES(9,'Anonyymi',15,'2017-02-27 18:46:12','Ei ole koskaan ennen tullut mietittyä');");
        lista.add("INSERT INTO Viesti VALUES(10,'Jouluontaas',15,'2017-02-28 00:04:09','Joulukaktus ainakin kukkii :o)');");
        lista.add("INSERT INTO Viesti VALUES(11,'hihhhih',5,'2017-03-01 00:29:06','pitänee kysyä Eevalta, olisko Maralla ylimääräsiä..');");
        lista.add("INSERT INTO Viesti VALUES(12,'esko',15,'2017-03-01 00:29:56','Kaktus on paljon muutakin kuin piikkejä!');");
        lista.add("INSERT INTO Viesti VALUES(13,'pertti',15,'2017-03-01 01:00:56','Onko kyseessä Pereskioideae vai Maihuenioideae?');");
        lista.add("INSERT INTO Viesti VALUES(14,'angervo tikkanen',15,'2017-03-02 11:06:49','Eihän kutikula ole vaurioitunut?');");
        lista.add("INSERT INTO Viesti VALUES(15,'Kaktus',15,'2017-03-02 13:58:48','Piikkikuoreesi kätkeydyt, pakoon maailmaa. "
                                            + "Samalla suojaat itseäsi kolhuilta, niin sisäisiltä ja ulkoisilta. "
                                            + "Ilman kuortasi olikuitenkin suojaton. Olet sisäänpäin kääntynyt ihminen, "
                                            + "voisin verrata sinua kaktukseen');");
        lista.add("INSERT INTO Viesti VALUES(16,'HOHOHOHO',5,'2017-03-01 16:51:22','OLIPA HUONO VITSI!!!');");
        lista.add("INSERT INTO Viesti VALUES(17,'Martti',7,'2017-03-01 18:13:06','Tai ainaki luulen nii koska kissa haukkuu :( ');");
        lista.add("INSERT INTO Viesti VALUES(18,'Sale',1,'2017-02-03 17:15:21','Preksut on niiin söpöjä ^^');");
        lista.add("INSERT INTO Viesti VALUES(19,'Carlo',2,'2017-02-05 16:18:31','Sucks on you.');");
        lista.add("INSERT INTO Viesti VALUES(20,'Karvoissa',8,'2017-03-01 18:11:49','Vai miten ne karvat tulee?');");
        lista.add("INSERT INTO Viesti VALUES(21,'Kervinen',10,'2017-03-01 16:42:25','Käytän purtavaksi bambukeppejä?');");
        lista.add("INSERT INTO Viesti VALUES(22,'Frii4eva',13,'2017-03-01 16:17:28','Preeriakoirat kuuluvat vain ja ainoastaan luontoon. "
                                            + "Tässä lähennellään jo eläinrääkkäystä!');");
        lista.add("INSERT INTO Viesti VALUES(23,'wikimiki',15,'2017-03-02 14:06:49','Kaktukset tarvitsevat piikkejään suojaamaan itseään vettä etsiviltä eläimiltä.');");
        lista.add("INSERT INTO Viesti VALUES(24,'anselmi83',15,'2017-03-02 19:28:20','Kaktus on toteemieläimeni.');");

        return lista;
    }
    
   

    
}