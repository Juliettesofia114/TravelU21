package com.example.travelu21;

public class DoubleLinkedList {
    private NodoDoble head;
    private NodoDoble tail;

    static class NodoDoble {
        Object data;
        NodoDoble prev;
        NodoDoble next;

        NodoDoble() {
            next = null;
            prev = null;
        }

        NodoDoble(Object data) {
            this.data = data;
            next = null;
            prev = null;
        }
    }

    DoubleLinkedList() {
        head = new NodoDoble();
        tail = new NodoDoble();
    }

    void pushFront(Object key) {
        NodoDoble nodo = new NodoDoble(key);
        nodo.next = head.next;
        head.next = nodo;
        nodo.next.prev = nodo;
        if (tail.next == null) {
            tail.next = nodo;
        }
    }

    Object topFront() {

        if (head.next == null) {
            return null;
        } else {
            return head.next.data;
        }
    }

    void popFront() {
        if (head.next == null) {
            System.out.println("Lista vacía");
        } else {
            head.next = head.next.next;
        }
        if (head.next == null) {
            tail.next = null;
        } else {
            head.next.prev = null;
        }
    }

    void pushBack(Object key) {
        NodoDoble nodo = new NodoDoble(key);
        if (tail.next == null) {
            head.next = nodo;
            tail.next = nodo;
            nodo.prev = null;
        } else {
            tail.next.next = nodo;
            nodo.prev = tail.next;
            tail.next = nodo;
        }
    }

    Object topBack() {
        if (tail.next == null) {
            return null;
        } else {
            return tail.next.data;
        }
    }

    void popBack() {
        if (head.next == null) {
            System.out.println("Lista vacía");
        } else if (head.next == tail.next) {
            head.next = null;
            tail.next = null;
        } else {
            tail.next = tail.next.prev;
            tail.next.next = null;
        }
    }

    boolean findKey(Object key) {
        NodoDoble p = head;
        while (p.next != null) {
            p = p.next;
            if (p.data == key) {
                return true;
            }
        }
        return false;
    }

    boolean isEmpty() {
        if (head== null){
            return false;
        } else {
            return true;
        }
    }

    void empty() {
        head.next = null;
        tail.next = null;
    }

    void addAfter(NodoDoble nodo, Object key) {
        NodoDoble nodo2 = new NodoDoble(key);
        nodo2.next = nodo.next;
        nodo2.prev = nodo;
        nodo.next = nodo2;
        if (nodo2.next != null) {
            nodo2.next.prev = nodo2;
        } else if (tail.next == nodo) {
            tail.next = nodo2;
        }
    }

    void addBefore(NodoDoble nodo, Object key) {
        NodoDoble nodo2 = new NodoDoble(key);
        nodo2.next = nodo;
        nodo2.prev = nodo.prev;
        nodo.prev = nodo2;
        if (nodo2.prev != null) {
            nodo2.prev.next = nodo2;
        } else if (head.next == nodo) {
            head.next = nodo2;
        }
    }
}
