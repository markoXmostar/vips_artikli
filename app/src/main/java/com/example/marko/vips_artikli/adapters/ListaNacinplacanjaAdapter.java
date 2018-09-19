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
import com.example.marko.vips_artikli.dataclass.NacinPlacanja;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rac157 on 14.12.2017.
 */

public class ListaNacinplacanjaAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaNacinplacanjaAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler {
        TextView ID, NAZIV, RID;
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
        ListaNacinplacanjaAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_jmj, parent, false);
            layoutHandler = new ListaNacinplacanjaAdapter.LayoutHandler();
            layoutHandler.ID = (TextView) row.findViewById(R.id.jmjID);
            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.jmjNaziv);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaNacinplacanjaAdapter.LayoutHandler) row.getTag();
        }
        NacinPlacanja myNacinPlacanja = (NacinPlacanja) this.getItem(position);
        layoutHandler.ID.setText(Long.toString(myNacinPlacanja.getId()));
        layoutHandler.NAZIV.setText(myNacinPlacanja.getNaziv());


        return row;
    }
}
