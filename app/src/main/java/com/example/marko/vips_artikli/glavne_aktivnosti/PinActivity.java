package com.example.marko.vips_artikli.glavne_aktivnosti;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marko.vips_artikli.R;
import com.example.marko.vips_artikli.postavkeAplikacije;

public class PinActivity extends AppCompatActivity {
    private EditText txtPin;
    boolean doubleBackToExitPressedOnce = false;
    int brojPokusaja = 1;
    private String TAG = "PIN_ACTIVITY";
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnDellAll, btnDell1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        txtPin = (EditText) findViewById(R.id.txtPass_pin);
        postaviDugmice();
        Button btnOK = (Button) findViewById(R.id.btnOK_pin);
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


    }

    private void postaviDugmice() {
        btn1 = (Button) findViewById(R.id.btn1_pin);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: 1");
                txtPin.setText(txtPin.getText().toString() + "1");
            }
        });
        btn2 = (Button) findViewById(R.id.btn2_pin);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: 2");
                txtPin.setText(txtPin.getText().toString() + "2");
            }
        });
        btn3 = (Button) findViewById(R.id.btn3_pin);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "3");
            }
        });
        btn4 = (Button) findViewById(R.id.btn4_pin);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "4");
            }
        });
        btn5 = (Button) findViewById(R.id.btn5_pin);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "5");
            }
        });
        btn6 = (Button) findViewById(R.id.btn6_pin);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "6");
            }
        });
        btn7 = (Button) findViewById(R.id.btn7_pin);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "7");
            }
        });
        btn8 = (Button) findViewById(R.id.btn8_pin);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "8");
            }
        });
        btn9 = (Button) findViewById(R.id.btn9_pin);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "9");
            }
        });
        btn0 = (Button) findViewById(R.id.btn0_pin);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText(txtPin.getText().toString() + "0");
            }
        });
        btnDellAll = (Button) findViewById(R.id.btnDelAll_pin);
        btnDellAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtPin.setText("");
            }
        });
        btnDell1 = (Button) findViewById(R.id.btnDelOne_pin);
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
