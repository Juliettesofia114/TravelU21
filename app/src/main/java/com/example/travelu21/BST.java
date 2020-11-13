package com.example.travelu21;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class BST<T extends Comparable> {
    BinaryNode<T> root;

    static class BinaryNode<T> {
        T data;
        String id;
        BinaryNode<T> left;
        BinaryNode<T> right;
        BinaryNode<T> parent;

        BinaryNode(T x, String id) {
            data = x;
            this.id = id;
        }
    }

    BST() {
        root = null;
    }

    void makeEmpty() {
        root = null;
    }

    boolean isEmpty() {
        return (root == null);
    }

    public void insert(T x, String id){
        root=insert(x,root,null,id);
    }
    private BinaryNode<T> insert(T x, BinaryNode<T> t, BinaryNode<T> P, String id) {
        if (t == null){
            BinaryNode nuevo = new BinaryNode<>(x,id);
            nuevo.parent = P;
            return nuevo;
        }
        if (t.id.compareTo(id)>0) {
            t.right = insert(x, t.right,t,id);
        } else if (t.id.compareTo(id) < 0) {
            t.left = insert(x, t.left,t,id);
        } else {
        }
        return t;
    }

    boolean isIn(String x, BinaryNode<T> t) {
        if (t == null) {
            return false;
        }
        if (t.id.compareTo(x)==0) {
            return true;
        }
        if (t.id.compareTo(x) > 0) {
            return isIn(x, t.right);
        } else if (t.id.compareTo(x) < 0) {
            return isIn(x, t.left);
        } else
            return true;
    }

    BinaryNode<T> findMin(BinaryNode<T> t) {
        BinaryNode<T> tmp = t;
        while (tmp.left != null) {
            tmp = tmp.left;
        }
        return tmp;
    }
    BinaryNode<T> findMax(BinaryNode<T> t){
        BinaryNode<T> tmp = t;
        while (tmp.right!= null) {
            tmp = tmp.right;
        }
        return tmp;
    }
    public void remove(T x){
        root=remove(x,root);
    }
    private BinaryNode<T> remove(T x, BinaryNode<T> t) {
        if (t == null)
            return t;
        if (x.compareTo(t.data) < 0) {
            t.left = remove(x, t.left);
        } else if (x.compareTo(t.data) > 0) {
            t.right = remove(x, t.right);
        } else if (t.left != null && t.right != null) {
            t.data = findMin(t.right).data;
            t.right = remove(t.data, t.right);
        } else
            t = (t.left != null) ? t.left : t.right;
        return t;
    }
    int height(BinaryNode<T> t){
        return t==null? -1:1+Math.max(height(t.left),height(t.right));
    }

    void preOrder(BinaryNode<T> t) {
        if (t != null) {
            System.out.print(t.data + " ");
            preOrder(t.left);
            preOrder(t.right);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    BinaryNode<T> find(String id, BinaryNode<T> t){
        if(Objects.equals(t.id, id)){
            return t;
        }else if(t.id.compareTo(id)>0){
            return find(id,t.right);
        }else if(t.id.compareTo(id)<0){
            if(t.left!=null){
                return find(id,t.left);
            }return  t;
        }
        return t;
    }
    void levelOrder(BinaryNode<T> t) {
        Queue<BinaryNode<T>> c = new Queue<>();
        BinaryNode<T> aux;

        c.enqueue(t);
        while (!c.isEmpty()) {
            aux = c.dequeue();
            System.out.print(aux.data + " ");
            if (aux.left != null) {
                c.enqueue(aux.left);
            }
            if (aux.right != null) {
                c.enqueue(aux.right);
            }
        }
    }
}
