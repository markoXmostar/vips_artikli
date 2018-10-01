package com.vanima.mvips.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.vanima.mvips.activities.MainActivity;

/**
 * Created by marko on 15.2.2018..
 */

public final class postavkeAplikacije {
    private static String TAG = "postavke";

    private int vrstaPretrageArtikala, vrstaAplikacije, brojDecimala, dlt_id, saldakonti, kasaId, pjFrmId;
    private float defoltnaKolicina;
    private long tipDokumenta, podtipDokumenta;
    private String pin;


    private boolean brziUnosArtikala, dopustenaIzmjenaTipaDokumenta, svirajUpozorenja, uvijekOtvoriDokumentNakonStvaranjaZaglavlja;

    private Activity activity;

    public postavkeAplikacije(Activity a) {
        activity = a;
        procitajPostavke();
    }

    private void procitajPostavke() {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        vrstaPretrageArtikala = settings.getInt("vrstaPretrageArtikala", 0);
        kasaId = settings.getInt("kasaId", 0);
        pjFrmId = settings.getInt("pjFrmId", 0);
        vrstaAplikacije = settings.getInt("vrstaAplikacije", 0);
        defoltnaKolicina = settings.getFloat("defoltnaKolicina", (float) 1.0);
        brojDecimala = settings.getInt("brojDecimala", 2);
        brziUnosArtikala = settings.getBoolean("brziUnosArtikala", true);
        tipDokumenta = settings.getLong("tipDokumenta", 0);
        podtipDokumenta = settings.getLong("podtipDokumenta", 0);
        dopustenaIzmjenaTipaDokumenta = settings.getBoolean("dopustenaIzmjenaTipaDokumenta", true);
        svirajUpozorenja = settings.getBoolean("svirajUpozorenja", true);
        dlt_id = settings.getInt("dlt_id", 0);
        pin = settings.getString("pin", "");
        saldakonti = settings.getInt("saldakonti", 1);
        uvijekOtvoriDokumentNakonStvaranjaZaglavlja = settings.getBoolean("uvijekOtvoriDokumentNakonStvaranjaZaglavlja", false);

        Log.d(TAG, "procitajPostavke: vrstaPretrage=" + vrstaPretrageArtikala);
        Log.d(TAG, "procitajPostavke: vrstaAplikacije=" + vrstaAplikacije);
        Log.d(TAG, "procitajPostavke: defoltnaKolicina=" + defoltnaKolicina);
        Log.d(TAG, "procitajPostavke: brojDecimala=" + brojDecimala);
        Log.d(TAG, "procitajPostavke: brziUnosArtikala=" + brziUnosArtikala);
        Log.d(TAG, "procitajPostavke: tipDokumenta=" + tipDokumenta);
        Log.d(TAG, "procitajPostavke: podtipDokumenta=" + podtipDokumenta);
        Log.d(TAG, "procitajPostavke: dopustenaIzmjenaTipaDokumenta=" + dopustenaIzmjenaTipaDokumenta);
        Log.d(TAG, "procitajPostavke: svirajUpozorenja=" + svirajUpozorenja);
        Log.d(TAG, "procitajPostavke: dlt_id=" + dlt_id);
        Log.d(TAG, "procitajPostavke: kasaId=" + kasaId);
        Log.d(TAG, "procitajPostavke: pjFrmId=" + pjFrmId);
        Log.d(TAG, "procitajPostavke: pin=" + pin);
        Log.d(TAG, "procitajPostavke: uvijekOtvoriDokumentNakonStvaranjaZaglavlja=" + uvijekOtvoriDokumentNakonStvaranjaZaglavlja);
    }

    public String getPin() {
        //pin=password
        return pin;
    }

    public int getDlt_id() {
        return dlt_id;
    }

    public int getKasaId() {
        return kasaId;
    }

    public int getPjFrmId() {
        return pjFrmId;
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

    public boolean isSvirajUpozorenja() {
        return svirajUpozorenja;
    }

    public boolean isUvijekOtvoriDokumentNakonStvaranjaZaglavlja(){
        return uvijekOtvoriDokumentNakonStvaranjaZaglavlja;
    }

    public long getTipDokumenta() {
        return tipDokumenta;
    }

    public long getPodtipDokumenta() {
        return podtipDokumenta;
    }

    public int getSaldakonti() {
        return saldakonti;
    }

    public void snimiDLT_ID(int value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("dlt_id", value);
        editor.commit();
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

    public void snimiSvirajUpozorenja(boolean value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("svirajUpozorenja", value);
        editor.commit();
    }

    public void snimiPin(String value) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("pin", value);
        editor.commit();
    }

    public void snimiUvijekOtvoriDokumentNakonStvaranjaZaglavlja(boolean value){
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("uvijekOtvoriDokumentNakonStvaranjaZaglavlja", value);
        editor.commit();
    }

    public void snimiSvePostavke(int dlt_id, int kasaId, int pjFrmId, long tipDokumenta, long podtipDokumenta, int vrstaAplikacije, int vrstaPretrageArtikala, boolean brziUnosArtikala, float defoltnaKolicina,
                                 int brojDecimala, boolean dopustenaIzmjenaTipaDokumenta, boolean svirajUpozorenja) {
        SharedPreferences settings = activity.getSharedPreferences(MainActivity.APP_POSTAVKE, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("dlt_id", dlt_id);
        editor.putInt("kasaId", kasaId);
        editor.putInt("pjFrmId", pjFrmId);
        editor.putLong("tipDokumenta", tipDokumenta);
        editor.putLong("podtipDokumenta", podtipDokumenta);
        editor.putInt("vrstaAplikacije", vrstaAplikacije);
        editor.putInt("vrstaPretrageArtikala", vrstaPretrageArtikala);
        editor.putBoolean("brziUnosArtikala", brziUnosArtikala);
        editor.putFloat("defoltnaKolicina", defoltnaKolicina);
        editor.putInt("brojDecimala", brojDecimala);
        editor.putBoolean("dopustenaIzmjenaTipaDokumenta", dopustenaIzmjenaTipaDokumenta);
        editor.putBoolean("svirajUpozorenja", svirajUpozorenja);
        editor.commit();

        procitajPostavke();

        Log.d(TAG, "POSTAVKE SNIMAM SA INTERNETA kasaId=" + kasaId);

        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije kasaID=" + getKasaId());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije pjFrmId=" + getPjFrmId());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije podtipDokumenta=" + getPodtipDokumenta());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije defKolicina=" + getDefoltnaKolicina());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije dltID=" + getDlt_id());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije tipDokumenta=" + getTipDokumenta());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije vrstaApp=" + getVrstaAplikacije());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije vrstaPretrage=" + getVrstaPretrageArtikala());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije saldoKomitenta=" + getSaldakonti());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije dopustenaIzmjenaTipa=" + isDopustenaIzmjenaTipaDokumenta());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije brziUnos=" + isBrziUnosArtikala());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije zvukoviUpozorenja=" + isSvirajUpozorenja());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije brojDecimala=" + getBrojDecimala());
        Log.d(TAG, "snimiSvePostavke: PostavkeAplikacije pin=" + getPin());
    }
}
