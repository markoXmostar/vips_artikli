package com.example.marko.vips_artikli.glavne_aktivnosti;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marko.vips_artikli.TipDokumentaFilter;
import com.example.marko.vips_artikli.dataclass.Komitent;
import com.example.marko.vips_artikli.R;
import com.example.marko.vips_artikli.dataclass.PjKomitent;

import java.util.Calendar;
import java.util.Date;

public class filterPretragaDokumenataActivity extends AppCompatActivity {

    private long filterKomitentID,filterPjKomitentaID;
    private int filterZakljucen;
    private String filterKomitentNaziv,filterPjKOmitentaNaziv;
    private String filterDatumOd;
    private String filterDatumDo;

    private String izabraniDatumOd,izabraniDatumDo;
    int dan1,mjesec1,godina1;
    int dan2,mjesec2,godina2;

    private TextView txtDatumOd,txtDatumDo,txtKomitent,txtPjKomitenta;
    private Button btnOk,btnCencel;
    private Spinner spinnerVrstaAplikacije;

    private Komitent izabraniKomitent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_pretraga_dokumenata);

        txtDatumOd=(TextView)findViewById(R.id.txtDatumOd_filterDokumenata);
        txtDatumDo=(TextView)findViewById(R.id.txtDatumDo_filterDokumenata);
        txtKomitent=(TextView)findViewById(R.id.txtKomitent_filterDokumenata);
        txtPjKomitenta=(TextView)findViewById(R.id.txtPjKomitenta_filterDokumenata);


        spinnerVrstaAplikacije = (Spinner) findViewById(R.id.spinTipDokumenta_filterDokumenata);

        spinnerVrstaAplikacije.setAdapter(new ArrayAdapter<TipDokumentaFilter>(this, R.layout.moj_spinner_za_izbor, TipDokumentaFilter.values()));




        btnOk=(Button)findViewById(R.id.btnOK_filterDokumenti);
        btnCencel=(Button)findViewById(R.id.btnCancel_filterDokumenti);

        Bundle b = getIntent().getExtras();
        filterKomitentID = b.getLong("komitentID", 0);
        filterKomitentNaziv=b.getString("komitentNaziv","");

        filterPjKomitentaID=b.getLong("filterPjKomitentID",0);
        filterPjKOmitentaNaziv=b.getString("filterPjKomitentNaziv","");
        filterZakljucen=b.getInt("filterZakljucen",0);
        spinnerVrstaAplikacije.setSelection(filterZakljucen);


        filterDatumOd=b.getString("datumOd","");
        filterDatumDo=b.getString("datumDo","");

        Date datumOd=MainActivity.getDateFromSQLLiteDBFormat(filterDatumOd);
        izabraniDatumOd=MainActivity.parseDateFromSQLLiteDBFormatToMyOnlyDateFormat(datumOd);
        txtDatumOd.setText(izabraniDatumOd);

        Calendar c1 = Calendar.getInstance();
        c1.setTime(datumOd);
        dan1 = c1.get(Calendar.DAY_OF_MONTH);
        mjesec1=c1.get(Calendar.MONTH);
        godina1=c1.get(Calendar.YEAR);

        Date datumDo=MainActivity.getDateFromSQLLiteDBFormat(filterDatumDo);
        izabraniDatumDo=MainActivity.parseDateFromSQLLiteDBFormatToMyOnlyDateFormat(datumDo);
        txtDatumDo.setText(izabraniDatumDo);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(datumDo);
        dan2 = c2.get(Calendar.DAY_OF_MONTH);
        mjesec2=c2.get(Calendar.MONTH);
        godina2=c2.get(Calendar.YEAR);

        txtDatumOd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(filterPretragaDokumenataActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int godina1, int mjesec1, int dan1) {
                        izabraniDatumOd=MainActivity.danMjesecGodinaToFormatString(dan1,mjesec1,godina1);
                        txtDatumOd.setText(izabraniDatumOd);
                    }
                },godina1,mjesec1,dan1);
                datePickerDialog.show();
            }
        });

        txtDatumDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(filterPretragaDokumenataActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int godina2, int mjesec2, int dan2) {
                        izabraniDatumDo=MainActivity.danMjesecGodinaToFormatString(dan2,mjesec2,godina2);
                        txtDatumDo.setText(izabraniDatumDo);
                    }
                },godina2,mjesec2,dan2);
                datePickerDialog.show();
            }
        });


        if (TextUtils.isEmpty(filterKomitentNaziv)){
            txtKomitent.setText("Izaberi komitenta");
            setIzabraniKomitent(null);
        }else{
            txtKomitent.setText(filterKomitentNaziv);
            Komitent myKom=MainActivity.getKomitentByID(filterPretragaDokumenataActivity.this,filterKomitentID);
            setIzabraniKomitent(myKom);
        }

        if (filterPjKomitentaID!=0){
            PjKomitent myPjKom=MainActivity.getPjKomitentByID(filterPretragaDokumenataActivity.this,filterPjKomitentaID);
            setIzabranaPJKomitenta(myPjKom);
        }

        if (filterPjKomitentaID!=0){
            PjKomitent myPjKom=MainActivity.getPjKomitentByID(filterPretragaDokumenataActivity.this,filterPjKomitentaID);
            setIzabranaPJKomitenta(myPjKom);
        }

        txtKomitent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(filterPretragaDokumenataActivity.this, PretragaKomitenata_Tipova_Podtipova_PJKomitenata_Activity.class);
                i.putExtra("varijanta", "komitenti");
                startActivityForResult(i, 1);
            }
        });

        txtPjKomitenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(filterPretragaDokumenataActivity.this, PretragaKomitenata_Tipova_Podtipova_PJKomitenata_Activity.class);
                i.putExtra("varijanta", "pjkomitenti");
                Komitent izabran=izabraniKomitent;
                i.putExtra("idKomitenta", izabran.getId());
                startActivityForResult(i, 2);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                filterDatumOd=txtDatumOd.getText().toString();
                filterDatumDo=txtDatumDo.getText().toString();
                filterZakljucen=spinnerVrstaAplikacije.getSelectedItemPosition();

                returnIntent.putExtra("filterDatumOd",filterDatumOd);
                returnIntent.putExtra("filterDatumDo",filterDatumDo);
                returnIntent.putExtra("filterKomitentID",filterKomitentID);
                returnIntent.putExtra("filterKomitentNaziv",filterKomitentNaziv);
                returnIntent.putExtra("filterPjKomitentaID",filterPjKomitentaID);
                returnIntent.putExtra("filterPjKOmitentaNaziv",filterPjKOmitentaNaziv);
                returnIntent.putExtra("filterZakjucen",filterZakljucen);

                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });


        btnCencel.setOnClickListener(new View.OnClickListener() {
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

                Komitent myKomitent = (Komitent) data.getSerializableExtra("komitent");
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

                PjKomitent myPjKomitent=new PjKomitent(idPjKom,nazivPjKom,ridPjKom);
                setIzabranaPJKomitenta(myPjKomitent);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setIzabranaPJKomitenta(null);
            }
        }
    }

    private void setIzabranaPJKomitenta(PjKomitent izabranaPJKomitenta){
        if(izabranaPJKomitenta!=null){
            filterPjKOmitentaNaziv=izabranaPJKomitenta.getNaziv();
            filterPjKomitentaID=izabranaPJKomitenta.getId();
            txtPjKomitenta.setText(filterPjKOmitentaNaziv);
        }else {
            filterPjKOmitentaNaziv="";
            filterPjKomitentaID=0L;
            txtPjKomitenta.setText("Izaberi PJ");
        }

    }

    private void setIzabraniKomitent(Komitent new_izabraniKomitent) {
        izabraniKomitent=new_izabraniKomitent;
        if (new_izabraniKomitent!=null) {
            txtKomitent.setText(new_izabraniKomitent.getNaziv());
            filterKomitentID=new_izabraniKomitent.getId();
            filterKomitentNaziv=new_izabraniKomitent.getNaziv();
            txtPjKomitenta.setEnabled(true);
            txtPjKomitenta.setText("Izaberi PJ");


        }else{
            txtKomitent.setText("Izaberi komitenta");
            filterKomitentID=0L;
            filterKomitentNaziv="";


            filterPjKomitentaID=0L;
            filterPjKOmitentaNaziv="";
            txtPjKomitenta.setEnabled(false);
            txtPjKomitenta.setText("N/A");


        }
    }


}
