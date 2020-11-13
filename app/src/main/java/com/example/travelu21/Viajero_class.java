package com.example.travelu21;

import java.io.IOException;
import java.util.ArrayList;

public class Viajero_class extends Usuario_class implements Comparable {
    SingleLinkedList presupuestos;
    SingleLinkedList reserva ;
    SingleLinkedList favoritos;

    public Viajero_class(String nombre, String contrasena, String correo, String descripcion, int tipo, String id){ //Contructor
        super(nombre,contrasena,correo,tipo, descripcion, id);
        this.reserva=new SingleLinkedList();
        this.favoritos=new SingleLinkedList();

    }

    //Método para reservar
    public void Reservar(Servicio_class s){
        Basededatos base = new Basededatos();
        reserva.pushBack(s, s.id);
        s.clientes.enqueue(this);
    }

    //Método para crear nuevo presupuesto
    public void Presupuesto(String[] datos){
        presupuestos.pushFront(datos, id);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
