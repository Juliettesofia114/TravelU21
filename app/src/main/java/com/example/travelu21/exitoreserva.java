package com.example.travelu21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class exitoreserva extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exitoreserva);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            String id_viajero = extras.getString("id_viajero");
            String estado = extras.getString("estado");
            String id_negocio = extras.getString("id_negocio");
            String fecha = extras.getString("fecha");
            final Intent i = new Intent(this,reservas_servicio.class);
            i.putExtra("id_viajero",id_viajero);
            i.putExtra("estado",estado);
            i.putExtra("fecha",fecha);
            i.putExtra("id_negocio",id_negocio);
            //Envía al usuario a la actividad donde se guardan las reservas
            //Declaramos un Handler que hace de unión entre el hilo principal y el secundario
            Handler handler = new Handler();
            //Llamamos al método postDelayed
            handler.postDelayed(new Runnable() {
                public void run() {
                    startActivity(i);
                    finish();
                }
            }, 2000); // 2 segundos de "delay"

        }
    }
}
