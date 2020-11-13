package com.example.travelu21;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class Basededatos <T> implements Serializable {

    //Árboles binarios de búsqueda para guardar los usuarios
    BST<Viajero_class> bviajero;
    BST<Restaurante_class> brestaurante;
    BST<Hotel_class> bhotel;
    Basededatos(){ //Constructor
        this.bviajero = new BST();
        this.bhotel = new BST();
        this.brestaurante = new BST();
    }

    //Método para registrar viajero
    void  rviajero(Viajero_class v) throws IOException {
        bviajero.insert(v,v.id);
    }

    //Método para registrar hotel
    public void rHotel(Hotel_class h){
        bhotel.insert(h, h.id);
    }

    //Método para registrar restaurante
    public void rRestaurante(Restaurante_class r){
        brestaurante.insert(r, r.id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Viajero_class findv(String id){
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

}
