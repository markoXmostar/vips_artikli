package com.vanima.mvips.models;

/**
 * Created by rac157 on 25.4.2018.
 */

public class AsortimanKupca {
    private long pjID;
    private long artiklID;

    public AsortimanKupca(long pjID, long artiklID) {
        this.pjID = pjID;
        this.artiklID = artiklID;
    }

    public long getPjID() {
        return pjID;
    }

    public void setPjID(long pjID) {
        this.pjID = pjID;
    }

    public long getArtiklID() {
        return artiklID;
    }

    public void setArtiklID(long artiklID) {
        this.artiklID = artiklID;
    }
}
