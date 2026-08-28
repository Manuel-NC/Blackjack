package com.example.blackjack.Controlador;

import com.example.blackjack.Modelo.ManoBlackjack;
import com.example.blackjack.Modelo.Mazo;

public class JuegoBlackjack {
    private Mazo mazo;
    private ManoBlackjack jugador;
    private ManoBlackjack dealer;

    public JuegoBlackjack() {
        jugador = new ManoBlackjack();
        dealer = new ManoBlackjack();
        iniciarNuevaRonda();
    }

    public void iniciarNuevaRonda() {
        mazo = new Mazo(); // Mazo nuevo y mezclado
        jugador.limpiar();
        dealer.limpiar();

        // Repartir 2 cartas iniciales a cada uno (visibles)
        jugador.agregarCarta(mazo.obtenerUnaCarta());
        dealer.agregarCarta(mazo.obtenerUnaCarta());
        jugador.agregarCarta(mazo.obtenerUnaCarta());
        dealer.agregarCarta(mazo.obtenerUnaCarta());
    }

    public void pedirCartaJugador() {
        if (!jugador.sePaso()) {
            jugador.agregarCarta(mazo.obtenerUnaCarta());
        }
    }

    // El dealer pide cartas automaticamente mientras su mano sume menos de 17
    public void turnoDealer() {
        while (dealer.calcularPuntaje() < 17) {
            dealer.agregarCarta(mazo.obtenerUnaCarta());
        }
    }

    // Retorna el resultado final de la ronda
    public String obtenerResultado() {
        int ptsJugador = jugador.calcularPuntaje();
        int ptsDealer = dealer.calcularPuntaje();

        if (jugador.sePaso()) {
            return "Te has pasado de 21! La casa gana.";
        } else if (dealer.sePaso()) {
            return "La casa se paso de 21! Ganaste!";
        } else if (ptsJugador > ptsDealer) {
            return "Ganaste la partida!";
        } else if (ptsDealer > ptsJugador) {
            return "Gana la casa.";
        } else {
            return "Empate!";
        }
    }

    public ManoBlackjack getJugador() {
        return jugador;
    }

    public ManoBlackjack getDealer() {
        return dealer;
    }
}
