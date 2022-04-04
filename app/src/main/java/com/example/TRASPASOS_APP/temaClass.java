package com.example.TRASPASOS_APP;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class temaClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.templete);

        Button button = (Button) findViewById(R.id.btnRegresar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               /* Intent intent=new Intent(getApplicationContext(), MainMenu.class);
                startActivity(intent);*/
            }
        });

    }
}
