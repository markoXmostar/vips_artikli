package com.example.marko.vips_artikli;

import android.app.Activity;

/**
 * Created by marko on 10.1.2018..
 */

public class ArtiklJmj {
    private long ArtiklID;
    private long JmjID;
    private String nazivJMJ;
    public ArtiklJmj(long artiklID, long jmjID) {
        ArtiklID = artiklID;
        JmjID = jmjID;
        this.nazivJMJ = nazivJMJ;
    }

    public long getArtiklID() {
        return ArtiklID;
    }

    public void setArtiklID(long artiklID) {
        ArtiklID = artiklID;
    }

    public long getJmjID() {
        return JmjID;
    }

    public void setJmjID(long jmjID) {
        JmjID = jmjID;
    }


}
