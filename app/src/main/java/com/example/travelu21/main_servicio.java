package com.example.travelu21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class main_servicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_servicio);
        //Se relacionan los botones de la interfaz gráfica
        Button config = findViewById(R.id.config);
        Button reservar = findViewById(R.id.reservasn);
        //Métodos para el momento en el que son clickeados
        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_servicio.this, reservas_servicio.class));
            }
        });
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_servicio.this, opciones_servicio.class));
            }
        });
    }
}
