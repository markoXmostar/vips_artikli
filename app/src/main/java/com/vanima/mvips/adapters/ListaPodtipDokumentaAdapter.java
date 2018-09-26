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
import com.vanima.mvips.models.PodtipDokumenta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rac157 on 14.12.2017.
 */

public class ListaPodtipDokumentaAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaPodtipDokumentaAdapter(@NonNull Context context, int resource) {
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
        ListaPodtipDokumentaAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_grupa, parent, false);
            layoutHandler = new ListaPodtipDokumentaAdapter.LayoutHandler();
            layoutHandler.ID = (TextView) row.findViewById(R.id.grupaID);
            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.grupaNaziv);
            layoutHandler.RID = (TextView) row.findViewById(R.id.grupaRid);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaPodtipDokumentaAdapter.LayoutHandler) row.getTag();
        }
        PodtipDokumenta myPodtipDokumenta = (PodtipDokumenta) this.getItem(position);
        layoutHandler.ID.setText(Long.toString(myPodtipDokumenta.getId()));
        layoutHandler.NAZIV.setText(myPodtipDokumenta.getNaziv());
        layoutHandler.RID.setText(Long.toString(myPodtipDokumenta.getRid()));

        return row;
    }
}
