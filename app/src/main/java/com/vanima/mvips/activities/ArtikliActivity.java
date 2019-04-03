package com.vanima.mvips.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.vanima.mvips.R;
import com.vanima.mvips.adapters.ListaArtikalaAdapter2;
import com.vanima.mvips.adapters.ListaArtiklaAdapter;
import com.vanima.mvips.models.App1Stavke;
import com.vanima.mvips.models.Artikl;
import com.vanima.mvips.models.ArtiklJmj;
import com.vanima.mvips.models.ArtiklSaKolicinom;
import com.vanima.mvips.models.jmjOdnos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ArtikliActivity extends AppCompatActivity {

    public static final String TAG="ARTIKLI";
    private int varijantaForme=99;
    private boolean unosKolicine = false;
    private long dokumentID = 0;

    private long pjKmtID = 0; // ovo služi za asortiman kupca

    ListView artiklListView;
    TextView NoDataText;

    boolean asortimanKupca = false;

    String myFilter="";
    private List<Artikl> listaSvihArtikala = new ArrayList<Artikl>();
    private List<Artikl> listaAsortimanaArtikala = new ArrayList<Artikl>();

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikli);

        getSupportActionBar().setTitle("Artikli");

        NoDataText = findViewById(R.id.noDataText);
        NoDataText.setVisibility(View.INVISIBLE);

        artiklListView = findViewById(R.id.artikliListView);


        Bundle b = getIntent().getExtras();
        if (b != null) {
            varijantaForme = b.getInt("varijanta");
            unosKolicine = b.getBoolean("unosKolicine");
            dokumentID = b.getLong("dokumentID");
            //asortimanKupca = b.getBoolean("asortimanKupca", false);
            pjKmtID = b.getLong("pjKmtID", 0);
        }
        if (unosKolicine) {
            getSupportActionBar().setTitle("Artikli unos preko liste");
            progressDialog=ProgressDialog.show(ArtikliActivity.this,null,"Pripremam listu. Molim pričekajte!",true);
            new UcitajUPozadini().execute();
        }else{
            postaviAdapterZaListu();
            artiklListView.setItemsCanFocus(false);
            artiklListView.setAdapter(myAdapterOLD);
        }

        artiklListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (varijantaForme==0){

                    Artikl selektiraniArtikl=(Artikl)adapterView.getItemAtPosition(position);
                    Log.d(TAG, "greška1: " + selektiraniArtikl.getNaziv());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("artikl",selektiraniArtikl);
                    //Toast.makeText(ArtikliActivity.this,selektiraniArtikl.getImaRokTrajanja(),Toast.LENGTH_LONG).show();
                    setResult(ArtikliActivity.this.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_artikli_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.artikli_pretraga);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String sFilter) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String sFilter) {
                myFilter=sFilter;
                if (unosKolicine){
                    myAdapter.setAsortiman(asortimanKupca);
                    myAdapter.getFilter().filter(myFilter);
                }else{
                    myAdapterOLD.setAsortiman(asortimanKupca);
                    myAdapterOLD.getFilter().filter(myFilter);
                }

                return false;
            }
        });
        return true;
    }

    private class UcitajUPozadini extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            postaviAdapterZaListu();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (unosKolicine){
                artiklListView.setItemsCanFocus(true);
                artiklListView.setAdapter(myAdapter);
            }else{
                artiklListView.setItemsCanFocus(false);
                artiklListView.setAdapter(myAdapterOLD);
            }
            progressDialog.dismiss();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.artikli_pretraga) {
            return true;
        }

        if (id==R.id.asortiman_kupca_switch){
            if(item.isChecked()){
                item.setChecked(false);
                asortimanKupca=false;
            }
            else{
                item.setChecked(true);
                asortimanKupca=true;

            }
            if (unosKolicine){
                myAdapter.setAsortiman(asortimanKupca);
                myAdapter.getFilter().filter(myFilter);
            }else{
                myAdapterOLD.setAsortiman(asortimanKupca);
                myAdapterOLD.getFilter().filter(myFilter);
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Artikl> UcitajSveArtikleIzBaze() {
        ArrayList<Artikl> mojaLista = new ArrayList<Artikl>();

        SQLiteDatabase mDatabase = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE,null);
        if (!MainActivity.isTableExists(mDatabase,"artikli")){
            NoDataText.setVisibility(View.VISIBLE);
            NoDataText.setEnabled(false);
            return null;
        }
        NoDataText.setVisibility(View.INVISIBLE);

        Log.d(TAG," ucitavam bazu!");

        SQLiteDatabase myDB=this.openOrCreateDatabase(MainActivity.myDATABASE,this.MODE_PRIVATE,null);
        Cursor c;
        String sql;
        sql = "SELECT  * FROM artikli ;";
        c = myDB.rawQuery(sql, null);

        int IdIndex=c.getColumnIndex("_id");
        int SifraIndex=c.getColumnIndex("sifra");
        int NazivIndex=c.getColumnIndex("naziv");
        int ProizvodacIndex=c.getColumnIndex("proizvodjac");
        int KataloskiBrojIndex=c.getColumnIndex("kataloskiBroj");
        int KratkiOpisIndex=c.getColumnIndex("kratkiOpis");
        int JmjIndex = c.getColumnIndex("jmjId");
        int JmjNazivIndex = c.getColumnIndex("jmjNaziv");
        int DugiOpisIndex=c.getColumnIndex("dugiOpis");
        int VrstaAmbalazeIndex=c.getColumnIndex("vrstaAmbalaze");
        int BrojKoletaIndex=c.getColumnIndex("brojKoleta");
        int BrojKoletaNaPaletiIndex=c.getColumnIndex("brojKoletaNaPaleti");
        int StanjeIndex=c.getColumnIndex("stanje");
        int VpcIndex=c.getColumnIndex("vpc");
        int MpcIndex=c.getColumnIndex("mpc");
        int NettoIndex=c.getColumnIndex("netto");
        int BruttoIndex=c.getColumnIndex("brutto");
        int ImaRokTrajanjaIndex=c.getColumnIndex("imaRokTrajanja");
        int PodgrupaIdIndex=c.getColumnIndex("podgrupaID");

        c.moveToFirst();
        int brojac=0;
        for (int j=0;j<c.getCount();j++){
            long id, jmjId;
            String sifra, naziv, jmjNaziv, kataloskiBroj, kratkiOpis, proizvodjac, dugiOpis, vrstaAmbalaze;
            double brojKoleta, brojKoletaNaPaleti, stanje, vpc, mpc, netto, brutto;
            boolean imaRokTrajanja;
            int podgrupaID;

            id=c.getLong(IdIndex);
            sifra=c.getString(SifraIndex);
            naziv=c.getString(NazivIndex);
            proizvodjac=c.getString(ProizvodacIndex);
            kataloskiBroj=c.getString(KataloskiBrojIndex);
            kratkiOpis=c.getString(KratkiOpisIndex);
            jmjId = c.getLong(JmjIndex);
            jmjNaziv = c.getString(JmjNazivIndex);
            dugiOpis=c.getString(DugiOpisIndex);
            vrstaAmbalaze=c.getString(VrstaAmbalazeIndex);
            brojKoleta=c.getDouble(BrojKoletaIndex);
            brojKoletaNaPaleti=c.getDouble(BrojKoletaNaPaletiIndex);
            stanje=c.getDouble(StanjeIndex);
            vpc=c.getDouble(VpcIndex);
            mpc=c.getDouble(MpcIndex);
            netto=c.getDouble(NettoIndex);
            brutto=c.getDouble(BruttoIndex);
            boolean vrijednostImaAtribut = (c.getInt(ImaRokTrajanjaIndex) == 1);
            imaRokTrajanja=vrijednostImaAtribut;
            podgrupaID=c.getInt(PodgrupaIdIndex);

            //Log.d(TAG," Red " + Integer.toString(brojac));
            Artikl artikl = new Artikl(id, sifra, naziv, kataloskiBroj, jmjId, jmjNaziv, kratkiOpis, proizvodjac, dugiOpis, vrstaAmbalaze, brojKoleta, brojKoletaNaPaleti, stanje, vpc, mpc, netto, brutto, imaRokTrajanja, podgrupaID);


            mojaLista.add(artikl);
            brojac++;
            if (j!=c.getCount()){
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG," Baza učitana!");
        mDatabase.close();

        Log.d(TAG, "xxxx: Broj svih artikala:" + mojaLista.size());
        return mojaLista;
    }

    public ListaArtikalaAdapter2 myAdapter;
    public ListaArtiklaAdapter myAdapterOLD;

    private void postaviAdapterZaListu() {
        Date currentTimeA = Calendar.getInstance().getTime();
        listaSvihArtikala = UcitajSveArtikleIzBaze();
        listaAsortimanaArtikala=getAsortimanKupca();
        for(Artikl art:listaSvihArtikala){
            if (listaAsortimanaArtikala.contains(art)){
                art.setAsortimanKupca(true);
            }
        }
        Date currentTimeB = Calendar.getInstance().getTime();
        long diff_0 = currentTimeB.getTime() - currentTimeA.getTime();
        Log.d(TAG, "12345: Vrijeme označavanja asortimana je:" +diff_0);
        Log.d(TAG, "12345: PJ_Komitent ID je:" +pjKmtID);
        if (unosKolicine) {
            //ubačeno za učitavanje liste JMJ za svaki artikal
            Date currentTime1 = Calendar.getInstance().getTime();
            List<ArtiklJmj> listaSaJMJ=MainActivity.getListaSvihArtiklSaSvojimJMJ(ArtikliActivity.this);
            Date currentTime2 = Calendar.getInstance().getTime();
            long diff = currentTime2.getTime() - currentTime1.getTime();
            Log.d(TAG, "12345: Vrijeme ucitavanja VIEWa liste ArtiklaSaJMJ je:" +diff);

            ArrayList<ArtiklSaKolicinom> myItems = new ArrayList<ArtiklSaKolicinom>();
            for (int i = 0; i < listaSvihArtikala.size(); i++) {
                ArtiklSaKolicinom artKol = new ArtiklSaKolicinom(listaSvihArtikala.get(i), 0,getListJMJ(listaSvihArtikala.get(i).getId(),listaSaJMJ));
                if (artKol.getArt().getImaRokTrajanja()==0){
                    myItems.add(artKol);
                }

            }
            Date currentTime3 = Calendar.getInstance().getTime();
            diff = currentTime3.getTime() - currentTime2.getTime();
            Log.d(TAG, "12345: Vrijeme pripremanja liste ArtiklaSaJMJ je:" +diff);
            List<App1Stavke> listaStavki=MainActivity.getListaStavki(dokumentID, ArtikliActivity.this);
            for(ArtiklSaKolicinom spisakArtikala : myItems) {
                for (App1Stavke stavka : listaStavki) {
                    if (stavka.getArtiklId() == spisakArtikala.getArt().getId()){
                        spisakArtikala.setKolicina(stavka.getKolicina());
                    }
                }
            }
            myAdapter = new ListaArtikalaAdapter2(ArtikliActivity.this, myItems, dokumentID);
            //artiklListView.setAdapter(myAdapter);
        } else {
            artiklListView.setItemsCanFocus(false);
            myAdapterOLD = new ListaArtiklaAdapter(ArtikliActivity.this, R.layout.row_artikl);
            myAdapterOLD.clear();
            for (int i = 0; i < listaSvihArtikala.size(); i++) {
                myAdapterOLD.add(listaSvihArtikala.get(i));
            }
            artiklListView.setAdapter(myAdapterOLD);
        }

    }

    private List<jmjOdnos> getListJMJ(long _artID,List<ArtiklJmj> listaSaJMJ){
        //for(Artikl glavna:listaSvihArtikala) {
        List<ArtiklJmj> zaBrisati=new ArrayList<ArtiklJmj>();
        ArrayList<jmjOdnos> myArtListaJMJ=new ArrayList<jmjOdnos>();
            for (ArtiklJmj stv : listaSaJMJ) {
                if (stv.getArtiklID()==_artID){
                    jmjOdnos jmjOdnos=new jmjOdnos(stv.getJmjID(),stv.getOdnos(),stv.getNazivJMJ());
                    myArtListaJMJ.add(jmjOdnos);
                    zaBrisati.add(stv);
                }
            }
            listaSaJMJ.removeAll(zaBrisati);
        //}
        return myArtListaJMJ;
    }

    private List<Artikl> getAsortimanKupca(){
        ArrayList<Artikl> myList = new ArrayList<Artikl>();
        SQLiteDatabase myDB=this.openOrCreateDatabase(MainActivity.myDATABASE,this.MODE_PRIVATE,null);
        Cursor c;
        String sql;
        sql = "SELECT * FROM artikli  WHERE _id IN (SELECT artiklId FROM asortimankupca WHERE pjKmtId=" + pjKmtID + ") ;";
        c = myDB.rawQuery(sql, null);
        int IdIndex=c.getColumnIndex("_id");
        int SifraIndex=c.getColumnIndex("sifra");
        int NazivIndex=c.getColumnIndex("naziv");
        int ProizvodacIndex=c.getColumnIndex("proizvodjac");
        int KataloskiBrojIndex=c.getColumnIndex("kataloskiBroj");
        int KratkiOpisIndex=c.getColumnIndex("kratkiOpis");
        int JmjIndex = c.getColumnIndex("jmjId");
        int JmjNazivIndex = c.getColumnIndex("jmjNaziv");
        int DugiOpisIndex=c.getColumnIndex("dugiOpis");
        int VrstaAmbalazeIndex=c.getColumnIndex("vrstaAmbalaze");
        int BrojKoletaIndex=c.getColumnIndex("brojKoleta");
        int BrojKoletaNaPaletiIndex=c.getColumnIndex("brojKoletaNaPaleti");
        int StanjeIndex=c.getColumnIndex("stanje");
        int VpcIndex=c.getColumnIndex("vpc");
        int MpcIndex=c.getColumnIndex("mpc");
        int NettoIndex=c.getColumnIndex("netto");
        int BruttoIndex=c.getColumnIndex("brutto");
        int ImaRokTrajanjaIndex=c.getColumnIndex("imaRokTrajanja");
        int PodgrupaIdIndex=c.getColumnIndex("podgrupaID");
        c.moveToFirst();
        int brojac=0;
        for (int j=0;j<c.getCount();j++){
            long id, jmjId;
            String sifra, naziv, jmjNaziv, kataloskiBroj, kratkiOpis, proizvodjac, dugiOpis, vrstaAmbalaze;
            double brojKoleta, brojKoletaNaPaleti, stanje, vpc, mpc, netto, brutto;
            boolean imaRokTrajanja;
            int podgrupaID;

            id=c.getLong(IdIndex);
            sifra=c.getString(SifraIndex);
            naziv=c.getString(NazivIndex);
            proizvodjac=c.getString(ProizvodacIndex);
            kataloskiBroj=c.getString(KataloskiBrojIndex);
            kratkiOpis=c.getString(KratkiOpisIndex);
            jmjId = c.getLong(JmjIndex);
            jmjNaziv = c.getString(JmjNazivIndex);
            dugiOpis=c.getString(DugiOpisIndex);
            vrstaAmbalaze=c.getString(VrstaAmbalazeIndex);
            brojKoleta=c.getDouble(BrojKoletaIndex);
            brojKoletaNaPaleti=c.getDouble(BrojKoletaNaPaletiIndex);
            stanje=c.getDouble(StanjeIndex);
            vpc=c.getDouble(VpcIndex);
            mpc=c.getDouble(MpcIndex);
            netto=c.getDouble(NettoIndex);
            brutto=c.getDouble(BruttoIndex);
            boolean vrijednostImaAtribut = (c.getInt(ImaRokTrajanjaIndex) == 1);
            imaRokTrajanja=vrijednostImaAtribut;
            podgrupaID=c.getInt(PodgrupaIdIndex);

            //Log.d(TAG," Red " + Integer.toString(brojac));
            Artikl artikl = new Artikl(id, sifra, naziv, kataloskiBroj, jmjId, jmjNaziv, kratkiOpis, proizvodjac, dugiOpis, vrstaAmbalaze, brojKoleta, brojKoletaNaPaleti, stanje, vpc, mpc, netto, brutto, imaRokTrajanja, podgrupaID);
            myList.add(artikl);
            brojac++;
            if (j!=c.getCount()){
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, "xxxx: Broj artikala u asortimanu je:" + myList.size());
        return myList;
    }


}
