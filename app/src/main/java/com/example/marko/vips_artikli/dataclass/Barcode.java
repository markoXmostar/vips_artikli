package com.example.marko.vips_artikli.dataclass;

/**
 * Created by marko on 29.12.2017..
 */

public class Barcode {
    private long artiklId;

    public long getArtiklId() {
        return artiklId;
    }

    public void setArtiklId(long artiklId) {
        this.artiklId = artiklId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Barcode(long artiklId, String barcode) {

        this.artiklId = artiklId;
        this.barcode = barcode;
    }

    private String barcode;


}
