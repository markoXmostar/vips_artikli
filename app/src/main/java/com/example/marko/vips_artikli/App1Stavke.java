package com.example.marko.vips_artikli;

import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by marko on 6.2.2018..
 */

public class App1Stavke implements Serializable {
    private long id;
    private long zaglavljeId;
    private long artiklId;
    private String artiklNaziv;
    private long jmjId;
    private String jmjNaziv;
    private boolean imaAtribut;
    private long atributId;
    private String atributNaziv;
    private String atributVrijednost;
    private double kolicina;
    private String napomena;

    private long vipsID;
    private int rbr;

    public int getRbr() {
        return rbr;
    }

    public void setRbr(int rbr) {
        this.rbr = rbr;
    }

    public long getVipsID() {
        return vipsID;
    }

    public void setVipsID(long vipsID) {
        this.vipsID = vipsID;
    }

    public App1Stavke(long id, long zaglavljeId, long artiklId, String artiklNaziv, long jmjId, String jmjNaziv,
                      boolean imaAtribut, long atributId, String atributNaziv, String atributVrijednost, double kolicina, String napomena) {
        this.id = id;
        this.zaglavljeId=zaglavljeId;
        this.artiklId = artiklId;
        this.artiklNaziv = artiklNaziv;
        this.jmjId = jmjId;
        this.jmjNaziv = jmjNaziv;
        this.imaAtribut = imaAtribut;
        this.atributId = atributId;
        this.atributNaziv = atributNaziv;
        this.atributVrijednost = atributVrijednost;
        this.kolicina = kolicina;
        this.napomena = napomena;
    }

    public long getId() {
        return id;
    }

    public long getZaglavljeId() {
        return zaglavljeId;
    }

    public void setZaglavljeId(long zaglavljeId) {
        this.zaglavljeId = zaglavljeId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getArtiklId() {
        return artiklId;
    }

    public void setArtiklId(long artiklId) {
        this.artiklId = artiklId;
    }

    public String getArtiklNaziv() {
        return artiklNaziv;
    }

    public void setArtiklNaziv(String artiklNaziv) {
        this.artiklNaziv = artiklNaziv;
    }

    public long getJmjId() {
        return jmjId;
    }

    public void setJmjId(long jmjId) {
        this.jmjId = jmjId;
    }

    public String getJmjNaziv() {
        return jmjNaziv;
    }

    public void setJmjNaziv(String jmjNaziv) {
        this.jmjNaziv = jmjNaziv;
    }

    public boolean isImaAtribut() {
        return imaAtribut;
    }

    public void setImaAtribut(boolean imaAtribut) {
        this.imaAtribut = imaAtribut;
    }


    public long getAtributId() {
        return atributId;
    }

    public void setAtributId(long atributId) {
        this.atributId = atributId;
    }


    public String getAtributNaziv() {
        return atributNaziv;
    }

    public void setAtributNaziv(String atributNaziv) {
        this.atributNaziv = atributNaziv;
    }


    public String getAtributVrijednost() {
        return atributVrijednost;
    }

    public void setAtributVrijednost(String atributVrijednost) {
        this.atributVrijednost = atributVrijednost;
    }

    public double getKolicina() {
        return kolicina;
    }

    public void setKolicina(double kolicina) {
        this.kolicina = kolicina;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
