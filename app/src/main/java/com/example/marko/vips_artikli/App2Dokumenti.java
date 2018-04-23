package com.example.marko.vips_artikli;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by marko on 10.4.2018..
 */

public class App2Dokumenti {
    private static final String TAG = "dokumenti2";
    //{"id":null,"kasaId":null,"podtipId":51,"podtip":"Narudžbenica kupca - Pocket PC","pjFrmId":1,"pjFrm":"VP Čitluk","pjKmtId":15595,"pjKmt":"Sjedište","kmt":"Vanima d.o.o. - Mostar",
    // "datumDokumenta":"2018-04-05T00:00:00","komercijalistId":null,"komercijalist":"","nacinPlacanjaId":1,"nacinPlacanja":"Virman","opaska":"","vipsId":501923}
    int id;
    private long podtipId, kasaId, pjFrmId, pjKmtId, komercijalistaId, nacinPlacanjaId, vipsId;
    private Date datumDokumenta;
    private Date datumSinkronizacije;
    private String opaska, podtipNaziv, pjFrmNaziv, pjKmtNaziv, KmtNaziv, komercijalistNaziv, nacinPlacanjaNaziv;
    private boolean zavrsen;
    private List<App2Stavke> spisakStavki;

    public App2Dokumenti(int id, long kasaId, long podtipId, String podtipNaziv, long pjFrmId, String pjFrmNaziv, long pjKmtId, String pjKmtNaziv, String kmtNaziv, Date DatumDokumenta, Date DatumSinkronizacije,
                         long komercijalistaId, String komercijalistNaziv, long nacinPlacanjaId, String nacinPlacanjaNaziv, String opaska, long vipsId, boolean zavrsen) {
        this.id = id;
        this.podtipId = podtipId;
        this.kasaId = kasaId;
        this.pjFrmId = pjFrmId;
        this.pjKmtId = pjKmtId;
        this.komercijalistaId = komercijalistaId;
        this.nacinPlacanjaId = nacinPlacanjaId;
        this.vipsId = vipsId;
        this.podtipNaziv = podtipNaziv;
        this.pjFrmNaziv = pjFrmNaziv;
        this.pjKmtNaziv = pjKmtNaziv;
        this.KmtNaziv = kmtNaziv;
        this.komercijalistNaziv = komercijalistNaziv;
        this.nacinPlacanjaNaziv = nacinPlacanjaNaziv;
        //Log.d(TAG, "App2Dokumenti: DATUM U JSON FORMATU JE:" + strDatumDokumenta);
        //2018-04-05T00:00:00
        this.datumDokumenta = DatumDokumenta;
        this.datumSinkronizacije = DatumSinkronizacije;
        this.opaska = opaska;
        this.zavrsen = zavrsen;

        spisakStavki = new ArrayList<App2Stavke>();
    }

    public List<App2Stavke> getSpisakStavki() {
        return spisakStavki;
    }

    public void izbrisiSveStavke() {
        spisakStavki.clear();
    }

    public void doadajStavku(App2Stavke stavka) {
        spisakStavki.add(stavka);
    }

    public boolean isZavrsen() {
        return zavrsen;
    }

    public void setZavrsen(boolean zavrsen) {
        this.zavrsen = zavrsen;
    }

    public String getPodtipNaziv() {
        return podtipNaziv;
    }

    public void setPodtipNaziv(String podtipNaziv) {
        this.podtipNaziv = podtipNaziv;
    }

    public String getPjFrmNaziv() {
        return pjFrmNaziv;
    }

    public void setPjFrmNaziv(String pjFrmNaziv) {
        this.pjFrmNaziv = pjFrmNaziv;
    }

    public String getPjKmtNaziv() {
        return pjKmtNaziv;
    }

    public void setPjKmtNaziv(String pjKmtNaziv) {
        this.pjKmtNaziv = pjKmtNaziv;
    }

    public String getKmtNaziv() {
        return KmtNaziv;
    }

    public void setKmtNaziv(String kmtNaziv) {
        KmtNaziv = kmtNaziv;
    }

    public String getKomercijalistNaziv() {
        return komercijalistNaziv;
    }

    public void setKomercijalistNaziv(String komercijalistNaziv) {
        this.komercijalistNaziv = komercijalistNaziv;
    }

    public String getNacinPlacanjaNaziv() {
        return nacinPlacanjaNaziv;
    }

    public void setNacinPlacanjaNaziv(String nacinPlacanjaNaziv) {
        this.nacinPlacanjaNaziv = nacinPlacanjaNaziv;
    }


    public void setSpisakStavki(List<App2Stavke> spisakStavki) {
        this.spisakStavki = spisakStavki;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPodtipId() {
        return podtipId;
    }

    public void setPodtipId(long podtipId) {
        this.podtipId = podtipId;
    }

    public long getKasaId() {
        return kasaId;
    }

    public void setKasaId(long kasaId) {
        this.kasaId = kasaId;
    }

    public long getPjFrmId() {
        return pjFrmId;
    }

    public void setPjFrmId(long pjFrmId) {
        this.pjFrmId = pjFrmId;
    }

    public long getPjKmtId() {
        return pjKmtId;
    }

    public void setPjKmtId(long pjKmtId) {
        this.pjKmtId = pjKmtId;
    }

    public long getKomercijalistaId() {
        return komercijalistaId;
    }

    public void setKomercijalistaId(long komercijalistaId) {
        this.komercijalistaId = komercijalistaId;
    }

    public long getNacinPlacanjaId() {
        return nacinPlacanjaId;
    }

    public void setNacinPlacanjaId(long nacinPlacanjaId) {
        this.nacinPlacanjaId = nacinPlacanjaId;
    }

    public long getVipsId() {
        return vipsId;
    }

    public void setVipsId(long vipsId) {
        this.vipsId = vipsId;
    }

    public Date getDatumDokumenta() {
        return datumDokumenta;
    }

    public String getDatumDokumentaString() {
        return DateFormat.format(MainActivity.DatumFormat, datumDokumenta).toString();
    }

    public String getDatumSinkronizacijeString() {
        return DateFormat.format(MainActivity.DatumFormat, datumSinkronizacije).toString();
    }

    public void setDatumDokumenta(Date datumDokumenta) {
        this.datumDokumenta = datumDokumenta;
    }

    public Date getDatumSinkronizacije() {
        return datumSinkronizacije;
    }

    public void setDatumSinkronizacije(Date datumSinkronizacije) {
        this.datumSinkronizacije = datumSinkronizacije;
    }

    public String getOpaska() {
        return opaska;
    }

    public void setOpaska(String opaska) {
        this.opaska = opaska;
    }

    @Override
    public String toString() {
        return "App2Dokumenti{" +
                "id=" + id +
                ", podtipId=" + podtipId +
                ", kasaId=" + kasaId +
                ", pjFrmId=" + pjFrmId +
                ", pjKmtId=" + pjKmtId +
                ", komercijalistaId=" + komercijalistaId +
                ", nacinPlacanjaId=" + nacinPlacanjaId +
                ", vipsId=" + vipsId +
                ", datumDokumenta=" + datumDokumenta +
                ", datumSinkronizacije=" + datumSinkronizacije +
                ", opaska='" + opaska + '\'' +
                ", podtipNaziv='" + podtipNaziv + '\'' +
                ", pjFrmNaziv='" + pjFrmNaziv + '\'' +
                ", pjKmtNaziv='" + pjKmtNaziv + '\'' +
                ", KmtNaziv='" + KmtNaziv + '\'' +
                ", komercijalistNaziv='" + komercijalistNaziv + '\'' +
                ", nacinPlacanjaNaziv='" + nacinPlacanjaNaziv + '\'' +
                ", zavrsen=" + zavrsen +
                ", spisakStavki=" + spisakStavki +
                '}';
    }
}
