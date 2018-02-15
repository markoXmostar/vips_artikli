package com.example.marko.vips_artikli;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import javax.xml.datatype.Duration;


public class PostavkeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavke);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner2);
        mySpinner.setAdapter(new ArrayAdapter<VrstaAplikacije>(this, android.R.layout.simple_spinner_item, VrstaAplikacije.values()));

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VrstaAplikacije value = VrstaAplikacije.values()[position];
                Toast.makeText(PostavkeActivity.this, value.toString(), Toast.LENGTH_LONG).show();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner vrstaPretrage = (Spinner) findViewById(R.id.spinner3);
        vrstaPretrage.setAdapter(new ArrayAdapter<VrstaPretrageArtikala>(this, android.R.layout.simple_spinner_item, VrstaPretrageArtikala.values()));

        vrstaPretrage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VrstaPretrageArtikala value = VrstaPretrageArtikala.values()[position];
                Toast.makeText(PostavkeActivity.this, value.toString(), Toast.LENGTH_LONG).show();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
