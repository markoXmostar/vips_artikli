package com.example.marko.vips_artikli;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marko on 10.1.2018..
 */

public class ListaArtiklAtributStanjeAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaArtiklAtributStanjeAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    static class LayoutHandler {
        TextView nazivAtributa1, vrijednostAtributa1, stanje;
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

        ListaArtiklAtributStanjeAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflater.inflate(R.layout.row_grupa, parent, false);
            layoutHandler = new ListaArtiklAtributStanjeAdapter.LayoutHandler();
            layoutHandler.nazivAtributa1 = (TextView) row.findViewById(R.id.grupaID);
            layoutHandler.vrijednostAtributa1 = (TextView) row.findViewById(R.id.grupaNaziv);
            layoutHandler.stanje = (TextView) row.findViewById(R.id.grupaRid);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaArtiklAtributStanjeAdapter.LayoutHandler) row.getTag();
        }
        ArtiklAtributStanje artiklAtributStanje = (ArtiklAtributStanje) this.getItem(position);
        String artNaziv;
        Activity a=(Activity) parent.getContext();
        artNaziv=MainActivity.getArtiklNaziv(a,artiklAtributStanje.getArtiklId());
        layoutHandler.nazivAtributa1.setText(artNaziv);
        //layoutHandler.nazivAtributa1.setText(artiklAtributStanje.getAtribut1());
        layoutHandler.vrijednostAtributa1.setText(artiklAtributStanje.getAtribut1() + ":" + artiklAtributStanje.getVrijednost1());
        layoutHandler.stanje.setText(Double.toString(artiklAtributStanje.getStanje()));
        return row;

    }


}
