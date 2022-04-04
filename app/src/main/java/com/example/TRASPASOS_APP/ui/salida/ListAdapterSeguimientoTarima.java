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

public class ListAdapterSeguimientoTarima extends ArrayAdapter<objTarima> {


    public ListAdapterSeguimientoTarima(Context context, ArrayList<objTarima> userArrayList){

        super(context,R.layout.list_view_seguimiento_tarima,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        objTarima tarima = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_seguimiento_tarima,parent,false);

        }

        TextView numDocumento = convertView.findViewById(R.id.numDocumento);
        TextView numLinea = convertView.findViewById(R.id.numLinea);
        TextView nomProducto = convertView.findViewById(R.id.nomProducto);
        TextView codBarras = convertView.findViewById(R.id.codBarras);
        TextView clave = convertView.findViewById(R.id.clave);
        TextView cantidad = convertView.findViewById(R.id.cantidad);
        TextView fechaCarga = convertView.findViewById(R.id.fechaCarga);
        TextView idProduccion = convertView.findViewById(R.id.idProduccion);
        TextView noTarimas = convertView.findViewById(R.id.noTarimas);
        TextView fechaProduccion = convertView.findViewById(R.id.fechaProduccion);
        TextView noLote = convertView.findViewById(R.id.noLote);
        TextView consecutivo = convertView.findViewById(R.id.consecutivo);

        numDocumento.setText("Numero de Documento: "+tarima.numero_documento);
        numLinea.setText("Linea: "+tarima.numero_linea);
        nomProducto.setText("Producto: "+tarima.nombre_producto);
        codBarras.setText("Barras: "+tarima.codigo_ean_upc);
        clave.setText("Clave: "+ tarima.clave);
        cantidad.setText("Cantidad: "+tarima.cantidad);
        fechaCarga.setText("Fecha de Carga: "+tarima.fecha_carga);
        idProduccion.setText("Id de Produccion: "+tarima.id_produccion);
        noTarimas.setText("Numero de Tarimas: "+tarima.no_tarima);
        fechaProduccion.setText("Fecha de Produccion: "+tarima.fecha_produccion);
        noLote.setText("Lote: "+tarima.no_lote);
        consecutivo.setText(tarima.consecutivo);

        return convertView;
    }

    public static class ListAdapterSeguimientoProducto extends ArrayAdapter<objProducto> {


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
}
