package com.example.marko.vips_artikli;

/**
 * Created by marko on 10.1.2018..
 */

public class ArtiklJmj {
    private long ArtiklID;
    private long JmjID;

    public ArtiklJmj(long artiklID, long jmjID) {
        ArtiklID = artiklID;
        JmjID = jmjID;
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
