package com.example.travelu21;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_Presupuesto extends RecyclerView.Adapter<Adapter_Presupuesto.ViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<String[]> data;
    Context mcontext;

    Adapter_Presupuesto(Context context, ArrayList<String[]> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        mcontext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_presupuestos, parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        final String nom = data.get(i)[0];
        final String fecha = data.get(i)[1];
        final String uuid = data.get(i)[2];
        final String total = data.get(i)[3];
        final String personas = data.get(i)[4];
        final String destino = data.get(i)[5];
        final String comida = data.get(i)[6];
        final String hotel = data.get(i)[7];

        holder.nombre.setText(nom);
        holder.fecha.setText(fecha);
        holder.total.setText("inversión total: "+total);
        holder.personas.setText("número de personas: "+personas);
        holder.destino.setText("destino: "+destino);
        holder.comida.setText("inversión en comida: "+comida);
        holder.hotel.setText("Inversión en hoteles: "+hotel);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nombre, fecha, total, personas, destino, comida, hotel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombrepres);
            fecha = itemView.findViewById(R.id.fech);
            total = itemView.findViewById(R.id.invertir);
            personas = itemView.findViewById(R.id.nopersona);
            destino = itemView.findViewById(R.id.destino);
            comida = itemView.findViewById(R.id.comida);
            hotel = itemView.findViewById(R.id.hotel);
        }
    }
}
