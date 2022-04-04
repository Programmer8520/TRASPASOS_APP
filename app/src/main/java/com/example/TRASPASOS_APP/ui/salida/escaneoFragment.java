package com.example.TRASPASOS_APP.ui.salida;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
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
import android.widget.AdapterView;
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
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.TRASPASOS_APP.GlobalClass;
import com.example.TRASPASOS_APP.R;
import com.google.android.gms.fido.fido2.api.common.ErrorCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class escaneoFragment extends Fragment {

    /***OBJETOS**/
    private ImageButton playImageButton,
            stopImageButton;
    // private ArrayAdapter adpEscaneo;
    private ListAdapter adpEscaneo;
    private String strJSON = "[";
    private String strValues = "";
    private JSONArray json = new JSONArray();

    private ProgressBar pg;
    private ListView lvTarima;
    private Thread crono;
    private Handler h = new Handler();
    private TextView contador, textContador, tituloEscaneo;
    private EditText etProducto;
    private ProgressDialog pCargando;
    private Button btnGenerarDS, btnMostrarResumen;
    private SoundPool soundPool;
    int sonido_correcto, sonido_incorrecto;

    /************/

    /*****VARIABLES*******/
    private ArrayList
            arEscaneo,
            arRegistros,
            arfecha_produccion,
            arclave,
            arcodigo_barras,
            arno_lote,
            arnombre_producto,
            arclave_etiqueta,
            arno_tarima,
            arIDsInsertados,
            listNombreProducto,listClaveProducto,
            listDatosUnidos;

    private ArrayList arProgramas = new ArrayList();

    String orden, prod, ca;
    public String[] producto, arrayClave;
    private String cdn="", cadenaMaestra="", cdnClaves = "";
    public String fecha;

    private boolean isOn=false, b = false;

    private int mili = 0,
            seg = 0,
            min = 0,
            hrs = 0,
            tarimasEscaneadas = 0;

    /***********/

    public escaneoFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        sonido_correcto = soundPool.load(getContext(), R.raw.sonido_correcto, 1);
        sonido_incorrecto = soundPool.load(getContext(), R.raw.sonido_error, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_escaneo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tituloEscaneo = view.findViewById(R.id.tituloEscaneo);
        playImageButton = view.findViewById(R.id.playImageButton);
        stopImageButton = view.findViewById(R.id.stopImageButton);
        contador = view.findViewById(R.id.contador);
        etProducto = view.findViewById(R.id.etProducto);
        textContador = view.findViewById(R.id.textContadorS);
        btnGenerarDS = view.findViewById(R.id.btnGenerarDocSAP);
        btnMostrarResumen = view.findViewById(R.id.btnMostrarResumen);

        btnGenerarDS.setVisibility(View.INVISIBLE);
        btnMostrarResumen.setVisibility(View.INVISIBLE);

        //arreglos que almacenan los datos que se obtiene al ejecutar el metodo obtenerEinsertarProducto
        arfecha_produccion = new ArrayList();
        arclave = new ArrayList();
        arcodigo_barras = new ArrayList();
        arnombre_producto = new ArrayList();
        arno_lote = new ArrayList();
        arclave_etiqueta = new ArrayList();
        arno_tarima=new ArrayList();
        arIDsInsertados = new ArrayList();

        //estos son los que se usan para insertar en el cabecero
        arRegistros = new ArrayList();

        listNombreProducto = new ArrayList();
        listClaveProducto = new ArrayList();
        listDatosUnidos = new ArrayList();

        arEscaneo = new ArrayList();
        adpEscaneo = new ListAdapter(getContext(),arEscaneo);

        lvTarima = view.findViewById(R.id.lvTarima);
        lvTarima.setAdapter(adpEscaneo);

        arProgramas.add("programa");

        //programacion del boton de PLAY
        playImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOn = true;
                stopImageButton.setImageDrawable(getResources().getDrawable(R.drawable.stop_player_red, getContext().getTheme()));
                playImageButton.setImageDrawable(getResources().getDrawable(R.drawable.start_player, getContext().getTheme()));
                tituloEscaneo.setVisibility(View.VISIBLE);
                btnGenerarDS.setVisibility(View.INVISIBLE);
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
                tituloEscaneo.setVisibility(View.INVISIBLE);
                btnGenerarDS.setVisibility(View.VISIBLE);

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

        etProducto.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (isOn && etProducto.getText().length()==10) {

                        etProducto.setKeyListener(new DigitsKeyListener());
                        etProducto.requestFocus();
                        mensajeObteniendoDatos(etProducto.getText().toString());

                        //crear un metodo que utilice este link del webservice -> obtenerEinsertarProductoTPT

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

        lvTarima.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("ELMINAR TARIMA").setMessage("¿DESEA ELIMINAR ESTE REGISTRO?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setTitle("CONFIRMACION DE ELIMINADO").setMessage("¿ESTAS SEGURO?");
                        builder.setPositiveButton("Si, eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                eliminarRegistroDeTarima(GlobalClass.gUrl+GlobalClass.gLink+"/HandHeld/eliminarRegistroEnDetalle/?id="+arIDsInsertados.get(position).toString());
                                tarimasEscaneadas--;
                                textContador.setText(tarimasEscaneadas + "Tarimas");

                                arIDsInsertados.remove(position);
                                arclave_etiqueta.remove(position);
                                arfecha_produccion.remove(position);
                                arcodigo_barras.remove(position);
                                arno_lote.remove(position);
                                arno_tarima.remove(position);
                                arRegistros.remove(position);
                                arEscaneo.remove(position);

                                lvTarima.setAdapter(adpEscaneo = new ListAdapter(getContext(), arEscaneo));

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                etProducto.setText("");
                                etProducto.requestFocus();
                            }
                        }).setCancelable(false);

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etProducto.setText("");
                        etProducto.requestFocus();
                    }
                }).setCancelable(true);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btnGenerarDS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                limpiarRegistrosDeDuplicados(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/limpiarRegistrosDuplicados/?uuid="+gSalida.paramUUID+"");
                for (int i = 0; i<arRegistros.size(); i++) { obtenerProductosUnicos((String[]) arRegistros.get(i)); }

                pCargando = new ProgressDialog(getActivity());
                pCargando.setMessage("GENERANDO SOLICITUD DE TRASPASO");
                pCargando.setCancelable(false);
                pCargando.show();
                //gSalida.paramSalidaRampa = obtenerFechaBaseDatos(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerFechaBD/");

                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        generarDocumentoSAP(GlobalClass.gURL_SAP + GlobalClass.gLink + "/HandHeld/generarDocumentoSAP_solicitud/?id=" + gSalida.paramIDRegistrado + "&fS="+gSalida.paramSalidaRampa+"");
                    }
                }, 2500);
            }
        });

        btnMostrarResumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalClass.openDialogPro(cadenaMaestra, getArguments(), getView(), getActivity().getSupportFragmentManager(), "resumen");
            }
        });

    }

    //todo el codigo relacionado con los sonidos
    public void AudioSoundPool(View v, int sonido){
        soundPool.play(sonido, 1, 1, 1, 0, 0);
    }

    //todo el codigo que nos permitira eliminar la tarima de nuestro list view
    private void eliminarRegistroDeTarima(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.isEmpty())
                {
                    Toast.makeText(getActivity(), "Tarima Eliminada", Toast.LENGTH_LONG).show();

                }else
                {
                    Toast.makeText(getActivity(), "Tarima NO Eliminada", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                GlobalClass.openDialog("ERROR DE CONEXION","Tipo de error: "+ error.toString(),"ERROR",getActivity().getSupportFragmentManager());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    //todo el codigo relacionado con obtener los datos e insertarlos
    private void mensajeObteniendoDatos(String clave) {
        pCargando = new ProgressDialog(getActivity());
        pCargando.setMessage("OBTENIENDO DATOS");
        pCargando.setCancelable(false);
        pCargando.show();

        if(tarimasEscaneadas>0) {

            if(arclave_etiqueta.contains(clave)) {
                Toast.makeText(getActivity(), "YA ESTA ESCANEADA", Toast.LENGTH_SHORT).show();

                pCargando.dismiss();
                etProducto.setText("");
                etProducto.requestFocus();

                AudioSoundPool(getView(), sonido_incorrecto);
            }
            else {
                //Toast.makeText(getActivity(), "Guardando", Toast.LENGTH_SHORT).show();
                obtenerEinsertarProducto(GlobalClass.gUrl+GlobalClass.gLink+"/HandHeld/obtenerEinsertarProductoTPT/?id="+etProducto.getText()+"&uuid="+gSalida.paramUUID+"");
            }

        } else {
            obtenerEinsertarProducto(GlobalClass.gUrl+GlobalClass.gLink+"/HandHeld/obtenerEinsertarProductoTPT/?id="+etProducto.getText()+"&uuid="+gSalida.paramUUID+"");

            GlobalClass.actualizarEstatusDeTraspaso(GlobalClass.gUrl+GlobalClass.gLink+"/HandHeld/actualizarEstatusDeTraspaso/?estatus=201&id="+gSalida.paramIDRegistrado, getContext());
        }

    }

    private void obtenerEinsertarProducto(String URL) {
        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);

                            arclave_etiqueta.add(jsonObject.getString("clave_etiqueta"));
                            arnombre_producto.add(jsonObject.getString("nombre_producto"));
                            arfecha_produccion.add(jsonObject.getString("fecha_produccion"));
                            arcodigo_barras.add(jsonObject.getString("codigo_barras"));
                            arclave.add(jsonObject.getString("clave"));
                            arno_lote.add(jsonObject.getString("no_lote"));
                            arno_tarima.add(jsonObject.getString("no_tarima_detalle"));
                            arIDsInsertados.add(jsonObject.getString("id_insertado"));

                            Toast.makeText(getContext(), "ID insertado:"+jsonObject.getString("id_insertado"), Toast.LENGTH_SHORT).show();

                            String registros[] = {String.valueOf(tarimasEscaneadas),
                                    jsonObject.getString("codigo_barras"),
                                    jsonObject.getString("clave"),
                                    String.valueOf(1),
                                    jsonObject.getString("no_tarima_detalle"),
                                    jsonObject.getString("fecha_produccion"),
                                    jsonObject.getString("no_lote"),
                                    jsonObject.getString("nombre_producto"),
                                    String.valueOf(-1),
                                    jsonObject.getString("clave_etiqueta"),
                                    gSalida.paramUUID
                            };
                            arRegistros.add(registros);

                            mostrarDato(jsonObject.getString("clave_etiqueta"),
                                    jsonObject.getString("nombre_producto"),
                                    jsonObject.getString("no_tarima_detalle"),
                                    jsonObject.getString("fecha_produccion"),
                                    String.valueOf(tarimasEscaneadas+1));

                            //while(!listNombreProducto.contains(nombre_producto)){ listNombreProducto.add(nombre_producto); }


                        } catch (JSONException e) {

                            if (pCargando.isShowing()) {

                                pCargando.dismiss();
                                GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                                etProducto.setText("");
                                etProducto.requestFocus();
                            }

                        }
                    }

                } else {

                    if (pCargando.isShowing()) {

                        pCargando.dismiss();
                        GlobalClass.openDialog("No hay datos para mostrar","No hay datos para mostrar ","ERROR",getActivity().getSupportFragmentManager());
                        etProducto.setText("");
                        etProducto.requestFocus();
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
                            etProducto.setText("");
                            etProducto.requestFocus();

                        }
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(JAR);
    }

    private void mostrarDato(String idEtiqueta, String nombreProducto, String noTarimaDetalle, String fechaCarga, String position){

        producto mpProducto = new producto(idEtiqueta,nombreProducto,noTarimaDetalle, fechaCarga, position);

        arEscaneo.add(mpProducto);

        adpEscaneo.notifyDataSetChanged();

        if (pCargando.isShowing()) {
            pCargando.dismiss();
            etProducto.setText("");
            etProducto.requestFocus();
        }

        AudioSoundPool(getView(), sonido_correcto);
        tarimasEscaneadas++;
        textContador.setText(tarimasEscaneadas + "Tarimas");
    }

    //todo el codigo relacionado con SAP

    public void limpiarRegistrosDeDuplicados(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty())
                {
                    Toast.makeText(getActivity(), "Registros Limpios", Toast.LENGTH_LONG).show();
                }else
                {
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                    }
                    GlobalClass.openDialog("Registros Limpios", response,"ERROR",getActivity().getSupportFragmentManager());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pCargando.isShowing()) {
                    pCargando.dismiss();
                }
                generarResumen();
                cadenaMaestra="";
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

    public void obtenerProductosUnicos(String x[]) {
         String clave_producto = x[2] ,nombre_producto = x[7];

        while(!listClaveProducto.contains(clave_producto)){ listClaveProducto.add(clave_producto); }

        while(!listNombreProducto.contains(nombre_producto)){ listNombreProducto.add(nombre_producto); }
    }

    private void generarDocumentoSAP(String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String r = "0123456789";
                if (!response.contains(r) && response.length()==6)
                {
                    Toast.makeText(getActivity(), "Numero:"+response, Toast.LENGTH_LONG).show();
                    String numeroSAP = response;
                    gSalida.paramNumeroDocumento = numeroSAP;
                    btnGenerarDS.setEnabled(false);
                    dialogoDocumentoSAP(numeroSAP);
                }else
                {
                    if (pCargando.isShowing()) {
                        pCargando.dismiss();
                        GlobalClass.openDialog("ERROR AL INGRESAR DATOS","Error: " + response,"ERROR",getActivity().getSupportFragmentManager());
                    }
                    generarResumen();
                    cadenaMaestra="";
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pCargando.isShowing()) {
                    pCargando.dismiss();
                }
                generarResumen();
                cadenaMaestra="";
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

        if (pCargando.isShowing()) {
            pCargando.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()){};

        builder.setTitle("DOCUMENTO SAP").setMessage("SU NUMERO DE DOCUMENTO ES\n"+nD);

        builder.setPositiveButton("Ok, GENERAR RESUMEN ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pCargando = new ProgressDialog(getActivity());
                pCargando.setMessage("GENERANDO RESUMEN...");
                pCargando.setCancelable(true);
                generarResumen();

            }
        }).setNegativeButton("", null).setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void generarResumen(){

        int limite = listNombreProducto.size();
        producto = new String[limite];
        arrayClave = new String[limite];

        for (int x = 0; x < limite; x++){
            producto[x] = listNombreProducto.get(x).toString();
            //arrayClave[x] = listClaveProducto.get(x).toString();

            cdn = cdn+producto[x]+"-";
            //cdnClaves = cdnClaves+arrayClave[x]+"--";
        }

        //Log.e("prueba", cdn);
        obtenerCantidadXProducto(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerConteoPorProducto/?uuid="+gSalida.paramUUID + "&producto="+cdn+ "");
        new Handler().postDelayed(new Runnable(){
            public void run() {

                GlobalClass.id = gSalida.paramIDRegistrado;
                GlobalClass.openDialogPro(cadenaMaestra, getArguments(), getView(), getActivity().getSupportFragmentManager(), "resumen");
                playImageButton.setEnabled(false);
                stopImageButton.setEnabled(false);
                btnGenerarDS.setVisibility(getView().INVISIBLE);
                btnMostrarResumen.setVisibility(getView().VISIBLE);

                limpiarGlobales();
                if (pCargando.isShowing()) {
                    pCargando.dismiss();
                }
            }
        }, 8000);
    }

    private void obtenerCantidadXProducto(String URL){
        JsonArrayRequest JAR = new JsonArrayRequest( URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (response.length() != 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            String cadena = "";
                            jsonObject = response.getJSONObject(i);

                            orden = jsonObject.getString("posicion");
                            prod = jsonObject.getString("nombre_producto");
                            ca = jsonObject.getString("cantidad");

                            prod = prod.substring(3);

                            //cadena = "["+orden+"]:{"+prod+"} >> ("+ca+")\n";
                            cadena = "("+ca+")>{"+prod+"}\n";
                            Log.e("cadenaUnida", cadena);
                            cadenaMaestra+=cadena;
                            listDatosUnidos.add(cadena);

                        } catch (JSONException e) {
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }
                    }

                } else {
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



    private void limpiarGlobales(){
        gSalida.paramUUID="";
        gSalida.paramBodegaOrigen="";
        gSalida.paramBodegaOrigenNombre="";
        gSalida.paramCortina="";
        gSalida.paramBodegaDestino="";
        gSalida.paramBodegaDestinoNombre="";
        gSalida.paramNumeroMontacarguista="";
        gSalida.paramNombreMontacarguista="";
        gSalida.paramNumeroAnalistaInventarios="";
        gSalida.paramNombreAnalistaInventarios="";
        gSalida.paramNumeroGuardiaSeguridad="";
        gSalida.paramNombreGuardiaSeguridad="";
        gSalida.paramClaveTransportista="";
        gSalida.paramNombreTransportista="";
        gSalida.paramConductor="";
        gSalida.paramPlacasTractor="";
        gSalida.paramPlacasRemolque="";
        gSalida.paramSellos="";
        gSalida.paramLlegadaTransportista="";
        gSalida.paramEntradaRampa="";
        gSalida.paramSalidaRampa="";
        gSalida.paramNumeroDocumento="";
        gSalida.paramCorreoUsuarioAplicacion="";
        gSalida.paramIDRegistrado="";
        gSalida.paramObservaciones="";
    }

}