package com.vanima.mvips.models;

/**
 * Created by marko on 10.1.2018..
 */

public class ArtiklJmj {
    private long ArtiklID;
    private long JmjID;
    private String nazivJMJ;


    private String nazivArtikla;

    public ArtiklJmj(long artiklID, long jmjID, String nazivArt, String nazivJM) {
        ArtiklID = artiklID;
        JmjID = jmjID;
        nazivArtikla = nazivArt;
        nazivJMJ = nazivJM;

    }

    public long getArtiklID() {
        return ArtiklID;
    }

    public void setArtiklID(long artiklID) {
        ArtiklID = artiklID;
    }

    public long getJmjID() {
        return JmjID;
    }

    public void setJmjID(long jmjID) {
        JmjID = jmjID;
    }

    public String getNazivJMJ() {
        return nazivJMJ;
    }

    public void setNazivJMJ(String nazivJMJ) {
        this.nazivJMJ = nazivJMJ;
    }

    public String getNazivArtikla() {
        return nazivArtikla;
    }

    public void setNazivArtikla(String nazivArtikla) {
        this.nazivArtikla = nazivArtikla;
    }

    public String toString() {
        if (nazivJMJ.equals("")) {
            return super.toString();
        } else {
            return nazivJMJ;
        }
    }
}
