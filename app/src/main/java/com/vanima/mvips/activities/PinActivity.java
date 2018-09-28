package com.vanima.mvips.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class PinActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    int brojPokusaja = 1;
    private String TAG = "PIN_ACTIVITY";
    EditText txtPin;
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnDellAll, btnDell1, btnOK;
    static String requiredPin;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        requiredPin = new postavkeAplikacije(PinActivity.this).getPin();
        txtPin = findViewById(R.id.txtPass_pin);

        postaviDugmice();

        btnOK = findViewById(R.id.btnOK_pin);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = txtPin.getText().toString();
                postavkeAplikacije myPOstavke = new postavkeAplikacije(PinActivity.this);
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

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {

                Log.i(TAG, "No fingerprint hardware");
                txtPin.setCompoundDrawables(null,null,null,null);

            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "No fingerprint permission");
                txtPin.setCompoundDrawables(null,null,null,null);

            } else if (!keyguardManager.isKeyguardSecure()) {

                Log.i(TAG, "Fingerprint not used");
                txtPin.setCompoundDrawables(null,null,null,null);

            } else if (!fingerprintManager.hasEnrolledFingerprints()) {

                Log.i(TAG, "No fingerprints");
                txtPin.setCompoundDrawables(null,null,null,null);

            } else {

                generateKey();

                if (cipherInit()) {

                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerprintHandler fingerprintHandler = new FingerprintHandler(this);
                    fingerprintHandler.startAuth(fingerprintManager, cryptoObject, btnOK, requiredPin, txtPin, this);

                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {

        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        } catch (KeyStoreException | IOException | CertificateException
                | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | NoSuchProviderException e) {

            e.printStackTrace();

        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {

            keyStore.load(null);

            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return true;

        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }

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
