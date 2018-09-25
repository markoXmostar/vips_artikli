package com.example.marko.vips_artikli.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marko.vips_artikli.models.App1Dokumenti;
import com.example.marko.vips_artikli.models.App1Stavke;
import com.example.marko.vips_artikli.utils.JSON_send;
import com.example.marko.vips_artikli.adapters.ListaApp1DokumentiAdapter;
import com.example.marko.vips_artikli.adapters.MyPrintDocumentAdapter;
import com.example.marko.vips_artikli.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.example.marko.vips_artikli.activities.MainActivity.DatumFormat;
import static com.example.marko.vips_artikli.activities.MainActivity.myDATABASE;

public class App1DokumentiActivity extends AppCompatActivity {
    private static String TAG = "App1Dokumenti";
    private static String tabelaApp1 = "dokumenti1";

    private Long filterKomitentID,filterPjKomitentID;
    private String filterKomitentNaziv,filterPjKomitentNaziv;
    private String filterDatumOd;
    private String filterDatumDo;
    private int filterZakljucen;

    int vrstaAplikacije = 0;

    private ListView listSpisakDokumenata;
    private FloatingActionButton fabNoviDokument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_dokumenti);

        setFilterPocetnePostavke();

        //getActionBar().setTitle(getString(R.string.title_activity_app1_dokumenti));
        Bundle b = getIntent().getExtras();
        vrstaAplikacije = b.getInt("vrstaAplikacije", 1);

        listSpisakDokumenata = (ListView) findViewById(R.id.listSpisakDokumenata_App1);
        listSpisakDokumenata.setItemsCanFocus(false);
        fabNoviDokument = (FloatingActionButton) findViewById(R.id.fabNovoZaglavlje_App1);


        kreirajTabeluDokumenata();
        ucitajDokumente();

        fabNoviDokument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App1DokumentiActivity.this, App1UnosZaglavljaActivity.class);
                intent.putExtra("vrstaAplikacije", vrstaAplikacije);
                intent.putExtra("akcija", 0);
                intent.putExtra("dokumentID", 0);
                startActivityForResult(intent,1);
            }
        });

        listSpisakDokumenata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                App1Dokumenti selektiranDok=(App1Dokumenti) adapterView.getItemAtPosition(i);
                //Toast.makeText(App1DokumentiActivity.this,selektiranDok.getId() +"/" +selektiranDok.getDatumDokumentaString() + "-" + selektiranDok.getKomitentNaziv(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(App1DokumentiActivity.this, App1StavkeActivity.class);
                intent.putExtra("idDokumenta", selektiranDok.getId());
                intent.putExtra("pjKmtID", selektiranDok.getIdPjKomitenta());
                boolean isSync = false;
                if (selektiranDok.getDatumSinkronizacije() != null) {
                    isSync = true;
                }
                intent.putExtra("isSync", isSync);
                startActivityForResult(intent,21); //21 označava završen unos stavki

            }
        });

        listSpisakDokumenata.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                final App1Dokumenti selektiranDok=(App1Dokumenti) adapterView.getItemAtPosition(i);
                final CharSequence akcije[];
                final String zakljucenString;
                if (selektiranDok.isZakljucen()){
                    zakljucenString="OTKLJUČAJ";
                }else {
                    zakljucenString="ZAKLJUČI";
                }
                if (selektiranDok.getDatumSinkronizacije() == null) {
                    if (selektiranDok.isZakljucen()){
                        akcije = new CharSequence[]{"Izbriši dokument!","Izmijeni dokument", zakljucenString, "Sinkroniziraj", "Ispis / detalji dokumenta"};
                    }else{
                        akcije = new CharSequence[]{"Izbriši dokument!","Izmijeni dokument", zakljucenString,  "Ispis / detalji dokumenta"};
                    }
                } else {
                    akcije = new CharSequence[]{"Izbriši dokument!","Izmijeni dokument", zakljucenString,  "Ispis / detalji dokumenta"};
                }

                //final CharSequence akcije[] = new CharSequence[] {"Izbriši", "Sinkroniziraj", "Prikaži detalje"};
                AlertDialog.Builder builder = new AlertDialog.Builder(App1DokumentiActivity.this);
                builder.setTitle("Opcije dokumenta");
                builder.setItems(akcije, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        switch (akcije[which].toString()){
                            case "Izbriši dokument!":
                                MainActivity.izbrisiRedakIzTabele(App1DokumentiActivity.this,tabelaApp1,"_id", selektiranDok.getId());
                                ucitajDokumente();
                                break;
                            case "Izmijeni dokument":
                                Intent intent = new Intent(App1DokumentiActivity.this, App1UnosZaglavljaActivity.class);
                                intent.putExtra("vrstaAplikacije", vrstaAplikacije);
                                intent.putExtra("akcija", 1);
                                intent.putExtra("dokumentID", selektiranDok.getId());
                                intent.putExtra("napomena", selektiranDok.getNapomena());
                                startActivityForResult(intent,1);
                                break;
                            case "ZAKLJUČI":
                            case "OTKLJUČAJ":
                                MainActivity.setZakljuciDokument_Dokumenti1(App1DokumentiActivity.this, selektiranDok.getId(),!selektiranDok.isZakljucen());
                                ucitajDokumente();
                                break;
                            case "Sinkroniziraj":
                                if (selektiranDok.isZakljucen()){
                                    List<App1Dokumenti> spisakDokZaSync = getDokumentZaSyncIliPrintanje(selektiranDok);
                                    try {
                                        String rezultat = new JSON_send(App1DokumentiActivity.this, spisakDokZaSync, false, vrstaAplikacije).execute().get();
                                        Log.d(TAG, "onClick: REZULTAT ASYNCTASKA JE=>" + rezultat);
                                        if (TextUtils.isEmpty(rezultat)) {
                                            Toast.makeText(App1DokumentiActivity.this, "Nije moguće poslati podatke! Provjerite internet konekciju i dostupnost servera!", Toast.LENGTH_LONG).show();
                                        } else {
                                            if (rezultat.equals("OK")) {
                                                //ucitajDokumente();
                                                MainActivity.updateZaglavljaPoslijeSinkronizacije(App1DokumentiActivity.this, spisakDokZaSync);
                                                ucitajDokumente();
                                            }
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    Toast.makeText(App1DokumentiActivity.this,"Samo zaključeni dokumenti se mogu sinkronizirati!",Toast.LENGTH_LONG).show();
                                }

                                break;
                            case "Ispis / detalji dokumenta":
                                List<App1Dokumenti> spisakDokZaPrint = getDokumentZaSyncIliPrintanje(selektiranDok);
                                doPrint(spisakDokZaPrint.get(0));
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                vrstaAplikacije = data.getIntExtra("vrstaAplikacije", vrstaAplikacije);
                long IdKomitenta = data.getLongExtra("idKomitenta", -1);
                long IdPjKomitenta = data.getLongExtra("idPjKomitenta", -1);
                long idTipDokumenta = data.getLongExtra("idTipDokumenta", -1);
                long idPodtipDokumenta = data.getLongExtra("idPodtipDokumenta", -1);
                long idVrstaPlacanja = data.getLongExtra("idVrstaPlacanja", -1);
                String datumDokumenta=data.getStringExtra("datumDokumenta");
                long myDokumentID=data.getLongExtra("dokumentID",0);
                int akcija=data.getIntExtra("akcija",0);

                SimpleDateFormat dateFormat = new SimpleDateFormat(DatumFormat);


                Date datumDok=new Date();
                SimpleDateFormat dateSQLLiteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                String SQLDatum;
                try {
                    datumDok=dateFormat.parse(datumDokumenta);
                    SQLDatum= dateSQLLiteFormat.format(datumDok);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.GreskaUnosDatuma,Toast.LENGTH_LONG).show();
                    return;
                }

                String nazivPjKomitenta=data.getStringExtra("nazivPjKomitenta");
                String nazivKomitenta=data.getStringExtra("nazivKomitenta");
                String nazivTipDokumenta=data.getStringExtra("nazivTipDokumenta");
                String nazivPodtipDokumenta=data.getStringExtra("nazivPodtipDokumenta");
                String nazivVrstaPlacanja = data.getStringExtra("nazivVrstaPlacanja");
                if (nazivVrstaPlacanja == null) {
                    nazivVrstaPlacanja = "";
                }
                String napomena=data.getStringExtra("napomena");
                if (napomena==null){
                    napomena="";
                }
                SQLiteDatabase myDB = null;
                Log.d(TAG, "Otvaram bazu");
                myDB = openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
                if (akcija==0){
                    myDB.execSQL("INSERT INTO " + tabelaApp1 +" (idTip, TipDokumentaNaziv, idPodtip, PodipDokumentaNaziv, idKomitent, KomitentNaziv, idPjKomitenta, PjKomitentaNaziv, " +
                            "idVrstaPlacanja, VrstaPlacanjaNaziv, datumDokumenta, vrstaAplikacije , napomena) VALUES (" +
                            idTipDokumenta + ",'" + nazivTipDokumenta + "', " + idPodtipDokumenta + ",'" + nazivPodtipDokumenta + "', " + IdKomitenta + ",'" + nazivKomitenta + "', " +
                            IdPjKomitenta + ",'" + nazivPjKomitenta + "'," + idVrstaPlacanja + ",'" + nazivVrstaPlacanja + "','" + SQLDatum + "'," + vrstaAplikacije + ",'" + napomena + "');");
                }else{
                    myDB.execSQL("UPDATE " + tabelaApp1 +" SET idTip=" + idTipDokumenta + ","
                                                                +" TipDokumentaNaziv='" + nazivTipDokumenta +"',"
                                                                +" idPodtip="+ idPodtipDokumenta +","
                                                                +" PodipDokumentaNaziv='"+ nazivPodtipDokumenta+"',"
                                                                +" idKomitent=" + IdKomitenta +","
                                                                +" KomitentNaziv='"+ nazivKomitenta+"',"
                                                                +" idPjKomitenta=" + IdPjKomitenta +","
                                                                +" PjKomitentaNaziv='" + nazivPjKomitenta + "',"
                                                                +" idVrstaPlacanja=" + idVrstaPlacanja + ","
                                                                +" VrstaPlacanjaNaziv='" + nazivVrstaPlacanja + "',"
                                                                +" datumDokumenta= '" +SQLDatum + "',"
                                                                +" vrstaAplikacije=" + vrstaAplikacije + ","
                                                                +" napomena='" + napomena +"'"
                                                                +" WHERE _id=" + myDokumentID + ";");
                }

                myDB.close();
                ucitajDokumente();

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
        if (requestCode == 21) {
            //treba osvježiti stavke na zadanom dokumentu (vraćanje sa actovotoja za stavke

            ucitajDokumente();
        }

        if (requestCode==31){
            //povratak sa filtera
            if (resultCode==Activity.RESULT_OK){
                long filterIdKom = data.getLongExtra("filterKomitentID", -1);
                String filterNazKom=data.getStringExtra("filterKomitentNaziv");
                String dat1=data.getStringExtra("filterDatumOd");
                String dat2=data.getStringExtra("filterDatumDo");
                long filterIdPjKom=data.getLongExtra("filterPjKomitentaID",-1);
                String filterNazPjKom=data.getStringExtra("filterPjKOmitentaNaziv");
                int filterZakljucen=data.getIntExtra("filterZakjucen",0);

                setFilterPostavke(dat1,dat2,filterIdKom,filterNazKom,filterZakljucen,filterIdPjKom,filterNazPjKom);
                ucitajDokumente();
            }
            if (resultCode==Activity.RESULT_CANCELED){
                setFilterPocetnePostavke();
                ucitajDokumente();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (MainActivity.zadanaVrstaAplikacija == 0) {
            super.onBackPressed();
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

    }

    private List<App1Dokumenti> getDokumentZaSyncIliPrintanje(App1Dokumenti selektiranDok) {
        List<App1Dokumenti> spisakDokZaSync = new ArrayList<App1Dokumenti>();
        spisakDokZaSync.add(selektiranDok);
        selektiranDok.izbrisiSveStavke();
        selektiranDok.setVrstaAplikacije(vrstaAplikacije);
        List<App1Stavke> mojeStavke = MainActivity.getListaStavki(selektiranDok.getId(), App1DokumentiActivity.this);
        for (App1Stavke stv : mojeStavke) {
            selektiranDok.doadajStavku(stv);
        }
        return spisakDokZaSync;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app1_dokumenti_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.drop_table_dokumenti1) {
            MainActivity.dropTable(App1DokumentiActivity.this,tabelaApp1);
            finish();
            return true;
        }
        if (id == R.id.delete_all_dokumenti1) {
            MainActivity.deleteAllTable(App1DokumentiActivity.this,tabelaApp1);
            ucitajDokumente();
            return true;
        }
        */
        if (id == R.id.sinkroniziraj_dokumenti1) {
            if (MainActivity.sendAllDokuments(App1DokumentiActivity.this, vrstaAplikacije)) {
                ucitajDokumente();
            }
            return true;
        }
        if (id == R.id.pretragaDokument1) {
            Intent intent=new Intent(App1DokumentiActivity.this, filterPretragaDokumenataActivity.class);
            //intent.putExtra("idDokumenta", selektiranDok.getId());
            intent.putExtra("datumOd", filterDatumOd);
            intent.putExtra("datumDo", filterDatumDo);
            intent.putExtra("komitentID", filterKomitentID);
            intent.putExtra("komitentNaziv", filterKomitentNaziv);
            intent.putExtra("filterPjKomitentID", filterPjKomitentID);
            intent.putExtra("filterPjKomitentNaziv", filterPjKomitentNaziv);
            intent.putExtra("filterZakljucen",filterZakljucen);

            startActivityForResult(intent,31); //21 označava završen unos stavki

            return true;
        }
        if (id == R.id.settingsActivity) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void doPrint(App1Dokumenti dokumentZaPrint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            String jobName = "DocumentName.pdf";
            PrintAttributes printAttrs = new PrintAttributes.Builder().
                    setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME).
                    setMediaSize(PrintAttributes.MediaSize.ISO_A4.asPortrait()).
                    setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                    build();

            printManager.print(jobName, new MyPrintDocumentAdapter(this, dokumentZaPrint), printAttrs);
        } else {
            Toast.makeText(App1DokumentiActivity.this, "Nije podržano na ovome sistemu!!! (Android APIlevel > 19 - KitKat)", Toast.LENGTH_LONG).show();
        }

    }

    private void kreirajTabeluDokumenata() {
        MainActivity.kreirajTabeluDokumenata(App1DokumentiActivity.this);
    }

    private void setFilterPocetnePostavke(){

        DateFormat dateFormat =new SimpleDateFormat(MainActivity.SqlLiteDateFormat);
        Calendar calOd = Calendar.getInstance();
        calOd.add(Calendar.DATE, 0);
        int dan=calOd.get(Calendar.DAY_OF_MONTH);
        int mjesec=calOd.get(Calendar.MONTH);
        int godina=calOd.get(Calendar.YEAR);
        int sat=0;
        int min=0;
        int sec=0;
        filterDatumOd=MainActivity.danMjesecGodinaToSQLLiteFormatString(dan,mjesec,godina,sat,min,sec);

        Calendar calDo = Calendar.getInstance();
        calDo.add(Calendar.DATE, 0);
        dan=calDo.get(Calendar.DAY_OF_MONTH);
        mjesec=calDo.get(Calendar.MONTH);
        godina=calDo.get(Calendar.YEAR);
        sat=23;
        min=59;
        sec=59;
        filterDatumDo=MainActivity.danMjesecGodinaToSQLLiteFormatString(dan,mjesec,godina,sat,min,sec);

        filterKomitentID=0L;
        filterKomitentNaziv="";
        filterPjKomitentID=0L;
        filterPjKomitentNaziv="";
        filterZakljucen=0;

    }

    private void setFilterPostavke(String datum1,String datum2, Long komID, String komNaziv, int zakljucen, Long pjKomId, String pjKomNaziv){
        Date datumOd=MainActivity.getDateFromMojString(datum1);
        Date datumDo=MainActivity.getDateFromMojString(datum2);
        Calendar calOd=Calendar.getInstance();
        calOd.setTime(datumOd);
        Calendar calDo=Calendar.getInstance();
        calDo.setTime(datumDo);

        int dan=calOd.get(Calendar.DAY_OF_MONTH);
        int mjesec=calOd.get(Calendar.MONTH);
        int godina=calOd.get(Calendar.YEAR);
        int sat=0;
        int min=0;
        int sec=0;
        filterDatumOd=MainActivity.danMjesecGodinaToSQLLiteFormatString(dan,mjesec,godina,sat,min,sec);

        dan=calDo.get(Calendar.DAY_OF_MONTH);
        mjesec=calDo.get(Calendar.MONTH);
        godina=calDo.get(Calendar.YEAR);
        sat=0;
        min=0;
        sec=0;
        filterDatumDo=MainActivity.danMjesecGodinaToSQLLiteFormatString(dan,mjesec,godina,sat,min,sec);

        filterKomitentID=komID;
        filterKomitentNaziv=komNaziv;
        filterPjKomitentNaziv=pjKomNaziv;
        filterZakljucen=zakljucen;
        filterPjKomitentID=pjKomId;
    }

    private void ucitajDokumente() {
        ListaApp1DokumentiAdapter listaDokumenta = new ListaApp1DokumentiAdapter(this, R.layout.row_app1_zaglavlje);
        listSpisakDokumenata.setAdapter(listaDokumenta);
        List<App1Dokumenti> spisakDok = MainActivity.getListaDokumenta(App1DokumentiActivity.this, vrstaAplikacije,filterKomitentID,filterDatumOd,filterDatumDo,filterPjKomitentID,filterZakljucen);
        for (App1Dokumenti dok:spisakDok) {
            listaDokumenta.add(dok);
            //sada svakom dokumetu pridruži njegove stavke kako bi mogli ispisati broj i sumu stavki
            dok.izbrisiSveStavke();
            List<App1Stavke> mojeStavke = MainActivity.getListaStavki(dok.getId(), App1DokumentiActivity.this);
            for (App1Stavke stv : mojeStavke) {
                dok.doadajStavku(stv);
            }
        }
    }

}
