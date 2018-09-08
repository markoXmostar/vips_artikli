package com.example.marko.vips_artikli;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.vision.barcode.*;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class App1UnosStavkeActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    static final String TAG = "UNOS STAVKE";
    private int varijantaPretrageArtikala;
    /*
    0-Tablica artikli omogućena tipkovnica
    1-Pretraga po barcodu, BEZ TIPKOVNICE
    2-Kamera, čitanje barcoda
     */
    private BarcodeReader barcodeReader;

    TextView txtArtikl;
    TextView txtRokTrajanja;
    TextView tvRokTrajanja;
    TextView txtJmj;
    EditText txtKolicina;
    //EditText txtNapomena;
    myEditTextNoKeyboard txtBarcode;
    TextView tvBarcode;

    Button btnOk, btnCancel;
    ToggleButton tbtnAsortimanKupca;

    private Artikl izabraniArtikl;

    List<ArtiklJmj> spisakJMJ = null;


    private void setIzabraniArtikl(Artikl _izabraniArtikl) {
        izabraniArtikl = _izabraniArtikl;
        izabraniAtribut = null;
        izabranaJMJ = null;

        if (izabraniArtikl==null){
            txtKolicina.setEnabled(false);
            //txtNapomena.setEnabled(false);
            txtRokTrajanja.setEnabled(false);
            txtJmj.setEnabled(false);
            txtRokTrajanja.setVisibility(View.GONE);
            tvRokTrajanja.setVisibility(View.GONE);

        }
        else {
            txtArtikl.setText(izabraniArtikl.getNaziv());
            Log.d(TAG, "setIzabraniArtikl: greška1" + izabraniArtikl.getNaziv() + "/ ima atribut =" + izabraniArtikl.isImaRokTrajanja());
            txtKolicina.setEnabled(true);
            //txtNapomena.setEnabled(true);

            if(izabraniArtikl.isImaRokTrajanja()){
                txtRokTrajanja.setEnabled(true);
                txtRokTrajanja.setVisibility(View.VISIBLE);
                tvRokTrajanja.setVisibility(View.VISIBLE);
                MainActivity.svirajUpozorenje(myPostavke);
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
                MainActivity.svirajUpozorenje(myPostavke);
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
    private long pjKmtID;

    private int unosStavke = 0;

    private postavkeAplikacije myPostavke;

    private void postaviZadanuKolicinu() {
        if (myPostavke.getDefoltnaKolicina() > 0) {
            txtKolicina.setText(Float.toString(myPostavke.getDefoltnaKolicina()));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_unos_stavke);

        Bundle b=getIntent().getExtras();
        idDokumenta=b.getLong("idDokumenta");
        unosStavke = b.getInt("tipStavke", 0);
        pjKmtID = b.getLong("pjKmtID");

        myPostavke = new postavkeAplikacije(App1UnosStavkeActivity.this);

        txtArtikl=(TextView)findViewById(R.id.txtArtikal_App1UnosStavke);
        txtRokTrajanja =(TextView)findViewById(R.id.txtRokTrajanja_App1UnosStavke);
        tvRokTrajanja = (TextView) findViewById(R.id.tvRokTrajanja_App1UnosStavke);
        txtJmj=(TextView)findViewById(R.id.txtJmj_App1UnosStavke);
        txtKolicina=(EditText)findViewById(R.id.txtKolicina_App1UnosStavke);
        postaviZadanuKolicinu();
        //txtNapomena=(EditText) findViewById(R.id.etxtNapomena_App1UnosStavke);
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);

        txtBarcode = (myEditTextNoKeyboard) findViewById(R.id.txtBarcode_App1UnosStavke);

        tvBarcode = (TextView) findViewById(R.id.tvBarcode_App1UnosStavke);


        tbtnAsortimanKupca = (ToggleButton) findViewById(R.id.btnAsortimanKupca_App1UnosStavke);
        int brojArtikalaInAsortiman = MainActivity.getBrojArtikalaInAsortimanKupca(App1UnosStavkeActivity.this, pjKmtID);

        String off = tbtnAsortimanKupca.getTextOff().toString();
        String on = tbtnAsortimanKupca.getTextOn().toString();
        off = "Asortiman kupca (" + String.valueOf(brojArtikalaInAsortiman) + ") " + off;
        on = "Asortiman kupca (" + String.valueOf(brojArtikalaInAsortiman) + ") " + on;
        tbtnAsortimanKupca.setTextOff(off);
        tbtnAsortimanKupca.setTextOn(on);
        tbtnAsortimanKupca.toggle();

        btnOk=(Button)findViewById(R.id.btnOK_App1UnosStavke);
        btnCancel =(Button)findViewById(R.id.btnCancel_App1UnosStavke);

        if (unosStavke == 0) {
            //za aplikaciju 1
            varijantaPretrageArtikala = myPostavke.getVrstaPretrageArtikala();
        } else {
            // za aplikaciju 2 ne treba kamera ni bar codereader;
            varijantaPretrageArtikala = 0;
        }

        switch (varijantaPretrageArtikala) {
            case 0:
                //pretraga normalna
                txtBarcode.setVisibility(View.GONE);
                tvBarcode.setVisibility(View.GONE);
                barcodeReader.getView().setVisibility(View.INVISIBLE);
                txtArtikl.setEnabled(true);
                break;
            case 1:
                //pretraga po BARCODU
                txtBarcode.setVisibility(View.VISIBLE);
                tvBarcode.setVisibility(View.VISIBLE);
                txtArtikl.setEnabled(false);
                barcodeReader.getView().setVisibility(View.INVISIBLE);
                break;
            case 2:
                //ZA KAMERU NAMIJENJENO
                barcodeReader.getView().setVisibility(View.VISIBLE);
                txtBarcode.setVisibility(View.GONE);
                tvBarcode.setVisibility(View.GONE);
                txtArtikl.setEnabled(false);
                //Toast.makeText(App1UnosStavkeActivity.this, "NIJE IMPLEMENTIRANO!", Toast.LENGTH_LONG).show();
                break;
        }
        setIzabraniArtikl(null);

        txtBarcode.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if ((keyCode == KeyEvent.KEYCODE_ENTER) && (keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
                    String barcodeStr = txtBarcode.getText().toString();
                    Log.d(TAG, "onKey: BARCODE=" + barcodeStr);
                    Artikl myArt = MainActivity.getArtiklByBarcode(App1UnosStavkeActivity.this, barcodeStr, tbtnAsortimanKupca.isChecked(), pjKmtID);
                    if (myArt != null) {
                        Log.d(TAG, "onKey: ARTIKL NAĐEN!!");
                        txtBarcode.setOkBackgroundColor();
                        setIzabraniArtikl(myArt);
                        txtKolicina.setText("1.00");
                        txtKolicina.requestFocus();


                    } else {
                        Log.d(TAG, "onKey: ARTIKL NIJE NAĐEN!!");
                        MainActivity.svirajUpozorenje(myPostavke);

                        txtBarcode.setAlertBackgroundColor();

                        //if (!barcodeStr.equals("")){
                        txtBarcode.selectAll();
                        //}
                    }
                    return true;
                }

                char c = (char) keyEvent.getUnicodeChar();
                if (Character.isDigit(c)) {
                    Log.d(TAG, "onKey: key pressed =" + c);
                    return false;
                } else {
                    txtBarcode.selectAll();
                    return true;
                }

            }
        });


        txtArtikl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (varijantaPretrageArtikala){
                    case 0:

                        Intent intent = new Intent(App1UnosStavkeActivity.this, ArtikliActivity.class);
                        intent.putExtra("varijanta", 0);
                        intent.putExtra("asortimanKupca", tbtnAsortimanKupca.isChecked());
                        intent.putExtra("pjKmtID", pjKmtID);
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
                            izabraniArtikl.getVpc(),
                            izabraniArtikl.getMpc(),
                            //txtNapomena.getText().toString());
                            "");
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
                            izabraniArtikl.getVpc(),
                            izabraniArtikl.getMpc(),
                            //txtNapomena.getText().toString());
                            "");
                }
                if (unosStavke == 0) {
                    //za aplikaciju 1
                    if (myPostavke.isBrziUnosArtikala()) {
                        MainActivity.snimiStavku(App1UnosStavkeActivity.this, idDokumenta, newStavka);

                        MainActivity.svirajOK(myPostavke);
                        recreate();
                        postaviZadanuKolicinu();
                        txtBarcode.setText("");
                        txtBarcode.requestFocus();

                    } else {
                        returnIntent.putExtra("stavka", newStavka);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                } else {
                    //za aplikaciju 2 NEMA BRZOG UNOSA!!
                    returnIntent.putExtra("stavka", newStavka);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }


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


    /*
        private void svirajUpozorenje() {
            if(myPostavke.isSvirajUpozorenja()){
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 90);
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);
            }

        }

        private void svirajOK() {
            if(myPostavke.isSvirajUpozorenja()) {
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 90);
                toneG.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 500);
            }
        }

    */
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

    @Override
    public void onScanned(final Barcode barcode) {
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();
        barcodeReader.pauseScanning();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();
            }
        });

        String barcodeStr = barcode.displayValue;
        Log.d(TAG, "onKey: BARCODE=" + barcodeStr);
        Artikl myArt = MainActivity.getArtiklByBarcode(App1UnosStavkeActivity.this, barcodeStr, tbtnAsortimanKupca.isChecked(), pjKmtID);
        if (myArt != null) {
            Log.d(TAG, "onKey: ARTIKL NAĐEN!!");
            setIzabraniArtikl(myArt);
            txtKolicina.setText("1.00");
            txtKolicina.requestFocus();


        } else {
            Log.d(TAG, "onKey: ARTIKL NIJE NAĐEN!!");
            MainActivity.svirajUpozorenje(myPostavke);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GREŠKA!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Whatever...
                    barcodeReader.resumeScanning();
                }
            }).show();
            //}
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
}
