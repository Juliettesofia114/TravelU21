package com.example.travelu21;

import java.io.Serializable;

public class Queue<T> {
    private NodoSimple<T> head;
    private NodoSimple<T> tail;

    static class NodoSimple<T> {
        T data;
        NodoSimple<T> next;

        NodoSimple() {
            next = null;
        }

        NodoSimple(T data) {
            next = null;
            this.data = data;
        }

    }

    Queue() {
        head = new NodoSimple();
        tail = new NodoSimple();
    }

    void enqueue(T key) {
        NodoSimple nodo = new NodoSimple(key);
        if (head.next == null) {
            head.next = nodo;
            tail.next = nodo;
        } else {
            tail.next.next = nodo;
            tail.next = nodo;
        }
    }

    T dequeue() {
        T key;
        if (head.next == null) {
            return null;
        } else {
            key = head.next.data;
            head.next = head.next.next;
        }
        if (head.next == null) {
            tail.next = null;
        }
        return key;
    }

    boolean isEmpty() {
        return head.next == null;
    }
}