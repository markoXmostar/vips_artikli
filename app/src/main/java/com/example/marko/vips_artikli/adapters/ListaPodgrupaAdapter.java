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
import com.example.marko.vips_artikli.dataclass.PodgrupaArtikala;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rac157 on 14.12.2017.
 */

public class ListaPodgrupaAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaPodgrupaAdapter(@NonNull Context context, int resource) {
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
        ListaPodgrupaAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_podgrupa, parent, false);
            layoutHandler = new ListaPodgrupaAdapter.LayoutHandler();
            layoutHandler.ID = (TextView) row.findViewById(R.id.podgrupaID);
            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.podgrupaNaziv);
            layoutHandler.RID = (TextView) row.findViewById(R.id.podgrupaRid);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaPodgrupaAdapter.LayoutHandler) row.getTag();
        }
        PodgrupaArtikala myPodgrupa = (PodgrupaArtikala) this.getItem(position);
        layoutHandler.ID.setText(Long.toString(myPodgrupa.getId()));
        layoutHandler.NAZIV.setText(myPodgrupa.getNaziv());
        layoutHandler.RID.setText(Long.toString(myPodgrupa.getRid()));

        return row;
    }
}
