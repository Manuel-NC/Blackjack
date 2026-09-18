package com.example.blackjack.Controlador;

import com.example.blackjack.Estructuras.Pila;
import com.example.blackjack.Modelo.Jugador;
import com.example.blackjack.Modelo.Mazo;

public class JuegoBlackjack {
    private Mazo mazo;
    private Pila<Jugador> jugadores;
    private Jugador dealer;
    private int indiceJugadorActual;

    public JuegoBlackjack(int numJugadores) {
        if (numJugadores < 1) numJugadores = 1;
        if (numJugadores > 4) numJugadores = 4;

        this.jugadores = new Pila<>(numJugadores);
        for (int i = 1; i <= numJugadores; i++) {
            jugadores.push(new Jugador("Jugador " + i));
        }
        this.dealer = new Jugador("Casa (Dealer)");

        iniciarNuevaRonda();
    }

    public void iniciarNuevaRonda() {
        mazo = new Mazo();
        dealer.reiniciarMano();

        for (int i = 0; i < jugadores.getTamano(); i++) {
            jugadores.get(i).reiniciarMano();
        }

        // Repartir 2 cartas a cada jugador y a la casa
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < jugadores.getTamano(); j++) {
                jugadores.get(j).getMano().agregarCarta(mazo.obtenerUnaCarta());
            }
            dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
        }

        indiceJugadorActual = 0;
        validarJugadorActual21();
    }

    public Jugador getJugadorActual() {
        if (indiceJugadorActual < jugadores.getTamano()) {
            return jugadores.get(indiceJugadorActual);
        }
        return null;
    }

    public void pedirCartaJugadorActual() {
        Jugador actual = getJugadorActual();
        if (actual != null && !actual.estaFueraDeJuego()) {
            actual.getMano().agregarCarta(mazo.obtenerUnaCarta());

            if (actual.getMano().sePaso() || actual.getMano().calcularPuntaje() == 21) {
                actual.plantarse();
                pasarAlSiguienteJugador();
            }
        }
    }

    public void plantarseJugadorActual() {
        Jugador actual = getJugadorActual();
        if (actual != null) {
            actual.plantarse();
            pasarAlSiguienteJugador();
        }
    }

    private void pasarAlSiguienteJugador() {
        indiceJugadorActual++;
        if (indiceJugadorActual >= jugadores.getTamano()) {
            turnoDealer();
        } else {
            validarJugadorActual21();
        }
    }

    public void turnoDealer() {
        boolean hayJugadoresVivos = false;
        for (int i = 0; i < jugadores.getTamano(); i++) {
            if (!jugadores.get(i).getMano().sePaso()) {
                hayJugadoresVivos = true;
                break;
            }
        }

        if (hayJugadoresVivos) {
            while (dealer.getMano().calcularPuntaje() < 17) {
                dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
            }
        }
    }

    public String evaluarResultadoJugador(Jugador j) {
        int ptsJugador = j.getMano().calcularPuntaje();
        int ptsDealer = dealer.getMano().calcularPuntaje();

        if (j.getMano().sePaso()) return "Se pasó de 21 (Perdió)";
        if (dealer.getMano().sePaso()) return "¡Ganó! (La casa se pasó)";
        if (ptsJugador > ptsDealer) return "¡Ganó!";
        if (ptsDealer > ptsJugador) return "Perdió";
        return "Empate";
    }

    public boolean esFinDeRonda() {
        return indiceJugadorActual >= jugadores.getTamano();
    }

    public Pila<Jugador> getJugadores() { return jugadores; }
    public Jugador getDealer() { return dealer; }

    private void validarJugadorActual21() {
        while (!esFinDeRonda()) {
            Jugador actual = getJugadorActual();
            if (actual != null && actual.getMano().calcularPuntaje() == 21) {
                actual.plantarse();
                indiceJugadorActual++;
                if (indiceJugadorActual >= jugadores.getTamano()) {
                    turnoDealer();
                }
            } else {
                break;
            }
        }
    }
}
