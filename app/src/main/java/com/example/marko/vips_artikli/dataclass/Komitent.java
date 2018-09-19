package com.example.marko.vips_artikli.dataclass;

import java.io.Serializable;

/**
 * Created by rac157 on 15.12.2017.
 */

public class Komitent implements Serializable {
    private long id;
    private String sifra;
    private String naziv;
    //"saldo":0.00,"uRoku":0.00,"vanRoka":0.00
    private double saldo, uRoku, vanRoka;

    public Komitent(long id, String sifra, String naziv, double saldo, double uRoku, double vanRoka) {
        this.id = id;
        this.sifra = sifra;
        this.naziv = naziv;
        this.saldo = saldo;
        this.uRoku = uRoku;
        this.vanRoka = vanRoka;
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

    public double getSaldo() {
        return saldo;
    }

    public double getuRoku() {
        return uRoku;
    }

    public double getVanRoka() {
        return vanRoka;
    }
}
