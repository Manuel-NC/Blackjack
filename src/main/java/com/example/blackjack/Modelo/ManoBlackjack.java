package com.example.blackjack.Modelo;

import com.example.blackjack.Estructuras.Pila;

// Representa la mano de cartas del jugador manteniendo la Pila para el Undo
public class ManoBlackjack {
    // Pila  para permitir el deshacer (pop) de la ultima carta recibida
    private Pila<CartaInglesa> cartas;

    public ManoBlackjack() {
        this.cartas = new Pila<>(12);
    }

    public void agregarCarta(CartaInglesa carta) {
        if (carta != null) {
            this.cartas.push(carta);
        }
    }

    public Pila<CartaInglesa> getCartas() {
        return cartas;
    }

    public int calcularPuntaje() {
        int puntaje = 0;
        int cantidadAs = 0;

        for (int i = 0; i < cartas.getTamano(); i++) {
            CartaInglesa c = cartas.getElemento(i);
            int valorOriginal = c.getValor();

            if (valorOriginal == 14) {
                cantidadAs++;
                puntaje += 11;
            } else if (valorOriginal >= 11 && valorOriginal <= 13) {
                puntaje += 10;
            } else {
                puntaje += valorOriginal;
            }
        }

        while (puntaje > 21 && cantidadAs > 0) {
            puntaje -= 10;
            cantidadAs--;
        }

        return puntaje;
    }

    public boolean sePaso() {
        return calcularPuntaje() > 21;
    }

    public void limpiar() {
        this.cartas = new Pila<>(12);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cartas.getTamano(); i++) {
            sb.append(cartas.getElemento(i));
            if (i < cartas.getTamano() - 1) sb.append(", ");
        }
        sb.append("] (Puntos: ").append(calcularPuntaje()).append(")");
        return sb.toString();
    }
}