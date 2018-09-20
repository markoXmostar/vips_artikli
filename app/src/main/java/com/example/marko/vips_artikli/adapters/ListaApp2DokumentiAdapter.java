package com.example.marko.vips_artikli.adapters;

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

import com.example.marko.vips_artikli.R;
import com.example.marko.vips_artikli.models.App2Dokumenti;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marko on 17.4.2018..
 */

public class ListaApp2DokumentiAdapter extends ArrayAdapter {
    private static String TAG = "LISTA DOKUMENATA2 ADAPTER: ";
    public List list = new ArrayList();

    public ListaApp2DokumentiAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    static class LayoutHandler {

        TextView DatumDokumenta, Komitent, NacinPlacanja;
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

        ListaApp2DokumentiAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflater.inflate(R.layout.row_app2_zaglavlje, parent, false);
            layoutHandler = new ListaApp2DokumentiAdapter.LayoutHandler();
            layoutHandler.DatumDokumenta = (TextView) row.findViewById(R.id.txtDatumDokumenta_App2RowDokumenti);
            layoutHandler.NacinPlacanja = (TextView) row.findViewById(R.id.txtNacinPlacanja_App2RowDokumenti);
            layoutHandler.Komitent = (TextView) row.findViewById(R.id.txtKomitent_App2RowDokumenti);
            layoutHandler.Slika = (ImageView) row.findViewById(R.id.imgDokumentSinkroniziran_App2RowDokumenti);

            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaApp2DokumentiAdapter.LayoutHandler) row.getTag();

        }
        //alternate row color start
        if (position % 2 == 1) {
            //row.setBackgroundColor(Color.LTGRAY);
            row.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.bojaDrugogReda));
        } else {
            //row.setBackgroundColor(Color.DKGRAY);
        }
        //alternate row color end
        App2Dokumenti myObject = (App2Dokumenti) this.getItem(position);
        //layoutHandler.ID.setText(komitent.getId());
        layoutHandler.DatumDokumenta.setText(myObject.getDatumDokumentaString());
        layoutHandler.Komitent.setText(myObject.getKmtNaziv() + " / " + myObject.getPjKmtNaziv());
        layoutHandler.NacinPlacanja.setText(myObject.getNacinPlacanjaNaziv());
        if (myObject.isZavrsen()) {
            layoutHandler.Slika.setImageResource(R.drawable.ic_baseline_check_circle_outline_24px);
        } else {
            layoutHandler.Slika.setImageResource(R.drawable.ic_baseline_error_outline_24px);
        }
        return row;

    }
}
