package com.vanima.mvips.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.vanima.mvips.models.Komitent;
import com.vanima.mvips.adapters.ListaKomitentAdapter;
import com.vanima.mvips.R;

public class KomitentiActivity extends AppCompatActivity {

    public static final String TAG = "KOMITENTI";
    ListView artiklListView;
    TextView NoDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komitenti);

        NoDataText = findViewById(R.id.noDataText_komitent);
        NoDataText.setVisibility(View.INVISIBLE);

        artiklListView = findViewById(R.id.komitentiListView);
        artiklListView.setItemsCanFocus(false);

        UcitajListuIzBaze("");
    }

    private void UcitajListuIzBaze(String filter) {

        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        if (!MainActivity.isTableExists(mDatabase, "komitenti")) {
            NoDataText.setVisibility(View.VISIBLE);
            NoDataText.setEnabled(false);
            return;
        }
        NoDataText.setVisibility(View.INVISIBLE);
        ListaKomitentAdapter listKomitenataAdapter = new ListaKomitentAdapter(this, R.layout.row_grupa);
        artiklListView.setAdapter(listKomitenataAdapter);
        Log.d(TAG, " ucitavam bazu!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM komitenti", null);
        } else {
            c = myDB.rawQuery("SELECT * FROM komitenti where sifra like '%" + filter + "%' or naziv like '%" + filter + "%' or kataloskiBroj like '%" + filter + "%'", null);
        }

        int IdIndex = c.getColumnIndex("_id");
        int SifraIndex = c.getColumnIndex("sifra");
        int NazivIndex = c.getColumnIndex("naziv");
        int SaldoIndex = c.getColumnIndex("saldo");
        int uRokuIndex = c.getColumnIndex("uroku");
        int vanRokaIndex = c.getColumnIndex("vanroka");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String sifra, naziv;
            double saldo, uroku, vanroka;

            id = c.getLong(IdIndex);
            sifra = c.getString(SifraIndex);
            naziv = c.getString(NazivIndex);
            saldo = c.getDouble(SaldoIndex);
            uroku = c.getDouble(uRokuIndex);
            vanroka = c.getDouble(vanRokaIndex);

            Komitent komitentProvider = new Komitent(id, naziv, sifra, saldo, uroku, vanroka);
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
