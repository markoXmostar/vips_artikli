package com.vanima.mvips.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vanima.mvips.models.ArtiklAtributStanje;
import com.vanima.mvips.models.ArtiklJmj;
import com.vanima.mvips.models.Barcode;
import com.vanima.mvips.models.GrupaArtikala;
import com.vanima.mvips.adapters.ListaArtiklAtributStanjeAdapter;
import com.vanima.mvips.adapters.ListaArtiklJmjAdapter;
import com.vanima.mvips.adapters.ListaBarcodeAdapter;
import com.vanima.mvips.adapters.ListaGrupaAdapter;
import com.vanima.mvips.adapters.ListaJmjAdapter;
import com.vanima.mvips.adapters.ListaNacinplacanjaAdapter;
import com.vanima.mvips.adapters.ListaPjKomitentAdapter;
import com.vanima.mvips.adapters.ListaPodgrupaAdapter;
import com.vanima.mvips.adapters.ListaPodtipDokumentaAdapter;
import com.vanima.mvips.adapters.ListaTipDokumentaAdapter;
import com.vanima.mvips.models.NacinPlacanja;
import com.vanima.mvips.models.PjKomitent;
import com.vanima.mvips.models.PodgrupaArtikala;
import com.vanima.mvips.models.PodtipDokumenta;
import com.vanima.mvips.R;
import com.vanima.mvips.models.TipDokumenta;
import com.vanima.mvips.models.jmj;

import java.util.List;

public class RegistriActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "REGISTRI";

    String[] registri = {"JMJ", "Tip dokumenta", "Podtip dokumenta", "Grupa artikala", "Podgrupa artikala", "Način plaćanja","PjKomitenta", "Barcode", "ArtikliJmj","ArtiklAtribut"};

    ListView mojListView;
    TextView NoDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registri);
        Spinner spin = (Spinner) findViewById(R.id.simpleSpinner);
        NoDataText = (TextView) findViewById(R.id.NoDataText);
        NoDataText.setVisibility(View.INVISIBLE);
        mojListView = (ListView) findViewById(R.id.mojaLista);
        mojListView.setItemsCanFocus(false);

        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, registri);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        String izabran = registri[position];
        Log.d(TAG, "Izabran je:" + izabran);
        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        switch (izabran) {
            case "JMJ":
                UcitajListuJMJIzBaze("");
                break;
            case "Tip dokumenta":
                UcitajListuTipovaDokumenataIzBaze("");
                break;
            case "Podtip dokumenta":
                UcitajListuPodtipovaDokumenataIzBaze("");
                break;
            case "Način plaćanja":
                UcitajNacinPlacanjaIzBaze("");
                break;
            case "Grupa artikala":
                UcitajGrupeIzBaze("");
                break;
            case "Podgrupa artikala":
                UcitajPodgrupeIzBaze("");
                break;
            case "PjKomitenta":
                UcitajPjKomitentaIzBaze("");
                break;
            case "Barcode":
                UcitajListuBarcodeIzBaze("");
                break;
            case "ArtikliJmj":
                UcitajListuArtiklJmjIzBaze("");
                break;
            case "ArtiklAtribut":
                UcitajListuArtiklAtributaIzBaze("");
                break;
            default:

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    private boolean PostaviVidljivostElemenata(String myTabela){
        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        boolean izlaz = false;
        if (!MainActivity.isTableExists(mDatabase, myTabela)) {
            NoDataText.setVisibility(View.VISIBLE);
            mojListView.setVisibility(View.INVISIBLE);
            NoDataText.setEnabled(false);
            izlaz = false;

        }
        else {
            mojListView.setVisibility(View.VISIBLE);
            NoDataText.setVisibility(View.INVISIBLE);
            izlaz = true;
        }
        mDatabase.close();
        return izlaz;
    }
    private void UcitajPjKomitentaIzBaze(String filter) {
        String myTabela="pjkomitenta";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaPjKomitentAdapter listaPjKom = new ListaPjKomitentAdapter(this, R.layout.row_grupa);
        mojListView.setAdapter(listaPjKom);
        Log.d(TAG, " ucitavam tabelu " + myTabela);

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM " +myTabela, null);
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela +" where naziv like '%" + filter + "%'", null);
        }

        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");
        int RidIndex=c.getColumnIndex("rid");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String naziv;
            long rid;

            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);
            rid=c.getLong(RidIndex);

            PjKomitent PjKom = new PjKomitent(id, naziv,rid);
            listaPjKom.add(PjKom);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Tabela "+ myTabela +" učitana!");
    }
    private void UcitajPodgrupeIzBaze(String filter) {
        String myTabela="podgrupa_artikala";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaPodgrupaAdapter listaPodgrupaAdapter = new ListaPodgrupaAdapter(this, R.layout.row_podgrupa);
        mojListView.setAdapter(listaPodgrupaAdapter);
        Log.d(TAG, " ucitavam tabelu " + myTabela + " dokumenata!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM " +myTabela, null);
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela +" where naziv like '%" + filter + "%'", null);
        }

        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");
        int RidIndex=c.getColumnIndex("rid");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String naziv;
            long rid;

            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);
            rid=c.getLong(RidIndex);

            PodgrupaArtikala PodgrupaArtikalaProvider = new PodgrupaArtikala(id, naziv,rid);
            listaPodgrupaAdapter.add(PodgrupaArtikalaProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Tabela "+ myTabela +" učitana!");
    }
    private void UcitajGrupeIzBaze(String filter) {
        String myTabela="grupa_artikala";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaGrupaAdapter listaGrupaAdapter = new ListaGrupaAdapter(this, R.layout.row_grupa);
        mojListView.setAdapter(listaGrupaAdapter);
        Log.d(TAG, " ucitavam tabelu " + myTabela + " dokumenata!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM " +myTabela, null);
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela +" where naziv like '%" + filter + "%'", null);
        }

        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");
        int RidIndex=c.getColumnIndex("rid");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String naziv;
            long rid;

            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);
            rid=c.getLong(RidIndex);

            GrupaArtikala GrupaArtikalaProvider = new GrupaArtikala(id, naziv,rid);
            listaGrupaAdapter.add(GrupaArtikalaProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Tabela "+ myTabela +" učitana!");
    }
    private void UcitajNacinPlacanjaIzBaze(String filter) {
        ListaNacinplacanjaAdapter listaNacinaPlacanjaAdapter = new ListaNacinplacanjaAdapter(this, R.layout.row_jmj);
        mojListView.setAdapter(listaNacinaPlacanjaAdapter);
        String myTabela="nacin_placanja";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }

        for (NacinPlacanja nacin : MainActivity.getListaNacinaPlacanja(RegistriActivity.this, filter)) {
            listaNacinaPlacanjaAdapter.add(nacin);
        }

    }
    private void UcitajListuPodtipovaDokumenataIzBaze(String filter) {
        String myTabela="podtip_dokumenta";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaPodtipDokumentaAdapter listaPodtipDokumentaAdapter = new ListaPodtipDokumentaAdapter(this, R.layout.row_grupa);
        mojListView.setAdapter(listaPodtipDokumentaAdapter);
        for (PodtipDokumenta podtip : MainActivity.getListaPodtipova(RegistriActivity.this, filter, 0, false, "")) {
            listaPodtipDokumentaAdapter.add(podtip);
        }
    }
    private void UcitajListuTipovaDokumenataIzBaze(String filter) {
        String myTabela="tip_dokumenta";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaTipDokumentaAdapter listaTipAdapter = new ListaTipDokumentaAdapter(this, R.layout.row_grupa);
        mojListView.setAdapter(listaTipAdapter);
        for (TipDokumenta tip : MainActivity.getListaTipovaDokumenta(RegistriActivity.this, filter, false, "")) {
            listaTipAdapter.add(tip);
        }

    }
    private void UcitajListuJMJIzBaze(String filter) {

        String myTabela="jmj";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaJmjAdapter listaJmjAdapter = new ListaJmjAdapter(this, R.layout.row_jmj);
        mojListView.setAdapter(listaJmjAdapter);
        List<jmj> mojaLista = MainActivity.getListaJMJ(this, -1, "");
        for (int j = 0; j < mojaLista.size(); j++) {
            listaJmjAdapter.add(mojaLista.get(j));
        }
    }

    private void UcitajListuBarcodeIzBaze(String filter) {
        Log.d(TAG, "UcitajListuBarcodeIzBaze: Start");
        String myTabela="artiklbarcode";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        Log.d(TAG, "UcitajListuArtiklbarcodeIzBaze: Postoji tabela. Počni učitavanje");

        ListaBarcodeAdapter listaBarcodeAdapter = new ListaBarcodeAdapter(this, R.layout.row_jmj);
        mojListView.setAdapter(listaBarcodeAdapter);
        Log.d(TAG, " ucitavam bazu!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM "+ myTabela, null);
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela + " where naziv like '%" + filter + "%'", null);
        }

        int IdIndex = c.getColumnIndex("artiklId");
        int NazivIndex = c.getColumnIndex("barcode");

        c.moveToFirst();
        int brojac = 0;
        Log.d(TAG, "UcitajListuBarcodeIzBaze: Broj podataka u bazi je:" + Integer.toString( c.getCount()+1));
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String naziv;
            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);

            Barcode BarcodeProvider = new Barcode(id, naziv);
            listaBarcodeAdapter.add(BarcodeProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Tabela jmj učitana!");
    }
    private void UcitajListuArtiklJmjIzBaze(String filter) {
        String myTabela="artikljmj";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaArtiklJmjAdapter listaArtiklJmjAdapter = new ListaArtiklJmjAdapter(this, R.layout.row_grupa);
        mojListView.setAdapter(listaArtiklJmjAdapter);

        List<ArtiklJmj> mojaLista = MainActivity.getListaArtiklJMJ(this, -1, "");
        for (int j = 0; j < mojaLista.size(); j++) {
            listaArtiklJmjAdapter.add(mojaLista.get(j));
        }

    }
    private void UcitajListuArtiklAtributaIzBaze(String filter) {

        String myTabela="artiklatribut";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaArtiklAtributStanjeAdapter listaArtiklAtributAdapter = new ListaArtiklAtributStanjeAdapter(this, R.layout.row_grupa);
        mojListView.setAdapter(listaArtiklAtributAdapter);
        List<ArtiklAtributStanje> mojaLista = MainActivity.getListaAtributa(this, -1, "");
        for (int j = 0; j < mojaLista.size(); j++) {
            listaArtiklAtributAdapter.add(mojaLista.get(j));
        }
    }
}
