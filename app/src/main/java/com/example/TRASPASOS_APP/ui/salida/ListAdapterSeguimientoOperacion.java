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

public class ListAdapterSeguimientoOperacion extends ArrayAdapter<objOperacion> {


    public ListAdapterSeguimientoOperacion(Context context, ArrayList<objOperacion> userArrayList){

        super(context,R.layout.list_view_seguimiento_operacion,userArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        objOperacion operacion = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_seguimiento_operacion,parent,false);

        }

        TextView tiempo = convertView.findViewById(R.id.tiempo);
        TextView docSap = convertView.findViewById(R.id.docSap);
        TextView nomMontacarguista = convertView.findViewById(R.id.nomMontacarguista);
        TextView nomAnalista = convertView.findViewById(R.id.nomAnalista);
        TextView nomGuardia = convertView.findViewById(R.id.nomGuardia);
        TextView nomConductor = convertView.findViewById(R.id.nomConductor);
        TextView placasTractor = convertView.findViewById(R.id.placasTractor);
        TextView placasRemolque = convertView.findViewById(R.id.placasRemolque);
        TextView entradaRampa = convertView.findViewById(R.id.entradaRampa);
        TextView salidaRampa = convertView.findViewById(R.id.salidaRampa);
        TextView contador = convertView.findViewById(R.id.contador);


        tiempo.setText("Tiempo: "+operacion.tiempo_de_operacion);
        docSap.setText("Documneto SAP: "+operacion.Documento_SAP);
        nomMontacarguista.setText("Montacarguista: "+operacion.nombre_montacarguista);
        nomAnalista.setText("Analista: "+operacion.nombre_analista_inventarios);
        nomGuardia.setText("Guardia: "+operacion.nombre_guardia_seguridad);
        nomConductor.setText("Conductor: "+operacion.conductor);
        placasTractor.setText("Placas del Tractor: "+operacion.placas_tractor);
        placasRemolque.setText("Placas del Remolque: "+operacion.placas_remolque);
        entradaRampa.setText("Entrada Rampa: "+operacion.entrada_rampa);
        salidaRampa.setText("Salida Rampa: "+operacion.salida_rampa);
        contador.setText(operacion.numero);



        return convertView;
    }
}
