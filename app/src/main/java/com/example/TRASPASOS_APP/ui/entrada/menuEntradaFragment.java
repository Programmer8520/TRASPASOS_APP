package com.example.TRASPASOS_APP.ui.entrada;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.TRASPASOS_APP.ui.salida.menuSalidaFragment;

public class menuEntradaFragment extends Fragment {

    CardView btnEscanerEntrada;
    CardView btnCerrarTraspasosMenu;
    CardView btnSalir;
    ImageView img;
    public menuEntradaFragment() {
        // Required empty public constructor

    }


    public static menuEntradaFragment newInstance(String param1, String param2) {
        menuEntradaFragment fragment = new menuEntradaFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_menu_navegacion);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.mobile_navigation);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu_entrada, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        TextView tvEmpleado = view.findViewById(R.id.myDashboard);
        tvEmpleado.setText(GlobalClass.nomEmpleado);
        TextView tvDep      = view.findViewById(R.id.dashboard_adminName);
        tvDep.setText(GlobalClass.gDep);
        img         =  view.findViewById(R.id.imgUsr);
        GlobalClass.showImage(img,getContext(), getActivity().getSupportFragmentManager());

        btnSalir = view.findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_home);
            }
        });

        btnEscanerEntrada           = view.findViewById(R.id.btnEscanerEntrada);
        btnEscanerEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.entradaFragment);
            }
        });

        btnCerrarTraspasosMenu = view.findViewById(R.id.btnCerrarTraspasosMenu);
        btnCerrarTraspasosMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.cerrarTraspasosFragment);
            }
        });

    }



}
