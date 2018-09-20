package com.example.marko.vips_artikli.models;

import java.io.Serializable;

/**
 * Created by marko on 10.4.2018..
 */

public class App2Stavke implements Serializable {
    //{"id":0,"zaglavljeId":501923,"rbr":1,"artiklId":170624,"jmjId":4,"vrijednostId1":0,"kolicina":1.00000,"opaska":null,"vipsId":5604939,"kolicinaZadana":1.00000}
    //{"id":0,"zaglavljeId":501923,"rbr":1,"artiklId":170624,"artiklSifra":"03947","artikl":" BACVA 50L","jmjId":4,"jmj":"kom","vrijednostId1":0,"vrijednost":"","atribut":"","kolicina":1.00000,"opaska":null,"vipsId":5604939,"kolicinaZadana":1.00000}
    int id;
    private long zaglavljeId, artiklId, jmjId, vrijednostId1, vipsId;
    private int rbr;
    private double kolicina, kolicinaZadana;
    private String opaska, artiklSifra, artiklNaziv, jmjNaziv, vrijednostNaziv, atributNaziv;

    public App2Stavke(int id, long zaglavljeId, long artiklId, long jmjId, long vrijednostId1, long vipsId, int rbr, double kolicina, double kolicinaZadana, String opaska, String artiklSifra, String artiklNaziv, String jmjNaziv, String vrijednostNaziv, String atributNaziv) {
        this.id = id;
        this.zaglavljeId = zaglavljeId;
        this.artiklId = artiklId;
        this.jmjId = jmjId;
        this.vrijednostId1 = vrijednostId1;
        this.vipsId = vipsId;
        this.rbr = rbr;
        this.kolicina = kolicina;
        this.kolicinaZadana = kolicinaZadana;
        this.opaska = opaska;
        this.artiklSifra = artiklSifra;
        this.artiklNaziv = artiklNaziv;
        this.jmjNaziv = jmjNaziv;
        this.vrijednostNaziv = vrijednostNaziv;
        this.atributNaziv = atributNaziv;
    }

    public int getId() {
        return id;
    }

    public String getArtiklSifra() {
        return artiklSifra;
    }

    public void setArtiklSifra(String artiklSifra) {
        this.artiklSifra = artiklSifra;
    }

    public String getArtiklNaziv() {
        return artiklNaziv;
    }

    public void setArtiklNaziv(String artiklNaziv) {
        this.artiklNaziv = artiklNaziv;
    }

    public String getJmjNaziv() {
        return jmjNaziv;
    }

    public void setJmjNaziv(String jmjNaziv) {
        this.jmjNaziv = jmjNaziv;
    }

    public String getVrijednostNaziv() {

        return vrijednostNaziv;
    }

    public void setVrijednostNaziv(String vrijednostNaziv) {

        this.vrijednostNaziv = vrijednostNaziv;
    }

    public String getAtributNaziv() {

        return atributNaziv;
    }

    public void setAtributNaziv(String atributNaziv) {
        this.atributNaziv = atributNaziv;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getZaglavljeId() {
        return zaglavljeId;
    }

    public void setZaglavljeId(long zaglavljeId) {
        this.zaglavljeId = zaglavljeId;
    }

    public long getArtiklId() {
        return artiklId;
    }

    public void setArtiklId(long artiklId) {
        this.artiklId = artiklId;
    }

    public long getJmjId() {
        return jmjId;
    }

    public void setJmjId(long jmjId) {
        this.jmjId = jmjId;
    }

    public long getVrijednostId1() {
        return vrijednostId1;
    }

    public void setVrijednostId1(long vrijednostId1) {
        this.vrijednostId1 = vrijednostId1;
    }

    public long getVipsId() {
        return vipsId;
    }

    public void setVipsId(long vipsId) {
        this.vipsId = vipsId;
    }

    public int getRbr() {
        return rbr;
    }

    public void setRbr(int rbr) {
        this.rbr = rbr;
    }

    public double getKolicina() {
        return kolicina;
    }

    public void setKolicina(double kolicina) {
        this.kolicina = kolicina;
    }

    public double getKolicinaZadana() {
        return kolicinaZadana;
    }

    public void setKolicinaZadana(double kolicinaZadana) {
        this.kolicinaZadana = kolicinaZadana;
    }

    public String getOpaska() {
        return opaska;
    }

    public void setOpaska(String opaska) {
        this.opaska = opaska;
    }

    @Override
    public String toString() {
        return "App2Stavke{" +
                "id=" + id +
                ", zaglavljeId=" + zaglavljeId +
                ", artiklId=" + artiklId +
                ", jmjId=" + jmjId +
                ", vrijednostId1=" + vrijednostId1 +
                ", vipsId=" + vipsId +
                ", rbr=" + rbr +
                ", kolicina=" + kolicina +
                ", kolicinaZadana=" + kolicinaZadana +
                ", opaska='" + opaska + '\'' +
                ", artiklSifra='" + artiklSifra + '\'' +
                ", artiklNaziv='" + artiklNaziv + '\'' +
                ", jmjNaziv='" + jmjNaziv + '\'' +
                ", vrijednostNaziv='" + vrijednostNaziv + '\'' +
                ", atributNaziv='" + atributNaziv + '\'' +
                '}';
    }
}
