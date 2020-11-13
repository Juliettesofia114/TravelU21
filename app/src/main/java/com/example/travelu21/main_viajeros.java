package com.example.travelu21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class main_viajeros extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_viajeros);
        //Se instancian como objetos de tipo botón los botones de la interfaz
        Button config = findViewById(R.id.conf);
        Button reservas = findViewById(R.id.reserv);
        Button buscarhotel = findViewById(R.id.hotel);
        Button buscarres = findViewById(R.id.rest);
        Button verpresu = findViewById(R.id.ver);
        Button crearpresu = findViewById(R.id.crea);

        //Métodos para hacer funcionales los botones
        reservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_viajeros.this, reservas_viajero.class));
            }
        });
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_viajeros.this, opciones_viajero.class));
            }
        });
        buscarhotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_viajeros.this, busqueda_hotel.class));
            }
        });
        buscarres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_viajeros.this, busqueda_restaurante.class));
            }
        });
        verpresu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_viajeros.this, verpresupuestos.class));
            }
        });
        crearpresu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_viajeros.this, crearpresupuesto.class));
            }
        });
    }
}
