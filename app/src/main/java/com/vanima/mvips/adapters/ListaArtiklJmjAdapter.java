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
import com.vanima.mvips.models.ArtiklJmj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marko on 10.1.2018..
 */

public class ListaArtiklJmjAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaArtiklJmjAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    static class LayoutHandler {
        TextView artiklID, jmjID, odnos;
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

        ListaArtiklJmjAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflater.inflate(R.layout.row_grupa, parent, false);
            layoutHandler = new ListaArtiklJmjAdapter.LayoutHandler();
            layoutHandler.artiklID = (TextView) row.findViewById(R.id.grupaID);
            layoutHandler.jmjID = (TextView) row.findViewById(R.id.grupaNaziv);
            layoutHandler.odnos= (TextView) row.findViewById(R.id.grupaRid);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaArtiklJmjAdapter.LayoutHandler) row.getTag();
        }

        ArtiklJmj artiklJmj = (ArtiklJmj) this.getItem(position);

        /*
        String artNaziv,jmjNaziv;
        Activity a=(Activity) parent.getContext();
        artNaziv=MainActivity.getArtiklNaziv_byID(a,artiklJmj.getArtiklID());
        jmjNaziv=MainActivity.getJmjNaziv_byID(a,artiklJmj.getJmjID());
        layoutHandler.artiklID.setText(artNaziv);
        layoutHandler.jmjID.setText(jmjNaziv);
        */
        layoutHandler.artiklID.setText(Long.toString(artiklJmj.getArtiklID()));
        layoutHandler.jmjID.setText(Long.toString(artiklJmj.getJmjID()));
        layoutHandler.odnos.setText(Double.toString(artiklJmj.getOdnos()));

        return row;

    }
}
