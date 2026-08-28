package com.example.blackjack.Controlador;

import com.example.blackjack.Modelo.Jugador;
import com.example.blackjack.Modelo.ManoBlackjack;
import com.example.blackjack.Modelo.Mazo;

import java.util.ArrayList;

public class JuegoBlackjack {
    private Mazo mazo;
    private ArrayList<Jugador> jugadores;
    private Jugador dealer;
    private int jugadorActual = 0;

    public JuegoBlackjack() {
        this.jugadores = new ArrayList<>();
        for(int i = 0 ; i<=4 ; i++) {
            jugadores.add(new Jugador("Jugador" + i));
        }

        this.dealer = new Jugador("Dealer");
        iniciarNuevaRonda();
    }

    public void iniciarNuevaRonda() {
        mazo = new Mazo(); // Mazo nuevo y mezclado

        dealer.reiniciarMano();

        for(Jugador j : jugadores){
            j.reiniciarMano();

        }

        // Repartir 2 cartas iniciales a cada uno (visibles)

        for(Jugador j : jugadores){
            for(int i = 0 ; i<2 ; i++){
                j.getMano().agregarCarta(mazo.obtenerUnaCarta());
            }
        }

        for(int i = 0 ; i < 2 ; i++){
            dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
        }

    }

    public Jugador getJugadorActual(){
        return jugadores.get(jugadorActual);
    }

    public void pedirCartaJugador() {
            if (!getJugadorActual().getMano().sePaso()) {
                getJugadorActual().getMano().agregarCarta(mazo.obtenerUnaCarta());
            }

    }

    // El dealer pide cartas automaticamente mientras su mano sume menos de 17
    public void turnoDealer() {
        while (dealer.getMano().calcularPuntaje() < 17) {
            dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
        }
    }

    // Retorna el resultado final de la ronda
    public String obtenerResultado() {
        int ptsJugador = getJugadorActual().getMano().calcularPuntaje();
        int ptsDealer = dealer.getMano().calcularPuntaje();

        if (getJugadorActual().getMano().sePaso()) {
            return "Te has pasado de 21! La casa gana.";
        } else if (dealer.getMano().sePaso()) {
            return "La casa se paso de 21! Ganaste!";
        } else if (ptsJugador > ptsDealer) {
            return "Ganaste la partida!";
        } else if (ptsDealer > ptsJugador) {
            return "Gana la casa.";
        } else {
            return "Empate!";
        }
    }

    public Jugador getDealer() {
        return dealer;
    }

    public void siguienteJugador(){

        jugadorActual++;

        if(jugadorActual > 4){
            jugadorActual = 0;
        }

    }
}
