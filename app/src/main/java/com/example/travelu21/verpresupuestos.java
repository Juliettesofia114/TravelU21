package com.example.travelu21;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
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
import java.io.InputStreamReader;
import java.util.ArrayList;

public class verpresupuestos extends AppCompatActivity {
    //Se instancian los elementos necesarios de forma global
    RecyclerView recyclerView;
    Adapter_Presupuesto adapterPresupuesto;
    String tipo;
    //Array que permite guardar los datos que serán pasados al adaptador
    ArrayList<String[]> items;

    //Base de datos para recuperar a los usuarios de tipo restaurante
    Basededatos basededatos;

    //Se instancian e inicializar los archivos en los que se encuentran los usuarios
    private static final String FILE_P = "presupuesto.json";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verpresupuestos);

        //Se inicializan los objetos instanciados previamente
        recyclerView = findViewById(R.id.cycler);
        basededatos = new Basededatos();
        items = new ArrayList<>();
        String len = Integer.toString(items.size());
        try{
            Bundle extras = getIntent().getExtras();
            if (extras!=null){
                tipo = extras.getString("tipo");
            } else {
                tipo = "0";
            }
        } catch (Exception e){
            Bundle extras = getIntent().getExtras();
            if (extras!=null){
                tipo = extras.getString("tipoh");
            } else {
                tipo = "0";
            }
        }

        //Se llena el árbol con los datos correspondientes
        recuperarP();

        //Métodos que permiten integrar la plantilla con el layout general de la actividad
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterPresupuesto = new Adapter_Presupuesto(this,items);
        recyclerView.setAdapter(adapterPresupuesto);
    }
    //Método para recuperar la base de datos de los usuarios de tipo viajero
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarP() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        FileInputStream fileInputStream = null;
        try {
            //Se abre el archivo donde se guardan los viajeros registrados
            fileInputStream = openFileInput(FILE_P);

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
                    Presupuesto_Class usercu = gson.fromJson(e, Presupuesto_Class.class);

                    if (usercu.id.contains(uid)){
                        //Se añade el nuevo objeto al árbol correpondiente
                        basededatos.ActualizarP(uid,usercu);

                        //Se crea un arreglo de tipo string que guarda los atributos pertinentes a la plantilla
                        String[] parts = new String[9];
                        parts[0] = usercu.des;
                        parts[1] = usercu.fecha;
                        parts[2] = uid;
                        parts[3] = usercu.dir;
                        parts[4] = usercu.per;
                        parts[5] = usercu.des;
                        parts[6] = usercu.com;
                        parts[7] = usercu.ho;
                        parts[8] = tipo;

                        items.add(parts);
                    }

                }
            }

        } catch (Exception e) {
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(verpresupuestos.this, "No se está cargando la base", Toast.LENGTH_LONG).show();
        } finally {
            if (fileInputStream != null) {
                try {
                    //Se cierra el lector de archivo
                    fileInputStream.close();
                } catch (Exception ignored) {

                }
            }
        }
    }
}
