 package com.example.travelu21;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;

 public class detalles_servicio extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_servicio);
        Button reservar = findViewById(R.id.reservares);
        Bundle extras = getIntent().getExtras();
        final EditText fecha = findViewById(R.id.fecha);
        TextView nombre = findViewById(R.id.nombreres);
        TextView correo = findViewById(R.id.correror);
        TextView ubicacion = findViewById(R.id.ubicacion);
        TextView horary = findViewById(R.id.horario);
        TextView maximo = findViewById(R.id.maximo);
        TextView tipor = findViewById(R.id.tipo);
        TextView medio = findViewById(R.id.medio);
        if (extras!=null) {
            final String nom = extras.getString("nombre");
            final String ubi = extras.getString("ubicacion");
            final String corr = extras.getString("correo");
            final String id = extras.getString("uid");
            final String tipo = extras.getString("tipo");
            final String maxi = extras.getString("maximo");
            final String horario = extras.getString("horario");
            //final String mediop = extras.getString("medio");

            nombre.setText(nom);
            ubicacion.setText(ubi);
            maximo.setText("Máximo de personas: "+maxi);
            tipor.setText("Tipo de restaurante: "+tipo);
            medio.setText("Medio de pago: ");
            horary.setText("Horario de atención: "+horario);
            correo.setText(corr);
            reservar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Se crea un intent que llevará consigo los datos recogidos a la siguiente actividad
                    final String fech = fecha.getText().toString().trim();
                    //Se pide la fecha y hora actual para guardarla en los datos
                    long date = System.currentTimeMillis();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                    String dateString = sdf.format(date);
                    Intent i = new Intent(detalles_servicio.this, reservas_viajero.class);
                    i.putExtra("nombre", nom);
                    i.putExtra("ubicacion", ubi);
                    i.putExtra("correo", corr);
                    i.putExtra("fecha", fech);
                    i.putExtra("uid", id);

                    //Envía al usuario a la actividad donde se guardan las reservas
                    startActivity(i);
                    finish();
                }
            });
        } else{
            nombre.setText("Lo sentimos, no hay información disponible para este servicio.");
            ubicacion.setText("");
            maximo.setText("");
            tipor.setText("");
            medio.setText("");
            horary.setText("");
            correo.setText("");
        }
    }
}
