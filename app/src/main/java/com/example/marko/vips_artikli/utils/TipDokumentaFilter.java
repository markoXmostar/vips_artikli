package com.example.marko.vips_artikli.utils;

public enum TipDokumentaFilter{
    SVI("SVI"),
    Zakljuceni("Zaključeni"),
    Nezakljucen("Nezaključeni");

    private String friendlyName;
    TipDokumentaFilter(String friendlyName) {
        this.friendlyName = friendlyName;
    }
    @Override
    public String toString() {
        return friendlyName;
    }

}
