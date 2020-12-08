package com.example.travelu21;

public class Servicio_class extends Usuario_class implements Comparable {
    String url ;
    String ubicacion ;
    String nombreEmpresa;
    Queue reseñas;
    String medio;
    //Constructor que hereda los atributos de la clase usuario
    public Servicio_class(String nombre, String nombreEmpresa, String ubicacion, String contrasena, String correo, int tipo, String descripcion, String url, String id, String medio) {
        super(nombre, contrasena, correo, tipo, descripcion, id);
        this.nombreEmpresa = nombreEmpresa;
        this.url = url;
        this.ubicacion = ubicacion;
        this.medio = medio;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
