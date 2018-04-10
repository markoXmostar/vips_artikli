package com.example.marko.vips_artikli;

/**
 * Created by marko on 10.4.2018..
 */

public class App2Stavke {
    //{"id":0,"zaglavljeId":501923,"rbr":1,"artiklId":170624,"jmjId":4,"vrijednostId1":0,"kolicina":1.00000,"opaska":null,"vipsId":5604939,"kolicinaZadana":1.00000}
    private long id, zaglavljeId, artiklId, jmjId, vrijednostId1, vipsId;
    private int rbr;
    private double kolicina, kolicinaZadana;
    private String opaska;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
