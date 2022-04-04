package com.example.TRASPASOS_APP;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class LoginCardClass extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String FLASH_STATE = "FLASH_STATE";

    private ZXingScannerView mScannerView;
    private boolean mFlash;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_login_card);
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.setAspectTolerance(0.2f);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }

    @Override
    public void handleResult(Result rawResult) {

        loginCamera(GlobalClass.gUrl + GlobalClass.gLink + "/inicio_sesion/loginCard/?id=" + rawResult.getText() + "");
        //Toast.makeText(this, "Contents = " + rawResult.getText() +", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();


       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(LoginCardClass.this);
            }
        }, 2000);*/
    }

    public void toggleFlash(View v) {
        mFlash = !mFlash;
        mScannerView.setFlash(mFlash);
    }

    private static class CustomViewFinderView extends ViewFinderView {
        public CustomViewFinderView(Context context) {
            super(context);
        }
    }

    private void loginCamera(String URL) {

        JsonArrayRequest JAR = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String cadena = "";
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {

                    try {

                        jsonObject = response.getJSONObject(i);
                        //listMarca.add(jsonObject.getString("Marca"));
                        //  cadena="#"+jsonObject.getString("bateria") + "—" +  jsonObject.getString("marca") + "—" + jsonObject.getString("serie");
                        Intent intent = new Intent(LoginCardClass.this, LoginClass.class);
                        intent.putExtra("usr", jsonObject.getString("usr"));
                        intent.putExtra("pas", jsonObject.getString("pas"));
                        startActivity(intent);

                    } catch (JSONException e) {
                        Toast.makeText(LoginCardClass.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(LoginCardClass.this, "Error de conexion", Toast.LENGTH_SHORT).show();
                        GlobalClass.openDialog("ERROR DE CONEXION","Error de conexion: Favor de revisar la conexion \n Tipo de error: "+ error.toString(),"ERROR",getSupportFragmentManager());

                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(LoginCardClass.this);
        requestQueue.add(JAR);


    }
}