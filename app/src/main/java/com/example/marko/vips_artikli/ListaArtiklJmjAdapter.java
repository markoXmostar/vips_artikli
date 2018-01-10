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
 * Created by marko on 10.1.2018..
 */

public class ListaArtiklJmjAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaArtiklJmjAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    static class LayoutHandler {
        TextView artiklID, jmjID;
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

            row = layoutInflater.inflate(R.layout.row_jmj, parent, false);
            layoutHandler = new ListaArtiklJmjAdapter.LayoutHandler();
            layoutHandler.artiklID = (TextView) row.findViewById(R.id.jmjID);
            layoutHandler.jmjID = (TextView) row.findViewById(R.id.jmjNaziv);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaArtiklJmjAdapter.LayoutHandler) row.getTag();
        }
        ArtiklJmj artiklJmj = (ArtiklJmj) this.getItem(position);
        layoutHandler.artiklID.setText(Long.toString(artiklJmj.getArtiklID()));
        layoutHandler.jmjID.setText(Long.toString(artiklJmj.getJmjID()));
        return row;

    }
}
