package com.example.travelu21;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class registroc extends AppCompatActivity {
    private EditText nombre;
    private EditText correo;
    private EditText contrasena;
    private EditText descripcion;
    private EditText fecha;
    private FirebaseAuth mAuth;
    Gson gson = new Gson();
    private static final String FILE_NAME = "viajero.json";
    private static final String FILE_CRED = "credenciales.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroc);

        //Se le pide a Firebase un "contexto"
        mAuth = FirebaseAuth.getInstance();

        //Se inicializan los objetos relacionándolos con su respectivo componente de la interfaz gráfica
        Button regisc = findViewById(R.id.registro);
        Button cancelar = findViewById(R.id.cancelc);
        nombre = findViewById(R.id.nombrec);
        correo = findViewById(R.id.emailc);
        contrasena = findViewById(R.id.contra);
        descripcion = findViewById(R.id.descripcion);
        fecha = findViewById(R.id.fechanacimiento);

        //Se le asignan funciones a los botones de la interfaz gráfica
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registroc.this, MainActivity.class));
            }
        });
        regisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }

    private void registrar() {
        //Se recogen los datos dados por el usuario desde la interfaz gráfica
        final String nom = nombre.getText().toString().trim();
        final String corr = correo.getText().toString().trim();
        final String pass = contrasena.getText().toString().trim();
        final String fech = fecha.getText().toString().trim();
        final String des = descripcion.getText().toString().trim();

        //Se verifica que los campos obligatorios hayan sido llenados correctamente
        if (nom.isEmpty()) {
            nombre.setError("Campo obligatorio");
            nombre.requestFocus();
            return;
        }

        if (corr.isEmpty()) {
            correo.setError("Campo obligatorio");
            correo.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(corr).matches()) {
            correo.setError("Ingrese un correo válido");
            correo.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            contrasena.setError("Campo obligatorio");
            contrasena.requestFocus();
            return;
        }
        if (fech.isEmpty()) {
            fecha.setError("Campo obligatorio");
            fecha.requestFocus();
            return;
        }

        if (pass.length() <= 5) {
            contrasena.setError("La contraseña debe ser mínimo 6 carácteres");
            contrasena.requestFocus();
        }

        //La función predeterminada por Firebase para el registro de un nuevo usuario, donde se piden correo y contraseña
        mAuth.createUserWithEmailAndPassword(corr, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Si el registro en Firebase es exitoso se llevan a cabo las siguientes instrucciones
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Se consige la id única creada por FireBase
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            assert currentUser != null;
                            String uid = currentUser.getUid();

                            //Se crea un nuevo objeto de tipo viajero el cual será el que almacenará los datos dados por el usuario
                            Viajero_class nuevo = new Viajero_class(nom,pass,corr,des,0,uid);
                            User_init_Class nuevascred = new User_init_Class(corr,pass);

                            try {
                                //Se intenta hacer el proceso de registro del nuevo objeto en el archivo JSON
                                guardar_viajero(nuevo);

                                //Se intenta hacer el proceso de registro del nuevo objeto en el archivo JSON
                                guardar_Hash(nuevascred);

                                //Se envía a pantalla un mensaje de bienvenida al usuario
                                Toast.makeText(registroc.this, "¡Bienvenido a TravelU2: "+nuevo.nombre+"!",
                                        Toast.LENGTH_SHORT).show();

                                //Se envía al usuario a la actividad principal de tipo viajero sin la posibilidad de devolverse
                                Intent i = new Intent(registroc.this, main_viajeros.class);
                                startActivity(i);
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            //De lo contrario se le muestra un mensaje de error.
                            Toast.makeText(registroc.this, "Algo falló :(, inténtalo nuevamente.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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

    void guardar_Hash(User_init_Class user){
        String json = gson.toJson(user);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput(FILE_CRED, MODE_APPEND);
            fileOutputStream.write(json.getBytes());
            Log.d("TAG1","Fichero salvado en: "+getFilesDir()+"/"+FILE_CRED);
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
