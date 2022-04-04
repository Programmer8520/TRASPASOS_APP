package com.example.TRASPASOS_APP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.TRASPASOS_APP.ui.MessageBox;
import com.example.TRASPASOS_APP.ui.MessageBoxResumen;
import com.example.TRASPASOS_APP.ui.salida.gSalida;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class GlobalClass extends AppCompatActivity {

    public String nUrl = "";

    public static String id = gSalida.paramIDRegistrado;

    public static String resumen;
    public static String
                        nave_de_escaneo = "8",
                        nomEmpleado = "N/A",
                        gID = "",
                        gEmail = "",
                        gDep = "",
                        gUsr = "",
                        gProyecto = "TRASPASOS_APP",
                        gVersion_actual = "1.0",
                        //gUrl = "http://192.168.1.100/",
                        gUrl = "http://192.168.1.100/",
                        gURL_SAP = "http://www.innovador.com.mx/",
                        gImg = "http://www.innovador.com.mx:8085/sipisa/_lib/file/img/",
                        MY_PREFS_NAME = "user_pass_pref",
                        //gLink = "SIPISA_APP",
                        gFecha = "",
                                gLink = "webservice";

    public static Boolean buscar = false;

    private static Map<Character, Character> MAP_NORM;

    public static String removeAccents(String value) {
        if (MAP_NORM == null || MAP_NORM.size() == 0) {
            MAP_NORM = new HashMap<Character, Character>();
            MAP_NORM.put('À', 'A');
            MAP_NORM.put('Á', 'A');
            MAP_NORM.put('Â', 'A');
            MAP_NORM.put('Ã', 'A');
            MAP_NORM.put('Ä', 'A');
            MAP_NORM.put('È', 'E');
            MAP_NORM.put('É', 'E');
            MAP_NORM.put('Ê', 'E');
            MAP_NORM.put('Ë', 'E');
            MAP_NORM.put('Í', 'I');
            MAP_NORM.put('Ì', 'I');
            MAP_NORM.put('Î', 'I');
            MAP_NORM.put('Ï', 'I');
            MAP_NORM.put('Ù', 'U');
            MAP_NORM.put('Ú', 'U');
            MAP_NORM.put('Û', 'U');
            MAP_NORM.put('Ü', 'U');
            MAP_NORM.put('Ò', 'O');
            MAP_NORM.put('Ó', 'O');
            MAP_NORM.put('Ô', 'O');
            MAP_NORM.put('Õ', 'O');
            MAP_NORM.put('Ö', 'O');
            MAP_NORM.put('Ñ', 'N');
            MAP_NORM.put('Ç', 'C');
            MAP_NORM.put('ª', 'A');
            MAP_NORM.put('º', 'O');
            MAP_NORM.put('§', 'S');
            MAP_NORM.put('³', '3');
            MAP_NORM.put('²', '2');
            MAP_NORM.put('¹', '1');
            MAP_NORM.put('à', 'a');
            MAP_NORM.put('á', 'a');
            MAP_NORM.put('â', 'a');
            MAP_NORM.put('ã', 'a');
            MAP_NORM.put('ä', 'a');
            MAP_NORM.put('è', 'e');
            MAP_NORM.put('é', 'e');
            MAP_NORM.put('ê', 'e');
            MAP_NORM.put('ë', 'e');
            MAP_NORM.put('í', 'i');
            MAP_NORM.put('ì', 'i');
            MAP_NORM.put('î', 'i');
            MAP_NORM.put('ï', 'i');
            MAP_NORM.put('ù', 'u');
            MAP_NORM.put('ú', 'u');
            MAP_NORM.put('û', 'u');
            MAP_NORM.put('ü', 'u');
            MAP_NORM.put('ò', 'o');
            MAP_NORM.put('ó', 'o');
            MAP_NORM.put('ô', 'o');
            MAP_NORM.put('õ', 'o');
            MAP_NORM.put('ö', 'o');
            MAP_NORM.put('ñ', 'n');
            MAP_NORM.put('ç', 'c');
        }

        if (value == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(value);

        for (int i = 0; i < value.length(); i++) {
            Character c = MAP_NORM.get(sb.charAt(i));
            if (c != null) {
                sb.setCharAt(i, c.charValue());
            }
        }

        return sb.toString();

    }

    public static void initialVar() {
        nomEmpleado = "N/A";
        buscar = false;
        gID = "";
        gDep = "";
        gUrl = "http://192.168.1.100/";
        gImg = "sipisa/_lib/file/img/";

    }

    public static void showImage(ImageView img, Context cnt, FragmentManager fm) {

        ImageRequest imageRequest = new ImageRequest(gImg, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                img.setImageBitmap(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                openDialog("ERROR EN IMG","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR", fm);
            }
        }

        );
        RequestQueue requestQueue = Volley.newRequestQueue(cnt);
        requestQueue.add(imageRequest);

    }

    public static void permisosVentanas(String vMod, Context cnt, NavController navController, int action, FragmentManager fg) {

        String URL = gUrl + gLink+"/inicio_sesion/permisosVentanas/?usr=" + gUsr + "&mod=" + vMod + "&prj="+gProyecto+"" ;
        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.length() == 0) {
                    //Toast.makeText(cnt, "NO TIENE PERMISO A ESA VENTANA", Toast.LENGTH_SHORT).show();
                    openDialog("NO TIENE PERMISO A ESA VENTANA","No tiene permiso para acceder a esta ventana","ALERTA",fg);
                } else {

                    navController.navigate(action);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(cnt, "Error de conexion", Toast.LENGTH_SHORT).show();
                        openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",fg);

                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(cnt);
        requestQueue.add(JAR);


    }

    public static void openDialog(String titulo, String mensaje, String tag, FragmentManager fg) {
        MessageBox messageBox = new MessageBox(titulo,mensaje);
        messageBox.show(fg, tag);
    }

    public static void openDialogPro(String mensaje, Bundle bd, View v, FragmentManager fg, String tag){

        MessageBoxResumen messageBoxR = new MessageBoxResumen();
        messageBoxR.setMsg(mensaje);
        messageBoxR.setV(v);
        messageBoxR.show(fg, tag);

    }

    public static void actualizarEstatusDeTraspaso(String URL, Context c){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.isEmpty())
                {
                    Toast.makeText(c, "Estatus Actualizado", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(c, "Error para actualizar estatus", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);
    }

    public static void openDialogDocumentoSAP(String mensaje, Bundle bd, View v, FragmentManager fg, String tag){

    }

}


