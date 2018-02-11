package com.example.marko.vips_artikli;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * Created by marko on 11.2.2018..
 */

public class SendJSON extends AsyncTask<String, String, String> {

    private static String TAG="SendData_JSON";

    List<App1Dokumenti> spisakDokumenta;
    public SendJSON(List<App1Dokumenti> spisakDokumentaZaSync){
        spisakDokumenta=spisakDokumentaZaSync;
    }
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            URL url = new URL("http://vanima.net:8099/api/dokumentirazmjena"); //Enter URL here
            Log.d(TAG, "doInBackground: " +url.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UredjajId", 0);
            jsonObject.put("DjelatnikId", MainActivity.DJELATNIK);

            JSONArray jsonArrayDokumenti = new JSONArray();
            for (App1Dokumenti dokument:spisakDokumenta) {
                JSONObject jsonDokument=new JSONObject();
                jsonDokument.put("ID",dokument.getId());
                jsonDokument.put("KasaID", 0);
                jsonDokument.put("PodtipId", dokument.getIdPodtip());
                jsonDokument.put("PjFrmId",0);
                jsonDokument.put("PjKmtId",dokument.getIdPjKomitenta());
                jsonDokument.put("DatumDokumenta",dokument.getDatumDokumentaJSONString());
                jsonDokument.put("KomercijalistID",0);
                jsonDokument.put("NacinPlacanjaId",0);
                jsonDokument.put("Opaska",dokument.getNapomena());

                JSONArray jsonArray = new JSONArray();
                for (App1Stavke stavka:dokument.getSpisakStavki()) {
                    JSONObject jsonStavka=new JSONObject();
                    jsonStavka.put("ID",stavka.getId());
                    jsonStavka.put("ZaglavljeId",dokument.getId());
                    jsonStavka.put("Rbr",0);
                    jsonStavka.put("ArtiklId",stavka.getArtiklId());
                    jsonStavka.put("JmjId",stavka.getJmjId());
                    jsonStavka.put("VrijednostId1",stavka.getAtributVrijednost());
                    jsonStavka.put("Kolicina",stavka.getKolicina());
                    jsonStavka.put("Opaska",stavka.getNapomena());
                    jsonArray.put(jsonStavka);
                }

                jsonDokument.put("Stavke", jsonArray);
                jsonArrayDokumenti.put(jsonDokument);
            }
            jsonObject.put("Dokumenti",jsonArrayDokumenti);
            String jsonStr = jsonObject.toString();
            Log.d(TAG, "doInBackground: " + jsonStr);


            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            wr.flush();
            wr.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute: " + result);
    }
}
