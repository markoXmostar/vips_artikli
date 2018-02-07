package com.example.marko.vips_artikli;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.marko.vips_artikli.MainActivity.DatumVrijemeFormat;
import static com.example.marko.vips_artikli.MainActivity.myDATABASE;

public class App1StavkeActivity extends AppCompatActivity {
    private static String TAG = "App1Stavke";
    private static String tabelaApp1 = "stavke1";

    private long IdDokumenta=0;

    ListView listSpisakStavki;
    private FloatingActionButton fabNovaStavka;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_stavke);



        Bundle b = getIntent().getExtras();
        final long IdDok = b.getLong("idDokumenta");
        IdDokumenta = IdDok;
        this.setTitle("STAVKE idDok=[" + IdDok + "]");
        fabNovaStavka = (FloatingActionButton) findViewById(R.id.fabNovaStavka_App1);
        listSpisakStavki=(ListView)findViewById(R.id.listSpisakStavki_App1);





        kreirajTabeluStavki();
        ucitajStavke();

        fabNovaStavka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App1StavkeActivity.this, App1UnosStavkeActivity.class);
                intent.putExtra("idDokumenta",IdDokumenta);
                startActivityForResult(intent,1);
            }
        });
    }

    private void ucitajStavke() {
        Log.d(TAG, "ucitajStavke: IdDOKUMENTA=" + IdDokumenta);
        ListaApp1StavkeAdapter listaStavki = new ListaApp1StavkeAdapter(this, R.layout.row_app1_stavke);
        listSpisakStavki.setAdapter(listaStavki);
        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        Log.d(TAG, "ucitajStavke: " + "SELECT * FROM " + tabelaApp1 + " WHERE idDokumenta=" + IdDokumenta + " ORDER BY datumUpisa DESC");
        c = myDB.rawQuery("SELECT * FROM " + tabelaApp1 + " WHERE idDokumenta=" + IdDokumenta +" ORDER BY datumUpisa DESC", null);

        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(DatumVrijemeFormat);
        SimpleDateFormat SQLLite_dateFormat = new SimpleDateFormat(MainActivity.SqlLiteDateFormat);

        long idStavke;
        long idArtikla;
        String nazivArtikla;
        double kolicina;
        boolean imaAtribut;
        long idAtributa;
        String vrijednostAtributa;
        String nazivAtributa;
        long idJmj;
        String nazivJmj;
        String napomena;

        int idStavkeIndex = c.getColumnIndex("_id");
        int idArtiklaIndex = c.getColumnIndex("idArtikla");
        int nazivArtiklaIndex = c.getColumnIndex("nazivArtikla");
        int kolicinaIndex = c.getColumnIndex("kolicina");
        int imaAtributIndex = c.getColumnIndex("imaAtribut");
        int idAtributaIndex = c.getColumnIndex("idAtributa");
        int vrijednostAtributaIndex = c.getColumnIndex("vrijednostAtributa");
        int nazivAtributaIndex = c.getColumnIndex("nazivAtributa");
        int idJmjIndex = c.getColumnIndex("idJmj");
        int nazivJmjIndex = c.getColumnIndex("nazivJmj");
        int napomenaIndex = c.getColumnIndex("napomena");

        c.moveToFirst();
        int brojac = 0;
        Log.d(TAG, "ucitajStavke: UCITANO JE STAVKI =" + c.getCount());
        for (int j = 0; j < c.getCount(); j++) {
            idStavke = c.getLong(idStavkeIndex);
            idArtikla = c.getLong(idArtiklaIndex);
            nazivArtikla = c.getString(nazivArtiklaIndex);
            kolicina = c.getDouble(kolicinaIndex);
            imaAtribut = Boolean.parseBoolean(c.getString(imaAtributIndex));
            idAtributa = c.getLong(idAtributaIndex);
            vrijednostAtributa = c.getString(vrijednostAtributaIndex);
            nazivAtributa = c.getString(nazivAtributaIndex);
            idJmj = c.getLong(idJmjIndex);
            nazivJmj = c.getString(nazivJmjIndex);
            napomena = c.getString(napomenaIndex);


            App1Stavke myObj = new App1Stavke(idStavke, IdDokumenta, idArtikla, nazivArtikla, idJmj, nazivJmj, imaAtribut, idAtributa, vrijednostAtributa, nazivAtributa, kolicina, napomena);
            listaStavki.add(myObj);

            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        Log.d(TAG, "ucitajDokumente: U tabeli se nalazi " + brojac + " dokumenta!");
        c.close();
        myDB.close();

    }

    private void kreirajTabeluStavki() {
        SQLiteDatabase myDB = null;
        Log.d(TAG, "Otvaram bazu");
        myDB = openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "Kreiram tabelu");
        //myDB.execSQL("DROP TABLE " + tabelaApp1 + ";");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + tabelaApp1 + " (" +
                "_id Integer PRIMARY KEY AUTOINCREMENT, " +
                "idDokumenta long, " +
                "idArtikla long," +
                "nazivArtikla VARCHAR," +
                "kolicina decimal," +
                "imaAtribut boolean, " +
                "idAtributa long," +
                "vrijednostAtributa VARCHAR, " +
                "nazivAtributa VARCHAR," +
                "idJmj long," +
                "nazivJmj VARCHAR," +
                "napomena VARCHAR," +
                "datumUpisa datetime default current_timestamp);");
        myDB.close();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                App1Stavke rezultat = (App1Stavke) data.getSerializableExtra("stavka");
                SQLiteDatabase myDB = null;
                myDB = openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
                myDB.execSQL("INSERT INTO " + tabelaApp1 + " (idDokumenta, idArtikla, nazivArtikla, kolicina, imaAtribut, idAtributa, vrijednostAtributa, nazivAtributa, " +
                        "idJmj, nazivJmj,napomena ) VALUES (" +
                        IdDokumenta + "," + rezultat.getArtiklId() + ",'" + rezultat.getArtiklNaziv() + "'," + rezultat.getKolicina() + ", '" + rezultat.isImaAtribut() + "'," + rezultat.getAtributId() + ", '" +
                        rezultat.getAtributVrijednost() + "','" + rezultat.getAtributNaziv() + "'," + rezultat.getJmjId() + ",'" + rezultat.getJmjNaziv() + "','" + rezultat.getNapomena() + "');");
                myDB.close();

                ucitajStavke();

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

}
