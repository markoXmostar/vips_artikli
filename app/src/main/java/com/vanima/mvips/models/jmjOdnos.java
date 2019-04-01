package com.vanima.mvips.models;

public class jmjOdnos {
    private long jmjId;
    private double odnos;
    private String jmjNaziv;

    public jmjOdnos(long jmjId, double odnos, String jmjNaziv) {
        this.jmjId = jmjId;
        this.odnos = odnos;
        this.jmjNaziv = jmjNaziv;
    }

    public long getJmjId() {
        return jmjId;
    }

    public void setJmjId(long jmjId) {
        this.jmjId = jmjId;
    }

    public double getOdnos() {
        return odnos;
    }

    public void setOdnos(double odnos) {
        this.odnos = odnos;
    }

    public String getJmjNaziv() {
        return jmjNaziv;
    }

    public void setJmjNaziv(String jmjNaziv) {
        this.jmjNaziv = jmjNaziv;
    }

    @Override
    public String toString() {
        return  jmjNaziv;
    }
}
