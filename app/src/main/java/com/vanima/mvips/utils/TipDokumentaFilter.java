package com.vanima.mvips.utils;

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
