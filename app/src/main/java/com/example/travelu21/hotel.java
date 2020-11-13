package com.example.travelu21;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class hotel extends AppCompatActivity {

    //Se intancian los widgets a utilizar de forma global
    private EditText precio;
    private EditText max;
    private CheckBox wifi;
    private CheckBox piscina;
    private CheckBox desayuno;
    private FirebaseAuth mAuth;
    Gson gson = new Gson();
    private static final String FILE_NAME = "hotel.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        //Se le pide a Firebase un "contexto"
        mAuth = FirebaseAuth.getInstance();

        //Se inicializan los objetos relacionándolos con su respectivo componente de la interfaz gráfica
        Button regist = findViewById(R.id.registodefh);
        Button cancelar = findViewById(R.id.cancel3);
        precio = findViewById(R.id.precio);
        max = findViewById(R.id.personas);
        wifi = findViewById(R.id.wifi);
        piscina = findViewById(R.id.piscina);
        desayuno = findViewById(R.id.desayuno);

        //Se le asignan funciones a los botones de la interfaz gráfica
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(hotel.this, MainActivity.class));
            }
        });
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }

    private void registrar() {
        //Se crea un objeto de tipo Bundle para traer la información recogida previamente y se verifica que no sea nulo
        Bundle extras = getIntent().getExtras();
        assert extras != null;

        //Se le asigna a cada String un dato del contenido en el bundle
        final String nom = extras.getString("nombre");
        final String emp = extras.getString("empresa");
        final String corr = extras.getString("correo");
        final String ubi = extras.getString("ubicacion");
        final String url1 = extras.getString("url");
        final String pass = extras.getString("password");
        final String des = extras.getString("descrip");
        String fech = extras.getString("fecha");

        //Se le asigna a cada String las entradas dadas por el usuario desde la interfaz gráfica
        final int prec = Integer.parseInt(precio.getText().toString().trim());
        final int maximum = Integer.parseInt(max.getText().toString().trim());

        //Se crean los strings que contendrán valores booleanos
        String wif;
        String pisci;
        String desa;

        //Se establecen los valores de cada string según sea el caso
        if (wifi.isChecked()){
            wif = "true";
        } else {
            wif = "false";
        }
        if (piscina.isChecked()){
            pisci = "true";
        } else {
            pisci = "false";
        }
        if (desayuno.isChecked()){
            desa = "true";
        } else {
            desa = "false";
        }

        //Se crean strings final para poder usarlas en el registro
        final String finalWif = wif;
        final String finalPisci = pisci;
        final String finalDesa = desa;

        //Se verifica que corr y pass no sean nulos y evitar errores
        assert corr != null;
        assert pass != null;

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
                            Hotel_class nuevo = new Hotel_class(nom,emp,ubi,pass,corr,2,des,url1,uid,prec,maximum,finalWif,finalDesa,finalPisci);

                            try {
                                //Se intenta hacer el proceso de registro del nuevo objeto en el archivo JSON
                                guardar_hotel(nuevo);
                                //Se envía a pantalla un mensaje de bienvenida al usuario
                                Toast.makeText(hotel.this, "¡Bienvenido a TravelU2: "+nuevo.nombreEmpresa+"!",
                                        Toast.LENGTH_SHORT).show();
                                //Se envía al usuario a la actividad principal de tipo servicio sin la posibilidad de devolverse
                                Intent i = new Intent(hotel.this, main_servicio.class);
                                startActivity(i);
                                finish();

                            } catch (Exception e) {
                                Toast.makeText(hotel.this, "No se pudo guardar correctamente, inténtalo más tarde.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //De lo contrario se le muestra un mensaje de error.
                            Toast.makeText(hotel.this, "Algo falló :(, inténtalo nuevamente.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    void guardar_hotel(Hotel_class user){
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
