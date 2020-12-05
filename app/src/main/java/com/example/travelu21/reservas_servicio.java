package com.example.travelu21;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class reservas_servicio extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter_Reservas_Servicio adapter;
    ArrayList<String[]> items;
    Basededatos basededatos;
    Queue<ReservaC_Class> queueC = new Queue<>();
    Queue<ReservaS_Class> queueS = new Queue<>();

    private static final String FILE_PC = "reservaC.json";
    private static final String FILE_PS = "reservaS.json";

    Gson gson = new Gson();

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

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            String id_viajero = extras.getString("id_viajero");
            String estado = extras.getString("estado");
            encolarC();
            borrarC();
            actualizarC(id_viajero, estado);
            encolarS();
            borrarS();
            actualizarS(id_viajero, estado);
        }
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

                        Date date=new Date(reserva.fecha);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                        String dateText = df2.format(date);
                        String[] parts = new String[4];

                        //Se crea un arreglo de tipo string que guarda los atributos pertinentes a la plantilla
                        parts[0] = reserva.Nombre;
                        parts[1] = reserva.correo;
                        parts[2] = dateText;
                        parts[3] = reserva.id_user;
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
    private void encolarC() {
        FileInputStream fileInputStream = null;
        try {
            //Se abre el archivo donde se guardan los hoteles registrados
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

                //Gracias al GSON y al JsonStreamParser se crea un objeto de tipo viajero con los datos proporcionados
                if (e.isJsonObject()) {

                    //Se añade el nuevo objeto al árbol correpondiente
                    ReservaC_Class user = gson.fromJson(e, ReservaC_Class.class);
                    queueC.enqueue(user);
                }
            }
            Toast.makeText(reservas_servicio.this,"Se carga", Toast.LENGTH_LONG).show();
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
    private void encolarS() {
        FileInputStream fileInputStream = null;
        try {
            //Se abre el archivo donde se guardan los hoteles registrados
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

                //Gracias al GSON y al JsonStreamParser se crea un objeto de tipo viajero con los datos proporcionados
                if (e.isJsonObject()) {

                    //Se añade el nuevo objeto al árbol correpondiente
                    ReservaS_Class user = gson.fromJson(e, ReservaS_Class.class);
                    queueS.enqueue(user);
                }
            }
            Toast.makeText(reservas_servicio.this,"Se carga", Toast.LENGTH_LONG).show();
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

    @SuppressLint("SetTextI18n")
    public void actualizarC(String id_user, String estado){

        while(!queueC.isEmpty()){
            ReservaC_Class current = queueC.dequeue();

            if (!current.id_user.equals(id_user)){
                guardar_reservaC(current);
            } else {
                current.estado = estado.equals("true");
                guardar_reservaC(current);
            }
        }
        Toast.makeText(reservas_servicio.this, "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    public void actualizarS(String id_user, String estado){

        while(!queueS.isEmpty()){
            ReservaS_Class current = queueS.dequeue();

            if (!current.id_user.equals(id_user)){
                guardar_reservaS(current);
            } else {
                current.estado = estado.equals("true");
                guardar_reservaS(current);
            }
        }
        Toast.makeText(reservas_servicio.this, "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
    }
    public void borrarS(){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_PS, MODE_PRIVATE);
            fileOutputStream.write(0);
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_PS);

            Toast.makeText(reservas_servicio.this, "Se ha borrado correctamente", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(reservas_servicio.this, "No se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(reservas_servicio.this, "No se ha actualizado correctamente x2", Toast.LENGTH_SHORT).show();
        } finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Toast.makeText(reservas_servicio.this, "No se ha actualizado correctamente x3", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void borrarC(){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_PC, MODE_PRIVATE);
            fileOutputStream.write(0);
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_PC);

            Toast.makeText(reservas_servicio.this, "Se ha borrado correctamente", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(reservas_servicio.this, "No se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(reservas_servicio.this, "No se ha actualizado correctamente x2", Toast.LENGTH_SHORT).show();
        } finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Toast.makeText(reservas_servicio.this, "No se ha actualizado correctamente x3", Toast.LENGTH_SHORT).show();
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

}
