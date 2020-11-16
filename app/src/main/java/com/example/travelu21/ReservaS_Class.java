package com.example.travelu21;

public class ReservaS_Class implements Comparable {
    String id;
    String Nombre;
    String correo;
    long fecha;
    Boolean estado;
    ReservaS_Class(String id, String nombre, String correo, long fecha){
        this.id = id;
        this.Nombre = nombre;
        this.correo = correo;
        this.fecha = fecha;
        this.estado = false;
    }
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
