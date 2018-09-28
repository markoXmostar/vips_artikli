package com.vanima.mvips.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.vanima.mvips.models.PodtipDokumenta;
import com.vanima.mvips.R;
import com.vanima.mvips.models.TipDokumenta;
import com.vanima.mvips.models.VrstaAplikacije;
import com.vanima.mvips.models.VrstaPretrageArtikala;
import com.vanima.mvips.utils.postavkeAplikacije;

import java.util.List;


public class PostavkeActivity extends AppCompatActivity {

    private static String TAG = "postavke";

    long idTipDokumenta = -1;
    long idPodtipDokumenta = -1;


    postavkeAplikacije _myPostavke = null;

    Spinner spinTipDokumenta;
    Spinner spinPodtipDokumenta;
    Spinner spinnerVrstaAplikacije;
    Spinner sppinerVrstaPretrage;

    private boolean spinnerTouched = false;

    Switch btnBrziUnosArtikla, btnDopustenaIzmjenaTipaDokumenta, btnSvirajUpozorenja;

    SeekBar sbarKolicina;
    SeekBar sbarBrojDecimala;

    TextView lblDefKolicina;
    TextView lblBrojDec;

    EditText pinText;

    static int progressKolicina;

    public void setDefKolicina(float defKolicina) {
        this.defKolicina = defKolicina;
        lblDefKolicina.setText(String.valueOf(defKolicina));
    }

    float defKolicina;

    int progresBrojDec;
    int defBrojDec;

    public void setDefBrojDec(int defBrojDec) {
        this.defBrojDec = defBrojDec;
        lblBrojDec.setText(String.valueOf(defBrojDec));
    }

    private static boolean radi = false;
    private static boolean omoguciPromjene = false;

    /*
        @Override
        protected void onStop(){
            Log.d(TAG, "onStop: XXXXX");
            super.onStop();
            Intent returnIntent= new Intent();
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postavke);

        radi = false;

        btnBrziUnosArtikla = (Switch) findViewById(R.id.tbtnBrziUnosartikala_Postavke);
        btnDopustenaIzmjenaTipaDokumenta = (Switch) findViewById(R.id.btnDopustenaIZmjenaTipa_Postavke);
        sbarKolicina = (SeekBar) findViewById(R.id.sbarDefoltnaKolicina_Postavke);
        spinTipDokumenta = (Spinner) findViewById(R.id.spinTipDokumenta_Postavke);
        spinPodtipDokumenta = (Spinner) findViewById(R.id.spinPodtipDokumenta_Postavke);
        spinnerVrstaAplikacije = (Spinner) findViewById(R.id.spinner2);
        sppinerVrstaPretrage = (Spinner) findViewById(R.id.spinner3);
        sbarBrojDecimala = (SeekBar) findViewById(R.id.sbarBrojDecimala_Postavke);
        lblDefKolicina = (TextView) findViewById(R.id.lblDefoltnaKolicina_Posavke);
        lblBrojDec = (TextView) findViewById(R.id.lblBrojDecimala_Postavke);
        pinText = (EditText) findViewById(R.id.pinText);
        btnSvirajUpozorenja = (Switch) findViewById(R.id.btnZvukoviObavijesti_Postavke);

        final postavkeAplikacije myPostavke = new postavkeAplikacije(PostavkeActivity.this);
        _myPostavke = myPostavke;

        spinnerVrstaAplikacije.setAdapter(new ArrayAdapter<VrstaAplikacije>(this, android.R.layout.simple_spinner_item, VrstaAplikacije.values()));
        sppinerVrstaPretrage.setAdapter(new ArrayAdapter<VrstaPretrageArtikala>(this, android.R.layout.simple_spinner_item, VrstaPretrageArtikala.values()));

        pinText.setText(myPostavke.getPin());
        pinText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.i(TAG,"Pin postavljen na " + s.toString());
                myPostavke.snimiPin(s.toString());
            }
        });

        spinPodtipDokumenta.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    spinnerTouched = true; // User DID touched the spinner!
                    Log.d(TAG, "onTouch: SNIMI:: onTouch=true");
                }
                return false;
            }
        });

        ArrayAdapter<TipDokumenta> tipAdapter = new ArrayAdapter<TipDokumenta>(this, android.R.layout.simple_spinner_item);
        List<TipDokumenta> listaTipovaDokumenata = MainActivity.getListaTipovaDokumenta(PostavkeActivity.this, "", true, "SVI TIPOVI DOKUMENATA");
        if (listaTipovaDokumenata != null) {
            for (TipDokumenta tipDok : listaTipovaDokumenata) {
                tipAdapter.add(tipDok);
            }


            spinTipDokumenta.setAdapter(tipAdapter);

            spinTipDokumenta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    spinnerTouched = false;
                    TipDokumenta tiDok = (TipDokumenta) adapterView.getItemAtPosition(i);
                    idTipDokumenta = tiDok.getId();
                    if (idTipDokumenta != myPostavke.getTipDokumenta()) {
                        if (radi) {
                            myPostavke.snimiTipDokumenta(idTipDokumenta);
                            Log.d(TAG, "onItemSelected: SNIMI tipDokumenta=" + idTipDokumenta);
                        }
                    }
                    ArrayAdapter<PodtipDokumenta> podtipAdapter = new ArrayAdapter<PodtipDokumenta>(PostavkeActivity.this, android.R.layout.simple_spinner_item);
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
                                if (spinnerTouched) {
                                    if (radi) {
                                        myPostavke.snimiPodtipDokumenta(idPodtipDokumenta);
                                        Log.d(TAG, "onItemSelected: SNIMI idPodtipDokumenta=" + idPodtipDokumenta);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinPodtipDokumenta.setSelection(getIndex_PodtipDokumenta(myPostavke.getPodtipDokumenta()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            //setRadi(true);
            spinTipDokumenta.setSelection(getIndex_TipDokumenta(myPostavke.getTipDokumenta()));
            spinPodtipDokumenta.setSelection(getIndex_PodtipDokumenta(myPostavke.getPodtipDokumenta()));

        }

        spinnerVrstaAplikacije.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VrstaAplikacije vrsta = VrstaAplikacije.values()[position];
                int value = vrsta.ordinal();
                if (value != myPostavke.getVrstaAplikacije()) {
                    if (radi) {
                        myPostavke.snimiVrstuAplikacije(value);
                        Log.d(TAG, "onItemSelected: SNIMI VrstaAplikacije=" + value);
                    }

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
                    if (radi) {
                        myPostavke.snimiVrstuPretrageArtikala(value);
                        Log.d(TAG, "onItemSelected: SNIMI VrstaPretrageArtikala=" + value);
                    }

                }

                //myPostavke.snimiVrstuPretrageArtikala(position);
                Log.d(TAG, "onItemSelected: SNIMI VrstaPretrageArtikala=" + position);
                //Toast.makeText(PostavkeActivity.this, value.toString(), Toast.LENGTH_LONG).show();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sppinerVrstaPretrage.setSelection(getIndex_VrstaPretrageArtikla(myPostavke.getVrstaPretrageArtikala()));

        sbarKolicina.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressKolicina = i;
                setDefKolicina(progressKolicina / 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (radi) {
                    myPostavke.snimiDefoltnuKolicinu(defKolicina);
                }

            }
        });
        sbarKolicina.setProgress(Math.round(myPostavke.getDefoltnaKolicina() * 10));

        sbarBrojDecimala.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                Log.d(TAG, "onProgressChanged: i=" + value);
                setDefBrojDec(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (radi) {
                    Log.d(TAG, "onStopTrackingTouch: SNIMAM BROJ DECIMALA");
                    myPostavke.snimiBrojDecimala(defBrojDec);
                }
            }
        });
        if (myPostavke.getBrojDecimala() >= 0 && myPostavke.getBrojDecimala() <= 5) {
            sbarBrojDecimala.setProgress(myPostavke.getBrojDecimala());
        } else {
            sbarBrojDecimala.setProgress(2);
        }

        btnBrziUnosArtikla.setChecked(myPostavke.isBrziUnosArtikala());
        btnBrziUnosArtikla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radi) {
                    myPostavke.snimiBrziUnos(btnBrziUnosArtikla.isChecked());
                }

            }
        });

        btnDopustenaIzmjenaTipaDokumenta.setChecked(myPostavke.isDopustenaIzmjenaTipaDokumenta());
        btnDopustenaIzmjenaTipaDokumenta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radi) {
                    myPostavke.snimiDopustenaIzmjenaTipaDokumenta(btnDopustenaIzmjenaTipaDokumenta.isChecked());
                }

            }
        });

        btnSvirajUpozorenja.setChecked(myPostavke.isSvirajUpozorenja());
        btnSvirajUpozorenja.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (radi) {
                    myPostavke.snimiSvirajUpozorenja(btnSvirajUpozorenja.isChecked());
                }

            }
        });

        //za omogućiti korisniku promjene postavki potrebno je samo postaviti omoguciPromjene na TRUE
        if (omoguciPromjene) {
            radi = true;
        } else {
            btnBrziUnosArtikla.setEnabled(false);
            btnDopustenaIzmjenaTipaDokumenta.setEnabled(false);
            sbarKolicina.setEnabled(false);
            spinTipDokumenta.setEnabled(false);
            spinPodtipDokumenta.setEnabled(false);
            spinnerVrstaAplikacije.setEnabled(false);
            sppinerVrstaPretrage.setEnabled(false);
            sbarBrojDecimala.setEnabled(false);
            lblDefKolicina.setEnabled(false);
            lblBrojDec.setEnabled(false);
            btnSvirajUpozorenja.setEnabled(false);

            spinTipDokumenta.setSelection(getIndex_TipDokumenta(_myPostavke.getTipDokumenta()));
            spinPodtipDokumenta.setSelection(getIndex_PodtipDokumenta(_myPostavke.getPodtipDokumenta()));
            spinnerVrstaAplikacije.setSelection(getIndex_VrstaAplikacije(_myPostavke.getVrstaAplikacije()));
            sppinerVrstaPretrage.setSelection(getIndex_VrstaPretrageArtikla(_myPostavke.getVrstaPretrageArtikala()));
            sbarKolicina.setProgress(Math.round(_myPostavke.getDefoltnaKolicina() * 10));
            if (_myPostavke.getBrojDecimala() >= 0 && _myPostavke.getBrojDecimala() <= 5) {
                sbarBrojDecimala.setProgress(_myPostavke.getBrojDecimala());
            } else {
                sbarBrojDecimala.setProgress(2);
            }
            btnBrziUnosArtikla.setChecked(_myPostavke.isBrziUnosArtikala());
            btnDopustenaIzmjenaTipaDokumenta.setChecked(_myPostavke.isDopustenaIzmjenaTipaDokumenta());
            btnSvirajUpozorenja.setChecked(_myPostavke.isSvirajUpozorenja());

//{"id":2,"kasaId":1,"pjFrmId":1,"saldoKomitenta":1,"vrstaAplikacije":3,"vrstaPretrage":0,"dopustenaIzmjenaTipaDokumenta":true,
// "zadaniTipDokumenta":5,"zadaniPodtipDokumenta":10,"brziUnosPodataka":true,"zadanaKolicinaArtikala":2,"zvukoviUpozorenja":true,"brojDecimala":2}
            Log.d(TAG, "onCreate: PostavkeAplikacije kasaID=" + _myPostavke.getKasaId());
            Log.d(TAG, "onCreate: PostavkeAplikacije pjFrmId=" + _myPostavke.getPjFrmId());
            Log.d(TAG, "onCreate: PostavkeAplikacije podtipDokumenta=" + _myPostavke.getPodtipDokumenta());
            Log.d(TAG, "onCreate: PostavkeAplikacije defKolicina=" + _myPostavke.getDefoltnaKolicina());
            Log.d(TAG, "onCreate: PostavkeAplikacije dltID=" + _myPostavke.getDlt_id());
            Log.d(TAG, "onCreate: PostavkeAplikacije tipDokumenta=" + _myPostavke.getTipDokumenta());
            Log.d(TAG, "onCreate: PostavkeAplikacije vrstaApp=" + _myPostavke.getVrstaAplikacije());
            Log.d(TAG, "onCreate: PostavkeAplikacije vrstaPretrage=" + _myPostavke.getVrstaPretrageArtikala());
            Log.d(TAG, "onCreate: PostavkeAplikacije saldoKomitenta=" + _myPostavke.getSaldakonti());
            Log.d(TAG, "onCreate: PostavkeAplikacije dopustenaIzmjenaTipa=" + _myPostavke.isDopustenaIzmjenaTipaDokumenta());
            Log.d(TAG, "onCreate: PostavkeAplikacije brziUnos=" + _myPostavke.isBrziUnosArtikala());
            Log.d(TAG, "onCreate: PostavkeAplikacije zvukoviUpozorenja=" + _myPostavke.isSvirajUpozorenja());
            Log.d(TAG, "onCreate: PostavkeAplikacije brojDecimala=" + _myPostavke.getBrojDecimala());


        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (spinPodtipDokumenta.getCount() > 0) {
            PodtipDokumenta podtipDok = (PodtipDokumenta) spinPodtipDokumenta.getSelectedItem();
            idPodtipDokumenta = podtipDok.getId();
            _myPostavke.snimiPodtipDokumenta(idPodtipDokumenta);
            Log.d(TAG, "onDestroy: SNIMI::: ON_DESTROY = snimljen podtip=" + idPodtipDokumenta);
        }

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
