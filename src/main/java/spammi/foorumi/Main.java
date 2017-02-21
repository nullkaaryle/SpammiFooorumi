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
import spammi.foorumi.domain.Viesti;
import spammi.foorumi.domain.Viestiketju;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:spammitestidata.db"); 
        //database.init();
        
        
        TestiKayttis kayttis = new TestiKayttis(database);
        kayttis.testaa();
        
        

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
        
        

        get("/",(req,res)->{  //puuttuu vielä viestien ajat ja viestien määrä ei toimi toivotusti
            HashMap<String,Object> data = new HashMap();
            data.put("alueet",alueDao.findAll());
            
            for (Alue alue : alueDao.findAll()){
                int maara = 0;
                
                for (Viestiketju viestiketju: viestiketjuDao.findByAlue(alue)){
                    maara += viestiDao.countViestit(viestiketju);
                }
                
                data.put(""+alue.getId(),""+maara);
            }
            
            return new ModelAndView(data,"Aihealueet");
        },new ThymeleafTemplateEngine());
        
        
        post("/alue",(req,res) ->{
            if(!req.queryParams("alue").isEmpty()){
                
                Alue alue = new Alue(alueDao.newId(),req.queryParams("alue"));
                alueDao.create(alue);
            }
            res.redirect("/");
            return "";
        });
        
        get("/alue/:id",(req,res)->{                       //ei toimi vielä kunnolla
            int id = Integer.parseInt(req.params(":id"));
            HashMap<String,Object> data = new HashMap();
            List<Viestiketju> ketjut = new ArrayList();
            
            for(Viestiketju viestiketju : viestiketjuDao.findAll()){
                if(viestiketju.getAlue().getId()== id){
                    ketjut.add(viestiketju);
                }
            }
            data.put("ketjut",ketjut);
            return new ModelAndView(data,"viestiketjut");
        },new ThymeleafTemplateEngine());
    }
}
