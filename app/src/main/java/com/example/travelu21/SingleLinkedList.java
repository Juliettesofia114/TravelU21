package com.example.travelu21;

import java.io.Serializable;

public class SingleLinkedList <T> implements Serializable {
    private NodoSimple head;
    private NodoSimple tail;

    static class NodoSimple implements Serializable {
        Object data;
        NodoSimple next;
        String id;

        NodoSimple() {
            next = null;
        }

        NodoSimple(Object data, String id) {
            next = null;
            this.data = data;
            this.id = id;
        }
    }
    SingleLinkedList() { //Constructor
        head = new NodoSimple();
        tail = new NodoSimple();
    }

    void pushFront(T key, String id) { //Añadir al inicio de la lista
        NodoSimple nodo = new NodoSimple(key, id);
        nodo.next = head.next;
        head.next = nodo;
        if (tail.next == null) {
            tail.next = nodo;
        }
    }

    Object topFront() { //Ver el primer objeto de la lista

        if (head.next == null) {
            return null;
        } else {
            return head.next.data;
        }
    }

    void popFront() { //Eliminar del inicio de la lista
        if (head.next == null) {
            System.out.println("Lista vacía");
        } else {
            head.next = head.next.next;
        }
        if (head.next == null) {
            tail.next = null;
        }
    }

    void pushBack(Object key, String id) { //Añadir al final
        NodoSimple nodo = new NodoSimple(key, id);
        if (tail.next == null) {
            head.next = nodo;
            tail.next = nodo;
        } else {
            tail.next.next = nodo;
            tail.next = nodo;
        }
    }

    Object topBack() { //Consultar el último objeto de la lista
        if (tail.next == null) {
            return null;
        } else {
            return tail.next.data;
        }
    }

    void popBack() { //Eliminar al final
        if (head.next == null) {
            System.out.println("Lista vacía");
        } else if (head.next == tail.next) {
            head.next = null;
            tail.next = null;
        } else {
            NodoSimple p = head;
            while (p.next.next != null) {
                p = p.next;
            }
            p.next = null;
            tail.next = p;
        }
    }
    Object Find(String id) {
        NodoSimple p = head;
        while (p.next != null) {
            p = p.next;
            if (p.id.equals(id)) {
                return p;
            }
        }
        return false;
    }
    void erase(Object key) {
        NodoSimple p = head;
        while (p.next != null) {
            if (p.next.data == key) {
                if (p == head) {
                    popFront();
                } else if (tail.next == p.next) {
                    popBack();
                } else {
                    p.next = p.next.next;
                }

            } else {
                p = p.next;
            }
        }
    }

    boolean isEmpty() {
        return head.next == null;
    }

    void empty() {
        head.next = null;
        tail.next = null;
    }

    void addAfter(NodoSimple nodo, Object key, String id) {
        NodoSimple nodo2 = new NodoSimple(key, id);
        nodo2.next = nodo.next;
        nodo.next = nodo2;
        if (tail.next == nodo) {
            tail.next = nodo2;
        }
    }
}
