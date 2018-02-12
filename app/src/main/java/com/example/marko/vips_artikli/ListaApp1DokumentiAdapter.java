package com.example.marko.vips_artikli;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marko on 22.12.2017..
 */

public class ListaApp1DokumentiAdapter extends ArrayAdapter{
    private static String TAG="LISTA DOKUMENATA ADAPTER: ";
    List list = new ArrayList();

    public ListaApp1DokumentiAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    static class LayoutHandler {

        TextView DatumDokumenta, Komitent, PjKomitenta, TipDkumenta,PodtipDokumenta;
        ImageView Slika;
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

        ListaApp1DokumentiAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflater.inflate(R.layout.row_app1_zaglavlje, parent, false);
            layoutHandler = new ListaApp1DokumentiAdapter.LayoutHandler();
            layoutHandler.DatumDokumenta = (TextView) row.findViewById(R.id.txtDatumDokumenta_App1);
            layoutHandler.Komitent = (TextView) row.findViewById(R.id.txtKomitent_App1);
            layoutHandler.PjKomitenta = (TextView) row.findViewById(R.id.txtPjKOmitenta_App1);
            layoutHandler.TipDkumenta = (TextView) row.findViewById(R.id.txtTipDokumenta_App1);
            layoutHandler.PodtipDokumenta = (TextView) row.findViewById(R.id.txtPodtipDokumenta_App1);
            layoutHandler.Slika=(ImageView)row.findViewById(R.id.imgDokumentSinkroniziran_App1);

            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaApp1DokumentiAdapter.LayoutHandler) row.getTag();

        }
        //alternate row color start
        if (position % 2 == 1) {
            //row.setBackgroundColor(Color.LTGRAY);
            row.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.bojaDrugogReda));
        } else {
            //row.setBackgroundColor(Color.DKGRAY);
        }
        //alternate row color end
        App1Dokumenti myObject = (App1Dokumenti) this.getItem(position);
        //layoutHandler.ID.setText(komitent.getId());
        layoutHandler.DatumDokumenta.setText(myObject.getDatumDokumentaString());
        layoutHandler.Komitent.setText(myObject.getKomitentNaziv());
        layoutHandler.PjKomitenta.setText(myObject.getPjKomitentNaziv());
        layoutHandler.TipDkumenta.setText(myObject.getTipDokumentaNaziv());
        layoutHandler.PodtipDokumenta.setText(myObject.getPodtipDokumentaNaziv());
        if (myObject.getDatumSinkronizacije()==null){
            Log.d(TAG, "getView:  NIJE SINKRONIZIRAN");
            layoutHandler.Slika.setImageResource(R.drawable.img_error);
        }
        else{
            Log.d(TAG, "getView:  SINKRONIZIRAN!");
            layoutHandler.Slika.setImageResource(R.drawable.img_ok);
        }
        return row;

    }
}
