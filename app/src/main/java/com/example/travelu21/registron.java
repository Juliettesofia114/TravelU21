package com.example.travelu21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registron extends AppCompatActivity {
    public EditText nombre;
    private EditText correo;
    private EditText contrasena;
    private EditText descripcion;
    private EditText fecha;
    private EditText negocio;
    private EditText ubicacion;
    private EditText url;
    private EditText empresa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registron);
        nombre = findViewById(R.id.nombren);
        empresa = findViewById(R.id.empresa);
        correo = findViewById(R.id.emailn);
        contrasena = findViewById(R.id.contran);
        descripcion = findViewById(R.id.descripcionn);
        fecha = findViewById(R.id.fechanacimienton);
        negocio = findViewById(R.id.empresa);
        ubicacion = findViewById(R.id.ubicacion);
        url = findViewById(R.id.url);
        Button cont = findViewById(R.id.continuer);
        Button cancelar = findViewById(R.id.cancel);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registron.this, MainActivity.class));
            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datos();
            }
        });
    }
    public void datos(){ //Función que permite enviar los datos del primer layout de registro
        //Se toman los datos puestos por el usuario desde la interfaz gráfica
        final String nom = nombre.getText().toString().trim();
        final String emp = empresa.getText().toString().trim();
        final String corr = correo.getText().toString().trim();
        final String pass = contrasena.getText().toString().trim();
        final String fech = fecha.getText().toString().trim();
        final String des = descripcion.getText().toString().trim();
        final String neg = negocio.getText().toString().trim();
        final String ubi = ubicacion.getText().toString().trim();
        final String url1 = url.getText().toString().trim();
        //Condiciones para que los campos no estén vacíos o no sean correctos
        if (nom.isEmpty()) {
            nombre.setError("Campo obligatorio");
            nombre.requestFocus();
            return;
        }
        if (emp.isEmpty()) {
            empresa.setError("Campo obligatorio");
            empresa.requestFocus();
            return;
        }
        if (corr.isEmpty()) {
            correo.setError("Campo obligatorio");
            correo.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(corr).matches()) {
            correo.setError("Ingrese un correo válido");
            correo.requestFocus();
            return;
        }

        if (pass.isEmpty()) {
            contrasena.setError("Campo obligatorio");
            contrasena.requestFocus();
            return;
        }
        if (des.isEmpty()) {
            fecha.setError("Campo obligatorio");
            fecha.requestFocus();
            return;
        }
        if (neg.isEmpty()) {
            fecha.setError("Campo obligatorio");
            fecha.requestFocus();
            return;
        }
        if (url1.isEmpty()) {
            fecha.setError("Campo obligatorio");
            fecha.requestFocus();
            return;
        }
        if (ubi.isEmpty()) {
            fecha.setError("Campo obligatorio");
            fecha.requestFocus();
            return;
        }
        if (fech.isEmpty()) {
            fecha.setError("Campo obligatorio");
            fecha.requestFocus();
            return;
        }
        //La contraseña debe tener más de 6 caracteres
        if (pass.length() <= 5) {
            contrasena.setError("La contraseña debe ser mínimo 6 caracteres");
            contrasena.requestFocus();
            return;
        }
        //Se crea un intent que llevará consigo los datos recogidos a la siguiente actividad
        Intent i = new Intent(registron.this,opreg2.class);
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
    }
}
