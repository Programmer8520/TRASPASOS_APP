package com.example.TRASPASOS_APP;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class splash extends AppCompatActivity {

    int REQUEST_CODE = 200;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        verificarPermisos();
    }

    private void consultaVersion(String URL) {
        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                if (response.length() == 0) {
                    GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: NO SE ENCONTRO EL REGISTRO DE ACTUALIZACION EN LA BASE DE DATOS","ERROR",getSupportFragmentManager());

                }
                else{
                    try {

                        jsonObject = response.getJSONObject(0);
                        if (GlobalClass.gVersion_actual.equals(jsonObject.getString("version"))) {
                            Intent intent = new Intent(getApplicationContext(), LoginClass.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent pantaActualizar = new Intent(getApplicationContext(), ActualizarClass.class);
                            pantaActualizar.putExtra("url", jsonObject.getString("url"));
                            finish();
                            startActivity(pantaActualizar);

                        }

                    } catch (JSONException e) {
                        GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getSupportFragmentManager());
                    }

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(),"Error de conexion",Toast.LENGTH_SHORT).show();
                        cambioConexion();
                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(JAR);

    }

    private void consultaVersionCambioRed(String URL) {
        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                if (response.length() == 0) {
                    GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: NO SE ENCONTRO EL REGISTRO DE ACTUALIZACION EN LA BASE DE DATOS","ERROR",getSupportFragmentManager());

                }
                else{
                    try {

                        jsonObject = response.getJSONObject(0);
                        if (GlobalClass.gVersion_actual.equals(jsonObject.getString("version"))) {
                            Intent intent = new Intent(getApplicationContext(), LoginClass.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent pantaActualizar = new Intent(getApplicationContext(), ActualizarClass.class);
                            pantaActualizar.putExtra("url", jsonObject.getString("url"));
                            finish();
                            startActivity(pantaActualizar);

                        }

                    } catch (JSONException e) {
                        GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getSupportFragmentManager());
                    }

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getSupportFragmentManager());
                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(JAR);

    }

    public void cambioConexion() {
        GlobalClass.gUrl = "http://www.innovador.com.mx:8085/";
        String nUrl = "http://www.innovador.com.mx:8085/";
        GlobalClass.gImg = nUrl + "sipisa/_lib/file/img/";

        consultaVersionCambioRed(GlobalClass.gUrl + GlobalClass.gLink + "/inicio_sesion/consultaVersion/?pry=" + GlobalClass.gProyecto + "");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {

        int permisosIn = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int permisosAN = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int permisosWES = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisosRES = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permisosIP = ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES);
        int permisosCa = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (permisosIn == PackageManager.PERMISSION_DENIED || permisosAN == PackageManager.PERMISSION_DENIED || permisosWES == PackageManager.PERMISSION_DENIED || permisosRES == PackageManager.PERMISSION_DENIED || permisosIP == PackageManager.PERMISSION_DENIED || permisosCa == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES, Manifest.permission.CAMERA}, REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
                System.exit(0);
            }
            else
            {
                consultaVersion(GlobalClass.gUrl + GlobalClass.gLink + "/inicio_sesion/consultaVersion/?pry=" + GlobalClass.gProyecto + "");

            }
        }
    }


}