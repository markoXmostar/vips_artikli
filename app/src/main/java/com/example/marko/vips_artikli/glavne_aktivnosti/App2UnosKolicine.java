package com.example.marko.vips_artikli.glavne_aktivnosti;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.marko.vips_artikli.R;
import com.example.marko.vips_artikli.dataclass.App2Stavke;


public class App2UnosKolicine extends AppCompatActivity {

    private App2Stavke myStv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app2_unos_kolicine);

        Bundle b = getIntent().getExtras();

        myStv = (App2Stavke) b.getSerializable("stavka");

        TextView txtSifraNaziv = (TextView) findViewById(R.id.txtNazivArtikla_App2UnosKolicine);
        TextView txtJmj = (TextView) findViewById(R.id.txtJmj_App2UnosKolicine);
        TextView txtZadanaKolicina = (TextView) findViewById(R.id.txtZadanaKolicina_App2UnosKolicine);

        txtSifraNaziv.setText("Šifra artikla: " + myStv.getArtiklSifra() + "\n" + "Naziv artikla: " + myStv.getArtiklNaziv());
        txtJmj.setText("JEDINICA MJERE: " + myStv.getJmjNaziv());
        txtZadanaKolicina.setText("ZADANA KOLIČINA: " + String.valueOf(myStv.getKolicinaZadana()));
        final EditText etxtKolicina = (EditText) findViewById(R.id.etxtKolicina_App2UnosKolicine);

        etxtKolicina.setText(String.valueOf(myStv.getKolicina()));
        etxtKolicina.setSelectAllOnFocus(true);
        etxtKolicina.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Button btnPostavi = (Button) findViewById(R.id.btnPopuni_App2UnosKolicine);
        btnPostavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etxtKolicina.setText(String.valueOf(myStv.getKolicinaZadana()));
            }
        });

        Button btnOk = (Button) findViewById(R.id.btnOK_App2UnosKolicine);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                App2Stavke newStavka = myStv;
                double novaKolicina = Double.parseDouble(etxtKolicina.getText().toString());
                newStavka.setKolicina(novaKolicina);
                returnIntent.putExtra("stavka", newStavka);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        Button btnCancel = (Button) findViewById(R.id.btnOdustani_App2UnosKolicine);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

    }
}
