package com.example.travelu21;

public class Presupuesto_Class implements Comparable {
    //Atributos
    String id;
    String fec, des, dir, com, ho, tip, per, fecha;

    Presupuesto_Class(String id, String fec, String des, String dir, String com, String ho, String tip, String per, String fechaactual){ //Constructor
        this.id = id;
        this.fec = fec;
        this.des = des;
        this.dir = dir;
        this.com = com;
        this.ho = ho;
        this.tip = tip;
        this.per = per;
        this.fecha = fechaactual;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
