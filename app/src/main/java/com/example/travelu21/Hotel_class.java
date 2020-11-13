package com.example.travelu21;

public class Hotel_class extends Servicio_class{
    int max;
    String wifi;
    String desayuno;
    String piscina;
    int precio;

    public Hotel_class(String nombre, String nombreEmpresa, String ubicacion, String contrasena, String correo, int tipo, String descripcion, String url, String id, int precio, int max, String wifi, String desayuno, String piscina) {
        super(nombre, nombreEmpresa, ubicacion, contrasena, correo, tipo, descripcion, url, id);
        this.max = max;
        this.wifi = wifi;
        this.desayuno = desayuno;
        this.piscina = piscina;
        this.precio = precio;
    }
}
