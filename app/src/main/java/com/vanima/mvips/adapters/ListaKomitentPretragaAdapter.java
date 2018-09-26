package com.vanima.mvips.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vanima.mvips.R;
import com.vanima.mvips.models.Komitent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marko on 24.12.2017..
 */

public class ListaKomitentPretragaAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaKomitentPretragaAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler {
        TextView  NAZIV, SIFRA;
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
        ListaKomitentPretragaAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_pretraga_komitenata, parent, false);
            layoutHandler = new ListaKomitentPretragaAdapter.LayoutHandler();

            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.txtNazivKomitenta_rowPretragaKomitenata);
            layoutHandler.SIFRA = (TextView) row.findViewById(R.id.txtSifraKomitenta_rowPretragaKomitenata);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaKomitentPretragaAdapter.LayoutHandler) row.getTag();
        }
        Komitent myKom = (Komitent) this.getItem(position);
        layoutHandler.NAZIV.setText(myKom.getNaziv());
        layoutHandler.SIFRA.setText(myKom.getSifra());

        return row;
    }
}
