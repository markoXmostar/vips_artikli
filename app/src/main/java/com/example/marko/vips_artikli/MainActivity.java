package com.example.marko.vips_artikli;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG="Glavni MainActivity";
    public static final String myDATABASE="VIPS.db";
    public static MainActivity ma;

    public static int DJELATNIK = 2;
    public static String url = "http://vanima.net:8099/api/";

    TextView txtLastSyncTime;
    TextView txtLastSyncID;
    ListView listSyncLog;
    Integer lastSyncID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity ma=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtLastSyncID=(TextView)findViewById(R.id.txtSyncID_main);
        txtLastSyncTime=(TextView) findViewById(R.id.txtSyncTime_main);
        listSyncLog=(ListView)findViewById(R.id.listSyncLog_main);
        getLOG();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    private void getLOG(){
        String myTabela="log";
        Integer rbr=0;
        ListaDbLogAdapter listaLog = new ListaDbLogAdapter(this, R.layout.row_log);
        listSyncLog.setAdapter(listaLog);
        SQLiteDatabase myDB = this.openOrCreateDatabase(MainActivity.myDATABASE, this.MODE_PRIVATE, null);
        if (isTableExists(myDB, myTabela)){
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

            c = myDB.rawQuery("SELECT + FROM " + myTabela + "WHERE redniBroj =" + Integer.toString(rbr) + ";", null);
            brojac = 0;
            int idIndex=c.getColumnIndex("_id");
            int TabelaIndex = c.getColumnIndex("tabela");
            int NazivIndex = c.getColumnIndex("greskaMsg");
            int vrijemeIndex=c.getColumnIndex("timestamp");
            int greskaIndex=c.getColumnIndex("greska");

            for (int j = 0; j < c.getCount(); j++) {
                Integer id;
                String tabela;
                String naziv;
                String timestamp;
                Integer greska;

                id=c.getInt(idIndex);
                tabela = c.getString(TabelaIndex);
                naziv = c.getString(NazivIndex);
                timestamp=c.getString(vrijemeIndex);
                greska=c.getInt(greskaIndex);

                dbLog myLog = new dbLog(id,timestamp,greska,naziv,rbr,tabela);
                listaLog.add(myLog);
                brojac++;
                if (j != c.getCount()) {
                    c.moveToNext();
                }
            }
            c.close();
            myDB.close();
        }
        else{

        }


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

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_artikli) {
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
            /*
            url=http://vanima.net:8099/api/

            api/artikli?d=2
            api/idnaziv?d=2&t=jmj
            api/idnaziv?d=2&t=tipdokumenta
            api/idnaziv?d=2&t=nacinplacanja
            api/idnaziv?d=2&t=grupaartikala
            api/idnazivrid?d=2&t=podgrupaartikala
            api/idnazivrid?d=2&t=komitentpj
            api/idnazivrid?d=2&t=podtipdokumenta

             */
            String akcija = "";
            String urlString="";

            akcija = "jmj";
            urlString = url + "idnaziv" + "?d=" + DJELATNIK +"&t=" + akcija;
            new JSON_task(this).execute(urlString, akcija);

            akcija="tipdokumenta";
            urlString = url + "idnaziv" + "?d=" + DJELATNIK +"&t=" + akcija;
            new JSON_task(this).execute(urlString, akcija);

            akcija="podtipdokumenta";
            urlString = url + "idnazivrid" + "?d=" + DJELATNIK +"&t=" + akcija;
            Log.d(TAG, "onNavigationItemSelected: " + urlString);
            new JSON_task(this).execute(urlString, akcija);

            akcija="nacinplacanja";
            urlString = url + "idnaziv" + "?d=" + DJELATNIK +"&t=" + akcija;
            new JSON_task(this).execute(urlString, akcija);

            akcija="grupaartikala";
            urlString = url + "idnazivrid" + "?d=" + DJELATNIK +"&t=" + akcija;
            Log.d(TAG, "onNavigationItemSelected: " + urlString);
            new JSON_task(this).execute(urlString, akcija);

            akcija="podgrupaartikala";
            urlString = url + "idnazivrid" + "?d=" + DJELATNIK +"&t=" + akcija;
            Log.d(TAG, "onNavigationItemSelected: " + urlString);
            new JSON_task(this).execute(urlString, akcija);

            akcija = "artikli";
            urlString = url + akcija + "?d=" + DJELATNIK;
            new JSON_task(this).execute(urlString, akcija);

            akcija = "komitenti";
            urlString = url + akcija + "?d=" + DJELATNIK;
            new JSON_task(this).execute(urlString, akcija);

            akcija="komitentpj";
            urlString = url + "idnazivrid" + "?d=" + DJELATNIK +"&t=" + akcija;
            Log.d(TAG, "onNavigationItemSelected: " + urlString);
            new JSON_task(this).execute(urlString, akcija);

        } else if (id==R.id.nav_log){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
