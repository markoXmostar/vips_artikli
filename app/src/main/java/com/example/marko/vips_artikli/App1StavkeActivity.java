package com.example.marko.vips_artikli;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class App1StavkeActivity extends AppCompatActivity {
    static final String TAG = "UNOS STAVKE";
    private int varijantaPretrageArtikala;
    /*
    0-Tablica artikli omogućena tipkovnica
    1-Pretraga po barcodu, BEZ TIPKOVNICE
    2-Kamera, čitanje barcoda
     */

    TextView txtArtikl;
    TextView txtRokTrajanja;
    TextView txtJmj;
    EditText txtKolicina;
    EditText txtNapomena;

    private Artikl izabraniArtikl;

    private Artikl getIzabraniArtikl() {
        return izabraniArtikl;
    }

    private void setIzabraniArtikl(Artikl _izabraniArtikl) {
        izabraniArtikl = _izabraniArtikl;

        if (izabraniArtikl==null){
            txtKolicina.setEnabled(false);
            txtNapomena.setEnabled(false);
            txtRokTrajanja.setEnabled(false);
            txtJmj.setEnabled(false);
            Log.d(TAG, "setIzabraniArtikl: greška1 = NULL" );
        }
        else {
            txtArtikl.setText(izabraniArtikl.getNaziv());
            Log.d(TAG, "setIzabraniArtikl: greška1" + izabraniArtikl.getNaziv() + "/ ima atrinut =" + izabraniArtikl.isImaRokTrajanja());
            txtKolicina.setEnabled(true);
            txtNapomena.setEnabled(true);
            txtJmj.setEnabled(true);
            if(izabraniArtikl.isImaRokTrajanja()){
                txtRokTrajanja.setEnabled(true);

            }
            else {
                txtRokTrajanja.setEnabled(false);
            }

        }
    }

    public ArtiklAtributStanje getIzabraniAtribut() {
        return izabraniAtribut;
    }

    public void setIzabraniAtribut(ArtiklAtributStanje izabraniAtribut) {
        this.izabraniAtribut = izabraniAtribut;
        if (izabraniAtribut==null){
            txtRokTrajanja.setText(getString(R.string.Izaberi));
        }
        else{
            txtRokTrajanja.setText(izabraniAtribut.toString());
        }
    }

    private ArtiklAtributStanje izabraniAtribut;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_stavke);

        txtArtikl=(TextView)findViewById(R.id.txtArtikal_App1UnosStavke);
        txtRokTrajanja =(TextView)findViewById(R.id.txtRokTrajanja_App1UnosStavke);
        txtJmj=(TextView)findViewById(R.id.txtJmj_App1UnosStavke);
        txtKolicina=(EditText)findViewById(R.id.txtKolicina_App1UnosStavke);
        txtNapomena=(EditText) findViewById(R.id.etxtNapomena_App1UnosStavke);
        varijantaPretrageArtikala=0;
        setIzabraniArtikl(null);

        txtArtikl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (varijantaPretrageArtikala){
                    case 0:
                        Intent intent=new Intent(App1StavkeActivity.this,ArtikliActivity.class);
                        intent.putExtra("varijanta", 0);
                        startActivityForResult(intent,1);
                        break;
                    case 1:

                        break;
                    case 2:

                        break;


                }

            }
        });

        txtRokTrajanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: počinjem učitavanje atributa");
                final List<ArtiklAtributStanje> spisakAtributa=MainActivity.getListaAtributa(App1StavkeActivity.this,izabraniArtikl.getId());
                Log.d(TAG, "onClick: Atributi učitani. broj im je =" +spisakAtributa.size());
                CharSequence[] items = new CharSequence[spisakAtributa.size()];
                for (int i = 0; i < spisakAtributa.size(); i++) {
                    items[i] = spisakAtributa.get(i).toString();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(App1StavkeActivity.this);
                builder.setTitle("Izaberi atribut");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArtiklAtributStanje myAtribut= spisakAtributa.get(which);
                        setIzabraniAtribut(myAtribut);
                        //Toast.makeText(App1StavkeActivity.this,izabranAtribut.getVrijednost1(),Toast.LENGTH_LONG).show();
                        }
                });
                builder.show();
            }
        });
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Artikl myArtikl = (Artikl) data.getSerializableExtra("artikl");
                setIzabraniArtikl(myArtikl);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
}
