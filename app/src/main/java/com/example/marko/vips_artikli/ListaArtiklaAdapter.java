package com.example.marko.vips_artikli;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marko on 4.11.2017.
 */

public class ListaArtiklaAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ListaArtiklaAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    static class LayoutHandler {

        TextView SIFRA, NAZIV, KATALOSKIBROJ, PROIZVODJAC;

    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            layoutHandler.SIFRA = (TextView) row.findViewById(R.id.sifraArtikla);
            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.nazivArtikla);
            layoutHandler.KATALOSKIBROJ = (TextView) row.findViewById(R.id.kataloskiBroj);
            layoutHandler.PROIZVODJAC = (TextView) row.findViewById(R.id.proizvodac);


            row.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) row.getTag();

        }
        Artikl artikl = (Artikl) this.getItem(position);
        //layoutHandler.ID.setText(komitent.getId());
        layoutHandler.NAZIV.setText(artikl.getNaziv());
        layoutHandler.SIFRA.setText(artikl.getSifra());
        layoutHandler.KATALOSKIBROJ.setText(artikl.getKataloskiBroj());
        layoutHandler.PROIZVODJAC.setText(artikl.getProizvodjac());
        return row;

    }
}
