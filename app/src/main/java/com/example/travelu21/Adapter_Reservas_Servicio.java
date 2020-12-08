package com.example.travelu21;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Reservas_Servicio extends RecyclerView.Adapter<Adapter_Reservas_Servicio.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<String[]> data;
    Context mcontext;

    Adapter_Reservas_Servicio(Context context, ArrayList<String[]>data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        mcontext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_servicios, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
         final String nom = data.get(i)[0];
         final String corr = data.get(i)[1];
         final String fecha = data.get(i)[2];
         final String id_user = data.get(i)[3];
         final String fechad = data.get(i)[4];
         final String id_neg = data.get(i)[5];
         holder.nombre.setText(nom);
         holder.correo.setText(corr);
         holder.fecha.setText(fecha);
         holder.aceptar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                holder.aceptar.setEnabled(false);
                holder.rechazar.setEnabled(false);
                Intent i = new Intent(mcontext,exitoreserva.class);
                i.putExtra("id_viajero",id_user);
                i.putExtra("estado","true");
                i.putExtra("fecha",fechad);
                i.putExtra("id_negocio",id_neg);

                //Envía al usuario a la actividad donde se guardan las reservas
                 mcontext.startActivity(i);
             }
         });
         holder.rechazar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 holder.rechazar.setEnabled(false);
                 holder.aceptar.setEnabled(false);
                 Intent i = new Intent(mcontext,exitoreserva.class);
                 i.putExtra("id_viajero",id_user);
                 i.putExtra("estado","false");
                 i.putExtra("fecha",fechad);
                 i.putExtra("id_negocio",id_neg);
                 //Envía al usuario a la actividad donde se guardan las reservas
                 mcontext.startActivity(i);

             }
         });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, correo, fecha;
        Button aceptar, rechazar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreusua);
            correo = itemView.findViewById(R.id.correousu);
            fecha = itemView.findViewById(R.id.fechar);
            aceptar = itemView.findViewById(R.id.aceptar);
            rechazar = itemView.findViewById(R.id.rechazar);
        }
    }

}
