package com.example.TRASPASOS_APP.ui.entrada;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.example.TRASPASOS_APP.ui.salida.gSalida;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class escaneoEntradaFragment extends Fragment {

    /***OBJETOS**/
    private ImageButton playImageButton,
            stopImageButton;
   // private ArrayAdapter adpEscaneo;
   private ListAdapterVerificacion adpTarimas;
    private String ClavesEtiqueta = "";
    private ArrayList ClavesEsperadas;
    private ProgressBar pg;
    private ListView lvTarima;
    private Thread crono;
    private Handler h = new Handler();
    private TextView contador, textContador;
    private EditText etProducto;
    private ProgressDialog pCargando;
    private SoundPool soundPool;
    int sonido_correcto, sonido_incorrecto;
    /************/

    /*****VARIABLES*******/
    private ArrayList arTarimas;
    private ArrayList arClave;
    private ArrayList arFechaCarga;
    private ArrayList arNombre_producto;
    private ArrayList arEtiquetasRegistro;
    private ArrayList arEtiquetasEntrada;
    private ArrayList tarimasQueCoinciden;

    private ArrayList numeroLinea, idProduccion, noTarima, loteTarima;
    String orden, prod, ca;
    String cdn, cadenaMaestra;

    private boolean isOn=false;

    private int mili = 0,
            seg = 0,
            min = 0,
            hrs = 0,
            tarimasEscaneadas = 0,
            tarimasPorEscanear = 0, tarimasPEPorcentaje = 0;

    private double tarimasEscaneadasPorcentaje = 0.0;

    private String nombre_producto, fecha_carga, numero_linea, clave, lote, idproduccion, notarima;
    /***********/

    public escaneoEntradaFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        sonido_correcto = soundPool.load(getContext(), R.raw.sonido_correcto, 1);
        sonido_incorrecto = soundPool.load(getContext(), R.raw.sonido_error, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_escaneo_entrada, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        pg = view.findViewById(R.id.progressBar);
        textContador = view.findViewById(R.id.textContador);
        playImageButton = view.findViewById(R.id.playImageButton);
        stopImageButton = view.findViewById(R.id.stopImageButton);
        contador = view.findViewById(R.id.contador);
        lvTarima = view.findViewById(R.id.lvTarima);
        etProducto = view.findViewById(R.id.etProductoo);
        etProducto.requestFocus();

        ClavesEsperadas = new ArrayList();
        arEtiquetasRegistro = new ArrayList();
        arFechaCarga = new ArrayList();
        arClave = new ArrayList();
        arNombre_producto = new ArrayList();
        arEtiquetasEntrada = new ArrayList();
        tarimasQueCoinciden = new ArrayList();

        numeroLinea = new ArrayList();
        idProduccion = new ArrayList();
        noTarima = new ArrayList();
        loteTarima = new ArrayList();

        arTarimas = new ArrayList();
        adpTarimas = new ListAdapterVerificacion(getContext(),arTarimas);

        //pg.setProgress(tarimasEscaneadas); //iniciamos el progreso de la barra

        //programacion del boton de PLAY
        playImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOn = true;
                stopImageButton.setImageDrawable(getResources().getDrawable(R.drawable.stop_player_red, getContext().getTheme()));
                playImageButton.setImageDrawable(getResources().getDrawable(R.drawable.start_player, getContext().getTheme()));
                etProducto.setKeyListener(new DigitsKeyListener());
                etProducto.requestFocus();
            }
        });

        //programacion del boton de STOP
        stopImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOn = false;
                stopImageButton.setImageDrawable(getResources().getDrawable(R.drawable.stop_player, getContext().getTheme()));
                playImageButton.setImageDrawable(getResources().getDrawable(R.drawable.start_player_red, getContext().getTheme()));
                etProducto.setKeyListener(null);
                etProducto.setText("");


            }
        });

        //programacion del cronometro
        crono = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isOn) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();

                        }
                        mili++;
                        if (mili == 999) {
                            seg++;
                            mili = 0;
                        }
                        if (seg == 59) {
                            min++;
                            seg = 0;
                        }
                        if (min == 59) {
                            hrs++;
                            min = 0;
                        }

                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                String sMili = "", sSeg = "", sMin = "", sHrs = "";
                                if (mili < 10) {
                                    sMili = "00" + mili;
                                } else if (mili < 100) {
                                    sMili = "0" + mili;
                                } else {
                                    sMili = "" + mili;
                                }

                                if (seg < 10) {
                                    sSeg = "0" + seg;
                                } else {
                                    sSeg = "" + seg;
                                }

                                if (min < 10) {
                                    sMin = "0" + min;
                                } else {
                                    sMin = "" + min;
                                }

                                if (hrs < 10) {
                                    sHrs = "0" + hrs;
                                } else {
                                    sHrs = "" + hrs;
                                }
                                contador.setText(sHrs + ":" + sMin + ":" + sSeg + ":" + sMili);
                            }
                        });
                    }

                }
            }
        });
        crono.start();

        lvTarima.setAdapter(adpTarimas);

        //obtenemos el detalle de la carga
        mensajeObteniendoDatos();

        pg.setProgress(tarimasEscaneadas);

        etProducto.setOnKeyListener (new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (isOn && etProducto.getText().length()==10) {

                        //etProducto.setKeyListener(new DigitsKeyListener());
                        etProducto.requestFocus();
                        //mensajeObteniendoDatosTarimaEntrante(etProducto.getText().toString());
                        comprobarTarimaEscaneadaContraLista(etProducto.getText().toString());

                        //GlobalClass.actualizarEstatusDeTraspaso(GlobalClass.gUrl+GlobalClass.gLink+"/HandHeld/actualizarEstatusDeTraspaso/?estatus=211&id="+gEntrada.paramIDRegistrado, getContext());


                    }else {
                        //Toast.makeText(getActivity(), "ID INVALIDO", Toast.LENGTH_SHORT).show();
                        AudioSoundPool(getView(), sonido_incorrecto);
                        etProducto.requestFocus();
                        etProducto.setText("");
                    }

                    return true;
                }
                return false;
            }

        });
    }

    private void mensajeObteniendoDatos(){
        //inicializamos nuestro dialogo de carga
        pCargando = new ProgressDialog(getActivity());
        pCargando.setMessage("OBTENIENDO LAS TARIMAS");
        pCargando.setCancelable(false);
        pCargando.show();

        //mandamos a llamar el metodo para obtener las tarimas
        obtenerListaDeClavesDeEtiqueta(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerEtiquetasDeTarimas/?solicitud="+gEntrada.paramNumeroSolicitudTraspaso+"&uuid="+gEntrada.paramUUID+"");
        obtenerListaDeTarimas(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerDetalle/?solicitud="+gEntrada.paramNumeroSolicitudTraspaso+"&uuid="+gEntrada.paramUUID+"");
    }

    private void comprobarTarimaEscaneadaContraLista(String parametro){

        if (ClavesEsperadas.contains(parametro)){

            tarimasEscaneadas++;
            tarimasEscaneadasPorcentaje = (tarimasEscaneadas * 100) / tarimasPEPorcentaje;
            pg.setProgress((int) tarimasEscaneadasPorcentaje);
            tarimasPorEscanear--;
            textContador.setText(tarimasPorEscanear + "Tarimas Pendientes");
            AudioSoundPool(getView(), sonido_correcto);

            if (tarimasPorEscanear == 0){
                isOn = false;
                Toast.makeText(getActivity(), "ESCANEO TERMINADO CON EXITO", Toast.LENGTH_SHORT).show();

                pCargando = new ProgressDialog(getActivity());
                pCargando.setMessage("GENERANDO TRASPASO DE STOCK");
                pCargando.setCancelable(true);
                pCargando.show();

                GlobalClass.actualizarEstatusDeTraspaso(GlobalClass.gUrl+GlobalClass.gLink+"/HandHeld/actualizarEstatusDeTraspaso/?estatus=212&id="+gEntrada.paramIDRegistrado, getContext());
                generarDocumentoSAP(GlobalClass.gURL_SAP + GlobalClass.gLink + "/HandHeld/generarDocumentoSAP_traspaso/?id=" + gEntrada.paramNumeroSolicitudTraspaso);
            }

            etProducto.setText("");
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    etProducto.requestFocus();
                }
            }, 500);
            ClavesEsperadas.remove(parametro);

        }else {
            etProducto.setText("");
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    etProducto.requestFocus();
                }
            }, 500);
        }
    }

    public void AudioSoundPool(View v, int sonido){
        soundPool.play(sonido, 1, 1, 1, 0, 0);

    }

    private void obtenerListaDeClavesDeEtiqueta(String URL){

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            //Intent intent;
                            jsonObject = response.getJSONObject(i);

                            ClavesEsperadas.add(jsonObject.getString("etiqueta"));

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
                GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);

    }

    private void obtenerListaDeTarimas(String URL){

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            //Intent intent;
                            jsonObject = response.getJSONObject(i);
                            tarimasPorEscanear = response.length();
                            tarimasPEPorcentaje = response.length();
                            textContador.setText(tarimasPorEscanear + "Tarimas Pendientes");

                            numero_linea = String.valueOf(i+1);
                            clave = jsonObject.getString ("clave");
                            fecha_carga = jsonObject.getString("fecha_carga");
                            nombre_producto = jsonObject.getString("nombre_producto");
                            lote = jsonObject.getString("no_lote");
                            idproduccion = jsonObject.getString("id_produccion");
                            notarima = jsonObject.getString("no_tarima");



                            arClave.add(clave);
                            arFechaCarga.add(fecha_carga);
                            arNombre_producto.add(nombre_producto);

                            numeroLinea.add(numero_linea);
                            idProduccion.add(idproduccion);
                            noTarima.add(notarima);
                            loteTarima.add(lote);

                            mostrarDato(numero_linea, clave, fecha_carga, nombre_producto, lote);

                            pCargando.dismiss();


                        } catch (JSONException e) {
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
                GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
            }
        });
        JAR.setRetryPolicy(new DefaultRetryPolicy(
                33000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);

    }

    private void mostrarDato(String no_linea, String clave, String fecha_carga, String nombre_producto, String etiqueta){
        datosTarimasEscaneadas sTE = new datosTarimasEscaneadas(no_linea, clave, fecha_carga, nombre_producto, etiqueta);

        arTarimas.add(sTE);
        adpTarimas.notifyDataSetChanged();

    }

    private void generarDocumentoSAP(String URL){
        Toast.makeText(getActivity(), "Generando documento FINAL", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String r = "0123456789";
                if (!response.contains(r) && response.length()==6)
                {
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                    }
                    Toast.makeText(getActivity(), "Numero:"+response, Toast.LENGTH_LONG).show();
                    String numeroSAP = response;
                    gEntrada.paramNumeroDocumento = numeroSAP;
                    dialogoDocumentoSAP(numeroSAP);
                }else
                {
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                    }
                    GlobalClass.openDialog("NO SE PUDO COMPLETAR EL TRASPASO","Lo sentimos, aun no se cuenta con stock existente para concluir el movimiento.\n" +
                            "Se dejara el traspaso en PENDIENTE y se le notificara en cuanto se pueda cerrar.\n","ERROR",getActivity().getSupportFragmentManager());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pCargando.isShowing()) {
                    pCargando.dismiss();
                }

                GlobalClass.openDialog("ERROR DE CONEXION","Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                65000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void dialogoDocumentoSAP(String nD) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final NavController navController = Navigation.findNavController(getView());
        builder.setTitle("DOCUMENTO SAP").setMessage("SU NUMERO DE DOCUMENTO ES\n\t\t"+nD);
        builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                navController.navigate(R.id.menuEntradaFragment);
            }
        }).setNegativeButton("", null).setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}