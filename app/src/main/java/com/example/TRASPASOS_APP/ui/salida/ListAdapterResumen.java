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

public class ListAdapterResumen extends ArrayAdapter<resumen> {

    public ListAdapterResumen(Context context, ArrayList<resumen> userArrayList){

        super(context,R.layout.list_view_resumen,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        resumen user = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_escaneo,parent,false);

        }

        TextView posicion = convertView.findViewById(R.id.posicion);
        TextView nombreProducto = convertView.findViewById(R.id.nombreProducto);
        TextView cantidad = convertView.findViewById(R.id.cantidad);

        posicion.setText("("+user.getPosicion()+"):");
        nombreProducto.setText(user.getNombreProducto()+">>[");
        cantidad.setText(user.getCantidad()+"]");

        return convertView;
    }
}
