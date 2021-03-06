package com.example.travelu21;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class busqueda_hotel extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Se instancian los elementos necesarios de forma global
    RecyclerView recyclerView;
    Adapter_Hotel adapterHo;
    Button filtro;
    //Array que permite guardar los datos que serán pasados al adaptador
    ArrayList<String[]> items;

    //Base de datos para recuperar a los usuarios de tipo restaurante
    Basededatos basededatos;

    //Nombre del archivo donde se guardan los usuarios de tipo restaurante
    private static final String FILE_H = "hotel.json";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_hotel);

        //Se referencia la lista desplegable
        Spinner dropdown = findViewById(R.id.spinner1);

        //Se crea la lista de elementos que va a tener el spinner
        String[] itemsvar = new String[]{"Ordernar por:", "Precio", "Nombre","Máximo de personas"};

        //Se crea un adaptador que define el contexto, el estilo y los elementos a utilizar
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsvar);

        //Se añade el adaptador al spinner
        dropdown.setAdapter(adapter);

        //Se le asignan funciones al spinner en caso de que se seleccione un elemento de la lista
        dropdown.setOnItemSelectedListener(this);

        //Se inicializan los objetos instanciados previamente
        recyclerView = findViewById(R.id.cycler);
        filtro = findViewById(R.id.filtros);
        items = new ArrayList<>();
        basededatos = new Basededatos();

        //Se le asigna una función al botón de la interfaz gráfica
        filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se envía al usuario a la actividad donde se muestran los presupuestos disponibles
                Intent i = new Intent(busqueda_hotel.this,verpresupuestos.class);
                i.putExtra("tipo","hotel");
                startActivity(i);
            }
        });
    }

    //Método para recuperar la base de datos de los usuarios de tipo hotel
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarH(int position) {
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

                    //Se traen los datos provenientes de búsqueda en caso de que provenga del layout de búsqueda
                    Bundle extras = getIntent().getExtras();
                    if (extras!= null) {
                        String total = extras.getString("total");
                        String personas = extras.getString("personas");
                        String destino = extras.getString("destino");
                        String hotel = extras.getString("hotel");
                        assert personas != null;
                        assert destino != null;
                        if (destino.equals(user.ubicacion) && Integer.parseInt(hotel)>user.precio) {
                            //Se crea un arreglo de tipo string que guarda los atributos pertinentes a la plantilla
                            String[] parts = new String[9];
                            parts[0] = user.nombreEmpresa;
                            parts[1] = user.ubicacion;
                            parts[2] = Integer.toString(user.max);
                            parts[3] = user.correo;
                            parts[4] = user.wifi;
                            parts[5] = user.piscina;
                            parts[6] = user.desayuno;
                            parts[7] = user.id;
                            parts[8] = Integer.toString(user.precio);
                            //Condicional que revisa si está aplicado algún orden de búsqueda
                            if (position == 0){

                                //Se añade el arreglo al arreglo que se pasará como parámetro al adaptador
                                items.add(parts);
                            } else if(position ==1){
                                int count = 0;
                                boolean in = false;
                                if(items.isEmpty()){
                                    items.add(parts);
                                } else {

                                    //Se itera en los elementos del arreglo que será pasada al adaptador
                                    for (int i = 0; i<items.size();i++) {

                                        //Si el objeto que ingresa tiene precio menor al de i, se añade el objeto en la posición de i y los elementos del arreglo se corren una posición
                                        if (Integer.parseInt(items.get(i)[8]) < user.precio) {
                                            items.add(count, parts);
                                            in = true;
                                            break;
                                        }
                                        count++;
                                    }

                                    //En caso de que se reccora toda la lista y el objeto no haya podido ser ingresado, se añade al final del arreglo
                                    if (!in) {
                                        items.add(parts);
                                    }
                                }
                            } else if(position ==2){
                                int count = 0;
                                boolean in = false;
                                if(items.isEmpty()){
                                    items.add(parts);
                                } else {

                                    //Se itera en los elementos del arreglo que será pasada al adaptador
                                    for (int i = 0; i<items.size();i++) {

                                        //Si el objeto que ingresa tiene un nombre con un primer caracter menor al primero de i, se añade el objeto en la posición de i y los elementos del arreglo se corren una posición
                                        int num = user.nombreEmpresa.charAt(0);
                                        int mu1 = items.get(i)[0].charAt(0);
                                        if (num<=mu1){
                                            items.add(count,parts);
                                            in = true;
                                            break;
                                        }
                                        count++;
                                    }

                                    //En caso de que se reccora toda la lista y el objeto no haya podido ser ingresado, se añade al final del arreglo
                                    if (!in) {
                                        items.add(parts);
                                    }
                                }
                            } else if(position ==3){
                                int count = 0;
                                boolean in = false;
                                if(items.isEmpty()){
                                    items.add(parts);
                                } else {

                                    //Se itera en los elementos del arreglo que será pasada al adaptador
                                    for (int i = 0; i<items.size();i++) {

                                        //Si el objeto que ingresa tiene precio menor al de i, se añade el objeto en la posición de i y los elementos del arreglo se corren una posición
                                        if (Integer.parseInt(items.get(i)[2]) < user.max) {
                                            items.add(count, parts);
                                            in = true;
                                            break;
                                        }
                                        count++;
                                    }

                                    //En caso de que se reccora toda la lista y el objeto no haya podido ser ingresado, se añade al final del arreglo
                                    if (!in) {
                                        items.add(parts);
                                    }
                                }
                            }
                        }
                    } else {
                    //Se crea un arreglo de tipo string que guarda los atributos pertinentes a la plantilla
                    String[] parts = new String[9];
                    parts[0] = user.nombreEmpresa;
                    parts[1] = user.ubicacion;
                    parts[2] = Integer.toString(user.max);
                    parts[3] = user.correo;
                    parts[4] = user.wifi;
                    parts[5] = user.piscina;
                    parts[6] = user.desayuno;
                    parts[7] = user.id;
                    parts[8] = Integer.toString(user.precio);
                    //Condicional que revisa si está aplicado algún orden de búsqueda
                    if (position == 0){

                        //Se añade el arreglo al arreglo que se pasará como parámetro al adaptador
                        items.add(parts);
                    } else if(position ==1){
                        int count = 0;
                        boolean in = false;
                        if(items.isEmpty()){
                            items.add(parts);
                        } else {

                            //Se itera en los elementos del arreglo que será pasada al adaptador
                            for (int i = 0; i<items.size();i++) {

                                //Si el objeto que ingresa tiene precio menor al de i, se añade el objeto en la posición de i y los elementos del arreglo se corren una posición
                                if (Integer.parseInt(items.get(i)[8]) < user.precio) {
                                    items.add(count, parts);
                                    in = true;
                                    break;
                                }
                                count++;
                            }

                            //En caso de que se reccora toda la lista y el objeto no haya podido ser ingresado, se añade al final del arreglo
                            if (!in) {
                                items.add(parts);
                            }
                        }
                    } else if(position ==2){
                        int count = 0;
                        boolean in = false;
                        if(items.isEmpty()){
                            items.add(parts);
                        } else {

                            //Se itera en los elementos del arreglo que será pasada al adaptador
                            for (int i = 0; i<items.size();i++) {

                                //Si el objeto que ingresa tiene un nombre con un primer caracter menor al primero de i, se añade el objeto en la posición de i y los elementos del arreglo se corren una posición
                                int num = user.nombreEmpresa.charAt(0);
                                int mu1 = items.get(i)[0].charAt(0);
                                if (num<=mu1){
                                    items.add(count,parts);
                                    in = true;
                                    break;
                                }
                                count++;
                            }

                            //En caso de que se reccora toda la lista y el objeto no haya podido ser ingresado, se añade al final del arreglo
                            if (!in) {
                                items.add(parts);
                            }
                        }
                    } else if(position ==3){
                        int count = 0;
                        boolean in = false;
                        if(items.isEmpty()){
                            items.add(parts);
                        } else {

                            //Se itera en los elementos del arreglo que será pasada al adaptador
                            for (int i = 0; i<items.size();i++) {

                                //Si el objeto que ingresa tiene precio menor al de i, se añade el objeto en la posición de i y los elementos del arreglo se corren una posición
                                if (Integer.parseInt(items.get(i)[2]) < user.max) {
                                    items.add(count, parts);
                                    in = true;
                                    break;
                                }
                                count++;
                            }

                            //En caso de que se reccora toda la lista y el objeto no haya podido ser ingresado, se añade al final del arreglo
                            if (!in) {
                                items.add(parts);
                            }
                        }
                    }
                }
                }
            }


        } catch (Exception e){
            //Mensaje en pantalla en caso de que no se haya cargado correctamente la base de datos
            Toast.makeText(busqueda_hotel.this,"No se está cargando la base", Toast.LENGTH_LONG).show();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //Método que le brinda al spinner sensibilidad a cambios en el elemento escogido
        //Se eliminan los elementos del arreglo que se pasa como parámetro al adaptador para que no hayan elementos repetidos
        items.clear();

        //Se llena el árbol con los datos correspondientes
        recuperarH(position);

        //Métodos que permiten integrar la plantilla con el layout general de la actividad
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterHo = new Adapter_Hotel(this,items);
        recyclerView.setAdapter(adapterHo);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
