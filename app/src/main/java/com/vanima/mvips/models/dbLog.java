package com.vanima.mvips.models;

/**
 * Created by marko on 15.12.2017..
 */

public class dbLog {
    Integer id;
    String timestamp;
    Integer greska;
    String greskaMsg;
    Integer redniBroj;
    String tabela;

    public dbLog(Integer id, String timestamp, Integer greska, String greskaMsg,Integer redniBroj, String tabela) {
        this.id = id;
        this.timestamp = timestamp;
        this.greska = greska;
        this.greskaMsg = greskaMsg;
        this.redniBroj=redniBroj;
        this.tabela = tabela;
    }

    public Integer getRedniBroj() {
        return redniBroj;
    }

    public void setRedniBroj(Integer redniBroj) {
        this.redniBroj = redniBroj;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getGreska() {
        return greska;
    }

    public void setGreska(Integer greska) {
        this.greska = greska;
    }

    public String getGreskaMsg() {
        return greskaMsg;
    }

    public void setGreskaMsg(String greskaMsg) {
        this.greskaMsg = greskaMsg;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }
}
