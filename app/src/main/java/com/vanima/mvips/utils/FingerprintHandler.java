package com.vanima.mvips.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    Button btnOK;
    String requiredPin;
    EditText txtPin;
    Context pinActivityContext;

    public FingerprintHandler(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject, Button btnOK, String requiredPin, EditText txtPin, Context context){

        this.btnOK =  btnOK;
        this.requiredPin = requiredPin;
        this.txtPin = txtPin;
        this.pinActivityContext = context;

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Toast.makeText(pinActivityContext, "Authentication error.\n" + errString,Toast.LENGTH_SHORT);
        txtPin.setCompoundDrawables(null,null,null,null);
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(pinActivityContext, "Authentication failed.",Toast.LENGTH_SHORT);
        txtPin.setCompoundDrawables(null,null,null,null);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Toast.makeText(pinActivityContext, helpString,Toast.LENGTH_SHORT);
        txtPin.setCompoundDrawables(null,null,null,null);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        txtPin.setText(requiredPin);
        btnOK.callOnClick();
    }
}