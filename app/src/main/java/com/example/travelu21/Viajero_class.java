package com.example.travelu21;

import java.io.IOException;
import java.util.ArrayList;

public class Viajero_class extends Usuario_class implements Comparable {
    SingleLinkedList favoritos;

    public Viajero_class(String nombre, String contrasena, String correo, String descripcion, int tipo, String id){ //Contructor
        super(nombre,contrasena,correo,tipo, descripcion, id);
        this.favoritos = new SingleLinkedList();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
