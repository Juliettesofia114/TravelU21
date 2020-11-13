package com.example.travelu21;

public class Stack {
    private NodoSimple head;

    static class NodoSimple {
        Object data;
        NodoSimple next;

        NodoSimple(){
            next=null;
        }
        NodoSimple(Object data) {
            next = null;
            this.data = data;
        }
    }

    Stack() {
        head = new NodoSimple();
    }

    void push(Object key) {
        NodoSimple nodo = new NodoSimple(key);
        nodo.next = head.next;
        head.next = nodo;
    }

    Object top() {
        if (head.next == null) {
            return null;
        } else {
            return head.next.data;
        }
    }

    Object pop() {
        Object data = null;
        if (head.next == null) {
            System.out.println("Lista vacía");
        } else {
            data = head.next.data;
            head.next = head.next.next;
        }
        return data;
    }

    Boolean isEmpty() {
        if (head== null){
            return false;
        } else {
            return true;
        }
    }
}
