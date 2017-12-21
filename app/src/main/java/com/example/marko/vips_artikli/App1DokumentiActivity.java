package com.example.marko.vips_artikli;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import static com.example.marko.vips_artikli.MainActivity.myDATABASE;

public class App1DokumentiActivity extends Activity {
    private static String TAG = "App1";
    private static String tabelaApp1 = "dokumenti1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_dokumenti);
    }

    private void kreirajTabeluDokumenata() {
        SQLiteDatabase myDB = null;
        Log.d(TAG, "Otvaram bazu");
        myDB = openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "Kreiram tabelu");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + tabelaApp1 + " (" +
                "_id long PRIMARY KEY AUTOINCREMENT, " +
                "idTip VARCHAR, " +
                "idPodtip VARCHAR," +
                "idKomitent VARCHAR, " +
                "idPjKomitenta VARCHAR, " +
                "datumDokumenta datetime, " +
                "datumSinkronizacije datetime," +
                "napomena VARCHAR);");

    }

}
