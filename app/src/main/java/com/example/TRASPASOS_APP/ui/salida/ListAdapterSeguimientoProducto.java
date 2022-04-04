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

public class ListAdapterSeguimientoProducto extends ArrayAdapter<objProducto> {


    public ListAdapterSeguimientoProducto(Context context, ArrayList<objProducto> userArrayList){

        super(context,R.layout.list_view_seguimiento_producto,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        objProducto producto = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_seguimiento_producto,parent,false);

        }

        TextView clave = convertView.findViewById(R.id.clave);
        TextView nomProducto = convertView.findViewById(R.id.nomProducto);
        TextView Tarimas = convertView.findViewById(R.id.Tarimas);
        TextView consecutivo = convertView.findViewById(R.id.consecutivo);

        clave.setText("Clave: "+producto.clave);
        nomProducto.setText("Producto: "+producto.nombre_producto);
        Tarimas.setText("Tarimas: "+producto.Tarimas);
        consecutivo.setText(producto.contador);


        return convertView;
    }
}
