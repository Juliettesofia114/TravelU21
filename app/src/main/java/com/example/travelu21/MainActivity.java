package com.example.travelu21;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity { //Clase que maneja la pantalla de inicio de sesión
    //Se instancian objetos propios de Firebase para llamar los datos que se encuentran en Firebase
    private FirebaseAuth mAuth;

    //Se instancian los objetos relacionados a la interfaz gráfica
    private EditText email, pass;
    private CheckBox viajero;
    private CheckBox negocio;

    //Se instancian e inicializar los archivos en los que se encuentran los usuarios
    private static final String FILE_V = "viajero.json";
    private static final String FILE_H = "hotel.json";
    private static final String FILE_R = "restaurante.json";
    private static final String FILE_CRED = "credenciales.json";

    //Se instacia e inicializa una nueva base de datos en donde se recuperan los datos
    Basededatos basededatos = new Basededatos();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    HashTable credenciales = new HashTable(500,503);
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se asocian los componentes del layout con los objetos instanciados previamente
        //Correo y contraseña
        email = findViewById(R.id.email);
        pass = findViewById(R.id.Password);

        //Botones de registro e inicio de sesión
        Button iniciar = findViewById(R.id.Iniciosesion);
        Button registrar = findViewById(R.id.Registro);

        //Link para cambiar contraseña
        TextView forgot = findViewById(R.id.forgot);

        //Checkbox para iniciar sesión
        viajero = findViewById(R.id.viajero);
        negocio = findViewById(R.id.negocio);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Se recuperan los datos de la base de datos y se poblan las estructuras de datos
        recuperarH();
        recuperarR();
        recuperarV();
        recuperarC();

        //Método que permite verificar si hay un usuario activo
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Si el usuario activo no es nulo
        if (firebaseUser != null){

            //Se pide a la base de datos el identificador único de cada usuario
            String uid = firebaseUser.getUid();

            //Se llama al método que retorna el número del tipo de usuario
            int ti = tipo(uid);

            //Condicional que permite al sistema saber qué tipo de usuario es el que se encuentra activo y así iniciar la actividad correspondiente
            if (ti == 0){

                //Si el tipo de usuario es 0, es un usuario de tipo viajero
                Intent i = new Intent(MainActivity.this, main_viajeros.class);
                startActivity(i);
            } else if (ti == 1 || ti == 2){

                //Si el tipo de usuario es 1 0 2, es un usuario de tipo servicio (restaurante y hotel respectivamente)
                Intent i = new Intent(MainActivity.this, main_servicio.class);
                startActivity(i);
            }
        }

        //Se generan eventos para el momento de clickear botones y checkboxes
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarUsuario();
            }
        });
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se dirige a la actividad de registro
                Intent i = new Intent(MainActivity.this, opreg1.class);
                startActivity(i);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
        viajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //¿Está checkeado?
                if (viajero.isChecked()) {
                    //El checkbox de negocio se inhabilita
                    negocio.setEnabled(false);
                } else {
                    negocio.setEnabled(true);
                }
            }
        });
        negocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //¿Está checkeado?
                if (negocio.isChecked()) {
                    //El checkbox de viajero se inhabilita
                    viajero.setEnabled(false);
                } else {
                    viajero.setEnabled(true);
                }
            }
        });
    }
    //Método en caso de que se olvide la contraseña
    public void resetPassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Se ha enviado un correo a la cuenta para reestablecer tu contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Hubo un error, inténtalo de nuevo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Método para iniciar usuario con la base de datos local y con Firebase
    private void iniciarUsuario() {
        //Obtenemos el email y la contraseña
        final String Em = email.getText().toString().trim();
        String Ps = pass.getText().toString().trim();

        //Evita los campos vacíos
        if (Em.isEmpty()) {
            email.setError("Campo obligatorio");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Em).matches()) {
            //Evita que se ingrese un correo electrónico no válido
            email.setError("Ingrese un correo válido");
            email.requestFocus();
            return;
        }

        if (Ps.isEmpty()) {
            pass.setError("Campo obligatorio");
            pass.requestFocus();
            return;
        }

        if (Ps.length() <= 5) {
            //Verifica que la contraseña tenga más de 6 caractéres
            pass.setError("La contraseña debe ser mínimo 6 carácteres");
            pass.requestFocus();
            return;
        }
        //Proceso para autenticar el usuario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (credenciales.isIn(Em,Ps)){
                mAuth.signInWithEmailAndPassword(Em, Ps)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Revisa qué tipo de usuario es el que está ingresando
                            if (viajero.isChecked()){

                                //Se obtiene el contexto del usuario que ingresa
                                FirebaseAuth.getInstance().getCurrentUser();

                                //Verificación de que Firebase está obteniendo correctamente la información de usuario para demás funcionalidades
                                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                //assert user != null;
                                //String uid = user.getUid();

                                //Mensaje de ingreso exitoso
                                Toast.makeText(MainActivity.this,"Bienvenido a TravelU2", Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this,uid, Toast.LENGTH_LONG).show();

                                //Se crea un intent para pasar de una actividad a otra (de tipo viajero) y no se permite regresar a la actividad anterior
                                Intent i = new Intent(MainActivity.this, main_viajeros.class);
                                startActivity(i);
                                finish();
                            } else if (negocio.isChecked()){
                                //Se obtiene el contexto del usuario que ingresa
                                FirebaseAuth.getInstance().getCurrentUser();

                                //Mensaje de ingreso exitoso
                                Toast.makeText(MainActivity.this,"Bienvenido a TravelU2", Toast.LENGTH_LONG).show();

                                //Se crea un intent para pasar de una actividad a otra (de tipo servicio) y no se permite regresar a la actividad anterior
                                Intent i = new Intent(MainActivity.this, main_servicio.class);
                                startActivity(i);
                                finish();
                            }

                        } else {
                            // Si la autenticación falla se muestra un mensaje al usuario.
                            Toast.makeText(MainActivity.this,"Ha ocurrido un error al iniciar sesión", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(MainActivity.this,"Las credenciales ingresadas son incorrectas, intenta nuevamente.", Toast.LENGTH_LONG).show();
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
            Toast.makeText(MainActivity.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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
            Toast.makeText(MainActivity.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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
            Toast.makeText(MainActivity.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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
    private void recuperarC() {
        FileInputStream fileInputStream = null;
        try {
            //Se abre el archivo donde se guardan los hoteles registrados
            fileInputStream = openFileInput(FILE_CRED);

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
                    User_init_Class user = gson.fromJson(e, User_init_Class.class);

                    //Se añade el nuevo objeto al árbol correpondiente
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        credenciales.add(user.correo,user.contrasena);
                    }
                }
            }
        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(MainActivity.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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

}
