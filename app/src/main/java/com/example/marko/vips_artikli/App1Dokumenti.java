package com.example.marko.vips_artikli;

import java.util.Date;

/**
 * Created by rac157 on 21.12.2017.
 */

public class App1Dokumenti {
    private long id;
    private long idTip;
    private long idPodtip;
    private long idKomitent;
    private long idPjKomitenta;
    private Date datumDokumenta;
    private Date datumSinkronizacije;
    private String napomena;

    public App1Dokumenti(long id, long idTip, long idPodtip, long idKomitent, long idPjKomitenta, Date datumDokumenta, Date datumSinkronizacije, String napomena) {
        this.id = id;
        this.idTip = idTip;
        this.idPodtip = idPodtip;
        this.idKomitent = idKomitent;
        this.idPjKomitenta = idPjKomitenta;
        this.datumDokumenta = datumDokumenta;
        this.datumSinkronizacije = datumSinkronizacije;
        this.napomena = napomena;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdTip() {
        return idTip;
    }

    public void setIdTip(long idTip) {
        this.idTip = idTip;
    }

    public long getIdPodtip() {
        return idPodtip;
    }

    public void setIdPodtip(long idPodtip) {
        this.idPodtip = idPodtip;
    }

    public long getIdKomitent() {
        return idKomitent;
    }

    public void setIdKomitent(long idKomitent) {
        this.idKomitent = idKomitent;
    }

    public long getIdPjKomitenta() {
        return idPjKomitenta;
    }

    public void setIdPjKomitenta(long idPjKomitenta) {
        this.idPjKomitenta = idPjKomitenta;
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

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
