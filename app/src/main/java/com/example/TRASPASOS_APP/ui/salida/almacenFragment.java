package com.example.TRASPASOS_APP.ui.salida;

import android.app.ProgressDialog;
import android.companion.WifiDeviceFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.LongSummaryStatistics;
import java.util.UUID;

public class almacenFragment extends Fragment {

    private ProgressDialog pCargando;

    private ArrayList<String> listAlmacenOrigen,listAlmacenOrigenClave,listCortina,listCortinaClave,listAlmacenDestino,listAlmacenDestinoClave, listIdNavesSeleccionables, listNavesSeleccionables;
    private String clave_carga_original, nave_carga_original;
    private int positionOfNaveDeCarga = 0;
    private Spinner sAlmacenOrigen,sCortina,sAlmacenDestino, sSeleccionDeNave;
    private ViewPager2 vpDatosGenerales, vpSalida;
    private int contadorEstatus = 0;
    public String fecha = "";
    public int positionOfOrigen = 1, positionOfDestino = 0;

    public almacenFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vpDatosGenerales = (ViewPager2) getActivity().findViewById(R.id.vpDatosGenerales);
        vpSalida = (ViewPager2) getActivity().findViewById(R.id.vpSalida);
        gSalida.paramUUID = UUID.randomUUID().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_almacen, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sSeleccionDeNave = view.findViewById(R.id.sSeleccionDeNave);
        sAlmacenOrigen = view.findViewById(R.id.sAlmacenOrigen);
        sAlmacenDestino = view.findViewById(R.id.sAlmacenDestino);
        sCortina = view.findViewById(R.id.sCortina);

        obtenerUltimoRegistroDeCargaDeNave(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/recuperarDatosDeNaveDeOrigen/");
        mensajeActualizandoDatos();


        listCortina = new ArrayList<String>();
        listCortina.add("SELECCIONAR");
        listCortinaClave = new ArrayList<String>();
        listCortinaClave.add("");
        sCortina.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listCortina));

        sSeleccionDeNave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                //Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                if (position != 0){
                    gSalida.paramClave_carga_original = listIdNavesSeleccionables.get(position);
                    gSalida.paramNave_carga_original = listNavesSeleccionables.get(position);
                    sAlmacenOrigen.performClick();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        sAlmacenOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position != 0) {

                    positionOfOrigen = position;
                    mensajeActualizandoDatosCortina(position);

                } else {

                    listCortina = new ArrayList<String>();
                    listCortina.add("SELECCIONAR");
                    listCortinaClave = new ArrayList<String>();
                    listCortinaClave.add("");
                    sCortina.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listCortina));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }

        });

        sCortina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (sAlmacenOrigen.getSelectedItemPosition() != 0) {
                    contadorEstatus++;
                    if (contadorEstatus > 1) {
                        sAlmacenDestino.performClick();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }

        });

        sAlmacenDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position != 0){
                    if (position != positionOfOrigen){
                        vpDatosGenerales.setCurrentItem(1);
                        obtenerDatos();
                    }else{
                        GlobalClass.openDialog("Advertencia Importante","Seleccione Un Destino que sea Diferente del ORIGEN","ERROR",getActivity().getSupportFragmentManager());
                        sAlmacenDestino.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }

        });

    }

    private void mensajeActualizandoDatos() {
        obtenerNavesInnovador(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerNavesInnovador/");
        obtenerAlmacen(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerAlmacen/");

        pCargando = new ProgressDialog(getActivity());
        pCargando.setMessage("OBTENIENDO DATOS");
        pCargando.setCancelable(false);
        pCargando.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                sSeleccionDeNave.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listNavesSeleccionables));
                sAlmacenOrigen.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listAlmacenOrigen));
                sAlmacenDestino.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listAlmacenDestino));

                if (pCargando.isShowing()) {
                    pCargando.dismiss();
                    //sSeleccionDeNave.performClick();
                    sSeleccionDeNave.setSelection(positionOfNaveDeCarga);
                }
            }
        }, 3000);

    }

    private void obtenerNavesInnovador(String URL) {
        listNavesSeleccionables = new ArrayList<String>();
        listNavesSeleccionables.add("SELECCIONAR");

        listIdNavesSeleccionables = new ArrayList<String>();
        listIdNavesSeleccionables.add("");

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {

                        try {
                            //Intent intent;
                            jsonObject = response.getJSONObject(i);


                            String nave = jsonObject.getString("nave");

                            if (nave.equals(gSalida.paramNave_carga_original)){
                                positionOfNaveDeCarga = i+1;
                                //Toast.makeText(getActivity(), "El id seleccionado es:" + positionOfNaveDeCarga, Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(getActivity(), "No se encontro coincidencia:" + nave + "->" + gSalida.paramNave_carga_original, Toast.LENGTH_SHORT).show();
                            }

                            listIdNavesSeleccionables.add(jsonObject.getString("id_nave"));
                            listNavesSeleccionables.add(jsonObject.getString("nave"));

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

    private void obtenerUltimoRegistroDeCargaDeNave(String URL) {
        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {

                        try {
                            //Intent intent;
                            jsonObject = response.getJSONObject(i);

                            gSalida.paramClave_carga_original = jsonObject.getString("clave");
                            gSalida.paramNave_carga_original = jsonObject.getString("nave");

                            //Toast.makeText(getActivity(), "La Ultima Nave es: " + gSalida.paramNave_carga_original, Toast.LENGTH_SHORT).show();

                            //Log.e("test", gSalida.paramClave_carga_original);
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

    private void mensajeActualizandoDatosCortina(int position) {
        contadorEstatus = 0;
        String clave = listAlmacenOrigenClave.get(position);

        cortina(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerCortina/?ubicacion=" + clave + "");

        pCargando = new ProgressDialog(getActivity());
        pCargando.setMessage("OBTENIENDO DATOS");
        pCargando.setCancelable(false);
        pCargando.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                sCortina.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listCortina));

                if (pCargando.isShowing()) {
                    pCargando.dismiss();
                    sCortina.performClick();

                }
            }
        }, 1000);

    }

    private void obtenerAlmacen(String URL) {
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

    private void cortina(String URL) {
        listCortina = new ArrayList<String>();
        listCortinaClave = new ArrayList<String>();
        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {

                        try {
                            //Intent intent;
                            jsonObject = response.getJSONObject(i);
                            if (i == 0) {
                                listCortina.add(jsonObject.getString("numero_cortina"));
                                listCortinaClave.add(jsonObject.getString("numero_cortina"));
                            } else {

                                listCortina.add("CORTINA " + jsonObject.getString("numero_cortina"));
                                listCortinaClave.add(jsonObject.getString("numero_cortina"));
                            }

                        } catch (JSONException e) {
                            //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }

                } else {
                    //Toast.makeText(getActivity(), "No existen registros", Toast.LENGTH_SHORT).show();
                    GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos ","ERROR",getActivity().getSupportFragmentManager());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "Error de conexionsss", Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);
    }

    private String obtenerFechaBaseDatos(String URL)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.isEmpty()) {
                    gSalida.paramLlegadaTransportista=response;

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

        gSalida.paramCortina=listCortinaClave.get(sCortina.getSelectedItemPosition());

        gSalida.paramBodegaOrigen=listAlmacenOrigenClave.get(sAlmacenOrigen.getSelectedItemPosition());
        gSalida.paramBodegaOrigenNombre=listAlmacenOrigen.get(sAlmacenOrigen.getSelectedItemPosition());

        gSalida.paramBodegaDestino=listAlmacenDestinoClave.get(sAlmacenDestino.getSelectedItemPosition());
        gSalida.paramBodegaDestinoNombre=listAlmacenDestino.get(sAlmacenDestino.getSelectedItemPosition());
        obtenerFechaBaseDatos(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerFechaBD/");

        vpDatosGenerales.setCurrentItem(1);
    }

    private void evitarAlmacenRepetido(String almacenOrigen, String almacenDestino)
    {
        if (almacenOrigen == almacenDestino)
        {

        }
    }

}