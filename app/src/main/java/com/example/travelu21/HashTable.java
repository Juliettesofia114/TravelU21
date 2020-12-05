package com.example.travelu21;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class HashTable {
    ArrayList<HashNode> table;
    int p; //valor usado para hashear strings
    int x; //valor usado para hashear strings 1<=x<=p-1
    int m;  //m es el tamaño de la tabla(cardinalidad de la función hash)
    int a; //valor usado para hashear int
    int b; //valor usado para hashear int
    int numOfUsers;

    static class HashNode {
        String user;
        String password;
        HashNode next;

        HashNode(String user, String password) {
            this.user = user;
            this.password = password;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    HashTable(int m, int p) { //debe ser un primo mayor a m
        this.m=m;
        this.p=p;
        setM(p);
        setX(p);
        setA(p);
        setB(p);
        table = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            table.add(null);
        }

    }

    boolean isIn(String user, String password) {
        HashNode head = table.get(polyHashFix(user));
        while (head != null) {
            if (head.user.equals(user) && head.password.equals(password)) {
                return true;
            } else {
                head = head.next;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void add(String user, String password) {
        HashNode usuarioNuevo = new HashNode(user, password);
        int pos = polyHashFix(user);
        HashNode head = table.get(pos);
        if (head != null) {
            while (head.next != null) {
                if (head.user.equals(user)) {
                    System.out.println("Usuario ya registrado");
                    break;
                } else {
                    head = head.next;
                }
            }
            if (head.user.equals(user)) {
                System.out.println("Usuario ya registrado");
            } else {
                head.next = usuarioNuevo;
                numOfUsers++;
            }
        } else {
            table.set(pos, usuarioNuevo);
            numOfUsers++;
        }
        rehash();
    }
    void print(int n){
        if(n>= table.size()){
            System.out.println("Indice fuera de la tabla");
        }else{
            HashNode head=table.get(n);
            if(head==null){
                System.out.println("Posición vacía");
            }else if(head.next==null){
                System.out.println(head.user+" "+head.password);
            }else{
                while(head.next!=null){
                    System.out.println(head.user+" "+head.password);
                    head=head.next;
                }
                System.out.println(head.user+" "+head.password);
            }
        }
    }

    void remove(String user,String password){
        if(!isIn(user,password)) {
            return;
        }else{
            HashNode head=table.get(polyHashFix(user));
            while(head.next!=null){
                if(head.user.equals(user) && head.password.equals(password)){
                    table.set(polyHash(user),head.next);
                }
                if(head.next.user.equals(user) && head.next.password.equals(password)){
                    head.next=head.next.next;
                    break;
                }else {
                    head = head.next;
                }
            }
        }
        numOfUsers--;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void rehash(){
        if(loadFactor()>0.9){
            ArrayList<HashNode> tmp=table;
            table=new ArrayList<>(2*m);
            setM(2*m);
            setP(find_next_prime(m));
            setB(p);
            setA(p);
            setX(p);
            for(int i=0;i<m;i++){
                table.add(null);
            }
            numOfUsers=0;
            for(HashNode headNode:tmp){
                while(headNode!=null){
                    add(headNode.user, headNode.password);
                    headNode=headNode.next;
                }
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void setX(int p){
        x=ThreadLocalRandom.current().nextInt(1,p);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void setA(int p){
        a=ThreadLocalRandom.current().nextInt(1,p);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void setB(int p){
        b=ThreadLocalRandom.current().nextInt(0,p);
    }
    void setP(int p){
        this.p=p;
    }
    void setM(int m){
        this.m=m;
    }
    int find_next_prime(int n) {
        boolean isPrime = false;

        int start = 2;

        while (!isPrime) {
            n += 1;
            int m = (int) Math.ceil(Math.sqrt(n));

            isPrime = true;
            for (int i = start; i <= m; i++) {
                if (n % i == 0) {
                    isPrime = false;
                    break;
                }
            }
        }
        return n;
    }

    int polyHashFix(String username){
        return intHash(a,b,p,m-1,polyHash(username));
    }
    int polyHash(String username) { //x tiene que ser menor a p
        int hash = 0;
        for (int i = 0; i < username.length(); i++) {
            hash = (hash * x + username.charAt(i)) % p;
        }
        return hash;
    }
    int intHash(int a,int b,int p,int m,int x){
        return ((a*x+b)%p)%m;
    }

    float loadFactor(){
        return (float) numOfUsers/ table.size();
    }
}
