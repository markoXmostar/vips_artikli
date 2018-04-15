package com.example.marko.vips_artikli;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by marko on 10.4.2018..
 */

public class App2Dokumenti {
    private static final String TAG = "dokumenti2";
    //{"id":null,"kasaId":null,"podtipId":51,"podtip":"Narudžbenica kupca - Pocket PC","pjFrmId":1,"pjFrm":"VP Čitluk","pjKmtId":15595,"pjKmt":"Sjedište","kmt":"Vanima d.o.o. - Mostar",
    // "datumDokumenta":"2018-04-05T00:00:00","komercijalistId":null,"komercijalist":"","nacinPlacanjaId":1,"nacinPlacanja":"Virman","opaska":"","vipsId":501923}
    private long id, podtipId, kasaId, pjFrmId, pjKmtId, komercijalistaId, nacinPlacanjaId, vipsId;
    private Date datumDokumenta;
    private Date datumSinkronizacije;
    private String opaska, podtipNaziv, pjFrmNaziv, pjKmtNaziv, KmtNaziv, komercijalistNaziv, nacinPlacanjaNaziv;

    private List<App2Stavke> spisakStavki;

    public App2Dokumenti(long id, long kasaId, long podtipId, String podtipNaziv, long pjFrmId, String pjFrmNaziv, long pjKmtId, String pjKmtNaziv, String kmtNaziv, String datumDokumenta,
                         long komercijalistaId, String komercijalistNaziv, long nacinPlacanjaId, String nacinPlacanjaNaziv, String opaska, long vipsId) {
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
        Log.d(TAG, "App2Dokumenti: StringDatum=" + datumDokumenta);
        SimpleDateFormat format = new SimpleDateFormat(MainActivity.BorisovFormatDatuma);
        Date date = null;
        try {
            date = format.parse(datumDokumenta);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.datumDokumenta = date;
        this.datumSinkronizacije = datumSinkronizacije;
        this.opaska = opaska;
        this.spisakStavki = spisakStavki;
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

    public List<App2Stavke> getSpisakStavki() {
        return spisakStavki;
    }

    public void setSpisakStavki(List<App2Stavke> spisakStavki) {
        this.spisakStavki = spisakStavki;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
