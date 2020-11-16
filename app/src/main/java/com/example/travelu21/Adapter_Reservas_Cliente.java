package com.example.travelu21;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Reservas_Cliente extends RecyclerView.Adapter<Adapter_Reservas_Cliente.ViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<String[]> data;
    Context mcontext;

    Adapter_Reservas_Cliente(Context context, ArrayList<String[]> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        mcontext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_viajero, parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final String nom = data.get(i)[0];
        final String ubi = data.get(i)[1];
        final String corr = data.get(i)[2];
        final String sta = data.get(i)[3];
        final String fech = data.get(i)[4];

        holder.nombre.setText(nom);
        holder.ubicacion.setText(ubi);
        holder.correo.setText(corr);
        holder.estado.setText("Estado de reserva: "+sta);
        holder.fecha.setText("Fecha de reserva: "+fech);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, ubicacion, correo, estado, fecha;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombrenego);
            ubicacion = itemView.findViewById(R.id.ubicacion);
            correo = itemView.findViewById(R.id.correorec);
            estado = itemView.findViewById(R.id.status);
            fecha = itemView.findViewById(R.id.fechare);
        }
    }
}
