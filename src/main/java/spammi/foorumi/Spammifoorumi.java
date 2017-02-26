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

    private Database database;
    private AlueDao alueDao;
    private ViestiketjuDao vkDao;
    private ViestiDao viestiDao;

    public Spammifoorumi(Database database) {
        this.database = database;
        this.vkDao = new ViestiketjuDao(database);
        this.alueDao = new AlueDao(database);
        this.viestiDao = new ViestiDao(database);
        vkDao.setAlueDao(alueDao);
        vkDao.setViestiDao(viestiDao);
        viestiDao.setVkDao(vkDao);
        alueDao.setVkDao(vkDao);
    }

    public void kaynnista() {

        get("/", (req, res) -> {
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

        get("/alue/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));

            HashMap<String, Object> data = new HashMap();
            data.put("ketjut", vkDao.findByAlue(alueDao.findOne(id)));
            data.put("alueId", id);
            data.put("alue", alueDao.findOne(id)); //lisäsin tän, koska otsikkoon tarvii alueen nimen

            return new ModelAndView(data, "viestiketjut");
        }, new ThymeleafTemplateEngine());

        post("/alue/:id/viestiketju", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            int viestiketjuId = Integer.parseInt(req.queryParams("viestiketju"));

            if (!req.queryParams("viestiketju").isEmpty()) {
                vkDao.create(new Viestiketju(req.queryParams("viestiketju"), alueDao.findOne(id)));
            }

            res.redirect("/alue/" + id + "/viestiketju/" + viestiketjuId);
            return "";
        });

        get("/alue/:alueId/viestiketju/:viestiketjuId", (req, res) -> {
            int alueId = Integer.parseInt(req.params(":alueId"));
            int viestiketjuId = Integer.parseInt(req.params(":viestiketjuId"));

            HashMap<String, Object> data = new HashMap();
            data.put("viestit", viestiDao.findByViestiketju(vkDao.findOne(viestiketjuId)));
            data.put("alueId", alueId);
            data.put("viestiketjuId", viestiketjuId);
            data.put("alue", alueDao.findOne(alueId));
            data.put("viestiketju", vkDao.findOne(viestiketjuId));

            return new ModelAndView(data, "viestit");
        }, new ThymeleafTemplateEngine());

        post("alue/:alueId/viestiketju/:viestiketjuId", (req, res) -> {
            String nimimerkki = req.queryParams("nimimerkki").trim();
            String sisalto = req.queryParams("sisalto").trim();

            int alueId = Integer.parseInt(req.params(":alueId"));
            int viestiketjuId = Integer.parseInt(req.params(":viestiketjuId"));

            if (nimimerkki.isEmpty()) {
                nimimerkki = "Anonyymi";
            }

            if (!sisalto.isEmpty()) {
                Viesti viesti = new Viesti(nimimerkki, vkDao.findOne(viestiketjuId), sisalto);
                viestiDao.create(viesti);
            }

            res.redirect("/alue/" + alueId + "/viestiketju/" + viestiketjuId);
            return "";
        });

    }
}
