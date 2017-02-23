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

    public void kaynnista() {

        get("/", (req, res) -> {  //puuttuu vielä viestien ajat, viestien määrä ehkä toimii
            HashMap<String, Object> data = new HashMap();
            data.put("alueet", alueDao.findAll());
         
            return new ModelAndView(data, "aihealueet");
        }, new ThymeleafTemplateEngine());

        post("/alue", (req, res) -> {
            if (!req.queryParams("alue").isEmpty()) {

                Alue alue = new Alue(req.queryParams("alue"));
                alueDao.create(alue);
            }
            res.redirect("/");
            return "";
        });

        post("/alue/:id/viestiketju", (req, res) -> {
            //pitäisi toimia, ongelma kai html:ssä
            int id = Integer.parseInt(req.params(":id"));

            if (!req.queryParams("viestiketju").isEmpty()) {
                vkDao.create(new Viestiketju(req.queryParams("viestiketju"), alueDao.findOne(id)));
            }

            res.redirect("/alue/" + id);
            return "";
        });

        get("/alue/:id", (req, res) -> { 
            int id = Integer.parseInt(req.params(":id"));
            HashMap<String, Object> data = new HashMap();

            data.put("ketjut", vkDao.findByAlue(alueDao.findOne(id)));
            return new ModelAndView(data, "viestiketjut");
        }, new ThymeleafTemplateEngine());
    }

}
