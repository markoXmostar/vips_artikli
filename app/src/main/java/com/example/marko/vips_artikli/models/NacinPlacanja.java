package com.example.marko.vips_artikli.models;

/**
 * Created by rac157 on 14.12.2017.
 */

public class NacinPlacanja {
    private long id;
    private String naziv;


    public NacinPlacanja(long id, String naziv) {
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

    @Override
    public String toString() {
        /*
        return "NacinPlacanja{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                '}';
        */
        return naziv;
    }
}
