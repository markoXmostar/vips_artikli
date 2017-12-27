package com.example.marko.vips_artikli;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class PretragaKomitenataActivity extends AppCompatActivity {
    private static final String TAG="PretragaKomitenata";

    EditText txtFilter;
    ListView listKomitenti;

    private long idKomitenta;
    private String nazivKomitenta;
    private String sifraKomitenta;

    private String varijantaPretrage;
    private long nadredeniKomitentID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretraga_komitenata);
        varijantaPretrage="";
        txtFilter =(EditText)findViewById(R.id.etxtFilter_PretragaKomitenata);

        Intent iin= getIntent();
        Bundle extras = iin.getExtras();

        if(extras != null) {
            varijantaPretrage= extras.getString("varijanta","");
            Log.d(TAG, "onCreate: VARIJANTA =" + varijantaPretrage);
            if(varijantaPretrage.equals("pjkomitenti")){
                nadredeniKomitentID=extras.getLong("idKomitenta",-1);
            }
        }

        switch (varijantaPretrage){
            case "komitenti":
                listKomitenti=(ListView)findViewById(R.id.listKomitenti_PretragaKomitenata);
                ucitajKomitente("");
                break;
            case "pjkomitenti":
                Log.d(TAG, "onCreate: VARIJANTA=pjkomitenti " );
                Log.d(TAG, "onCreate: IDkomitenta=" + nadredeniKomitentID);
                listKomitenti=(ListView)findViewById(R.id.listKomitenti_PretragaKomitenata);
                ucitajPjKomitenta("");
                break;
            default:
                Log.d(TAG, "onCreate: NIJE IZABRANA VARIJANTA PRETRAGE!!!!");

        }





        txtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String myFilter=txtFilter.getText().toString();
                ucitajKomitente(myFilter);
            }
        });

        listKomitenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (varijantaPretrage){

                    case "komitenti":
                        Komitent kk=(Komitent)listKomitenti.getItemAtPosition(i);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("idKomitenta",kk.getId());
                        returnIntent.putExtra("nazivKomitenta",kk.getNaziv());
                        returnIntent.putExtra("sifraKomitenta",kk.getSifra());

                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                        break;
                    case "pjkomitenti":
                        PjKomitent pj=(PjKomitent)listKomitenti.getItemAtPosition(i);
                        Intent returnInt = new Intent();
                        returnInt.putExtra("idPjKomitenta",pj.getId());
                        returnInt.putExtra("nazivPjKomitenta",pj.getNaziv());
                        returnInt.putExtra("ridPjKomitenta",pj.getRid());
                        setResult(Activity.RESULT_OK,returnInt);
                        finish();
                        break;

                        default:

                }

            }
        });
    }



    private void ucitajKomitente(String filter){
        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);

        ListaKomitentPretragaAdapter listKomitenataAdapter = new ListaKomitentPretragaAdapter(this, R.layout.row_grupa);
        listKomitenti.setAdapter(listKomitenataAdapter);
        Log.d(TAG, " ucitavam bazu!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM komitenti", null);
        } else {
            c = myDB.rawQuery("SELECT * FROM komitenti where sifra like '%" + filter + "%' or naziv like '%" + filter + "%';", null);
        }

        int IdIndex = c.getColumnIndex("_id");
        int SifraIndex = c.getColumnIndex("sifra");
        int NazivIndex = c.getColumnIndex("naziv");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String sifra, naziv;

            id = c.getLong(IdIndex);
            sifra = c.getString(SifraIndex);
            naziv = c.getString(NazivIndex);


            Komitent komitentProvider = new Komitent(id, naziv, sifra);
            listKomitenataAdapter.add(komitentProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Baza učitana!");
        mDatabase.close();
    }

    private void ucitajPjKomitenta(String filter){
        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);

        ListaPjKomitenataPretragaAdapter listKomitenataAdapter = new ListaPjKomitenataPretragaAdapter(this, R.layout.row_pretraga_pjkomitenata);
        listKomitenti.setAdapter(listKomitenataAdapter);
        Log.d(TAG, " ucitavam bazu!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM pjkomitenta where rid =" + nadredeniKomitentID +";", null);
        } else {
            c = myDB.rawQuery("SELECT * FROM pjkomitenta where rid =" +nadredeniKomitentID+ " and naziv like '%" + filter + "%' ;", null);
        }

        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String  naziv;

            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);


            PjKomitent komitentPjProvider = new PjKomitent(id, naziv, nadredeniKomitentID);
            listKomitenataAdapter.add(komitentPjProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Baza učitana!");
        mDatabase.close();
    }
}
