package com.example.blackjack.Controlador;

import com.example.blackjack.Modelo.CartaInglesa;
import com.example.blackjack.Modelo.Jugador;
import com.example.blackjack.Modelo.Mazo;
import java.util.ArrayList;

// Controlador principal del juego
public class JuegoBlackjack {
    private Mazo mazo;
    private ArrayList<Jugador> jugadores;
    private Jugador dealer;
    private int indiceJugadorActual;

    // Se puede usar ArrayList para descartes si se gestiona de forma LIFO (remove del ultimo indice)
    private ArrayList<CartaInglesa> cartasDescartadas;

    public JuegoBlackjack(int numJugadores) {
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
        cartasDescartadas = new ArrayList<>();
        dealer.reiniciarMano();

        for (Jugador j : jugadores) {
            j.reiniciarMano();
        }

        for (int i = 0; i < 2; i++) {
            for (Jugador j : jugadores) {
                j.getMano().agregarCarta(mazo.obtenerUnaCarta());
            }
            dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
        }

        indiceJugadorActual = 0;
        validarJugadorActual21();
    }

    public Jugador getJugadorActual() {
        if (indiceJugadorActual < jugadores.size()) {
            return jugadores.get(indiceJugadorActual);
        }
        return null;
    }

    public void pedirCartaJugadorActual() {
        verificarMazoVacio();
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

    // Realiza el Undo retirando la carta con pop() de la Pila de la Mano
    public boolean deshacerUltimaCartaJugadorActual() {
        if (esFinDeRonda()) {
            dealer.reiniciarMano();
            dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
            dealer.getMano().agregarCarta(mazo.obtenerUnaCarta());
            indiceJugadorActual = jugadores.size() - 1;
        }

        Jugador actual = getJugadorActual();

        if (actual != null) {
            if (actual.getMano().getCartas().getTamano() > 2) {
                CartaInglesa cartaRetirada = actual.getMano().getCartas().pop(); // Invocacion obligatoria a la Pila
                cartasDescartadas.add(cartaRetirada);
                actual.desplantar();
                return true;
            } else if (indiceJugadorActual > 0) {
                actual.desplantar();
                indiceJugadorActual--;

                Jugador anterior = getJugadorActual();
                if (anterior != null) {
                    anterior.desplantar();
                    if (anterior.getMano().getCartas().getTamano() > 2) {
                        CartaInglesa cartaRetirada = anterior.getMano().getCartas().pop();
                        cartasDescartadas.add(cartaRetirada);
                    }
                }
                return true;
            } else if (actual.getPlantado()) {
                actual.desplantar();
                return true;
            }
        }

        return false;
    }

    private void pasarAlSiguienteJugador() {
        indiceJugadorActual++;
        if (indiceJugadorActual >= jugadores.size()) {
            turnoDealer();
        } else {
            validarJugadorActual21();
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

        if (j.getMano().sePaso()) return "Se paso de 21 (Perdio)";
        if (dealer.getMano().sePaso()) return "Gano! (La casa se paso)";
        if (ptsJugador > ptsDealer) return "Gano!";
        if (ptsDealer > ptsJugador) return "Perdio";
        return "Empate";
    }

    public boolean esFinDeRonda() {
        return indiceJugadorActual >= jugadores.size();
    }

    public ArrayList<Jugador> getJugadores() { return jugadores; }
    public Jugador getDealer() { return dealer; }

    private void validarJugadorActual21() {
        while (!esFinDeRonda()) {
            Jugador actual = getJugadorActual();
            if (actual != null && actual.getMano().calcularPuntaje() == 21) {
                actual.plantarse();
                indiceJugadorActual++;
                if (indiceJugadorActual >= jugadores.size()) {
                    turnoDealer();
                }
            } else {
                break;
            }
        }
    }

    private void verificarMazoVacio() {
        if (mazo.getCartas().isEmpty() && !cartasDescartadas.isEmpty()) {
            while (!cartasDescartadas.isEmpty()) {
                mazo.getCartas().add(cartasDescartadas.remove(cartasDescartadas.size() - 1));
            }
        }
    }
}