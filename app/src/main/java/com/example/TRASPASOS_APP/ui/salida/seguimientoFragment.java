package com.example.TRASPASOS_APP.ui.salida;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.TRASPASOS_APP.GlobalClass;
import com.example.TRASPASOS_APP.R;
import com.example.TRASPASOS_APP.ui.DatePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class seguimientoFragment extends Fragment {

    private ListAdapterSeguimientoOperacion adpSeguimientoOperacion;
    private ListAdapterSeguimientoDestino adpSeguimientoDestino;
    private ListAdapterSeguimientoTarima.ListAdapterSeguimientoProducto adpSeguimientoProducto;
    private ListAdapterSeguimientoTarima adpSeguimientoTarima;


    private EditText fechaInicial, fechaFinal;
    private ListView lvSeguimiento;
    private ArrayList arSeguimiento;
    private int tipo;
    private ImageButton btnBusqueda;

    private ProgressDialog pCargando;

    public seguimientoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seguimiento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBusqueda =  view.findViewById(R.id.btnBusqueda);

        fechaInicial = (EditText) view.findViewById(R.id.etFechaInicial);
        fechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(fechaInicial);
            }

        });

        fechaFinal = (EditText) view.findViewById(R.id.etFechaFinal);
        fechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(fechaFinal);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                    }
                }, 5000);


            }
        });


        btnBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("fechaFinal", fechaFinal.getText().toString());
                Log.e("fechaIncial", fechaInicial.getText().toString());

                Bundle datosRecuperados = getArguments();
                if (datosRecuperados != null) {
                    tipo = datosRecuperados.getInt("tipo");
                    switch (tipo)
                    {
                        case 0:
                            adpSeguimientoOperacion = new ListAdapterSeguimientoOperacion(getContext(),arSeguimiento);
                            lvSeguimiento.setAdapter(adpSeguimientoOperacion);
                            break;
                        case 1:
                            adpSeguimientoDestino = new ListAdapterSeguimientoDestino(getContext(),arSeguimiento);
                            lvSeguimiento.setAdapter(adpSeguimientoDestino);
                            break;
                        case 2:
                            adpSeguimientoProducto = new ListAdapterSeguimientoTarima.ListAdapterSeguimientoProducto(getContext(),arSeguimiento);
                            lvSeguimiento.setAdapter(adpSeguimientoProducto);
                            break;
                        case 3:
                            adpSeguimientoTarima = new ListAdapterSeguimientoTarima(getContext(),arSeguimiento);
                            lvSeguimiento.setAdapter(adpSeguimientoTarima);
                            break;
                    }
                }

                mensajeActualizandoDatos(tipo);
            }
        });


        lvSeguimiento = view.findViewById(R.id.lvSeguimiento);
        arSeguimiento = new ArrayList();



    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                final String selectedDate = year + "-" + twoDigits(month+1) + "-" + twoDigits(day);
                editText.setText(selectedDate);

            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    private void obtenerSeguimientoOperacion(String URL) {

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);

                            String tiempo_de_operacion = jsonObject.getString("tiempo_de_operacion");
                            String Documento_SAP = jsonObject.getString("Documento_SAP");
                            String nombre_montacarguista = jsonObject.getString("nombre_montacarguista");
                            String nombre_analista_inventarios = jsonObject.getString("nombre_analista_inventarios");
                            String nombre_guardia_seguridad = jsonObject.getString("nombre_guardia_seguridad");
                            String conductor = jsonObject.getString("conductor");
                            String placas_tractor = jsonObject.getString("placas_tractor");
                            String placas_remolque = jsonObject.getString("placas_remolque");
                            String entrada_rampa = jsonObject.getString("entrada_rampa");
                            String salida_rampa = jsonObject.getString("salida_rampa");


                           objOperacion  mpOperacion = new objOperacion(
                                   tiempo_de_operacion,
                                   Documento_SAP,
                                   nombre_montacarguista,
                                   nombre_analista_inventarios,
                                   nombre_guardia_seguridad,
                                   conductor,
                                   placas_tractor,
                                   placas_remolque,
                                   entrada_rampa,
                                   salida_rampa,
                                   String.valueOf(i+1)


                           );

                           arSeguimiento.add(mpOperacion);

                        } catch (JSONException e) {
                           // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }
                    adpSeguimientoOperacion.notifyDataSetChanged();
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                    }

                } else {
                    // Toast.makeText(getActivity(), "No se encontro el numero de empleado", Toast.LENGTH_SHORT).show();
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                        GlobalClass.openDialog("ERROR AL OBTENER DATOS","No existen datos","ERROR",getActivity().getSupportFragmentManager());
                    }
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(getActivity(), "Error de conexion", Toast.LENGTH_SHORT).show();
                        if (pCargando.isShowing()) {
                            pCargando.dismiss();
                            GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }

                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);


    }
    private void obtenerSeguimientoDestino(String URL) {

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);

                            String cortina = jsonObject.getString("cortina");
                            String bodega_destino_nombre = jsonObject.getString("bodega_destino_nombre");
                            String Destino = jsonObject.getString("Destino");

                            objDestino  mpDestino= new objDestino(cortina,bodega_destino_nombre,Destino, String.valueOf(i+1));

                            arSeguimiento.add(mpDestino);

                        } catch (JSONException e) {
                            //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }
                    adpSeguimientoDestino.notifyDataSetChanged();
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                    }

                } else {
                    // Toast.makeText(getActivity(), "No se encontro el numero de empleado", Toast.LENGTH_SHORT).show();
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                        GlobalClass.openDialog("ERROR AL OBTENER DATOS","No existen datos","ERROR",getActivity().getSupportFragmentManager());
                    }
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(getActivity(), "Error de conexion", Toast.LENGTH_SHORT).show();
                        if (pCargando.isShowing()) {
                            pCargando.dismiss();
                            GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }

                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);

    }
    private void obtenerSeguimientoProducto(String URL) {

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);

                            String clave = jsonObject.getString("clave");
                            String nombre_producto = jsonObject.getString("nombre_producto");
                            String Tarimas = jsonObject.getString("Tarimas");

                            objProducto  mpProducto = new objProducto(clave,nombre_producto,Tarimas,String.valueOf(i+1));

                            arSeguimiento.add(mpProducto);

                        } catch (JSONException e) {
                            //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }
                    adpSeguimientoProducto.notifyDataSetChanged();
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                    }

                } else {
                    // Toast.makeText(getActivity(), "No se encontro el numero de empleado", Toast.LENGTH_SHORT).show();
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                        GlobalClass.openDialog("ERROR AL OBTENER DATOS","No existen datos","ERROR",getActivity().getSupportFragmentManager());
                    }
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "Error de conexion", Toast.LENGTH_SHORT).show();
                        if (pCargando.isShowing()) {
                            pCargando.dismiss();
                            GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }

                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);


    }

    private void obtenerSeguimientoTarima(String URL) {

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);

                            String numero_documento = jsonObject.getString("numero_documento");
                            String numero_linea = jsonObject.getString("numero_linea");
                            String nombre_producto = jsonObject.getString("nombre_producto");
                            String codigo_ean_upc = jsonObject.getString("codigo_ean_upc");
                            String clave = jsonObject.getString("clave");
                            String cantidad = jsonObject.getString("cantidad");
                            String fecha_carga = jsonObject.getString("fecha_carga");
                            String id_produccion = jsonObject.getString("id_produccion");
                            String no_tarima = jsonObject.getString("no_tarima");
                            String fecha_produccion = jsonObject.getString("fecha_produccion");
                            String no_lote = jsonObject.getString("no_lote");


                            objTarima  mpTarima = new objTarima(numero_documento,
                                    numero_linea,
                                    nombre_producto,
                                    codigo_ean_upc,
                                    clave,
                                    cantidad,
                                    fecha_carga,
                                    id_produccion,
                                    no_tarima,
                                    fecha_produccion,
                                    no_lote,
                                    String.valueOf(i+1)
                                    );

                            arSeguimiento.add(mpTarima);

                        } catch (JSONException e) {
                           // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }
                    adpSeguimientoTarima.notifyDataSetChanged();
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                    }

                } else {
                    // Toast.makeText(getActivity(), "No se encontro el numero de empleado", Toast.LENGTH_SHORT).show();
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                        GlobalClass.openDialog("ERROR AL OBTENER DATOS","No existen datos","ERROR",getActivity().getSupportFragmentManager());
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "Error de conexion", Toast.LENGTH_SHORT).show();

                        if (pCargando.isShowing()) {
                            pCargando.dismiss();
                            GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }

                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);


    }

    private void mensajeActualizandoDatos(int vTipo) {

        switch (vTipo)
        {
            case 0:
                obtenerSeguimientoOperacion(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerSeguimiento/?op="+vTipo+"&fIni="+fechaInicial.getText().toString()+"&fEnd="+fechaFinal.getText().toString()+"");
                break;
            case 1:
                obtenerSeguimientoDestino(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerSeguimiento/?op="+vTipo+"&fIni="+fechaInicial.getText().toString()+"&fEnd="+fechaFinal.getText().toString()+"");
                break;
            case 2:
               obtenerSeguimientoProducto(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerSeguimiento/?op="+vTipo+"&fIni="+fechaInicial.getText().toString()+"&fEnd="+fechaFinal.getText().toString()+"");
                break;
            case 3:
                obtenerSeguimientoTarima(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerSeguimiento/?op="+vTipo+"&fIni="+fechaInicial.getText().toString()+"&fEnd="+fechaFinal.getText().toString()+"");
                break;
        }

        pCargando = new ProgressDialog(getActivity());
        pCargando.setMessage("OBTENIENDO DATOS");
        pCargando.setCancelable(false);
        pCargando.show();



    }


}