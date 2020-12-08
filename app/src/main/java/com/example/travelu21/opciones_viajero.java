package com.example.travelu21;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class opciones_viajero extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_viajero);

        //Se pide la instancia de usuario en Firebase
        mAuth = FirebaseAuth.getInstance();

        //Se instancian los objetos relacionádolos con la interfaz gráfica
        Button cerrar = findViewById(R.id.cerrar);
        Button datos = findViewById(R.id.verdato);

        //Métodos que le dan funcionalidad a los botones
        datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), vercuenta.class));
            }
        });
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Método para cerrar sesión
                mAuth.signOut();
                //Pasa al layout de inicio de sesión y no permite regresar
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

}
