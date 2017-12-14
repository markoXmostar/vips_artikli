package com.example.marko.vips_artikli;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class RegistriActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String TAG = "REGISTRI";

    String[] registri = {"JMJ", "Tip dokumenta", "Podtip dokumenta", "Grupa artikala", "Podgrupa artikala", "Način plaćanja"};

    ListView mojListView;
    TextView NoDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registri);
        Spinner spin = (Spinner) findViewById(R.id.simpleSpinner);
        NoDataText = (TextView) findViewById(R.id.NoDataText);
        NoDataText.setVisibility(View.INVISIBLE);
        mojListView = (ListView) findViewById(R.id.mojaLista);
        mojListView.setItemsCanFocus(false);

        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, registri);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        String izabran = registri[position];
        Log.d(TAG, "Izabran je:" + izabran);
        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        switch (izabran) {
            case "JMJ":
                UcitajListuJMJIzBaze("");
            case "":

            default:

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private void UcitajListuJMJIzBaze(String filter) {

        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        if (!MainActivity.isTableExists(mDatabase, "jmj")) {
            NoDataText.setVisibility(View.VISIBLE);
            NoDataText.setEnabled(false);
            return;
        }
        NoDataText.setVisibility(View.INVISIBLE);
        ListaJmjAdapter listaJmjAdapter = new ListaJmjAdapter(this, R.layout.row_jmj);
        mojListView.setAdapter(listaJmjAdapter);
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
