package com.example.travelu21;

public class ReservaC_Class implements Comparable {
    String id;
    String Nombre;
    String ubicacion;
    String correo;
    long fecha;
    Boolean estado;
    ReservaC_Class(String id, String nombre, String correo, long fecha, String ubicacion){
        this.id = id;
        this.Nombre = nombre;
        this.correo = correo;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.estado = false;
    }
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
