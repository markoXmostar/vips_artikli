package com.example.marko.vips_artikli;

public enum VrstaAplikacije {
    Sve("Sve"),
    App1("Aplikacija 1"),
    App2("Aplikacija 2"),
    App3("Aplikacija 3");


    private String friendlyName;

    private VrstaAplikacije(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }

}
