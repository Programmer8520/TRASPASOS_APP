package com.example.TRASPASOS_APP.ui.salida;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.TRASPASOS_APP.GlobalClass;
import com.example.TRASPASOS_APP.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class datosRecuperadosFragment extends Fragment {

    public String fecha;
    public int positionOfOrigen = 0, positionOfDestino = 1, positionOfNameAnalista = 2;
    private EditText etTarimasR, etBodegaOrigenR, etBodegaDestinoR, etAnalistaR, etGuardiaR, etMontacarguistaR,etConductorR, etPlacasTractorR, etPlacasRemolqueR, etEntradaRampaR, etObservaciones;

    private List ids, uuids, tarimas, origen, destino, bdgOrigen,  bdgDestino,  analista,  guardia,  monta,  conductorLinea,  placasTractor,  placasRemolque,  entrada,  observaciones;
    private ArrayList<String> listAlmacenDestino, listAlmacenOrigen, listAlmacenDestinoClave, listAlmacenOrigenClave, listNumeroAnalista, listNombreAnalista;

    private Spinner sAlmacenOrigen, sAlmacenDestino, sAnalistasDeInventarios;
    private LinearLayout linear1, linear2, linear3;

    private ViewPager2 vpDatosRecuperados;

    private Button btnRecuperarProgreso, btnEditarRegistros;
    private boolean interruptorDeGuardado = false;

    private ProgressDialog pCargando;

    public datosRecuperadosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vpDatosRecuperados=(ViewPager2) getActivity().findViewById(R.id.vpDatosRecuperados);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_datos_recuperados, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTarimasR = view.findViewById(R.id.etTarimasR);
        etBodegaOrigenR = view.findViewById(R.id.etBodegaOrigenR);
        etBodegaDestinoR = view.findViewById(R.id.etBodegaDestinoR);
        etAnalistaR = view.findViewById(R.id.etAnalistaR);
        etGuardiaR = view.findViewById(R.id.etGuardiaR);
        etMontacarguistaR = view.findViewById(R.id.etMontacarguistaR);
        etConductorR = view.findViewById(R.id.etConductorR);
        etPlacasTractorR = view.findViewById(R.id.etPlacasTractorR);
        etPlacasRemolqueR = view.findViewById(R.id.etPlacasRemolqueR);
        etEntradaRampaR = view.findViewById(R.id.etEntradaRampaR);
        etObservaciones = view.findViewById(R.id.etObservaciones);

        sAlmacenOrigen = view.findViewById(R.id.sAlmacenOrigen);
        sAlmacenDestino = view.findViewById(R.id.sAlmacenDestino);
        sAnalistasDeInventarios = view.findViewById(R.id.sAnalistasDeInventarios);

        linear1 = view.findViewById(R.id.linear1);
        linear2 = view.findViewById(R.id.linear2);
        linear3 = view.findViewById(R.id.linear3);

        sAlmacenOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0){
                    gSalida.paramBodegaOrigen = listAlmacenOrigenClave.get(position);
                    gSalida.paramBodegaOrigenNombre = listAlmacenOrigen.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        sAlmacenDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0 && position != positionOfOrigen){
                    gSalida.paramBodegaDestino = listAlmacenDestinoClave.get(position);
                    gSalida.paramBodegaDestinoNombre = listAlmacenDestino.get(position);
                }else{
                    GlobalClass.openDialog("Advertencia Importante","Seleccione Un Destino que sea Diferente del ORIGEN","ERROR",getActivity().getSupportFragmentManager());
                    sAlmacenDestino.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        sAnalistasDeInventarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0){
                    gSalida.paramNumeroAnalistaInventarios = listNumeroAnalista.get(position);
                    gSalida.paramNombreAnalistaInventarios = listNombreAnalista.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        ids = new ArrayList();
        uuids = new ArrayList();
        tarimas = new ArrayList();
        origen = new ArrayList();
        bdgOrigen = new ArrayList();
        destino = new ArrayList();
        bdgDestino = new ArrayList();
        analista = new ArrayList();
        guardia = new ArrayList();
        monta = new ArrayList();
        conductorLinea = new ArrayList();
        placasTractor = new ArrayList();
        placasRemolque = new ArrayList();
        entrada = new ArrayList();
        observaciones = new ArrayList();

        mensajeRecuperandoDatos();
        mostrarSpinners(false, view);
        btnRecuperarProgreso = view.findViewById(R.id.btnRecuperarProgreso);

        btnRecuperarProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (interruptorDeGuardado){
                    //programar el guardado y actualizacion del cabecero
                    actualizarCabeceroConNuevosDatos(GlobalClass.gUrl+GlobalClass.gLink+"/HandHeld/actualizarCabeceroConNuevosDatos/");

                }else{
                    //solo cambiar a la pagina de escaneo}
                    vpDatosRecuperados.setCurrentItem(1);
                }


            }
        });

        btnEditarRegistros = view.findViewById(R.id.btnEditarRegistros);

        btnEditarRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bloquearEditText(true);
                mostrarSpinners(true, view);
                mostrarEditText(false, view);
                mensajeDeCargaDeEdicion();
                interruptorDeGuardado = true;
            }
        });

    }

    private void mensajeRecuperandoDatos(){
        pCargando = new ProgressDialog(getActivity());
        pCargando.setMessage("RECUPERANDO DATOS");
        pCargando.setCancelable(false);
        pCargando.show();

        peticionDeRecuperacionDatos(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/recuperarDatosEscaneo/");
    }

    private void colocarDatos(String tarimas, String bdgOrigen, String bdgDestino, String analista, String guardia, String monta, String conductorLinea, String placasTractor, String placasRemolque, String entrada, String observaciones ){
        etTarimasR.setText(tarimas);
        etBodegaOrigenR.setText(bdgOrigen);
        etBodegaDestinoR.setText(bdgDestino);
        etAnalistaR.setText(analista);
        etGuardiaR.setText(guardia);
        etMontacarguistaR.setText(monta);
        etConductorR.setText(conductorLinea);
        etPlacasTractorR.setText(placasTractor);
        etPlacasRemolqueR.setText(placasRemolque);
        etEntradaRampaR.setText(entrada);
        etObservaciones.setText(observaciones);

        bloquearEditText(false);
    }

    private void bloquearEditText(boolean interruptor){
        etTarimasR.setEnabled(interruptor);
        etBodegaOrigenR.setEnabled(interruptor);
        etBodegaDestinoR.setEnabled(interruptor);
        etAnalistaR.setEnabled(interruptor);
        etGuardiaR.setEnabled(interruptor);
        etMontacarguistaR.setEnabled(interruptor);
        etConductorR.setEnabled(interruptor);
        etPlacasTractorR.setEnabled(interruptor);
        etPlacasRemolqueR.setEnabled(interruptor);
        etEntradaRampaR.setEnabled(interruptor);
        etObservaciones.setEnabled(interruptor);
    }

    private void mostrarSpinners(boolean interruptor, View v) {

        if (interruptor){
            //hacer visibles los SPINNERs
            sAlmacenDestino.setVisibility(v.VISIBLE);
            sAlmacenOrigen.setVisibility(v.VISIBLE);
            sAnalistasDeInventarios.setVisibility(v.VISIBLE);
        }else{
            //hacer invisibles os SPINNERs
            sAlmacenDestino.setVisibility(v.INVISIBLE);
            sAlmacenOrigen.setVisibility(v.INVISIBLE);
            sAnalistasDeInventarios.setVisibility(v.INVISIBLE);
        }

    }

    private void mostrarEditText(boolean interruptor, View v){

        if (interruptor){

            linear1.setVisibility(v.VISIBLE);
            linear2.setVisibility(v.VISIBLE);
            linear3.setVisibility(v.VISIBLE);
        }else{

            linear1.setVisibility(v.INVISIBLE);
            linear2.setVisibility(v.INVISIBLE);
            linear3.setVisibility(v.INVISIBLE);
        }
    }

    private void mensajeDeCargaDeEdicion(){
        obtenerAlmacenes(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerAlmacen/");
        obtenerAnalistasRecientes(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerAnalistasRecientes/");

        pCargando = new ProgressDialog(getActivity());
        pCargando.setMessage("OBTENIENDO DATOS");
        pCargando.setCancelable(false);
        pCargando.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                sAlmacenOrigen.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listAlmacenOrigen));
                sAlmacenDestino.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listAlmacenDestino));
                sAnalistasDeInventarios.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listNombreAnalista));

                if (pCargando.isShowing()) {
                    pCargando.dismiss();
                    //sAlmacenOrigen.performClick();
                    sAlmacenOrigen.setSelection(positionOfOrigen);
                    sAlmacenDestino.setSelection(positionOfDestino);
                    sAnalistasDeInventarios.setSelection(positionOfNameAnalista);
                }
            }
        }, 1000);
    }

    private void obtenerAlmacenes(String URL){
        listAlmacenOrigen = new ArrayList<String>();
        listAlmacenOrigen.add("SELECCIONAR");
        listAlmacenOrigenClave = new ArrayList<String>();
        listAlmacenOrigenClave.add("");

        listAlmacenDestino = new ArrayList<String>();
        listAlmacenDestino.add("SELECCIONAR");
        listAlmacenDestinoClave = new ArrayList<String>();
        listAlmacenDestinoClave.add("");

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {

                        try {
                            //Intent intent;
                            jsonObject = response.getJSONObject(i);

                            if (gSalida.paramBodegaDestinoNombre.equals(jsonObject.getString("nombre"))){
                                positionOfDestino = i+1;
                                //Toast.makeText(getContext(), "Destino Es:"+positionOfDestino, Toast.LENGTH_SHORT).show();
                            }

                            if (gSalida.paramBodegaOrigenNombre.equals(jsonObject.getString("nombre"))){
                                positionOfOrigen = i+1;
                            }

                            listAlmacenOrigen.add(jsonObject.getString("nombre"));
                            listAlmacenOrigenClave.add(jsonObject.getString("clave"));

                            listAlmacenDestino.add(jsonObject.getString("nombre"));
                            listAlmacenDestinoClave.add(jsonObject.getString("clave"));

                        } catch (JSONException e) {
                            //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }


                } else {
                    //Toast.makeText(getActivity(), "No existen registros", Toast.LENGTH_SHORT).show();
                    GlobalClass.openDialog("NO HAY DATOS","No existen datos","ERROR",getActivity().getSupportFragmentManager());
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "Error de conexion", Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);
    }

    private void obtenerAnalistasRecientes(String URL) {

        listNombreAnalista = new ArrayList<String>();
        listNombreAnalista.add("SELECCIONAR");
        listNumeroAnalista = new ArrayList<String>();
        listNumeroAnalista.add("");

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {

                        try {
                            //Intent intent;
                            jsonObject = response.getJSONObject(i);

                            if (gSalida.paramNombreAnalistaInventarios.equals(jsonObject.getString("nombre_analista"))){
                                positionOfNameAnalista = i+1;
                            }

                            listNumeroAnalista.add(jsonObject.getString("numero_analista"));
                            listNombreAnalista.add(jsonObject.getString("nombre_analista"));


                        } catch (JSONException e) {
                            //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }


                } else {
                    //Toast.makeText(getActivity(), "No existen registros", Toast.LENGTH_SHORT).show();
                    GlobalClass.openDialog("NO HAY DATOS","No existen datos","ERROR",getActivity().getSupportFragmentManager());
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "Error de conexion", Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);
    }

    private void mostrarDialogoDeRecuperacion(){

        String[] id = new String[entrada.size()];

        id = (String[]) entrada.toArray(id);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione El Traspaso Que Desea Recuperar")
                .setItems(id, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                gSalida.paramUUID = uuids.get(i).toString();
                gSalida.paramIDRegistrado = ids.get(i).toString();

                gSalida.paramBodegaOrigen = origen.get(i).toString();
                gSalida.paramBodegaOrigenNombre = bdgOrigen.get(i).toString();
                gSalida.paramBodegaDestino = destino.get(i).toString();
                gSalida.paramBodegaDestinoNombre = bdgDestino.get(i).toString();

                gSalida.paramNombreMontacarguista = monta.get(i).toString();
                gSalida.paramNombreAnalistaInventarios = analista.get(i).toString();
                gSalida.paramNombreGuardiaSeguridad = guardia.get(i).toString();

                gSalida.paramConductor = conductorLinea.get(i).toString();

                gSalida.paramPlacasTractor = placasTractor.get(i).toString();
                gSalida.paramPlacasRemolque = placasRemolque.get(i).toString();

                gSalida.paramEntradaRampa = entrada.get(i).toString();
                gSalida.paramCorreoUsuarioAplicacion = GlobalClass.gEmail;
                gSalida.paramObservaciones = observaciones.get(i).toString();

                colocarDatos(tarimas.get(i).toString(),
                        bdgOrigen.get(i).toString(),
                        bdgDestino.get(i).toString(),
                        analista.get(i).toString(),
                        guardia.get(i).toString(),
                        monta.get(i).toString(),
                        conductorLinea.get(i).toString(),
                        placasTractor.get(i).toString(),
                        placasRemolque.get(i).toString(),
                        entrada.get(i).toString(),
                        observaciones.get(i).toString());

                //Toast.makeText(getContext(), "Selecciono: "+ids.get(i), Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }

    private void peticionDeRecuperacionDatos(String URL){
        JsonArrayRequest JAR = new JsonArrayRequest( URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);

                            ids.add(jsonObject.getString("id"));
                            uuids.add(jsonObject.getString("uuid"));
                            tarimas.add(jsonObject.getString("cantidad_tarimas"));
                            origen.add(jsonObject.getString("bodega_origen"));
                            bdgOrigen.add(jsonObject.getString("bodega_origen_nombre"));
                            destino.add(jsonObject.getString("bodega_destino"));
                            bdgDestino.add(jsonObject.getString("bodega_destino_nombre"));
                            analista.add(jsonObject.getString("nombre_analista"));
                            guardia.add(jsonObject.getString("nombre_guardia"));
                            monta.add(jsonObject.getString("nombre_montacarguista"));
                            conductorLinea.add(jsonObject.getString("conductor"));
                            placasTractor.add(jsonObject.getString("placas_tractor"));
                            placasRemolque.add(jsonObject.getString("placas_remolque"));
                            entrada.add(jsonObject.getString("entrada_rampa"));
                            observaciones.add(jsonObject.getString("observaciones"));

                            if (pCargando.isShowing()) {
                                pCargando.dismiss();
                            }

                        } catch (JSONException e) {
                            if (pCargando.isShowing()) {
                                pCargando.dismiss();
                            }
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }


                    }

                    mostrarDialogoDeRecuperacion();

                } else {
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                    }
                    GlobalClass.openDialog("No hay datos para mostrar","No hay datos para mostrar","ERROR",getActivity().getSupportFragmentManager());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());

            }
        }
        );

        JAR.setRetryPolicy(new DefaultRetryPolicy(
                33000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);
    }

    private void actualizarCabeceroConNuevosDatos(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.isEmpty()) {

                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                    }
                    GlobalClass.openDialog("Datos Actualizados","Los datos que se selecionaron fueron actualizados correctamente en  la base de datos","EXITO",getActivity().getSupportFragmentManager());
                    vpDatosRecuperados.setCurrentItem(1);

                } else {
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();

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

                parametros.put("paramID", gSalida.paramIDRegistrado);
                parametros.put("paramUUID", gSalida.paramUUID);

                parametros.put("paramBodegaOrigen", gSalida.paramBodegaOrigen);
                parametros.put("paramBodegaOrigenNombre", gSalida.paramBodegaOrigenNombre);
                parametros.put("paramBodegaDestino", gSalida.paramBodegaDestino);
                parametros.put("paramBodegaDestinoNombre", gSalida.paramBodegaDestinoNombre);
                parametros.put("paramNumeroAnalistaInventarios", gSalida.paramNumeroAnalistaInventarios);
                parametros.put("paramNombreAnalistaInventarios", gSalida.paramNombreAnalistaInventarios);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

}