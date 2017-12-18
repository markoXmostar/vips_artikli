package com.example.marko.vips_artikli;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marko on 15.12.2017..
 */

public class ListaDbLogAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaDbLogAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler {
        TextView TABELA, NAZIV, VRIJEME;
        ImageView myOK;
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
        ListaDbLogAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_log, parent, false);
            layoutHandler = new ListaDbLogAdapter.LayoutHandler();
            layoutHandler.TABELA = (TextView) row.findViewById(R.id.txtNazivTabele_log);
            layoutHandler.NAZIV = (TextView) row.findViewById(R.id.txtBrojZapisa_log);
            layoutHandler.VRIJEME = (TextView) row.findViewById(R.id.txtVrijemeSync_log);
            layoutHandler.myOK = (ImageView) row.findViewById(R.id.imgOk_logrow);


            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaDbLogAdapter.LayoutHandler) row.getTag();
        }
        dbLog myLOG = (dbLog) this.getItem(position);
        layoutHandler.TABELA.setText(myLOG.getTabela());
        layoutHandler.TABELA.setTag(myLOG.getGreska());
        if (myLOG.getGreska() == 0) {

            layoutHandler.myOK.setImageResource(R.drawable.img_ok);
            //layoutHandler.TABELA.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.myCrvena));
        }
        else{
            layoutHandler.myOK.setImageResource(R.drawable.img_error);
            //layoutHandler.TABELA.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.myZelena));
        }
        layoutHandler.NAZIV.setText(myLOG.getGreskaMsg());
        layoutHandler.VRIJEME.setText(myLOG.getTimestamp());

        return row;
    }
}
