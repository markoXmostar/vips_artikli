package com.example.marko.vips_artikli;

/**
 * Created by rac157 on 14.12.2017.
 */

public class PodtipDokumenta {
    private long id;
    private String naziv;
    private long rid;

    public PodtipDokumenta(long id, String naziv, long rid) {
        this.id = id;
        this.naziv = naziv;
        this.rid = rid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }
}
