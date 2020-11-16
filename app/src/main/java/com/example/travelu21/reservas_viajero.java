package com.example.travelu21;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.Date;

public class reservas_viajero extends AppCompatActivity {

    //Se instancian los elementos necesarios de forma global
    RecyclerView recyclerView;
    Adapter_Reservas_Cliente adapterRS;
    //Array que permite guardar los datos que serán pasados al adaptador
    ArrayList<String[]> items;

    //Base de datos para recuperar a las reservas del usuario
    Basededatos basededatos;

    //Se instancian e inicializar los archivos en los que se encuentran los usuarios
    private static final String FILE_PC = "reservaC.json";
    private static final String FILE_PS = "reservaS.json";
    private static final String FILE_V = "viajero.json";


    Gson gson = new Gson();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String uid;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_viajero);

        //Se inicializan los objetos instanciados previamente
        recyclerView = findViewById(R.id.cycler);
        items = new ArrayList<>();
        basededatos = new Basededatos();
        if(user!=null){
            uid = user.getUid();
        }

        recuperarV();

        Viajero_class user = basededatos.findv(uid);

        //Se traen los datos provenientes de búsqueda en caso de que provenga del layout de búsqueda
        Bundle extras = getIntent().getExtras();
        try{
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
                ReservaC_Class reservaC = new ReservaC_Class(uid,id,nom,corr,date.getTime(),ubi);
                ReservaS_Class reservaS = new ReservaS_Class(uid,id,user.nombre, user.correo,date.getTime());
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
        } catch (Exception e){
        //Se traen los datos provenientes de búsqueda en caso de que provenga del layout de búsqueda
            Bundle extrash = getIntent().getExtras();
            if (extrash!=null) {
                String nom = extrash.getString("nombreh");
                String ubi = extrash.getString("ubicacionh");
                String corr = extrash.getString("correoh");
                String fech = extrash.getString("fechah");
                String id = extrash.getString("uidh");

                try {
                    DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
                    assert fech != null;
                    Date date = sourceFormat.parse(fech);
                    assert date != null;
                    ReservaC_Class reservaC = new ReservaC_Class(uid, id, nom, corr, date.getTime(), ubi);
                    ReservaS_Class reservaS = new ReservaS_Class(uid, id, user.nombre, user.correo, date.getTime());
                    guardar_reservaC(reservaC);
                    guardar_reservaS(reservaS);
                    Toast.makeText(reservas_viajero.this, "¡Se ha guardado correctamente tu reserva!",
                        Toast.LENGTH_SHORT).show();
                    recuperarPC();
                } catch (ParseException e1) {
                    Toast.makeText(reservas_viajero.this, "El problema está aquí",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
        //Métodos que permiten integrar la plantilla con el layout general de la actividad
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterRS = new Adapter_Reservas_Cliente(this,items);
        recyclerView.setAdapter(adapterRS);

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
                    if(reserva.id_user.equals(uid)){
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
                        String[] parts = new String[5];

                        //Se crea un arreglo de tipo string que guarda los atributos pertinentes a la plantilla
                        parts[0] = reserva.Nombre;
                        parts[1] = reserva.ubicacion;
                        parts[2] = reserva.correo;
                        parts[3] = estado;
                        parts[4] = dateText;
                        items.add(parts);
                    }
                }
            }

        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(reservas_viajero.this,"No se está cargando la base de reservas cliente", Toast.LENGTH_LONG).show();
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
                        basededatos.EnqueueS(reserva,reserva.fecha,reserva.id_neg);
                    }

                }
            }

        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(reservas_viajero.this,"No se está cargando la base de reservas servicio", Toast.LENGTH_LONG).show();
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
            Toast.makeText(reservas_viajero.this,"No se está cargando la base de viajeros", Toast.LENGTH_LONG).show();
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
