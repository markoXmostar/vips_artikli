package com.example.marko.vips_artikli;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG="Glavni MainActivity";
    public static final String myDATABASE="VIPS.db";
    public static final String SqlLiteDateFormat="yyyy-MM-dd HH:mm:ss";
    public static String DatumVrijemeFormat = "dd.MM.yyyy HH:mm:ss";
    public static String DatumFormat="dd.MM.yyyy";

    public static MainActivity ma;

    public static int DJELATNIK = 2;
    public static String url = "http://vanima.net:8099/api/";

    public static int zadnjaSinkronizacijaID;

    public static void setZadnjaSinkronizacijaVrijeme(Date zadnjaSinkronizacijaVrijeme) {
        MainActivity._zadnjaSinkronizacijaVrijeme = zadnjaSinkronizacijaVrijeme;
    }

    public static Date getZadnjaSinkronizacijaVrijeme() {
        return _zadnjaSinkronizacijaVrijeme;
    }

    private static Date _zadnjaSinkronizacijaVrijeme;

    public List<UrlTabele> spisakSyncTabela;

    TextView txtLastSyncDate;
    TextView txtLastSyncID;
    ListView listSyncLog;
    TextView txtPotrebnaSinkronizacija;
    FloatingActionButton fabUpdatePodataka,fabApp1,fabApp2,fabApp3;

    View glavniView;

    Integer lastSyncID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity ma=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtLastSyncID=(TextView)findViewById(R.id.txtSyncID_main);
        txtLastSyncDate = (TextView) findViewById(R.id.txtDatumZadnjeSinkronizacije_main);

        listSyncLog=(ListView)findViewById(R.id.listSyncLog_main);
        txtPotrebnaSinkronizacija=(TextView)findViewById(R.id.txtPotrebnaSinkronizacija);
        fabUpdatePodataka = (FloatingActionButton) findViewById(R.id.fabUpdatePodataka);
        fabApp1 = (FloatingActionButton) findViewById(R.id.fabApp1);
        fabApp2 = (FloatingActionButton) findViewById(R.id.fabApp2);
        fabApp3 = (FloatingActionButton) findViewById(R.id.fabApp3);
        spisakSyncTabela = new ArrayList<UrlTabele>();

        postaviTabeleZaSync();
        getLOG();



        fabUpdatePodataka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                napraviSinkronizacijuDownload(view);

            }
        });

        fabApp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, App1DokumentiActivity.class);
                startActivity(intent);
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
    }
private void postaviVidljivostFabKontrola(boolean potrebnaSyncVisible){
        if (potrebnaSyncVisible){
            Log.d(TAG, "postaviVidljivostFabKontrola: Vidljivost" + !potrebnaSyncVisible);
            fabUpdatePodataka.setVisibility(View.VISIBLE);
            txtPotrebnaSinkronizacija.setVisibility(View.VISIBLE);

            fabApp1.setVisibility(View.INVISIBLE);
            fabApp2.setVisibility(View.INVISIBLE);
            fabApp3.setVisibility(View.INVISIBLE);


            txtLastSyncID.setText("-1");
            txtLastSyncDate.setText("/ NIKAD");
        }
        else{
            Log.d(TAG, "postaviVidljivostFabKontrola: Vidljivost" + potrebnaSyncVisible);
            fabUpdatePodataka.setVisibility(View.INVISIBLE);
            txtPotrebnaSinkronizacija.setVisibility(View.INVISIBLE);
            fabApp1.setVisibility(View.VISIBLE);
            fabApp2.setVisibility(View.VISIBLE);
            fabApp3.setVisibility(View.VISIBLE);


        }

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
        Integer rbr=0;
        ListaDbLogAdapter listaLog = new ListaDbLogAdapter(this, R.layout.row_log);
        listSyncLog.setAdapter(listaLog);
        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        boolean tabelaLogPostoji=true;




        if (isTableExists(myDB, myTabela)){
            if (isFieldExist(myDB,myTabela,"redniBroj")){

                postaviVidljivostFabKontrola(false);
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

                txtLastSyncID.setText(Integer.toString(rbr));
                zadnjaSinkronizacijaID=rbr;
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
                        String myDatum=parseDateFromSQLLiteDBFormatToMyFormat(dd);
                        setZadnjaSinkronizacijaVrijeme(dd);
                        greska = z.getInt(greskaIndex);
                        Log.d(TAG, "getLOG: ");
                        //dbLog myLog = new dbLog(id, timestamp, greska, naziv, rbr, tabela);
                        dbLog myLog = new dbLog(id, myDatum, greska, naziv, rbr, tabela);
                        listaLog.add(myLog);
                        brojac++;
                        if (j != z.getCount()) {
                            z.moveToNext();
                        }
                    }
                    if (getZadnjaSinkronizacijaVrijeme() != null) {
                        txtLastSyncDate.setText(parseDateFromSQLLiteDBFormatToMyFormat(getZadnjaSinkronizacijaVrijeme()));
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
        if (!tabelaLogPostoji){
            rekreirajLogTabelu(myDB);
            rbr=0;
            postaviVidljivostFabKontrola(true);
        }
        myDB.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        } else if (id== R.id.nav_recive){

            fabUpdatePodataka.callOnClick();

        } else if (id==R.id.nav_log){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class UrlTabele {
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
        urlString = url + akcija + "?d=" + DJELATNIK;
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "komitenti"));

        akcija = "komitentpj";
        urlString = url + "idnazivrid" + "?d=" + DJELATNIK + "&t=" + akcija;
        spisakSyncTabela.add(new UrlTabele(akcija, urlString, true, "PjKomitenta"));

    }

    private void napraviSinkronizacijuDownload(View view) {


        Snackbar.make(view, "Radim download podataka molim pričekajte", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        Log.d(TAG, "napraviSinkronizacijuDownload: BROJ ABEA ZA SYNC=" + spisakSyncTabela.size());

        for (UrlTabele tbl : spisakSyncTabela) {
            Log.d(TAG, "napraviSinkronizacijuDownload: Radim tabelu =" + tbl.NazivTabele);
            tbl.ZavrsenaSyncronizacija = false;
            new JSON_task(this, tbl).execute(tbl.urlTabele, tbl.Akcija);
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

    public static String danMjesecGodinaToFormatString(int dan,int mjesec,int godina){
        SimpleDateFormat dateFormat=new SimpleDateFormat(MainActivity.DatumFormat);
        //SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");
        Calendar c=Calendar.getInstance();
        c.set(godina,mjesec,dan,0,0);
        Date datum=c.getTime();

        String datumStr=dateFormat.format(datum);
        return datumStr;
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
    public static String parseDateFromSQLLiteDBFormatToMyFormat(Date date){

        SimpleDateFormat mojDateFormat=new SimpleDateFormat(DatumVrijemeFormat);
        String myDateString=mojDateFormat.format(date);
        return myDateString;

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
        return naziv;
    }

    public static List<ArtiklAtributStanje> getListaAtributa(Activity a,long artiklID){
        List<ArtiklAtributStanje> listaAtributa = new ArrayList<ArtiklAtributStanje>();
        SQLiteDatabase myDB = a.openOrCreateDatabase(MainActivity.myDATABASE, a.MODE_PRIVATE, null);
        Cursor c;
        c = myDB.rawQuery("SELECT * FROM artiklatribut where artiklID = " +artiklID + ";", null);
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
        return  listaAtributa;
    }
}
