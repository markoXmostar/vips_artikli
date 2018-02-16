package com.example.marko.vips_artikli;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import javax.xml.datatype.Duration;


public class PostavkeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavke);

        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.tbtnBrziUnosartikala_Postavke);
        EditText txtKolicina = (EditText) findViewById(R.id.txtDefoltnaKolicina_Postavke);
        Spinner spinTipDokumenta = (Spinner) findViewById(R.id.spinTipDokumenta_Postavke);
        Spinner spinPodtipDokumenta = (Spinner) findViewById(R.id.spinPodtipDokumenta_Postavke);
        Spinner spinnerVrstaAplikacije = (Spinner) findViewById(R.id.spinner2);
        Spinner sppinerVrstaPretrage = (Spinner) findViewById(R.id.spinner3);

        spinnerVrstaAplikacije.setAdapter(new ArrayAdapter<VrstaAplikacije>(this, android.R.layout.simple_spinner_item, VrstaAplikacije.values()));
        sppinerVrstaPretrage.setAdapter(new ArrayAdapter<VrstaPretrageArtikala>(this, android.R.layout.simple_spinner_item, VrstaPretrageArtikala.values()));
        ArrayAdapter<TipDokumenta> tipAdapter = new ArrayAdapter<TipDokumenta>(this, android.R.layout.simple_spinner_item);
        for (TipDokumenta tipDok : MainActivity.getListaTipovaDokumenta(PostavkeActivity.this, "")) {
            tipAdapter.add(tipDok);
        }
        ArrayAdapter<PodtipDokumenta> podtipAdapter = new ArrayAdapter<PodtipDokumenta>(this, android.R.layout.simple_spinner_item);
        for (PodtipDokumenta podtip : MainActivity.getListaPodtipova(PostavkeActivity.this, "")) {
            podtipAdapter.add(podtip);
        }
        spinTipDokumenta.setAdapter(tipAdapter);
        spinPodtipDokumenta.setAdapter(podtipAdapter);

        spinnerVrstaAplikacije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VrstaAplikacije value = VrstaAplikacije.values()[position];
                Toast.makeText(PostavkeActivity.this, value.toString(), Toast.LENGTH_LONG).show();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sppinerVrstaPretrage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VrstaPretrageArtikala value = VrstaPretrageArtikala.values()[position];
                Toast.makeText(PostavkeActivity.this, value.toString(), Toast.LENGTH_LONG).show();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        postavkeAplikacije postavke = new postavkeAplikacije(PostavkeActivity.this);
        spinnerVrstaAplikacije.setSelection(postavke.getVrstaAplikacije());
        sppinerVrstaPretrage.setSelection(postavke.getVrstaPretrageArtikala());
        txtKolicina.setText(String.valueOf(postavke.getDefoltnaKolicina()));


    }
    //snimi postavke
    //https://stackoverflow.com/questions/8669435/storing-and-retrieving-values-from-sharedpreferences-on-activity-state-changes

}
