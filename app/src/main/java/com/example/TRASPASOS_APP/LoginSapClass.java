package com.example.TRASPASOS_APP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginSapClass extends AppCompatActivity {

    private EditText u, p;
    private RequestQueue requestQueue;
    private CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sap);

        u = findViewById(R.id.usuario);
        p = findViewById(R.id.contra);

        u.setText("");
        p.setText("");
        cb = findViewById(R.id.cbCuenta);


        recuperarPreferencias();

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            String usr = parametros.getString("usr");
            String pas = parametros.getString("pas");
            u.setText(usr);
            p.setText(pas);

            validar(GlobalClass.gUrl + GlobalClass.gLink + "/inicio_sesion/login/");
        }


        Button buttonLogin = findViewById(R.id.cirLoginButton);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(u.getText().toString()) || TextUtils.isEmpty(p.getText().toString())) {
                    Toast.makeText(LoginSapClass.this, "NO PUEDES DEJAR CAMPOS VACIOS", Toast.LENGTH_SHORT).show();
                    u.setText("");
                    p.setText("");
                    u.requestFocus();
                } else {
                    validar(GlobalClass.gUrl + GlobalClass.gLink + "/inicio_sesion/login/");

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finish();
        System.exit(0);
    }

    private void validar(String URL) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {

                    //consultaEmpleado(GlobalClass.gUrl + GlobalClass.gLink + "/inicio_sesion/consultaEmpleado/?usr=" + u.getText() + "&pass=" + p.getText() + "");
                    //Toast.makeText(LoginClass.this,"PRUEBA EXITOSA",Toast.LENGTH_SHORT).show();

                    try {

                        JSONArray   jsonArray  = new JSONArray(response);
                        JSONObject  jsonObject = null;
                        jsonObject             = jsonArray.getJSONObject(0);

                        GlobalClass.nomEmpleado = jsonObject.getString("empleado");
                        GlobalClass.gImg        = "http://192.168.1.100/sipisa/_lib/file/img/" + jsonObject.getString("foto");
                        GlobalClass.gDep        = jsonObject.getString("dep");
                        GlobalClass.gUsr        = u.getText().toString();
                        GlobalClass.gEmail      = jsonObject.getString("email");

                        guardarPreferencias();
                        Intent intent = new Intent(getApplicationContext(), menuNavegacion.class);
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {
                        GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getSupportFragmentManager());
                    }


                } else {
                    //Toast.makeText(LoginClass.this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
                    GlobalClass.openDialog("USUARIO INCORRECTO","El usuario o la contraseña esta mal","ERROR",getSupportFragmentManager());
                    u.setText("");
                    p.setText("");
                    u.requestFocus();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(LoginClass.this, error.toString(), Toast.LENGTH_SHORT).show();
                GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getSupportFragmentManager());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("usr", u.getText().toString());
                parametros.put("pass", p.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void consultaEmpleado(String URL) {

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                try {

                    jsonObject = response.getJSONObject(0);
                    GlobalClass.nomEmpleado = jsonObject.getString("empleado");
                    GlobalClass.gImg = "pimex/sipisa/_lib/file/img/" + jsonObject.getString("foto");
                    GlobalClass.gDep = jsonObject.getString("dep");
                    GlobalClass.gUsr = u.getText().toString();

                } catch (JSONException e) {
                    GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getSupportFragmentManager());
                }

                Intent intent = new Intent(getApplicationContext(), menuNavegacion.class);
                startActivity(intent);
                finish();


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getSupportFragmentManager());
                    }
                }
        );


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(JAR);


    }

    //para lo que necesitamos programar debemos de

    private void guardarPreferencias() {
        SharedPreferences prefs = getSharedPreferences("recordarmeCuenta", Context.MODE_PRIVATE);
        if (cb.isChecked()) {

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("usr", u.getText().toString());
            editor.putString("pass", p.getText().toString());
            editor.putBoolean("stateSwitch", cb.isChecked());
            editor.commit();

        } else {

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("usr", "");
            editor.putString("pass", "");
            editor.putBoolean("stateSwitch", cb.isChecked());
            editor.commit();
        }

    }

    private void recuperarPreferencias() {
        SharedPreferences prefs = getSharedPreferences("recordarmeCuenta", Context.MODE_PRIVATE);
        String usr = prefs.getString("usr", "");
        String pass = prefs.getString("pass", "");
        boolean stateCb = prefs.getBoolean("stateSwitch", false);
        cb.setChecked(stateCb);
        u.setText(usr);
        p.setText(pass);

    }


}
