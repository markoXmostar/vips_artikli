package com.vanima.mvips.models;

public enum VrstaAplikacije {
    Sve("Sve"),
    App1("Aplikacija 1"),
    App2("Aplikacija 2"),
    App3("Aplikacija 3");


    private String friendlyName;

    VrstaAplikacije(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }

}

