package com.example.marko.vips_artikli.models;

import android.text.format.DateFormat;

import com.example.marko.vips_artikli.activities.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rac157 on 21.12.2017.
 */

public class App1Dokumenti {
    private long id;
    private long idTip;
    private long idPodtip;
    private long idKomitent;
    private long idPjKomitenta;
    private Date datumDokumenta;
    private Date datumSinkronizacije;
    private String napomena;
    private long idNacinPlacanja;
    private String nacinPlacanjaNaziv;
    private boolean zakljucen;


    public long getIdNacinPlacanja() {
        return idNacinPlacanja;
    }

    public void setIdNacinPlacanja(long idNacinPlacanja) {
        this.idNacinPlacanja = idNacinPlacanja;
    }

    public String getNacinPlacanjaNaziv() {
        return nacinPlacanjaNaziv;
    }

    public void setNacinPlacanjaNaziv(String nacinPlacanjaNaziv) {
        this.nacinPlacanjaNaziv = nacinPlacanjaNaziv;
    }

    private int vrstaAplikacije;

    public int getVrstaAplikacije() {
        return vrstaAplikacije;
    }

    public void setVrstaAplikacije(int vrstaAplikacije) {
        this.vrstaAplikacije = vrstaAplikacije;
    }

    private long vipsId;

    public long getVipsId() {
        return vipsId;
    }

    public void setVipsId(long vipsId) {
        this.vipsId = vipsId;
    }

    private String KomitentNaziv,PjKomitentNaziv,TipDokumentaNaziv,PodtipDokumentaNaziv;
    
    private List<App1Stavke> spisakStavki;
    
    public void doadajStavku(App1Stavke stavka){
        spisakStavki.add(stavka);
    }
    
    public void izbrisiStavku(long id){
        App1Stavke stavkaZaBrisanje=null;
        for (App1Stavke stavka:spisakStavki) {
            if (stavka.getId()==id){
                stavkaZaBrisanje=stavka;
            }
        }
        spisakStavki.remove(stavkaZaBrisanje);
    }

    public List<App1Stavke> getSpisakStavki(){
        return spisakStavki;
    }

    public void izbrisiSveStavke(){
        spisakStavki.clear();
    }

    public App1Dokumenti(long id, long idTip, long idPodtip, long idKomitent, long idPjKomitenta, Date datumDokumenta, Date datumSinkronizacije, String napomena,
                         String komitentNaziv, String pjKomitentNaziv, String tipDokumentaNaziv, String podtipDokumentaNaziv, long idNacinPlacanja, String nacinPlacanjaNAziv, boolean zakljucen) {
        this.id = id;
        this.idTip = idTip;
        this.idPodtip = idPodtip;
        this.idKomitent = idKomitent;
        this.idPjKomitenta = idPjKomitenta;
        this.datumDokumenta = datumDokumenta;
        this.datumSinkronizacije = datumSinkronizacije;
        this.napomena = napomena;
        this.KomitentNaziv = komitentNaziv;
        this.PjKomitentNaziv = pjKomitentNaziv;
        this.TipDokumentaNaziv = tipDokumentaNaziv;
        this.PodtipDokumentaNaziv = podtipDokumentaNaziv;
        this.idNacinPlacanja = idNacinPlacanja;
        this.nacinPlacanjaNaziv = nacinPlacanjaNAziv;
        this.zakljucen=zakljucen;

        spisakStavki=new ArrayList<App1Stavke>();
        
    }

    public int getBrojStavki(){
        int broj=0;
        broj=getSpisakStavki().size();
        return broj;
    }
    public double getSumaStavki(){
        double suma=0d;
        for (App1Stavke stv:spisakStavki) {
            double kol=stv.getKolicina();
            double cijena=stv.getVpc();
            suma+=kol*cijena; //sada ne vraćam dobro treba naći način da se vrati vpc ili mpc artikla;
        }
        return suma;
    }

    public boolean isZakljucen() {
        return zakljucen;
    }

    public void setZakljucen(boolean zakljucen) {
        this.zakljucen = zakljucen;
    }

    public String getKomitentNaziv() {
        return KomitentNaziv;
    }

    public void setKomitentNaziv(String komitentNaziv) {
        KomitentNaziv = komitentNaziv;
    }

    public String getPjKomitentNaziv() {
        return PjKomitentNaziv;
    }

    public void setPjKomitentNaziv(String pjKomitentNaziv) {
        PjKomitentNaziv = pjKomitentNaziv;
    }

    public String getTipDokumentaNaziv() {
        return TipDokumentaNaziv;
    }

    public void setTipDokumentaNaziv(String tipDokumentaNaziv) {
        TipDokumentaNaziv = tipDokumentaNaziv;
    }

    public String getPodtipDokumentaNaziv() {
        return PodtipDokumentaNaziv;
    }

    public void setPodtipDokumentaNaziv(String podtipDokumentaNaziv) {
        PodtipDokumentaNaziv = podtipDokumentaNaziv;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdTip() {
        return idTip;
    }

    public void setIdTip(long idTip) {
        this.idTip = idTip;
    }

    public long getIdPodtip() {
        return idPodtip;
    }

    public void setIdPodtip(long idPodtip) {
        this.idPodtip = idPodtip;
    }

    public long getIdKomitent() {
        return idKomitent;
    }

    public void setIdKomitent(long idKomitent) {
        this.idKomitent = idKomitent;
    }

    public long getIdPjKomitenta() {
        return idPjKomitenta;
    }

    public void setIdPjKomitenta(long idPjKomitenta) {
        this.idPjKomitenta = idPjKomitenta;
    }

    public Date getDatumDokumenta() {
        return datumDokumenta;
    }

    public String getDatumDokumentaString(){
        return  DateFormat.format(MainActivity.DatumFormat, datumDokumenta).toString();
    }

    public String getDatumDokumentaJSONString(){
        return  DateFormat.format(MainActivity.BorisovFormatDatuma, datumDokumenta).toString();
    }

    public void setDatumDokumenta(Date datumDokumenta) {
        this.datumDokumenta = datumDokumenta;
    }

    public Date getDatumSinkronizacije() {
        return datumSinkronizacije;
    }

    public void setDatumSinkronizacije(Date datumSinkronizacije) {
        this.datumSinkronizacije = datumSinkronizacije;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
