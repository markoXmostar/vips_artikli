package com.vanima.mvips.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.vanima.mvips.R;
import com.vanima.mvips.models.App1Dokumenti;
import com.vanima.mvips.models.App1Stavke;
import com.vanima.mvips.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by marko on 11.2.2018..
 */

public class JSON_send extends AsyncTask<String, String, String> {

    private static String TAG = "SendData_JSON";

    List<App1Dokumenti> spisakDokumenta;
    Activity myActivity=null;
    private ProgressDialog progressDialog;
    private boolean sendVipsID;
    private int vrstaAplikacije;

    public JSON_send(Activity a, List<App1Dokumenti> spisakDokumentaZaSync, boolean sendVipsID, int vrstaAplikacije) {
        spisakDokumenta = spisakDokumentaZaSync;
        myActivity=a;
        this.sendVipsID = sendVipsID;
        this.vrstaAplikacije = vrstaAplikacije;
    }

    protected void onPreExecute() {
        progressDialog = new ProgressDialog(myActivity);
        progressDialog = new ProgressDialog(myActivity);
        String poruka = myActivity.getApplicationContext().getResources().getString(R.string.Poruka_app_molimPricekajte);
        progressDialog.setMessage(poruka);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    protected String doInBackground(String... params) {
        String httpResponse = null;
        String JsonDATA = mojJson().toString();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://vanima.net:8099/api/dokumentirazmjena?d=" + MainActivity.getDjelatnik());
            Log.d(TAG, "doInBackground: URL=" + url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            // is output buffer writter
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            //set headers and method
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(JsonDATA);
            Log.d(TAG, "doInBackground: JSON=" + JsonDATA);
            // json data
            writer.close();

            Log.d(TAG, "doInBackground: Rezultat je " + urlConnection.getResponseCode());
            int a = urlConnection.getResponseCode();
            if (a == 200) {
                httpResponse = "OK";
            } else {
                httpResponse = "ERROR";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return httpResponse;
    }

    private JSONObject mojJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            postavkeAplikacije pa=new postavkeAplikacije(myActivity);

            jsonObject.put("UredjajId", MainActivity.UREDJAJ);
            jsonObject.put("DjelatnikId", MainActivity.getDjelatnik());
            jsonObject.put("VrstaAplikacije", vrstaAplikacije);

            JSONArray jsonArrayDokumenti = new JSONArray();
            for (App1Dokumenti dokument : spisakDokumenta) {
                JSONObject jsonDokument = new JSONObject();
                jsonDokument.put("ID", dokument.getId());
                jsonDokument.put("KasaID", pa.getKasaId());
                jsonDokument.put("PodtipId", dokument.getIdPodtip());
                jsonDokument.put("PjFrmId", pa.getPjFrmId());
                jsonDokument.put("PjKmtId", dokument.getIdPjKomitenta());
                jsonDokument.put("DatumDokumenta", dokument.getDatumDokumentaJSONString());
                jsonDokument.put("KomercijalistID", MainActivity.getDjelatnik());
                jsonDokument.put("NacinPlacanjaId", dokument.getIdNacinPlacanja());
                jsonDokument.put("Opaska", dokument.getNapomena());
                if (sendVipsID) {
                    jsonDokument.put("vipsID", dokument.getVipsId());
                }

                JSONArray jsonArray = new JSONArray();
                for (App1Stavke stavka : dokument.getSpisakStavki()) {
                    JSONObject jsonStavka = new JSONObject();
                    jsonStavka.put("ID", stavka.getId());
                    jsonStavka.put("ZaglavljeId", dokument.getId());
                    if (vrstaAplikacije == 2) {
                        jsonStavka.put("Rbr", stavka.getRbr());
                    } else {
                        jsonStavka.put("Rbr", 0);
                    }

                    jsonStavka.put("ArtiklId", stavka.getArtiklId());
                    jsonStavka.put("JmjId", stavka.getJmjId());
                    jsonStavka.put("VrijednostId1", stavka.getAtributId());
                    jsonStavka.put("Kolicina", stavka.getKolicina());
                    jsonStavka.put("Opaska", stavka.getNapomena());
                    if (sendVipsID) {
                        jsonStavka.put("vipsID", stavka.getVipsID());
                    }
                    jsonArray.put(jsonStavka);
                }

                jsonDokument.put("Stavke", jsonArray);
                jsonArrayDokumenti.put(jsonDokument);
            }
            jsonObject.put("Dokumenti", jsonArrayDokumenti);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(String result) {
        if (TextUtils.isEmpty(result)) {
            Log.d(TAG, "onPostExecute: PRAZAN STRING");
            progressDialog.dismiss();
        } else {
            Log.d(TAG, "onPostExecute: " + result);
            if (result.equals("OK")) {
                //MainActivity.updateZaglavljaPoslijeSinkronizacije(myActivity,spisakDokumenta);
            }
            progressDialog.dismiss();
        }

    }
}
