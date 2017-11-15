package com.example.marko.vips_artikli;

/**
 * Created by marko on 1.11.2017.
 */

public class Artikl {
    private long id;
    private String sifra, naziv, kataloskiBroj, jmj, kratkiOpis, proizvodjac, dugiOpis, vrstaAmbalaze;
    private double brojKoleta, brojKoletaNaPaleti, stanje, vpc, mpc, netto, brutto;
    private boolean imaRokTrajanja;
    private int podgrupaID;

    public Artikl(long id, String sifra, String naziv, String kataloskiBroj, String jmj, String kratkiOpis,
                  String proizvodjac, String dugiOpis, String vrstaAmbalaze, double brojKoleta, double brojKoletaNaPaleti,
                  double stanje, double vpc, double mpc, double netto, double brutto, boolean imaRokTrajanja, int podgrupaID) {
        this.id = id;
        this.sifra = sifra;
        this.naziv = naziv;
        this.kataloskiBroj = kataloskiBroj;
        this.jmj = jmj;
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
        return kataloskiBroj;
    }

    public void setKataloskiBroj(String kataloskiBroj) {
        this.kataloskiBroj = kataloskiBroj;
    }

    public String getJmj() {
        return jmj;
    }

    public void setJmj(String jmj) {
        this.jmj = jmj;
    }

    public String getKratkiOpis() {
        return kratkiOpis;
    }

    public void setKratkiOpis(String kratkiOpis) {
        this.kratkiOpis = kratkiOpis;
    }

    public String getProizvodjac() {
        return proizvodjac;
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
