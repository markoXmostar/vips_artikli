package com.example.marko.vips_artikli.dataclass;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by marko on 1.11.2017.
 */

public class Artikl  implements Serializable{
    private long id, jmjId;
    private String sifra;
    private String naziv;
    private String kataloskiBroj;

    public String getJmjNaziv() {
        return jmjNaziv;
    }

    public void setJmjNaziv(String jmjNaziv) {
        this.jmjNaziv = jmjNaziv;
    }

    private String jmjNaziv;
    private String kratkiOpis;
    private String proizvodjac;
    private String dugiOpis;
    private String vrstaAmbalaze;
    private double brojKoleta, brojKoletaNaPaleti, stanje, vpc, mpc, netto, brutto;
    private boolean imaRokTrajanja;
    private int podgrupaID;

    public Artikl(long id, String sifra, String naziv, String kataloskiBroj, long jmjId, String jmjNaziv, String kratkiOpis,
                  String proizvodjac, String dugiOpis, String vrstaAmbalaze, double brojKoleta, double brojKoletaNaPaleti,
                  double stanje, double vpc, double mpc, double netto, double brutto, boolean imaRokTrajanja, int podgrupaID) {
        this.id = id;
        this.sifra = sifra;
        this.naziv = naziv;
        this.kataloskiBroj = kataloskiBroj;
        this.jmjId = jmjId;
        this.jmjNaziv = jmjNaziv;
        this.kratkiOpis = kratkiOpis;
        this.proizvodjac = proizvodjac;
        this.dugiOpis = dugiOpis;
        this.vrstaAmbalaze = vrstaAmbalaze;
        this.brojKoleta = brojKoleta;
        this.brojKoletaNaPaleti = brojKoletaNaPaleti;
        this.stanje = stanje;
        this.vpc = vpc;
        this.mpc = mpc;
        this.netto = netto;
        this.brutto = brutto;
        this.imaRokTrajanja = imaRokTrajanja;
        this.podgrupaID = podgrupaID;
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

        return  naziv;

    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getKataloskiBroj() {
        String pro="";
        if (TextUtils.isEmpty(proizvodjac)){
            pro="N/A";
        }else
        {
            pro=kataloskiBroj;
        }
        return pro;
    }

    public void setKataloskiBroj(String kataloskiBroj) {
        this.kataloskiBroj = kataloskiBroj;
    }

    public long getJmjId() {
        return jmjId;
    }

    public void setJmjId(long jmjId) {
        this.jmjId = jmjId;
    }

    public String getKratkiOpis() {
        return kratkiOpis;
    }

    public void setKratkiOpis(String kratkiOpis) {
        this.kratkiOpis = kratkiOpis;
    }

    public String getProizvodjac() {
        String pro="";
        if (TextUtils.isEmpty(proizvodjac)){
            pro="N/A";
        }else
        {
            pro=proizvodjac;
        }
        return pro;
    }

    public void setProizvodjac(String proizvodjac) {
        this.proizvodjac = proizvodjac;
    }

    public String getDugiOpis() {
        return dugiOpis;
    }

    public void setDugiOpis(String dugiOpis) {
        this.dugiOpis = dugiOpis;
    }

    public String getVrstaAmbalaze() {
        return vrstaAmbalaze;
    }

    public void setVrstaAmbalaze(String vrstaAmbalaze) {
        this.vrstaAmbalaze = vrstaAmbalaze;
    }

    public double getBrojKoleta() {
        return brojKoleta;
    }

    public void setBrojKoleta(double brojKoleta) {
        this.brojKoleta = brojKoleta;
    }

    public double getBrojKoletaNaPaleti() {
        return brojKoletaNaPaleti;
    }

    public void setBrojKoletaNaPaleti(double brojKoletaNaPaleti) {
        this.brojKoletaNaPaleti = brojKoletaNaPaleti;
    }

    public double getStanje() {
        return stanje;
    }

    public void setStanje(double stanje) {
        this.stanje = stanje;
    }

    public double getVpc() {
        return vpc;
    }

    public void setVpc(double vpc) {
        this.vpc = vpc;
    }

    public double getMpc() {
        return mpc;
    }

    public void setMpc(double mpc) {
        this.mpc = mpc;
    }

    public double getNetto() {
        return netto;
    }

    public void setNetto(double netto) {
        this.netto = netto;
    }

    public double getBrutto() {
        return brutto;
    }

    public void setBrutto(double brutto) {
        this.brutto = brutto;
    }

    public boolean isImaRokTrajanja() {
        return imaRokTrajanja;
    }

    public int getImaRokTrajanja() {
        if (imaRokTrajanja) {
            return 1;
        }else{
            return 0;
        }

    }

    public void setImaRokTrajanja(boolean imaRokTrajanja) {
        this.imaRokTrajanja = imaRokTrajanja;
    }

    public int getPodgrupaID() {
        return podgrupaID;
    }

    public void setPodgrupaID(int podgrupaID) {
        this.podgrupaID = podgrupaID;
    }
}
