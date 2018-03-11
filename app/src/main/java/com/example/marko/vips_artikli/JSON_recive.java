package com.example.marko.vips_artikli;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.SQLException;
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
import static com.example.marko.vips_artikli.MainActivity.zadnjaSinkronizacijaID;

public abstract class JSON_recive extends AsyncTask<String, String, String> implements ClientIF {

    public static final String TAG = "JSON_task";

    private MainActivity.UrlTabele myTbl;

    private String tipPodataka = "";

    private String porukaNaEkranu = null;

    private boolean connectionGreska = false;
    private String connectionGreskaMessage = "";

        private ProgressDialog progressDialog;
    private Activity myMainActivity;

        Date vrijeme1,vrijeme2;

    public JSON_recive(Activity activity, MainActivity.UrlTabele _myTbl, String poruka) {
            myMainActivity=activity;
            progressDialog = new ProgressDialog(activity);
            myTbl = _myTbl;
        porukaNaEkranu = poruka;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            vrijeme1 = new Date(System.currentTimeMillis());
            progressDialog = new ProgressDialog(myMainActivity);
            String poruka =(String) myMainActivity.getApplicationContext().getResources().getString(R.string.Poruka_app_molimPricekajte);
            poruka = poruka + " " + porukaNaEkranu;
            progressDialog.setMessage(poruka);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }


        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                String myUrl=params[0];
                tipPodataka = params[1];
                URL url =new URL(myUrl);
                Log.d(TAG, "doInBackground: " + myTbl.urlTabele);

                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000); //5 sekundi
                connection.setReadTimeout(30000); //5 sekundi
                connection.connect();

                int statusCode = connection.getResponseCode();





                InputStream stream = connection.getInputStream();

                if (statusCode >= 200 && statusCode < 400) {
                    // Create an InputStream in order to extract the response object
                    stream = connection.getInputStream();
                    connectionGreska = false;
                    connectionGreskaMessage = "";
                } else {
                    connectionGreska = true;
                    connectionGreskaMessage = connection.getErrorStream().toString();
                    stream = connection.getErrorStream();
                }
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

            JSONObject jObject = null;
            int rezultat = 0;
            if (connectionGreska) {
                String myTabela = myTbl.NazivTabele;
                UpisiLOG(1, "WEB adresa nedostupna", myTabela, 0, -1);
                Log.d(TAG, "onPostExecute: GREŠKA PRI ČITANJU WEB ADRESE!!!!!");
            }
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
                                    myArtikl.optLong("jmjId", 0),
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
                case "artiklbarcode":
                    result = "{\"Artiklbarcode\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String bar = jObject.getString("Artiklbarcode");
                        JSONArray arr = new JSONArray(bar);
                        ArrayList<Barcode> ListaBarcode = new ArrayList<Barcode>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myJmj = arr.getJSONObject(i);
                            Barcode _barcode = new Barcode(myJmj.optLong("artiklId", 0),
                                    myJmj.optString("barcode"));
                            ListaBarcode.add(_barcode);
                        }
                        Log.d(TAG, "onPostExecute: BROJ JMJ=" + ListaBarcode.size());
                        UpisiBarcodeUBazu(ListaBarcode);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "artikljmj":
                    result = "{\"Artikljmj\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String bar = jObject.getString("Artikljmj");
                        JSONArray arr = new JSONArray(bar);
                        ArrayList<ArtiklJmj> ListaArtiklJmj = new ArrayList<ArtiklJmj>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myJmj = arr.getJSONObject(i);
                            ArtiklJmj _artJmj = new ArtiklJmj(myJmj.optLong("artiklId", 0),
                                    myJmj.optLong("jmjId", 0), "", "");
                            ListaArtiklJmj.add(_artJmj);
                        }
                        Log.d(TAG, "onPostExecute: BROJ ArtiklJmjID=" + ListaArtiklJmj.size());
                        UpisiArtiklJmjUBazu(ListaArtiklJmj);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "artiklatribut":
                    result = "{\"Artiklatribut\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String bar = jObject.getString("Artiklatribut");
                        JSONArray arr = new JSONArray(bar);
                        ArrayList<ArtiklAtributStanje> ListaArtiklAtribut = new ArrayList<ArtiklAtributStanje>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myObject = arr.getJSONObject(i);
                            //{"artiklId":176242,"vrijednostId1":42,"vrijednost1":"2017-10-01","atribut1":"Rok trajanja","stanje":0.00000}
                            ArtiklAtributStanje _artiklAtribut = new ArtiklAtributStanje(myObject.optLong("artiklId", 0),
                                    myObject.optLong("vrijednostId1",0), myObject.optString("vrijednost1"),myObject.getString("atribut1"),myObject.optDouble("stanje",0));
                            ListaArtiklAtribut.add(_artiklAtribut);
                        }
                        Log.d(TAG, "onPostExecute: BROJ ArtiklAtributStanje=" + ListaArtiklAtribut.size());
                        UpisiArtiklAtributUBazu(ListaArtiklAtribut);
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
                        Log.d(TAG, "onPostExecute: BROJ Tipova Dokumenta =" + ListaTipova.size());
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
                        Log.d(TAG, "onPostExecute: BROJ PodTipova Dokumenta =" + ListaPodTipova.size());
                        UpisiPodtipUBazu(ListaPodTipova);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "komitenti":
                    result = "{\"Komitenti\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String kom = jObject.getString("Komitenti");
                        JSONArray arr = new JSONArray(kom);
                        ArrayList<Komitent> ListaKomitenata = new ArrayList<Komitent>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myKomitent = arr.getJSONObject(i);
                            Komitent _komitent = new Komitent(myKomitent.optLong("id", 0),
                                    myKomitent.optString("naziv"), myKomitent.optString("sifra"));
                            ListaKomitenata.add(_komitent);
                        }
                        Log.d(TAG, "onPostExecute: BROJ komitenata =" + ListaKomitenata.size());
                        UpisiKomitentUBazu(ListaKomitenata);
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
                        Log.d(TAG, "onPostExecute: BROJ Grupa artikala =" + ListaGrupa.size());
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
                        Log.d(TAG, "onPostExecute: BROJ Podgrupa artikala =" + ListaPodgrupa.size());
                        UpisiPodGrupuUBazu(ListaPodgrupa);
                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "komitentpj":
                    result = "{\"Komitentpj\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecute: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        String podgrupa = jObject.getString("Komitentpj");
                        JSONArray arr = new JSONArray(podgrupa);
                        ArrayList<PjKomitent> ListaPjKomitenata = new ArrayList<PjKomitent>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject myPjKom = arr.getJSONObject(i);
                            PjKomitent _PjKom = new PjKomitent(myPjKom.optLong("id", 0),
                                    myPjKom.optString("naziv"), myPjKom.optLong("rid",0));
                            ListaPjKomitenata.add(_PjKom);
                        }
                        Log.d(TAG, "onPostExecute: BROJ PjKomitenata =" + ListaPjKomitenata.size());
                        UpisiPjKomitenataUBazu(ListaPjKomitenata);
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

                case "prijavaKorisnika":
                    //result = "{\"prijavaKorisnika\":" + result + ",\"ResponseStatus\":{}}";

                    Log.d(TAG, "onPostExecuteXXX: " + result);
                    jObject = null;
                    try {
                        jObject = new JSONObject(result);
                        //String _prijavaKorisnika = jObject.getString("prijavaKorisnika");
                        //JSONArray arr = new JSONArray(_prijavaKorisnika);
                        //Log.d(TAG, "onPostExecute: BROJ ZAPISA PRIJAVE JE: " +arr.length());
                        JSONObject myPostavke = jObject;
                        int dlt_id = myPostavke.optInt("id", 0);
                        int vrstaAplikacije = myPostavke.optInt("", 0);
                        int vrstaPretrage = myPostavke.optInt("", 0);
                        boolean dopustenaIzmjenaTipaDokumenta = myPostavke.optBoolean("dopustenaIzmjenaTipaDokumenta", false);
                        long zadaniTipDokumenta = myPostavke.optLong("zadaniTipDokumenta", 0);
                        long zadaniPodtipDokumenta = myPostavke.optLong("zadaniPodtipDokumenta", 0);
                        boolean brziUnosPodataka = myPostavke.optBoolean("brziUnosPodataka", true);
                        double zadanaKolicinaArtikala = myPostavke.optDouble("zadanaKolicinaArtikala", 1);
                        boolean zvukoviUpozorenja = myPostavke.optBoolean("zvukoviUpozorenja", true);
                        int brojDecimala = myPostavke.optInt("brojDecimala", 2);

                        if (dlt_id > 0) {

                            postavkeAplikacije postavke = new postavkeAplikacije(myMainActivity);
                            postavke.snimiDLT_ID(dlt_id);
                            //postavke.snimiSvePostavke(dlt_id,zadaniTipDokumenta,zadaniPodtipDokumenta,vrstaAplikacije,vrstaPretrage,brziUnosPodataka,
                            //        (float)zadanaKolicinaArtikala,brojDecimala,dopustenaIzmjenaTipaDokumenta,zvukoviUpozorenja);
                            Log.d(TAG, "onPostExecute: URL PRIJAVE: " + myTbl.urlTabele);
                            Log.d(TAG, "onPostExecute: SNIMAM DLT_ID: " + dlt_id);
                            rezultat = dlt_id;
                        }

                        vrijeme2 = new Date(System.currentTimeMillis());
                        long different = vrijeme2.getTime() - vrijeme1.getTime();
                        Log.d(TAG, "onPostExecute: VRIJEME UČITAVANJA S INTERNETA I UPISA U BAZU JE :" + different + "ms");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:

            }

            //ovde treba reći glavnom da je gotovo pa da može napraviti refresh baze
            Log.d(TAG, "onPostExecute: ZOVEM UPDATE PODATKA ZAVRŠENO za" + myTbl.NazivTabele);
            myTbl.ZavrsenaSyncronizacija = true;
            Log.d(TAG, "onPostExecute: ZOVEM UPDATE PODATKA ZAVRŠENO je promjenjeno u ->" + myTbl.ZavrsenaSyncronizacija);
            //myMainActivity.updateSyncTabele(tipPodataka,true);
            progressDialog.dismiss();
            if (!tipPodataka.equals("prijavaKorisnika")) {
                MainActivity mainActivity = (MainActivity) myMainActivity;
                mainActivity.getLOG();
            } else {
                onResponseReceived(rezultat);
            }


        }

    public abstract void onResponseReceived(int result);

    private void UpisiArtikleUBazu(ArrayList<Artikl> Lista) {
//prvo otvori ili kreiraj bazu komitenata

        SQLiteDatabase myDB = null;
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        Log.d(TAG, "UpisiArtikleUBazu: XXX ARTIKLI" );
        try {
            Log.d(TAG, "XXX Otvaram bazu");
            myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "XXX UpisiArtikleUBazu: brišem tabelu ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS " + myTabela);
            Log.d(TAG, "XXX Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS " + myTabela + " (" +
                    "_id long, " +
                    "sifra VARCHAR, " +
                    "naziv VARCHAR," +
                    "kataloskiBroj VARCHAR, " +
                    "jmjId long, " +
                    "jmjNaziv VARCHAR, " +
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
            Log.d(TAG, "XXX Brišem sve iz tabele");
            myDB.execSQL("DELETE FROM " + myTabela);
            for (int i = 0; i < Lista.size(); i++) {
                Artikl myArt = Lista.get(i);
                myDB.execSQL("INSERT INTO " + myTabela + " (_id, sifra , " +
                        "naziv ,kataloskiBroj , jmjId, jmjNaziv, kratkiOpis , proizvodjac , dugiOpis , vrstaAmbalaze, brojKoleta, brojKoletaNaPaleti,stanje,vpc,mpc,netto,brutto,imaRokTrajanja,podgrupaID ) VALUES ('" +
                        myArt.getId() + "','" +
                        myArt.getSifra() + "','" +
                        myArt.getNaziv().replaceAll("'", "_") + "','" +
                        myArt.getKataloskiBroj() + "'," +
                        myArt.getJmjId() + ",'" +
                        myArt.getJmjNaziv() + "','" +
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
            Log.d(TAG, "XXX Gotovo");
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
                Log.d(TAG, "UpisiArtikleUBazu: XXX UPISANO U LOG GREŠKA");
                Log.d(TAG, "XXX UpisiArtikleUBazu: "+ greskaStr);
            }
            else{

                greskaStr = "Uspješno upisano Artikala:" + Integer.toString(Lista.size());
                Log.d(TAG, "UpisiArtikleUBazu: "+ greskaStr);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
                Log.d(TAG, "UpisiArtikleUBazu: XXX UPISANO U LOG SVE OK");
            }
        }
    }


    private void UpisiLOG(int greska, String LOGporuka, String tabela, Integer smjer, int brojPodataka) {
        SQLiteDatabase myDB = null;
        myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);

        //smjer 0 za download
        //smjer 1 za upload
        Integer lastSyncID=zadnjaSinkronizacijaID+1;
        if (brojPodataka == 0) {
            greska = 1;
            LOGporuka = "Nisu dohvaćeni podaci sa servera!!!";
        }

        myDB.execSQL("INSERT INTO log (greska ,poruka , smjer ,tabela, redniBroj) VALUES ('" +
                greska + "','" + LOGporuka + "'," + smjer + ",'" + tabela + "'," + lastSyncID + ");");

        myDB.close();
    }

    private void UpisiJMJUBazu(ArrayList<jmj> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        try {

            Log.d(TAG, "Otvaram bazu");
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiJMJUBazu: brišem tabelu " + myTabela + " ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS " + myTabela + ";");
            Log.d(TAG, "Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS " + myTabela + " (" +
                    "_id long, " +
                    "naziv VARCHAR);");

            Log.d(TAG, "Brišem sve iz tabele");
            myDB.execSQL("DELETE FROM " + myTabela + ";");
            for (int i = 0; i < Lista.size(); i++) {
                jmj myJMJ = Lista.get(i);
                myDB.execSQL("INSERT INTO " + myTabela + " (_id, naziv ) VALUES (" +
                        myJMJ.getId() + ",'" +
                        myJMJ.getNaziv()+ "');");

            }
            Log.d(TAG, "Gotovo ");
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
                Log.d(TAG, "UpisiJMJUBazu: " + greskaStr);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

    private void UpisiBarcodeUBazu(ArrayList<Barcode> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        try {

            Log.d(TAG, "Otvaram bazu");
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiBarcodeUBazu: brišem tabelu " + myTabela + " ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS " + myTabela + ";");
            Log.d(TAG, "Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS " + myTabela + " (" +
                    "artiklId long, " +
                    "barcode VARCHAR);");

            Log.d(TAG, "Brišem sve iz tabele");
            myDB.execSQL("DELETE FROM " + myTabela + ";");
            for (int i = 0; i < Lista.size(); i++) {
                Barcode barcode = Lista.get(i);
                myDB.execSQL("INSERT INTO " + myTabela + " (artiklId, barcode ) VALUES (" +
                        barcode.getArtiklId() + ",'" +
                        barcode.getBarcode()+ "');");

            }
            Log.d(TAG, "Gotovo ");
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
                Log.d(TAG, "UpisiBarcodeUBazu: " + greskaStr);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

    private void UpisiArtiklJmjUBazu(ArrayList<ArtiklJmj> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        try {

            Log.d(TAG, "Otvaram bazu");
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiArtiklJmjUBazu: brišem tabelu " + myTabela + " ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS " + myTabela + ";");
            Log.d(TAG, "Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS " + myTabela + " (" +
                    "artiklId long, " +
                    "jmjId long);");

            Log.d(TAG, "Brišem sve iz tabele");
            myDB.execSQL("DELETE FROM " + myTabela + ";");
            for (int i = 0; i < Lista.size(); i++) {
                ArtiklJmj object = Lista.get(i);
                myDB.execSQL("INSERT INTO " + myTabela + " (artiklId, jmjId ) VALUES (" +
                        Long.toString(object.getArtiklID()) + "," +
                        Long.toString(object.getJmjID())+ ");");

            }
            Log.d(TAG, "Gotovo ");
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
                Log.d(TAG, "UpisiArticleJmjUBazu: " + greskaStr);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

    private void UpisiArtiklAtributUBazu(ArrayList<ArtiklAtributStanje> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        try {

            Log.d(TAG, "Otvaram bazu");
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiArtiklAtributUBazu: brišem tabelu " + myTabela + " ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS " + myTabela + ";");
            Log.d(TAG, "Kreiram tabelu");
            //{"artiklId":176242,"vrijednostId1":42,"vrijednost1":"2017-10-01","atribut1":"Rok trajanja","stanje":0.00000}
            myDB.execSQL("CREATE TABLE IF NOT EXISTS " + myTabela + " (" +
                    "artiklId long, " +
                    "vrijednostId1 long," +
                    "vrijednost1 varchar," +
                    "atribut1 varchar," +
                    "stanje double);");

            Log.d(TAG, "Brišem sve iz tabele");
            myDB.execSQL("DELETE FROM " + myTabela + ";");
            for (int i = 0; i < Lista.size(); i++) {
                ArtiklAtributStanje object = Lista.get(i);
                myDB.execSQL("INSERT INTO " + myTabela + " (artiklId, vrijednostId1, vrijednost1, atribut1, stanje) VALUES (" +
                        object.getArtiklId() + "," +
                        object.getAtributId1() + ",'" +
                        object.getAtributNaziv1() + "','" +
                        object.getAtributVrijednost1() + "'," +
                        object.getStanje()+ ");");

            }
            Log.d(TAG, "Gotovo ");
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
                Log.d(TAG, "UpisiArtiklAtributUBazu: " + greskaStr);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

    private void UpisiTipUBazu(ArrayList<TipDokumenta> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        try {

            Log.d(TAG, "Otvaram bazu");
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiTipUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
            Log.d(TAG, "Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                    "_id long, " +
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
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

    private void UpisiPodtipUBazu(ArrayList<PodtipDokumenta> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;

        try {
            Log.d(TAG, "Otvaram bazu");
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiTipUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
            Log.d(TAG, "Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                    "_id long, " +
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
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

    private void UpisiKomitentUBazu(ArrayList<Komitent> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        try {
            Log.d(TAG, "Otvaram bazu");
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiTipUBazu: brišem tabelu " + myTabela + " ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS " + myTabela + ";");
            Log.d(TAG, "Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS " + myTabela + " (" +
                    "_id long, " +
                    "naziv VARCHAR, sifra VARCHAR);");

            Log.d(TAG, "Brišem sve iz tabele " + myTabela);
            myDB.execSQL("DELETE FROM " + myTabela + ";");
            for (int i = 0; i < Lista.size(); i++) {
                Komitent myKom = Lista.get(i);
                myDB.execSQL("INSERT INTO " + myTabela + " (_id, naziv,sifra ) VALUES (" +
                        myKom.getId() + ",'" +
                        myKom.getNaziv() + "','" + myKom.getSifra() + "');");

            }
            Log.d(TAG, "Gotovo " + myTabela);
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

    private void UpisiNacinPlacanjaUBazu(ArrayList<NacinPlacanja> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        try {
            Log.d(TAG, "Otvaram bazu" + myTabela);
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
            Log.d(TAG, "Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                    "_id long, " +
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
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

    private void UpisiGrupuUBazu(ArrayList<GrupaArtikala> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        try {
            Log.d(TAG, "Otvaram bazu");
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiTipUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
            Log.d(TAG, "Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                    "_id long, " +
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
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

    private void UpisiPodGrupuUBazu(ArrayList<PodgrupaArtikala> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        try {
            Log.d(TAG, "Otvaram bazu");
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiTipUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
            Log.d(TAG, "Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                    "_id long, " +
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
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

    private void UpisiPjKomitenataUBazu(ArrayList<PjKomitent> Lista) {
        boolean greska=false;
        String greskaStr="";
        String myTabela = myTbl.NazivTabele;
        try {
            Log.d(TAG, "Otvaram bazu");
            SQLiteDatabase myDB = myMainActivity.openOrCreateDatabase(myDATABASE, MODE_PRIVATE, null);
            Log.d(TAG, "UpisiTipUBazu: brišem tabelu "+ myTabela+" ukoliko postoji");
            myDB.execSQL("DROP TABLE IF EXISTS "+ myTabela +";");
            Log.d(TAG, "Kreiram tabelu");
            myDB.execSQL("CREATE TABLE IF NOT EXISTS "+ myTabela + " (" +
                    "_id long, " +
                    "naziv VARCHAR, rid VARCHAR);");

            Log.d(TAG, "Brišem sve iz tabele " +myTabela);
            myDB.execSQL("DELETE FROM "+ myTabela +";");
            for (int i = 0; i < Lista.size(); i++) {
                PjKomitent myPjKom = Lista.get(i);
                myDB.execSQL("INSERT INTO " + myTabela +" (_id, naziv,rid ) VALUES (" +
                        myPjKom.getId() + ",'" +
                        myPjKom.getNaziv()+ "',"+ myPjKom.getRid() +");");

            }
            Log.d(TAG, "Gotovo " + myTabela);
            myDB.close();
        } catch (SQLException e) {
            e.printStackTrace();
            greskaStr=e.getMessage();
            greska=true;
        } finally {
            if (greska){
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(1, greskaStr, myTabela, 0, -1);
            }
            else{
                greskaStr="Uspješno upisano :" +Integer.toString(Lista.size()) + " podataka";
                Log.d(TAG, "UpisiUBazu: "+ greskaStr + "/" +myTabela);
                UpisiLOG(0, greskaStr, myTabela, 0, Lista.size());
            }
        }
    }

}
