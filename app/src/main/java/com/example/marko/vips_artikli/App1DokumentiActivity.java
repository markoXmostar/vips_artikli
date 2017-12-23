package com.example.marko.vips_artikli;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.marko.vips_artikli.MainActivity.DatumFormat;
import static com.example.marko.vips_artikli.MainActivity.DatumVrijemeFormat;
import static com.example.marko.vips_artikli.MainActivity.myDATABASE;

public class App1DokumentiActivity extends Activity {
    private static String TAG = "App1";
    private static String tabelaApp1 = "dokumenti1";

    private ListView listSpisakDokumenata;
    private FloatingActionButton fabNoviDokument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_dokumenti);

        listSpisakDokumenata = (ListView) findViewById(R.id.listSpisakDokumenata_App1);
        fabNoviDokument = (FloatingActionButton) findViewById(R.id.fabNovoZaglavlje_App1);


        kreirajTabeluDokumenata();
        ucitajDokumente();

        fabNoviDokument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(App1DokumentiActivity.this,App1ZaglavljeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void kreirajTabeluDokumenata() {
        SQLiteDatabase myDB = null;
        Log.d(TAG, "Otvaram bazu");
        myDB = openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "Kreiram tabelu");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + tabelaApp1 + " (" +
                "_id Integer PRIMARY KEY AUTOINCREMENT, " +
                "idTip VARCHAR, " +
                "TipDokumentaNaziv VARCHAR," +
                "idPodtip VARCHAR," +
                "PodipDokumentaNaziv VARCHAR," +
                "idKomitent VARCHAR, " +
                "KomitentNaziv VARCHAR," +
                "idPjKomitenta VARCHAR, " +
                "PjKomitentaNaziv VARCHAR," +
                "datumDokumenta datetime, " +
                "datumSinkronizacije datetime," +
                "napomena VARCHAR);");
        myDB.close();

    }

    private void ucitajDokumente() {
        ListaApp1DokumentiAdapter listaDokumenta = new ListaApp1DokumentiAdapter(this, R.layout.row_app1_zaglavlje);
        listSpisakDokumenata.setAdapter(listaDokumenta);
        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM " + tabelaApp1, null);

        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(DatumVrijemeFormat);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DatumFormat);

        long id;
        long idTip;
        long idPodtip;
        long idKomitent;
        long idPjKomitenta;
        String datumDokumentaString;
        String datumSinkronizacijeString;
        String napomena;
        String komitentNaziv;
        String komitentPjNaziv;
        String tipNaziv;
        String podtipNaziv;

        Date datumDokumenta = new Date();
        Date datumSinkronizacije = new Date();

        int idIndex = c.getColumnIndex("id");
        int idTipIndex = c.getColumnIndex("idTip");
        int idPodipIndex = c.getColumnIndex("idPodtip");
        int idKomitentIndex = c.getColumnIndex("idKomitent");
        int idPjKomitentaIndex = c.getColumnIndex("idPjKomitenta");
        int idDatumDokumentaIndex = c.getColumnIndex("datumDokumenta");
        int idDatumSinkronizacijeIndex = c.getColumnIndex("datumSinkronizacije");
        int idNapomenaIndex = c.getColumnIndex("napomena");

        int idKomitentNaziv = c.getColumnIndex("KomitentNaziv");
        int idKomitentPjIndex = c.getColumnIndex("PjKomitentaNaziv");
        int idTipNazivIndex = c.getColumnIndex("TipDokumentaNaziv");
        int idPodtpNazivIndex = c.getColumnIndex("PodipDokumentaNaziv");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            id = c.getLong(idIndex);
            idTip = c.getLong(idTipIndex);
            tipNaziv = c.getString(idTipNazivIndex);
            idPodtip = c.getLong(idPodipIndex);
            podtipNaziv = c.getString(idPodtpNazivIndex);
            idKomitent = c.getLong(idKomitentIndex);
            komitentNaziv = c.getString(idKomitentNaziv);
            idPjKomitenta = c.getLong(idPjKomitentaIndex);
            komitentPjNaziv = c.getString(idKomitentPjIndex);
            datumDokumentaString = c.getString(idDatumDokumentaIndex);
            datumSinkronizacijeString = c.getString(idDatumSinkronizacijeIndex);
            napomena = c.getString(idNapomenaIndex);
            try {
                datumDokumenta = (Date) dateFormat.parse(datumDokumentaString);
            } catch (ParseException e) {
                e.printStackTrace();

            }
            try {
                datumSinkronizacije = (Date) simpleDateTimeFormat.parse(datumSinkronizacijeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            App1Dokumenti myObj = new App1Dokumenti(id, idTip, idPodtip, idKomitent, idPjKomitenta, datumDokumenta, datumSinkronizacije, napomena, komitentNaziv, komitentPjNaziv, tipNaziv, podtipNaziv);
            listaDokumenta.add(myObj);

            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        Log.d(TAG, "ucitajDokumente: U tabeli se nalazi " + brojac + " dokumenta!");
        c.close();
        myDB.close();
    }

}
