package com.example.marko.vips_artikli;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import javax.xml.datatype.Duration;


public class PostavkeActivity extends AppCompatActivity {

    private static String TAG = "postavke";

    long idTipDokumenta = -1;
    long idPodtipDokumenta = -1;

    Spinner spinTipDokumenta;
    Spinner spinPodtipDokumenta;
    Spinner spinnerVrstaAplikacije;
    Spinner sppinerVrstaPretrage;

    ToggleButton toggleButton;
    EditText txtKolicina;
    EditText txtBrojDecimala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavke);

        toggleButton = (ToggleButton) findViewById(R.id.tbtnBrziUnosartikala_Postavke);
        txtKolicina = (EditText) findViewById(R.id.txtDefoltnaKolicina_Postavke);
        spinTipDokumenta = (Spinner) findViewById(R.id.spinTipDokumenta_Postavke);
        spinPodtipDokumenta = (Spinner) findViewById(R.id.spinPodtipDokumenta_Postavke);
        spinnerVrstaAplikacije = (Spinner) findViewById(R.id.spinner2);
        sppinerVrstaPretrage = (Spinner) findViewById(R.id.spinner3);
        txtBrojDecimala = (EditText) findViewById(R.id.txtBrojDecimala_Postavke);

        final postavkeAplikacije myPostavke = new postavkeAplikacije(PostavkeActivity.this);

        spinnerVrstaAplikacije.setAdapter(new ArrayAdapter<VrstaAplikacije>(this, android.R.layout.simple_spinner_item, VrstaAplikacije.values()));
        sppinerVrstaPretrage.setAdapter(new ArrayAdapter<VrstaPretrageArtikala>(this, android.R.layout.simple_spinner_item, VrstaPretrageArtikala.values()));

        ArrayAdapter<TipDokumenta> tipAdapter = new ArrayAdapter<TipDokumenta>(this, android.R.layout.simple_spinner_item);
        for (TipDokumenta tipDok : MainActivity.getListaTipovaDokumenta(PostavkeActivity.this, "")) {
            tipAdapter.add(tipDok);
        }
        spinTipDokumenta.setAdapter(tipAdapter);

        spinTipDokumenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TipDokumenta tiDok = (TipDokumenta) adapterView.getItemAtPosition(i);
                idTipDokumenta = tiDok.getId();
                if (idTipDokumenta != myPostavke.getTipDokumenta()) {
                    myPostavke.snimiTipDokumenta(idTipDokumenta);
                    Log.d(TAG, "onItemSelected: SNIMI tipDokumenta=" + idTipDokumenta);
                }
                ArrayAdapter<PodtipDokumenta> podtipAdapter = new ArrayAdapter<PodtipDokumenta>(PostavkeActivity.this, android.R.layout.simple_spinner_item);
                for (PodtipDokumenta podtip : MainActivity.getListaPodtipova(PostavkeActivity.this, "", idTipDokumenta, false, " ")) {
                    podtipAdapter.add(podtip);
                }
                spinPodtipDokumenta.setAdapter(podtipAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<PodtipDokumenta> podtipAdapter = new ArrayAdapter<PodtipDokumenta>(this, android.R.layout.simple_spinner_item);
        for (PodtipDokumenta podtip : MainActivity.getListaPodtipova(PostavkeActivity.this, "", idTipDokumenta, false, " ")) {
            podtipAdapter.add(podtip);
        }
        spinPodtipDokumenta.setAdapter(podtipAdapter);


        spinPodtipDokumenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PodtipDokumenta podtipDok = (PodtipDokumenta) adapterView.getItemAtPosition(i);
                idPodtipDokumenta = podtipDok.getId();
                if (idPodtipDokumenta != myPostavke.getPodtipDokumenta()) {
                    myPostavke.snimiPodtipDokumenta(idPodtipDokumenta);
                    Log.d(TAG, "onItemSelected: SNIMI idPodtipDokumenta=" + idPodtipDokumenta);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinTipDokumenta.setSelection(getIndex_TipDokumenta(myPostavke.getTipDokumenta()));
        spinPodtipDokumenta.setSelection(getIndex_PodtipDokumenta(myPostavke.getPodtipDokumenta()));

        spinnerVrstaAplikacije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VrstaAplikacije vrsta = VrstaAplikacije.values()[position];
                int value = vrsta.ordinal();
                if (value != myPostavke.getVrstaAplikacije()) {
                    myPostavke.snimiVrstuAplikacije(value);
                    Log.d(TAG, "onItemSelected: SNIMI VrstaAplikacije=" + value);
                }
                //Toast.makeText(PostavkeActivity.this, value.toString(), Toast.LENGTH_LONG).show();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerVrstaAplikacije.setSelection(getIndex_VrstaAplikacije(myPostavke.getVrstaAplikacije()));


        sppinerVrstaPretrage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VrstaPretrageArtikala vrstaPretrageArtikala = VrstaPretrageArtikala.values()[position];
                int value = vrstaPretrageArtikala.ordinal();
                if (value != myPostavke.getVrstaPretrageArtikala()) {
                    myPostavke.snimiVrstuPretrageArtikala(value);
                    Log.d(TAG, "onItemSelected: SNIMI VrstaPretrageArtikala=" + value);
                }
                //Toast.makeText(PostavkeActivity.this, value.toString(), Toast.LENGTH_LONG).show();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtKolicina.setText(String.valueOf(myPostavke.getDefoltnaKolicina()));
        txtKolicina.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String str = txtKolicina.getText().toString();
                float defoltnaKol = 0;
                if (!str.equals("")) {
                    defoltnaKol = Float.valueOf(str);
                }
                myPostavke.snimiDefoltnuKolicinu(defoltnaKol);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        txtBrojDecimala.setText(String.valueOf(myPostavke.getBrojDecimala()));
        txtBrojDecimala.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String str = txtBrojDecimala.getText().toString();
                int defoltniBrojDecimala = 1;
                if (!str.equals("")) {
                    defoltniBrojDecimala = Integer.valueOf(str);
                }
                if (defoltniBrojDecimala < 1) {
                    defoltniBrojDecimala = 1;
                }
                myPostavke.snimiBrojDecimala(defoltniBrojDecimala);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        toggleButton.setChecked(myPostavke.isBrziUnosArtikala());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                myPostavke.snimiBrziUnos(toggleButton.isChecked());
            }
        });

    }
    //snimi postavke
    //https://stackoverflow.com/questions/8669435/storing-and-retrieving-values-from-sharedpreferences-on-activity-state-changes

    private int getIndex_TipDokumenta(long id) {
        int index = 0;
        for (int i = 0; i < spinTipDokumenta.getCount(); i++) {
            TipDokumenta tip = (TipDokumenta) spinTipDokumenta.getItemAtPosition(i);
            if (tip.getId() == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int getIndex_PodtipDokumenta(long id) {
        int index = 0;
        for (int i = 0; i < spinPodtipDokumenta.getCount(); i++) {
            PodtipDokumenta podtip = (PodtipDokumenta) spinPodtipDokumenta.getItemAtPosition(i);
            if (podtip.getId() == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int getIndex_VrstaAplikacije(int id) {
        int index = 0;
        for (int i = 0; i < spinnerVrstaAplikacije.getCount(); i++) {
            VrstaAplikacije vrstaApp = (VrstaAplikacije) spinnerVrstaAplikacije.getItemAtPosition(i);
            if (vrstaApp.ordinal() == id) {
                index = i;
                break;
            }
        }
        return index;
    }

    private int getIndex_VrstaPretrageArtikla(int id) {
        int index = 0;
        for (int i = 0; i < sppinerVrstaPretrage.getCount(); i++) {
            VrstaPretrageArtikala vrstaPretrage = (VrstaPretrageArtikala) sppinerVrstaPretrage.getItemAtPosition(i);
            if (vrstaPretrage.ordinal() == id) {
                index = i;
                break;
            }
        }
        return index;
    }

}
