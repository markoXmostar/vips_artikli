package com.example.marko.vips_artikli;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class App2StavkeActivity extends AppCompatActivity {
    private static String TAG = "App2Stavke";
    private static String tabelaApp1 = "stavke2";
    private long IdDokumenta = 0;

    ListView listSpisakStavki;
    private FloatingActionButton fabNovaStavka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app2_stavke);

        Bundle b = getIntent().getExtras();

        final long IdDok = b.getLong("idDokumenta");
        IdDokumenta = IdDok;
        this.setTitle("Dokument ID=" + IdDokumenta);

        //fabNovaStavka = (FloatingActionButton) findViewById(R.id.fabNovaStavka_App1);


        listSpisakStavki = (ListView) findViewById(R.id.listSpisakStavki_App2);
        ucitajStavke();

        listSpisakStavki.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                App2Stavke stavka = (App2Stavke) listSpisakStavki.getItemAtPosition(i);
                Intent intent = new Intent(App2StavkeActivity.this, App2UnosKolicine.class);
                intent.putExtra("stavka", stavka);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void ucitajStavke() {
        ListaApp2StavkeAdapter listaStavki = new ListaApp2StavkeAdapter(this, R.layout.row_app2_stavke);
        listSpisakStavki.setAdapter(listaStavki);
        List<App2Stavke> lista = MainActivity.getListaStavki2(IdDokumenta, App2StavkeActivity.this);
        for (App2Stavke stavka : lista) {
            listaStavki.add(stavka);
        }
    }
}
