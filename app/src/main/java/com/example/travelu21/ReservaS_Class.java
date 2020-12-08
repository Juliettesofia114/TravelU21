package com.example.travelu21;

public class ReservaS_Class implements Comparable {
    String id_user;
    String id;
    String id_neg;
    String Nombre;
    String correo;
    long fecha;
    String estado;
    ReservaS_Class(String id_user, String id_neg, String nombre, String correo, long fecha){
        this.id_user = id_user;
        this.id_neg = id_neg;
        this.Nombre = nombre;
        this.correo = correo;
        this.fecha = fecha;
        this.estado = "null";
        this.id = id_user+id_neg+fecha;
    }
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
