package com.vanima.mvips.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
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
    private List<Artikl> listaArtikala = new ArrayList<Artikl>();


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
        }
        ListaArtiklaAdapter myAdapter = new ListaArtiklaAdapter(this, R.layout.row_artikl);
        postaviAdapterZaListu("");

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

                myAdapter.getFilter().filter(sFilter);
                /*myFilter=sFilter;
                postaviAdapterZaListu(myFilter);*/
                return false;
            }
        });

        return true;
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
                postaviAdapterZaListu(myFilter);
            }
            else{
                item.setChecked(true);
                asortimanKupca=true;

                postaviAdapterZaListu(myFilter);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Artikl> UcitajListuIzBaze(String filter) {
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
        if (filter.equals("")) {
            String sql;
            if (asortimanKupca) {
                sql = "SELECT * FROM artikli  WHERE _id IN (SELECT artiklId FROM asortimankupca WHERE pjKmtId=" + pjKmtID + ") ;";
            } else {
                sql = "SELECT  * FROM artikli ;";
            }
            c = myDB.rawQuery(sql, null);
        }else{
            String sql;
            if (asortimanKupca) {
                sql = "SELECT  * FROM artikli WHERE _id IN (SELECT artiklId FROM asortimankupca WHERE pjKmtId=" + pjKmtID + ") AND (sifra like '%" + filter + "%' or naziv like '%" + filter + "%' or kataloskiBroj like '%" + filter + "%');";
            } else {
                sql = "SELECT  * FROM artikli where sifra like '%" + filter + "%' or naziv like '%" + filter + "%' or kataloskiBroj like '%" + filter + "%';";
            }
            c = myDB.rawQuery(sql, null);
        }

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


        return mojaLista;
    }

    public ListaArtikalaAdapter2 myAdapter;

    private void postaviAdapterZaListu(String filter) {
        listaArtikala = UcitajListuIzBaze(filter);
        if (unosKolicine) {
            artiklListView.setItemsCanFocus(true);
            ArrayList<ArtiklSaKolicinom> myItems = new ArrayList<ArtiklSaKolicinom>();
            for (int i = 0; i < listaArtikala.size(); i++) {
                ArtiklSaKolicinom artKol = new ArtiklSaKolicinom(listaArtikala.get(i), 0);
                if (artKol.getArt().getImaRokTrajanja()==0){
                    myItems.add(artKol);
                }

            }
            List<App1Stavke> listaStavki=MainActivity.getListaStavki(dokumentID, ArtikliActivity.this);
            for(ArtiklSaKolicinom spisakArtikala : myItems) {
                for (App1Stavke stavka : listaStavki) {
                    if (stavka.getArtiklId() == spisakArtikala.getArt().getId()){
                        spisakArtikala.setKolicina(stavka.getKolicina());
                    }
                }
            }
            myAdapter = new ListaArtikalaAdapter2(ArtikliActivity.this, myItems, dokumentID);
            artiklListView.setAdapter(myAdapter);
        } else {
            artiklListView.setItemsCanFocus(false);
            ListaArtiklaAdapter myAdapterOLD = new ListaArtiklaAdapter(ArtikliActivity.this, R.layout.row_artikl);
            myAdapterOLD.clear();
            for (int i = 0; i < listaArtikala.size(); i++) {
                myAdapterOLD.add(listaArtikala.get(i));
            }
            artiklListView.setAdapter(myAdapterOLD);
        }

    }



}
