package com.example.travelu21;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class crearpresupuesto extends AppCompatActivity {
    EditText fecha, destino, dinero, personas, comida, hotel, tipo;
    String fec, des, dir, com, ho, tip, per;
    Basededatos basededatos = new Basededatos();
    String dateString;
    String[] datos;
    Gson gson = new Gson();

    //Nombre del archivo donde se guardan los usuarios de tipo restaurante
    private static final String FILE_P = "presupuesto.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearpresupuesto);

        //Se inicializan los objetos relacionándolos con su respectivo componente de la interfaz gráfica
        Button cancelar = findViewById(R.id.cancelar);
        final Button guardar = findViewById(R.id.guardar);

        //Se intancian los widgets y strings a utilizar
        fecha = findViewById(R.id.fecha);
        destino = findViewById(R.id.destino);
        dinero = findViewById(R.id.dinero);
        personas = findViewById(R.id.numeroper);
        comida = findViewById(R.id.comida);
        hotel = findViewById(R.id.hotel);
        tipo = findViewById(R.id.tipo);


        //Se pide la fecha y hora actual para guardarla en los datos
        long date = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy. h:mm a");
        dateString = sdf.format(date);

        //Se le asignan funciones a los botones de la interfaz gráfica
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Envía al usuario nuevamente a la página principal
                startActivity(new Intent(crearpresupuesto.this, main_viajeros.class));
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //Método que se encarga de realizar el guardado de la información recibida
                guardarpresupuesto();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void guardarpresupuesto() {
        //Se recogen los datos dados por el usuario como strings
        fec = fecha.getText().toString().trim();
        des = destino.getText().toString().trim();
        dir = dinero.getText().toString().trim();
        per = personas.getText().toString().trim();
        com = comida.getText().toString().trim();
        ho = hotel.getText().toString().trim();
        tip = tipo.getText().toString().trim();
        Toast.makeText(crearpresupuesto.this, per,
                Toast.LENGTH_SHORT).show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        Presupuesto_Class nuevo_presupuesto = new Presupuesto_Class(uid,fec,des,dir,per,com,ho,tip,dateString);
        basededatos.ActualizarP(uid, nuevo_presupuesto);
        guardar_presupuesto(nuevo_presupuesto);

        //Envía al usuario al layout que permite ver los presupuestos guardados
        startActivity(new Intent(crearpresupuesto.this, verpresupuestos.class));
        finish();

    }

    public void borrar(){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_P, MODE_PRIVATE);
            fileOutputStream.write(0);
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_P);

            Toast.makeText(crearpresupuesto.this, "Se ha borrado correctamente", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(crearpresupuesto.this, "No se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(crearpresupuesto.this, "No se ha actualizado correctamente x2", Toast.LENGTH_SHORT).show();
        } finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Toast.makeText(crearpresupuesto.this, "No se ha actualizado correctamente x3", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    void guardar_presupuesto(Presupuesto_Class user){
        String json = gson.toJson(user);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_P, MODE_APPEND);
            fileOutputStream.write(json.getBytes());
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_P);
        } catch (FileNotFoundException e) {
            Toast.makeText(crearpresupuesto.this, "Llévame ahora.",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(crearpresupuesto.this, "Llévame ahora.",
                    Toast.LENGTH_SHORT).show();
        } finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Toast.makeText(crearpresupuesto.this, "Llévame ahora.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
