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
import com.example.marko.vips_artikli.dataclass.jmj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rac157 on 12.12.2017.
 */

public class ListaJmjAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaJmjAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler {
        TextView ID, NAZIV;
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
        ListaJmjAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_jmj, parent, false);
            layoutHandler = new ListaJmjAdapter.LayoutHandler();
            layoutHandler.ID = (TextView) row.findViewById(R.id.jmjID);
            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.jmjNaziv);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaJmjAdapter.LayoutHandler) row.getTag();
        }
        jmj myJMJ = (jmj) this.getItem(position);
        layoutHandler.ID.setText(Long.toString(myJMJ.getId()));
        layoutHandler.NAZIV.setText(myJMJ.getNaziv());


        return row;
    }
}
