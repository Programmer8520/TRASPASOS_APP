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

public class ListAdapter extends ArrayAdapter<producto> {

    public ListAdapter(Context context, ArrayList<producto> userArrayList){

        super(context,R.layout.list_view_escaneo,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        producto user = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_escaneo,parent,false);

        }

        TextView idEtiqueta = convertView.findViewById(R.id.idEtiqueta);
        TextView nombreProducto = convertView.findViewById(R.id.nombreProducto);
        TextView ennumerado = convertView.findViewById(R.id.ennumerado);
        TextView fechaEtiquetado = convertView.findViewById(R.id.fechaEtiquetado);
        TextView tarimaa = convertView.findViewById(R.id.tarimaa);

        ennumerado.setText(new StringBuilder().append("(").append(user.getPosition()).append(")").toString());
        nombreProducto.setText(user.getNombreProducto());
        idEtiqueta.setText(new StringBuilder().append("Etiqueta\n").append(user.getIdEtiqueta()).toString());
        fechaEtiquetado.setText(new StringBuilder().append("Fecha Carga\n").append(user.getFechayHora()).toString());
        tarimaa.setText(new StringBuilder().append("No Tarima\n").append(user.getNoTarimaDetalle()).toString());

        return convertView;
    }
}
