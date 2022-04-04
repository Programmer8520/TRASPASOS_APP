package com.example.TRASPASOS_APP.ui.entrada;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.TRASPASOS_APP.GlobalClass;
import com.example.TRASPASOS_APP.R;
import com.example.TRASPASOS_APP.ui.salida.gSalida;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class verificacionFragment extends Fragment {

    private ViewPager2 vpEntradaTarimas;

    private EditText etnombre_montacarguista, etnombre_analista, etnombre_guardia
            , ettarimas,  etconductor, etnombre_transportista,
            etbodega_destino_nombre, etbodega_origen_nombre, etObservaciones;

    private TextView textView;

    private String nombre_montacarguista, nombre_analista, nombre_guardia
            , tarimas,  conductor, nombre_transportista,
            bodega_destino_nombre, bodega_origen_nombre, observaciones;

    private Button btnConfirmar;
    public String fecha;

    public verificacionFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceStatement){
        super.onCreate(savedInstanceStatement);



        vpEntradaTarimas = (ViewPager2) getActivity().findViewById(R.id.vpEntradaTarimas);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_verificacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        onCreate(savedInstanceState);
        textView = view.findViewById(R.id.numeroSAP);

        etnombre_montacarguista = view.findViewById(R.id.editMontacargista);
        etnombre_analista = view.findViewById(R.id.editAnalista);
        etnombre_guardia = view.findViewById(R.id.editGuardia);
        ettarimas = view.findViewById(R.id.editTarimasEntrada);
        etconductor = view.findViewById(R.id.editTransporte);
        etnombre_transportista = view.findViewById(R.id.editNombre);
        etbodega_destino_nombre = view.findViewById(R.id.editBodegaD);
        etbodega_origen_nombre = view.findViewById(R.id.editBodegaO);
        etObservaciones = view.findViewById(R.id.editObservaciones);
        btnConfirmar = view.findViewById(R.id.btnConfirmar);

        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                bodega_origen_nombre = bundle.getString("bodegaOrigen");
                bodega_destino_nombre = bundle.getString("bodegaDestino");
                nombre_montacarguista = bundle.getString("nMontacarguista");
                nombre_analista = bundle.getString("nAnalista");
                nombre_guardia = bundle.getString("nGuardia");
                nombre_transportista = bundle.getString("transportista");
                conductor = bundle.getString("conductor");
                tarimas = bundle.getString("tarimas");
                observaciones = bundle.getString("observaciones");

                textView.setText(gEntrada.paramNumeroSolicitudTraspaso);
                etnombre_montacarguista.setText(nombre_montacarguista);
                etnombre_analista.setText(nombre_analista);
                etnombre_guardia.setText(nombre_guardia);
                ettarimas.setText(tarimas);
                etconductor.setText(conductor);
                etnombre_transportista.setText(nombre_transportista);
                etbodega_destino_nombre.setText(bodega_destino_nombre);
                etbodega_origen_nombre.setText(bodega_origen_nombre);
                etObservaciones.setText(observaciones);

            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext().getApplicationContext(), "CONFIRMADO", Toast.LENGTH_SHORT).show();

                obtenerFechaBaseDatos(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerFechaBD/");
                GlobalClass.actualizarEstatusDeTraspaso(GlobalClass.gUrl+GlobalClass.gLink+"/HandHeld/actualizarEstatusDeTraspaso/?estatus=204&id="+gEntrada.paramIDRegistrado, getContext());
                vpEntradaTarimas.setCurrentItem(2);
            }
        });

    }

    private String obtenerFechaBaseDatos(String URL)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.isEmpty()) {
                    gEntrada.llegadaTransportistaDestino=response;

                }else {

                    GlobalClass.openDialog("ERROR AL OBTENER FECHA","Error para obtener la fecha de la base de datos","ERROR",getActivity().getSupportFragmentManager());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        return fecha;

    }
}
