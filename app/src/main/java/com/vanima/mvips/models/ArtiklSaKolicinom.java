package com.vanima.mvips.models;

public class ArtiklSaKolicinom {
    private Artikl art;

    private double kolicina;

    public ArtiklSaKolicinom(Artikl _art, double _kolicina) {
        this.art = _art;
        this.kolicina = _kolicina;
    }

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


}
