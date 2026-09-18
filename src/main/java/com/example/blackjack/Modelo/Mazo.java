package com.example.blackjack.Modelo;

import java.util.ArrayList;
import java.util.Collections;

// Administra el conjunto de cartas disponibles utilizando un ArrayList
public class Mazo {
    private ArrayList<CartaInglesa> cartas;

    // Constructor que inicializa y mezcla las cartas
    public Mazo() {
        llenarYMezclar();
    }

    // Regresa la lista completa de cartas
    public ArrayList<CartaInglesa> getCartas() {
        return cartas;
    }

    // Remueve y regresa la carta superior de la lista
    public CartaInglesa obtenerUnaCarta() {
        if (!cartas.isEmpty()) {
            return cartas.remove(cartas.size() - 1);
        }
        return null;
    }

    // Genera las 52 cartas y las mezcla en el ArrayList
    private void llenarYMezclar() {
        cartas = new ArrayList<>();

        for (int i = 2; i <= 14; i++) {
            for (Palo palo : Palo.values()) {
                cartas.add(new CartaInglesa(i, palo, palo.getColor()));
            }
        }

        Collections.shuffle(cartas);
    }

    @Override
    public String toString() {
        return cartas.toString();
    }
}