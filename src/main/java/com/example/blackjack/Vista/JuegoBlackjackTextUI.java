package com.example.blackjack.Vista;

import com.example.blackjack.Controlador.JuegoBlackjack;
import com.example.blackjack.Modelo.Jugador;

import java.util.Scanner;

public class JuegoBlackjackTextUI {
    private JuegoBlackjack juego;
    private Scanner scanner;

    public JuegoBlackjackTextUI() {
        this.scanner = new Scanner(System.in);
    }

    public void ejecutar() {
        System.out.println("=========================================");
        System.out.println("       ¡BIENVENIDO AL JUEGO DEL 21!      ");
        System.out.println("=========================================");

        // Preguntar la cantidad de jugadores (1-4)
        int numJugadores = 0;
        while (numJugadores < 1 || numJugadores > 4) {
            System.out.print("¿Cuántos jugadores van a participar? (1-4): ");
            try {
                numJugadores = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
            }
        }

        // Instanciar el controlador con el número seleccionado
        this.juego = new JuegoBlackjack(numJugadores);

        // [Aquí continúa la lógica normal de turnos e iteración de jugadores]
        boolean jugarNuevamente = true;
        while (jugarNuevamente) {
            while (!juego.esFinDeRonda()) {
                Jugador actual = juego.getJugadorActual();
                System.out.println("\n-----------------------------------------");
                System.out.println("TURNO DE: " + actual.getNombre());
                System.out.println("Cartas: " + actual.getMano());
                System.out.println("1. Pedir Carta");
                System.out.println("2. Plantarse");
                System.out.print("Selecciona una opción: ");

                String op = scanner.nextLine();
                if (op.equals("1")) {
                    juego.pedirCartaJugadorActual();
                } else if (op.equals("2")) {
                    juego.plantarseJugadorActual();
                }
            }

            // Resultados al terminar la ronda de todos
            System.out.println("\n================ RESULTADOS ================");
            System.out.println("Casa: " + juego.getDealer().getMano());
            for (Jugador j : juego.getJugadores()) {
                System.out.println(j.getNombre() + ": " + j.getMano() + " -> " + juego.evaluarResultadoJugador(j));
            }
            System.out.println("===========================================");

            System.out.print("\n¿Desean jugar otra ronda? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                juego.iniciarNuevaRonda();
            } else {
                jugarNuevamente = false;
            }
        }
    }
}
