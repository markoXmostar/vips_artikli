package com.vanima.mvips.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.vanima.mvips.R;
import com.vanima.mvips.activities.MainActivity;
import com.vanima.mvips.models.App1Stavke;
import com.vanima.mvips.models.Artikl;
import com.vanima.mvips.models.ArtiklSaKolicinom;
import com.vanima.mvips.models.jmj;

import java.util.ArrayList;

public class ListaArtikalaAdapter2 extends BaseAdapter {
    Context context;
    public ArrayList<ArtiklSaKolicinom> myItems;
    LayoutInflater inflater;
    long dokumentID;

    public ListaArtikalaAdapter2(Context context, ArrayList<ArtiklSaKolicinom> _myItems, long _dokID) {
        this.context = context;
        this.myItems = _myItems;
        this.inflater = LayoutInflater.from(context);
        this.dokumentID = _dokID;
    }

    @Override
    public int getCount() {
        return myItems.size();
    }

    @Override
    public Object getItem(int i) {
        return myItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return myItems.get(i).getArt().getId();
    }

    private class ViewHolder {
        TextView SIFRA, NAZIV, KATALOSKIBROJ, PROIZVODJAC, OSTALO, jmjNaziv;
        EditText etKolicinaJMJ;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.row_artikli2, viewGroup, false);
            holder.NAZIV = (TextView) view.findViewById(R.id.nazivArtikla_artikli);
            holder.SIFRA = (TextView) view.findViewById(R.id.sifraArtikla_artikli);
            holder.KATALOSKIBROJ = (TextView) view.findViewById(R.id.kataloskiBroj_artikli);
            holder.PROIZVODJAC = (TextView) view.findViewById(R.id.proizvodac_artikli);
            holder.OSTALO = (TextView) view.findViewById(R.id.ostaliPodaci_artikli);
            holder.jmjNaziv = (TextView) view.findViewById(R.id.tvJMJnaziv_artikli);
            holder.etKolicinaJMJ = (EditText) view.findViewById(R.id.etJMJkolicina_artikli);

            view.setTag(holder);
            view.setTag(R.id.etJMJkolicina_artikli, holder.etKolicinaJMJ);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.etKolicinaJMJ.setTag(i);
        holder.etKolicinaJMJ.setId(i);

        holder.etKolicinaJMJ.addTextChangedListener(new TextWatcher() {
            double oldKolicina;
            double kolicina;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().isEmpty()) {
                    oldKolicina = 0;
                } else {
                    if (charSequence.toString() == "") {
                        oldKolicina = 0;
                    } else {
                        oldKolicina = Double.parseDouble(charSequence.toString());
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.toString().isEmpty()) {
                    kolicina = 0;
                } else {
                    if (charSequence.toString() == "") {
                        kolicina = 0;
                    } else {
                        kolicina = Double.parseDouble(charSequence.toString());
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
                int i = (Integer) holder.etKolicinaJMJ.getTag();
                if (kolicina != oldKolicina) {
                    myItems.get(i).setKolicina(kolicina);
                    App1Stavke newStavka;
                    Artikl izabraniArtikl = myItems.get(i).getArt();
                    newStavka = new App1Stavke(-1, dokumentID,
                            izabraniArtikl.getId(),
                            izabraniArtikl.getNaziv(),
                            myItems.get(i).getArt().getJmjId(),
                            myItems.get(i).getArt().getJmjNaziv(),
                            izabraniArtikl.isImaRokTrajanja(),
                            //izabraniAtribut.getAtributId1(),
                            //izabraniAtribut.getAtributNaziv1(),
                            //izabraniAtribut.getAtributVrijednost1(),
                            -1,
                            null,
                            null,
                            kolicina,
                            izabraniArtikl.getVpc(),
                            izabraniArtikl.getMpc(),
                            //txtNapomena.getText().toString());
                            "");
                    MainActivity.snimiStavku((Activity) context, dokumentID, newStavka);
                }
            }
        });

        holder.etKolicinaJMJ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    int itemIndex = view.getId();
                    String unesenaKolicina = ((EditText) view).getText().toString();
                    myItems.get(itemIndex).setKolicina(Double.valueOf(unesenaKolicina));
                }
            }
        });
        String prozvodac = "";
        String katbroj = "";
        if (myItems.get(i).getArt().getProizvodjac().isEmpty()) {
            prozvodac = "N/A";
        } else {
            prozvodac = myItems.get(i).getArt().getProizvodjac();
        }
        if (myItems.get(i).getArt().getKataloskiBroj().isEmpty()) {
            katbroj = "N/A";
        } else {
            katbroj = myItems.get(i).getArt().getKataloskiBroj();
        }
        String vpc, mpc, stanje, brojKoleta, brojKomadaNaPaleti;
        String formatString = MainActivity.formatDecimalbyPostavke();
        vpc = "VPC=" + String.format(formatString, myItems.get(i).getArt().getVpc());
        mpc = "MPC=" + String.format(formatString, myItems.get(i).getArt().getMpc());
        stanje = "STANJE=" + myItems.get(i).getArt().getStanje();
        brojKoleta = "Broj koleta: " + myItems.get(i).getArt().getBrojKoleta();
        brojKomadaNaPaleti = "Broj komada na paleti: " + myItems.get(i).getArt().getBrojKoletaNaPaleti();

        holder.NAZIV.setText(myItems.get(i).getArt().getNaziv());
        holder.SIFRA.setText(myItems.get(i).getArt().getSifra());
        holder.KATALOSKIBROJ.setText("Kat. broj: " + katbroj + " Proizvođač: " + prozvodac);
        holder.PROIZVODJAC.setText(stanje + " / " + vpc + " / " + mpc);
        holder.OSTALO.setText(brojKoleta + " / " + brojKomadaNaPaleti);
        holder.jmjNaziv.setText(myItems.get(i).getArt().getJmjNaziv());
        holder.etKolicinaJMJ.setText(String.valueOf(myItems.get(i).getKolicina()));
        return view;
    }
}
