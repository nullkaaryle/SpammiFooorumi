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
    private int alueenSisainenSivunumero;
    private int ketjunSisainenSivunumero;
    private int offset = 0;

    public Spammifoorumi(Database database) {
        this.database = database;
        this.vkDao = new ViestiketjuDao(database);
        this.alueDao = new AlueDao(database);
        this.viestiDao = new ViestiDao(database);
        setDaot();
        this.alueenSisainenSivunumero = 1;
        this.ketjunSisainenSivunumero = 1;
    }

    public void kaynnista() {

        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

//ALUE        
        //JUURIPOLKU, NÄYTTÄÄ KAIKKI TIETOKANNASSA OLEVAT ALUEET
        get("/", (req, res) -> {
            this.alueenSisainenSivunumero = 1;
            HashMap<String, Object> data = new HashMap();
            data.put("alueet", alueDao.findAll());

            return new ModelAndView(data, "aihealueet");
        }, new ThymeleafTemplateEngine());

        //LUO TIETOKANTAAN UUDEN ALUEEN JONKA NIMI SAADAAN
        //PYYNNÖN PARAMETRINA NIMELTÄ "alue"
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
        //NÄYTTÄÄ :alueId-TUNNISTEELLA MERKITYN ALUEEN VIESTIKETJUT
        //JOISTA ON VALITTU NÄYTETTÄVÄKSI TIETYT 10 KETJUA :sivunumero-TUNNISTEEN AVULLA 
        get("/alue/:alueId/sivu/:sivunumero", (req, res) -> {
            int alueId = Integer.parseInt(req.params(":alueId"));

            int sivunro = Integer.parseInt(req.params(":sivunumero"));
            this.offset = (alueenSisainenSivunumero - 1) * 10;

            HashMap<String, Object> data = new HashMap();
            data.put("ketjut", vkDao.findNextTen(alueDao.findOne(alueId), offset));
            data.put("alue", alueDao.findOne(alueId));

            return new ModelAndView(data, "viestiketjut");
        }, new ThymeleafTemplateEngine());

        //PIENENTÄÄ SIVUNUMEROA YHDELLÄ JA UUDELLEENOHJAA :alueId-TUNNISTEELLA
        //MERKITYN ALUEEN VIESTIKETJULISTAUKSEN EDELLISELLE SIVULLE
        post("/alue/:alueId/edellinen", (req, res) -> {
            int alueId = Integer.parseInt(req.params(":alueId"));
            this.alueenSisainenSivunumero--;

            if (this.alueenSisainenSivunumero < 1) {
                this.alueenSisainenSivunumero = 1;
            }

            res.redirect("/alue/" + alueId + "/sivu/" + this.alueenSisainenSivunumero);
            return "";
        });

        //SUURENTAA SIVUNUMEROA YHDELLÄ JA UUDELLEENOHJAA :alueId-TUNNISTEELLA
        //MERKITYN ALUEEN VIESTIKETJULISTAUKSEN SEURAAVALLE SIVULLE
        post("/alue/:alueId/seuraava", (req, res) -> {
            int alueId = Integer.parseInt(req.params(":alueId"));

            Alue alue = alueDao.findOne(alueId);
            int viestiketjujenMaara = alue.getVkMaara();
            double tarvittavaSivumaara = Math.ceil(viestiketjujenMaara / 10);

            if(viestiketjujenMaara%10 >=1){  //en ehtiny testaamaa toimiiko
                tarvittavaSivumaara ++;
            }
            
            if (this.alueenSisainenSivunumero < tarvittavaSivumaara) {
                this.alueenSisainenSivunumero++;
            }
            

            res.redirect("/alue/" + alueId + "/sivu/" + this.alueenSisainenSivunumero);
            return "";
        });

        //LUO :alueId-TUNNISTEELLA MERKITYLLE ALUEELLE UUDEN VIESTIKETJUN
        //TOIMII RIIPPUMATTA SIITÄ MILLÄ VIESTIKETJULISTAUKSEN SIVULLA OLLAAN
        post("/alue/:alueId/viestiketju", (req, res) -> {
            int alueId = Integer.parseInt(req.params(":alueId"));
            String nimimerkki = req.queryParams("nimimerkki");

            if (nimimerkki.isEmpty()) {
                nimimerkki = "Anonyymi";
            }

            String viestiketjunOtsikko = req.queryParams("viestiketju").trim();
            String sisalto = req.queryParams("sisalto").trim();

            if (viestiketjunOtsikko.equals("") || sisalto.equals("")) {
                res.redirect("/alue/" + alueId + "/sivu/" + 1);
                return "";
            }

            if (!viestiketjunOtsikko.isEmpty() && !sisalto.isEmpty()) {
                vkDao.create(new Viestiketju(viestiketjunOtsikko, alueDao.findOne(alueId)));
            }

            Viestiketju viestiketju = vkDao.findViimeisin();
            int viestiketjuId = viestiketju.getId();

            if (!sisalto.isEmpty()) {
                Viesti uusiViesti = new Viesti(nimimerkki, viestiketju, sisalto);
                viestiDao.create(uusiViesti);
            }

            res.redirect("/alue/" + alueId + "/viestiketju/" + viestiketjuId+"/sivu/1");
            return "";
        });

//TIETYN ALUEEN TIETYN VIESTIKETJUN VIESTIT

        //NÄYTTÄÄ KAIKKI :alueId-TUNNUKSELLA MERKITYN ALUEEN
        //:viestiketjuId-TUNNUKSELLA MERKITYT VIESTIKETJUN VIESTIT 
        get("/alue/:alueId/viestiketju/:viestiketjuId/sivu/:sivunumero", (req, res) -> {
            int alueId = Integer.parseInt(req.params(":alueId"));
            int viestiketjuId = Integer.parseInt(req.params(":viestiketjuId"));
            int sivunumero = Integer.parseInt(req.params(":sivunumero"));
            this.offset = (ketjunSisainenSivunumero - 1) * 10;

            HashMap<String, Object> data = new HashMap();
            data.put("viestit", viestiDao.findNextTen(vkDao.findOne(viestiketjuId), offset));

            data.put("alue", alueDao.findOne(alueId));
            data.put("viestiketju", vkDao.findOne(viestiketjuId));
            data.put("sivunumero", sivunumero);
            return new ModelAndView(data, "viestit");
        }, new ThymeleafTemplateEngine());

        //PIENENTÄÄ SIVUNUMEROA YHDELLÄ JA UUDELLEENOHJAA :alueId-TUNNISTEELLA
        //MERKITYN ALUEEN :viestiketjuId-TUNNISTEEN MERKITTYYN 
        //VIESTIKETJULISTAUKSEN SEURAAVALLE SIVULLE
        post("/alue/:alueId/viestiketju/:viestiketjuId/edellinen", (req, res) -> {
            int alueId = Integer.parseInt(req.params(":alueId"));
            int vkId = Integer.parseInt(req.params(":viestiketjuId"));
            this.ketjunSisainenSivunumero--;

            if (this.ketjunSisainenSivunumero < 1) {
                this.ketjunSisainenSivunumero = 1;
            }

            res.redirect("/alue/" + alueId + "/viestiketju/" + vkId + "/sivu/" + this.alueenSisainenSivunumero);
            return "";
        });

        //SUURENTAA SIVUNUMEROA YHDELLÄ JA UUDELLEENOHJAA :alueId-TUNNISTEELLA
        //MERKITYN ALUEEN :viestiketjuId-TUNNISTEEN MERKITTYYN 
        //VIESTIKETJULISTAUKSEN SEURAAVALLE SIVULLE
        post("/alue/:alueId/viestiketju/:viestiketjuId/seuraava", (req, res) -> {
            int alueId = Integer.parseInt(req.params(":alueId"));
            int vkId = Integer.parseInt(req.params(":viestiketjuId"));

            Viestiketju viestiketju = vkDao.findOne(vkId);
            int viestienMaara = viestiketju.getvMaara();
            double tarvittavaSivumaara = Math.ceil(viestienMaara / 10);
            
            if(viestienMaara%10 >=1){
                tarvittavaSivumaara ++;
            }

            if (this.ketjunSisainenSivunumero < tarvittavaSivumaara) {
                this.ketjunSisainenSivunumero++;
            }

            res.redirect("/alue/" + alueId + "/viestiketju/" + vkId + "/sivu/" + this.ketjunSisainenSivunumero);
            return "";
        });

        //LUO TIETOKANTAAN UUDEN VIESTIN
        //:alueId-MERKITYN ALUEEN :viestiketjuId-MERKITTYYN VIESTIKETJUUN
        //UUDELLEENOHJAA KYS. VIESTIKETJUN SIVULLE
        post("alue/:alueId/viestiketju/:viestiketjuId/sivu/:sivunumero/viesti", (req, res) -> {
            String nimimerkki = req.queryParams("nimimerkki").trim();
            String sisalto = req.queryParams("sisalto").trim();

            int alueId = Integer.parseInt(req.params(":alueId"));
            int viestiketjuId = Integer.parseInt(req.params(":viestiketjuId"));
            int sivunumero = Integer.parseInt(req.params(":sivunumero"));

            if (nimimerkki.isEmpty()) {
                nimimerkki = "Anonyymi";
            }

            if (!sisalto.isEmpty()) {
                Viesti viesti = new Viesti(nimimerkki, vkDao.findOne(viestiketjuId), sisalto);
                viestiDao.create(viesti);
            }

            res.redirect("/alue/" + alueId + "/viestiketju/" + viestiketjuId+ "/sivu/"+sivunumero);
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
