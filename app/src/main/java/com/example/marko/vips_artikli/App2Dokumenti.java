package com.example.marko.vips_artikli;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by marko on 10.4.2018..
 */

public class App2Dokumenti {
    //{"id":null,"kasaId":null,"podtipId":51,"pjFrmId":1,"pjKmtId":15595,"datumDokumenta":"2018-04-05T00:00:00","komercijalistId":null,"nacinPlacanjaId":1,"opaska":"","vipsId":501923}
    private long id, podtipId, kasaId, pjFrmId, pjKmtId, komercijalistaId, nacinPlacanjaId, vipsId;
    private Date datumDokumenta;
    private Date datumSinkronizacije;
    private String opaska;

    private List<App2Stavke> spisakStavki;

    public App2Dokumenti(long id, long kasaId, long podtipId, long pjFrmId, long pjKmtId, String datumDokumenta, long komercijalistaId, long nacinPlacanjaId, String opaska, long vipsId) {
        this.id = id;
        this.podtipId = podtipId;
        this.kasaId = kasaId;
        this.pjFrmId = pjFrmId;
        this.pjKmtId = pjKmtId;
        this.komercijalistaId = komercijalistaId;
        this.nacinPlacanjaId = nacinPlacanjaId;
        this.vipsId = vipsId;
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
