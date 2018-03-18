package com.example.marko.vips_artikli;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText txtName = (EditText) findViewById(R.id.txtUserName_Prijava);
        final EditText txtPass = (EditText) findViewById(R.id.txtLozinka_Prijava);

        final Button btnPrijava = (Button) findViewById(R.id.btnPrijava_Prijava);
        btnPrijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String akcija = "prijavaKorisnika";
                String urlString = MainActivity.url + "auth?username=" + txtName.getText().toString() + "" + "&password=" + txtPass.getText().toString();
                //url = "http://vanima.net:8099/api/"
                //api/auth?username=nikola&password=1111111
                //spisakSyncTabela.add(new MainActivity.UrlTabele(akcija, urlString, true, "jmj"));
                MainActivity.UrlTabele newUrl = new MainActivity.UrlTabele(akcija, urlString, true, "");

                JSON_recive newJSON = (JSON_recive) new JSON_recive(LoginActivity.this, newUrl, "") {
                    @Override
                    public void onResponseReceived(int result) {
                        //btnPrijava.setText(Integer.toString(result));
                        if (result > 0) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("dlt_id", result);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }
                }.execute(newUrl.urlTabele, newUrl.Akcija);

            }
        });

    }
}