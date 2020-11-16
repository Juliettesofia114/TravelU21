package com.example.travelu21;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class vercuentaservicio extends AppCompatActivity {

    EditText editText;
    Button guarda, recupera, actualiza;
    private FirebaseAuth mAuth;
    Gson gson = new Gson();
    Queue<Viajero_class> queue = new Queue<>();
    private static final String FILE_NAME = "viajero.json";
    private static final String FILE_T = "presupuesto.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vercuentaservicio);
        editText = findViewById(R.id.editText3);
        guarda = findViewById(R.id.button);
        recupera = findViewById(R.id.button3);
        actualiza = findViewById(R.id.button2);
        //recuperarV();
        final Viajero_class nuevo = new Viajero_class("nom","pass","corr","des",0,"uid5");
        guarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardar_viajero(nuevo);
            }
        });
        recupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarV();
            }
        });
        actualiza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrar();
            }
        });
    }

    void guardar_viajero(Viajero_class user){
        String json = gson.toJson(user);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_T, MODE_APPEND);
            fileOutputStream.write(json.getBytes());
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_T);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void recuperarV() {
        FileInputStream fileInputStream = null;
        try {
            //Se abre el archivo donde se guardan los hoteles registrados
            fileInputStream = openFileInput(FILE_T);

            //Se crea un objeto para leer el archivo
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            new BufferedReader(inputStreamReader);

            //Se crea un Gson y un JsonStreamParser para la lectura de los objetos
            Gson gson = new GsonBuilder().create();
            JsonStreamParser p = new JsonStreamParser(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Mientras que exista un siguiente objeto
            while (p.hasNext()) {

                //Se hace referencia al siguiente objeto de p
                JsonElement e = p.next();

                //Gracias al GSON y al JsonStreamParser se crea un objeto de tipo viajero con los datos proporcionados
                if (e.isJsonObject()) {

                    //Se añade el nuevo objeto al árbol correpondiente
                    Viajero_class user = gson.fromJson(e, Viajero_class.class);
                    stringBuilder.append(user.nombre).append("\n");
                }
            }
            editText.setText(stringBuilder.toString());
        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(vercuentaservicio.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
        }
        finally {
            if(fileInputStream != null){
                try{
                    //Se cierra el lector de archivo
                    fileInputStream.close();
                } catch (Exception ignored){

                }
            }
        }
    }

    private void encolar() {
        FileInputStream fileInputStream = null;
        try {
            //Se abre el archivo donde se guardan los hoteles registrados
            fileInputStream = openFileInput(FILE_T);

            //Se crea un objeto para leer el archivo
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            new BufferedReader(inputStreamReader);

            //Se crea un Gson y un JsonStreamParser para la lectura de los objetos
            Gson gson = new GsonBuilder().create();
            JsonStreamParser p = new JsonStreamParser(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Mientras que exista un siguiente objeto
            while (p.hasNext()) {

                //Se hace referencia al siguiente objeto de p
                JsonElement e = p.next();

                //Gracias al GSON y al JsonStreamParser se crea un objeto de tipo viajero con los datos proporcionados
                if (e.isJsonObject()) {

                    //Se añade el nuevo objeto al árbol correpondiente
                    Viajero_class user = gson.fromJson(e, Viajero_class.class);
                    queue.enqueue(user);
                }
            }
            Toast.makeText(vercuentaservicio.this,"Se carga", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(vercuentaservicio.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
        }
        finally {
            if(fileInputStream != null){
                try{
                    //Se cierra el lector de archivo
                    fileInputStream.close();
                } catch (Exception ignored){

                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    public void actualizar(){
        int count = 0;
        while(!queue.isEmpty()){
            Viajero_class current = queue.dequeue();
            String nombre = current.nombre;
            if (!nombre.equals("nombre")){
                guardar_viajero(current);
                count++;
            }
        }
        editText.setText(Integer.toString(count));
        Toast.makeText(vercuentaservicio.this, "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
    }
    public void borrar(){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_T, MODE_PRIVATE);
            fileOutputStream.write(0);
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_T);

            Toast.makeText(vercuentaservicio.this, "Se ha borrado correctamente", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(vercuentaservicio.this, "No se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(vercuentaservicio.this, "No se ha actualizado correctamente x2", Toast.LENGTH_SHORT).show();
        } finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Toast.makeText(vercuentaservicio.this, "No se ha actualizado correctamente x3", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
