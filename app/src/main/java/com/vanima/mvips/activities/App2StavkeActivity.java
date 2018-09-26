package com.vanima.mvips.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vanima.mvips.models.App1Stavke;
import com.vanima.mvips.models.App2Stavke;
import com.vanima.mvips.models.Artikl;
import com.vanima.mvips.adapters.ListaApp2StavkeAdapter;
import com.vanima.mvips.R;

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

        fabNovaStavka = (FloatingActionButton) findViewById(R.id.fabNovaStavka_App2Stavke);
        fabNovaStavka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App2StavkeActivity.this, App1UnosStavkeActivity.class);
                intent.putExtra("idDokumenta", IdDokumenta);
                intent.putExtra("tipStavke", 1);
                startActivityForResult(intent, 2);
            }
        });

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

        listSpisakStavki.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                final App2Stavke selektiranaStavka = (App2Stavke) adapterView.getItemAtPosition(i);
                final CharSequence akcije[];
                if (selektiranaStavka.getVipsId() == 0) {
                    akcije = new CharSequence[]{"Prikaži detalje", "Izbriši"};
                } else {
                    akcije = new CharSequence[]{"Prikaži detalje"};
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(App2StavkeActivity.this);
                builder.setTitle("Opcije stavki");
                builder.setItems(akcije, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        switch (which) {
                            case 1:
                                //izbrisiRedakIzTabele
                                int y = selektiranaStavka.getId();
                                long x = Long.valueOf(y);
                                MainActivity.izbrisiRedakIzTabele(App2StavkeActivity.this, "stavke2", "_id", x);
                                MainActivity.updateOstalihRBR(App2StavkeActivity.this, selektiranaStavka.getZaglavljeId(), selektiranaStavka.getRbr());
                                MainActivity.updateZavrsenoInDokumenti2(App2StavkeActivity.this, selektiranaStavka.getZaglavljeId());
                                ucitajStavke();
                                break;

                            case 0:
                                //Detalji
                                Toast.makeText(App2StavkeActivity.this, selektiranaStavka.toString(), Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
                builder.show();


                return true;
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                App2Stavke rezultat = (App2Stavke) data.getSerializableExtra("stavka");
                MainActivity.updateStavke2(App2StavkeActivity.this, rezultat);

                ucitajStavke();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //ucitajStavke();
            }
        }
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                App1Stavke rezultat = (App1Stavke) data.getSerializableExtra("stavka");
                //treba naći šifru artikla i rbr stavke
                Artikl myArt = MainActivity.getArtiklById(App2StavkeActivity.this, rezultat.getArtiklId(), 0, false);
                int newRbr = MainActivity.getStavke2NoviRbr(App2StavkeActivity.this, IdDokumenta);
                //pretvori stavku1 u stavku2
                Log.d(TAG, "onActivityResult: ATRIBUT NAZIV: " + rezultat.getAtributNaziv());
                Log.d(TAG, "onActivityResult: ATRIBUT VRIJEDNOST: " + rezultat.getAtributVrijednost());
                Log.d(TAG, "onActivityResult: ATRIBUT ID: " + rezultat.getAtributId());

                App2Stavke newStavka = new App2Stavke(0, IdDokumenta, rezultat.getArtiklId(), rezultat.getJmjId(), rezultat.getAtributId(), 0, newRbr, rezultat.getKolicina(),
                        0, rezultat.getNapomena(), myArt.getSifra(), rezultat.getArtiklNaziv(), rezultat.getJmjNaziv(), rezultat.getAtributNaziv(), rezultat.getAtributVrijednost());
                MainActivity.snimiStavku2(App2StavkeActivity.this, IdDokumenta, newStavka);
                ucitajStavke();

            }
        }
    }
}
