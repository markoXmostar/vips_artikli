package com.vanima.mvips.models;

import java.util.List;

public class ArtiklSaKolicinom {
    private Artikl art;

    private double kolicina;

    private List<jmjOdnos> listaJMJ;

    public ArtiklSaKolicinom(Artikl art, double kolicina, List<jmjOdnos> listaJMJ) {
        this.art = art;
        this.kolicina = kolicina;
        this.listaJMJ = listaJMJ;
    }
/*
    public ArtiklSaKolicinom(Artikl _art, double _kolicina) {
        this.art = _art;
        this.kolicina = _kolicina;
    }
*/
    public double getKolicina() {
        return kolicina;
    }

    public void setKolicina(double kolicina) {
        this.kolicina = kolicina;
    }

    public Artikl getArt() {
        return art;
    }

    public void setArt(Artikl art) {
        this.art = art;
    }

    public List<jmjOdnos> getListaJMJ() {
        return listaJMJ;
    }

    public void setListaJMJ(List<jmjOdnos> listaJMJ) {
        this.listaJMJ = listaJMJ;
    }
}
