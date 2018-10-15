package tikape.kysymyspankki.domain;

public class Vastaus {

    private Integer id;
    private String vastausteksti;
    private Boolean oikein;
    private Integer kysymys_id;
    private String oikeinSuomeksi;

    public Vastaus(Integer id, String vastausteksti, Boolean oikein, Integer kysymys_id) {
        this.id = id;
        this.vastausteksti = vastausteksti;
        this.oikein = oikein;
        this.kysymys_id = kysymys_id;
        if (this.oikein) {
            this.oikeinSuomeksi = "Oikein";
        } else {
            this.oikeinSuomeksi = "Väärin";
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVastausteksti() {
        return vastausteksti;
    }

    public void setVastausteksti(String vastausteksti) {
        this.vastausteksti = vastausteksti;
    }

    public Boolean getOikein() {
        return oikein;
    }

    public void setOikein(Boolean oikein) {
        this.oikein = oikein;
        if (this.oikein) {
            this.oikeinSuomeksi = "Oikein";
        } else {
            this.oikeinSuomeksi = "Väärin";
        }
    }

    public Integer getKysymys_id() {
        return kysymys_id;
    }

    public void setKysymys_id(Integer kysymys_id) {
        this.kysymys_id = kysymys_id;
    }

    public String getOikeinSuomeksi() {
        return oikeinSuomeksi;
    }

}
