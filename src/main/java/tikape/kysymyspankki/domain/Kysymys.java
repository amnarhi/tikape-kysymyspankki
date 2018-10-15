
package tikape.kysymyspankki.domain;

import java.util.List;


public class Kysymys {
    
    private Integer id;
    private String kurssi;
    private String aihe;
    private String kysymysteksti;
    private List<Vastaus> vastaukset;

    public Kysymys(Integer id, String kurssi, String aihe, String kysymysteksti, List<Vastaus> vastaukset) {
        this.id = id;
        this.kurssi = kurssi;
        this.aihe = aihe;
        this.kysymysteksti = kysymysteksti;
        this.vastaukset = vastaukset;
    }

   

    public Integer getId() {
        return id;
    }

    public String getKurssi() {
        return kurssi;
    }

    public String getAihe() {
        return aihe;
    }

    public String getKysymysteksti() {
        return kysymysteksti;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setKurssi(String kurssi) {
        this.kurssi = kurssi;
    }

    public void setAihe(String aihe) {
        this.aihe = aihe;
    }

    public void setKysymysteksti(String kysymysteksti) {
        this.kysymysteksti = kysymysteksti;
    }

    public List<Vastaus> getVastaukset() {
        return vastaukset;
    }

    public void setVastaukset(List<Vastaus> vastaukset) {
        this.vastaukset = vastaukset;
    }

    

    
}
