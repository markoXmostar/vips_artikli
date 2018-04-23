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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class App2DokumentiActivity extends AppCompatActivity {

    private static String TAG = "App2Dokumenti";
    private static String tabelaApp1 = "dokumenti2";

    private ListView listSpisakDokumenata;
    private ListaApp2DokumentiAdapter listaDokumenta;

    private int lastDokId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app2_dokumenti);
        lastDokId = 0;

        listSpisakDokumenata = (ListView) findViewById(R.id.listSpisakDokumenata_App2);
        listSpisakDokumenata.setItemsCanFocus(false);
        listaDokumenta = new ListaApp2DokumentiAdapter(this, R.layout.row_app2_zaglavlje);
        listSpisakDokumenata.setAdapter(listaDokumenta);

        setTitle();
        ucitajDokumente();

        Log.d(TAG, "onCreate: CREATE Dokument2");
        listSpisakDokumenata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                App2Dokumenti selektiranDok = (App2Dokumenti) adapterView.getItemAtPosition(i);
                //Toast.makeText(App1DokumentiActivity.this,selektiranDok.getId() +"/" +selektiranDok.getDatumDokumentaString() + "-" + selektiranDok.getKomitentNaziv(),Toast.LENGTH_LONG).show();
                lastDokId = selektiranDok.getId();
                Intent intent = new Intent(App2DokumentiActivity.this, App2StavkeActivity.class);
                intent.putExtra("idDokumenta", selektiranDok.getVipsId());
                startActivity(intent);

            }
        });

        listSpisakDokumenata.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final App2Dokumenti selektiraniDok = (App2Dokumenti) adapterView.getItemAtPosition(i);
                final CharSequence akcije[];
                akcije = new CharSequence[]{"Prikaži detalje", "Sinkroniziraj"};
                AlertDialog.Builder builder = new AlertDialog.Builder(App2DokumentiActivity.this);
                builder.setTitle("Opcije dokumenta");
                builder.setItems(akcije, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        switch (which) {
                            case 0:
                                //Detalji
                                Toast.makeText(App2DokumentiActivity.this, selektiraniDok.toString(), Toast.LENGTH_LONG).show();
                                break;

                            case 1:
                                //UPLOAD
                                Toast.makeText(App2DokumentiActivity.this, selektiraniDok.toString(), Toast.LENGTH_LONG).show();
                                List<App2Dokumenti> spisakDokZaSync = getDokumentZaSyncIliPrintanje(selektiraniDok);
                                List<App1Dokumenti> spisak = new ArrayList<App1Dokumenti>();
                                for (App2Dokumenti dok : spisakDokZaSync) {
                                    App1Dokumenti d1 = MainActivity.pretvoriApp2Dok_App1Dok(dok);
                                    d1.setVipsId(dok.getVipsId());
                                    d1.izbrisiSveStavke();
                                    spisak.add(d1);
                                    for (App2Stavke stv : dok.getSpisakStavki()) {
                                        App1Stavke s1 = MainActivity.pretvoriApp2Stv_App1Stv(stv);
                                        s1.setVipsID(stv.getVipsId());
                                        s1.setRbr(stv.getRbr());
                                        d1.doadajStavku(s1);
                                    }
                                }
                                try {
                                    String rezultat = new JSON_send(App2DokumentiActivity.this, spisak, true, 2).execute().get();
                                    if (rezultat.equals("OK")) {

                                        //izbrisi dokumente i stavke
                                        MainActivity.izbrisiDokumente2PoslijeSinkronizacije(App2DokumentiActivity.this, selektiraniDok.getId());
                                        ucitajDokumente();

                                    } else {
                                        Toast.makeText(App2DokumentiActivity.this, "GREŠKA PRI SLANJU PODATAKA! POKUŠAJTE KASNIJE.", Toast.LENGTH_LONG).show();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                });
                builder.show();


                return true;
            }
        });
    }

    private List<App2Dokumenti> getDokumentZaSyncIliPrintanje(App2Dokumenti selektiranDok) {
        List<App2Dokumenti> spisakDokZaSync = new ArrayList<App2Dokumenti>();
        spisakDokZaSync.add(selektiranDok);
        selektiranDok.izbrisiSveStavke();
        List<App2Stavke> mojeStavke = MainActivity.getListaStavki2(selektiranDok.getVipsId(), App2DokumentiActivity.this);
        for (App2Stavke stv : mojeStavke) {

            selektiranDok.doadajStavku(stv);
        }
        return spisakDokZaSync;
    }
    private void ucitajDokumente() {
        Log.d(TAG, "onCreate: UČITAVAM Dokument2");
        Log.d(TAG, "ucitajDokumente: Učitavam dokumente 2 /START");
        List<App2Dokumenti> spisakDok = MainActivity.getListaDokumenta2(App2DokumentiActivity.this, 0);
        Log.d(TAG, "ucitajDokumente: Učitao sam dokumente 2 /END");
        for (App2Dokumenti dok : spisakDok) {
            listaDokumenta.add(dok);
        }
    }

    private void updateDokumenata() {

        if (lastDokId != 0) {
            boolean value = MainActivity.getZavrsenoDokumenti2ById(this, lastDokId);
            for (int i = 0; i < listaDokumenta.getCount(); i++) {
                App2Dokumenti dok2 = (App2Dokumenti) listaDokumenta.getItem(i);
                if (dok2.getId() == lastDokId) {
                    dok2.setZavrsen(value);
                    listaDokumenta.notifyDataSetChanged();
                }
            }
        }

//        }
    }

    private void setTitle() {
        this.setTitle(MainActivity.getNazivZadanogPodtipaDokumenta(this));
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        updateDokumenata();
    }
}
