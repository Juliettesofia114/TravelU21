package com.example.travelu21;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class reservas_viajero extends AppCompatActivity {

    //Se instancian e inicializar los archivos en los que se encuentran los usuarios
    private static final String FILE_PC = "reservaC.json";
    private static final String FILE_PS = "reservaS.json";
    private static final String FILE_V = "viajero.json";

    //Se instacia e inicializa una nueva base de datos en donde se recuperan los datos
    Basededatos basededatos = new Basededatos();

    Gson gson = new Gson();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_viajero);

        recuperarV();

        Viajero_class user = basededatos.findv(uid);

        //Se traen los datos provenientes de búsqueda en caso de que provenga del layout de búsqueda
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            String nom = extras.getString("nombre");
            String ubi = extras.getString("ubicacion");
            String corr = extras.getString("correo");
            String fech = extras.getString("fecha");
            String id = extras.getString("uid");

            try {
                DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
                assert fech != null;
                Date date = sourceFormat.parse(fech);
                assert date != null;
                ReservaC_Class reservaC = new ReservaC_Class(id,nom,corr,date.getTime(),ubi);
                ReservaS_Class reservaS = new ReservaS_Class(uid,user.nombre, user.correo,date.getTime());
                guardar_reservaC(reservaC);
                guardar_reservaS(reservaS);
                Toast.makeText(reservas_viajero.this,"¡Se ha guardado correctamente tu reserva!",
                    Toast.LENGTH_SHORT).show();
                recuperarPC();
            } catch (ParseException e) {
                Toast.makeText(reservas_viajero.this,"El problema está aquí",
                        Toast.LENGTH_SHORT).show();
            }


        } else {
            recuperarPC();
        }

    }
    //Método para recuperar la base de datos de los usuarios de tipo viajero
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarPC() {
        FileInputStream fileInputStream = null;
        try {

            //Se abre el archivo donde se guardan los viajeros registrados
            fileInputStream = openFileInput(FILE_PC);

            //Se crea un objeto para leer el archivo
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            new BufferedReader(inputStreamReader);

            //Se crea un Gson y un JsonStreamParser para la lectura de los objetos
            Gson gson = new GsonBuilder().create();
            JsonStreamParser p = new JsonStreamParser(inputStreamReader);

            //Mientras que exista un siguiente objeto
            while (p.hasNext()) {

                //Se hace referencia al siguiente objeto de p
                JsonElement e = p.next();

                //Sí el siguiente es un objeto JSON
                if (e.isJsonObject()) {

                    //Gracias al GSON y al JsonStreamParser se crea un objeto de tipo viajero con los datos proporcionados
                    ReservaC_Class reserva = gson.fromJson(e, ReservaC_Class.class);

                    if(reserva.id.equals(uid)){
                        //Se añade el nuevo objeto al árbol correpondiente
                        basededatos.EnqueueC(reserva,reserva.fecha,reserva.id);
                    }
                }
            }

        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(reservas_viajero.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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
    //Método para recuperar la base de datos de los usuarios de tipo viajero
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarPS() {
        FileInputStream fileInputStream = null;
        try {
            //Se abre el archivo donde se guardan los viajeros registrados
            fileInputStream = openFileInput(FILE_PS);

            //Se crea un objeto para leer el archivo
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            new BufferedReader(inputStreamReader);

            //Se crea un Gson y un JsonStreamParser para la lectura de los objetos
            Gson gson = new GsonBuilder().create();
            JsonStreamParser p = new JsonStreamParser(inputStreamReader);

            //Mientras que exista un siguiente objeto
            while (p.hasNext()) {

                //Se hace referencia al siguiente objeto de p
                JsonElement e = p.next();

                //Sí el siguiente es un objeto JSON
                if (e.isJsonObject()) {

                    //Gracias al GSON y al JsonStreamParser se crea un objeto de tipo viajero con los datos proporcionados
                    ReservaS_Class reserva = gson.fromJson(e, ReservaS_Class.class);

                    if(reserva.id.equals(uid)){
                        //Se añade el nuevo objeto al árbol correpondiente
                        basededatos.EnqueueS(reserva,reserva.fecha,reserva.id);
                    }

                }
            }

        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(reservas_viajero.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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
    //Método para recuperar la base de datos de los usuarios de tipo viajero
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarV() {
        FileInputStream fileInputStream = null;
        try {
            //Se abre el archivo donde se guardan los viajeros registrados
            fileInputStream = openFileInput(FILE_V);

            //Se crea un objeto para leer el archivo
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            new BufferedReader(inputStreamReader);

            //Se crea un Gson y un JsonStreamParser para la lectura de los objetos
            Gson gson = new GsonBuilder().create();
            JsonStreamParser p = new JsonStreamParser(inputStreamReader);

            //Mientras que exista un siguiente objeto
            while (p.hasNext()) {

                //Se hace referencia al siguiente objeto de p
                JsonElement e = p.next();

                //Sí el siguiente es un objeto JSON
                if (e.isJsonObject()) {

                    //Gracias al GSON y al JsonStreamParser se crea un objeto de tipo viajero con los datos proporcionados
                    Viajero_class user = gson.fromJson(e, Viajero_class.class);

                    //Se añade el nuevo objeto al árbol correpondiente
                    basededatos.rviajero(user);
                }
            }

        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(reservas_viajero.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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
    void guardar_reservaS(ReservaS_Class user){
        String json = gson.toJson(user);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_PS, MODE_APPEND);
            fileOutputStream.write(json.getBytes());
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_PS);
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
    void guardar_reservaC(ReservaC_Class user){
        String json = gson.toJson(user);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_PC, MODE_APPEND);
            fileOutputStream.write(json.getBytes());
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_PC);
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
}
