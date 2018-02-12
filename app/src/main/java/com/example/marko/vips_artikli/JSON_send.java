package com.example.marko.vips_artikli;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

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

    public JSON_send(Activity a,List<App1Dokumenti> spisakDokumentaZaSync) {
        spisakDokumenta = spisakDokumentaZaSync;
        myActivity=a;
    }

    protected void onPreExecute() {
        progressDialog = new ProgressDialog(myActivity);
        progressDialog = new ProgressDialog(myActivity);
        String poruka =(String) myActivity.getApplicationContext().getResources().getString(R.string.Poruka_app_molimPricekajte);
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
            URL url = new URL("http://vanima.net:8099/api/dokumentirazmjena?d=" + MainActivity.DJELATNIK);
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
            jsonObject.put("UredjajId", 0);
            jsonObject.put("DjelatnikId", MainActivity.DJELATNIK);

            JSONArray jsonArrayDokumenti = new JSONArray();
            for (App1Dokumenti dokument : spisakDokumenta) {
                JSONObject jsonDokument = new JSONObject();
                jsonDokument.put("ID", dokument.getId());
                jsonDokument.put("KasaID", 0);
                jsonDokument.put("PodtipId", dokument.getIdPodtip());
                jsonDokument.put("PjFrmId", 0);
                jsonDokument.put("PjKmtId", dokument.getIdPjKomitenta());
                jsonDokument.put("DatumDokumenta", dokument.getDatumDokumentaJSONString());
                jsonDokument.put("KomercijalistID", 0);
                jsonDokument.put("NacinPlacanjaId", 0);
                jsonDokument.put("Opaska", dokument.getNapomena());

                JSONArray jsonArray = new JSONArray();
                for (App1Stavke stavka : dokument.getSpisakStavki()) {
                    JSONObject jsonStavka = new JSONObject();
                    jsonStavka.put("ID", stavka.getId());
                    jsonStavka.put("ZaglavljeId", dokument.getId());
                    jsonStavka.put("Rbr", 0);
                    jsonStavka.put("ArtiklId", stavka.getArtiklId());
                    jsonStavka.put("JmjId", stavka.getJmjId());
                    jsonStavka.put("VrijednostId1", stavka.getAtributVrijednost());
                    jsonStavka.put("Kolicina", stavka.getKolicina());
                    jsonStavka.put("Opaska", stavka.getNapomena());
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
        Log.d(TAG, "onPostExecute: " + result);
        if (result.equals("OK")) {
            MainActivity.updateZaglavljaPoslijeSinkronizacije(myActivity,spisakDokumenta);
        }
        progressDialog.dismiss();
    }
}
