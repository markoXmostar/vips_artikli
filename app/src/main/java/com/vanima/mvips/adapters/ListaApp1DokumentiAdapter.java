package com.vanima.mvips.adapters;

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

import com.vanima.mvips.R;
import com.vanima.mvips.models.App1Dokumenti;
import com.vanima.mvips.activities.MainActivity;

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

        TextView DatumDokumenta,  PjKomitenta, PodtipDokumenta;
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

            row = layoutInflater.inflate(R.layout.row_app1_zaglavlje_v2, parent, false);
            layoutHandler = new ListaApp1DokumentiAdapter.LayoutHandler();
            layoutHandler.DatumDokumenta = (TextView) row.findViewById(R.id.tvDaatum_rowApp1Zaglavlje);
            layoutHandler.PjKomitenta = (TextView) row.findViewById(R.id.tvPjKomitenta_rowApp1Zaglavlje);
            layoutHandler.PodtipDokumenta = (TextView) row.findViewById(R.id.tvPodtipDokumenta_rowApp1Zaglavlje);
            layoutHandler.Slika=(ImageView)row.findViewById(R.id.imgDokumentSinkroniziran_rowApp1Zaglavlje);

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

        //treba ovdje sada pozvati f-ju koja će vratiti broj stavki i ukupan iznos Para!
        String formatString=MainActivity.formatDecimalbyPostavke();
        layoutHandler.DatumDokumenta.setText(myObject.getDatumDokumentaString() + " Broj stavki: " + myObject.getBrojStavki() + " - " + String.format(formatString, myObject.getSumaStavki()) + "KM");
        if (myObject.isZakljucen()){
            layoutHandler.DatumDokumenta.setTextColor(ContextCompat.getColor(this.getContext(), R.color.myCrna));
        }else{
            layoutHandler.DatumDokumenta.setTextColor(ContextCompat.getColor(this.getContext(), R.color.myCrvena));
        }
        layoutHandler.PjKomitenta.setText(myObject.getKomitentNaziv() + " / " + myObject.getPjKomitentNaziv() + " / " +myObject.getNacinPlacanjaNaziv());

        layoutHandler.PodtipDokumenta.setText(myObject.getTipDokumentaNaziv() + " / " + myObject.getPodtipDokumentaNaziv());

        if (myObject.getDatumSinkronizacije()==null){
            if (myObject.isZakljucen()){
                layoutHandler.Slika.setImageResource(R.drawable.img_dokument_zut);
            }else {
                layoutHandler.Slika.setImageResource(R.drawable.img_dokument_crven);
            }

        }
        else{
            layoutHandler.Slika.setImageResource(R.drawable.img_dokument_zelen);
        }
        return row;
    }
}
