package com.example.marko.vips_artikli;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;


public class App1ZaglavljeActivity extends AppCompatActivity {

    private static final String TAG="App1Zaglavlje";
    TextView txtDatumDokumenta,txtKomitent,txtPjKomitenta,labelPjKomitenta,txtTipDokumenta,txtPodtipDokumenta,labelPodtipDokumenta;
    EditText etxtNapomena;
    Button btnOk,btnCancel;

    Calendar kalendar;
    int dan,mjesec,godina;

    private String izabraniDatum;

    private Komitent izabraniKomitent;


    public Komitent getIzabraniKomitent() {
        return izabraniKomitent;
    }
    public void setIzabraniKomitent(Komitent new_izabraniKomitent) {
        izabraniKomitent = new_izabraniKomitent;
        if (new_izabraniKomitent!=null){
            txtKomitent.setText(izabraniKomitent.getNaziv());
            //txtPjKomitenta.setVisibility(View.VISIBLE);
            txtPjKomitenta.setEnabled(true);
            //labelPjKomitenta.setVisibility(View.VISIBLE);
            labelPjKomitenta.setEnabled(true);
        }
        else {
            //txtPjKomitenta.setVisibility(View.INVISIBLE);
            //labelPjKomitenta.setVisibility(View.INVISIBLE);

            txtPjKomitenta.setEnabled(false);
            labelPjKomitenta.setEnabled(false);
        }
        setIzabranaPJKomitenta(null);
    }

    public PjKomitent getIzabranaPJKomitenta() {
        return izabranaPJKomitenta;
    }

    public void setIzabranaPJKomitenta(PjKomitent izabranaPJKomitenta) {
        this.izabranaPJKomitenta = izabranaPJKomitenta;
        if (izabranaPJKomitenta!=null){
            txtPjKomitenta.setText(getIzabranaPJKomitenta().getNaziv());
        }
        else {
            txtPjKomitenta.setText(getString(R.string.Izaberi));
        }
    }

    private PjKomitent izabranaPJKomitenta;


    public TipDokumenta getIzabraniTiP() {
        return izabraniTiP;
    }

    public void setIzabraniTiP(TipDokumenta izabraniTiP) {
        this.izabraniTiP = izabraniTiP;
        if(izabraniTiP!=null){
            txtTipDokumenta.setText(izabraniTiP.getNaziv());
            labelPodtipDokumenta.setEnabled(true);
            txtPodtipDokumenta.setEnabled(true);
        }else{
            //txtTipDokumenta.setText(Izaberi);
            labelPodtipDokumenta.setEnabled(false);
            txtPodtipDokumenta.setEnabled(false);
        }
        setIzabranPodtip(null);
    }

    private TipDokumenta izabraniTiP;

    public PodtipDokumenta getIzabraniPodtip() {
        return izabranPodtip;

    }

    public void setIzabranPodtip(PodtipDokumenta izabranPodtip) {
        this.izabranPodtip = izabranPodtip;
        if(izabranPodtip!=null){
            txtPodtipDokumenta.setText(izabranPodtip.getNaziv());
        }else{
            txtPodtipDokumenta.setText(getString(R.string.Izaberi));
        }
    }

    private PodtipDokumenta izabranPodtip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_zaglavlje);

        txtDatumDokumenta =(TextView)findViewById(R.id.txtDatumDokumenta_App1Zaglavlje);
        txtKomitent=(TextView)findViewById(R.id.txtKomitent_App1Zaglavlje);
        txtPjKomitenta=(TextView)findViewById(R.id.txtPjKomitenta_App1Zaglavlje);
        labelPjKomitenta=(TextView)findViewById(R.id.labelPjKomitenta_App1Zaglavlje);
        txtTipDokumenta=(TextView)findViewById(R.id.txtTipDokumenta_App1Zaglavlje);
        txtPodtipDokumenta=(TextView)findViewById(R.id.txtPodtipDokumenta_App1Zaglavlje);
        labelPodtipDokumenta=(TextView)findViewById(R.id.labelPodtipDokumenta_App1Zaglavlje);
        btnOk=(Button)findViewById(R.id.btnOK_App1ZaglavljeDokumenata);
        btnCancel=(Button)findViewById(R.id.btnCancel_App1ZaglavljeDokumenata);
        etxtNapomena=(EditText)findViewById(R.id.etxtNapomena_App1ZaglavljeDokumenta);

        setIzabraniKomitent(null);
        setIzabraniTiP(null);

        kalendar=Calendar.getInstance();

        dan=kalendar.get(Calendar.DAY_OF_MONTH);
        mjesec=kalendar.get(Calendar.MONTH);
        //mjesec=mjesec+1;
        godina=kalendar.get(Calendar.YEAR);


        izabraniDatum=MainActivity.danMjesecGodinaToFormatString(dan,mjesec,godina);
        txtDatumDokumenta.setText(izabraniDatum);

        txtDatumDokumenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(App1ZaglavljeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int godina, int mjesec, int dan) {
                        izabraniDatum=MainActivity.danMjesecGodinaToFormatString(dan,mjesec,godina);
                        txtDatumDokumenta.setText(izabraniDatum);
                    }
                },godina,mjesec,dan);
                datePickerDialog.show();
            }
        });

        txtKomitent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(App1ZaglavljeActivity.this, PretragaKomitenataActivity.class);
                i.putExtra("varijanta", "komitenti");
                Log.d(TAG, "onClick: PUT EXTRA varijanta komitenti" );
                startActivityForResult(i, 1);
            }
        });

        txtPjKomitenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(App1ZaglavljeActivity.this, PretragaKomitenataActivity.class);
                i.putExtra("varijanta", "pjkomitenti");
                Log.d(TAG, "onClick: PUT EXTRA varijanta pjkomitenti" );
                Komitent izabran=getIzabraniKomitent();
                i.putExtra("idKomitenta", izabran.getId());
                Log.d(TAG, "onClick: PUT EXTRA KomitentID= " +izabran.getId());
                startActivityForResult(i, 2);
            }
        });

        txtTipDokumenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(App1ZaglavljeActivity.this, PretragaKomitenataActivity.class);
                i.putExtra("varijanta", "tipDokumenta");
                Log.d(TAG, "onClick: PUT EXTRA varijanta TIP Dokumenta" );
                startActivityForResult(i, 3);
            }
        });

        txtPodtipDokumenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(App1ZaglavljeActivity.this, PretragaKomitenataActivity.class);
                i.putExtra("varijanta", "podtipDokumenta");
                Log.d(TAG, "onClick: PUT EXTRA varijanta podtipDokumenta" );
                TipDokumenta izabran=getIzabraniTiP();
                i.putExtra("idTipDokumenta", izabran.getId());
                Log.d(TAG, "onClick: PUT EXTRA TipDokumentaID= " +izabran.getId());
                startActivityForResult(i, 4);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("idKomitenta",getIzabraniKomitent().getId());
                returnIntent.putExtra("idPjKomitenta",getIzabranaPJKomitenta().getId());
                returnIntent.putExtra("idTipDokumenta",getIzabraniTiP().getId());
                returnIntent.putExtra("idPodtipDokumenta", getIzabraniPodtip().getId());
                returnIntent.putExtra("datumDokumenta", izabraniDatum);
                returnIntent.putExtra("nazivKomitenta",getIzabraniKomitent().getNaziv());
                returnIntent.putExtra("nazivPjKomitenta",getIzabranaPJKomitenta().getNaziv());
                returnIntent.putExtra("nazivTipDokumenta",getIzabraniTiP().getNaziv());
                returnIntent.putExtra("nazivPodtipDokumenta", getIzabraniPodtip().getNaziv());
                returnIntent.putExtra("napomena",etxtNapomena.getText().toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //String result=data.getStringExtra("Komitent");
                Long idKom=data.getLongExtra("idKomitenta",0);
                String nazivKom=data.getStringExtra("nazivKomitenta");
                String sifraKom=data.getStringExtra("sifraKomitenta");

                Log.d(TAG, "onActivityResult: vraćeno je:::> Naziv komitenta=" + nazivKom);
                Log.d(TAG, "onActivityResult: vraćeno je:::> Sifra komitenta=" + sifraKom);
                Log.d(TAG, "onActivityResult: vraćeno je:::> ID komitenta=" + idKom.toString());

                Komitent myKomitent=new Komitent(idKom,sifraKom,nazivKom);
                setIzabraniKomitent(myKomitent);


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setIzabraniKomitent(null);
            }
        }
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                Long idPjKom=data.getLongExtra("idPjKomitenta",0);
                String nazivPjKom=data.getStringExtra("nazivPjKomitenta");
                Long ridPjKom=data.getLongExtra("ridPjKomitenta",0);

                Log.d(TAG, "onActivityResult: vraćeno je:::> Naziv Pj komitenta=" + nazivPjKom);
                Log.d(TAG, "onActivityResult: vraćeno je:::> rid komitenta=" + ridPjKom);
                Log.d(TAG, "onActivityResult: vraćeno je:::> ID Pj komitenta=" + idPjKom.toString());

                PjKomitent myPjKomitent=new PjKomitent(idPjKom,nazivPjKom,ridPjKom);
                setIzabranaPJKomitenta(myPjKomitent);
                //setIzabraniKomitent(myPjKomitent);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setIzabranaPJKomitenta(null);
            }
        }
        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                Long idTip=data.getLongExtra("idTipDokumenta",0);
                String nazivTip=data.getStringExtra("nazivTipDokumenta");


                Log.d(TAG, "onActivityResult: vraćeno je:::> Naziv TIP DOKUMENTA =" + nazivTip);
                Log.d(TAG, "onActivityResult: vraćeno je:::> ID TIP DOKUMENTA =" + idTip.toString());

                TipDokumenta myTip=new TipDokumenta(idTip,nazivTip);
                setIzabraniTiP(myTip);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setIzabraniTiP(null);
            }
        }
        if (requestCode == 4) {
            if(resultCode == Activity.RESULT_OK){
                Long idPodtip=data.getLongExtra("idPodtipDokumenta",0);
                String nazivPodtipa=data.getStringExtra("nazivPodtipDokumenta");
                Long ridTipDok=data.getLongExtra("ridPodtipDokumenta",0);

                Log.d(TAG, "onActivityResult: vraćeno je:::> nazivPodtipDokumenta=" + nazivPodtipa);
                Log.d(TAG, "onActivityResult: vraćeno je:::> ridPodtipDokumenta=" + ridTipDok);
                Log.d(TAG, "onActivityResult: vraćeno je:::> idPodtipDokumenta=" + idPodtip.toString());

                PodtipDokumenta myPodtip=new PodtipDokumenta(idPodtip,nazivPodtipa,ridTipDok);
                setIzabranPodtip(myPodtip);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setIzabranPodtip(null);
            }
        }
    }//onActivityResult


}
