package com.vanima.mvips.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vanima.mvips.R;
import com.vanima.mvips.utils.FingerprintHandler;
import com.vanima.mvips.utils.postavkeAplikacije;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class PinActivityLowAPI extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    int brojPokusaja = 1;
    private String TAG = "PIN_ACTIVITY";
    EditText txtPin;
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnDellAll, btnDell1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        txtPin = findViewById(R.id.txtPass_pin);

        postaviDugmice();

        Button btnOK = findViewById(R.id.btnOK_pin);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = txtPin.getText().toString();
                postavkeAplikacije myPOstavke = new postavkeAplikacije(PinActivityLowAPI.this);
                //Log.d(TAG, "onClick: UPSIAN JE PIN=" + pin);
                //Log.d(TAG, "onClick: TREBA BITI PIN=" + myPOstavke.getPin());
                if (pin.equals(myPOstavke.getPin())) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                } else {
                    brojPokusaja += 1;
                    TextView txtBrojPokusaja = findViewById(R.id.txtBrojPokusaja);
                    txtPin.setText("");
                    if (brojPokusaja > 3) {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();
                    }
                    txtBrojPokusaja.setText(Integer.toString(brojPokusaja));
                }

            }
        });
    }

    private void postaviDugmice() {
        btn1 = findViewById(R.id.btn1_pin);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: 1");
                txtPin.setText(txtPin.getText().toString() + "1");
            }
        });
        btn2 = findViewById(R.id.btn2_pin);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: 2");
                txtPin.setText(txtPin.getText().toString() + "2");
            }
        });
        btn3 = findViewById(R.id.btn3_pin);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "3");
            }
        });
        btn4 = findViewById(R.id.btn4_pin);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "4");
            }
        });
        btn5 = findViewById(R.id.btn5_pin);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "5");
            }
        });
        btn6 = findViewById(R.id.btn6_pin);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "6");
            }
        });
        btn7 = findViewById(R.id.btn7_pin);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "7");
            }
        });
        btn8 = findViewById(R.id.btn8_pin);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "8");
            }
        });
        btn9 = findViewById(R.id.btn9_pin);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "9");
            }
        });
        btn0 = findViewById(R.id.btn0_pin);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "0");
            }
        });
        btnDellAll = findViewById(R.id.btnDelAll_pin);
        btnDellAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText("");
            }
        });
        btnDell1 = findViewById(R.id.btnDelOne_pin);
        btnDell1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subStr = txtPin.getText().toString();
                if (subStr.length() > 0) {
                    String newSubStr = subStr.substring(0, subStr.length() - 1);
                    txtPin.setText(newSubStr);
                }
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
