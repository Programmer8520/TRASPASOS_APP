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

public class ListAdapterCierreDeTraspasos extends ArrayAdapter<datosDocumento> {

    public ListAdapterCierreDeTraspasos(Context context, ArrayList<datosDocumento> userArrayLister){
        super(context, R.layout.list_view_solicitudes_trapaso_verificadas, userArrayLister);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        datosDocumento user = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_solicitudes_trapaso_verificadas, parent, false);
        }

        TextView idNUMEROSAP = convertView.findViewById(R.id.idNUMEROSAP);
        TextView bodegaOrigen = convertView.findViewById(R.id.bodegaOrigen);
        TextView bodegaDestino = convertView.findViewById(R.id.bodegaDestino);
        TextView nTransportista = convertView.findViewById(R.id.nTransportista);

        idNUMEROSAP.setText(user.getNumero_documento());
        bodegaOrigen.setText("Origen\n"+user.getBodega_origen_nombre());
        bodegaDestino.setText("Destino\n"+user.getBodega_destino_nombre());
        nTransportista.setText("Transportado por:\n"+user.getNombre_transportista());

        return  convertView;

    }

}
