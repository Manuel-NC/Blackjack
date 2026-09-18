package com.example.blackjack.Controlador;

import com.example.blackjack.Estructuras.Pila;
import com.example.blackjack.Modelo.CartaInglesa;
import com.example.blackjack.Modelo.Jugador;
import com.example.blackjack.Modelo.Mazo;

// Controlador principal del juego de Blackjack
// Administra el flujo de turnos, el mazo, la casa y los jugadores usando Pilas
public class JuegoBlackjack {
    private Mazo mazo;
    private Pila<Jugador> jugadores;
    private Jugador dealer;
    private int indiceJugadorActual;

    // Pila de cartas descartadas por Undo para garantizar que no vuelvan a salir en la misma ronda
    private Pila<CartaInglesa> cartasDescartadas;

    // Constructor que inicializa los jugadores y prepara la partida
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

    // Inicia una nueva ronda limpiando manos, mezclando el mazo y repartiendo cartas iniciales
    public void iniciarNuevaRonda() {
        mazo = new Mazo();
        cartasDescartadas = new Pila<>(52);
        dealer.reiniciarMano();

        for (int i = 0; i < jugadores.getTamano(); i++) {
            jugadores.getElemento(i).reiniciarMano();
        }

        // Repartir 2 cartas a cada jugador y a la casa
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < jugadores.getTamano(); j++) {
                jugadores.getElemento(j).getMano().agregarCarta(mazo.obtenerUnaCarta());
            }
            dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
        }

        indiceJugadorActual = 0;
        validarJugadorActual21();
    }

    // Regresa el jugador que tiene el turno activo actualmente
    public Jugador getJugadorActual() {
        if (indiceJugadorActual < jugadores.getTamano()) {
            return jugadores.getElemento(indiceJugadorActual);
        }
        return null;
    }

    // Le da una carta al jugador en turno si no se ha plantado ni se ha pasado
    public void pedirCartaJugadorActual() {
        verificarMazoVacio(); // Rellena el mazo si quedo vacio por excesivos Undos
        Jugador actual = getJugadorActual();
        if (actual != null && !actual.estaFueraDeJuego()) {
            actual.getMano().agregarCarta(mazo.obtenerUnaCarta());

            // Si el jugador se pasa o alcanza 21, se planta y pasa al siguiente turno
            if (actual.getMano().sePaso() || actual.getMano().calcularPuntaje() == 21) {
                actual.plantarse();
                pasarAlSiguienteJugador();
            }
        }
    }

    // Planta al jugador actual y avanza el turno
    public void plantarseJugadorActual() {
        Jugador actual = getJugadorActual();
        if (actual != null) {
            actual.plantarse();
            pasarAlSiguienteJugador();
        }
    }

    // Revierte la ultima jugada (carta pedida o plantado) regresando turnos de forma limpia
    public boolean deshacerUltimaCartaJugadorActual() {
        // 1. Si la ronda termino (jugo el Dealer), cancelamos la mano del Dealer y regresamos al ultimo jugador
        if (esFinDeRonda()) {
            dealer.reiniciarMano();
            dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
            dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
            indiceJugadorActual = jugadores.getTamano() - 1;
        }

        // 2. Revisar el jugador en el turno actual
        Jugador actual = getJugadorActual();

        if (actual != null) {
            // Si el jugador actual tiene mas de 2 cartas, le quitamos la ultima carta pedida
            if (actual.getMano().getCartas().getTamano() > 2) {
                CartaInglesa cartaRetirada = actual.getMano().getCartas().pop();
                cartasDescartadas.push(cartaRetirada); // Va al descarte
                actual.desplantar(); // Reabrimos su turno por si estaba plantado o fuera de juego
                return true;
            }
            // Si el jugador actual solo tiene sus 2 cartas iniciales, retrocedemos al jugador anterior
            else if (indiceJugadorActual > 0) {
                actual.desplantar(); // Aseguramos que el jugador actual quede desplantado
                indiceJugadorActual--; // Retrocedemos exactamente 1 turno hacia el jugador anterior

                Jugador anterior = getJugadorActual();
                if (anterior != null) {
                    anterior.desplantar(); // Reabrimos el turno del jugador anterior
                    // Si el jugador anterior tenia cartas extras, le retiramos su ultima carta
                    if (anterior.getMano().getCartas().getTamano() > 2) {
                        CartaInglesa cartaRetirada = anterior.getMano().getCartas().pop();
                        cartasDescartadas.push(cartaRetirada);
                    }
                }
                return true;
            }
            // Si estamos en el Jugador 1 con sus 2 cartas iniciales, solo reabrimos su turno si estaba plantado
            else if (actual.getPlantado()) {
                actual.desplantar();
                return true;
            }
        }

        return false;
    }

    // Avanza el turno al siguiente jugador o inicia el turno del dealer si terminaron todos
    private void pasarAlSiguienteJugador() {
        indiceJugadorActual++;
        if (indiceJugadorActual >= jugadores.getTamano()) {
            turnoDealer();
        } else {
            validarJugadorActual21();
        }
    }

    // Ejecuta las decisiones automáticas de la casa (Dealer) si al menos un jugador no se paso
    public void turnoDealer() {
        boolean hayJugadoresVivos = false;
        for (int i = 0; i < jugadores.getTamano(); i++) {
            if (!jugadores.getElemento(i).getMano().sePaso()) {
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

    // Determina si el jugador gana, pierde o empata contra el dealer
    public String evaluarResultadoJugador(Jugador j) {
        int ptsJugador = j.getMano().calcularPuntaje();
        int ptsDealer = dealer.getMano().calcularPuntaje();

        if (j.getMano().sePaso()) return "Se paso de 21 (Perdio)";
        if (dealer.getMano().sePaso()) return "Gano! (La casa se paso)";
        if (ptsJugador > ptsDealer) return "Gano!";
        if (ptsDealer > ptsJugador) return "Perdio";
        return "Empate";
    }

    // Indica si se han completado los turnos de todos los jugadores
    public boolean esFinDeRonda() {
        return indiceJugadorActual >= jugadores.getTamano();
    }

    // Regresa la pila de jugadores
    public Pila<Jugador> getJugadores() { return jugadores; }

    // Regresa al dealer
    public Jugador getDealer() { return dealer; }

    // Verifica si el jugador actual tiene 21 desde el inicio para plantar automaticamente y avanzar
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

    // Rellena el mazo utilizando la pila de descarte si el mazo principal se queda sin cartas
    private void verificarMazoVacio() {
        if (mazo.getCartas().vacia() && !cartasDescartadas.vacia()) {
            while (!cartasDescartadas.vacia()) {
                mazo.getCartas().push(cartasDescartadas.pop());
            }
        }
    }
}