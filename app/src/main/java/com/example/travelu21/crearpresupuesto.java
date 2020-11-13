package com.example.travelu21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class crearpresupuesto extends AppCompatActivity {

    //Se intancian los widgets y strings a utilizar
    private EditText fecha, destino, dinero, personas, comida, hotel, tipo;
    String fec, des, dir, com, ho, tip, per;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearpresupuesto);

        //Se inicializan los objetos relacionándolos con su respectivo componente de la interfaz gráfica
        Button cancelar = findViewById(R.id.cancelar);
        final Button guardar = findViewById(R.id.guardar);
        fecha = findViewById(R.id.fecha);
        destino = findViewById(R.id.destino);
        dinero = findViewById(R.id.dinero);
        personas = findViewById(R.id.numeroper);
        comida = findViewById(R.id.comida);
        hotel = findViewById(R.id.hotel);
        tipo = findViewById(R.id.tipo);

        //Se recogen los datos dados por el usuario como strings
        fec = fecha.getText().toString().trim();
        des = destino.getText().toString().trim();
        dir = dinero.getText().toString().trim();
        per = personas.getText().toString().trim();
        com = comida.getText().toString().trim();
        ho = hotel.getText().toString().trim();
        tip = tipo.getText().toString().trim();

        //Se le asignan funciones a los botones de la interfaz gráfica
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Envía al usuario nuevamente a la página principal
                startActivity(new Intent(crearpresupuesto.this, main_viajeros.class));
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Método que se encarga de realizar el guardado de la información recibida
                guardarpresupuesto();
            }
        });
    }

    private void guardarpresupuesto() {

        //Envía al usuario al layout que permite ver los presupuestos guardados
        Toast.makeText(crearpresupuesto.this, "¡Se ha creado correctamente tu presupuesto",
                Toast.LENGTH_SHORT).show();
        startActivity(new Intent(crearpresupuesto.this, verpresupuestos.class));
    }
}
