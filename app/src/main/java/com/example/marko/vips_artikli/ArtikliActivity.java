package com.example.marko.vips_artikli;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

public class ArtikliActivity extends AppCompatActivity {

    public static final String TAG="ARTIKLI";

    ListView artiklListView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_artikli_menu, menu);

        MenuItem searchItem=menu.findItem(R.id.artikli_pretraga);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String sFilter) {
                /*
                if(sFilter.equals("")){
                    UcitajListuIzBaze("");
                    //this.onQueryTextSubmit("");
                }
                UcitajListuIzBaze(sFilter);
                */
                return false;
            }

            @Override
            public boolean onQueryTextChange(String sFilter) {
                UcitajListuIzBaze(sFilter);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikli);


        artiklListView = (ListView) findViewById(R.id.artikliListView);
        artiklListView.setItemsCanFocus(false);

        UcitajListuIzBaze("");
    }

    private void UcitajListuIzBaze(String filter){

        ListaArtiklaAdapter listArtikalaAdapter=new ListaArtiklaAdapter(this,R.layout.row_artikl);
        artiklListView.setAdapter(listArtikalaAdapter);
        Log.d(TAG," ucitavam bazu!");

        SQLiteDatabase myDB=this.openOrCreateDatabase(MainActivity.myDATABASE,this.MODE_PRIVATE,null);
        Cursor c;
        if (filter.equals("")) {
            c=myDB.rawQuery("SELECT * FROM artikli",null);
        }else{
            c=myDB.rawQuery("SELECT * FROM artikli where sifra like '%" + filter + "%' or naziv like '%" + filter + "%' or kataloskiBroj like '%" + filter + "%'" ,null);
        }




        int IdIndex=c.getColumnIndex("_id");
        int SifraIndex=c.getColumnIndex("sifra");
        int NazivIndex=c.getColumnIndex("naziv");
        int ProizvodacIndex=c.getColumnIndex("proizvodjac");
        int KataloskiBrojIndex=c.getColumnIndex("kataloskiBroj");
        int KratkiOpisIndex=c.getColumnIndex("kratkiOpis");
        int JmjIndex=c.getColumnIndex("jmj");
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
            long id;
            String sifra, naziv, kataloskiBroj, jmj, kratkiOpis, proizvodjac, dugiOpis, vrstaAmbalaze;
            double brojKoleta, brojKoletaNaPaleti, stanje, vpc, mpc, netto, brutto;
            boolean imaRokTrajanja;
            int podgrupaID;

            id=c.getLong(IdIndex);
            sifra=c.getString(SifraIndex);
            naziv=c.getString(NazivIndex);
            proizvodjac=c.getString(ProizvodacIndex);
            kataloskiBroj=c.getString(KataloskiBrojIndex);
            kratkiOpis=c.getString(KratkiOpisIndex);
            jmj=c.getString(JmjIndex);
            dugiOpis=c.getString(DugiOpisIndex);
            vrstaAmbalaze=c.getString(VrstaAmbalazeIndex);
            brojKoleta=c.getDouble(BrojKoletaIndex);
            brojKoletaNaPaleti=c.getDouble(BrojKoletaNaPaletiIndex);
            stanje=c.getDouble(StanjeIndex);
            vpc=c.getDouble(VpcIndex);
            mpc=c.getDouble(MpcIndex);
            netto=c.getDouble(NettoIndex);
            brutto=c.getDouble(BruttoIndex);
            imaRokTrajanja=false;
            podgrupaID=c.getInt(PodgrupaIdIndex);


            //Log.d(TAG," Red " + Integer.toString(brojac));
            Artikl artikliProvider=new Artikl(id,sifra,naziv,kataloskiBroj,jmj,kratkiOpis,proizvodjac,dugiOpis,vrstaAmbalaze,brojKoleta,brojKoletaNaPaleti,stanje,vpc,mpc,netto,brutto,imaRokTrajanja,podgrupaID);


            listArtikalaAdapter.add(artikliProvider);
            brojac++;
            if (j!=c.getCount()){
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG," Baza učitana!");
    }

}
