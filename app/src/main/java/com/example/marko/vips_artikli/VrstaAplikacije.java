package com.example.marko.vips_artikli;

public enum VrstaAplikacije {
    App1("Aplikacija 1"),
    App2("Aplikacija 2"),
    App3("Aplikacija 3"),
    Sve("Sve");

    private String friendlyName;

    private VrstaAplikacije(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }

}
