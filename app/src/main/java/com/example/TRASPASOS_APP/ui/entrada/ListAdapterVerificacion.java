package com.example.TRASPASOS_APP.ui.entrada;

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

public class ListAdapterVerificacion extends ArrayAdapter<datosTarimasEscaneadas> {

    public ListAdapterVerificacion(Context context, ArrayList<datosTarimasEscaneadas> userArrayLister){
        super(context, R.layout.list_view_datos_tarimas_escaneadas, userArrayLister);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        datosTarimasEscaneadas user = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_escaneo_entrada, parent, false);
        }


        TextView numLinea = convertView.findViewById(R.id.ennumeradoo);
        TextView nombreProducto = convertView.findViewById(R.id.nombreProductoo);
        TextView etiqueta = convertView.findViewById(R.id.idEtiquetaa);
        TextView fechaEtiquetado = convertView.findViewById(R.id.fechaEtiquetadoo);
        TextView clave = convertView.findViewById(R.id.clavee);

        numLinea.setText(new StringBuilder().append("(").append(user.getNo_linea()).append(")").toString());
        nombreProducto.setText(user.getNombre_producto());
        etiqueta.setText(new StringBuilder().append("LOTE\n").append(user.getEtiqueta()).toString());
        fechaEtiquetado.setText(new StringBuilder().append("Fecha Carga\n").append(user.getFecha_carga()).toString());
        clave.setText(user.getClave());

        return  convertView;

    }
}
