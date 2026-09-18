package com.example.blackjack.Estructuras;

public class Pila<T> {

    private Object[] pila;
    private int tope;

    public Pila() {
        pila = new Object[10];
        tope = -1;
    }

    public Pila(int capacidad) {
        pila = new Object[capacidad];
        tope = -1;
    }

    public boolean push(T dato) {
        boolean seHizoPush = false;

        if (pila.length - 1 == tope) {
            System.out.println("Desbordamiento");
        } else {
            tope++;
            pila[tope] = dato;
            seHizoPush = true;
        }
        return seHizoPush;
    }

    public T pop() {
        if (tope == -1) {
            System.out.println("Pila vacia");
            return null;
        } else {
            T dato = (T) pila[tope];
            pila[tope] = null;
            tope--;
            return dato;
        }
    }

    public T peek() {
        if (tope == -1) return null;
        return (T) pila[tope];
    }

    public boolean vacia() {
        return (tope == -1);
    }

    public boolean llena() {
        return tope >= pila.length - 1;
    }

    public int getTamano() {
        return tope + 1;
    }

    public T get(int indice) {
        if (indice < 0 || indice > tope) return null;
        return (T) pila[indice];
    }
}