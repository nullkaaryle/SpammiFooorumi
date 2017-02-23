
package spammi.foorumi;

import java.util.HashMap;
import spammi.foorumi.database.*;
import spammi.foorumi.domain.*;
import spark.ModelAndView;
import static spark.Spark.get;
import static spark.Spark.post;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 *
 * @author mari
 */
public class Spammifoorumi {
    
    Database database;
    AlueDao alueDao;
    ViestiketjuDao vkDao;
    ViestiDao viestiDao;

    public Spammifoorumi(Database database) {
        this.database = database;
        this.alueDao = new AlueDao(database);
        this.viestiDao = new ViestiDao(database);
        this.vkDao = new ViestiketjuDao(database);
    }
    
    public void kaynnista(){
        
        get("/",(req,res)->{  //puuttuu vielä viestien ajat ja viestien määrä ei toimi toivotusti
            HashMap<String,Object> data = new HashMap();
            data.put("alueet",alueDao.findAll());
            HashMap<String,Integer> maarat = new HashMap();
            
            for (Alue alue : alueDao.findAll()){
                int maara = 0;
                
                for (Viestiketju viestiketju: vkDao.findByAlue(alue)){
                    maara += viestiDao.countViestit(viestiketju);
                }
                maarat.put(alue.getId()+"",maara);
            }
            
            data.put("maarat",maarat);
            return new ModelAndView(data,"aihealueet");
        },new ThymeleafTemplateEngine());
        
        
        post("/alue",(req,res) ->{
            if(!req.queryParams("alue").isEmpty()){
                
                Alue alue = new Alue(req.queryParams("alue"));
                alueDao.create(alue);
            }
            res.redirect("/");
            return "";
        });
        
        post("/alue/:id",(req,res)->{
            //ei tiedetä toimiiko
            System.out.println(req.queryParams(":id"));
            int id = Integer.parseInt(req.params(":id"));
            
            if(!req.queryParams("viestiketju").isEmpty()){
                vkDao.create(new Viestiketju(req.queryParams("viestiketju"), alueDao.findOne(id)));
            }
            
            res.redirect("/alue/" + id);
            return "";
        });
        
        get("/alue/:id",(req,res)->{                       //ei toimi vielä kunnolla
            int id = Integer.parseInt(req.params(":id"));
            HashMap<String,Object> data = new HashMap();
            
            data.put("ketjut",vkDao.findByAlue(alueDao.findOne(id)));
            return new ModelAndView(data,"viestiketjut");
        },new ThymeleafTemplateEngine());
    }
    
    
}
