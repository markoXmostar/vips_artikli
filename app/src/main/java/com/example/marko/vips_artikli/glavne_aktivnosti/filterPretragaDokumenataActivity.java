package com.example.marko.vips_artikli.glavne_aktivnosti;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.marko.vips_artikli.dataclass.Komitent;
import com.example.marko.vips_artikli.R;

import java.util.Calendar;
import java.util.Date;

public class filterPretragaDokumenataActivity extends AppCompatActivity {

    private long filterKomitentID;
    private String filterKomitentNaziv;
    private String filterDatumOd;
    private String filterDatumDo;

    private String izabraniDatumOd,izabraniDatumDo;
    int dan1,mjesec1,godina1;
    int dan2,mjesec2,godina2;

    private TextView txtDatumOd,txtDatumDo,txtKomitent;
    private Button btnOk,btnCencel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_pretraga_dokumenata);

        txtDatumOd=(TextView)findViewById(R.id.txtDatumOd_filterDokumenata);
        txtDatumDo=(TextView)findViewById(R.id.txtDatumDo_filterDokumenata);
        txtKomitent=(TextView)findViewById(R.id.txtKomitent_filterDokumenata);

        btnOk=(Button)findViewById(R.id.btnOK_filterDokumenti);
        btnCencel=(Button)findViewById(R.id.btnCancel_filterDokumenti);

        Bundle b = getIntent().getExtras();
        filterKomitentID = b.getLong("komitentID", 0);
        filterKomitentNaziv=b.getString("komitentNaziv","");
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
        }else{
            txtKomitent.setText(filterKomitentNaziv);
        }
        txtKomitent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(filterPretragaDokumenataActivity.this, PretragaKomitenata_Tipova_Podtipova_PJKomitenata_Activity.class);
                i.putExtra("varijanta", "komitenti");
                startActivityForResult(i, 1);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                filterDatumOd=txtDatumOd.getText().toString();
                filterDatumDo=txtDatumDo.getText().toString();

                returnIntent.putExtra("filterDatumOd",filterDatumOd);
                returnIntent.putExtra("filterDatumDo",filterDatumDo);
                returnIntent.putExtra("filterKomitentID",filterKomitentID);
                returnIntent.putExtra("filterKomitentNaziv",filterKomitentNaziv);
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
    }

    private void setIzabraniKomitent(Komitent new_izabraniKomitent) {
        if (new_izabraniKomitent!=null) {
            txtKomitent.setText(new_izabraniKomitent.getNaziv());
            filterKomitentID=new_izabraniKomitent.getId();
            filterKomitentNaziv=new_izabraniKomitent.getNaziv();
        }else{
            txtKomitent.setText("");
            filterKomitentID=0L;
            filterKomitentNaziv="";
        }
    }


}
