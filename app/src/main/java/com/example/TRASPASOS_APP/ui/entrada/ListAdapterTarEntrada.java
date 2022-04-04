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

public class ListAdapterTarEntrada extends ArrayAdapter<datosDocumento> {

    public ListAdapterTarEntrada(Context context, ArrayList<datosDocumento> userArrayLister){
        super(context, R.layout.list_view_escaneo_entrada, userArrayLister);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        datosDocumento user = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_escaneo_entrada, parent, false);
        }


        TextView numLinea = convertView.findViewById(R.id.ennumeradoo);
        TextView nombreProducto = convertView.findViewById(R.id.nombreProductoo);
        TextView etiqueta = convertView.findViewById(R.id.idEtiquetaa);
        TextView fechaEtiquetado = convertView.findViewById(R.id.fechaEtiquetadoo);
        TextView clave = convertView.findViewById(R.id.clavee);

        numLinea.setText(user.getNumero_documento());
        nombreProducto.setText(user.getBodega_origen_nombre());
        etiqueta.setText(user.getBodega_destino_nombre());
        fechaEtiquetado.setText(user.getNombre_transportista());
        clave.setText("Tarimas: "+ user.getNumeroTarimas());

        return  convertView;

    }

}
