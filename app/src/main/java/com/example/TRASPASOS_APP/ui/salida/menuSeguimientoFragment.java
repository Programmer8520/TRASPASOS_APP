package com.example.TRASPASOS_APP.ui.salida;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.TRASPASOS_APP.GlobalClass;
import com.example.TRASPASOS_APP.R;

public class menuSeguimientoFragment extends Fragment {

    CardView btnTiempoOp,btnAlmacenDest, btnTarimaProd, btnTarimaTras;

    public menuSeguimientoFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_menu_navegacion);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.menuSalidaFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu_seguimiento, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        btnTiempoOp = view.findViewById(R.id.btnTiempoOp);
        btnAlmacenDest = view.findViewById(R.id.btnAlmacenDest);
        btnTarimaProd = view.findViewById(R.id.btnTarimaProd);
        btnTarimaTras = view.findViewById(R.id.btnTarimaTras);

        Bundle datosAEnviar = new Bundle();
        btnTiempoOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datosAEnviar.putInt("tipo", 0);
                navController.navigate(R.id.seguimientoFragment, datosAEnviar);
            }
        });

        btnAlmacenDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datosAEnviar.putInt("tipo", 1);
                navController.navigate(R.id.seguimientoFragment, datosAEnviar);
            }
        });

        btnTarimaProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datosAEnviar.putInt("tipo", 2);
                navController.navigate(R.id.seguimientoFragment, datosAEnviar);
            }
        });

        btnTarimaTras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datosAEnviar.putInt("tipo", 3);
                navController.navigate(R.id.seguimientoFragment, datosAEnviar);
            }
        });


    }

}