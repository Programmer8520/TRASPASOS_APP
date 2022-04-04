package com.example.TRASPASOS_APP.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.TRASPASOS_APP.GlobalClass;
import com.example.TRASPASOS_APP.R;
import com.example.TRASPASOS_APP.ui.salida.gSalida;

public class MessageBoxResumen extends AppCompatDialogFragment {

    View v;
    String msg;

    private TextView textResumen;
    private Button btnSalir;

    public MessageBoxResumen(){

    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setV(View v) {
        this.v = v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_success_dailog, null);
        final NavController navController = Navigation.findNavController(v);

        textResumen = (TextView) view.findViewById(R.id.textMessage);
        textResumen.setText(msg);

        btnSalir = (Button) view.findViewById(R.id.buttonAction);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalClass.actualizarEstatusDeTraspaso(GlobalClass.gUrl+GlobalClass.gLink+"/HandHeld/actualizarEstatusDeTraspaso/?estatus=203&id="+ GlobalClass.id, getContext());

                navController.navigate(R.id.menuSalidaFragment);
                getDialog().dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }


}
