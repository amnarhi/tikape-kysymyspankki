package tikape.kysymyspankki;

import java.util.ArrayList;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.kysymyspankki.database.Database;
import tikape.kysymyspankki.database.KysymysDao;
import tikape.kysymyspankki.database.VastausDao;
import tikape.kysymyspankki.domain.Kysymys;
import tikape.kysymyspankki.domain.Vastaus;

public class Main {

    public static void main(String[] args) throws Exception {

        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        Database database = new Database("jdbc:sqlite:kysymyspankki.db");
        database.init();

        KysymysDao kysymysDao = new KysymysDao(database);
        VastausDao vastausDao = new VastausDao(database);

        get("/", (req, res) -> { // näyttää sivun index
            HashMap map = new HashMap<>();

            map.put("kysymykset", kysymysDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/kysymys/:id", (req, res) -> { // näyttää kysymyksen sivun id:n perusteella
            HashMap map = new HashMap<>();
            Integer id = kysymysDao.findOne(Integer.parseInt(req.params("id"))).getId();
            map.put("kysymys", kysymysDao.findOne(Integer.parseInt(req.params("id"))));

            map.put("vastaukset", vastausDao.findAllWhere(id));
            return new ModelAndView(map, "kysymys");

        }, new ThymeleafTemplateEngine());

        post("/poista", (req, res) -> { // poistaa kysymyksen 
            Integer id = Integer.parseInt(req.queryParams("kysymys.id"));
            kysymysDao.delete(id);
            res.redirect("/");
            return "";
        });

        post("/kysymys/poista", (req, res) -> { // poistaa kysymykseen liittyvän vastausvaihtoehdon
            Integer kysymys_id = Integer.parseInt(req.queryParams("kysymys.id"));
            Integer vastaus_id = Integer.parseInt(req.queryParams("vastaus.id"));
            vastausDao.delete(vastaus_id);
            res.redirect("/kysymys/" + kysymys_id);
            return "";
        });

        post("/", (req, res) -> { // lisää uuden kysymyksen

            if (req.queryParams("kurssi").isEmpty() || req.queryParams("aihe").isEmpty() || req.queryParams("kysymysteksti").isEmpty()) {
                res.redirect("/");
                return "";
            }
            String kurssi = req.queryParams("kurssi");
            String aihe = req.queryParams(("aihe"));
            String kysymysteksti = req.queryParams("kysymysteksti");
            kysymysDao.saveOrUpdate(new Kysymys(null, kurssi, aihe, kysymysteksti, new ArrayList<>()));
            res.redirect("/");
            return "";

        });

        post("/kysymys", (req, res) -> { // lisää uuden vastausvaihtoehdon

            Integer kysymys_id = Integer.parseInt(req.queryParams("kysymys.id"));
            if (req.queryParams("vastausteksti") == null || req.queryParams("vastausteksti").isEmpty()) {
                res.redirect("/kysymys/" + kysymys_id);
                return "";
            }            
            String vastausteksti = req.queryParams("vastausteksti");
            Boolean oikein;
            if (req.queryParams("oikein") == null || req.queryParams("oikein").isEmpty()) {
                oikein = false;
            } else {
                oikein = true;
            }
            vastausDao.saveOrUpdate(new Vastaus(null, vastausteksti, oikein, kysymys_id));
            res.redirect("/kysymys/" + kysymys_id);
            return "";

        });

    }
}
