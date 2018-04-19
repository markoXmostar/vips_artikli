package com.example.marko.vips_artikli;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class App2DokumentiActivity extends AppCompatActivity {

    private static String TAG = "App2Dokumenti";
    private static String tabelaApp1 = "dokumenti2";

    private ListView listSpisakDokumenata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app2_dokumenti);

        listSpisakDokumenata = (ListView) findViewById(R.id.listSpisakDokumenata_App2);
        listSpisakDokumenata.setItemsCanFocus(false);

        setTitle();
        ucitajDokumente();

        listSpisakDokumenata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                App2Dokumenti selektiranDok = (App2Dokumenti) adapterView.getItemAtPosition(i);
                //Toast.makeText(App1DokumentiActivity.this,selektiranDok.getId() +"/" +selektiranDok.getDatumDokumentaString() + "-" + selektiranDok.getKomitentNaziv(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(App2DokumentiActivity.this, App2StavkeActivity.class);
                intent.putExtra("idDokumenta", selektiranDok.getVipsId());
                startActivity(intent);

            }
        });
    }

    private void ucitajDokumente() {
        ListaApp2DokumentiAdapter listaDokumenta = new ListaApp2DokumentiAdapter(this, R.layout.row_app2_zaglavlje);
        listSpisakDokumenata.setAdapter(listaDokumenta);
        Log.d(TAG, "ucitajDokumente: Učitavam dokumente 2 /START");
        List<App2Dokumenti> spisakDok = MainActivity.getListaDokumenta2(App2DokumentiActivity.this, 0);
        Log.d(TAG, "ucitajDokumente: Učitao sam dokumente 2 /END");
        for (App2Dokumenti dok : spisakDok) {
            listaDokumenta.add(dok);
        }
    }

    private void setTitle() {
        this.setTitle(MainActivity.getNazivZadanogPodtipaDokumenta(this));

    }


}
