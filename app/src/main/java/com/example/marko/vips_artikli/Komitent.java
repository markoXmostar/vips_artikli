package com.example.marko.vips_artikli;

/**
 * Created by rac157 on 15.12.2017.
 */

public class Komitent  {
    private long id;
    private String sifra;
    private String naziv;

    public Komitent(long id, String sifra, String naziv) {
        this.id = id;
        this.sifra = sifra;
        this.naziv = naziv;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}
