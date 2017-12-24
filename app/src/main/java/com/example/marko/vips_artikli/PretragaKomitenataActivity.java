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
import android.widget.EditText;
import android.widget.ListView;

public class PretragaKomitenataActivity extends AppCompatActivity {
    private static final String TAG="PretragaKomitenata";

    EditText txtFilter;
    ListView listKomitenti;

    private long idKomitenta;
    private String nazivKomitenta;
    private String sifraKomitenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretraga_komitenata);

        txtFilter =(EditText)findViewById(R.id.etxtFilter_PretragaKomitenata);
        listKomitenti=(ListView)findViewById(R.id.listKomitenti_PretragaKomitenata);

        ucitajKomitente("");

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
    }

    private void btnOK_click(View view){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("idKomitenta",idKomitenta);
        returnIntent.putExtra("nazivKomitenta",nazivKomitenta);
        returnIntent.putExtra("sifraKomitenta",sifraKomitenta);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void btnCancel_click(View view){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
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
}
