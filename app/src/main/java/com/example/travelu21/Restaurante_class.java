package com.example.travelu21;

public class Restaurante_class extends Servicio_class implements Comparable{

    String horario;
    int max;
    String tipor;

    public Restaurante_class(String nombre, String nombreEmpresa, String ubicacion, String contrasena, String correo, int tipo, String descripcion, String url, String id, String med, String horario, int max, String tipores) {
        super(nombre, nombreEmpresa, ubicacion, contrasena, correo, tipo, descripcion, url, id, med);
        this.horario = horario;
        this.max = max;
        this.tipor = tipores;
    }
    public int compareTo(Restaurante_class o) {
        int resultado=0;
        if (this.nombreEmpresa.compareTo(o.nombreEmpresa)<0) {   resultado = -1;      }
        else if (this.tipor.compareTo(o.tipor)>0) {    resultado = 1;      }
        return resultado;
    }

}
