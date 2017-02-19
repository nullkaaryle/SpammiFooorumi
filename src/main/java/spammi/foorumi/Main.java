package spammi.foorumi;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spammi.foorumi.database.AlueDao;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import spammi.foorumi.database.Database;
import spammi.foorumi.database.OpiskelijaDao;
import spammi.foorumi.database.ViestiDao;
import spammi.foorumi.database.ViestiketjuDao;
import spammi.foorumi.domain.Alue;
import spammi.foorumi.domain.Viestiketju;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:spammitestidata.db"); 
        //database.init();
        
//        TestiKayttis kayttis = new TestiKayttis(database);
//        
//        kayttis.naytaAlueet();
//        System.out.println("");
//        kayttis.naytaViestiketjut();
//        System.out.println("");
//        kayttis.viestiketjutAlueittain();
//        System.out.println("");
//        kayttis.lisaaAlueViestiketjuJaViesti();
//        kayttis.naytaViestit();
        
        

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

        AlueDao alueDao = new AlueDao(database);
        ViestiDao viestiDao = new ViestiDao(database);
        ViestiketjuDao viestiketjuDao = new ViestiketjuDao(database);

        get("/",(req,res)->{
            HashMap<String,Object> data = new HashMap();
            data.put("alueet",alueDao.findAll());
            
            for (Alue alue : alueDao.findAll()){
                Integer maara = 0;
                
                for (Viestiketju viestiketju: viestiketjuDao.findByAlue(alue)){
                    maara += viestiDao.countViestit(viestiketju);
                }
                
                data.put(""+alue.getId(),""+maara);
            }
            
            return new ModelAndView(data,"Aihealueet");
        },new ThymeleafTemplateEngine());
    }
}
