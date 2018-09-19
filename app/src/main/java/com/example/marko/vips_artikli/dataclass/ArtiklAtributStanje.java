package com.example.marko.vips_artikli.dataclass;

/**
 * Created by marko on 10.1.2018..
 */

public class ArtiklAtributStanje {

    private long artiklId;
    private long atributId1;
    private String atributNaziv1;
    private String atributVrijednost1;
    private double stanje;

    public ArtiklAtributStanje(long artiklId, long atributId1, String atributNaziv1, String atributVrijednost1, double stanje) {
        this.artiklId = artiklId;
        this.atributId1 = atributId1;
        this.atributNaziv1 = atributNaziv1;
        this.atributVrijednost1 = atributVrijednost1;
        this.stanje = stanje;
    }

    public long getArtiklId() {
        return artiklId;
    }

    public void setArtiklId(long artiklId) {
        this.artiklId = artiklId;
    }

    public long getAtributId1() {
        return atributId1;
    }

    public void setAtributId1(long atributId1) {
        this.atributId1 = atributId1;
    }

    public String getAtributNaziv1() {
        return atributNaziv1;
    }

    public void setAtributNaziv1(String vrijednostNaziv1) {
        this.atributNaziv1 = vrijednostNaziv1;
    }

    public String getAtributVrijednost1() {
        return atributVrijednost1;
    }

    public void setAtributVrijednost1(String atributVrijednost1) {
        this.atributVrijednost1 = atributVrijednost1;
    }

    public double getStanje() {
        return stanje;
    }

    public void setStanje(double stanje) {
        this.stanje = stanje;
    }

    public String toString(){
        return getAtributNaziv1();
    }
}
