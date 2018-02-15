package com.example.marko.vips_artikli;

public enum VrstaPretrageArtikala {
    PretragaPoArtiklu("Pretraga po artiklu"),
    PretragaPoBarcodu("Pretraga po BARCODU"),
    PretragaKamerom("Pretraga kamerom");


    private String friendlyName;

    private VrstaPretrageArtikala(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
