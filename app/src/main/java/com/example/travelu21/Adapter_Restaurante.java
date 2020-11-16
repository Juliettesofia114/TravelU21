package com.example.travelu21;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_Restaurante extends RecyclerView.Adapter<Adapter_Restaurante.ViewHolder> {

    //Se instancia un Inflater el cual permite adaptar la plantilla al layout principal, un array que contiene los datos y el contexto de la clase de búsqueda de restaurantes
    private LayoutInflater layoutInflater;
    private ArrayList<String[]> data;
    Context mcontext;
    Basededatos basededatos;
    ReservaC_Class reservaC;
    ReservaS_Class reservaS;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Adapter_Restaurante(Context context, ArrayList<String[]> data, Basededatos basededatos){ //Constructor
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        mcontext = context;
        this.basededatos = basededatos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Se genera el Inflater el cual se relaciona con su respectiva plantilla
        View view = layoutInflater.inflate(R.layout.card_busqueda_r, parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        //Se toman los datos del arreglo en String
        final String nom = data.get(i)[0];
        final String ubi = data.get(i)[1];
        String max = data.get(i)[2];
        final String corr = data.get(i)[3];
        String hor = data.get(i)[4];
        final String uuid = data.get(i)[5];
        final String tip = data.get(i)[6];
        final String uid = user.getUid();


        //Se llenan los datos de la plantilla con los datos de la base de datos
        holder.nombre.setText(nom);
        holder.ubicacion.setText(ubi);
        holder.correo.setText(corr);
        holder.maximo.setText("máximo de personas: "+max);
        holder.horario.setText("horario: "+hor);
        holder.tipo.setText("Tipo: "+tip);

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
                i.putExtra("nombre",nom);
                i.putExtra("ubicacion",ubi);
                i.putExtra("correo", corr);
                i.putExtra("fecha",fech);
                i.putExtra("uid",uuid);

                //Envía al usuario a la actividad donde se guardan las reservas
                mcontext.startActivity(i);
                //Toast.makeText(mcontext, fech,
                        //Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Se instancian los text views
        TextView nombre;
        TextView ubicacion;
        TextView maximo;
        TextView correo;
        TextView horario;
        TextView tipo;
        EditText fecha;
        Button reservar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Se relacionan los text views con los elementos del layout de la plantilla
            nombre = itemView.findViewById(R.id.nombreres);
            ubicacion = itemView.findViewById(R.id.ubicacion);
            maximo = itemView.findViewById(R.id.maximo);
            correo = itemView.findViewById(R.id.correror);
            fecha = itemView.findViewById(R.id.fecha);
            reservar = itemView.findViewById(R.id.reservares);
            horario = itemView.findViewById(R.id.horario);
            tipo = itemView.findViewById(R.id.tipo);
        }
    }


}
