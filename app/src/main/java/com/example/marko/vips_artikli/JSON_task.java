package com.example.marko.vips_artikli;

import android.app.Application;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.example.marko.vips_artikli.MainActivity.myDATABASE;

/**
 * Created by marko on 1.11.2017.
 */

public class JSON_task  extends AsyncTask<String, String, String>{

        public static final String TAG="JSON";

    private String tipPodataka = "";

        private ProgressDialog pd;
        private MainActivity myMainActivity;

        Date vrijeme1,vrijeme2;

        public JSON_task(MainActivity activity) {
            myMainActivity=activity;
            pd = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            vrijeme1 = new Date(System.currentTimeMillis());
            pd = new ProgressDialog(myMainActivity);
            String poruka =(String) myMainActivity.getApplicationContext().getResources().getString(R.string.Poruka_app_molimPricekajte);
            pd.setMessage(poruka);
            pd.setCancelable(false);
            pd.show();
        }


        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                String myUrl=params[0];
                tipPodataka = params[1];
                URL url =new URL(myUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }  finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            JSONObject jObject = null;

            switch (tipPodataka) {
                case "artikli":
                    result = "{\"Artikli\":" + result + ",\"ResponseStatus\":{}}";
                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String artikli = jObject.getString("Artikli");
                        JSONArray arr = new JSONArray(artikli);
                        ArrayList<Artikl> ListaArtikala = new ArrayList<Artikl>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myArtikl = arr.getJSONObject(i);
                            Artikl art = new Artikl(myArtikl.optLong("id", 0),
                                    myArtikl.optString("sifra"),
                                    myArtikl.optString("naziv"),
                                    myArtikl.optString("kataloskiBroj"),
                                    myArtikl.optString("jmj"),
                                    myArtikl.optString("kratkiOpis"),
                                    myArtikl.optString("proizvodjac"),
                                    myArtikl.optString("dugiOpis"),
                                    myArtikl.optString("vrstaAmbalaze"),
                                    myArtikl.optDouble("brojKoleta", 0),
                                    myArtikl.optDouble("brojKoletaNaPaleti", 0),
                                    myArtikl.optDouble("stanje", 0),
                                    myArtikl.optDouble("vpc", 0),
                                    myArtikl.optDouble("mpc", 0),
                                    myArtikl.optDouble("netto", 0),
                                    myArtikl.optDouble("brutto", 0),
                                    myArtikl.optBoolean("imaRokTrajanja", false),
                                    myArtikl.optInt("podgrupaID", 0));
                            ListaArtikala.add(art);
                        }
                        Log.d(TAG, "onPostExecute: BROJ ARTIKALA=" + ListaArtikala.size());
                        UpisiArtikleUBazu(ListaArtikala);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "jmj":
                    result = "{\"Jmj\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String jmj = jObject.getString("Jmj");
                        JSONArray arr = new JSONArray(jmj);
                        ArrayList<jmj> ListaJmj = new ArrayList<jmj>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myJmj = arr.getJSONObject(i);
                            jmj _jmj = new jmj(myJmj.optLong("id", 0),
                                    myJmj.optString("naziv"));
                            ListaJmj.add(_jmj);
                        }
                        Log.d(TAG, "onPostExecute: BROJ JMJ=" + ListaJmj.size());
                        UpisiJMJUBazu(ListaJmj);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "tipdokumenta":
                    result = "{\"Tipdokumenta\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String tip = jObject.getString("Tipdokumenta");
                        JSONArray arr = new JSONArray(tip);
                        ArrayList<TipDokumenta> ListaTipova = new ArrayList<TipDokumenta>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myTipDokumenta = arr.getJSONObject(i);
                            TipDokumenta _tipDokumenta = new TipDokumenta(myTipDokumenta.optLong("id", 0),
                                    myTipDokumenta.optString("naziv"));
                            ListaTipova.add(_tipDokumenta);
                        }
                        Log.d(TAG, "onPostExecute: BROJ TipovaDokumenta =" + ListaTipova.size());
                        UpisiTipUBazu(ListaTipova);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "podtipdokumenta":
                    result = "{\"Podtipdokumenta\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String podtip = jObject.getString("Podtipdokumenta");
                        JSONArray arr = new JSONArray(podtip);
                        ArrayList<PodtipDokumenta> ListaPodTipova = new ArrayList<PodtipDokumenta>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myPodtipDokumenta = arr.getJSONObject(i);
                            PodtipDokumenta _podtipDokumenta = new PodtipDokumenta(myPodtipDokumenta.optLong("id", 0),
                                    myPodtipDokumenta.optString("naziv"),myPodtipDokumenta.optLong("rid",0));
                            ListaPodTipova.add(_podtipDokumenta);
                        }
                        Log.d(TAG, "onPostExecute: BROJ PODTipovaDokumenta =" + ListaPodTipova.size());
                        UpisiPodtipUBazu(ListaPodTipova);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "grupaartikala":
                    result = "{\"Grupaartikala\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String grupa = jObject.getString("Grupaartikala");
                        JSONArray arr = new JSONArray(grupa);
                        ArrayList<GrupaArtikala> ListaGrupa = new ArrayList<GrupaArtikala>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myGrupaArtikala = arr.getJSONObject(i);
                            GrupaArtikala _grupa = new GrupaArtikala(myGrupaArtikala.optLong("id", 0),
                                    myGrupaArtikala.optString("naziv"),myGrupaArtikala.optLong("rid",0));
                            ListaGrupa.add(_grupa);
                        }
                        Log.d(TAG, "onPostExecute: BROJ PODTipovaDokumenta =" + ListaGrupa.size());
                        UpisiGrupuUBazu(ListaGrupa);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "podgrupaartikala":
                    result = "{\"Podgrupaartikala\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String podgrupa = jObject.getString("Podgrupaartikala");
                        JSONArray arr = new JSONArray(podgrupa);
                        ArrayList<PodgrupaArtikala> ListaPodgrupa = new ArrayList<PodgrupaArtikala>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myPodGrupaArtikala = arr.getJSONObject(i);
                            PodgrupaArtikala _podgrupa = new PodgrupaArtikala(myPodGrupaArtikala.optLong("id", 0),
                                    myPodGrupaArtikala.optString("naziv"),myPodGrupaArtikala.optLong("rid",0));
                            ListaPodgrupa.add(_podgrupa);
                        }
                        Log.d(TAG, "onPostExecute: BROJ PODTipovaDokumenta =" + ListaPodgrupa.size());
                        UpisiPodGrupuUBazu(ListaPodgrupa);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "nacinplacanja":
                    result = "{\"Nacinplacanja\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String _nacinPlacanja = jObject.getString("Nacinplacanja");
                        JSONArray arr = new JSONArray(_nacinPlacanja);
                        ArrayList<NacinPlacanja> ListaNacinaPlacanja = new ArrayList<NacinPlacanja>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myPodtipDokumenta = arr.getJSONObject(i);
                            NacinPlacanja _nacPlacanja = new NacinPlacanja(myPodtipDokumenta.optLong("id", 0),
                                    myPodtipDokumenta.optString("naziv"));
                            ListaNacinaPlacanja.add(_nacPlacanja);
                        }
                        Log.d(TAG, "onPostExecute: BROJ NačinaPlaćanja =" + ListaNacinaPlacanja.size());
                        UpisiNacinPlacanjaUBazu(ListaNacinaPlacanja);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:

            }


        }

    private void UpisiArtikleUBazu(ArrayList<Artikl> Lista) {
//prvo otvori ili kreiraj bazu komitenata
        Log.d(TAG, "Otvaram bazu");
        SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "UpisiArtikleUBazu: brišem tabelu ukoliko postoji");
        myDB.execSQL("DROP TABLE IF EXISTS artikli");
        Log.d(TAG, "Kreiram tabelu");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS artikli (" +
                "_id VARCHAR, " +
                "sifra VARCHAR, " +
                "naziv VARCHAR," +
                "kataloskiBroj VARCHAR, " +
                "jmj VARCHAR, " +
                "kratkiOpis VARCHAR, " +
                "proizvodjac VARCHAR, " +
                "dugiOpis VARCHAR, " +
                "vrstaAmbalaze VARCHAR, " +
                "brojKoleta double," +
                "brojKoletaNaPaleti double, " +
                "stanje double," +
                "vpc double, " +
                "mpc double, " +
                "netto double, " +
                "brutto double, " +
                "imaRokTrajanja double, " +
                "podgrupaID int);");
        Log.d(TAG, "Brišem sve iz tabele");
        myDB.execSQL("DELETE FROM artikli");
        for (int i = 0; i < Lista.size(); i++) {
            Artikl myArt = Lista.get(i);
            myDB.execSQL("INSERT INTO artikli (_id, sifra , " +
                    "naziv ,kataloskiBroj , jmj , kratkiOpis , proizvodjac , dugiOpis , vrstaAmbalaze, brojKoleta, brojKoletaNaPaleti,stanje,vpc,mpc,netto,brutto,imaRokTrajanja,podgrupaID ) VALUES ('" +
                    myArt.getId() + "','" +
                    myArt.getSifra() + "','" +
                    myArt.getNaziv().replaceAll("'", "_") + "','" +
                    myArt.getKataloskiBroj() + "','" +
                    myArt.getJmj() + "','" +
                    myArt.getKratkiOpis() + "','" +
                    myArt.getProizvodjac() + "','" +
                    myArt.getDugiOpis() + "','" +
                    myArt.getVrstaAmbalaze() + "',"
                    + myArt.getBrojKoleta() + "," +
                    myArt.getBrojKoletaNaPaleti() + "," +
                    myArt.getStanje() + "," +
                    myArt.getVpc() + "," +
                    myArt.getMpc() + "," +
                    myArt.getNetto() + "," +
                    myArt.getBrutto() + "," +
                    myArt.getImaRokTrajanja() + "," +
                    myArt.getPodgrupaID()
                    + ");");
        }
        Log.d(TAG, "Gotovo");
    }

    private void UpisiJMJUBazu(ArrayList<jmj> Lista) {
//prvo otvori ili kreiraj bazu komitenata
        Log.d(TAG, "Otvaram bazu");
        SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "UpisiJMJUBazu: brišem tabelu JMJ ukoliko postoji");
        myDB.execSQL("DROP TABLE IF EXISTS jmj");
        Log.d(TAG, "Kreiram tabelu");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS jmj (" +
                "_id VARCHAR, " +
                "naziv VARCHAR);");

        Log.d(TAG, "Brišem sve iz tabele");
        myDB.execSQL("DELETE FROM jmj");
        for (int i = 0; i < Lista.size(); i++) {
            jmj myJMJ = Lista.get(i);
            myDB.execSQL("INSERT INTO jmj (_id, naziv ) VALUES (" +
                    myJMJ.getId() + ",'" +
                    myJMJ.getNaziv()+ "');");

            }
            Log.d(TAG, "Gotovo");
        }

    private void UpisiTipUBazu(ArrayList<TipDokumenta> Lista) {
//prvo otvori ili kreiraj bazu komitenata
        String myTabela="tip_dokumenta";
        Log.d(TAG, "Otvaram bazu");
        SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "UpisiTipUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
        myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
        Log.d(TAG, "Kreiram tabelu");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                "_id VARCHAR, " +
                "naziv VARCHAR);");

        Log.d(TAG, "Brišem sve iz tabele " +myTabela);
        myDB.execSQL("DELETE FROM "+ myTabela +";");
        for (int i = 0; i < Lista.size(); i++) {
            TipDokumenta myTip = Lista.get(i);
            myDB.execSQL("INSERT INTO " + myTabela +" (_id, naziv ) VALUES (" +
                    myTip.getId() + ",'" +
                    myTip.getNaziv()+ "');");

        }
        Log.d(TAG, "Gotovo " + myTabela);
    }

    private void UpisiPodtipUBazu(ArrayList<PodtipDokumenta> Lista) {
//prvo otvori ili kreiraj bazu komitenata
        String myTabela="podtip_dokumenta";
        Log.d(TAG, "Otvaram bazu");
        SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "UpisiTipUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
        myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
        Log.d(TAG, "Kreiram tabelu");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                "_id VARCHAR, " +
                "naziv VARCHAR, rid VARCHAR);");

        Log.d(TAG, "Brišem sve iz tabele " +myTabela);
        myDB.execSQL("DELETE FROM "+ myTabela +";");
        for (int i = 0; i < Lista.size(); i++) {
            PodtipDokumenta myPodTip = Lista.get(i);
            myDB.execSQL("INSERT INTO " + myTabela +" (_id, naziv,rid ) VALUES (" +
                    myPodTip.getId() + ",'" +
                    myPodTip.getNaziv()+ "',"+ myPodTip.getRid() +");");

        }
        Log.d(TAG, "Gotovo " + myTabela);
    }

    private void UpisiNacinPlacanjaUBazu(ArrayList<NacinPlacanja> Lista) {

        String myTabela="nacin_placanja";
        Log.d(TAG, "Otvaram bazu" + myTabela);
        SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "UpisiUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
        myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
        Log.d(TAG, "Kreiram tabelu");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                "_id VARCHAR, " +
                "naziv VARCHAR);");

        Log.d(TAG, "Brišem sve iz tabele " +myTabela);
        myDB.execSQL("DELETE FROM "+ myTabela +";");
        for (int i = 0; i < Lista.size(); i++) {
            NacinPlacanja myNacinPlacanja = Lista.get(i);
            myDB.execSQL("INSERT INTO " + myTabela +" (_id, naziv ) VALUES (" +
                    myNacinPlacanja.getId() + ",'" +
                    myNacinPlacanja.getNaziv()+ "');");

        }
        Log.d(TAG, "Gotovo " + myTabela);
    }

    private void UpisiGrupuUBazu(ArrayList<GrupaArtikala> Lista) {
//prvo otvori ili kreiraj bazu komitenata
        String myTabela="grupa_artikala";
        Log.d(TAG, "Otvaram bazu");
        SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "UpisiTipUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
        myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
        Log.d(TAG, "Kreiram tabelu");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                "_id VARCHAR, " +
                "naziv VARCHAR, rid VARCHAR);");

        Log.d(TAG, "Brišem sve iz tabele " +myTabela);
        myDB.execSQL("DELETE FROM "+ myTabela +";");
        for (int i = 0; i < Lista.size(); i++) {
            GrupaArtikala myGrupa = Lista.get(i);
            myDB.execSQL("INSERT INTO " + myTabela +" (_id, naziv,rid ) VALUES (" +
                    myGrupa.getId() + ",'" +
                    myGrupa.getNaziv()+ "',"+ myGrupa.getRid() +");");

        }
        Log.d(TAG, "Gotovo " + myTabela);
    }

    private void UpisiPodGrupuUBazu(ArrayList<PodgrupaArtikala> Lista) {
//prvo otvori ili kreiraj bazu komitenata
        String myTabela="podgrupa_artikala";
        Log.d(TAG, "Otvaram bazu");
        SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
        Log.d(TAG, "UpisiTipUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
        myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
        Log.d(TAG, "Kreiram tabelu");
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                "_id VARCHAR, " +
                "naziv VARCHAR, rid VARCHAR);");

        Log.d(TAG, "Brišem sve iz tabele " +myTabela);
        myDB.execSQL("DELETE FROM "+ myTabela +";");
        for (int i = 0; i < Lista.size(); i++) {
            PodgrupaArtikala myPodgrupa = Lista.get(i);
            myDB.execSQL("INSERT INTO " + myTabela +" (_id, naziv,rid ) VALUES (" +
                    myPodgrupa.getId() + ",'" +
                    myPodgrupa.getNaziv()+ "',"+ myPodgrupa.getRid() +");");

        }
        Log.d(TAG, "Gotovo " + myTabela);
    }

}
