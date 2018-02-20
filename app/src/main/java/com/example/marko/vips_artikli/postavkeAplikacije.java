package com.example.marko.vips_artikli;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by marko on 15.2.2018..
 */

public final class postavkeAplikacije {
    private static String TAG = "postavke";

    private int vrstaPretrageArtikala, vrstaAplikacije, brojDecimala;
    private float defoltnaKolicina;
    private long tipDokumenta, podtipDokumenta;


    private boolean brziUnosArtikala, dopustenaIzmjenaTipaDokumenta;

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
        tipDokumenta = settings.getLong("tipDokumenta", 0);
        podtipDokumenta = settings.getLong("podtipDokumenta", 0);
        dopustenaIzmjenaTipaDokumenta = settings.getBoolean("dopustenaIzmjenaTipaDokumenta", true);
        Log.d(TAG, "procitajPostavke: vrstaPretrage=" + vrstaPretrageArtikala);
        Log.d(TAG, "procitajPostavke: vrstaAplikacije=" + vrstaAplikacije);
        Log.d(TAG, "procitajPostavke: defoltnaKolicina=" + defoltnaKolicina);
        Log.d(TAG, "procitajPostavke: brojDecimala=" + brojDecimala);
        Log.d(TAG, "procitajPostavke: brziUnosArtikala=" + brziUnosArtikala);
        Log.d(TAG, "procitajPostavke: tipDokumenta=" + tipDokumenta);
        Log.d(TAG, "procitajPostavke: podtipDokumenta=" + podtipDokumenta);
        Log.d(TAG, "procitajPostavke: dopustenaIzmjenaTipaDokumenta=" + dopustenaIzmjenaTipaDokumenta);

    }

    public boolean isDopustenaIzmjenaTipaDokumenta() {
        return dopustenaIzmjenaTipaDokumenta;
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

    public long getTipDokumenta() {
        return tipDokumenta;
    }

    public long getPodtipDokumenta() {
        return podtipDokumenta;
    }


    public void snimiTipDokumenta(long value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("tipDokumenta", value);
        editor.commit();
    }

    public void snimiPodtipDokumenta(long value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("podtipDokumenta", value);
        editor.commit();
    }

    public void snimiVrstuAplikacije(int value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("vrstaAplikacije", value);
        editor.commit();
    }

    public void snimiVrstuPretrageArtikala(int value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("vrstaPretrageArtikala", value);
        editor.commit();
    }

    public void snimiBrziUnos(boolean value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("brziUnosArtikala", value);
        editor.commit();
    }

    public void snimiDefoltnuKolicinu(float value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("defoltnaKolicina", value);
        editor.commit();
    }

    public void snimiBrojDecimala(int value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("brojDecimala", value);
        editor.commit();
    }

    public void snimiDopustenaIzmjenaTipaDokumenta(boolean value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("dopustenaIzmjenaTipaDokumenta", value);
        editor.commit();
    }
}
