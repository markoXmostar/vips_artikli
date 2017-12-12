package com.example.marko.vips_artikli;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class JmjActivity extends AppCompatActivity {
    public static final String TAG = "JMJ";

    ListView JmjListView;
    TextView NoDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jmj);
        NoDataText = (TextView) findViewById(R.id.noDataText);
        NoDataText.setVisibility(View.INVISIBLE);
        JmjListView = (ListView) findViewById(R.id.jmjListView);
        JmjListView.setItemsCanFocus(false);

        UcitajListuIzBaze("");
    }

    private void UcitajListuIzBaze(String filter) {

        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        if (!MainActivity.isTableExists(mDatabase, "jmj")) {
            NoDataText.setVisibility(View.VISIBLE);
            NoDataText.setEnabled(false);
            return;
        }
        NoDataText.setVisibility(View.INVISIBLE);
        ListaJmjAdapter listaJmjAdapter = new ListaJmjAdapter(this, R.layout.row_jmj);
        JmjListView.setAdapter(listaJmjAdapter);
        Log.d(TAG, " ucitavam bazu!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM jmj", null);
        } else {
            c = myDB.rawQuery("SELECT * FROM jmj where naziv like '%" + filter + "%'", null);
        }

        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String naziv;
            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);

            jmj jmjProvider = new jmj(id, naziv);
            listaJmjAdapter.add(jmjProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Baza učitana!");
    }
}
