package com.example.TRASPASOS_APP.ui.salida;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.TRASPASOS_APP.R;

import java.util.ArrayList;

public class ListAdapterSeguimientoDestino extends ArrayAdapter<objDestino> {


    public ListAdapterSeguimientoDestino(Context context, ArrayList<objDestino> userArrayList){

        super(context,R.layout.list_view_seguimiento_destino,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        objDestino destino = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_seguimiento_destino,parent,false);

        }

        TextView cortina = convertView.findViewById(R.id.cortina);
        TextView bodega = convertView.findViewById(R.id.bodega);
        TextView destinoAlm = convertView.findViewById(R.id.destino);
        TextView contador = convertView.findViewById(R.id.contador);

        cortina.setText("Cortina: "+destino.cortina);
        bodega.setText("Bodega: "+destino.bodega_destino_nombre);
        destinoAlm.setText("Destino: "+destino.Destino);
        contador.setText(destino.contador);


        return convertView;
    }
}
