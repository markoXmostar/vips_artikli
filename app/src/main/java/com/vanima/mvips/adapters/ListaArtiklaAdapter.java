package com.vanima.mvips.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.vanima.mvips.R;
import com.vanima.mvips.models.Artikl;
import com.vanima.mvips.activities.MainActivity;
import com.vanima.mvips.models.ArtiklSaKolicinom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marko on 4.11.2017.
 */

public class ListaArtiklaAdapter extends ArrayAdapter implements Filterable {

    List myList = new ArrayList();

    private final List<Artikl> fullList = new ArrayList<>();

    public ListaArtiklaAdapter(@NonNull Context context, int resource) {
        super(context, resource);

    }


    static class LayoutHandler {

        TextView SIFRA, NAZIV, KATALOSKIBROJ, PROIZVODJAC, OSTALO;

    }

    private boolean asortiman=false;

    public void setAsortiman(boolean asortiman) {
        this.asortiman = asortiman;
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        myList.add(object);
        fullList.add((Artikl) object);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflater.inflate(R.layout.row_artikl, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.SIFRA = (TextView) row.findViewById(R.id.sifraArtikla_artikli);
            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.nazivArtikla_artikli);
            layoutHandler.KATALOSKIBROJ = (TextView) row.findViewById(R.id.kataloskiBroj_artikli);
            layoutHandler.PROIZVODJAC = (TextView) row.findViewById(R.id.proizvodac_artikli);
            layoutHandler.OSTALO = (TextView) row.findViewById(R.id.ostaliPodaci_artikli);


            row.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) row.getTag();

        }
        Artikl artikl = (Artikl) this.getItem(position);
        //layoutHandler.ID.setText(komitent.getId());
        layoutHandler.NAZIV.setText(artikl.getNaziv());
        layoutHandler.SIFRA.setText(artikl.getSifra());
        String prozvodac="";
        String katbroj="";
        if (artikl.getProizvodjac().isEmpty()){
            prozvodac="N/A";
        }else {
            prozvodac=artikl.getProizvodjac();
        }
        if (artikl.getKataloskiBroj().isEmpty()){
            katbroj="N/A";
        }else {
            katbroj=artikl.getKataloskiBroj();
        }
        layoutHandler.KATALOSKIBROJ.setText("Kat. broj: " + katbroj + " Proizvođač: " + prozvodac);

        String vpc,mpc,stanje,brojKoleta,brojKomadaNaPaleti;
        String formatString=MainActivity.formatDecimalbyPostavke();
        vpc="VPC=" + String.format(formatString,artikl.getVpc());
        mpc="MPC=" + String.format(formatString,artikl.getMpc());
        stanje="STANJE=" + artikl.getStanje();

        layoutHandler.PROIZVODJAC.setText(stanje + " / " + vpc + " / " +mpc);

        brojKoleta="Broj koleta: " + artikl.getBrojKoleta();
        brojKomadaNaPaleti="Broj komada na paleti: " + artikl.getBrojKoletaNaPaleti();
        layoutHandler.OSTALO.setText(brojKoleta + " / " + brojKomadaNaPaleti);
        return row;

    }



    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) { // if your editText field is empty, return full list of FriendItem
                    List<Artikl> filteredList = new ArrayList<>();
                    for (Artikl item : fullList) {
                        if (asortiman){
                            if(item.isAsortimanKupca()){
                                filteredList.add(item);
                            }
                        }else{
                            filteredList.add(item);
                        }
                    }
                    results.count = filteredList.size();
                    results.values = filteredList;
                } else {
                    List<Artikl> filteredList = new ArrayList<>();

                    constraint = constraint.toString().toLowerCase();
                    for (Artikl item : fullList) {
                        String nazivArt = item.getNaziv().toLowerCase();
                        String sifraArt = item.getSifra().toLowerCase();
                        String kataloskiArt =item.getKataloskiBroj().toLowerCase();

                        if (nazivArt.contains(constraint.toString()) || sifraArt.contains(constraint.toString()) || kataloskiArt.contains(constraint.toString())) {
                            if (asortiman){
                                if(item.isAsortimanKupca()){
                                    filteredList.add(item);
                                }
                            }else{
                                filteredList.add(item);
                            }

                        }
                    }

                    results.count = filteredList.size();
                    results.values = filteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                myList = (List<Artikl>) results.values; // replace list to filtered list
                notifyDataSetChanged(); // refresh adapter
            }
        };
        return filter;
    }
}
