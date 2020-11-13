package com.example.travelu21;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class vercuenta extends AppCompatActivity {
    //Se instancian objetos propios de Firebase para llamar los datos que se encuentran en Firebase
    private FirebaseAuth mAuth;

    //Se intancian los objetos de forma global
    EditText correo, descripcion;
    Button guardar, contra, cancelar;

    //Se instancia e inicializa el archivo en el que se encuentran los usuarios
    private static final String FILE_V = "viajero.json";

    //Se intancia una nueva base de datos para recuperar los datos
    Basededatos basededatos = new Basededatos();

    //Se instancia un nuevo viajero que tomará los datos del usuario activo
    Viajero_class current;

    Gson gson = new Gson();
    private static final String FILE_NAME = "viajero.json";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vercuenta);

        //Se relacionan las instancias con la interfaz gráfica
        //EditTexts
        correo = findViewById(R.id.correo);
        descripcion = findViewById(R.id.descripcion);
        //Botones
        guardar = findViewById(R.id.guardar);
        contra = findViewById(R.id.contra);
        cancelar = findViewById(R.id.cance);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Se recuperan los datos de los viajeros
        recuperarV();

        //Se piden los datos del usuario activo a la base de datos
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        //Se pide el identificador único del usuario
        String uid = user.getUid();

        //Se buscan los datos del usuario en el árbol de búsqueda binario
        current = basededatos.findv(uid);
        correo.setText(current.correo);
        descripcion.setText(current.descripcion);

        //Se le asignan funciones a los botones
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            Toast.makeText(vercuenta.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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
    //Método en caso de que se olvide la contraseña
    public void resetPassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(correo.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(vercuenta.this, "Se ha enviado un correo a la cuenta para reestablecer tu contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(vercuenta.this, "Hubo un error, inténtalo de nuevo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void guardar(){
        final String nuevo_correo = correo.getText().toString();
        final String nuevo_descrip = descripcion.getText().toString();
        if(nuevo_correo.isEmpty()){
            correo.setError("Campo obligatorio");
            correo.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(nuevo_correo).matches()) {
            correo.setError("Ingrese un correo válido");
            correo.requestFocus();
            return;
        }

        if(nuevo_descrip.isEmpty()){
            descripcion.setError("Campo obligatorio");
            descripcion.requestFocus();
            return;
        }

        if (current.correo!= nuevo_correo){
        }
        if (current.descripcion != nuevo_descrip){

        }
    }
    void guardar_viajero(Viajero_class user){
        String json = gson.toJson(user);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_NAME, MODE_APPEND);
            fileOutputStream.write(json.getBytes());
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_NAME);
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
