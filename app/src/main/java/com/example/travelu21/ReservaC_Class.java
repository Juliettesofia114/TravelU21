package com.example.travelu21;

public class ReservaC_Class implements Comparable {
    String id_user;
    String id;
    String id_neg;
    String Nombre;
    String ubicacion;
    String correo;
    long fecha;
    String estado;
    ReservaC_Class(String id_user, String id_neg, String nombre, String correo, long fecha, String ubicacion){
        this.id_user = id_user;
        this.id_neg = id_neg;
        this.Nombre = nombre;
        this.correo = correo;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.estado = "null";
        this.id = id_user+id_neg+fecha;
    }
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
