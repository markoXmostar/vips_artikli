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
import java.util.Date;
import java.util.Locale;

import static com.example.marko.vips_artikli.MainActivity.DatumFormat;
import static com.example.marko.vips_artikli.MainActivity.DatumVrijemeFormat;
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
            Toast.makeText(App1DokumentiActivity.this,"Nije implementirana funkcija",Toast.LENGTH_LONG).show();
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
                Intent intent=new Intent(App1DokumentiActivity.this,App1ZaglavljeActivity.class);
                startActivityForResult(intent,1);
            }
        });

        listSpisakDokumenata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                App1Dokumenti selektiranDok=(App1Dokumenti) adapterView.getItemAtPosition(i);
                //Toast.makeText(App1DokumentiActivity.this,selektiranDok.getId() +"/" +selektiranDok.getDatumDokumentaString() + "-" + selektiranDok.getKomitentNaziv(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(App1DokumentiActivity.this, App1PodDokumentiActivity.class);
                intent.putExtra("idDokumenta", selektiranDok.getId());
                startActivity(intent);

            }
        });

        listSpisakDokumenata.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        App1DokumentiActivity.this);
                alert.setTitle(R.string.Upozorenje);
                alert.setMessage(R.string.UpitBrisanjeDokumenta);
                alert.setPositiveButton(R.string.Da, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton(R.string.Ne, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();
                */
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
                                Toast.makeText(App1DokumentiActivity.this,akcije[which].toString(),Toast.LENGTH_LONG).show();
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
                /*
                upisano:
                returnIntent.putExtra("idKomitenta",getIzabraniKomitent().getId());
                returnIntent.putExtra("idPjKomitenta",getIzabranaPJKomitenta().getId());
                returnIntent.putExtra("idTipDokumenta",getIzabraniTiP().getId());
                returnIntent.putExtra("idPodtipDokumenta", getIzabraniPodtip().getId());
                returnIntent.putExtra("datumDokumenta", izabraniDatum);
                returnIntent.putExtra("nazivKomitenta",getIzabraniKomitent().getNaziv());
                returnIntent.putExtra("nazivPjKomitenta",getIzabranaPJKomitenta().getNaziv());
                returnIntent.putExtra("nazivTipDokumenta",getIzabraniTiP().getNaziv());
                returnIntent.putExtra("nazivPodtipDokumenta", getIzabraniPodtip().getNaziv());
                returnIntent.putExtra("napomena",etxtNapomena.getText());
                 */
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
        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM " + tabelaApp1 + " ORDER BY datumUpisa DESC", null);

        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(DatumVrijemeFormat);
        SimpleDateFormat SQLLite_dateFormat = new SimpleDateFormat(MainActivity.SqlLiteDateFormat);

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

        int idIndex = c.getColumnIndex("_id");
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
                datumDokumenta = (Date) SQLLite_dateFormat.parse(datumDokumentaString);
            } catch (ParseException e) {
                e.printStackTrace();

            }
            try {
                if (datumSinkronizacijeString==null){
                    datumSinkronizacije=null;
                }else{
                    datumSinkronizacije = (Date) SQLLite_dateFormat.parse(datumSinkronizacijeString);
                }

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
