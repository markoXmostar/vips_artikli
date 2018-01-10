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
        Log.d(TAG, "UcitajListuArtiklJMJIzBaze: Start");
        String myTabela="artikljmj";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        Log.d(TAG, "UcitajListuArtiklJMJIzBaze: Postoji tabela. Počni učitavanje");

        ListaArtiklJmjAdapter listaArtiklJmjAdapter = new ListaArtiklJmjAdapter(this, R.layout.row_jmj);
        mojListView.setAdapter(listaArtiklJmjAdapter);
        Log.d(TAG, " ucitavam bazu!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM "+ myTabela + " ORDER BY artiklId ASC;", null);
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela + " where artiklId like '%" + filter + "%'  ORDER BY artiklId ASC;", null);
        }

        int ArtiklIdIndex = c.getColumnIndex("artiklId");
        int jmjIdIndex = c.getColumnIndex("jmjId");

        c.moveToFirst();
        int brojac = 0;
        Log.d(TAG, "ListaArtiklJmjAdapter: Broj podataka u bazi je:" + Integer.toString( c.getCount()+1));
        for (int j = 0; j < c.getCount(); j++) {
            long artId;
            long jmjId;
            artId = c.getLong(ArtiklIdIndex);
            jmjId = c.getLong(jmjIdIndex);

            ArtiklJmj ArtJmjProvider = new ArtiklJmj(artId, jmjId);
            listaArtiklJmjAdapter.add(ArtJmjProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Tabela jmj učitana!");
    }
    private void UcitajListuArtiklAtributaIzBaze(String filter) {
        Log.d(TAG, "UcitajListuArtiklJMJIzBaze: Start");
        String myTabela="artiklatribut";
        if (!PostaviVidljivostElemenata(myTabela)){
            return;
        }
        Log.d(TAG, "UcitajListuArtiklAtributIzBaze: Postoji tabela. Počni učitavanje");

        ListaArtiklAtributStanjeAdapter listaArtiklAtributAdapter = new ListaArtiklAtributStanjeAdapter(this, R.layout.row_grupa);
        mojListView.setAdapter(listaArtiklAtributAdapter);
        Log.d(TAG, " ucitavam bazu!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM "+ myTabela + " ORDER BY artiklID ASC;", null);
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela + " where artiklId like '%" + filter + "%' ORDER BY artiklID ASC;", null);
        }

        int ArtiklIdIndex = c.getColumnIndex("artiklId");
        int vrijednostId1Index = c.getColumnIndex("vrijednostId1");
        int vrijednost1Index = c.getColumnIndex("vrijednost1");
        int atribut1Index = c.getColumnIndex("atribut1");
        int stanjeIndex = c.getColumnIndex("stanje");

        c.moveToFirst();
        int brojac = 0;
        Log.d(TAG, "ListaArtiklAtributAdapter: Broj podataka u bazi je:" + Integer.toString( c.getCount()+1));
        for (int j = 0; j < c.getCount(); j++) {
            long artId;
            long vrj1Id;
            String vrijednost;
            String atribut1;
            double stanje;

            artId = c.getLong(ArtiklIdIndex);
            vrj1Id = c.getLong(vrijednostId1Index);
            vrijednost=c.getString(vrijednost1Index);
            atribut1=c.getString(atribut1Index);
            stanje=c.getDouble(stanjeIndex);


            ArtiklAtributStanje object = new ArtiklAtributStanje(artId, vrj1Id,vrijednost,atribut1,stanje);
            listaArtiklAtributAdapter.add(object);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Tabela ArtiklAtribut učitana!");
    }
}
