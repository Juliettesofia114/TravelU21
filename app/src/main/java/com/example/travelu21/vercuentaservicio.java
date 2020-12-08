package com.example.travelu21;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class vercuentaservicio extends AppCompatActivity {

    //Se instancian objetos propios de Firebase para llamar los datos que se encuentran en Firebase
    private FirebaseAuth mAuth;

    //Se intancian los objetos de forma global
    EditText correo, descripcion;
    Button guardar, contra, cancelar;

    //Se instacia e inicializa una nueva base de datos en donde se recuperan los datos
    Basededatos basededatos = new Basededatos();

    //Se instancia e inicializa el archivo en el que se encuentran los usuarios
    private static final String FILE_R = "restaurante.json";
    private static final String FILE_H = "hotel.json";

    Queue<Restaurante_class> queuer = new Queue<>();
    Queue<Hotel_class> queueh = new Queue<>();

    //Se instancia un nuevo viajero que tomará los datos del usuario activo
    Restaurante_class currentr;
    Hotel_class currenth;

    Gson gson = new Gson();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vercuentaservicio);
        //Se relacionan las instancias con la interfaz gráfica
        //EditTexts
        correo = findViewById(R.id.correo);
        descripcion = findViewById(R.id.descripcion);
        //Botones
        guardar = findViewById(R.id.guardar);
        contra = findViewById(R.id.contra);
        cancelar = findViewById(R.id.cance);

        //Se piden los datos del usuario activo a la base de datos
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Se recuperan los datos de los viajeros
        recuperarR();
        recuperarH();

        //Se pide el identificador único del usuario
        final String uid = user.getUid();

        //Se llama al método que retorna el número del tipo de usuario
        int ti = tipo(uid);

        //Condicional que permite al sistema saber qué tipo de usuario es el que se encuentra activo y así iniciar la actividad correspondiente
        if (ti == 1){
            //Se buscan los datos del usuario en el árbol de búsqueda binario
            currentr = basededatos.findr(uid);
            correo.setText(currentr.correo);
            descripcion.setText(currentr.descripcion);
            //Se le asignan funciones a los botones
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String corr = correo.getText().toString().trim();
                    String des = descripcion.getText().toString().trim();

                    if (!corr.equals(currentr.correo)){
                        if(corr.isEmpty()){
                            correo.setError("Campo obligatorio");
                            correo.requestFocus();
                            return;
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(corr).matches()) {
                            correo.setError("Ingrese un correo válido");
                            correo.requestFocus();
                            return;
                        }
                        currentr.correo = corr;
                        encolarr();
                        borrarr();
                        actualizarr(currentr);
                        guardar_restaurante(currentr);
                    }
                    if (!des.equals(currentr.descripcion)){
                        currentr.descripcion = des;
                        encolarr();
                        borrarr();
                        actualizarr(currentr);
                        guardar_restaurante(currentr);
                        recuperarR();
                        currentr = basededatos.findr(uid);
                        descripcion.setText(currentr.descripcion);
                    }
                }
            });
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), main_viajeros.class));
                }
            });
            contra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPassword();
                }
            });
        } else if (ti == 2){
            currenth = basededatos.findh(uid);
            correo.setText(currenth.correo);
            descripcion.setText(currenth.descripcion);
            //Se le asignan funciones a los botones
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String corr = correo.getText().toString().trim();
                    String des = descripcion.getText().toString().trim();

                    if (!corr.equals(currenth.correo)){
                        if(corr.isEmpty()){
                            correo.setError("Campo obligatorio");
                            correo.requestFocus();
                            return;
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(corr).matches()) {
                            correo.setError("Ingrese un correo válido");
                            correo.requestFocus();
                            return;
                        }
                        currenth.correo = corr;
                        encolarh();
                        borrarh();
                        actualizarh(currenth);
                        guardar_hotel(currenth);
                    }
                    if (!des.equals(currenth.descripcion)){
                        currenth.descripcion = des;
                        encolarh();
                        borrarh();
                        actualizarh(currenth);
                        guardar_hotel(currenth);
                        recuperarH();
                        currenth = basededatos.findh(uid);
                        descripcion.setText(currenth.descripcion);
                    }
                }
            });
            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), main_viajeros.class));
                }
            });
            contra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPassword();
                }
            });
        }



    }
    void guardar_restaurante(Restaurante_class user){
        String json = gson.toJson(user);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_R, MODE_APPEND);
            fileOutputStream.write(json.getBytes());
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_R);
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
    void guardar_hotel(Hotel_class user){
        String json = gson.toJson(user);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_H, MODE_APPEND);
            fileOutputStream.write(json.getBytes());
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_H);
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
    //Método en caso de que se olvide la contraseña
    public void resetPassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(correo.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(vercuentaservicio.this, "Se ha enviado un correo a la cuenta para reestablecer tu contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(vercuentaservicio.this, "Hubo un error, inténtalo de nuevo", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    //Método que retorna el tipo de usuario del usuario activo
    public int tipo(String uid){ //Se pide el identificador único del usuario activo
        if(basededatos.IsInV(uid)){ //Se busca en la base de datos de viajeros
            return 0;
        } else if(basededatos.IsInH(uid)){ //Se busca en la base de datos de hoteles
            return 1;
        } else if(basededatos.IsInR(uid)){ //Se busca en la base de datos de restaurantes
            return 2;
        } else { //En caso de que no se encuentre
            return -1;
        }
    }

    private void encolarh() {
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

                //Gracias al GSON y al JsonStreamParser se crea un objeto de tipo viajero con los datos proporcionados
                if (e.isJsonObject()) {

                    //Se añade el nuevo objeto al árbol correpondiente
                    Hotel_class user = gson.fromJson(e, Hotel_class.class);
                    queueh.enqueue(user);
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
    public void actualizarh(Hotel_class user){

        while(!queueh.isEmpty()){
            Hotel_class current = queueh.dequeue();

            if (!current.id.equals(user.id)){
                guardar_hotel(current);

            }
        }
        Toast.makeText(vercuentaservicio.this, "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
    }
    public void borrarh(){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_H, MODE_PRIVATE);
            fileOutputStream.write(0);
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_H);

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
    private void encolarr() {
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
                    queuer.enqueue(user);
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
    public void actualizarr(Restaurante_class user){

        while(!queuer.isEmpty()){
            Restaurante_class current = queuer.dequeue();

            if (!current.id.equals(user.id)){
                guardar_restaurante(current);

            }
        }
        Toast.makeText(vercuentaservicio.this, "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
    }
    public void borrarr(){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_R, MODE_PRIVATE);
            fileOutputStream.write(0);
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_R);

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
