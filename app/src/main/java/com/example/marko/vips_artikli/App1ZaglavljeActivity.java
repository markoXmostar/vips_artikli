package com.example.marko.vips_artikli;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class App1ZaglavljeActivity extends AppCompatActivity {

    private static final String TAG="App1Zaglavlje";
    TextView txtDatumDokumenta,txtKomitent,txtPjKomitenta,labelPjKomitenta;
    Calendar kalendar;
    int dan,mjesec,godina;



    private Komitent izabraniKomitent;

    public Komitent getIzabraniKomitent() {
        return izabraniKomitent;
    }
    public void setIzabraniKomitent(Komitent new_izabraniKomitent) {
        izabraniKomitent = new_izabraniKomitent;
        if (new_izabraniKomitent!=null){
            txtKomitent.setText(izabraniKomitent.getNaziv());
            txtPjKomitenta.setVisibility(View.VISIBLE);
            labelPjKomitenta.setVisibility(View.VISIBLE);
        }
        else {
            txtPjKomitenta.setVisibility(View.INVISIBLE);
            labelPjKomitenta.setVisibility(View.INVISIBLE);
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
            txtPjKomitenta.setText("Izaberi");
        }
    }

    private PjKomitent izabranaPJKomitenta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_zaglavlje);

        txtDatumDokumenta =(TextView)findViewById(R.id.txtDatumDokumenta_App1Zaglavlje);
        txtKomitent=(TextView)findViewById(R.id.txtKomitent_App1Zaglavlje);
        txtPjKomitenta=(TextView)findViewById(R.id.txtPjKomitenta_App1Zaglavlje);
        labelPjKomitenta=(TextView)findViewById(R.id.labelPjKomitenta_App1Zaglavlje);
        setIzabraniKomitent(null);

        kalendar=Calendar.getInstance();

        dan=kalendar.get(Calendar.DAY_OF_MONTH);
        mjesec=kalendar.get(Calendar.MONTH);
        //mjesec=mjesec+1;
        godina=kalendar.get(Calendar.YEAR);



        txtDatumDokumenta.setText(danMjesecGodinaToFormatString(dan,mjesec,godina));

        txtDatumDokumenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(App1ZaglavljeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int godina, int mjesec, int dan) {

                        txtDatumDokumenta.setText(danMjesecGodinaToFormatString(dan,mjesec,godina));
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
    }//onActivityResult

    private String danMjesecGodinaToFormatString(int dan,int mjesec,int godina){
        SimpleDateFormat dateFormat=new SimpleDateFormat(MainActivity.DatumFormat);
        //SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy");
        Calendar c=Calendar.getInstance();
        c.set(godina,mjesec,dan,0,0);
        Date datum=c.getTime();

        String datumStr=dateFormat.format(datum);
        return datumStr;
    }
}
