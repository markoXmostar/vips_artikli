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
import com.example.marko.vips_artikli.models.PodtipDokumenta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marko on 28.12.2017..
 */

public class ListaPodtipDokumentaPretragaAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaPodtipDokumentaPretragaAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler {
        TextView NAZIV;
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
        ListaPodtipDokumentaPretragaAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_pretraga_pjkomitenata, parent, false);
            layoutHandler = new ListaPodtipDokumentaPretragaAdapter.LayoutHandler();

            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.txtNazivPJKomitenta_PretragaPjKomitenata);

            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaPodtipDokumentaPretragaAdapter.LayoutHandler) row.getTag();
        }
        PodtipDokumenta myPodtipDokumenta = (PodtipDokumenta) this.getItem(position);

        layoutHandler.NAZIV.setText(myPodtipDokumenta.getNaziv());


        return row;
    }
}
