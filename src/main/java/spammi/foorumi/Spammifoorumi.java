package spammi.foorumi;

import java.util.HashMap;
import spammi.foorumi.database.*;
import spammi.foorumi.domain.*;
import spark.ModelAndView;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 * @author mari, ninna, pinni
 */
public class Spammifoorumi {

    private Database database;
    private AlueDao alueDao;
    private ViestiketjuDao vkDao;
    private ViestiDao viestiDao;
    private int sivunumero;

    public Spammifoorumi(Database database) {
        this.database = database;
        this.vkDao = new ViestiketjuDao(database);
        this.alueDao = new AlueDao(database);
        this.viestiDao = new ViestiDao(database);
        setDaot();
        this.sivunumero = 0;
    }

    public void kaynnista() {

        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

//ALUE        
        //JUURIPOLKU, NÄYTTÄÄ KAIKKI TIETOKANNASSA OLEVAT ALUEET
        get("/", (req, res) -> {
            HashMap<String, Object> data = new HashMap();
            data.put("alueet", alueDao.findAll());

            return new ModelAndView(data, "aihealueet");
        }, new ThymeleafTemplateEngine());

        //LUO TIETOKANTAAN UUDEN ALUEEN
        //UUDEN ALUEEN NIMI TULEE PYYNNÖN PARAMETRINA NIMELTÄ "alue"
        //UUDELLEENOHJAA JUURIPOLKUUN
        post("/alue", (req, res) -> {

            if (!req.queryParams("alue").isEmpty()) {
                Alue alue = new Alue(req.queryParams("alue"));
                alueDao.create(alue);
            }

            res.redirect("/");
            return "";
        });

//TIETYN ALUEEN VIESTIKETJUT        
        
        //NÄYTTÄÄ KAIKKI :alueId-TUNNUKSELLA MERKITYN ALUEEN VIESTIKETJUT
        get("/alue/:alueId", (req, res) -> {
            int alueId = Integer.parseInt(req.params(":alueId"));

            HashMap<String, Object> data = new HashMap();
            data.put("ketjut", vkDao.findByAlue(alueDao.findOne(alueId)));
            data.put("alue", alueDao.findOne(alueId)); //lisäsin tän, koska otsikkoon tarvii alueen nimen

            return new ModelAndView(data, "viestiketjut");
        }, new ThymeleafTemplateEngine());
        
        get("/alue/:id/sivu/:sivunumero", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            sivunumero = Integer.parseInt(req.params(":sivunumero"));
           
            if (sivunumero < 0 || req.params(":sivunumero").isEmpty()) {
                sivunumero = 0;
            }

            HashMap<String, Object> data = new HashMap();
            data.put("ketjut", vkDao.findNextTen(alueDao.findOne(id), sivunumero * 10));
            data.put("alue", alueDao.findOne(id));
            data.put("sivunumeroS", sivunumero + 1);
            data.put("sivunumeroE", sivunumero - 1);

            return new ModelAndView(data, "viestiketjut");
        }, new ThymeleafTemplateEngine());
        
        post("/alue/:id/sivu/:sivunumero", (req, res) -> {

            res.redirect("/alue/:id/sivu/:sivunumero");
            return "";
        });

        post("/alue/:id/viestiketju", (req, res) -> {  //ei toimi,ei löydä viestiketjun Id:tä ja kommentoitu id on musta aika mahoton
            int id = Integer.parseInt(req.params(":id"));
//            int viestiketjuId = Integer.parseInt(req.queryParams("viestiketju"));
            String nimimerkki = req.queryParams("nimimerkki").trim();
            String sisalto = req.queryParams("sisalto").trim();

            if (!(req.queryParams("viestiketju").isEmpty() && req.queryParams("sisalto").isEmpty())) {
                Viestiketju ketju = new Viestiketju(req.queryParams("viestiketju"), alueDao.findOne(id));
                vkDao.create(ketju);

                if (nimimerkki.isEmpty()) {
                    nimimerkki = "Anonyymi";
                }

                Viestiketju vk = vkDao.findViimeisin();
                Viesti viesti = new Viesti(nimimerkki, vk, sisalto);
                viestiDao.create(viesti);

                res.redirect("/alue/" + id + "/viestiketju/" + vk.getId());
            }
//            res.redirect("/alue/" + id + "/viestiketju/" + ketju.getId());
            return "";
        });

        
//TIETYN ALUEEN TIETYN VIESTIKETJUN VIESTIT
        
        //NÄYTTÄÄ KAIKKI :alueId-TUNNUKSELLA MERKITYN ALUEEN
        //:viestiketjuId-TUNNUKSELLA MERKITYT VIESTIKETJUN VIESTIT
        get("/alue/:alueId/viestiketju/:viestiketjuId", (req, res) -> {
            int alueId = Integer.parseInt(req.params(":alueId"));
            int viestiketjuId = Integer.parseInt(req.params(":viestiketjuId"));

            HashMap<String, Object> data = new HashMap();
            data.put("viestit", viestiDao.findByViestiketju(vkDao.findOne(viestiketjuId)));
            data.put("alue", alueDao.findOne(alueId));
            data.put("viestiketju", vkDao.findOne(viestiketjuId));

            return new ModelAndView(data, "viestit");
        }, new ThymeleafTemplateEngine());

        //LUO TIETOKANTAAN UUDEN VIESTIN
        //:alueId-MERKITYN ALUEEN :viestiketjuId-MERKITTYYN VIESTIKETJUUN
        //UUDELLEENOHJAA KYS. VIESTIKETJUN SIVULLE
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

    private void setDaot() {
        vkDao.setAlueDao(alueDao);
        vkDao.setViestiDao(viestiDao);
        viestiDao.setVkDao(vkDao);
        alueDao.setVkDao(vkDao);
    }
}
