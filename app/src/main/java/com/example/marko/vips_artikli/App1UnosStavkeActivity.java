package com.example.marko.vips_artikli;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class App1UnosStavkeActivity extends AppCompatActivity {
    static final String TAG = "UNOS STAVKE";
    private int varijantaPretrageArtikala;
    /*
    0-Tablica artikli omogućena tipkovnica
    1-Pretraga po barcodu, BEZ TIPKOVNICE
    2-Kamera, čitanje barcoda
     */

    TextView txtArtikl;
    TextView txtRokTrajanja;
    TextView tvRokTrajanja;
    TextView txtJmj;
    EditText txtKolicina;
    EditText txtNapomena;
    EditText txtBarcode;
    TextView tvBarcode;

    Button btnOk, btnCancel;

    private Artikl izabraniArtikl;

    List<ArtiklJmj> spisakJMJ = null;

    private void setIzabraniArtikl(Artikl _izabraniArtikl) {
        izabraniArtikl = _izabraniArtikl;
        izabraniAtribut = null;
        izabranaJMJ = null;

        if (izabraniArtikl==null){
            txtKolicina.setEnabled(false);
            txtNapomena.setEnabled(false);
            txtRokTrajanja.setEnabled(false);
            txtJmj.setEnabled(false);
            txtRokTrajanja.setVisibility(View.GONE);
            tvRokTrajanja.setVisibility(View.GONE);

        }
        else {
            txtArtikl.setText(izabraniArtikl.getNaziv());
            Log.d(TAG, "setIzabraniArtikl: greška1" + izabraniArtikl.getNaziv() + "/ ima atribut =" + izabraniArtikl.isImaRokTrajanja());
            txtKolicina.setEnabled(true);
            txtNapomena.setEnabled(true);

            if(izabraniArtikl.isImaRokTrajanja()){
                txtRokTrajanja.setEnabled(true);
                txtRokTrajanja.setVisibility(View.VISIBLE);
                tvRokTrajanja.setVisibility(View.VISIBLE);
            }
            else {
                txtRokTrajanja.setEnabled(false);
                txtRokTrajanja.setVisibility(View.GONE);
                tvRokTrajanja.setVisibility(View.GONE);
            }
            spisakJMJ = MainActivity.getListaArtiklJMJ(App1UnosStavkeActivity.this, izabraniArtikl.getId(), "");
            if (spisakJMJ.size() > 1) {
                txtJmj.setEnabled(true);
                txtJmj.setText(getString(R.string.Izaberi));
            } else {
                txtJmj.setEnabled(false);
                ArtiklJmj jmj = (ArtiklJmj) spisakJMJ.get(0);
                setIzabranaJMJ(jmj);
            }
        }
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




    public void setIzabranaJMJ(ArtiklJmj izabranaJMJ) {
        this.izabranaJMJ = izabranaJMJ;
        if (izabranaJMJ == null) {
            txtJmj.setText(getString(R.string.Izaberi));
        } else {
            txtJmj.setText(izabranaJMJ.toString());
        }
    }

    private ArtiklJmj izabranaJMJ;
    private long idDokumenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_unos_stavke);

        Bundle b=getIntent().getExtras();
        idDokumenta=b.getLong("idDokumenta");


        txtArtikl=(TextView)findViewById(R.id.txtArtikal_App1UnosStavke);
        txtRokTrajanja =(TextView)findViewById(R.id.txtRokTrajanja_App1UnosStavke);
        tvRokTrajanja = (TextView) findViewById(R.id.tvRokTrajanja_App1UnosStavke);
        txtJmj=(TextView)findViewById(R.id.txtJmj_App1UnosStavke);
        txtKolicina=(EditText)findViewById(R.id.txtKolicina_App1UnosStavke);
        txtNapomena=(EditText) findViewById(R.id.etxtNapomena_App1UnosStavke);

        txtBarcode = (EditText) findViewById(R.id.txtBarcode_App1UnosStavke);
        tvBarcode = (TextView) findViewById(R.id.tvBarcode_App1UnosStavke);


        btnOk=(Button)findViewById(R.id.btnOK_App1UnosStavke);
        btnCancel =(Button)findViewById(R.id.btnCancel_App1UnosStavke);


        varijantaPretrageArtikala=0;
        setIzabraniArtikl(null);

        txtBarcode.setOnKeyListener(new View.OnKeyListener() {
            String barcodeText = "";

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                //txtBarcode.setText("");
                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {


                    Artikl artikl = MainActivity.getArtiklByBarcode(App1UnosStavkeActivity.this, barcodeText);
                    if (artikl != null) {
                        setIzabraniArtikl(artikl);
                        txtBarcode.setText(barcodeText);


                    } else {
                        setIzabraniArtikl(null);
                        txtBarcode.setText(barcodeText);
                        txtBarcode.requestFocus();
                        txtBarcode.selectAll();
                    }


                    barcodeText = "";
                    return false; //ovaj false znači da se enter ne pr ocesira dalje
                }
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    char c = (char) keyEvent.getUnicodeChar();
                    if (Character.isDigit(c)) {
                        barcodeText += c;
                        Log.d(TAG, "onKey: key pressed =" + c);
                        Log.d(TAG, "onKey: barcode=" + barcodeText);
                    }
                }
                return false;
            }
        });

        txtArtikl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (varijantaPretrageArtikala){
                    case 0:
                        Intent intent = new Intent(App1UnosStavkeActivity.this, ArtikliActivity.class);
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

                final List<ArtiklAtributStanje> spisak = MainActivity.getListaAtributa(App1UnosStavkeActivity.this, izabraniArtikl.getId(), "");
                Log.d(TAG, "onClick: Atributi učitani. broj im je =" + spisak.size());
                CharSequence[] items = new CharSequence[spisak.size()];
                for (int i = 0; i < spisak.size(); i++) {
                    items[i] = spisak.get(i).toString();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(App1UnosStavkeActivity.this);
                builder.setTitle("Izaberi atribut");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArtiklAtributStanje myAtribut = spisak.get(which);
                        setIzabraniAtribut(myAtribut);
                        //Toast.makeText(App1StavkeActivity.this,izabranAtribut.getVrijednostNaziv1(),Toast.LENGTH_LONG).show();
                        }
                });
                builder.show();
            }
        });

        txtJmj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<ArtiklJmj> spisak = spisakJMJ;
                CharSequence[] items = new CharSequence[spisak.size()];
                for (int i = 0; i < spisak.size(); i++) {
                    items[i] = spisak.get(i).toString();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(App1UnosStavkeActivity.this);
                builder.setTitle("Izaberi JMJ");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArtiklJmj myAtribut = spisak.get(which);
                        setIzabranaJMJ(myAtribut);

                    }
                });
                builder.show();
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                App1Stavke newStavka=null;
                Log.d(TAG, "onClick: PRITISNUTO OK DUGME!!!!!!");
                if(izabraniArtikl==null) {
                    ispisiPorukuNisteUnijeliPotrebnePodatke(view);
                    return;
                } else {
                    if (izabraniArtikl.isImaRokTrajanja()) {
                        if (izabraniAtribut == null) {
                            ispisiPorukuNisteUnijeliPotrebnePodatke(view);
                            return;
                        }
                    }
                }
                if (izabranaJMJ == null) {
                    ispisiPorukuNisteUnijeliPotrebnePodatke(view);
                    return;
                }
                double myKolicina=0;
                String text =txtKolicina.getText().toString();
                if(!text.isEmpty()){
                    try
                    {
                        myKolicina= Double.parseDouble(text);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Snackbar.make(view, e1.getMessage()  , Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }
                }
                Log.d(TAG, "onClick: myKolicina=" + myKolicina);
                if(myKolicina==0){
                    String poruka= getResources().getString(R.string.NedozvoljenaNula);
                    Snackbar.make(view, poruka  , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }


                if (izabraniArtikl.isImaRokTrajanja()){
                    Log.d(TAG, "onClick: IMA ROK TRAJANJA TRUE");
                    newStavka = new App1Stavke(-1, idDokumenta,
                            izabraniArtikl.getId(),
                            izabraniArtikl.getNaziv(),
                            izabranaJMJ.getJmjID(),
                            izabranaJMJ.getNazivJMJ(),
                            izabraniArtikl.isImaRokTrajanja(),
                            izabraniAtribut.getAtributId1(),
                            izabraniAtribut.getAtributNaziv1(),
                            izabraniAtribut.getAtributVrijednost1(),
                            myKolicina,
                            txtNapomena.getText().toString());
                }else{
                    Log.d(TAG, "onClick: IMA ROK TRAJANJA FALSE");
                    newStavka = new App1Stavke(-1, idDokumenta,
                            izabraniArtikl.getId(),
                            izabraniArtikl.getNaziv(),
                            izabranaJMJ.getJmjID(),
                            izabranaJMJ.getNazivJMJ(),
                            izabraniArtikl.isImaRokTrajanja(),
                            -1,
                            null,
                            null,
                            myKolicina,
                            txtNapomena.getText().toString());
                }


                returnIntent.putExtra("stavka",newStavka);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });
    }


    private void ispisiPorukuNisteUnijeliPotrebnePodatke(View view) {

        String poruka = getResources().getString(R.string.NepotpunUnos);
        Snackbar.make(view, poruka, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
