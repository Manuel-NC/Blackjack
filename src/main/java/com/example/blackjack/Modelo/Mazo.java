package com.example.blackjack.Modelo;

import com.example.blackjack.Estructuras.Pila;
import java.util.ArrayList;
import java.util.Collections;

// Representa el mazo de cartas utilizado en el juego
// Administra la pila de cartas inglesas disponibles para repartir
public class Mazo {
    // Pila principal que contiene las cartas del mazo
    private Pila<CartaInglesa> cartas;

    // Constructor que inicializa y prepara el mazo listo para jugar
    public Mazo() {
        llenarYMezclar();
    }

    // Obtiene la pila completa de cartas del mazo
    public Pila<CartaInglesa> getCartas() {
        return cartas;
    }

    // Extrae y regresa la carta superior de la pila del mazo
    public CartaInglesa obtenerUnaCarta() {
        return cartas.pop();
    }

    // Genera las 52 cartas inglesas, las mezcla aleatoriamente y las inserta en la pila
    private void llenarYMezclar() {
        // Se utiliza una lista temporal solo para aprovechar el metodo shuffle
        ArrayList<CartaInglesa> listaTemporal = new ArrayList<>();

        // Genera cartas del valor 2 al 14 (As) para cada uno de los palos
        for (int i = 2; i <= 14; i++) {
            for (Palo palo : Palo.values()) {
                listaTemporal.add(new CartaInglesa(i, palo, palo.getColor()));
            }
        }

        // Mezcla aleatoriamente las cartas de la lista
        Collections.shuffle(listaTemporal);

        // Transfiere las cartas mezcladas a la estructura de Pila con capacidad de 52
        cartas = new Pila<>(52);
        for (CartaInglesa c : listaTemporal) {
            cartas.push(c);
        }
    }

    // Representacion en texto del mazo recorriendo la pila con getElemento
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cartas.getTamano(); i++) {
            sb.append(cartas.getElemento(i));
            if (i < cartas.getTamano() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}