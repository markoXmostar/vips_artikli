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
 * Created by marko on 6.2.2018..
 */

public class ListaApp1StavkeAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ListaApp1StavkeAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    static class LayoutHandler {

        TextView NazivArtikla, Jmj, Kolicina, OpisAtributa, OstaliPodaci;

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

        ListaApp1StavkeAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = layoutInflater.inflate(R.layout.row_app1_stavke, parent, false);
            layoutHandler = new ListaApp1StavkeAdapter.LayoutHandler();
            layoutHandler.NazivArtikla = (TextView) row.findViewById(R.id.txtNazivArtikla_App1Stavke);
            layoutHandler.Jmj = (TextView) row.findViewById(R.id.txtJmj_App1Stavke);
            layoutHandler.Kolicina = (TextView) row.findViewById(R.id.txtKolicina_App1Stavke);
            layoutHandler.OpisAtributa = (TextView) row.findViewById(R.id.txtOpisAtributa_App1Stavke);
            layoutHandler.OstaliPodaci = (TextView) row.findViewById(R.id.txtOstaliPodaci_App1Stavke);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ListaApp1StavkeAdapter.LayoutHandler) row.getTag();

        }



        //alternate row color start
        if (position % 2 == 1) {

            row.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.bojaDrugogReda));
        } else {
            //row.setBackgroundColor(Color.DKGRAY);
        }
        //alternate row color end
        App1Stavke myObject = (App1Stavke) this.getItem(position);
        //layoutHandler.ID.setText(komitent.getId());
        layoutHandler.NazivArtikla.setText(myObject.getArtiklNaziv());
        layoutHandler.Jmj.setText(myObject.getJmjNaziv());
        layoutHandler.Kolicina.setText(Double.toString(myObject.getKolicina()));
        String formatString=MainActivity.formatDecimalbyPostavke();
        String ostaliPodaci="VPC = " + String.format(formatString,myObject.getVpc()) + "KM / MPC = " + String.format(formatString,myObject.getMpc()) +"KM";
        layoutHandler.OstaliPodaci.setText(ostaliPodaci);

        if (myObject.isImaAtribut()){
            layoutHandler.OpisAtributa.setText(myObject.getAtributNaziv() + " : " +myObject.getAtributVrijednost());
        }else{
            layoutHandler.OpisAtributa.setText("");
            layoutHandler.OpisAtributa.setVisibility(View.GONE);
        }


        return row;

    }
}
