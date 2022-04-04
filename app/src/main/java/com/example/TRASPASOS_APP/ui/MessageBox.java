package com.example.TRASPASOS_APP.ui;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class  MessageBox extends AppCompatDialogFragment {
    private String titulo, mensaje;

    public MessageBox(String titulo, String mensaje) {
        this.titulo=titulo;
        this.mensaje=mensaje;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


        return builder.create();
    }


}
