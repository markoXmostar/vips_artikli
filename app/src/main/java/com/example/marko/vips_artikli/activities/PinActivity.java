package com.example.marko.vips_artikli.activities;

import android.Manifest;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.vips_artikli.utils.FingerprintHandler;
import com.example.marko.vips_artikli.R;
import com.example.marko.vips_artikli.utils.postavkeAplikacije;

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
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnDellAll, btnDell1;
    static String requiredPin;
    private static final String KEY_NAME = "fingerprintKeyName";
    private Cipher cipher;
    private KeyStore keyStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        requiredPin = new postavkeAplikacije(PinActivity.this).getPin();
        txtPin = (EditText) findViewById(R.id.txtPass_pin);
        postaviDugmice();
        Button btnOK = findViewById(R.id.btnOK_pin);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = txtPin.getText().toString();
                postavkeAplikacije myPOstavke = new postavkeAplikacije(PinActivity.this);
                Log.d(TAG, "onClick: UPSIAN JE PIN=" + pin);
                Log.d(TAG, "onClick: TREBA BITI PIN=" + myPOstavke.getPin());
                if (pin.equals(myPOstavke.getPin())) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                } else {
                    brojPokusaja += 1;
                    TextView txtBrojPokusaja = (TextView) findViewById(R.id.txtBrojPokusaja);
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

        // If you’ve set your app’s minSdkVersion to anything lower than 23, then you’ll need to verify that the device is running Marshmallow
        // or higher before executing any fingerprint-related code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Get an instance of KeyguardManager and FingerprintManager//
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            //Check whether the device has a fingerprint sensor//
            if (!fingerprintManager.isHardwareDetected()) {
                // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
                txtPin.setCompoundDrawables(null,null,null,null);
            }
            //Check whether the user has granted your app the USE_FINGERPRINT permission//
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                // If your app doesn't have this permission, then display the following text//
                txtPin.setCompoundDrawables(null,null,null,null);
            }

            //Check that the user has registered at least one fingerprint//
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                // If the user hasn’t configured any fingerprints, then display the following message//
                txtPin.setCompoundDrawables(null,null,null,null);
            }

            //Check that the lockscreen is secured//
            if (!keyguardManager.isKeyguardSecure()) {
                // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
                txtPin.setCompoundDrawables(null,null,null,null);
            } else {
                try {
                    generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {
                    //If the cipher is initialized successfully, then create a CryptoObject instance//
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);

                    // Here, I’m referencing the FingerprintHandler class that we’ll create in the next section. This class will be responsible
                    // for starting the authentication process (via the startAuth method) and processing the authentication process events//
                    FingerprintHandler helper = new FingerprintHandler(this);
                    helper.startAuth(fingerprintManager, cryptoObject, txtPin, btnOK, requiredPin);

                }
            }
        }
    }
    public static void fingerprintSuccessful(EditText txtPinField, Button enterButton, String pinToEnter){
        txtPinField.setText(pinToEnter);
        enterButton.callOnClick();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKey() throws FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key//
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore//
            keyStore.load(null);

            //Initialize the KeyGenerator//
            keyGenerator.init(new

                    //Specify the operation(s) this key can be used for//
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Generate the key//
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    //Create a new method that we’ll use to initialize our cipher//
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class FingerprintException extends Exception {
        private FingerprintException(Exception e) {
            super(e);
        }
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
