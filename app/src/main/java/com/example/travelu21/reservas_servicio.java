package com.example.travelu21;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class reservas_servicio extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter_Reservas_Servicio adapter;
    ArrayList<String[]> items;
    Basededatos basededatos;

    private static final String FILE_PC = "reservaC.json";
    private static final String FILE_PS = "reservaS.json";


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Se instancian e inicializar los archivos en los que se encuentran los usuarios
    private static final String FILE_H = "hotel.json";
    private static final String FILE_R = "restaurante.json";

    String uid;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_servicio);

        //Se inicializan los objetos instanciados previamente
        recyclerView = findViewById(R.id.cycler);
        items = new ArrayList<>();
        basededatos = new Basededatos();
        if(user!=null){
            uid = user.getUid();
        }

        recuperarH();
        recuperarR();
        recuperarPS();

        try {
            Hotel_class user = basededatos.findh(uid);

        } catch (Exception e){
            Restaurante_class user = basededatos.findr(uid);
        }
        //Métodos que permiten integrar la plantilla con el layout general de la actividad
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Reservas_Servicio(this,items);
        recyclerView.setAdapter(adapter);
    }
    //Método para recuperar la base de datos de los usuarios de tipo hotel
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarH() {
        FileInputStream fileInputStream = null;
        try {
            //Se abre el archivo donde se guardan los hoteles registrados
            fileInputStream = openFileInput(FILE_H);

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
                    Hotel_class user = gson.fromJson(e, Hotel_class.class);

                    //Se añade el nuevo objeto al árbol correpondiente
                    basededatos.rHotel(user);
                }
            }

        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(reservas_servicio.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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

    //Método para recuperar la base de datos de los usuarios de tipo restaurante
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarR() {
        FileInputStream fileInputStream = null;
        try {
            //Se abre el archivo donde se guardan los hoteles registrados
            fileInputStream = openFileInput(FILE_R);

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

                //Gracias al GSON y al JsonStreamParser se crea un objeto de tipo viajero con los datos proporcionados
                if (e.isJsonObject()) {

                    //Se añade el nuevo objeto al árbol correpondiente
                    Restaurante_class user = gson.fromJson(e, Restaurante_class.class);

                    //Se añade el nuevo objeto al árbol correpondiente
                    basededatos.rRestaurante(user);
                }
            }
        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(reservas_servicio.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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
                    if(reserva.id_neg.equals(uid)){
                        //Se añade el nuevo objeto al árbol correpondiente
                        //basededatos.EnqueueC(reserva,reserva.fecha,reserva.id_user);
                        Boolean est = reserva.estado;
                        String estado = "";
                        if (est){
                            estado = "Aceptada";
                        } else {
                            estado = "En espera";
                        }
                        Date date=new Date(reserva.fecha);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                        String dateText = df2.format(date);
                        String[] parts = new String[3];

                        //Se crea un arreglo de tipo string que guarda los atributos pertinentes a la plantilla
                        parts[0] = reserva.Nombre;
                        parts[1] = reserva.correo;
                        parts[2] = dateText;
                        items.add(parts);
                    }
                }
            }

        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(reservas_servicio.this,"No se está cargando la base de reservas servicio", Toast.LENGTH_LONG).show();
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

}
