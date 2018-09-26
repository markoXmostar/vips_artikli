package com.vanima.mvips.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.vanima.mvips.models.App1Dokumenti;
import com.vanima.mvips.models.Komitent;
import com.vanima.mvips.models.NacinPlacanja;
import com.vanima.mvips.models.PjKomitent;
import com.vanima.mvips.models.PodtipDokumenta;
import com.vanima.mvips.R;
import com.vanima.mvips.models.TipDokumenta;
import com.vanima.mvips.utils.postavkeAplikacije;

import java.util.Calendar;


public class App1UnosZaglavljaActivity extends AppCompatActivity {

    private static final String TAG="App1Zaglavlje";
    TextView txtDatumDokumenta, txtKomitent, txtPjKomitenta, labelPjKomitenta, txtTipDokumenta, txtPodtipDokumenta, labelPodtipDokumenta, txtSaldoKupca, lblVrstaPlacanja;
    //TextView lblSaldoKupca;
    Spinner spinVrstaPlacanja;
    EditText etxtNapomena;
    Button btnOk,btnCancel;

    private Menu myMenu;

    Calendar kalendar;
    int dan,mjesec,godina;

    private String izabraniDatum;

    private Komitent izabraniKomitent;

    private int akcija=0;//defoltno novi akcije kao u vips-u
    private long dokumentID=0;
    private String napomena="";


    public Komitent getIzabraniKomitent() {
        return izabraniKomitent;
    }
    public void setIzabraniKomitent(Komitent new_izabraniKomitent) {
        izabraniKomitent = new_izabraniKomitent;
        double saldo, uroku, vanroka;

        if (new_izabraniKomitent!=null){
            txtKomitent.setText(izabraniKomitent.getNaziv());

            txtPjKomitenta.setVisibility(View.VISIBLE);
            labelPjKomitenta.setVisibility(View.VISIBLE);

            txtPjKomitenta.setEnabled(true);
            labelPjKomitenta.setEnabled(true);

            if (vrstaAplikacije == 3) {
                txtSaldoKupca.setVisibility(View.VISIBLE);
                //lblSaldoKupca.setVisibility(View.VISIBLE);
                saldo = new_izabraniKomitent.getSaldo();
                uroku = new_izabraniKomitent.getuRoku();
                vanroka = new_izabraniKomitent.getVanRoka();
                //if (saldo > 0) {
                    String txtSaldo = "Saldo = " + String.valueOf(saldo) + "\n" + "U roku = " + String.valueOf(uroku) + "\n" + "Van roka = " + String.valueOf(vanroka);
                    txtSaldoKupca.setText(txtSaldo);
                //} else {
                //    txtSaldoKupca.setText(String.valueOf(saldo));
                //}
            }
            //ovdje treba dodati da se automatski dodaje PJ ukoliko je sama
            txtPjKomitenta.callOnClick();
        }
        else {
            txtPjKomitenta.setVisibility(View.GONE);
            labelPjKomitenta.setVisibility(View.GONE);
            txtPjKomitenta.setEnabled(false);
            labelPjKomitenta.setEnabled(false);
            if (vrstaAplikacije == 3) {
                txtSaldoKupca.setVisibility(View.GONE);
                //lblSaldoKupca.setVisibility(View.GONE);
                txtSaldoKupca.setText(String.valueOf(0));
            }
        }
        setIzabranaPJKomitenta(null);
    }

    public PjKomitent getIzabranaPJKomitenta() {
        return izabranaPJKomitenta;
    }

    public void setIzabranaPJKomitenta(PjKomitent izabranaPJKomitenta) {
        this.izabranaPJKomitenta = izabranaPJKomitenta;
        if (izabranaPJKomitenta!=null){
            txtPjKomitenta.setText(getIzabranaPJKomitenta().getNaziv());
        }
        else {
            txtPjKomitenta.setText(getString(R.string.Izaberi));
        }
    }

    private PjKomitent izabranaPJKomitenta;


    public TipDokumenta getIzabraniTiP() {
        return izabraniTiP;
    }

    public void setIzabraniTiP(TipDokumenta izabraniTiP) {
        this.izabraniTiP = izabraniTiP;
        izabranPodtip = null;
        if(izabraniTiP!=null){
            txtTipDokumenta.setText(izabraniTiP.getNaziv());
            labelPodtipDokumenta.setEnabled(true);
            txtPodtipDokumenta.setEnabled(true);
            labelPodtipDokumenta.setVisibility(View.VISIBLE);
            txtPodtipDokumenta.setVisibility(View.VISIBLE);
            txtPodtipDokumenta.callOnClick();
        }else{
            //txtTipDokumenta.setText(Izaberi);
            labelPodtipDokumenta.setEnabled(false);
            txtPodtipDokumenta.setEnabled(false);
            labelPodtipDokumenta.setVisibility(View.GONE);
            txtPodtipDokumenta.setVisibility(View.GONE);
        }
        setIzabranPodtip(null);
    }

    private TipDokumenta izabraniTiP;

    public PodtipDokumenta getIzabraniPodtip() {
        return izabranPodtip;

    }

    public void setIzabranPodtip(PodtipDokumenta izabranPodtip) {
        this.izabranPodtip = izabranPodtip;
        if(izabranPodtip!=null){
            txtPodtipDokumenta.setText(izabranPodtip.getNaziv());
        }else{
            txtPodtipDokumenta.setText(getString(R.string.Izaberi));
        }
    }

    private PodtipDokumenta izabranPodtip;

    postavkeAplikacije myPostavke;
    int vrstaAplikacije = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app1_unos_zaglavlje);

        txtDatumDokumenta =(TextView)findViewById(R.id.txtDatumDokumenta_App1Zaglavlje);
        txtKomitent=(TextView)findViewById(R.id.txtKomitent_App1Zaglavlje);
        txtPjKomitenta=(TextView)findViewById(R.id.txtPjKomitenta_App1Zaglavlje);
        labelPjKomitenta=(TextView)findViewById(R.id.labelPjKomitenta_App1Zaglavlje);
        txtTipDokumenta=(TextView)findViewById(R.id.txtTipDokumenta_App1Zaglavlje);
        txtPodtipDokumenta=(TextView)findViewById(R.id.txtPodtipDokumenta_App1Zaglavlje);
        labelPodtipDokumenta=(TextView)findViewById(R.id.labelPodtipDokumenta_App1Zaglavlje);
        btnOk=(Button)findViewById(R.id.btnOK_App1ZaglavljeDokumenata);
        btnCancel=(Button)findViewById(R.id.btnCancel_App1ZaglavljeDokumenata);
        etxtNapomena = (EditText) findViewById(R.id.etxtNapomena_App1Zaglavlje);

        txtSaldoKupca = (TextView) findViewById(R.id.txtSaldoKupca_App1Zaglavlje);
        //lblSaldoKupca = (TextView) findViewById(R.id.labelSaldoKupca_App1Zaglavlje);
        lblVrstaPlacanja = (TextView) findViewById(R.id.labelVrstaPlacanja_App1Zaglavlje);
        spinVrstaPlacanja = (Spinner) findViewById(R.id.spinVrstaPlacanja_App1Zaglavlje);

        Bundle b = getIntent().getExtras();
        vrstaAplikacije = b.getInt("vrstaAplikacije", 1);
        akcija=b.getInt("akcija",0);
        dokumentID=b.getLong("dokumentID",0);
        napomena=b.getString("napomena","");

        if (vrstaAplikacije != 3) {
            txtSaldoKupca.setVisibility(View.GONE);
            //lblSaldoKupca.setVisibility(View.GONE);
            lblVrstaPlacanja.setVisibility(View.GONE);
            spinVrstaPlacanja.setVisibility(View.GONE);
        } else {
            ArrayAdapter<NacinPlacanja> nacinPlacanjaArrayAdapter = new ArrayAdapter<NacinPlacanja>(App1UnosZaglavljaActivity.this, R.layout.spinner_item_mydialog);
            for (NacinPlacanja podtip : MainActivity.getListaNacinaPlacanja(App1UnosZaglavljaActivity.this, "")) {
                nacinPlacanjaArrayAdapter.add(podtip);
            }
            nacinPlacanjaArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_mydialog);
            spinVrstaPlacanja.setAdapter(nacinPlacanjaArrayAdapter);

        }

        setIzabraniKomitent(null);
        myPostavke = new postavkeAplikacije(this);

        long zadaniTipDokumenta = myPostavke.getTipDokumenta();
        TipDokumenta tipDok = MainActivity.getTipDokumentaByID(this, zadaniTipDokumenta);

        setIzabraniTiP(tipDok);
        // dopuštenje za izmjenu tipa dokumenta!
        txtTipDokumenta.setEnabled(myPostavke.isDopustenaIzmjenaTipaDokumenta());


        if (myPostavke.getPodtipDokumenta() > 0) {
            PodtipDokumenta podtipDokumenta = MainActivity.getPodtipDokumentaByID(this, myPostavke.getPodtipDokumenta());
            setIzabranPodtip(podtipDokumenta);
        }

        if (akcija==0){
            kalendar=Calendar.getInstance();
            dan=kalendar.get(Calendar.DAY_OF_MONTH);
            mjesec=kalendar.get(Calendar.MONTH);
            godina=kalendar.get(Calendar.YEAR);
            izabraniDatum=MainActivity.danMjesecGodinaToFormatString(dan,mjesec,godina);
            txtDatumDokumenta.setText(izabraniDatum);
        }


        //ovdje za izmejnu se upisuju podaci
        if (akcija==1){
            //izmjena
            App1Dokumenti izabraniDokument=MainActivity.getDokumentApp1_ByID(this,dokumentID);

            kalendar=Calendar.getInstance();
            kalendar.setTime(izabraniDokument.getDatumDokumenta());
            dan=kalendar.get(Calendar.DAY_OF_MONTH);
            mjesec=kalendar.get(Calendar.MONTH);
            godina=kalendar.get(Calendar.YEAR);
            izabraniDatum=MainActivity.danMjesecGodinaToFormatString(dan,mjesec,godina);
            txtDatumDokumenta.setText(izabraniDatum);
            etxtNapomena.setText(napomena);

            Komitent myKom=MainActivity.getKomitentByID(this,izabraniDokument.getIdKomitent());
            setIzabraniKomitent(myKom);

            PjKomitent myPjKom=MainActivity.getPjKomitentByID(this,izabraniDokument.getIdPjKomitenta());
            setIzabranaPJKomitenta(myPjKom);

            TipDokumenta myTip=MainActivity.getTipDokumentaByID(this,izabraniDokument.getIdTip());
            setIzabraniTiP(myTip);

            PodtipDokumenta myPodtip=MainActivity.getPodtipDokumentaByID(this,izabraniDokument.getIdPodtip());
            setIzabranPodtip(myPodtip);

        }
        txtDatumDokumenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(App1UnosZaglavljaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int godina, int mjesec, int dan) {
                        izabraniDatum=MainActivity.danMjesecGodinaToFormatString(dan,mjesec,godina);
                        txtDatumDokumenta.setText(izabraniDatum);
                    }
                },godina,mjesec,dan);
                datePickerDialog.show();
            }
        });

        txtKomitent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(App1UnosZaglavljaActivity.this, PretragaKomitenata_Tipova_Podtipova_PJKomitenata_Activity.class);
                i.putExtra("varijanta", "komitenti");
                Log.d(TAG, "onClick: PUT EXTRA varijanta komitenti" );
                startActivityForResult(i, 1);
            }
        });

        txtPjKomitenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(App1UnosZaglavljaActivity.this, PretragaKomitenata_Tipova_Podtipova_PJKomitenata_Activity.class);
                i.putExtra("varijanta", "pjkomitenti");
                Log.d(TAG, "onClick: PUT EXTRA varijanta pjkomitenti" );
                Komitent izabran=getIzabraniKomitent();
                i.putExtra("idKomitenta", izabran.getId());
                Log.d(TAG, "onClick: PUT EXTRA KomitentID= " +izabran.getId());
                startActivityForResult(i, 2);
            }
        });

        txtTipDokumenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(App1UnosZaglavljaActivity.this, PretragaKomitenata_Tipova_Podtipova_PJKomitenata_Activity.class);
                i.putExtra("varijanta", "tipDokumenta");
                Log.d(TAG, "onClick: PUT EXTRA varijanta TIP Dokumenta" );
                startActivityForResult(i, 3);
            }
        });

        txtPodtipDokumenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(App1UnosZaglavljaActivity.this, PretragaKomitenata_Tipova_Podtipova_PJKomitenata_Activity.class);
                i.putExtra("varijanta", "podtipDokumenta");
                Log.d(TAG, "onClick: PUT EXTRA varijanta podtipDokumenta" );
                TipDokumenta izabran=getIzabraniTiP();
                i.putExtra("idTipDokumenta", izabran.getId());
                Log.d(TAG, "onClick: PUT EXTRA TipDokumentaID= " +izabran.getId());
                startActivityForResult(i, 4);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (izabraniKomitent==null || izabraniTiP==null || izabranaPJKomitenta==null || izabranPodtip==null){
                    String poruka= getResources().getString(R.string.NepotpunUnos);
                    Snackbar.make(view, poruka  , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("vrstaAplikacije", vrstaAplikacije);
                returnIntent.putExtra("akcija", akcija);
                returnIntent.putExtra("dokumentID", dokumentID);
                returnIntent.putExtra("idKomitenta",getIzabraniKomitent().getId());
                returnIntent.putExtra("idPjKomitenta",getIzabranaPJKomitenta().getId());
                returnIntent.putExtra("idTipDokumenta",getIzabraniTiP().getId());
                returnIntent.putExtra("idPodtipDokumenta", getIzabraniPodtip().getId());
                returnIntent.putExtra("datumDokumenta", izabraniDatum);
                returnIntent.putExtra("nazivKomitenta",getIzabraniKomitent().getNaziv());
                returnIntent.putExtra("nazivPjKomitenta",getIzabranaPJKomitenta().getNaziv());
                returnIntent.putExtra("nazivTipDokumenta",getIzabraniTiP().getNaziv());
                returnIntent.putExtra("nazivPodtipDokumenta", getIzabraniPodtip().getNaziv());
                if (vrstaAplikacije == 3) {
                    NacinPlacanja np = (NacinPlacanja) spinVrstaPlacanja.getSelectedItem();
                    returnIntent.putExtra("idVrstaPlacanja", np.getId());
                    returnIntent.putExtra("nazivVrstaPlacanja", np.getNaziv());
                } else {
                    returnIntent.putExtra("idVrstaPlacanja", 0);
                    returnIntent.putExtra("nazivVrstaPlacanja", "");
                }

                returnIntent.putExtra("napomena",etxtNapomena.getText().toString());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                Komitent myKomitent = (Komitent) data.getSerializableExtra("komitent");
                setIzabraniKomitent(myKomitent);

                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.unos_stavke, getMenu());

            }
            if (resultCode == Activity.RESULT_CANCELED) {

                setIzabraniKomitent(null);

            }
        }
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                Long idPjKom=data.getLongExtra("idPjKomitenta",0);
                String nazivPjKom=data.getStringExtra("nazivPjKomitenta");
                Long ridPjKom=data.getLongExtra("ridPjKomitenta",0);

                Log.d(TAG, "onActivityResult: vraćeno je:::> Naziv Pj komitenta=" + nazivPjKom);
                Log.d(TAG, "onActivityResult: vraćeno je:::> rid komitenta=" + ridPjKom);
                Log.d(TAG, "onActivityResult: vraćeno je:::> ID Pj komitenta=" + idPjKom.toString());

                PjKomitent myPjKomitent=new PjKomitent(idPjKom,nazivPjKom,ridPjKom);
                setIzabranaPJKomitenta(myPjKomitent);
                //setIzabraniKomitent(myPjKomitent);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setIzabranaPJKomitenta(null);
            }
        }
        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                Long idTip=data.getLongExtra("idTipDokumenta",0);
                String nazivTip=data.getStringExtra("nazivTipDokumenta");


                Log.d(TAG, "onActivityResult: vraćeno je:::> Naziv TIP DOKUMENTA =" + nazivTip);
                Log.d(TAG, "onActivityResult: vraćeno je:::> ID TIP DOKUMENTA =" + idTip.toString());

                TipDokumenta myTip=new TipDokumenta(idTip,nazivTip);
                setIzabraniTiP(myTip);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setIzabraniTiP(null);
            }
        }
        if (requestCode == 4) {
            if(resultCode == Activity.RESULT_OK){
                Long idPodtip=data.getLongExtra("idPodtipDokumenta",0);
                String nazivPodtipa=data.getStringExtra("nazivPodtipDokumenta");
                Long ridTipDok=data.getLongExtra("ridPodtipDokumenta",0);

                Log.d(TAG, "onActivityResult: vraćeno je:::> nazivPodtipDokumenta=" + nazivPodtipa);
                Log.d(TAG, "onActivityResult: vraćeno je:::> ridPodtipDokumenta=" + ridTipDok);
                Log.d(TAG, "onActivityResult: vraćeno je:::> idPodtipDokumenta=" + idPodtip.toString());

                PodtipDokumenta myPodtip=new PodtipDokumenta(idPodtip,nazivPodtipa,ridTipDok);
                setIzabranPodtip(myPodtip);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setIzabranPodtip(null);
            }
        }
    }//onActivityResult

    private Menu getMenu() {
        return myMenu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        myMenu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ok_button:
                btnOk.callOnClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
