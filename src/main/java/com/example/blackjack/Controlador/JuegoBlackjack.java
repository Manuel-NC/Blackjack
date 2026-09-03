package com.example.blackjack.Controlador;

import com.example.blackjack.Modelo.Jugador;
import com.example.blackjack.Modelo.Mazo;

import java.util.ArrayList;

public class JuegoBlackjack {
    private Mazo mazo;
    private ArrayList<Jugador> jugadores;
    private Jugador dealer;
    private int indiceJugadorActual;

    // Ahora recibe por parámetro la cantidad elegida (de 1 a 4)
    public JuegoBlackjack(int numJugadores) {
        // Validar que el número esté en el rango permitido
        if (numJugadores < 1) numJugadores = 1;
        if (numJugadores > 4) numJugadores = 4;

        this.jugadores = new ArrayList<>();
        for (int i = 1; i <= numJugadores; i++) {
            jugadores.add(new Jugador("Jugador " + i));
        }
        this.dealer = new Jugador("Casa (Dealer)");

        iniciarNuevaRonda();
    }

    public void iniciarNuevaRonda() {
        mazo = new Mazo();
        dealer.reiniciarMano();

        for (Jugador j : jugadores) {
            j.reiniciarMano();
        }

        // Repartir 2 cartas a cada jugador configurado y a la casa
        for (int i = 0; i < 2; i++) {
            for (Jugador j : jugadores) {
                j.getMano().agregarCarta(mazo.obtenerUnaCarta());
            }
            dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
        }

        indiceJugadorActual = 0;
    }

    // [El resto de métodos de Juego21 se mantienen exactamente igual que antes]
    public Jugador getJugadorActual() {
        if (indiceJugadorActual < jugadores.size()) {
            return jugadores.get(indiceJugadorActual);
        }
        return null;
    }

    public void pedirCartaJugadorActual() {
        Jugador actual = getJugadorActual();
        if (actual != null && !actual.estaFueraDeJuego()) {
            actual.getMano().agregarCarta(mazo.obtenerUnaCarta());
            if (actual.getMano().sePaso()) {
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
        if (indiceJugadorActual >= jugadores.size()) {
            turnoDealer();
        }
    }

    public void turnoDealer() {
        boolean hayJugadoresVivos = false;
        for (Jugador j : jugadores) {
            if (!j.getMano().sePaso()) {
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
        return indiceJugadorActual >= jugadores.size();
    }

    public ArrayList<Jugador> getJugadores() { return jugadores; }
    public Jugador getDealer() { return dealer; }
}
