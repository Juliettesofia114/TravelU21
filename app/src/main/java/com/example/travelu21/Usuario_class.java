package com.example.travelu21;

public class Usuario_class {
    String nombre ;
    String contrasena ;
    String correo ;
    String descripcion;
    int tipo;
    String id;

    public Usuario_class(String nombre, String contrasena, String correo, int tipo, String descripcion, String id) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.correo = correo;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.id = id;
    }
    //Retorna el ID del usuario
    String getID(){
        return this.id;
    }

}
