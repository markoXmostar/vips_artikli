package com.example.marko.vips_artikli;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by marko on 15.2.2018..
 */

public final class postavkeAplikacije {

    private int vrstaPretrageArtikala, vrstaAplikacije, brojDecimala;
    private float defoltnaKolicina;
    private boolean brziUnosArtikala;

    private Activity activity;

    public postavkeAplikacije(Activity a) {
        activity = a;
        procitajPostavke();
    }

    private void procitajPostavke() {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        vrstaPretrageArtikala = settings.getInt("vrstaPretrageArtikala", 0);
        vrstaAplikacije = settings.getInt("vrstaAplikacije", 1);
        defoltnaKolicina = settings.getFloat("defoltnaKolicina", (float) 0.0);
        brojDecimala = settings.getInt("brojDecimala", 2);
        brziUnosArtikala = settings.getBoolean("brziUnosArtikala", false);
    }


    public int getVrstaPretrageArtikala() {
        return vrstaPretrageArtikala;
    }

    public int getVrstaAplikacije() {
        return vrstaAplikacije;
    }

    public int getBrojDecimala() {
        return brojDecimala;
    }

    public float getDefoltnaKolicina() {
        return defoltnaKolicina;
    }

    public boolean isBrziUnosArtikala() {
        return brziUnosArtikala;
    }
}
