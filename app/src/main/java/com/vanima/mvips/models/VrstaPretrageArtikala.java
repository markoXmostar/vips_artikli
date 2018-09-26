package com.vanima.mvips.models;

public enum VrstaPretrageArtikala {
    PretragaPoArtiklu("Pretraga po artiklu"),
    PretragaPoBarcodu("Pretraga po BARCODU"),
    PretragaKamerom("Pretraga kamerom");


    private String friendlyName;

    VrstaPretrageArtikala(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
