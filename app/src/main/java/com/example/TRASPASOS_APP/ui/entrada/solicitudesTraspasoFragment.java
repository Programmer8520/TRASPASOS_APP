package com.example.TRASPASOS_APP.ui.entrada;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.TRASPASOS_APP.GlobalClass;
import com.example.TRASPASOS_APP.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class solicitudesTraspasoFragment extends Fragment {

    private Handler h = new Handler();
    private ProgressDialog pCargando;

    private ListView solicitudesDeTraspaso;
    private ListAdapterCierreDeTraspasos adpDatos;
    private ArrayList arDatosDocumento;


    private String numSAP, uuid, idregistrado;
    private String nombre_montacarguista, nombre_analista, nombre_guardia
            , salida_rampa,  conductor, nombre_transportista,
            bodega_destino_nombre, bodega_origen_nombre, cnt_tarimas, observaciones;

    public solicitudesTraspasoFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_solicitudes_traspaso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arDatosDocumento = new ArrayList();
        adpDatos = new ListAdapterCierreDeTraspasos(getContext(), arDatosDocumento);

        solicitudesDeTraspaso = view.findViewById(R.id.solicitudesDeTraspaso);
        solicitudesDeTraspaso.setAdapter(adpDatos);
        mensajeActualizandoDatos();

        solicitudesDeTraspaso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                datosDocumento d = (datosDocumento) arDatosDocumento.get(position);

                gEntrada.paramIDRegistrado = d.getIdregistrado();
                gEntrada.paramUUID = d.getUuid();
                gEntrada.paramNumeroSolicitudTraspaso = d.getNumero_documento();
                gEntrada.paramNombreMontacarguista = d.getNombre_montacarguista();
                gEntrada.paramNombreAnalistaInventarios = d.getNombre_analista();
                gEntrada.paramNombreGuardiaSeguridad = d.getNombre_guardia();
                gEntrada.paramNombreTransportista = d.getNombre_transportista();
                gEntrada.paramConductor = d.getConductor();
                gEntrada.paramBodegaOrigenNombre = d.getBodega_origen_nombre();
                gEntrada.paramBodegaDestinoNombre = d.getBodega_destino_nombre();
                gEntrada.tarimas = d.getNumeroTarimas();
                gEntrada.paramObservaciones = d.getObservaciones();



            }
        });

    }

    public void mensajeActualizandoDatos(){

        obtenerListaDeTrasladosSAP(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerTraspasosCompletamenteValidados");

        pCargando = new ProgressDialog(getActivity());
        pCargando.setMessage("OBTENIENDO TRASPASOS EN PROCESO");
        pCargando.setCancelable(false);
        pCargando.show();

    }

    public void obtenerListaDeTrasladosSAP(String URL){

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);

                            idregistrado = jsonObject.getString("idregistrado");
                            uuid = jsonObject.getString("uuid");
                            numSAP = jsonObject.getString ("numero_documento");
                            nombre_montacarguista = jsonObject.getString("nombre_montacarguista");
                            nombre_analista = jsonObject.getString("nombre_analista");
                            nombre_guardia = jsonObject.getString("nombre_guardia");
                            salida_rampa = jsonObject.getString("salida_rampa");
                            conductor = jsonObject.getString("conductor");
                            nombre_transportista = jsonObject.getString("nombre_transportista");
                            bodega_origen_nombre = jsonObject.getString("bodega_origen_nombre");
                            bodega_destino_nombre = jsonObject.getString("bodega_destino_nombre");

                            observaciones = jsonObject.getString("observaciones");

                            mostrarDato(idregistrado, uuid, bodega_origen_nombre, bodega_destino_nombre, nombre_transportista, numSAP, cnt_tarimas, conductor, salida_rampa, nombre_analista, nombre_montacarguista, nombre_guardia, observaciones);
                            pCargando.dismiss();

                        } catch (JSONException e) {
                            // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }
                } else {
                    GlobalClass.openDialog("ERROR AL OBTENER DATOS","El JSON viene vacio","ERROR",getActivity().getSupportFragmentManager());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: NO HAY TRASPASOS EN PROCESO!","ERROR",getActivity().getSupportFragmentManager());
                if(pCargando.isShowing()){
                    pCargando.dismiss();
                }
            }
        });
        JAR.setRetryPolicy(new DefaultRetryPolicy(
                65000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);

    }

    public void mostrarDato(String id, String uuid, String bodega_origen_nombre, String bodega_destino_nombre, String nombre_transportista, String numero_documento, String numero_tarimas, String conductor, String salida_rampa, String nombre_analista, String nombre_montacarguista, String nombre_guardia, String observaciones){

        datosDocumento dD = new datosDocumento(id, uuid, bodega_origen_nombre,bodega_destino_nombre, nombre_transportista, numero_documento, numero_tarimas, conductor, salida_rampa, nombre_analista, nombre_montacarguista, nombre_guardia, observaciones);
        arDatosDocumento.add(dD);
        adpDatos.notifyDataSetChanged();


    }

}
