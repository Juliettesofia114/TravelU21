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

public class detalles_servicio_hotel extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_servicio_hotel);
        Button reserva = findViewById(R.id.reservares);
        final EditText fecha = findViewById(R.id.fechah);
        TextView nombre = findViewById(R.id.nombrehotel);
        TextView correo = findViewById(R.id.correrh);
        TextView ubicacion = findViewById(R.id.ubicacionh);
        TextView maximoh = findViewById(R.id.maximoh);
        TextView prec = findViewById(R.id.precio);
        TextView wif = findViewById(R.id.wifi);
        TextView pisc = findViewById(R.id.piscna);
        TextView desay = findViewById(R.id.desayuno);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            final String nom = extras.getString("nombreh");
            final String ubi = extras.getString("ubicacionh");
            final String corr = extras.getString("correoh");
            final String id = extras.getString("uidh");
            final String max = extras.getString("maximo");
            final String precio = extras.getString("precio");
            final String wifi = extras.getString("wifi");
            final String piscina = extras.getString("piscina");
            final String desayuno = extras.getString("desayuno");

            nombre.setText(nom);
            ubicacion.setText(ubi);
            maximoh.setText("Máximo de personas: "+max);
            prec.setText("Precio por noche: "+precio);
            wif.setText("Wifi: "+wifi);
            pisc.setText("Piscina: Holi");
            desay.setText("Desayuno: "+desayuno);
            correo.setText(corr);

            reserva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String fech = fecha.getText().toString().trim();
                    //Se pide la fecha y hora actual para guardarla en los datos
                    long date = System.currentTimeMillis();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                    String dateString = sdf.format(date);

                    //Se crea un intent que llevará consigo los datos recogidos a la siguiente actividad
                    Intent i = new Intent(detalles_servicio_hotel.this, reservas_viajero.class);
                    i.putExtra("nombreh", nom);
                    i.putExtra("ubicacionh", ubi);
                    i.putExtra("correoh", corr);
                    i.putExtra("fechah", fech);
                    i.putExtra("uidh", id);

                    //Envía al usuario a la actividad donde se guardan las reservas
                    startActivity(i);
                    finish();
                }
            });
        }
    }
}
