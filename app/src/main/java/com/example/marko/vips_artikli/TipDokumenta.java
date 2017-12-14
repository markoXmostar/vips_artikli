package com.example.marko.vips_artikli;

/**
 * Created by rac157 on 14.12.2017.
 */

public class TipDokumenta {
    private long id;
    private String naziv;

    public TipDokumenta(long id, String naziv) {
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
