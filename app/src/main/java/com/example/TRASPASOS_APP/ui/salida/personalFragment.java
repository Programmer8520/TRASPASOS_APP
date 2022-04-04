package com.example.TRASPASOS_APP.ui.salida;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

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

import java.util.ArrayList;


public class personalFragment extends Fragment {

    private EditText etMontacargista,etAnalista,etGuardia,etNMontacargista,etNAnalista,etNGuardia;
    private ViewPager2 vpDatosGenerales;

    public personalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vpDatosGenerales=(ViewPager2) getActivity().findViewById(R.id.vpDatosGenerales);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //final NavController navController = Navigation.findNavController(view);
        etMontacargista = view.findViewById(R.id.etMontacagista);
        etAnalista = view.findViewById(R.id.etAnalista);
        etGuardia = view.findViewById(R.id.etGuardia);
        etMontacargista.setKeyListener(null);
        etAnalista.setKeyListener(null);
        etGuardia.setKeyListener(null);

        etNMontacargista = view.findViewById(R.id.etNMontacarguista);
        etNAnalista = view.findViewById(R.id.etNAnalista);
        etNGuardia = view.findViewById(R.id.etNGuardia);
        etNMontacargista.requestFocus();

        obtenerUltimoRegistroPersonal(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerUltimoRegistroPersonal/");

        etNMontacargista.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    obtenerEmpleado(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerEmpleado/?nEmpleado="+etNMontacargista.getText()+"", etMontacargista,etNAnalista,etNMontacargista);
                    return true;
                }
                return false;
            }
        });

        etNMontacargista.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    obtenerEmpleado(GlobalClass.gUrl + GlobalClass.gLink + "/HandHeld/obtenerEmpleado/?nEmpleado="+etNMontacargista.getText()+"", etMontacargista);

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
    }

    private void obtenerUltimoRegistroPersonal(String URL) {

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                if (response.length() != 0) {
                    try {
                        //Intent intent;
                        jsonObject = response.getJSONObject(0);

                        etNAnalista.setText(jsonObject.getString("analista"));
                        etNGuardia.setText(jsonObject.getString("guardia"));
                        etNMontacargista.setText(jsonObject.getString("montacarguista"));

                    } catch (JSONException e) {
                        //  Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                    }


                } else {
                    Toast.makeText(getActivity(), "No se encontro el numero de empleado", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getActivity(), "No se encontro el numero de empleado", Toast.LENGTH_SHORT).show();
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
                            obtenerDatos();

                        } catch (JSONException e) {
                            //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            GlobalClass.openDialog("ERROR AL OBTENER DATOS","Error al obtener datos \n Tipo de error: "+ e.toString(),"ERROR",getActivity().getSupportFragmentManager());
                        }


                } else {
                    Toast.makeText(getActivity(), "No se encontro el numero de empleado", Toast.LENGTH_SHORT).show();
                    et.setText("");
                    etActual.requestFocus();
                    vpDatosGenerales.setCurrentItem(1);
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

        gSalida.paramNumeroMontacarguista=etNMontacargista.getText().toString();
        gSalida.paramNombreMontacarguista=etMontacargista.getText().toString();
        gSalida.paramNumeroAnalistaInventarios=etNAnalista.getText().toString();
        gSalida.paramNombreAnalistaInventarios=etAnalista.getText().toString();
        gSalida.paramNumeroGuardiaSeguridad=etNGuardia.getText().toString();
        gSalida.paramNombreGuardiaSeguridad=etGuardia.getText().toString();
        vpDatosGenerales.setCurrentItem(2);
    }
}