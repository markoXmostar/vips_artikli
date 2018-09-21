package com.example.marko.vips_artikli.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.vips_artikli.utils.JSON_recive;
import com.example.marko.vips_artikli.R;

public class LoginActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText txtName = findViewById(R.id.txtUserName_Prijava);
        final EditText txtPass = findViewById(R.id.txtLozinka_Prijava);

        CheckBox passwordVisibleCheck = findViewById(R.id.showPassword);

        final Button btnPrijava = findViewById(R.id.btnPrijava_Prijava);

        passwordVisibleCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    txtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    txtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        txtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnPrijava.callOnClick();
                }
                return false;
            }
        });

        btnPrijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String akcija = "prijavaKorisnika";
                String urlString = MainActivity.url + "auth?username=" + txtName.getText().toString() + "" + "&password=" + txtPass.getText().toString();
                //url = "http://vanima.net:8099/api/"
                //api/auth?username=nikola&password=1111111
                //spisakSyncTabela.add(new MainActivity.UrlTabele(akcija, urlString, true, "jmj"));
                MainActivity.UrlTabele newUrl = new MainActivity.UrlTabele(akcija, urlString, true, "");
                Activity a = LoginActivity.this;
                int rbrLOG = MainActivity.getZadnjiID_log("log", a);

                @SuppressLint("StaticFieldLeak") JSON_recive newJSON = (JSON_recive) new JSON_recive(LoginActivity.this, newUrl, "", rbrLOG) {
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
    // čeka na dvostruki klik nazad da ne bi bilo slučajnih izlaza iz aplikacije
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.pritisnite_natrag_još_jednom_exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
