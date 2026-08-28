package com.example.blackjack.Modelo;

import java.util.ArrayList;

public class ManoBlackjack {
    private ArrayList<CartaInglesa> cartas;

    public ManoBlackjack() {
        this.cartas = new ArrayList<>();
    }

    // Agrega una carta
    public void agregarCarta(CartaInglesa carta) {
        if (carta != null) {
            this.cartas.add(carta);
        }
    }

    // Regresa las cartas
    public ArrayList<CartaInglesa> getCartas() {
        return cartas;
    }

    // Calcula los puntos utilizando las reglas de Blackjack (As = 11 o 1, J/Q/K = 10)
    public int calcularPuntaje() {
        int puntaje = 0;
        int cantidadAs = 0;

        for (CartaInglesa c : cartas) {
            int valorOriginal = c.getValor();

            if (valorOriginal == 14) { // Es un As
                cantidadAs++;
                puntaje += 11;
            } else if (valorOriginal >= 11 && valorOriginal <= 13) { // J, Q, K
                puntaje += 10;
            } else { // 2 al 10
                puntaje += valorOriginal;
            }
        }

        // Si el puntaje pasa de 21 y tenemos As, ajustamos los As a 1 punto cada uno
        while (puntaje > 21 && cantidadAs > 0) {
            puntaje -= 10; // Reducimos el valor del As de 11 a 1
            cantidadAs--;
        }

        return puntaje;
    }

    // Verificar si se paso de 21
    public boolean sePaso() {
        return calcularPuntaje() > 21;
    }

    public void limpiar() {
        cartas.clear();
    }

    @Override
    public String toString() {
        return cartas.toString() + " (Puntos: " + calcularPuntaje() + ")";
    }
}
