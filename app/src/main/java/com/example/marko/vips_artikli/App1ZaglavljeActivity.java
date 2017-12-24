package com.example.marko.vips_artikli;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class App1ZaglavljeActivity extends AppCompatActivity {

    TextView txtDatumDokumenta,txtKomitent,txtPjKomitenta;
    Calendar kalendar;
    int dan,mjesec,godina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_zaglavlje);

        txtDatumDokumenta =(TextView)findViewById(R.id.txtDatumDokumenta_App1Zaglavlje);
        txtKomitent=(TextView)findViewById(R.id.txtKomitent_App1Zaglavlje);
        txtPjKomitenta=(TextView)findViewById(R.id.txtPjKomitenta_App1Zaglavlje);

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
                startActivityForResult(i, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
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
