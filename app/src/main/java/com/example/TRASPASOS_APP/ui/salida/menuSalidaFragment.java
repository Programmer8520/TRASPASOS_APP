package com.example.TRASPASOS_APP.ui.salida;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.TRASPASOS_APP.GlobalClass;
import com.example.TRASPASOS_APP.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class menuSalidaFragment extends Fragment {

    CardView btnEscanerSalida,btnSeguimiento, btnRecuperarEscaneo ,btnSalir;

    ImageView img;
    public menuSalidaFragment() {
        // Required empty public constructor

    }


    public static menuSalidaFragment newInstance(String param1, String param2) {
        menuSalidaFragment fragment = new menuSalidaFragment();
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

        View v = inflater.inflate(R.layout.fragment_menu_salida, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        //en este apartado podemos visualizar la parte donde vemos
        //la foto del empleado y su nombre
        TextView tvEmpleado = view.findViewById(R.id.myDashboard);
        tvEmpleado.setText(GlobalClass.nomEmpleado);
        TextView tvDep      = view.findViewById(R.id.dashboard_adminName);
        tvDep.setText(GlobalClass.gDep);
        img         =  view.findViewById(R.id.imgUsr);
        GlobalClass.showImage(img,getContext(), getActivity().getSupportFragmentManager());

        btnEscanerSalida           = view.findViewById(R.id.btnEscanerSalida);
        btnSeguimiento             = view.findViewById(R.id.btnSeguimiento);
        btnRecuperarEscaneo        = view.findViewById(R.id.btnRecuperarEscaneo);
        btnSalir = view.findViewById(R.id.btnSalirS);

        btnEscanerSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.salidaFragment);
            }
        });

        btnSeguimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.menuSeguimientoFragment);
            }
        });

        btnRecuperarEscaneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { navController.navigate(R.id.recuperacionFragment); }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_home);
            }
        });

    }

}