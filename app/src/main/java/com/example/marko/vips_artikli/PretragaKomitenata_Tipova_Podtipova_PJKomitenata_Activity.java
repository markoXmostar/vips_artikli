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

public class PretragaKomitenata_Tipova_Podtipova_PJKomitenata_Activity extends AppCompatActivity {
    private static final String TAG="PretragaKomitenata";

    EditText txtFilter;
    ListView listaObjekata;

    private long idKomitenta;
    private String nazivKomitenta;
    private String sifraKomitenta;

    private String varijantaPretrage;
    private long nadredeniKomitentID=0;
    private long nadredeniTipID=0;

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
            if(varijantaPretrage.equals("podtipDokumenta")){
                nadredeniTipID=extras.getLong("idTipDokumenta",-1);
            }
        }

        switch (varijantaPretrage){
            case "komitenti":
                this.setTitle("Pretraga: komitenti");
                listaObjekata =(ListView)findViewById(R.id.listKomitenti_PretragaKomitenata);
                ucitajKomitente("");
                break;
            case "pjkomitenti":
                this.setTitle("Pretraga: PJ komitenata");
                Log.d(TAG, "onCreate: VARIJANTA=pjkomitenti " );
                Log.d(TAG, "onCreate: IDkomitenta=" + nadredeniKomitentID);
                listaObjekata =(ListView)findViewById(R.id.listKomitenti_PretragaKomitenata);
                ucitajPjKomitenta("");
                break;
            case "tipDokumenta":
                this.setTitle("Pretraga: tipovi dokumenata");
                Log.d(TAG, "onCreate: VARIJANTA=tipDokumenta " );
                listaObjekata =(ListView)findViewById(R.id.listKomitenti_PretragaKomitenata);
                ucitajTipDokumenta("");
                break;
            case "podtipDokumenta":
                this.setTitle("Pretraga: podtipovi dokumenata");
                Log.d(TAG, "onCreate: VARIJANTA=podtipDokumenta " );
                Log.d(TAG, "onCreate: IDtipa=" + nadredeniTipID);
                listaObjekata =(ListView)findViewById(R.id.listKomitenti_PretragaKomitenata);
                ucitajPodtipDokumenta("");
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
                switch (varijantaPretrage){
                    case "komitenti":
                        ucitajKomitente(myFilter);
                        break;
                    case "pjkomitenti":
                        ucitajPjKomitenta(myFilter);
                        break;
                    case "tipDokumenta":
                        ucitajTipDokumenta(myFilter);
                        break;
                    case"podtipDokumenta":
                        ucitajPodtipDokumenta(myFilter);
                        break;
                }

            }
        });

        listaObjekata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (varijantaPretrage){

                    case "komitenti":
                        Komitent kk=(Komitent) listaObjekata.getItemAtPosition(i);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("komitent", kk);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                        break;
                    case "pjkomitenti":
                        PjKomitent pj=(PjKomitent) listaObjekata.getItemAtPosition(i);
                        Intent returnInt = new Intent();
                        returnInt.putExtra("idPjKomitenta",pj.getId());
                        returnInt.putExtra("nazivPjKomitenta",pj.getNaziv());
                        returnInt.putExtra("ridPjKomitenta",pj.getRid());
                        setResult(Activity.RESULT_OK,returnInt);
                        finish();
                        break;
                    case "tipDokumenta":
                        TipDokumenta td=(TipDokumenta) listaObjekata.getItemAtPosition(i);
                        Intent returnTd=new Intent();
                        returnTd.putExtra("idTipDokumenta",td.getId());
                        returnTd.putExtra("nazivTipDokumenta",td.getNaziv());
                        setResult(Activity.RESULT_OK,returnTd);
                        finish();
                        break;
                    case "podtipDokumenta":
                        PodtipDokumenta podTip=(PodtipDokumenta) listaObjekata.getItemAtPosition(i);
                        Intent returnPod = new Intent();
                        returnPod.putExtra("idPodtipDokumenta",podTip.getId());
                        returnPod.putExtra("nazivPodtipDokumenta",podTip.getNaziv());
                        returnPod.putExtra("ridPodtipDokumenta",podTip.getRid());
                        setResult(Activity.RESULT_OK,returnPod);
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
        listaObjekata.setAdapter(listKomitenataAdapter);
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

    private void ucitajPjKomitenta(String filter){
        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);

        ListaPjKomitenataPretragaAdapter listKomitenataAdapter = new ListaPjKomitenataPretragaAdapter(this, R.layout.row_pretraga_pjkomitenata);
        listaObjekata.setAdapter(listKomitenataAdapter);
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

    private void ucitajTipDokumenta(String filter){
        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);

        ListaTipDokumentaPretragaAdapter listaTipovaDok = new ListaTipDokumentaPretragaAdapter(this, R.layout.row_pretraga_pjkomitenata);
        listaObjekata.setAdapter(listaTipovaDok);
        Log.d(TAG, " ucitavam bazu!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM tip_dokumenta", null);
        } else {
            c = myDB.rawQuery("SELECT * FROM tip_dokumenta where naziv like '%" + filter + "%';", null);
        }

        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String sifra, naziv;

            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);


            TipDokumenta tipDokProvider = new TipDokumenta(id, naziv);
            listaTipovaDok.add(tipDokProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, " Baza učitana!");
        mDatabase.close();
    }

    private void ucitajPodtipDokumenta(String filter){
        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);

        ListaPodtipDokumentaPretragaAdapter listPodtipDok = new ListaPodtipDokumentaPretragaAdapter(this, R.layout.row_pretraga_pjkomitenata);
        listaObjekata.setAdapter(listPodtipDok);
        Log.d(TAG, " ucitavam bazu!");

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM podtip_dokumenta where rid =" + nadredeniTipID +";", null);
        } else {
            c = myDB.rawQuery("SELECT * FROM podtip_dokumenta where rid =" +nadredeniTipID+ " and naziv like '%" + filter + "%' ;", null);
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


            PodtipDokumenta podtipDok = new PodtipDokumenta(id, naziv, nadredeniTipID);
            listPodtipDok.add(podtipDok);
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
