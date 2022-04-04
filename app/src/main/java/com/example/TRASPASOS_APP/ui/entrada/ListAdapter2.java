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

public class ListAdapter2 extends ArrayAdapter<datosDocumento> {

    public ListAdapter2(Context context, ArrayList<datosDocumento> userArrayLister){
        super(context, R.layout.list_view_datos_doc, userArrayLister);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        datosDocumento user = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_datos_doc, parent, false);
        }

        TextView idNUMEROSAP = convertView.findViewById(R.id.idNUMEROSAP);
        TextView bodegaOrigen = convertView.findViewById(R.id.bodegaOrigen);
        TextView bodegaDestino = convertView.findViewById(R.id.bodegaDestino);
        TextView nTransportista = convertView.findViewById(R.id.nTransportista);
        TextView nTarimas = convertView.findViewById(R.id.cantidadTarimas);

        idNUMEROSAP.setText(user.getNumero_documento());
        bodegaOrigen.setText("Origen\n"+user.getBodega_origen_nombre());
        bodegaDestino.setText("Destino\n"+user.getBodega_destino_nombre());
        nTransportista.setText("Transportado por:\n"+user.getNombre_transportista());
        nTarimas.setText("Tarimas: "+ user.getNumeroTarimas());

        return  convertView;

    }

}
