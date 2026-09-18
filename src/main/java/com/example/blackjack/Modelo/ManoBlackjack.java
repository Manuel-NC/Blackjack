package com.example.blackjack.Modelo;

import com.example.blackjack.Estructuras.Pila;

// Representa la mano de cartas que posee un jugador o el dealer
public class ManoBlackjack {
    // Pila que almacena las cartas de la mano
    private Pila<CartaInglesa> cartas;

    // Constructor que inicializa la pila de cartas con una capacidad maxima de 12
    public ManoBlackjack() {
        this.cartas = new Pila<>(12);
    }

    // Agrega una carta a la pila de la mano si no es nula
    public void agregarCarta(CartaInglesa carta) {
        if (carta != null) {
            this.cartas.push(carta);
        }
    }

    // Regresa la pila de cartas de la mano
    public Pila<CartaInglesa> getCartas() {
        return cartas;
    }

    // Calcula el puntaje total siguiendo las reglas del Blackjack
    // As vale 11 o 1, mientras que J, Q y K valen 10
    public int calcularPuntaje() {
        int puntaje = 0;
        int cantidadAs = 0;

        // Recorre la pila sin modificarla usando getElemento
        for (int i = 0; i < cartas.getTamano(); i++) {
            CartaInglesa c = cartas.getElemento(i);
            int valorOriginal = c.getValor();

            if (valorOriginal == 14) { // Es un As
                cantidadAs++;
                puntaje += 11;
            } else if (valorOriginal >= 11 && valorOriginal <= 13) { // J, Q, K
                puntaje += 10;
            } else { // Cartas del 2 al 10
                puntaje += valorOriginal;
            }
        }

        // Si el puntaje excede 21 y hay Ases, ajusta el valor del As de 11 a 1
        while (puntaje > 21 && cantidadAs > 0) {
            puntaje -= 10;
            cantidadAs--;
        }

        return puntaje;
    }

    // Indica si la mano supera los 21 puntos
    public boolean sePaso() {
        return calcularPuntaje() > 21;
    }

    // Reinicia la mano creando una pila nueva vacia
    public void limpiar() {
        this.cartas = new Pila<>(12);
    }

    // Representacion en cadena de texto de la mano y su puntaje actual
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