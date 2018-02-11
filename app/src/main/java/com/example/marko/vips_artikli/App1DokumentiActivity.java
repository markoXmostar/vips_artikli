package com.example.marko.vips_artikli;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.marko.vips_artikli.MainActivity.DatumFormat;
import static com.example.marko.vips_artikli.MainActivity.DatumVrijemeFormat;
import static com.example.marko.vips_artikli.MainActivity.ma;
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
        if (id == R.id.sinkroniziraj_dokumenti1) {
            //Toast.makeText(App1DokumentiActivity.this,"Nije implementirana funkcija",Toast.LENGTH_LONG).show();
            List<App1Dokumenti> spisakSvihDokumenta=MainActivity.getListaDokumenta(App1DokumentiActivity.this);
            List<App1Dokumenti> spisakDokumentaZaSync=MainActivity.getListaDokumenta(App1DokumentiActivity.this);
            for (App1Dokumenti dok:spisakSvihDokumenta) {
                if(dok.getDatumSinkronizacije()==null ){
                    spisakDokumentaZaSync.add(dok);
                }
            }

            for (App1Dokumenti dok:spisakDokumentaZaSync) {
                dok.izbrisiSveStavke();
                List<App1Stavke> mojeStavke=MainActivity.getListaStavki(dok.getId(),App1DokumentiActivity.this);
                for (App1Stavke stv:mojeStavke) {
                    dok.doadajStavku(stv);
                }
            }
            new SendJSON(spisakDokumentaZaSync).execute();
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
                startActivity(intent);

            }
        });

        listSpisakDokumenata.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final CharSequence akcije[] = new CharSequence[] {"Izbriši", "Sinkroniziraj", "Prikaži detalje"};
                final App1Dokumenti selektiranDok=(App1Dokumenti) adapterView.getItemAtPosition(i);
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
                                List<App1Dokumenti> spisakDokZaSync=new ArrayList<App1Dokumenti>();
                                spisakDokZaSync.add(selektiranDok);
                                selektiranDok.izbrisiSveStavke();
                                List<App1Stavke> mojeStavke=MainActivity.getListaStavki(selektiranDok.getId(),App1DokumentiActivity.this);
                                for (App1Stavke stv:mojeStavke) {
                                    selektiranDok.doadajStavku(stv);
                                }

                                new SendJSON(spisakDokZaSync).execute();

                                //Toast.makeText(App1DokumentiActivity.this,akcije[which].toString(),Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                //Detalji
                                Toast.makeText(App1DokumentiActivity.this,akcije[which].toString(),Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
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
        SQLiteDatabase myDB = null;
        Log.d(TAG, "Otvaram bazu");
        myDB = openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "Kreiram tabelu");
        //myDB.execSQL("DROP TABLE " + tabelaApp1 + ";");
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
                "datumUpisa datetime default current_timestamp," +
                "napomena VARCHAR);");
        myDB.close();

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
