package com.example.marko.vips_artikli;

import android.content.Context;
import android.graphics.Color;
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
 * Created by rac157 on 19.4.2018.
 */

public class ListaApp2StavkeAdapter extends ArrayAdapter {
    private static String TAG = "LISTA DOKUMENATA2 ADAPTER: ";
    List list = new ArrayList();

    public ListaApp2StavkeAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    static class LayoutHandler {
        TextView NazivArtikla, JMJ, Kolicina, ZadanaKolicina;
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

        ListaApp2StavkeAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflater.inflate(R.layout.row_app2_stavke, parent, false);
            layoutHandler = new ListaApp2StavkeAdapter.LayoutHandler();
            layoutHandler.NazivArtikla = (TextView) row.findViewById(R.id.txtSifraNazivAtribut_app2RowStavke);
            layoutHandler.JMJ = (TextView) row.findViewById(R.id.txtJMJ_app2RowStavke);
            layoutHandler.Kolicina = (TextView) row.findViewById(R.id.txtKolicina_app2RowStavke);
            layoutHandler.ZadanaKolicina = (TextView) row.findViewById(R.id.txtZadanaKolicina_app2RowStavke);

            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaApp2StavkeAdapter.LayoutHandler) row.getTag();

        }
        //alternate row color start
        if (position % 2 == 1) {
            //row.setBackgroundColor(Color.LTGRAY);
            row.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.bojaDrugogReda));
        } else {
            //row.setBackgroundColor(Color.DKGRAY);
        }
        //alternate row color end
        App2Stavke myObject = (App2Stavke) this.getItem(position);
        //layoutHandler.ID.setText(komitent.getId());
        //layoutHandler.NazivArtikla.setText(myObject.getArtiklSifra() + " | " + myObject.getArtiklNaziv() + "\r\n" + myObject.getAtributNaziv());
        layoutHandler.NazivArtikla.setText(myObject.getArtiklSifra() + " | " + myObject.getArtiklNaziv() + "\n" + "Nema atributa");
        layoutHandler.JMJ.setText(myObject.getJmjNaziv());
        layoutHandler.Kolicina.setText(String.valueOf(myObject.getKolicina()));
        layoutHandler.ZadanaKolicina.setText(String.valueOf(myObject.getKolicinaZadana()));
        if (myObject.getKolicina() != myObject.getKolicinaZadana()) {
            layoutHandler.Kolicina.setTextColor(Color.RED);
        } else {
            //layoutHandler.Kolicina.setTextColor(Color.GREEN);
        }
        return row;

    }
}
