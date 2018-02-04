package com.example.marko.vips_artikli;

/**
 * Created by marko on 10.1.2018..
 */

public class ArtiklAtributStanje {

    private long artiklId;
    private long vrijednostId1;
    private String vrijednost1;
    private String atribut1;
    private double stanje;

    public ArtiklAtributStanje(long artiklId, long vrijednostId1, String vrijednost1, String atribut1, double stanje) {
        this.artiklId = artiklId;
        this.vrijednostId1 = vrijednostId1;
        this.vrijednost1 = vrijednost1;
        this.atribut1 = atribut1;
        this.stanje = stanje;
    }

    public long getArtiklId() {
        return artiklId;
    }

    public void setArtiklId(long artiklId) {
        this.artiklId = artiklId;
    }

    public long getVrijednostId1() {
        return vrijednostId1;
    }

    public void setVrijednostId1(long vrijednostId1) {
        this.vrijednostId1 = vrijednostId1;
    }

    public String getVrijednost1() {
        return vrijednost1;
    }

    public void setVrijednost1(String vrijednost1) {
        this.vrijednost1 = vrijednost1;
    }

    public String getAtribut1() {
        return atribut1;
    }

    public void setAtribut1(String atribut1) {
        this.atribut1 = atribut1;
    }

    public double getStanje() {
        return stanje;
    }

    public void setStanje(double stanje) {
        this.stanje = stanje;
    }

    public String toString(){
        return getVrijednost1();
    }
}
