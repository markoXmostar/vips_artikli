package com.example.marko.vips_artikli.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.marko.vips_artikli.R;
import com.example.marko.vips_artikli.models.Komitent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rac157 on 15.12.2017.
 */

public class ListaKomitentAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ListaKomitentAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler {
        TextView ID, NAZIV, SIFRA;
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
        ListaKomitentAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_grupa, parent, false);
            layoutHandler = new ListaKomitentAdapter.LayoutHandler();
            layoutHandler.ID = (TextView) row.findViewById(R.id.grupaID);
            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.grupaNaziv);
            layoutHandler.SIFRA = (TextView) row.findViewById(R.id.grupaRid);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaKomitentAdapter.LayoutHandler) row.getTag();
        }
        Komitent myKom = (Komitent) this.getItem(position);
        layoutHandler.ID.setText(Long.toString(myKom.getId()));
        layoutHandler.NAZIV.setText(myKom.getNaziv());
        layoutHandler.SIFRA.setText(myKom.getSifra());

        return row;
    }

}
