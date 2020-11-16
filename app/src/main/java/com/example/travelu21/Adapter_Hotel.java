package com.example.travelu21;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Adapter_Hotel extends RecyclerView.Adapter<Adapter_Hotel.ViewHolder> {
    //Se instancia un Inflater el cual permite adaptar la plantilla al layout principal, un array que contiene los datos y el contexto de la clase de búsqueda de hoteles
    private LayoutInflater layoutInflater;
    private ArrayList<String[]> data;
    private Context mcontext;

    Adapter_Hotel(Context context, ArrayList<String[]> data){ //Constructor
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        mcontext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Se genera el Inflater el cual se relaciona con su respectiva plantilla
        View view = layoutInflater.inflate(R.layout.card_busqueda_h,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        //Se toman los datos del arreglo en String
        final String nom = data.get(i)[0];
        final String ubi = data.get(i)[1];
        String max = data.get(i)[2];
        final String corr = data.get(i)[3];
        String wifi = data.get(i)[4];
        String pis = data.get(i)[5];
        String des = data.get(i)[6];
        final String uuid = data.get(i)[7];
        final String precio = data.get(i)[8];
        final String fech = holder.fecha.getText().toString().trim();

        //Se crea una nueva base datos para añadir la reserva a los atributos del usuario de tipo viajero y de tipo servicio
        Basededatos base = new Basededatos();

        String wif = "";
        String desa = "";
        String psi = "";
        if(wifi.equals("false")){
            wif = "no";
        } else {
            wif = "sí";
        }
        if(des.equals("false")){
            desa = "no";
        } else {
            desa = "sí";
        }
        if(pis.equals("false")){
            psi = "no";
        } else {
            psi = "sí";
        }

        //Se llenan los datos de la plantilla con los datos de la base de datos
        holder.wifi.setText("wifi: "+wif);
        holder.piscina.setText("piscina: "+psi);
        holder.desayuno.setText("desayuno incluído: "+desa);
        holder.nombre.setText(nom);
        holder.ubicacion.setText(ubi);
        holder.correo.setText(corr);
        holder.maximo.setText("máximo de personas: "+max);
        holder.precio.setText("precio por noche: "+precio);

        //Se le da una función al botón de reserva
        //Se le da una función al botón de reserva
        holder.reservar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                final String fech = holder.fecha.getText().toString().trim();
                //Se pide la fecha y hora actual para guardarla en los datos
                long date = System.currentTimeMillis();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                String dateString = sdf.format(date);

                //Se crea un intent que llevará consigo los datos recogidos a la siguiente actividad
                Intent i = new Intent(mcontext,reservas_viajero.class);
                i.putExtra("nombreh",nom);
                i.putExtra("ubicacionh",ubi);
                i.putExtra("correoh", corr);
                i.putExtra("fechah",fech);
                i.putExtra("uidh",uuid);

                //Envía al usuario a la actividad donde se guardan las reservas
                mcontext.startActivity(i);
                Toast.makeText(mcontext, uuid,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //Se instancian los text views
        TextView nombre;
        TextView ubicacion;
        TextView maximo;
        TextView correo;
        TextView wifi;
        TextView piscina;
        TextView desayuno;
        TextView precio;
        EditText fecha;
        Button reservar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se relacionan los text views con los elementos del layout de la plantilla
            nombre = itemView.findViewById(R.id.nombrehotel);
            ubicacion = itemView.findViewById(R.id.ubicacionh);
            maximo = itemView.findViewById(R.id.maximoh);
            correo = itemView.findViewById(R.id.correrh);
            wifi = itemView.findViewById(R.id.wifi);
            piscina = itemView.findViewById(R.id.piscna);
            desayuno = itemView.findViewById(R.id.desayuno);
            fecha = itemView.findViewById(R.id.fechah);
            precio = itemView.findViewById(R.id.precio);
            reservar = itemView.findViewById(R.id.reservarh);
        }
    }
}
