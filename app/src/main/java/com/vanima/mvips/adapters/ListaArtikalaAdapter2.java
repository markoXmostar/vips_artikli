package com.vanima.mvips.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;

import com.vanima.mvips.R;
import com.vanima.mvips.activities.MainActivity;
import com.vanima.mvips.models.App1Stavke;
import com.vanima.mvips.models.Artikl;
import com.vanima.mvips.models.ArtiklJmj;
import com.vanima.mvips.models.ArtiklSaKolicinom;
import com.vanima.mvips.models.jmj;

import java.util.ArrayList;
import java.util.List;

public class ListaArtikalaAdapter2 extends BaseAdapter implements Filterable {
    Context context;
    public List<ArtiklSaKolicinom> myItems;
    private final List<ArtiklSaKolicinom> fullList = new ArrayList<>();
    LayoutInflater inflater;
    long dokumentID;

    public ListaArtikalaAdapter2(Context context, ArrayList<ArtiklSaKolicinom> _myItems, long _dokID) {
        this.context = context;
        this.myItems = _myItems;
        fullList.addAll(_myItems);
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
        TextView spinJMJ;
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
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
            holder.spinJMJ=(TextView) view.findViewById(R.id.spinnJMJ_artikli) ;

            view.setTag(holder);
            view.setTag(R.id.etJMJkolicina_artikli, holder.etKolicinaJMJ);
            view.setTag(R.id.spinnJMJ_artikli, holder.spinJMJ);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.etKolicinaJMJ.setTag(i);
        holder.etKolicinaJMJ.setId(i);
        holder.spinJMJ.setTag(i);
        holder.spinJMJ.setId(i);

 /*       holder.etKolicinaJMJ.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
        holder.etKolicinaJMJ.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        holder.etKolicinaJMJ.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    int i = view.getId();
                    String unesenaKolicina = ((EditText) view).getText().toString();
                    double novaKolicina=Double.valueOf(unesenaKolicina);
                    double staraKolicina=myItems.get(i).getKolicina();
                    if (novaKolicina!=staraKolicina){
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
                                novaKolicina,
                                izabraniArtikl.getVpc(),
                                izabraniArtikl.getMpc(),
                                //txtNapomena.getText().toString());
                                "");
                        //ako je stara količina 0 onda je stavka nova a ako je starakoličina>0 onda ide izmjena stavke
                        //ako je nova količina 0 onda ide brisanje stavke
                        myItems.get(i).setKolicina(novaKolicina);
                        for(ArtiklSaKolicinom item : fullList){
                            if (item.getArt().getId()==myItems.get(i).getArt().getId()){
                                item.setKolicina(novaKolicina);
                            }
                        }
                        if (staraKolicina==0){
                            MainActivity.snimiStavku((Activity) context, dokumentID, newStavka);
                        }
                        if (staraKolicina>0){
                            if (novaKolicina==0){
                                MainActivity.izbrisiStavkuSaArtiklom((Activity) context,dokumentID,newStavka);
                            }else{
                                MainActivity.izmjeniStavkuSaArtiklom((Activity) context,dokumentID,newStavka);
                            }
                        }

                    }

                } else{
                    /*
                    int i = view.getId();
                    Spinner mySpinner=holder.spinJMJ;
                    List<ArtiklJmj> myListaJMJ=MainActivity.getListaArtiklJMJ( (Activity) context, myItems.get(i).getArt().getId(),"");
                    ArrayAdapter<ArtiklJmj> adp = new ArrayAdapter<ArtiklJmj> (context,android.R.layout.simple_spinner_dropdown_item,myListaJMJ);
                    holder.spinJMJ.setAdapter(adp);
                    */
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
        //holder.jmjNaziv.setText(myItems.get(i).getArt().getJmjNaziv());
        /*ArrayList<jmj> listaJMJ=new ArrayList<jmj>();
        jmj myJMJ=new jmj(myItems.get(i).getArt().getJmjId(),myItems.get(i).getArt().getJmjNaziv());
        listaJMJ.add(myJMJ);
        ArrayAdapter<jmj> adp = new ArrayAdapter<jmj> (context,android.R.layout.simple_spinner_dropdown_item,listaJMJ);
        holder.spinJMJ.setAdapter(adp);*/
        holder.spinJMJ.setText(myItems.get(i).getArt().getJmjNaziv());
        holder.etKolicinaJMJ.setText(String.valueOf(myItems.get(i).getKolicina()));
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) { // if your editText field is empty, return full list of FriendItem
                    results.count = fullList.size();
                    results.values = fullList;
                } else {
                    List<ArtiklSaKolicinom> filteredList = new ArrayList<>();

                    constraint = constraint.toString().toLowerCase();
                    for (ArtiklSaKolicinom item : fullList) {
                        String nazivArt = item.getArt().getNaziv().toLowerCase();
                        String sifraArt = item.getArt().getSifra().toLowerCase();
                        String kataloskiArt =item.getArt().getKataloskiBroj().toLowerCase();

                        if (nazivArt.contains(constraint.toString()) || sifraArt.contains(constraint.toString()) || kataloskiArt.contains(constraint.toString())) {
                            filteredList.add(item);
                        }
                    }

                    results.count = filteredList.size();
                    results.values = filteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                myItems = (List<ArtiklSaKolicinom>) results.values; // replace list to filtered list
                notifyDataSetChanged(); // refresh adapter
            }
        };
        return filter;
    }
}
