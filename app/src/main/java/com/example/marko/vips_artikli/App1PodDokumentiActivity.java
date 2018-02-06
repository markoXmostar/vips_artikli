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

import java.text.SimpleDateFormat;

import static com.example.marko.vips_artikli.MainActivity.DatumVrijemeFormat;
import static com.example.marko.vips_artikli.MainActivity.myDATABASE;

public class App1PodDokumentiActivity extends AppCompatActivity {
    private static String TAG = "App1Stavke";
    private static String tabelaApp1 = "stavke1";

    private long IdDokumenta=0;

    ListView listSpisakStavki;
    private FloatingActionButton fabNovaStavka;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_pod_dokumenti);



        Bundle b = getIntent().getExtras();
        IdDokumenta = b.getInt("idDokumenta");

        fabNovaStavka = (FloatingActionButton) findViewById(R.id.fabNovaStavka_App1);
        listSpisakStavki=(ListView)findViewById(R.id.listSpisakStavki_App1);





        kreirajTabeluStavki();
        ucitajStavke();

        fabNovaStavka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App1PodDokumentiActivity.this, App1UnosStavkeActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private void ucitajStavke() {
        ListaApp1DokumentiAdapter listaDokumenta = new ListaApp1DokumentiAdapter(this, R.layout.row_app1_stavke);
        listSpisakStavki.setAdapter(listaDokumenta);
        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM " + tabelaApp1 + " WHERE idDokumenta=" + IdDokumenta +" ORDER BY datumUpisa DESC", null);

        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(DatumVrijemeFormat);
        SimpleDateFormat SQLLite_dateFormat = new SimpleDateFormat(MainActivity.SqlLiteDateFormat);
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
                "vrijednostAtributa datetime, " +
                "idVrijednostiAtributa long," +
                "datumUpisa datetime default current_timestamp);");
        myDB.close();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
               /*
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
                */
                ucitajStavke();

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

}
