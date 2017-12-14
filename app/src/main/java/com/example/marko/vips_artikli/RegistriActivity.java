package com.example.marko.vips_artikli;

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

public class RegistriActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "REGISTRI";

    String[] registri = {"JMJ", "Tip dokumenta", "Podtip dokumenta", "Grupa artikala", "Podgrupa artikala", "Način plaćanja"};

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
            default:

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    private boolean PostaviVidljivostElemenata(String myTabela){
        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        if (!MainActivity.isTableExists(mDatabase, myTabela)) {
            NoDataText.setVisibility(View.VISIBLE);
            mojListView.setVisibility(View.INVISIBLE);
            NoDataText.setEnabled(false);
            return false;
        }
        else {
            mojListView.setVisibility(View.VISIBLE);
            NoDataText.setVisibility(View.INVISIBLE);
            return true;
        }
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
        String myTabela="nacin_placanja";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaNacinplacanjaAdapter listaNacinaPlacanjaAdapter = new ListaNacinplacanjaAdapter(this, R.layout.row_jmj);
        mojListView.setAdapter(listaNacinaPlacanjaAdapter);
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


        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String naziv;


            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);


            NacinPlacanja nacinPlacanjaProvider = new NacinPlacanja(id, naziv);
            listaNacinaPlacanjaAdapter.add(nacinPlacanjaProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Tabela "+ myTabela +" učitana!");
    }
    private void UcitajListuPodtipovaDokumenataIzBaze(String filter) {
        String myTabela="podtip_dokumenta";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaPodtipDokumentaAdapter listaPodtipDokumentaAdapter = new ListaPodtipDokumentaAdapter(this, R.layout.row_grupa);
        mojListView.setAdapter(listaPodtipDokumentaAdapter);
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

            PodtipDokumenta podtipProvider = new PodtipDokumenta(id, naziv,rid);
            listaPodtipDokumentaAdapter.add(podtipProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Tabela "+ myTabela +" učitana!");
    }
    private void UcitajListuTipovaDokumenataIzBaze(String filter) {
        String myTabela="tip_dokumenta";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        ListaTipDokumentaAdapter listaTipAdapter = new ListaTipDokumentaAdapter(this, R.layout.row_grupa);
        mojListView.setAdapter(listaTipAdapter);
        Log.d(TAG, " ucitavam tabelu tipova dokumenata!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM " + myTabela, null);
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela + " where naziv like '%" + filter + "%'", null);
        }

        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String naziv;

            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);


            TipDokumenta tipProvider = new TipDokumenta(id, naziv);
            listaTipAdapter.add(tipProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Tabela tip_dokumenta učitana!");
    }
    private void UcitajListuJMJIzBaze(String filter) {
        Log.d(TAG, "UcitajListuJMJIzBaze: Start");
        String myTabela="jmj";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        Log.d(TAG, "UcitajListuJMJIzBaze: Postoji tabela. Počni učitavanje");

        ListaJmjAdapter listaJmjAdapter = new ListaJmjAdapter(this, R.layout.row_jmj);
        mojListView.setAdapter(listaJmjAdapter);
        Log.d(TAG, " ucitavam bazu!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM "+ myTabela, null);
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela + " where naziv like '%" + filter + "%'", null);
        }

        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");

        c.moveToFirst();
        int brojac = 0;
        Log.d(TAG, "UcitajListuJMJIzBaze: Broj podataka u bazi je:" + Integer.toString( c.getCount()+1));
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String naziv;
            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);

            jmj jmjProvider = new jmj(id, naziv);
            listaJmjAdapter.add(jmjProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Tabela jmj učitana!");
    }
}
