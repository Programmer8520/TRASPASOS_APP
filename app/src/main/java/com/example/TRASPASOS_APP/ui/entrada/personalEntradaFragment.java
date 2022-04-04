package com.example.TRASPASOS_APP.ui.entrada;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.LongSummaryStatistics;
import java.util.Map;

public class personalEntradaFragment extends Fragment {
    private EditText etMontacarguista, etAnalista, etGuardia, etNMontacarguista, etNAnalista, etNGuardia, etEntradaRampa;
    private Button btnEntradaRampa, btnGuardarDatosEntrada;
    private String fecha;
    private ViewPager2 vpEntrada;
    private ProgressDialog pCargando;

    public personalEntradaFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        vpEntrada=(ViewPager2) getActivity().findViewById(R.id.vpEntrada);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_personal_entrada, container, false);
    }


    public int main(){


        return 0;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        etMontacarguista = view.findViewById(R.id.etMontacarguista);
        etAnalista = view.findViewById(R.id.etAnalistaa);
        etGuardia = view.findViewById(R.id.etGuardiaa);
        etMontacarguista.setKeyListener(null);
        etAnalista.setKeyListener(null);
        etGuardia.setKeyListener(null);

        btnEntradaRampa = view.findViewById(R.id.btnEntradaRampaa);
        btnGuardarDatosEntrada = view.findViewById(R.id.btnGuardarDatosEntrada);
        etEntradaRampa = view.findViewById(R.id.etEntradaRampaa);


        etNMontacarguista = view.findViewById(R.id.etNMontacarguista);
        etNAnalista = view.findViewById(R.id.etNAnalistaa);
        etNGuardia = view.findViewById(R.id.etNGuardiaa);

        etNMontacarguista.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    obtenerEmpleado(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerEmpleado/?nEmpleado="+etNMontacarguista.getText()+"", etMontacarguista,etNAnalista,etNMontacarguista);
                    return true;
                }
                return false;
            }
        });

        etNMontacarguista.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    obtenerEmpleado(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerEmpleado/?nEmpleado="+etNMontacarguista.getText()+"", etMontacarguista);

                }
            }
        });

        etNAnalista.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    obtenerEmpleado(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerEmpleado/?nEmpleado="+etNAnalista.getText()+"", etAnalista,etNGuardia,etNAnalista);


                    return true;
                }
                return false;
            }
        });

        etNAnalista.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    obtenerEmpleado(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerEmpleado/?nEmpleado="+etNAnalista.getText()+"", etAnalista);

                }
            }
        });

        etNGuardia.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    obtenerEmpleado(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerEmpleado/?nEmpleado="+etNGuardia.getText()+"", etGuardia, etNGuardia);

                    return true;
                }
                return false;
            }
        });

        etNGuardia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    obtenerEmpleado(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerEmpleado/?nEmpleado="+etNGuardia.getText()+"", etGuardia, etNGuardia);

                }
            }
        });

        btnEntradaRampa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //le programamos la funcion que nos va poner la fecha en el text view
                String currentTime = obtenerFechaBaseDatos(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerFechaBD/");
                etEntradaRampa.setText(currentTime);
                gEntrada.paramEntradaRampaDestino = etEntradaRampa.getText().toString();
                gEntrada.paramSalidaRampaDestino = gEntrada.paramEntradaRampaDestino;
                etNMontacarguista.setEnabled(false);
                etMontacarguista.setEnabled(false);
                etNAnalista.setEnabled(false);
                etAnalista.setEnabled(false);
                etNGuardia.setEnabled(false);
                etGuardia.setEnabled(false);
                etEntradaRampa.setEnabled(false);

            }
        });

        btnGuardarDatosEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerDatos();
                guardarEntradaTraspaso(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/insertarCabeceroCargaEntrada/");

            }
        });

    }

    private void obtenerEmpleado(String URL, EditText et, EditText etSiguiente, EditText etActual) {

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {


                    try {
                        //Intent intent;
                        jsonObject = response.getJSONObject(0);

                        et.setText(jsonObject.getString("nombre"));
                        etSiguiente.requestFocus();

                    } catch (JSONException e) {
                        //  Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }


                } else {
                    Toast.makeText(getActivity(), "No se encontro el numero de Empleado", Toast.LENGTH_SHORT).show();
                    et.setText("");
                    etActual.requestFocus();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(getActivity(), "Error de conexion", Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);



    }

    private void obtenerEmpleado(String URL, EditText et,  EditText etActual) {

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {

                    try {
                        //Intent intent;
                        jsonObject = response.getJSONObject(0);
                        et.setText(jsonObject.getString("nombre"));

                        //este es el ultimo "obtener empleado" que se usa
                       // obtenerDatos();


                    } catch (JSONException e) {
                        //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }


                } else {
                    Toast.makeText(getActivity(), "No se encontro el numero de empleado", Toast.LENGTH_SHORT).show();
                    et.setText("");
                    etActual.requestFocus();
                    vpEntrada.setCurrentItem(0);
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

    private void obtenerEmpleado(String URL, EditText et) {
        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (response.length() != 0) {
                    try {
                        //Intent intent;
                        jsonObject = response.getJSONObject(0);
                        et.setText(jsonObject.getString("nombre"));
                    } catch (JSONException e) {
                        //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }


                } else {
                    et.setText("");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);
    }

    private void obtenerDatos(){

        gEntrada.paramNumeroMontacarguistaEntrada=etNMontacarguista.getText().toString();
        gEntrada.paramNombreMontacarguistaEntrada=etMontacarguista.getText().toString();
        Log.e("nombre1", gEntrada.paramNombreMontacarguistaEntrada);
        gEntrada.paramNumeroAnalistaInventariosEntrada=etNAnalista.getText().toString();
        gEntrada.paramNombreAnalistaInventariosEntrada=etAnalista.getText().toString();
        Log.e("nombre2", gEntrada.paramNombreAnalistaInventariosEntrada);
        gEntrada.paramNumeroGuardiaSeguridadEntrada=etNGuardia.getText().toString();
        gEntrada.paramNombreGuardiaSeguridadEntrada=etGuardia.getText().toString();
        gEntrada.paramNumeroDocumento="-";
        gEntrada.paramCorreoUsuarioEntrada = GlobalClass.gEmail;

    }

    private void guardarEntradaTraspaso(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.isEmpty()) {

                    Toast.makeText(getContext().getApplicationContext(), "DATOS GUARDADOS", Toast.LENGTH_SHORT).show();
                    vpEntrada.setUserInputEnabled(false);
                    vpEntrada.setCurrentItem(1);

                } else {

                    GlobalClass.openDialog("ERROR AL INGRESAR DATOS","Error al ingresar datos \n Tipo de error: "+ response.toString(),"ERROR",getActivity().getSupportFragmentManager());

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

                parametros.put("paramUUID", gEntrada.paramUUID);
                parametros.put("paramNumeroMontacarguista", gEntrada.paramNumeroMontacarguistaEntrada);
                parametros.put("paramNombreMontacarguista", gEntrada.paramNombreMontacarguistaEntrada);
                parametros.put("paramNumeroAnalistaInventarios", gEntrada.paramNumeroAnalistaInventariosEntrada);
                parametros.put("paramNombreAnalistaInventarios", gEntrada.paramNombreAnalistaInventariosEntrada);
                parametros.put("paramNumeroGuardiaSeguridad", gEntrada.paramNumeroGuardiaSeguridadEntrada);
                parametros.put("paramNombreGuardiaSeguridad", gEntrada.paramNombreGuardiaSeguridadEntrada);
                parametros.put("paramLlegadaTransportistaDestino", gEntrada.llegadaTransportistaDestino);
//              parametros.put("paramEntradaRampa", gEntrada.paramEntradaRampaDestino);
//              parametros.put("paramSalidaRampa", gEntrada.paramSalidaRampaDestino);
                parametros.put("paramNumeroDocumento", gEntrada.paramNumeroDocumento);
                parametros.put("paramCorreoUsuarioAplicacion", gEntrada.paramCorreoUsuarioEntrada);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
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
}
