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
import com.vanima.mvips.models.TipDokumenta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rac157 on 14.12.2017.
 */

public class ListaTipDokumentaAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaTipDokumentaAdapter(@NonNull Context context, int resource) {
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
        ListaTipDokumentaAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_jmj, parent, false);
            layoutHandler = new ListaTipDokumentaAdapter.LayoutHandler();
            layoutHandler.ID = (TextView) row.findViewById(R.id.jmjID);
            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.jmjNaziv);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaTipDokumentaAdapter.LayoutHandler) row.getTag();
        }
        TipDokumenta myTipDokumenta = (TipDokumenta) this.getItem(position);
        layoutHandler.ID.setText(Long.toString(myTipDokumenta.getId()));
        layoutHandler.NAZIV.setText(myTipDokumenta.getNaziv());


        return row;
    }
}
