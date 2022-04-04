package com.example.TRASPASOS_APP.ui.salida;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class transporteFragment extends Fragment {

    private ArrayList<String> listTransportista;
    private Spinner sTransportista;
    private ArrayList<String> listTransportistaClave;

    private ArrayList<String> listNombresConductores;
    private ArrayList<String> listPlacasTractor;
    private ArrayList<String> listPlacasRemolque;
    private ArrayList<String> listIDsConductores;
    private Spinner sNombreConductor;

    public String fecha;
    private EditText
            etEntradaRampa,
            etPlacasTractor,
            etPlacasRemolque,
            etSello,
            etObservacion;



    private ViewPager2 vpSalida, vpDatosGenerales;

    private Button btnEntradaRampa, btnGuardarProgreso;

    private ProgressDialog pCargando;

    public transporteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vpSalida=(ViewPager2) getActivity().findViewById(R.id.vpSalida);
        vpDatosGenerales = (ViewPager2) getActivity().findViewById(R.id.vpDatosGenerales);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transporte, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sTransportista = view.findViewById(R.id.sTransportista);
        sNombreConductor = view.findViewById(R.id.sNombresConductores);

        etEntradaRampa= view.findViewById(R.id.etEntradaRampa);
        //etSalidaRampa= view.findViewById(R.id.etSalidaRampa);

        etPlacasTractor= view.findViewById(R.id.etPlacasTractor);
        etPlacasRemolque= view.findViewById(R.id.etPlacasRemolque);
        etSello= view.findViewById(R.id.etSello);
        etObservacion= view.findViewById(R.id.etObservacion);


        obtenerTransportista(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerTransportista/");

        sTransportista.performClick();
        sTransportista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0) {

                    sNombreConductor.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        obtenerNombresOperadores(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerNombresOperadores/");

        sNombreConductor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0)
                {
                    gSalida.paramConductor = sNombreConductor.getSelectedItem().toString();
                    etPlacasTractor.setText(listPlacasTractor.get(i));
                    etPlacasRemolque.setText(listPlacasRemolque.get(i));
                    Log.e("position", "La posicion es:" + i);
                    etSello.requestFocus();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnEntradaRampa= view.findViewById(R.id.btnEntradaRampa);
        btnGuardarProgreso = view.findViewById(R.id.btnGuardarProgreso);
        //btnSalidaRampa= view.findViewById(R.id.btnSalidaRampa);

        btnEntradaRampa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFechaBaseDatos(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerFechaBD/");
                etPlacasRemolque.setEnabled(false);
                etPlacasTractor.setEnabled(false);
                etSello.setEnabled(false);
                etObservacion.setEnabled(false);
                etEntradaRampa.setEnabled(false);
                btnEntradaRampa.setEnabled(false);
                vpDatosGenerales.setUserInputEnabled(false);
                vpSalida.setUserInputEnabled(true);
            }
        });

        btnGuardarProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //en este boton sera donde guardaremos los datos en la BD
                obtenerDatos();
                mensajeGuardandoDatos();
                vpSalida.setCurrentItem(1);
            }
        });
    }

    private void obtenerNombresOperadores(String URL)
    {
        listNombresConductores = new ArrayList<>();
        listNombresConductores.add("Seleccionar Conductor");

        listIDsConductores = new ArrayList<>();
        listIDsConductores.add("");

        listPlacasRemolque = new ArrayList<>();
        listPlacasRemolque.add("");

        listPlacasTractor = new ArrayList<>();
        listPlacasTractor.add("");

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {

                        try {
                            //Intent intent;
                            jsonObject = response.getJSONObject(i);

                            listIDsConductores.add(jsonObject.getString("id_operador"));
                            listNombresConductores.add(jsonObject.getString("nombre_operador"));
                            listPlacasRemolque.add(jsonObject.getString("placas_remolque"));
                            listPlacasTractor.add(jsonObject.getString("placas_tractor"));

                        } catch (JSONException e) {
                            // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }
                    sNombreConductor.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listNombresConductores));

                } else {
                    // Toast.makeText(getActivity(), "No existen registros", Toast.LENGTH_SHORT).show();
                    GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos","ERROR",getActivity().getSupportFragmentManager());
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(), "Error de conexion", Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);
    }

    private void obtenerTransportista(String URL) {
        listTransportista = new ArrayList<String>();
        listTransportista.add("SELECCIONAR");
        listTransportistaClave = new ArrayList<String>();
        listTransportistaClave.add("");


        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {

                        try {
                            //Intent intent;
                            jsonObject = response.getJSONObject(i);

                            listTransportista.add(jsonObject.getString("nombre"));
                            listTransportistaClave.add(jsonObject.getString("clave"));

                        } catch (JSONException e) {
                            // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }
                    sTransportista.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listTransportista));

                } else {
                    // Toast.makeText(getActivity(), "No existen registros", Toast.LENGTH_SHORT).show();
                    GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos","ERROR",getActivity().getSupportFragmentManager());
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(getActivity(), "Error de conexion", Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);

    }

    private void insertarCabeceroCargaTpt(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.isEmpty()) {
                    //Log.e("SE INGRESO ", "OK");
                    String id_obtenido = response.toString();
                    Toast.makeText(getActivity(), "Numero de ID[" + id_obtenido + "]", Toast.LENGTH_SHORT).show();
                    gSalida.paramIDRegistrado = id_obtenido;
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                        vpSalida.setCurrentItem(1);
                    }

                } else {
                    // Log.e("SE INGRESO ", "NO");
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                        //vpSalida.setCurrentItem(1);
                    }
                    GlobalClass.openDialog("ERROR AL INGRESAR DATOS","Error al ingresar datos \n Tipo de error: Algun dato es erroneo","ERROR",getActivity().getSupportFragmentManager());

                }
                Log.e("DEV ", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("ESTA VACIO ", "VACIO");
                GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("paramUUID", gSalida.paramUUID);
                parametros.put("paramBodegaOrigen", gSalida.paramBodegaOrigen);
                parametros.put("paramBodegaOrigenNombre", gSalida.paramBodegaOrigenNombre);
                parametros.put("paramCortina", gSalida.paramCortina);
                parametros.put("paramBodegaDestino", gSalida.paramBodegaDestino);
                parametros.put("paramBodegaDestinoNombre", gSalida.paramBodegaDestinoNombre);
                parametros.put("paramNumeroMontacarguista", gSalida.paramNumeroMontacarguista);
                parametros.put("paramNombreMontacarguista", gSalida.paramNombreMontacarguista);
                parametros.put("paramNumeroAnalistaInventarios", gSalida.paramNumeroAnalistaInventarios);
                parametros.put("paramNombreAnalistaInventarios", gSalida.paramNombreAnalistaInventarios);
                parametros.put("paramNumeroGuardiaSeguridad", gSalida.paramNumeroGuardiaSeguridad);
                parametros.put("paramNombreGuardiaSeguridad", gSalida.paramNombreGuardiaSeguridad);
                parametros.put("paramClaveTransportista", gSalida.paramClaveTransportista);
                parametros.put("paramNombreTransportista", gSalida.paramNombreTransportista);
                parametros.put("paramConductor", gSalida.paramConductor);
                parametros.put("paramPlacasTractor", gSalida.paramPlacasTractor);
                parametros.put("paramPlacasRemolque", gSalida.paramPlacasRemolque);
                parametros.put("paramSellos", gSalida.paramSellos);
                parametros.put("paramLlegadaTransportista", gSalida.paramLlegadaTransportista);
                parametros.put("paramEntradaRampa", gSalida.paramEntradaRampa);
                parametros.put("paramSalidaRampa", gSalida.paramSalidaRampa);
                parametros.put("paramNumeroDocumento", gSalida.paramNumeroDocumento);
                parametros.put("paramCorreoUsuarioAplicacion", gSalida.paramCorreoUsuarioAplicacion);
                parametros.put("paramIdStatus", "200");
                parametros.put("paramObservaciones", gSalida.paramObservaciones);
                parametros.put("paramClaveCargaOriginal", gSalida.paramClave_carga_original);
                parametros.put("paramNaveCargaOriginal", gSalida.paramNave_carga_original);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void mensajeGuardandoDatos() {

        pCargando = new ProgressDialog(getActivity());
        pCargando.setMessage("GUARDANDO DATOS");
        pCargando.setCancelable(false);
        pCargando.show();

        insertarCabeceroCargaTpt(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/insertarCabeceroCargaTpt/");
    }

    private String obtenerFechaBaseDatos(String URL)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.isEmpty()) {
                    Log.e("fecha", "fecha: " + response);
                    etEntradaRampa.setText(response);
                    gSalida.paramSalidaRampa = response;
                    gSalida.paramEntradaRampa = response;
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

    private void obtenerDatos(){

        gSalida.paramClaveTransportista=listTransportistaClave.get(sTransportista.getSelectedItemPosition());
        gSalida.paramNombreTransportista=listTransportista.get(sTransportista.getSelectedItemPosition());
//      gSalida.paramConductor=etConductor.getText().toString();

        gSalida.paramPlacasTractor=etPlacasTractor.getText().toString();
        gSalida.paramPlacasRemolque=etPlacasRemolque.getText().toString();
        gSalida.paramSellos=etSello.getText().toString();
        gSalida.paramNumeroDocumento="-";
        gSalida.paramCorreoUsuarioAplicacion=GlobalClass.gEmail;
        gSalida.paramObservaciones = etObservacion.getText().toString();

    }

}