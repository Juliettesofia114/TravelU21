package com.example.travelu21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class opreg2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opreg2);
    }
    public void buttonRestaurante(View view){
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String nom = extras.getString("nombre");
        String emp = extras.getString("empresa");
        String corr = extras.getString("correo");
        String ubi = extras.getString("ubicacion");
        String url1 = extras.getString("url");
        String pass = extras.getString("password");
        String des = extras.getString("descrip");
        String neg = extras.getString("negocio");
        String fech = extras.getString("fecha");
        Intent i = new Intent(opreg2.this,restaurante.class);
        i.putExtra("nombre",nom);
        i.putExtra("empresa",emp);
        i.putExtra("correo",corr);
        i.putExtra("password",pass);
        i.putExtra("fecha",fech);
        i.putExtra("descrip",des);
        i.putExtra("negocio",neg);
        i.putExtra("ubicacion",ubi);
        i.putExtra("url",url1);
        startActivity(i);
        finish();
    }
    public void buttonHotel(View view){
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String nom = extras.getString("nombre");
        String emp = extras.getString("empresa");
        String corr = extras.getString("correo");
        String ubi = extras.getString("ubicacion");
        String url1 = extras.getString("url");
        String pass = extras.getString("password");
        String des = extras.getString("descrip");
        String neg = extras.getString("negocio");
        String fech = extras.getString("fecha");
        Intent i = new Intent(opreg2.this,hotel.class);
        i.putExtra("nombre",nom);
        i.putExtra("empresa",emp);
        i.putExtra("correo",corr);
        i.putExtra("password",pass);
        i.putExtra("fecha",fech);
        i.putExtra("descrip",des);
        i.putExtra("negocio",neg);
        i.putExtra("ubicacion",ubi);
        i.putExtra("url",url1);
        startActivity(i);
        finish();
    }
}
