package com.example.marko.vips_artikli.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marko.vips_artikli.models.App1Stavke;
import com.example.marko.vips_artikli.adapters.ListaApp1StavkeAdapter;
import com.example.marko.vips_artikli.R;

import java.util.List;

public class App1StavkeActivity extends AppCompatActivity {
    private static String TAG = "App1Stavke";
    private static String tabelaApp1 = "stavke1";

    private long IdDokumenta=0;
    private long pjKmtID = 0;

    private ListView listSpisakStavki;
    private FloatingActionButton fabNovaStavka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_stavke);

        Bundle b = getIntent().getExtras();

        final long IdDok = b.getLong("idDokumenta");
        pjKmtID = b.getLong("pjKmtID");

        IdDokumenta = IdDok;

        fabNovaStavka = (FloatingActionButton) findViewById(R.id.fabNovaStavka_App1);
        final boolean isSync = b.getBoolean("isSync");
        if (isSync) {
            fabNovaStavka.setVisibility(View.INVISIBLE);
        }

        listSpisakStavki = findViewById(R.id.listSpisakStavki_App1);





        kreirajTabeluStavki();
        ucitajStavke();

        fabNovaStavka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App1StavkeActivity.this, App1UnosStavkeActivity.class);
                intent.putExtra("idDokumenta",IdDokumenta);
                intent.putExtra("pjKmtID", pjKmtID);
                startActivityForResult(intent,1);
            }
        });

        listSpisakStavki.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final CharSequence akcije[] = new CharSequence[] {"Izbriši",  "Prikaži detalje"};
                final App1Stavke selektiranDok=(App1Stavke) adapterView.getItemAtPosition(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(App1StavkeActivity.this);
                builder.setTitle("Opcije stavki");
                builder.setItems(akcije, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        switch (which){
                            case 0:
                                //izbrisiRedakIzTabele
                                MainActivity.izbrisiRedakIzTabele(App1StavkeActivity.this,tabelaApp1,"_id", selektiranDok.getId());
                                ucitajStavke();
                                break;

                            case 1:
                                //Detalji
                                Toast.makeText(App1StavkeActivity.this,akcije[which].toString(),Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putLong("IdDokumenta", IdDokumenta);
        Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        setResult(RESULT_OK, mIntent);
        super.onBackPressed();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                App1Stavke rezultat = (App1Stavke) data.getSerializableExtra("stavka");
                MainActivity.snimiStavku(App1StavkeActivity.this, IdDokumenta, rezultat);
                /*
                SQLiteDatabase myDB = null;
                myDB = openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
                if (rezultat.isImaAtribut()){
                    Log.d(TAG, "onActivityResult: UPISUJEM ARTIKL SA ATRIBUTOM!");
                    myDB.execSQL("INSERT INTO " + tabelaApp1 + " (idDokumenta, idArtikla, nazivArtikla, kolicina, imaAtribut, idAtributa, vrijednostAtributa, nazivAtributa, " +
                            "idJmj, nazivJmj,napomena ) VALUES (" +
                            IdDokumenta + "," + rezultat.getArtiklId() + ",'" + rezultat.getArtiklNaziv() + "'," + rezultat.getKolicina() + ", '" + rezultat.isImaAtribut() + "'," + rezultat.getAtributId() + ", '" +
                            rezultat.getAtributVrijednost() + "','" + rezultat.getAtributNaziv() + "'," + rezultat.getJmjId() + ",'" + rezultat.getJmjNaziv() + "','" + rezultat.getNapomena() + "');");
                }
                else{
                    Log.d(TAG, "onActivityResult: UPISUJEM ARTIKL BEZ ATRIBUTA!");
                    myDB.execSQL("INSERT INTO " + tabelaApp1 + " (idDokumenta, idArtikla, nazivArtikla, kolicina, imaAtribut, idAtributa, vrijednostAtributa, nazivAtributa, " +
                            "idJmj, nazivJmj,napomena ) VALUES (" +
                            IdDokumenta + "," + rezultat.getArtiklId() + ",'" + rezultat.getArtiklNaziv() + "'," + rezultat.getKolicina() + ", '" + rezultat.isImaAtribut() + "',null,null" +
                              ",null," + rezultat.getJmjId() + ",'" + rezultat.getJmjNaziv() + "','" + rezultat.getNapomena() + "');");
                }
                myDB.close();
                */
                ucitajStavke();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                ucitajStavke();
            }
        }
    }

    private void kreirajTabeluStavki() {
        MainActivity.kreirajTabeluStavki(App1StavkeActivity.this);
    }

    private void ucitajStavke() {
        ListaApp1StavkeAdapter listaStavki = new ListaApp1StavkeAdapter(this, R.layout.row_app1_stavke);
        listSpisakStavki.setAdapter(listaStavki);
        List<App1Stavke> lista=MainActivity.getListaStavki(IdDokumenta,App1StavkeActivity.this);
        for (App1Stavke stavka:lista) {
            listaStavki.add(stavka);
        }
    }

}
