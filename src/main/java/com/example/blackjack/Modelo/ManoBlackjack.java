package com.example.blackjack.Modelo;

import com.example.blackjack.Estructuras.Pila;

public class ManoBlackjack {
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
            CartaInglesa c = cartas.get(i);
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
            sb.append(cartas.get(i));
            if (i < cartas.getTamano() - 1) sb.append(", ");
        }
        sb.append("] (Puntos: ").append(calcularPuntaje()).append(")");
        return sb.toString();
    }
}