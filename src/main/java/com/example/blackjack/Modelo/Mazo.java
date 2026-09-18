package com.example.blackjack.Modelo;

import com.example.blackjack.Estructuras.Pila;
import java.util.ArrayList;
import java.util.Collections;

public class Mazo {
    private Pila<CartaInglesa> cartas;

    public Mazo() {
        llenarYMezclar();
    }

    public Pila<CartaInglesa> getCartas() {
        return cartas;
    }

    public CartaInglesa obtenerUnaCarta() {
        return cartas.pop();
    }

    private void llenarYMezclar() {
        ArrayList<CartaInglesa> listaTemporal = new ArrayList<>();

        for (int i = 2; i <= 14; i++) {
            for (Palo palo : Palo.values()) {
                listaTemporal.add(new CartaInglesa(i, palo, palo.getColor()));
            }
        }

        Collections.shuffle(listaTemporal);

        cartas = new Pila<>(52);
        for (CartaInglesa c : listaTemporal) {
            cartas.push(c);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cartas.getTamano(); i++) {
            sb.append(cartas.get(i));
            if (i < cartas.getTamano() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}