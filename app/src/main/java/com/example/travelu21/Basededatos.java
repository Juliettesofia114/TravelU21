package com.example.travelu21;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.Serializable;


public class Basededatos <T> implements Serializable {

    //Árboles binarios de búsqueda para guardar los usuarios
    BST<Viajero_class> bviajero;
    BST<Restaurante_class> brestaurante;
    BST<Hotel_class> bhotel;
    BST<Presupuesto_Class> presupuestos;
    PriorityQueue<ReservaC_Class> reservas_viajero;
    PriorityQueue<ReservaS_Class> reservas_servicio;

    Basededatos(){ //Constructor
        this.bviajero = new BST<>();
        this.bhotel = new BST<>();
        this.brestaurante = new BST<>();
        this.presupuestos = new BST<>();
    }

    //Método para registrar viajero
    void  rviajero(Viajero_class v) throws IOException {
        bviajero.insert(v,v.id);
    }

    //Método para registrar hotel
    void rHotel(Hotel_class h){
        bhotel.insert(h, h.id);
    }

    //Método para registrar restaurante
    void rRestaurante(Restaurante_class r){
        brestaurante.insert(r, r.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    Viajero_class findv(String id){
        BST.BinaryNode<Viajero_class> node = bviajero.find(id, bviajero.root);
        Viajero_class user = node.data;
        return user;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Restaurante_class findr(String id){
        BST.BinaryNode<Restaurante_class> node = brestaurante.find(id, brestaurante.root);
        Restaurante_class user = node.data;
        return user;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Hotel_class findh(String id){
        BST.BinaryNode<Hotel_class> node = bhotel.find(id, bhotel.root);
        Hotel_class user = node.data;
        return user;
    }

    public boolean IsInV(String id){
        return bviajero.isIn(id, bviajero.root);
    }
    public boolean IsInH(String id){
        return bhotel.isIn(id, bhotel.root);
    }
    public boolean IsInR(String id){
        return brestaurante.isIn(id,brestaurante.root);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void ActualizarP(String id_user, Presupuesto_Class p){
        presupuestos.insert(p,id_user+presupuestos.height(presupuestos.root));
    }

    public void EnqueueC(ReservaC_Class x, long tiempo, String id){
        reservas_viajero.insert(x,tiempo,id);
    }

    public void EnqueueS(ReservaS_Class x, long tiempo, String id){
        reservas_servicio.insert(x,tiempo,id);
    }
}
