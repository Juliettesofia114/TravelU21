package com.example.travelu21;

import java.util.Arrays;

public class PriorityQueue<T> {
    int currentSize;
    int capacity;
    Node[] arr;

    static class Node<T> {
        T data;
        int priority;

        Node(T data, int priority) {
            this.data = data;
            this.priority = priority;
        }

        Node() {
            data = null;
            priority = 0;
        }
    }

    PriorityQueue(int capacity) {
        this.capacity = capacity + 1;
        arr = new Node[capacity];
    }

    void insert(T x, int priority) { //insertar un Nodo nuevo
        if (currentSize == arr.length - 1) {
            arr = Arrays.copyOf(arr, capacity * 2 + 1);
            capacity = arr.length;
        }
        Node<T> t = new Node<>(x, priority);
        int hole = ++currentSize;
        arr[hole]=t;
        siftUp(hole);

    }


    boolean isEmpty(){
        return arr[1]==null;
    }
    public T findMax() throws Exception {
        return findMaxNode().data;
    }
    private Node<T> findMaxNode() throws Exception {
        if(isEmpty()){
            throw new Exception("Cola vacía");
        }else {
            return arr[1];
        }
    }
    Node<T> deleteMax() throws Exception {
        if(isEmpty()){
            throw new Exception("Cola vacía");
        }else {
            Node<T> t=findMaxNode();
            arr[1]=arr[currentSize--];
            siftDown(1);
            return t;
        }
    }


    public void delete(int index) throws Exception {
        arr[index].priority=Integer.MAX_VALUE;
        siftUp(index);
        deleteMax();

    }

    private void siftDown(int hole){
        int child;
        Node tmp=arr[hole];
        for(;hole*2<=currentSize;hole=child){
            child=hole*2;
            if(child!=currentSize && arr[child+1].priority>arr[child].priority){
                child++;
            }
            if(arr[child].priority>tmp.priority){
                arr[hole]=arr[child];
            }else{
                break;
            }
        }
        arr[hole]=tmp;
    }
    void siftUp(int hole){
        Node t=arr[hole];
        while(hole!=1 && t.priority>arr[parent(hole)].priority) {//lo debemos subir
            arr[hole]=arr[parent(hole)]; //bajamos el padre
            hole=parent(hole); //pasamos al padre
        }
        arr[hole] = t;
    }
    int parent(int index) {
        return index / 2;
    }
    int leftChild(int index) {
        return (index - 1) * 2;
    }
    int rightChild(int index){
        return 2*index+1;
    }

    void print() {
        for (int i = 1; i <= currentSize; i++) {
            System.out.print("["+arr[i].data+","+ arr[i].priority+"," +i+ "]" + " ");
        }
    }


}
