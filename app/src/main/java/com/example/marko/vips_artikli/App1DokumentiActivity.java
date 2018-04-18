package com.example.marko.vips_artikli;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.example.marko.vips_artikli.MainActivity.DatumFormat;
import static com.example.marko.vips_artikli.MainActivity.myDATABASE;

public class App1DokumentiActivity extends AppCompatActivity {
    private static String TAG = "App1Dokumenti";
    private static String tabelaApp1 = "dokumenti1";

    private ListView listSpisakDokumenata;
    private FloatingActionButton fabNoviDokument;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app1_dokumenti_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.drop_table_dokumenti1) {
            MainActivity.dropTable(App1DokumentiActivity.this,tabelaApp1);
            finish();
            return true;
        }
        if (id == R.id.delete_all_dokumenti1) {
            MainActivity.deleteAllTable(App1DokumentiActivity.this,tabelaApp1);
            ucitajDokumente();
            return true;
        }
        */
        if (id == R.id.sinkroniziraj_dokumenti1) {
            if (MainActivity.sendAllDokuments(App1DokumentiActivity.this)) {
                ucitajDokumente();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_dokumenti);

        //getActionBar().setTitle(getString(R.string.title_activity_app1_dokumenti));

        listSpisakDokumenata = (ListView) findViewById(R.id.listSpisakDokumenata_App1);
        listSpisakDokumenata.setItemsCanFocus(false);
        fabNoviDokument = (FloatingActionButton) findViewById(R.id.fabNovoZaglavlje_App1);


        kreirajTabeluDokumenata();
        ucitajDokumente();

        fabNoviDokument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App1DokumentiActivity.this, App1UnosZaglavljaActivity.class);
                startActivityForResult(intent,1);
            }
        });

        listSpisakDokumenata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                App1Dokumenti selektiranDok=(App1Dokumenti) adapterView.getItemAtPosition(i);
                //Toast.makeText(App1DokumentiActivity.this,selektiranDok.getId() +"/" +selektiranDok.getDatumDokumentaString() + "-" + selektiranDok.getKomitentNaziv(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(App1DokumentiActivity.this, App1StavkeActivity.class);
                intent.putExtra("idDokumenta", selektiranDok.getId());
                boolean isSync = false;
                if (selektiranDok.getDatumSinkronizacije() != null) {
                    isSync = true;
                }
                intent.putExtra("isSync", isSync);
                startActivity(intent);

            }
        });

        listSpisakDokumenata.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                final App1Dokumenti selektiranDok=(App1Dokumenti) adapterView.getItemAtPosition(i);
                final CharSequence akcije[];
                if (selektiranDok.getDatumSinkronizacije() == null) {
                    akcije = new CharSequence[]{"Izbriši dokument!", "Sinkroniziraj", "Ispis / detalji dokumenta"};
                } else {
                    akcije = new CharSequence[]{"Izbriši dokument!", "Ispis / detalji dokumenta"};
                }
                //final CharSequence akcije[] = new CharSequence[] {"Izbriši", "Sinkroniziraj", "Prikaži detalje"};
                AlertDialog.Builder builder = new AlertDialog.Builder(App1DokumentiActivity.this);
                builder.setTitle("Opcije dokumenta");
                builder.setItems(akcije, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        switch (which){
                            case 0:
                                //izbrisiRedakIzTabele
                                MainActivity.izbrisiRedakIzTabele(App1DokumentiActivity.this,tabelaApp1,"_id", selektiranDok.getId());
                                ucitajDokumente();
                                break;
                            case 1:
                                //Sinkronizraj

                                List<App1Dokumenti> spisakDokZaSync = getDokumentZaSyncIliPrintanje(selektiranDok);

                                try {
                                    String rezultat=new JSON_send(App1DokumentiActivity.this,spisakDokZaSync).execute().get();
                                    Log.d(TAG, "onClick: REZULTAT ASYNCTASKA JE=>" + rezultat);
                                    if (rezultat.equals("OK")){

                                        //ucitajDokumente();
                                        MainActivity.updateZaglavljaPoslijeSinkronizacije(App1DokumentiActivity.this, spisakDokZaSync);
                                        ucitajDokumente();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                ucitajDokumente();
                                //Toast.makeText(App1DokumentiActivity.this,akcije[which].toString(),Toast.LENGTH_LONG).show();
                                break;


                            case 2:
                                //printaj

                                List<App1Dokumenti> spisakDokZaPrint = getDokumentZaSyncIliPrintanje(selektiranDok);
                                doPrint(spisakDokZaPrint.get(0));
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    private List<App1Dokumenti> getDokumentZaSyncIliPrintanje(App1Dokumenti selektiranDok) {
        List<App1Dokumenti> spisakDokZaSync = new ArrayList<App1Dokumenti>();
        spisakDokZaSync.add(selektiranDok);
        selektiranDok.izbrisiSveStavke();
        List<App1Stavke> mojeStavke = MainActivity.getListaStavki(selektiranDok.getId(), App1DokumentiActivity.this);
        for (App1Stavke stv : mojeStavke) {
            selektiranDok.doadajStavku(stv);
        }
        return spisakDokZaSync;
    }

    private void doPrint(App1Dokumenti dokumentZaPrint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            String jobName = "DocumentName.pdf";
            PrintAttributes printAttrs = new PrintAttributes.Builder().
                    setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME).
                    setMediaSize(PrintAttributes.MediaSize.ISO_A4.asPortrait()).
                    setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                    build();

            printManager.print(jobName, new MyPrintDocumentAdapter(this, dokumentZaPrint), printAttrs);
        } else {
            Toast.makeText(App1DokumentiActivity.this, "Nije podržano na ovome sistemu!!! (Android APIlevel > 19 - KitKat)", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                Long IdKomitenta=data.getLongExtra("idKomitenta",-1);
                Long IdPjKomitenta=data.getLongExtra("idPjKomitenta",-1);
                Long idTipDokumenta=data.getLongExtra("idTipDokumenta",-1);
                Long idPodtipDokumenta=data.getLongExtra("idPodtipDokumenta",-1);
                String datumDokumenta=data.getStringExtra("datumDokumenta");
                SimpleDateFormat dateFormat = new SimpleDateFormat(DatumFormat);

                Date datumDok=new Date();
                SimpleDateFormat dateSQLLiteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                String SQLDatum;
                try {
                    datumDok=dateFormat.parse(datumDokumenta);
                    SQLDatum= dateSQLLiteFormat.format(datumDok);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.GreskaUnosDatuma,Toast.LENGTH_LONG).show();
                    return;
                }

                String nazivPjKomitenta=data.getStringExtra("nazivPjKomitenta");
                String nazivKomitenta=data.getStringExtra("nazivKomitenta");
                String nazivTipDokumenta=data.getStringExtra("nazivTipDokumenta");
                String nazivPodtipDokumenta=data.getStringExtra("nazivPodtipDokumenta");
                String napomena=data.getStringExtra("napomena");
                if (napomena==null){
                    napomena="";
                }
                SQLiteDatabase myDB = null;
                Log.d(TAG, "Otvaram bazu");
                myDB = openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
                myDB.execSQL("INSERT INTO " + tabelaApp1 +" (idTip, TipDokumentaNaziv, idPodtip, PodipDokumentaNaziv, idKomitent, KomitentNaziv, idPjKomitenta, PjKomitentaNaziv, " +
                        "datumDokumenta, napomena) VALUES (" +
                        idTipDokumenta + ",'" + nazivTipDokumenta + "', " + idPodtipDokumenta + ",'" + nazivPodtipDokumenta + "', " + IdKomitenta + ",'" + nazivKomitenta + "', " +
                        IdPjKomitenta + ",'" + nazivPjKomitenta + "','" + SQLDatum +"','" + napomena + "');");
                myDB.close();
                ucitajDokumente();

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }


    private void kreirajTabeluDokumenata() {
        MainActivity.kreirajTabeluDokumenata(App1DokumentiActivity.this);
    }

    private void ucitajDokumente() {
        ListaApp1DokumentiAdapter listaDokumenta = new ListaApp1DokumentiAdapter(this, R.layout.row_app1_zaglavlje);
        listSpisakDokumenata.setAdapter(listaDokumenta);
        List<App1Dokumenti> spisakDok=MainActivity.getListaDokumenta(App1DokumentiActivity.this);
        for (App1Dokumenti dok:spisakDok) {
            listaDokumenta.add(dok);
        }
    }

}
