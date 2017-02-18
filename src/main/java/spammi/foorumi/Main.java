package spammi.foorumi;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spammi.foorumi.database.Database;
import spammi.foorumi.database.OpiskelijaDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:spammitestidata.db"); 
        //database.init();
        
        TestiKayttis kayttis = new TestiKayttis(database);
        
        kayttis.naytaAlueet();
        System.out.println("");
        kayttis.naytaViestiketjut();
        System.out.println("");
        kayttis.viestiketjutAlueittain();
        System.out.println("");
        kayttis.lisaaAlueViestiketjuJaViesti();
        //kayttis.naytaViestit();
        

//        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);
//
//        get("/", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("viesti", "tervehdys");
//
//            return new ModelAndView(map, "index");
//        }, new ThymeleafTemplateEngine());
//
//        get("/opiskelijat", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelijat", opiskelijaDao.findAll());
//
//            return new ModelAndView(map, "opiskelijat");
//        }, new ThymeleafTemplateEngine());
//
//        get("/opiskelijat/:id", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));
//
//            return new ModelAndView(map, "opiskelija");
//        }, new ThymeleafTemplateEngine());
    }
}
