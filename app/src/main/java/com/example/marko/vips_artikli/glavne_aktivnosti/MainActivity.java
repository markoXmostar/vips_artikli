package com.example.marko.vips_artikli.glavne_aktivnosti;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.marko.vips_artikli.dataclass.App1Dokumenti;
import com.example.marko.vips_artikli.dataclass.App1Stavke;
import com.example.marko.vips_artikli.dataclass.App2Dokumenti;
import com.example.marko.vips_artikli.dataclass.App2Stavke;
import com.example.marko.vips_artikli.dataclass.Artikl;
import com.example.marko.vips_artikli.dataclass.ArtiklAtributStanje;
import com.example.marko.vips_artikli.dataclass.ArtiklJmj;
import com.example.marko.vips_artikli.JSON_recive;
import com.example.marko.vips_artikli.JSON_send;
import com.example.marko.vips_artikli.adapters.ListaDbLogAdapter;
import com.example.marko.vips_artikli.dataclass.NacinPlacanja;
import com.example.marko.vips_artikli.dataclass.PodtipDokumenta;
import com.example.marko.vips_artikli.R;
import com.example.marko.vips_artikli.dataclass.TipDokumenta;
import com.example.marko.vips_artikli.dataclass.dbLog;
import com.example.marko.vips_artikli.dataclass.jmj;
import com.example.marko.vips_artikli.postavkeAplikacije;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG="Glavni MainActivity";

    public static final int UREDJAJ = 1; //ovo poslije treba doraditi

    public static final String myDATABASE="VIPS.db";
    public static final String SqlLiteDateFormat="yyyy-MM-dd HH:mm:ss";
    public static String DatumVrijemeFormat = "dd.MM.yyyy HH:mm:ss";
    public static String DatumFormat="dd.MM.yyyy";
    public static String BorisovFormatDatuma = "yyyy-MM-dd'T'HH:mm:ss";

    public static int DJELATNIK = 2;
    public static String url = "http://vanima.net:8099/api/";


    boolean lastPotrebnaSyncVisible;
    boolean lastVidljiveTxtKontrole;

    public static final String APP_POSTAVKE = "MyPostavke";
    postavkeAplikacije myPostavke;

    public static int zadnjaSinkronizacijaID;

    public static void setZadnjaSinkronizacijaVrijeme(Date zadnjaSinkronizacijaVrijeme) {
        MainActivity._zadnjaSinkronizacijaVrijeme = zadnjaSinkronizacijaVrijeme;
    }

    public static Date getZadnjaSinkronizacijaVrijeme() {
        return _zadnjaSinkronizacijaVrijeme;
    }

    private static Date _zadnjaSinkronizacijaVrijeme;

    public List<UrlTabele> spisakSyncTabela;

    Button btnViewLog, btnApp1, btnApp2, btnApp3, btnDownloadPodataka, btnPostavke;

    View glavniView;

    Integer lastSyncID=0;

    public static int zadanaVrstaAplikacija = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity ma=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fabUpdatePodataka = (FloatingActionButton) findViewById(R.id.fabUpdatePodataka);
        //fabApp1 = (FloatingActionButton) findViewById(R.id.fabApp1);
        //fabApp2 = (FloatingActionButton) findViewById(R.id.fabApp2);
        //fabApp3 = (FloatingActionButton) findViewById(R.id.fabApp3);

        btnViewLog = (Button) findViewById(R.id.btnViewLog);
        btnApp1 = (Button) findViewById(R.id.btnApp1);
        btnApp2 = (Button) findViewById(R.id.btnApp2);
        btnApp3 = (Button) findViewById(R.id.btnApp3);
        btnDownloadPodataka = (Button) findViewById(R.id.btnDownloadData);
        btnPostavke = (Button) findViewById(R.id.btnPostavke);

        spisakSyncTabela = new ArrayList<UrlTabele>();


        procitajPostavke();
        zadanaVrstaAplikacija = myPostavke.getVrstaAplikacije();
        if (!myPostavke.getPin().equals("")) {
            Intent intent = new Intent(this, PinActivity.class);
            startActivityForResult(intent, 998);
        }

        postaviTabeleZaSync();
        getLOG();

        //kreiraj potrebne tabele ako ne postoje!
        kreirajTabeluDokumenata(this);
        kreirajTabeluStavki(this);
        //kraj


        btnDownloadPodataka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                napraviSinkronizacijuDownload(view);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        });
        btnViewLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                startActivity(intent);
            }
        });
        btnApp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, App1DokumentiActivity.class);
                intent.putExtra("vrstaAplikacije", 1);
                startActivity(intent);
            }
        });
        btnApp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, App2DokumentiActivity.class);
                startActivity(intent);
            }
        });
        btnApp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, App1DokumentiActivity.class);
                intent.putExtra("vrstaAplikacije", 3);
                startActivity(intent);
            }
        });
        btnPostavke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PostavkeActivity.class);
                startActivityForResult(intent, 1);

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //new JSON_task(this).execute("http://vanima.net:8099/api/artikli?d=2");
        //provjeriti da li je upisan DLT_ID ako nije upaliti login screen i preuzeti sa servera podatke
        if (myPostavke.getDlt_id() == 0) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 999);
        } else {
            DJELATNIK = myPostavke.getDlt_id();
        }

        postaviDugmiceOvisnoOdOvlasti();

    }

    private void postaviDugmiceOvisnoOdOvlasti() {
        if (potrebnaSinkronizacija) {
            setEnableAppButton(false);
        } else {

            if (myPostavke.getVrstaAplikacije() != 0) {
                switch (myPostavke.getVrstaAplikacije()) {
                    case 1:
                        btnApp3.setEnabled(false);
                        btnApp2.setEnabled(false);
                        btnApp1.setEnabled(true);
                        //btnApp1.callOnClick();
                        break;
                    case 2:
                        btnApp3.setEnabled(false);
                        btnApp2.setEnabled(true);
                        btnApp1.setEnabled(false);
                        //btnApp2.callOnClick();
                        break;
                    case 3:
                        btnApp1.setEnabled(false);
                        btnApp2.setEnabled(false);
                        btnApp3.setEnabled(true);
                        //btnApp3.callOnClick();
                        break;
                }
            } else {
                setEnableAppButton(true);
            }
        }
    }


    private void setEnableAppButton(boolean enable) {
        btnApp1.setEnabled(enable);
        btnApp2.setEnabled(enable);
        btnApp3.setEnabled(enable);
    }

    /*
        private void postaviVidljivostFabKontrola(boolean potrebnaSyncVisible,boolean vidljiveTxtKontrole){
            lastPotrebnaSyncVisible = potrebnaSyncVisible;
            lastVidljiveTxtKontrole = vidljiveTxtKontrole; //ovo služi samo za pamćenje zadnje postavke
            if (potrebnaSyncVisible){
                Log.d(TAG, "postaviVidljivostFabKontrola: Vidljivost" + !potrebnaSyncVisible);
                fabUpdatePodataka.setVisibility(View.VISIBLE);


                fabApp1.setVisibility(View.INVISIBLE);
                fabApp2.setVisibility(View.INVISIBLE);
                fabApp3.setVisibility(View.INVISIBLE);

                if (vidljiveTxtKontrole){
                    txtLastSyncID.setText("-1");
                    txtLastSyncDate.setText("/ NIKAD");
                    //txtPotrebnaSinkronizacija.setVisibility(View.VISIBLE);
                }else{
                    //txtPotrebnaSinkronizacija.setVisibility(View.INVISIBLE);
                }

            }
            else{
                Log.d(TAG, "postaviVidljivostFabKontrola: Vidljivost" + potrebnaSyncVisible);
                fabUpdatePodataka.setVisibility(View.INVISIBLE);
                //txtPotrebnaSinkronizacija.setVisibility(View.INVISIBLE);
                switch (myPostavke.getVrstaAplikacije()) {
                    case 0:
                        //sve vidljive
                        fabApp1.setVisibility(View.VISIBLE);
                        fabApp2.setVisibility(View.VISIBLE);
                        fabApp3.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        //vidljiva 1
                        fabApp1.setVisibility(View.VISIBLE);
                        fabApp2.setVisibility(View.GONE);
                        fabApp3.setVisibility(View.GONE);
                        break;
                    case 2:
                        //vidljiva 2
                        fabApp1.setVisibility(View.GONE);
                        fabApp2.setVisibility(View.VISIBLE);
                        fabApp3.setVisibility(View.GONE);
                        break;
                    case 3:
                        //vidljiva 3
                        fabApp1.setVisibility(View.GONE);
                        fabApp2.setVisibility(View.GONE);
                        fabApp3.setVisibility(View.VISIBLE);
                        break;
                }



            }

    }
    */
    private boolean potrebnaSinkronizacija = true;
    private static ListaDbLogAdapter ZadnjiLog;

    public static ListaDbLogAdapter getListaZadnjiLog() {
        return ZadnjiLog;
    }

    public static int getZadnjiID_log(String myTabela, Activity a) {

        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        Integer rbr = 0;
        Cursor c;
        c = myDB.rawQuery("SELECT MAX(redniBroj) AS rbr FROM " + myTabela, null);
        int IdMax = c.getColumnIndex("rbr");
        c.moveToFirst();

        for (int j = 0; j < c.getCount(); j++) {

            rbr = c.getInt(IdMax);

            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        return rbr;
    }
    public void getLOG() {


        if (spisakSyncTabela.size() == 0) {
            return;
        } else {
            boolean gotovo = true;
            for (UrlTabele myTb : spisakSyncTabela) {
                // do something with object
                gotovo = gotovo && myTb.ZavrsenaSyncronizacija;
                Log.d(TAG, "getLOG: GOTOVO =" + gotovo + " do TABELE " + myTb.NazivTabele);
            }
            if (!gotovo) {
                return;
            }
        }

        String myTabela="log";

        ListaDbLogAdapter listaLog = new ListaDbLogAdapter(this, R.layout.row_log);
        ZadnjiLog = new ListaDbLogAdapter(this, R.layout.row_log);
        //listSyncLog.setAdapter(listaLog);

        List<dbLog> myListaLog=new ArrayList<dbLog>();

        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        boolean tabelaLogPostoji=true;

        int brojZadnjihSyncTabela=0;
        int rbr = 0;

        if (isTableExists(myDB, myTabela)){
            if (isFieldExist(myDB,myTabela,"redniBroj")){

                //postaviVidljivostFabKontrola(false,true);

                rbr = getZadnjiID_log(myTabela, this);
                int brojac = 0;

                /*
                Cursor c;
                c = myDB.rawQuery("SELECT MAX(redniBroj) AS rbr FROM " + myTabela, null);
                int IdMax = c.getColumnIndex("rbr");
                c.moveToFirst();
                int brojac = 0;
                for (int j = 0; j < c.getCount(); j++) {

                    rbr = c.getInt(IdMax);
                    brojac++;
                    if (j != c.getCount()) {
                        c.moveToNext();
                    }
                }
                c.close();
                */
                //txtLastSyncID.setText(Integer.toString(rbr));
                //zadnjaSinkronizacijaID=rbr;
                Log.d(TAG, "getLOG: ZADNJI RBR =" + Integer.toString(rbr));
                Cursor z;
                z = myDB.rawQuery("SELECT * FROM " + myTabela + " WHERE redniBroj =" + rbr + ";", null);
                brojac = 0;

                /*
                    myDB.execSQL("CREATE TABLE IF NOT EXISTS log (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "vrijeme datetime default current_timestamp, " +
                        "greska INTEGER, " +
                        "poruka VARCHAR," +
                        "redniBroj INTEGER," +
                        "smjer INTEGER," +
                        "tabela VARCHAR); " );
                     */

                int idIndex=z.getColumnIndex("_id");
                int vrijemeIndex=z.getColumnIndex("vrijeme");
                int greskaIndex=z.getColumnIndex("greska");
                int NazivIndex = z.getColumnIndex("poruka");
                int TabelaIndex = z.getColumnIndex("tabela");
                brojZadnjihSyncTabela=z.getCount();
                Log.d(TAG, "getLOG: BROJ ZAPISA U LOG TABELI JE" + z.getCount());
                if (rbr == 0) {
                    tabelaLogPostoji = false; //ovo je potrebno radi prvog pokretanja!
                } else {
                    z.moveToFirst();

                    for (int j = 0; j < z.getCount(); j++) {
                        Log.d(TAG, "getLOG: POČINJEM FOR PETLJU. brojač= " + brojac);
                        Integer id;
                        String tabela;
                        String naziv;
                        String timestamp;
                        Integer greska;

                        id = z.getInt(idIndex);
                        tabela = z.getString(TabelaIndex);
                        naziv = z.getString(NazivIndex);
                        timestamp = z.getString(vrijemeIndex);
                        Date dd;
                        dd=getDateFromSQLLiteDBFormat(timestamp);

                        //String myDatum=mojDateFormat.format(dd);
                        String myDatum= parseDateFromSQLLiteDBFormatToMyFormat_DateTime(dd);
                        setZadnjaSinkronizacijaVrijeme(dd);
                        greska = z.getInt(greskaIndex);
                        dbLog myLog = new dbLog(id, myDatum, greska, naziv, rbr, tabela);
                        myListaLog.add(myLog);

                        brojac++;
                        if (j != z.getCount()) {
                            z.moveToNext();
                        }
                    }
                    if (getZadnjaSinkronizacijaVrijeme() != null) {
                        //txtLastSyncDate.setText(parseDateFromSQLLiteDBFormatToMyFormat_DateTime(getZadnjaSinkronizacijaVrijeme()));
                    }
                    z.close();
                }
            }
            else{
                tabelaLogPostoji=false;
            }
        }
        else{
            tabelaLogPostoji=false;
        }


        for (dbLog db:myListaLog) {
            listaLog.add(db);
            ZadnjiLog.add(db);
        }

        if (!tabelaLogPostoji){
            rekreirajLogTabelu(myDB);
            rbr=0;
            //postaviVidljivostFabKontrola(true,true);
            potrebnaSinkronizacija = true;
        }
        Log.d(TAG, "getLOG: BROJ ZAPISA U LOG TABELI JE " + listaLog.getCount());
        Log.d(TAG, "getLOG: BROJ POTREBNIH SYNC TABELA JE " + spisakSyncTabela.size());

        if(brojZadnjihSyncTabela!=spisakSyncTabela.size()){
            //zadnja sinkronizacija je bila nepotpuna

            sastaviTabelePotrebnieZaSinkronizaciju(myListaLog);
            Log.d(TAG, "getLOG: Zadnja sinkronizacija nepotpuna potrebno je uraditi opet!");
            //postaviVidljivostFabKontrola(true,false);
            potrebnaSinkronizacija = true;
        }
        else {
            Log.d(TAG, "getLOG: Zadnja sinkronizacija uspješna!");
            potrebnaSinkronizacija = false;
        }
        postaviDugmiceOvisnoOdOvlasti();
        myDB.close();
    }

    private void sastaviTabelePotrebnieZaSinkronizaciju(List<dbLog> novaListaLog){
        for (UrlTabele myTb : spisakSyncTabela) {
            boolean postoji=false;
            for (int i=0;i<novaListaLog.size();i++){
                dbLog db=novaListaLog.get(i);
                if(myTb.NazivTabele.equals(db.getTabela())){
                    postoji=true;
                }
                /*
                if (myTb.NazivTabele.equals("artikli") || myTb.NazivTabele.equals("komitenti")){
                    postoji=true;
                }
                */
            }
            if (!postoji){
                String poruka=getResources().getString(R.string.TabelaNijeUpToDate);

                dbLog novaTbl=new dbLog(0,"?",1,poruka,novaListaLog.size(),myTb.NazivTabele);
                novaListaLog.add(0,novaTbl);
            }
        }

        ListaDbLogAdapter listaLog = new ListaDbLogAdapter(this, R.layout.row_log);
        for (dbLog db:novaListaLog) {
            listaLog.add(db);
        }
        //listSyncLog.setAdapter(listaLog);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (MainActivity.zadanaVrstaAplikacija == 0) {
                super.onBackPressed();
            } else {
                switch (zadanaVrstaAplikacija) {
                    case 1:
                        btnApp1.callOnClick();
                        break;
                    case 2:
                        btnApp2.callOnClick();
                        break;
                    case 3:
                        btnApp3.callOnClick();
                        break;
                }
            }


            //super.onBackPressed();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: ZATVARAM POSTAVKE---OK");
                procitajPostavke();
            } else {
                Log.d(TAG, "onActivityResult: ZATVARAM POSTAVKE---CANCEL");
                procitajPostavke();
            }
            postaviDugmiceOvisnoOdOvlasti();
        }
        if (requestCode == 999) {
            //LOGIN
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: ZATVARAM LOGIN---OK");
                int result = data.getIntExtra("dlt_id", 0);
                this.DJELATNIK = result;

            } else {
                Log.d(TAG, "onActivityResult: ZATVARAM LOGIN---CANCEL");
                this.DJELATNIK = 0;

            }
        }
        if (requestCode == 998) {
            //LOGIN
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: ZATVARAM PIN ACTIVITY---OK");

                if (myPostavke.getVrstaAplikacije() != 0 && !potrebnaSinkronizacija) {
                    switch (myPostavke.getVrstaAplikacije()) {
                        case 1:
                            btnApp1.callOnClick();
                            break;
                        case 2:
                            btnApp2.callOnClick();
                            break;
                        case 3:
                            btnApp3.callOnClick();
                            break;
                    }
                }

            } else {
                Log.d(TAG, "onActivityResult: ZATVARAM PIN ACTIVITY---CANCEL");
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());

            }
        }
    }

    private static postavkeAplikacije myStaticPostavke;

    private void procitajPostavke() {
        myPostavke = new postavkeAplikacije(MainActivity.this);
        myStaticPostavke=myPostavke;
        //postaviVidljivostFabKontrola(lastPotrebnaSyncVisible, lastVidljiveTxtKontrole);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_artikli) {
            Intent intent = new Intent(this, ArtikliActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_komitenti) {
            Intent intent = new Intent(this, KomitentiActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_jmj) {
            //Intent intent = new Intent(this, JmjActivity.class);
            Intent intent = new Intent(this, RegistriActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            int vrstaAplikacije = myPostavke.getVrstaAplikacije();
            if (vrstaAplikacije != 0) {
                sendAllDokuments(this, vrstaAplikacije);
            } else {
                Toast.makeText(MainActivity.this, "Nije moguće poslati podatke jer nije izabran tip aplikacije koji se šalje!", Toast.LENGTH_LONG).show();
            }

        } else if (id== R.id.nav_recive){
            btnDownloadPodataka.callOnClick();
        } else if (id == R.id.nav_odjava) {
            myPostavke.snimiDLT_ID(0);
            brisiSvePodatke();
            recreate();
        } else if (id == R.id.nav_setings) {
            Intent intent = new Intent(this, PostavkeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_security) {
            Intent intent = new Intent(this, PinActivity.class);
            startActivityForResult(intent, 998);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void recreate() {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            super.recreate();
        } else {
            startActivity(getIntent());
            finish();
        }
    }


    public void brisiSvePodatke() {
        //
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Brišem podatke sa uređaja. Molim pričekajte!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        deleteAllTable(this, "jmj");
        deleteAllTable(this, "tip_dokumenta");
        deleteAllTable(this, "podtip_dokumenta");
        deleteAllTable(this, "nacin_placanja");
        deleteAllTable(this, "grupa_artikala");
        deleteAllTable(this, "podgrupa_artikala");
        deleteAllTable(this, "artikli");
        deleteAllTable(this, "artiklbarcode");
        deleteAllTable(this, "artikljmj");
        deleteAllTable(this, "artiklatribut");
        deleteAllTable(this, "komitenti");
        deleteAllTable(this, "PjKomitenta");
        deleteAllTable(this, "dokumenti1");
        deleteAllTable(this, "stavke1");
        if (isTableExists(myDB, "dokumenti2")) {
            deleteAllTable(this, "dokumenti2");
        }
        if (isTableExists(myDB, "stavke2")) {
            deleteAllTable(this, "stavke2");
        }
        if (isTableExists(myDB, "asortimanKupca")) {
            deleteAllTable(this, "asortimanKupca");
        }

        rekreirajLogTabelu(myDB);
        progressDialog.dismiss();

    }

    public static class UrlTabele {
        public String NazivTabele;
        public String urlTabele;
        public boolean ZavrsenaSyncronizacija;
        public String Akcija;

        public UrlTabele(String akcija, String urlTabele, boolean zavrsenaSyncronizacija, String nazivTabele) {
            Akcija = akcija;
            this.urlTabele = urlTabele;
            ZavrsenaSyncronizacija = zavrsenaSyncronizacija;
            NazivTabele = nazivTabele;
        }
    }


    private void postaviTabeleZaSync() {
        String akcija = "";
        String urlString = "";
        akcija = "jmj";
        urlString = url + "idnaziv" + "?d=" + DJELATNIK + "&t=" + akcija;
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "jmj"));

        akcija = "tipdokumenta";
        urlString = url + "idnaziv" + "?d=" + DJELATNIK + "&t=" + akcija;
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "tip_dokumenta"));

        akcija = "podtipdokumenta";
        urlString = url + "idnazivrid" + "?d=" + DJELATNIK + "&t=" + akcija;
        Log.d(TAG, "onNavigationItemSelected: " + urlString);
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "podtip_dokumenta"));

        akcija = "nacinplacanja";
        urlString = url + "idnaziv" + "?d=" + DJELATNIK + "&t=" + akcija;
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "nacin_placanja"));

        akcija = "grupaartikala";
        urlString = url + "idnazivrid" + "?d=" + DJELATNIK + "&t=" + akcija;
        Log.d(TAG, "onNavigationItemSelected: " + urlString);
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "grupa_artikala"));

        akcija = "podgrupaartikala";
        urlString = url + "idnazivrid" + "?d=" + DJELATNIK + "&t=" + akcija;
        Log.d(TAG, "onNavigationItemSelected: " + urlString);
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "podgrupa_artikala"));

        akcija = "artikli";
        urlString = url + akcija + "?d=" + DJELATNIK;
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "artikli"));

        akcija = "artiklbarcode";
        urlString = url + akcija + "?d=" + DJELATNIK;
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "artiklbarcode"));

        akcija = "artikljmj";
        urlString = url + akcija + "?d=" + DJELATNIK;
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "artikljmj"));

        akcija = "artiklatribut";
        urlString = url + akcija + "?d=" + DJELATNIK;
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "artiklatribut"));

        akcija = "komitenti";
        urlString = url + akcija + "?d=" + DJELATNIK + "&sk=" + myPostavke.getSaldakonti();
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "komitenti"));

        akcija = "komitentpj";
        urlString = url + "idnazivrid" + "?d=" + DJELATNIK + "&t=" + akcija;
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "PjKomitenta"));

        //if (myPostavke.getVrstaAplikacije() == 0) {
            long podtipDokumenta = 0;
            if (myPostavke.getPodtipDokumenta() == 0) {
                podtipDokumenta = 51;
            } else {
                podtipDokumenta = myPostavke.getPodtipDokumenta();
            }
            Log.d(TAG, "postaviTabeleZaSync: POSTAVLJEN PODTIP ZA APP 2/ podtipID = " + podtipDokumenta);
            akcija = "dokumentizaglavlja";
            urlString = url + akcija + "?d=" + DJELATNIK + "&u=" + UREDJAJ + "&p=" + String.valueOf(podtipDokumenta);
            spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "dokumenti2"));

            akcija = "dokumentistavke";
            urlString = url + akcija + "?d=" + DJELATNIK + "&u=" + UREDJAJ + "&p=" + String.valueOf(podtipDokumenta);
            spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "stavke2"));

            akcija = "asortimankupca";
            urlString = url + akcija + "?d=" + DJELATNIK;
            spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "asortimankupca"));

        //}
/*        if (myPostavke.getVrstaAplikacije() == 2) {

            long podtipDokumenta = 0;
            if (myPostavke.getPodtipDokumenta() == 0) {
                podtipDokumenta = 51;
            } else {
                podtipDokumenta = myPostavke.getPodtipDokumenta();
            }
            Log.d(TAG, "postaviTabeleZaSync: POSTAVLJEN PODTIP ZA APP 2/ podtipID = " + podtipDokumenta);
            akcija = "dokumentizaglavlja";
            urlString = url + akcija + "?d=" + DJELATNIK + "&u=" + UREDJAJ + "&p=" + String.valueOf(podtipDokumenta);
            spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "dokumenti2"));

            akcija = "dokumentistavke";
            urlString = url + akcija + "?d=" + DJELATNIK + "&u=" + UREDJAJ + "&p=" + String.valueOf(podtipDokumenta);
            spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "stavke2"));
        }

        if (myPostavke.getVrstaAplikacije() == 3) {
            akcija = "asortimankupca";
            urlString = url + akcija + "?d=" + DJELATNIK;
            spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "asortimanKupca"));
        }*/

    }

    private void napraviSinkronizacijuDownload(View view) {


        Snackbar.make(view, "Radim download podataka molim pričekajte", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        int rbrLOG = getZadnjiID_log("log", this);
        rbrLOG += 1;

        Log.d(TAG, "napraviSinkronizacijuDownload: BROJ TABELA ZA SYNC=" + spisakSyncTabela.size());
        int brojac = 0;
        int ukupnoZaSync = spisakSyncTabela.size();
        for (UrlTabele tbl : spisakSyncTabela) {
            Log.d(TAG, "napraviSinkronizacijuDownload: Radim tabelu =" + tbl.NazivTabele);
            tbl.ZavrsenaSyncronizacija = false;

            brojac += 1;


                new JSON_recive(this, tbl, "", rbrLOG) {
                    @Override
                    public void onResponseReceived(int result) {

                    }
                }.execute(tbl.urlTabele, tbl.Akcija);

        }


    }


    public static boolean isTableExists(SQLiteDatabase mDatabase, String tableName) {
        Cursor c = null;
        boolean tableExists = false;
        try
        {
            c = mDatabase.query(tableName, null,null, null, null, null, null);
            tableExists = true;
        }
        catch (Exception e) {
            tableExists = false;
            Log.d(TAG, "isTableExists: Tabela " + tableName + " ne postoji! :(((");

        }
        return tableExists;
    }


    public boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName)
    {
        boolean isExist = false;
        Cursor res = null;
        try {
            res = db.rawQuery("Select * from "+ tableName +" limit 1", null);
            int colIndex = res.getColumnIndex(fieldName);
            if (colIndex!=-1){
                isExist = true;
            }

        } catch (Exception e) {
        } finally {
            try { if (res !=null){ res.close(); } } catch (Exception e1) {}
        }
        if (!isExist){
            Log.d(TAG, "isFieldExist: NE POSTOJI KOLONA " + fieldName + " U TABELI " + tableName);
        }
        return isExist;
    }

    private void rekreirajLogTabelu(SQLiteDatabase myDB){
        myDB.execSQL("DROP TABLE IF EXISTS  log;");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS log (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "vrijeme datetime default current_timestamp, " +
                "greska INTEGER, " +
                "poruka VARCHAR," +
                "redniBroj INTEGER," +
                "smjer INTEGER," +
                "tabela VARCHAR); " );
        myDB.close();
        Log.d(TAG, "rekreirajLogTabelu: KREIRANA LOG TABELA!");
    }



    public static void dropTable(Activity a, String myTable){
        SQLiteDatabase myDB = null;
        myDB = a.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        myDB.execSQL("DROP TABLE " + myTable + ";");
        myDB.close();
    }
    public static void deleteAllTable(Activity a, String myTable){
        SQLiteDatabase myDB = null;
        myDB = a.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        myDB.execSQL("DELETE FROM " + myTable + ";");
        myDB.close();
    }
    public static void izbrisiRedakIzTabele(Activity a, String myTable, String myIDpolje, Long myIDvrijjednost){
        SQLiteDatabase myDB = null;
        myDB = a.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        myDB.execSQL("DELETE FROM " + myTable + " WHERE " + myIDpolje + " = " + myIDvrijjednost +";");
        myDB.close();
    }

    public static void setZakljuciDokument_Dokumenti1(Activity a, Long id, boolean zakljuci){
        SQLiteDatabase myDB = null;
        int myZakljuci=0;
        if (zakljuci){
            myZakljuci=1;
        }
        myDB = a.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        myDB.execSQL("UPDATE dokumenti1 SET zakljucen = " + myZakljuci + " WHERE  _id=" + id + ";" );
        myDB.close();
    }

    public static void updateOstalihRBR(Activity a, Long myID, int izbrisaniRBR) {
        SQLiteDatabase myDB = null;
        myDB = a.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        myDB.execSQL("UPDATE stavke2 SET rbr = rbr-1 WHERE zaglavljeId = " + myID + " AND rbr > " + izbrisaniRBR + " ;");
        myDB.close();
    }

    //datumi

    public static String danMjesecGodinaToFormatString(int dan,int mjesec,int godina){
        SimpleDateFormat dateFormat=new SimpleDateFormat(MainActivity.DatumFormat);
        //SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");
        Calendar c=Calendar.getInstance();
        c.set(godina,mjesec,dan,0,0);
        Date datum=c.getTime();

        String datumStr=dateFormat.format(datum);
        return datumStr;
    }
    public static String danMjesecGodinaToSQLLiteFormatString(int dan,int mjesec,int godina,int sat,int min,int sec){
        SimpleDateFormat dateFormat=new SimpleDateFormat(MainActivity.SqlLiteDateFormat);
        //SQLLIte format datuma je:     yyyy-MM-dd HH:mm:ss
        Calendar c=Calendar.getInstance();
        c.set(godina,mjesec,dan,sat,min,sec);
        Date datum=c.getTime();

        String datumStr=dateFormat.format(datum);
        return datumStr;
    }

    public static Date SQLLiteStringToDate(String datum){

        SimpleDateFormat format = new SimpleDateFormat(MainActivity.SqlLiteDateFormat);
        try {
            Date date = format.parse(datum);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Date getDateFromSQLLiteDBFormat(String dateFromSqlLiteAsString){
        Date date=new Date();
        SimpleDateFormat simpleSqlDateFormat = new SimpleDateFormat(SqlLiteDateFormat);
        try {
            date=(Date)simpleSqlDateFormat.parse(dateFromSqlLiteAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static Date getDateFromMojString(String dateFromString){
        Date date=new Date();
        SimpleDateFormat simpleSqlDateFormat = new SimpleDateFormat(DatumFormat);
        try {
            date=(Date)simpleSqlDateFormat.parse(dateFromString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String parseDateFromSQLLiteDBFormatToMyFormat_DateTime(Date date){

        SimpleDateFormat mojDateFormat=new SimpleDateFormat(DatumVrijemeFormat);
        String myDateString=mojDateFormat.format(date);
        return myDateString;

    }

    public static String parseDateFromSQLLiteDBFormatToMyOnlyDateFormat(Date date){

        SimpleDateFormat mojDateFormat=new SimpleDateFormat(DatumFormat);
        String myDateString=mojDateFormat.format(date);
        return myDateString;

    }

    public static String parseDateFromSQLLiteDBFormatToJSONFormat(Date date) {

        SimpleDateFormat mojDateFormat = new SimpleDateFormat(BorisovFormatDatuma);
        String myDateString = mojDateFormat.format(date);
        return myDateString;

    }

    public static Date getDateFromJSONFormat(String dateFromWEB_String) {
        SimpleDateFormat dateformat2 = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date newdate = new Date();

        String strdate2 = "01-01-1990 00:00:00";
        try {
            newdate = dateformat2.parse(strdate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date(); // ovo je da defoltni datum bude 1.1.1990 ako Boris ne vrati dobro!

        date = newdate;
        SimpleDateFormat simpleSqlDateFormat = new SimpleDateFormat(BorisovFormatDatuma);
        try {
            date = (Date) simpleSqlDateFormat.parse(dateFromWEB_String);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //kraj datuma

    public static Artikl getArtiklByBarcode(Activity a, String barcode, boolean asortimanKupca, long pjKmtId) {
        Artikl artikl = null;
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;

        c = myDB.rawQuery("SELECT * FROM artiklbarcode WHERE barcode = '" + barcode + "';", null);
        Log.d(TAG, "getArtiklByBarcode: BARCODE" + barcode);
        int ArtikIdIndex = c.getColumnIndex("artiklId");
        c.moveToFirst();
        long artId = 0;
        for (int j = 0; j < c.getCount(); j++) {
            artId = c.getLong(ArtikIdIndex);
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();

        if (artId > 0) {
            Log.d(TAG, "getArtiklByBarcode: DOHVAĆAM ARTIKL PO ID=" + artId);
            artikl = getArtiklById(a, artId, pjKmtId, asortimanKupca);
        }
        return artikl;
    }

    public static int getStavke2NoviRbr(Activity a, long dokumentID) {
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT count(*) FROM stavke2 WHERE zaglavljeId =" + dokumentID + ";", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        myDB.close();
        Log.d(TAG, "getStavke2NoviRbr: STAVKE2. Novi RBR=" + String.valueOf(count + 1));
        return count + 1;
    }

    public static boolean getZavrsenoDokumenti2ById(Activity a, int id) {
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT zavrsen FROM dokumenti2 WHERE _id =" + id + ";", null);
        c.moveToFirst();
        boolean value = (c.getInt(0) == 1);
        return value;
    }

    public static int getBrojArtikalaInAsortimanKupca(Activity a, long kmtID) {
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT count(*) FROM asortimankupca WHERE pjKmtId =" + kmtID + ";", null);
        c.moveToFirst();
        int value = c.getInt(0);
        return value;
    }

    public static void updateZavrsenoInDokumenti2(Activity a, long dokumentID) {
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT count(*) FROM stavke2 WHERE zaglavljeId =" + dokumentID + " AND vipsID != 0 AND kolicina != kolicinaZadana;", null);
        c.moveToFirst();
        int count = c.getInt(0);
        Log.d(TAG, "updateZavrsenoInDokumenti2: STAVKE2. Broj stavki koje se razlikuju od zadanih količina je = " + count);
        int zavrsen = 0;
        if (count == 0) {
            zavrsen = 1;
        }
        c.close();
        myDB.execSQL("UPDATE dokumenti2 SET zavrsen = " + zavrsen + " WHERE vipsId =" + dokumentID + ";");
        myDB.close();
    }

    public static Artikl getArtiklById(Activity a, long artId, long pjKmtId, boolean asortimanKUpca) {
        Artikl artikl = null;

        SQLiteDatabase mDatabase = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        String sql;
        if (asortimanKUpca) {
            sql = "SELECT  * FROM artikli WHERE _id IN (SELECT artiklId FROM asortimankupca WHERE pjKmtId=" + pjKmtId + ") AND _id=" + artId + ";";
        } else {
            sql = "SELECT  * FROM artikli WHERE _id =" + artId + ";";
        }
        c = myDB.rawQuery(sql, null);

        int IdIndex = c.getColumnIndex("_id");
        int SifraIndex = c.getColumnIndex("sifra");
        int NazivIndex = c.getColumnIndex("naziv");
        int ProizvodacIndex = c.getColumnIndex("proizvodjac");
        int KataloskiBrojIndex = c.getColumnIndex("kataloskiBroj");
        int KratkiOpisIndex = c.getColumnIndex("kratkiOpis");
        int JmjIndex = c.getColumnIndex("jmjId");
        int JmjNazivIndex = c.getColumnIndex("jmjNaziv");
        int DugiOpisIndex = c.getColumnIndex("dugiOpis");
        int VrstaAmbalazeIndex = c.getColumnIndex("vrstaAmbalaze");
        int BrojKoletaIndex = c.getColumnIndex("brojKoleta");
        int BrojKoletaNaPaletiIndex = c.getColumnIndex("brojKoletaNaPaleti");
        int StanjeIndex = c.getColumnIndex("stanje");
        int VpcIndex = c.getColumnIndex("vpc");
        int MpcIndex = c.getColumnIndex("mpc");
        int NettoIndex = c.getColumnIndex("netto");
        int BruttoIndex = c.getColumnIndex("brutto");
        int ImaRokTrajanjaIndex = c.getColumnIndex("imaRokTrajanja");
        int PodgrupaIdIndex = c.getColumnIndex("podgrupaID");

        c.moveToFirst();

        for (int j = 0; j < c.getCount(); j++) {
            long id, jmjId;
            String sifra, naziv, jmjNaziv, kataloskiBroj, kratkiOpis, proizvodjac, dugiOpis, vrstaAmbalaze;
            double brojKoleta, brojKoletaNaPaleti, stanje, vpc, mpc, netto, brutto;
            boolean imaRokTrajanja;
            int podgrupaID;

            id = c.getLong(IdIndex);
            sifra = c.getString(SifraIndex);
            naziv = c.getString(NazivIndex);
            proizvodjac = c.getString(ProizvodacIndex);
            kataloskiBroj = c.getString(KataloskiBrojIndex);
            kratkiOpis = c.getString(KratkiOpisIndex);
            jmjId = c.getLong(JmjIndex);
            jmjNaziv = c.getString(JmjNazivIndex);
            dugiOpis = c.getString(DugiOpisIndex);
            vrstaAmbalaze = c.getString(VrstaAmbalazeIndex);
            brojKoleta = c.getDouble(BrojKoletaIndex);
            brojKoletaNaPaleti = c.getDouble(BrojKoletaNaPaletiIndex);
            stanje = c.getDouble(StanjeIndex);
            vpc = c.getDouble(VpcIndex);
            mpc = c.getDouble(MpcIndex);
            netto = c.getDouble(NettoIndex);
            brutto = c.getDouble(BruttoIndex);
            boolean vrijednostImaAtribut = (c.getInt(ImaRokTrajanjaIndex) == 1);
            imaRokTrajanja = vrijednostImaAtribut;
            podgrupaID = c.getInt(PodgrupaIdIndex);

            artikl = new Artikl(id, sifra, naziv, kataloskiBroj, jmjId, jmjNaziv, kratkiOpis, proizvodjac, dugiOpis, vrstaAmbalaze, brojKoleta, brojKoletaNaPaleti, stanje, vpc, mpc, netto, brutto, imaRokTrajanja, podgrupaID);

            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        mDatabase.close();

        return artikl;
    }
    public static String getArtiklNaziv_byID(Activity a, long id){
        Log.d(TAG, "POČETAK ČITANJA PODATAKA IZ TABELE ARTIKLI: ");
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        Log.d(TAG, "OTVOREN CURSOR ARTIKLI: ");
        c = myDB.rawQuery("SELECT * FROM artikli WHERE _id = "+ id + ";", null);
        int ArtiklNazivIndex = c.getColumnIndex("naziv");
        c.moveToFirst();
        String naziv="";
        for (int j = 0; j < c.getCount(); j++) {
            naziv=c.getString(ArtiklNazivIndex);
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        myDB.close();
        return naziv;
    }

    public static String getJmjNaziv_byID(Activity a, long id){
        Log.d(TAG, "POČETAK ČITANJA PODATAKA IZ TABELE JMJ: ");
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        Log.d(TAG, "OTVOREN CURSOR ARTIKLI: ");
        c = myDB.rawQuery("SELECT * FROM jmj WHERE _id = "+ id + ";", null);
        int ArtiklNazivIndex = c.getColumnIndex("naziv");
        c.moveToFirst();
        String naziv="";
        for (int j = 0; j < c.getCount(); j++) {
            naziv=c.getString(ArtiklNazivIndex);
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        myDB.close();
        return naziv;
    }

    public static String formatDecimalbyPostavke(){
        return "%." + myStaticPostavke.getBrojDecimala() + "f";
    }

    public static List<ArtiklAtributStanje> getListaAtributa(Activity a, long artiklID, String filter) {
        String myTabela = "artiklatribut";
        List<ArtiklAtributStanje> listaAtributa = new ArrayList<ArtiklAtributStanje>();
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        Cursor c;
        if (artiklID == -1) {
            if (filter.equals("")) {
                c = myDB.rawQuery("SELECT * FROM " + myTabela + " ORDER BY artiklID ASC;", null);
            } else {
                c = myDB.rawQuery("SELECT * FROM " + myTabela + " WHERE artiklId LIKE '%" + filter + "%' ORDER BY artiklID ASC;", null);
            }
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela + " where artiklID = " + artiklID + ";", null);
        }

        int ArtiklIdIndex = c.getColumnIndex("artiklId");
        int vrijednostId1Index = c.getColumnIndex("vrijednostId1");
        int vrijednost1Index = c.getColumnIndex("vrijednost1");
        int atribut1Index = c.getColumnIndex("atribut1");
        int stanjeIndex = c.getColumnIndex("stanje");
        c.moveToFirst();
        for (int j = 0; j < c.getCount(); j++) {
            long artId;
            long vrj1Id;
            String vrijednost;
            String atribut1;
            double stanje;
            artId = c.getLong(ArtiklIdIndex);
            vrj1Id = c.getLong(vrijednostId1Index);
            vrijednost=c.getString(vrijednost1Index);
            atribut1=c.getString(atribut1Index);
            stanje=c.getDouble(stanjeIndex);
            ArtiklAtributStanje object = new ArtiklAtributStanje(artId, vrj1Id,vrijednost,atribut1,stanje);
            listaAtributa.add(object);
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        myDB.close();
        return  listaAtributa;
    }

    public static List<ArtiklJmj> getListaArtiklJMJ(Activity a, long artiklID, String filter) {
        List<ArtiklJmj> lista = new ArrayList<ArtiklJmj>();
        /*
        prvo create view da bi uhvatio naziv artikla i naziv jmj u ovu tabelu
        CREATE VIEW IF NOT EXISTS vwArtiklJmj
        AS
       SELECT
             tbArtJmjOdn.ArtiklID
             ,tbArt.Naziv AS ArtiklNaziv
             ,tbArtJmjOdn.JmjID
             ,tbJmj.Naziv Jmj
       FROM
             tbArtJmjOdn INNER JOIN
             tbArt ON tbArtJmjOdn.ArtiklID = tbArt.ID INNER JOIN
             tbJmj ON tbArtJmjOdn.JmjID = tbJmj.ID;

         */
        //String myTabela = "artikljmj";
        String myView = "vwArtikliJmj";
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        Log.d(TAG, "getListaArtiklJMJ: create view START");
        myDB.execSQL("DROP VIEW IF EXISTS " + myView + ";");
        myDB.execSQL("CREATE VIEW IF NOT EXISTS vwArtikliJmj " +
                "AS SELECT " +
                "artikljmj.artiklId, " +
                "artikli.naziv AS nazivArtikla, " +
                "artikljmj.jmjId, " +
                "jmj.naziv AS nazivJmj" +
                " FROM " +
                "artikljmj INNER JOIN artikli ON artikljmj.artiklId = artikli._id INNER JOIN jmj on artikljmj.jmjId = jmj._id;");

        Log.d(TAG, "getListaArtiklJMJ: create view END");
        Cursor c;
        if (artiklID == -1) {
            if (filter.equals("")) {
                c = myDB.rawQuery("SELECT * FROM " + myView + " ORDER BY artiklId ASC;", null);
            } else {
                c = myDB.rawQuery("SELECT * FROM " + myView + " where artiklId like '%" + filter + "%'  ORDER BY artiklId ASC;", null);
            }
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myView + " WHERE artiklId=" + artiklID + ";", null);
        }
        Log.d(TAG, "getListaArtiklJMJ: broj zapisa je ->" + c.getCount());

        int ArtiklIdIndex = c.getColumnIndex("artiklId");
        int jmjIdIndex = c.getColumnIndex("jmjId");
        int nazivArtiklaIndex = c.getColumnIndex("nazivArtikla");
        int nazivJmjIndex = c.getColumnIndex("nazivJmj");
        Log.d(TAG, "getListaArtiklJMJ: " + ArtiklIdIndex + "/" + jmjIdIndex + "/" + nazivArtiklaIndex + "/" + nazivJmjIndex);
        c.moveToFirst();
        Log.d(TAG, "ListaArtiklJmjAdapter: Broj podataka u bazi je:" + Integer.toString(c.getCount() + 1));
        for (int j = 0; j < c.getCount(); j++) {
            long artId;
            long jmjId;
            String nazivartikla;
            String nazivjmj;

            artId = c.getLong(ArtiklIdIndex);
            jmjId = c.getLong(jmjIdIndex);
            nazivartikla = c.getString(nazivArtiklaIndex);
            nazivjmj = c.getString(nazivJmjIndex);

            ArtiklJmj ArtJmjProvider = new ArtiklJmj(artId, jmjId, nazivartikla, nazivjmj);
            lista.add(ArtJmjProvider);
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        myDB.close();
        return lista;
    }


    public static List<jmj> getListaJMJ(Activity a, long id, String filter) {
        List<jmj> lista = new ArrayList<jmj>();
        String myTabela = "jmj";
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        Cursor c;
        if (id == -1) {
            if (filter.equals("")) {
                c = myDB.rawQuery("SELECT * FROM " + myTabela + ";", null);
            } else {
                c = myDB.rawQuery("SELECT * FROM " + myTabela + " where naziv like '%" + filter + "%'", null);
            }
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela + " WHERE _id=" + id + ";", null);
        }
        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");

        c.moveToFirst();

        for (int j = 0; j < c.getCount(); j++) {
            long idJmj;
            String naziv;
            idJmj = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);

            jmj jmjProvider = new jmj(idJmj, naziv);
            lista.add(jmjProvider);
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        myDB.close();
        return lista;
    }

    public static App1Dokumenti pretvoriApp2Dok_App1Dok(App2Dokumenti dok2) {

        App1Dokumenti rezultat = new App1Dokumenti(dok2.getId(), 0, dok2.getPodtipId(), 0, dok2.getPjKmtId(),
                dok2.getDatumDokumenta(), null, dok2.getOpaska(), dok2.getKmtNaziv(), dok2.getPjKmtNaziv(), "", dok2.getPodtipNaziv(), 0, "", dok2.isZakljucen());
        return rezultat;
    }

    public static App1Stavke pretvoriApp2Stv_App1Stv(Activity a, App2Stavke stv2) {
        boolean imaAtribut = false;
        if (stv2.getVrijednostId1() > 0) {
            imaAtribut = true;
        }
        Artikl art=getArtiklById(a,stv2.getArtiklId(),0,false);

        App1Stavke rezultat = new App1Stavke(stv2.getId(), stv2.getZaglavljeId(), stv2.getArtiklId(), stv2.getArtiklNaziv(), stv2.getJmjId(),
                stv2.getJmjNaziv(), imaAtribut, stv2.getVrijednostId1(), stv2.getAtributNaziv(), stv2.getVrijednostNaziv(), stv2.getKolicina(),art.getVpc(),art.getMpc(), stv2.getOpaska());

        return rezultat;
    }
    public static List<App1Stavke> getListaStavki(long IdDokumenta, Activity a){
        String tabelaApp1 = "stavke1";

        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        if (!isTableExists(myDB, tabelaApp1)) {
            return null;
        }
        List<App1Stavke> listaStavki = new ArrayList<App1Stavke>();

        Log.d(TAG, "ucitajStavke: IdDOKUMENTA=" + IdDokumenta);


        Cursor c;
        Log.d(TAG, "ucitajStavke: " + "SELECT * FROM " + tabelaApp1 + " WHERE idDokumenta=" + IdDokumenta + " ORDER BY datumUpisa DESC");
        c = myDB.rawQuery("SELECT * FROM " + tabelaApp1 + " WHERE idDokumenta=" + IdDokumenta +" ORDER BY datumUpisa DESC", null);

        long idStavke;
        long idArtikla;
        String nazivArtikla;
        double kolicina;
        boolean imaAtribut;
        long idAtributa;
        String vrijednostAtributa;
        String nazivAtributa;
        long idJmj;
        String nazivJmj;
        String napomena;

        int idStavkeIndex = c.getColumnIndex("_id");
        int idArtiklaIndex = c.getColumnIndex("idArtikla");
        int nazivArtiklaIndex = c.getColumnIndex("nazivArtikla");
        int kolicinaIndex = c.getColumnIndex("kolicina");
        int imaAtributIndex = c.getColumnIndex("imaAtribut");
        int idAtributaIndex = c.getColumnIndex("idAtributa");
        int vrijednostAtributaIndex = c.getColumnIndex("vrijednostAtributa");
        int nazivAtributaIndex = c.getColumnIndex("nazivAtributa");
        int idJmjIndex = c.getColumnIndex("idJmj");
        int nazivJmjIndex = c.getColumnIndex("nazivJmj");
        int napomenaIndex = c.getColumnIndex("napomena");

        c.moveToFirst();
        int brojac = 0;
        Log.d(TAG, "ucitajStavke: UCITANO JE STAVKI =" + c.getCount());
        for (int j = 0; j < c.getCount(); j++) {
            idStavke = c.getLong(idStavkeIndex);
            idArtikla = c.getLong(idArtiklaIndex);
            nazivArtikla = c.getString(nazivArtiklaIndex);
            kolicina = c.getDouble(kolicinaIndex);
            imaAtribut = Boolean.parseBoolean(c.getString(imaAtributIndex));
            idAtributa = c.getLong(idAtributaIndex);
            vrijednostAtributa = c.getString(vrijednostAtributaIndex);
            nazivAtributa = c.getString(nazivAtributaIndex);
            idJmj = c.getLong(idJmjIndex);
            nazivJmj = c.getString(nazivJmjIndex);
            napomena = c.getString(napomenaIndex);

            Artikl art=getArtiklById(a,idArtikla,0,false);

            App1Stavke myObj = new App1Stavke(idStavke, IdDokumenta, idArtikla, nazivArtikla, idJmj, nazivJmj, imaAtribut, idAtributa, vrijednostAtributa, nazivAtributa, kolicina,art.getVpc(),art.getMpc(), napomena);
            listaStavki.add(myObj);

            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        Log.d(TAG, "ucitajDokumente: U tabeli se nalazi " + brojac + " dokumenta!");
        c.close();
        myDB.close();
        return listaStavki;
    }

    public static List<App2Stavke> getListaStavki2(long IdDokumenta, Activity a) {
        String tabelaApp1 = "stavke2";

        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        if (!isTableExists(myDB, tabelaApp1)) {
            return null;
        }
        List<App2Stavke> listaStavki = new ArrayList<App2Stavke>();

        Log.d(TAG, "ucitajStavke: IdDOKUMENTA=" + IdDokumenta);


        Cursor c;
        Log.d(TAG, "ucitajStavke: " + "SELECT * FROM " + tabelaApp1 + " WHERE zaglavljeId=" + IdDokumenta + " ORDER BY rbr ASC");
        c = myDB.rawQuery("SELECT * FROM " + tabelaApp1 + " WHERE zaglavljeId=" + IdDokumenta + " ORDER BY rbr ASC", null);

        int idStavke;
        long zaglavljeId;
        int rbr;
        long artiklId;
        String artiklSifra;
        String artiklNaziv;
        long jmjId;
        String jmjNaziv;
        long vrijednostId1;
        String vrijednostNaziv;
        String atributNaziv;
        double kolicina;
        String opaska;
        long vipsId;
        double kolicinaZadana;

        int idStavkeIndex = c.getColumnIndex("_id");
        int zaglavljeIdIndex = c.getColumnIndex("zaglavljeId");
        int rbrIndex = c.getColumnIndex("rbr");
        int artiklIdIndex = c.getColumnIndex("artiklId");
        int artiklSifraIndex = c.getColumnIndex("artiklSifra");
        int artiklNazivIndex = c.getColumnIndex("artiklNaziv");
        int jmjIdIndex = c.getColumnIndex("jmjId");
        int jmjNazivIndex = c.getColumnIndex("jmjNaziv");
        int vrijednostId1Index = c.getColumnIndex("vrijednostId1");
        int vrijednostNazivIndex = c.getColumnIndex("vrijednostNaziv");
        int atributNazivIndex = c.getColumnIndex("atributNaziv");
        int kolicinaIndex = c.getColumnIndex("kolicina");
        int opaskaIndex = c.getColumnIndex("opaska");
        int vipsIdIndex = c.getColumnIndex("vipsId");
        int kolicinaZadanaIndex = c.getColumnIndex("kolicinaZadana");

        c.moveToFirst();
        int brojac = 0;
        Log.d(TAG, "ucitajStavke: UCITANO JE STAVKI =" + c.getCount());
        for (int j = 0; j < c.getCount(); j++) {
            idStavke = c.getInt(idStavkeIndex);
            zaglavljeId = c.getLong(zaglavljeIdIndex);
            rbr = c.getInt(rbrIndex);
            artiklId = c.getLong(artiklIdIndex);
            artiklSifra = c.getString(artiklSifraIndex);
            artiklNaziv = c.getString(artiklNazivIndex);
            jmjId = c.getLong(jmjIdIndex);
            jmjNaziv = c.getString(jmjNazivIndex);
            vrijednostId1 = c.getLong(vrijednostId1Index);
            vrijednostNaziv = c.getString(vrijednostNazivIndex);
            atributNaziv = c.getString(atributNazivIndex);
            kolicina = c.getDouble(kolicinaIndex);
            opaska = c.getString(opaskaIndex);
            vipsId = c.getLong(vipsIdIndex);
            kolicinaZadana = c.getDouble(kolicinaZadanaIndex);


            App2Stavke myObj = new App2Stavke(idStavke, zaglavljeId, artiklId, jmjId, vrijednostId1, vipsId, rbr, kolicina, kolicinaZadana, opaska, artiklSifra, artiklNaziv, jmjNaziv, vrijednostNaziv, atributNaziv);

            Log.d(TAG, "BAZA getListaDokumenta2: STAVKE2 = " + myObj.toString());
            listaStavki.add(myObj);

            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        Log.d(TAG, "ucitajDokumente: U tabeli se nalazi " + brojac + " dokumenta!");
        c.close();
        myDB.close();
        return listaStavki;
    }


    public static  App1Dokumenti getDokumentApp1_ByID(Activity a,Long idDok){
        if (idDok==0){
            return null;
        }

        String tabelaApp1 = "dokumenti1";
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM " + tabelaApp1 + " WHERE _id = " + idDok + ";", null);

        SimpleDateFormat SQLLite_dateFormat = new SimpleDateFormat(MainActivity.SqlLiteDateFormat);

        long id;
        long idTip;
        long idPodtip;
        long idKomitent;
        long idPjKomitenta;
        long idNacinPlacanja;

        Boolean zakljucen;

        String datumDokumentaString;
        String datumSinkronizacijeString;
        String napomena;
        String komitentNaziv;
        String komitentPjNaziv;
        String tipNaziv;
        String podtipNaziv;
        String nacinPlacanjaNaziv;

        Date datumDokumenta = new Date();
        Date datumSinkronizacije = new Date();

        int idIndex = c.getColumnIndex("_id");
        int idTipIndex = c.getColumnIndex("idTip");
        int idPodipIndex = c.getColumnIndex("idPodtip");
        int idKomitentIndex = c.getColumnIndex("idKomitent");
        int idPjKomitentaIndex = c.getColumnIndex("idPjKomitenta");
        int idDatumDokumentaIndex = c.getColumnIndex("datumDokumenta");
        int idDatumSinkronizacijeIndex = c.getColumnIndex("datumSinkronizacije");
        int idNapomenaIndex = c.getColumnIndex("napomena");

        int idKomitentNaziv = c.getColumnIndex("KomitentNaziv");
        int idKomitentPjIndex = c.getColumnIndex("PjKomitentaNaziv");
        int idTipNazivIndex = c.getColumnIndex("TipDokumentaNaziv");
        int idPodtpNazivIndex = c.getColumnIndex("PodipDokumentaNaziv");
        int idNacinPlacanjaIndex = c.getColumnIndex("idVrstaPlacanja");
        int nacinPlacanjaNazivIndex = c.getColumnIndex("VrstaPlacanjaNaziv");
        int zakljucenIndex=c.getColumnIndex("zakljucen");

        c.moveToFirst();

            id = c.getLong(idIndex);
            idTip = c.getLong(idTipIndex);
            tipNaziv = c.getString(idTipNazivIndex);
            idPodtip = c.getLong(idPodipIndex);
            podtipNaziv = c.getString(idPodtpNazivIndex);
            idKomitent = c.getLong(idKomitentIndex);
            idNacinPlacanja = c.getLong(idNacinPlacanjaIndex);
            komitentNaziv = c.getString(idKomitentNaziv);
            idPjKomitenta = c.getLong(idPjKomitentaIndex);
            komitentPjNaziv = c.getString(idKomitentPjIndex);
            datumDokumentaString = c.getString(idDatumDokumentaIndex);
            datumSinkronizacijeString = c.getString(idDatumSinkronizacijeIndex);
            napomena = c.getString(idNapomenaIndex);
            nacinPlacanjaNaziv = c.getString(nacinPlacanjaNazivIndex);
            zakljucen=(c.getInt(zakljucenIndex)== 1);

            try {
                datumDokumenta = (Date) SQLLite_dateFormat.parse(datumDokumentaString);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            try {
                if (datumSinkronizacijeString==null){
                    datumSinkronizacije=null;
                }else{
                    datumSinkronizacije = (Date) SQLLite_dateFormat.parse(datumSinkronizacijeString);
                }

            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }


            Log.d(TAG, "getListaDokumenta: DOKUMENT: _id=" + id +"/ datumSinkronizacije=" + datumSinkronizacijeString);
         App1Dokumenti myObj = new App1Dokumenti(id, idTip, idPodtip, idKomitent, idPjKomitenta, datumDokumenta, datumSinkronizacije,
                 napomena, komitentNaziv, komitentPjNaziv, tipNaziv, podtipNaziv, idNacinPlacanja, nacinPlacanjaNaziv,zakljucen);
         return  myObj;

        }
    public static List<App1Dokumenti> getListaDokumenta(Activity a, int vrstaAplikacije, Long filterKomitentID,String filterDatumOd,String filterDatumDo, Long filterPjKomitentID, int filterZakljucen) {

        List<App1Dokumenti> listaDokumenta=new ArrayList<App1Dokumenti>();
        String tabelaApp1 = "dokumenti1";

        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        Cursor c;
        String whereUvijet="";
        //pretvorba filteraZakljucen u boolean
        int filterZ;
        switch (filterZakljucen){
            case 0:
                //SVI
                whereUvijet+=" AND 1 = 1";
                break;
            case 1:
                //ZAKLJUČENI
                whereUvijet+=" AND zakljucen = 1";
                break;
            case 2:
                //NEZAKLJUČENI
                whereUvijet+=" AND zakljucen = 0";
                break;
        }
        if (filterPjKomitentID==0){

        }else{
            whereUvijet += " AND idPjKomitenta = " + filterPjKomitentID;
        }
        /*if (filterZakljucen==0){
            if (filterPjKomitentID==0){
                whereUvijet+=" AND 1 = 1";
            }else{
                whereUvijet+=" AND idPjKomitenta = " + filterPjKomitentID;
            }
        }else{
            whereUvijet+=" AND zakljucen =" + filterZakljucen;
            if (filterPjKomitentID==0){

            }else{
                whereUvijet += " AND idPjKomitenta = " + filterPjKomitentID;
            }
        }*/
        if ( TextUtils.isEmpty(filterDatumOd) && TextUtils.isEmpty(filterDatumDo)){
            c = myDB.rawQuery("SELECT * FROM " + tabelaApp1 + " WHERE vrstaAplikacije = " + vrstaAplikacije + whereUvijet +  " ORDER BY datumUpisa DESC", null);
        }else{
            if (filterKomitentID==0){
                c = myDB.rawQuery("SELECT * FROM " + tabelaApp1 + " WHERE vrstaAplikacije = " + vrstaAplikacije + whereUvijet + " AND datumDokumenta BETWEEN '" + filterDatumOd + "' AND '" + filterDatumDo + "' ORDER BY datumUpisa DESC", null);
            }else{
                c = myDB.rawQuery("SELECT * FROM " + tabelaApp1 + " WHERE vrstaAplikacije = " + vrstaAplikacije + " AND idKomitent = " + filterKomitentID + whereUvijet + " AND datumDokumenta BETWEEN '" + filterDatumOd + "' AND '" + filterDatumDo + "' ORDER BY datumUpisa DESC", null);
            }
        }



        SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(DatumVrijemeFormat);
        SimpleDateFormat SQLLite_dateFormat = new SimpleDateFormat(MainActivity.SqlLiteDateFormat);

        long id;
        long idTip;
        long idPodtip;
        long idKomitent;
        long idPjKomitenta;
        long idNacinPlacanja;

        boolean zakljucen;

        String datumDokumentaString;
        String datumSinkronizacijeString;
        String napomena;
        String komitentNaziv;
        String komitentPjNaziv;
        String tipNaziv;
        String podtipNaziv;
        String nacinPlacanjaNaziv;

        Date datumDokumenta = new Date();
        Date datumSinkronizacije = new Date();

        int idIndex = c.getColumnIndex("_id");
        int idTipIndex = c.getColumnIndex("idTip");
        int idPodipIndex = c.getColumnIndex("idPodtip");
        int idKomitentIndex = c.getColumnIndex("idKomitent");
        int idPjKomitentaIndex = c.getColumnIndex("idPjKomitenta");
        int idDatumDokumentaIndex = c.getColumnIndex("datumDokumenta");
        int idDatumSinkronizacijeIndex = c.getColumnIndex("datumSinkronizacije");
        int idNapomenaIndex = c.getColumnIndex("napomena");

        int idKomitentNaziv = c.getColumnIndex("KomitentNaziv");
        int idKomitentPjIndex = c.getColumnIndex("PjKomitentaNaziv");
        int idTipNazivIndex = c.getColumnIndex("TipDokumentaNaziv");
        int idPodtpNazivIndex = c.getColumnIndex("PodipDokumentaNaziv");
        int idNacinPlacanjaIndex = c.getColumnIndex("idVrstaPlacanja");
        int nacinPlacanjaNazivIndex = c.getColumnIndex("VrstaPlacanjaNaziv");

        int zakljucenIndex=c.getColumnIndex("zakljucen");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            id = c.getLong(idIndex);
            idTip = c.getLong(idTipIndex);
            tipNaziv = c.getString(idTipNazivIndex);
            idPodtip = c.getLong(idPodipIndex);
            podtipNaziv = c.getString(idPodtpNazivIndex);
            idKomitent = c.getLong(idKomitentIndex);
            idNacinPlacanja = c.getLong(idNacinPlacanjaIndex);
            komitentNaziv = c.getString(idKomitentNaziv);
            idPjKomitenta = c.getLong(idPjKomitentaIndex);
            komitentPjNaziv = c.getString(idKomitentPjIndex);
            datumDokumentaString = c.getString(idDatumDokumentaIndex);
            datumSinkronizacijeString = c.getString(idDatumSinkronizacijeIndex);
            napomena = c.getString(idNapomenaIndex);
            nacinPlacanjaNaziv = c.getString(nacinPlacanjaNazivIndex);
            zakljucen=c.getInt(zakljucenIndex)==1;

            try {
                datumDokumenta = (Date) SQLLite_dateFormat.parse(datumDokumentaString);
            } catch (ParseException e) {
                e.printStackTrace();

            }
            try {
                if (datumSinkronizacijeString==null){
                    datumSinkronizacije=null;
                }else{
                    datumSinkronizacije = (Date) SQLLite_dateFormat.parse(datumSinkronizacijeString);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "getListaDokumenta: DOKUMENT: _id=" + id +"/ datumSinkronizacije=" + datumSinkronizacijeString);
            App1Dokumenti myObj = new App1Dokumenti(id, idTip, idPodtip, idKomitent, idPjKomitenta, datumDokumenta, datumSinkronizacije, napomena, komitentNaziv, komitentPjNaziv, tipNaziv, podtipNaziv, idNacinPlacanja, nacinPlacanjaNaziv,zakljucen);
            listaDokumenta.add(myObj);

            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        Log.d(TAG, "ucitajDokumente: U tabeli se nalazi " + brojac + " dokumenta!");
        c.close();
        myDB.close();

        return listaDokumenta;
    }

    public static List<App2Dokumenti> getListaDokumenta2(Activity a, int filter) {


        List<App2Dokumenti> listaDokumenta = new ArrayList<App2Dokumenti>();
        String tabelaApp1 = "dokumenti2";

        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM " + tabelaApp1 + " ORDER BY datumDokumenta DESC", null);

        SimpleDateFormat SQLLite_dateFormat = new SimpleDateFormat(MainActivity.DatumFormat);

        int id;
        long kasaId;
        long podtipId;
        String podtipNaziv;
        long pjFrmId;
        String pjFrmNaziv;
        long pjKmtId;
        String pjKmtNaziv;
        String kmtNaziv;
        long komercijalistaId;
        String komercijalistaNaziv;
        long nacinPlacanjaId;
        String nacinPlacanjaNaziv;
        long vipsId;
        String opaska;
        String datumDokumentaString;
        String datumSinkronizacijeString;
        boolean zavrsen;
        boolean zakljucen;

        int idIndex = c.getColumnIndex("_id");
        int idkasaIdIndex = c.getColumnIndex("kasaId");
        int idpodtipIdIndex = c.getColumnIndex("podtipId");
        int idpodtipNazivIndex = c.getColumnIndex("podtipNaziv");
        int idpjFrmIdIndex = c.getColumnIndex("pjFrmId");
        int idpjFrmNazivIndex = c.getColumnIndex("pjFrmNaziv");
        int idpjKmtIdIndex = c.getColumnIndex("pjKmtId");
        int idpjKmtNazivIndex = c.getColumnIndex("pjKmtNaziv");
        int idkmtNazivIndex = c.getColumnIndex("kmtNaziv");
        int iddatumDokumentaIndex = c.getColumnIndex("datumDokumenta");
        int iddatumSinkronizacijeIndex = c.getColumnIndex("datumSinkronizacije");
        int idkomercijalistaIdIndex = c.getColumnIndex("komercijalistaId");
        int idkomercijalistaNazivIndex = c.getColumnIndex("komercijalistaNaziv");
        int idnacinPlacanjaIdIndex = c.getColumnIndex("nacinPlacanjaId");
        int idnacinPlacanjaNazivIndex = c.getColumnIndex("nacinPlacanjaNaziv");
        int idvipsIdIndex = c.getColumnIndex("vipsId");
        int idopaskaIndex = c.getColumnIndex("opaska");
        int idzavrsenIndex = c.getColumnIndex("zavrsen");
        int zakljucenIndex=c.getColumnIndex("zakljucen");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            id = c.getInt(idIndex);
            kasaId = c.getLong(idkasaIdIndex);
            podtipId = c.getLong(idpodtipIdIndex);
            podtipNaziv = c.getString(idpodtipNazivIndex);
            pjFrmId = c.getLong(idpjFrmIdIndex);
            pjFrmNaziv = c.getString(idpjFrmNazivIndex);
            pjKmtId = c.getLong(idpjKmtIdIndex);
            pjKmtNaziv = c.getString(idpjKmtNazivIndex);
            kmtNaziv = c.getString(idkmtNazivIndex);
            komercijalistaId = c.getLong(idkomercijalistaIdIndex);
            komercijalistaNaziv = c.getString(idkomercijalistaNazivIndex);
            nacinPlacanjaId = c.getLong(idnacinPlacanjaIdIndex);
            nacinPlacanjaNaziv = c.getString(idnacinPlacanjaNazivIndex);
            vipsId = c.getLong(idvipsIdIndex);
            opaska = c.getString(idopaskaIndex);
            zavrsen = (c.getInt(idzavrsenIndex) == 1);
            zakljucen=c.getInt(zakljucenIndex)==1;


            datumDokumentaString = c.getString(iddatumDokumentaIndex);
            Date datumDokumenta = null;
            if (!TextUtils.isEmpty(datumDokumentaString)) {
                if (!datumDokumentaString.isEmpty()) {
                    try {
                        datumDokumenta = (Date) SQLLite_dateFormat.parse(datumDokumentaString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }


            datumSinkronizacijeString = c.getString(iddatumSinkronizacijeIndex);
            Date datumSinkronizacijeDokumenta = null;
            if (!TextUtils.isEmpty(datumSinkronizacijeString)) {
                if (!datumSinkronizacijeString.isEmpty()) {
                    try {
                        datumSinkronizacijeDokumenta = (Date) SQLLite_dateFormat.parse(datumSinkronizacijeString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            App2Dokumenti myObj = new App2Dokumenti(id, kasaId, podtipId, podtipNaziv, pjFrmId, pjFrmNaziv, pjKmtId, pjKmtNaziv, kmtNaziv,
                    datumDokumenta, datumSinkronizacijeDokumenta, komercijalistaId, komercijalistaNaziv,
                    nacinPlacanjaId, nacinPlacanjaNaziv, opaska, vipsId, zavrsen,zakljucen);
            Log.d(TAG, "BAZA getListaDokumenta2: DOKUMENT2 = " + myObj.toString());
            listaDokumenta.add(myObj);

            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        Log.d(TAG, "ucitajDokumente: U tabeli se nalazi " + brojac + " dokumenta!");
        c.close();
        myDB.close();

        return listaDokumenta;
    }

    public static Komitent getKomitentByID(Activity a, long komID){
        Komitent myKom=null;
        String myTabela="komitenti";
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM " + myTabela + " WHERE _id=" + komID + ";", null);
        int IdIndex = c.getColumnIndex("_id");
        int SifraIndex = c.getColumnIndex("sifra");
        int NazivIndex = c.getColumnIndex("naziv");
        int SaldoIndex = c.getColumnIndex("saldo");
        int uRokuIndex = c.getColumnIndex("uroku");
        int vanRokaIndex = c.getColumnIndex("vanroka");
        c.moveToFirst();
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

            myKom = new Komitent(id, naziv, sifra, saldo, uroku, vanroka);

            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        return myKom;
    }

    public static PjKomitent getPjKomitentByID(Activity a, long pjKomID){
        PjKomitent myPjKom=null;
        String myTabela="komitenti";
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM pjkomitenta where _id =" + pjKomID +";", null);
        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");
        int ridIndex=c.getColumnIndex("rid");
        c.moveToFirst();
        for (int j = 0; j < c.getCount(); j++) {
            long id,rid;
            String  naziv;
            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);
            rid=c.getLong(ridIndex);
            myPjKom = new PjKomitent(id, naziv, rid);
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        return myPjKom;
    }

    public static TipDokumenta getTipDokumentaByID(Activity a, long idDokumenta) {
        TipDokumenta tip = null;
        String myTabela = "tip_dokumenta";
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM " + myTabela + " WHERE _id=" + idDokumenta + ";", null);

        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");

        if (c.getCount() == 0) {
            return null;
        } else {
            c.moveToFirst();
            for (int j = 0; j < c.getCount(); j++) {
                long id;
                String naziv;

                id = c.getLong(IdIndex);
                naziv = c.getString(NazivIndex);

                tip = new TipDokumenta(id, naziv);

                if (j != c.getCount()) {
                    c.moveToNext();
                }
            }
            c.close();
            myDB.close();

            return tip;
        }

    }

    public static List<TipDokumenta> getListaTipovaDokumenta(Activity a, String filter, boolean prazanRed, String labelPrazanRed) {
        List<TipDokumenta> spisak = new ArrayList<TipDokumenta>();
        String myTabela = "tip_dokumenta";

        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        if (!isTableExists(myDB, myTabela)) {
            return null;
        }
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM " + myTabela, null);
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela + " where naziv like '%" + filter + "%'", null);
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


            TipDokumenta tipProvider = new TipDokumenta(id, naziv);
            spisak.add(tipProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        myDB.close();
        if (prazanRed) {
            TipDokumenta prazanRED = new TipDokumenta(0, labelPrazanRed);
            spisak.add(0, prazanRED);
        }
        return spisak;
    }

    public static PodtipDokumenta getPodtipDokumentaByID(Activity a, long idPodtipa) {
        PodtipDokumenta podtipDokumenta = null;
        String myTabela = "podtip_dokumenta";
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM " + myTabela + " WHERE _id = " + idPodtipa + ";", null);
        if (c.getCount() == 0) {
            return null;
        } else {
            int IdIndex = c.getColumnIndex("_id");
            int NazivIndex = c.getColumnIndex("naziv");
            int RidIndex = c.getColumnIndex("rid");

            c.moveToFirst();
            for (int j = 0; j < c.getCount(); j++) {
                long id;
                String naziv;
                long rid;

                id = c.getLong(IdIndex);
                naziv = c.getString(NazivIndex);
                rid = c.getLong(RidIndex);

                podtipDokumenta = new PodtipDokumenta(id, naziv, rid);

                if (j != c.getCount()) {
                    c.moveToNext();
                }
            }
            c.close();
            myDB.close();
            return podtipDokumenta;
        }
    }
    public static List<PodtipDokumenta> getListaPodtipova(Activity a, String filter, long idTipDokumenta_filter, boolean prazanRed, String labelPrazanRed) {
        List<PodtipDokumenta> spisak = new ArrayList<PodtipDokumenta>();
        String myTabela = "podtip_dokumenta";

        if (idTipDokumenta_filter == 0) {
            prazanRed = true;
            labelPrazanRed = "SVI";
        }
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;

        if (filter.equals("")) {
            if (idTipDokumenta_filter > 0) {
                c = myDB.rawQuery("SELECT * FROM " + myTabela + " WHERE rid = " + idTipDokumenta_filter + ";", null);
            } else {
                c = myDB.rawQuery("SELECT * FROM " + myTabela + ";", null);
            }
        } else {
            if (idTipDokumenta_filter > 0) {
                c = myDB.rawQuery("SELECT * FROM " + myTabela + " WHERE rid = " + idTipDokumenta_filter + " AND naziv like '%" + filter + "%'", null);
            } else {
                c = myDB.rawQuery("SELECT * FROM " + myTabela + " WHERE naziv like '%" + filter + "%'", null);
            }
        }

        int IdIndex = c.getColumnIndex("_id");
        int NazivIndex = c.getColumnIndex("naziv");
        int RidIndex = c.getColumnIndex("rid");

        c.moveToFirst();
        int brojac = 0;
        for (int j = 0; j < c.getCount(); j++) {
            long id;
            String naziv;
            long rid;

            id = c.getLong(IdIndex);
            naziv = c.getString(NazivIndex);
            rid = c.getLong(RidIndex);

            PodtipDokumenta podtipProvider = new PodtipDokumenta(id, naziv, rid);
            spisak.add(podtipProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        myDB.close();
        if (prazanRed) {
            PodtipDokumenta prazanRED = new PodtipDokumenta(0, labelPrazanRed, 0);
            spisak.add(0, prazanRED);
        }
        return spisak;
    }

    public static String getNazivZadanogPodtipaDokumenta(Activity a) {
        String myTabela = "podtip_dokumenta";
        postavkeAplikacije myPostavke = new postavkeAplikacije(a);
        long id = myPostavke.getPodtipDokumenta();
        if (id == 0) {
            return "";
        }
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM " + myTabela + " WHERE _id = " + id + ";", null);

        int NazivIndex = c.getColumnIndex("naziv");

        c.moveToFirst();
        String naziv = "";
        for (int j = 0; j < c.getCount(); j++) {
            naziv = c.getString(NazivIndex);
            break;
        }
        c.close();
        myDB.close();
        return naziv;
    }
    public static List<NacinPlacanja> getListaNacinaPlacanja(Activity a, String filter) {
        List<NacinPlacanja> spisak = new ArrayList<NacinPlacanja>();
        String myTabela = "nacin_placanja";
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, MODE_PRIVATE, null);
        Cursor c;
        if (filter.equals("")) {
            c = myDB.rawQuery("SELECT * FROM " + myTabela, null);
        } else {
            c = myDB.rawQuery("SELECT * FROM " + myTabela + " where naziv like '%" + filter + "%'", null);
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
            NacinPlacanja nacinPlacanjaProvider = new NacinPlacanja(id, naziv);
            spisak.add(nacinPlacanjaProvider);
            brojac++;
            if (j != c.getCount()) {
                c.moveToNext();
            }
        }
        c.close();
        myDB.close();
        Log.d(TAG, " Tabela " + myTabela + " učitana!");
        return spisak;
    }

    public static void updateZaglavljaPoslijeSinkronizacije(Activity a, List<App1Dokumenti> spisakSyncDokumenta) {

        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        for (App1Dokumenti dok : spisakSyncDokumenta) {
            myDB.execSQL("UPDATE dokumenti1 SET datumSinkronizacije=datetime('now') WHERE _id=" + dok.getId() + ";");
            Log.d(TAG, "updateZaglavljaPoslijeSinkronizacije: UPDATE ODRAĐEN!");
        }
        myDB.close();
    }

    public static void izbrisiDokumente2PoslijeSinkronizacije(Activity a, int idDok) {
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        myDB.execSQL("DELETE FROM dokumenti2 WHERE _id = " + idDok + ";");
        Log.d(TAG, "izbrisiDokumente2PoslijeSinkronizacije: DOKUMENTI2: Dokument IZBRISAN! ID=" + idDok);
        myDB.close();
    }

    public static boolean sendAllDokuments(Activity a, int vrstaAplikacije) {
        ProgressDialog pd = new ProgressDialog(a);
        pd.setMessage("Šaljem podatke, molim pričekajte!");
        pd.setCancelable(false);
        pd.show();

        boolean odgovor = false;
        List<App1Dokumenti> spisakSvihDokumenta = MainActivity.getListaDokumenta(a, vrstaAplikacije,0L, "","",0L,0);
        List<App1Dokumenti> spisakDokumentaZaSync = new ArrayList<App1Dokumenti>();
        for (App1Dokumenti dok : spisakSvihDokumenta) {
            if (dok.getDatumSinkronizacije() == null) {
                if (dok.isZakljucen()){
                    spisakDokumentaZaSync.add(dok);
                }
            }
        }

        for (App1Dokumenti dok : spisakDokumentaZaSync) {
            dok.izbrisiSveStavke();
            List<App1Stavke> mojeStavke = MainActivity.getListaStavki(dok.getId(), a);
            for (App1Stavke stv : mojeStavke) {
                dok.doadajStavku(stv);
            }
        }
        try {
            String rezultat = new JSON_send(a, spisakDokumentaZaSync, false, vrstaAplikacije).execute().get();
            if (TextUtils.isEmpty(rezultat)) {
                Toast.makeText(a, "Nije moguće poslati podatke! Provjerite internet konekciju i dostupnost servera!", Toast.LENGTH_LONG).show();
                pd.dismiss();
                return false;
            }
            if (rezultat.equals("OK")) {
                MainActivity.updateZaglavljaPoslijeSinkronizacije(a, spisakDokumentaZaSync);
                odgovor = true;
            }
            Log.d(TAG, "UPDATE URAĐEN ZA " + spisakDokumentaZaSync.size() + " DOKUMENATA!");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        pd.dismiss();
        return odgovor;
    }

    public static void kreirajTabeluStavki(Activity a) {
        String tabelaApp1 = "stavke1";
        SQLiteDatabase myDB = null;
        Log.d(TAG, "Otvaram bazu");
        myDB = a.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
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
                "vrijednostAtributa VARCHAR, " +
                "nazivAtributa VARCHAR," +
                "idJmj long," +
                "nazivJmj VARCHAR," +
                "napomena VARCHAR," +
                "datumUpisa datetime default current_timestamp);");
        myDB.close();
    }

    public static void kreirajTabeluDokumenata(Activity a) {
        String tabelaApp1 = "dokumenti1";
        SQLiteDatabase myDB = null;
        Log.d(TAG, "Otvaram bazu");
        myDB = a.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "Kreiram tabelu");
        //myDB.execSQL("DROP TABLE " + tabelaApp1 + ";");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS " + tabelaApp1 + " (" +
                "_id Integer PRIMARY KEY AUTOINCREMENT, " +
                "idTip VARCHAR, " +
                "TipDokumentaNaziv VARCHAR," +
                "idPodtip long," +
                "PodipDokumentaNaziv VARCHAR," +
                "idKomitent long, " +
                "KomitentNaziv VARCHAR," +
                "idPjKomitenta long, " +
                "PjKomitentaNaziv VARCHAR," +
                "idVrstaPlacanja long, " +
                "VrstaPlacanjaNaziv VARCHAR, " +
                "datumDokumenta datetime, " +
                "datumSinkronizacije datetime," +
                "datumUpisa datetime default current_timestamp," +
                "vrstaAplikacije INTEGER, " +
                "zakljucen INTEGER DEFAULT 0," +
                "napomena VARCHAR);");
        myDB.close();

    }

    public static void snimiStavku(Activity a, long IdDokumenta, App1Stavke stavka) {
        App1Stavke rezultat = stavka;
        String tabelaApp1 = "stavke1";
        SQLiteDatabase myDB = null;
        myDB = a.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        if (rezultat.isImaAtribut()) {
            Log.d(TAG, "onActivityResult: UPISUJEM ARTIKL SA ATRIBUTOM!");
            myDB.execSQL("INSERT INTO " + tabelaApp1 + " (idDokumenta, idArtikla, nazivArtikla, kolicina, imaAtribut, idAtributa, vrijednostAtributa, nazivAtributa, " +
                    "idJmj, nazivJmj,napomena ) VALUES (" +
                    IdDokumenta + "," + rezultat.getArtiklId() + ",'" + rezultat.getArtiklNaziv() + "'," + rezultat.getKolicina() + ", '" + rezultat.isImaAtribut() + "'," + rezultat.getAtributId() + ", '" +
                    rezultat.getAtributVrijednost() + "','" + rezultat.getAtributNaziv() + "'," + rezultat.getJmjId() + ",'" + rezultat.getJmjNaziv() + "','" + rezultat.getNapomena() + "');");
        } else {
            Log.d(TAG, "onActivityResult: UPISUJEM ARTIKL BEZ ATRIBUTA!");
            myDB.execSQL("INSERT INTO " + tabelaApp1 + " (idDokumenta, idArtikla, nazivArtikla, kolicina, imaAtribut, idAtributa, vrijednostAtributa, nazivAtributa, " +
                    "idJmj, nazivJmj,napomena ) VALUES (" +
                    IdDokumenta + "," + rezultat.getArtiklId() + ",'" + rezultat.getArtiklNaziv() + "'," + rezultat.getKolicina() + ", '" + rezultat.isImaAtribut() + "',null,null" +
                    ",null," + rezultat.getJmjId() + ",'" + rezultat.getJmjNaziv() + "','" + rezultat.getNapomena() + "');");
        }
        myDB.close();
    }

    public static void snimiStavku2(Activity a, long IdDokumenta, App2Stavke stavka) {
        App2Stavke myStv2 = stavka;
        String tabelaApp2 = "stavke2";
        SQLiteDatabase myDB = null;
        myDB = a.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);

        myDB.execSQL("INSERT INTO " + tabelaApp2 + " ( zaglavljeId, rbr, artiklId, artiklSifra, artiklNaziv,  jmjId, jmjNaziv, vrijednostId1, vrijednostNaziv," +
                " atributNaziv, kolicina, opaska, vipsId, kolicinaZadana) VALUES (" +
                //myStv2.getId() + "," +
                IdDokumenta + "," +
                myStv2.getRbr() + "," +
                myStv2.getArtiklId() + ",'" +
                myStv2.getArtiklSifra() + "','" +
                myStv2.getArtiklNaziv() + "'," +
                myStv2.getJmjId() + ",'" +
                myStv2.getJmjNaziv() + "'," +
                myStv2.getVrijednostId1() + ",'" +
                myStv2.getVrijednostNaziv() + "','" +
                myStv2.getAtributNaziv() + "'," +
                myStv2.getKolicina() + ",'" +
                myStv2.getOpaska() + "',0,0);");

        myDB.close();
    }

    public static void updateStavke2(Activity a, App2Stavke stavka) {
        App2Stavke rezultat = stavka;
        String tabelaApp1 = "stavke2";
        SQLiteDatabase myDB = null;
        myDB = a.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        String strSQL = "UPDATE " + tabelaApp1 + " SET kolicina = " + String.valueOf(rezultat.getKolicina()) + " WHERE _id = " + rezultat.getId() + ";";
        myDB.execSQL(strSQL);
        myDB.close();
        //poslije update-a stavke mora se i dokument updetati!!!
        MainActivity.updateZavrsenoInDokumenti2(a, stavka.getZaglavljeId());
    }

    public static void svirajUpozorenje(postavkeAplikacije myPostavke) {
        if (myPostavke.isSvirajUpozorenja()) {
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 90);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);
        }

    }

    public static void svirajOK(postavkeAplikacije myPostavke) {
        if (myPostavke.isSvirajUpozorenja()) {
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 90);
            toneG.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 500);
        }
    }
}
