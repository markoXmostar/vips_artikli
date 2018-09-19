package com.example.marko.vips_artikli.dataclass;

/**
 * Created by rac157 on 12.12.2017.
 */

public class jmj {
    /*
    "id":16,"naziv":"dan"
     */
    private long id;
    private String naziv;

    public jmj(long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }


}
