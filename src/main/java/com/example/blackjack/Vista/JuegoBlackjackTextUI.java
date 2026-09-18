package com.example.blackjack.Vista;

import com.example.blackjack.Controlador.JuegoBlackjack;
import com.example.blackjack.Modelo.Jugador;
import java.util.ArrayList;
import java.util.Scanner;

// Interfaz de usuario por consola adaptada para usar ArrayList de jugadores
public class JuegoBlackjackTextUI {
    private JuegoBlackjack juego;
    private Scanner scanner;

    public JuegoBlackjackTextUI() {
        this.scanner = new Scanner(System.in);
    }

    public void ejecutar() {
        System.out.println("HORA DE JUGAR BLACKJACK! (sin apuestas)      ");
        System.out.println("=========================================");

        int numJugadores = 0;
        while (numJugadores < 1 || numJugadores > 4) {
            System.out.print("Ingrese la cantidad de jugadores: (1-4): ");
            try {
                numJugadores = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un numero valido.");
            }
        }

        this.juego = new JuegoBlackjack(numJugadores);

        boolean jugarNuevamente = true;
        while (jugarNuevamente) {
            while (!juego.esFinDeRonda()) {
                Jugador actual = juego.getJugadorActual();

                System.out.println("\n-----------------------------------------");
                System.out.println(">>> TURNO ACTUAL: " + actual.getNombre() + " <<<");
                System.out.println("-----------------------------------------");

                System.out.println("Casa: " + juego.getDealer().getMano());

                // Muestra las manos recorriendo la lista de jugadores como ArrayList
                ArrayList<Jugador> lista = juego.getJugadores();
                for (Jugador j : lista) {
                    if (j.equals(actual)) {
                        System.out.println("-> " + j.getNombre() + ": " + j.getMano() + " [TURNO ACTUAL]");
                    } else {
                        System.out.println("   " + j.getNombre() + ": " + j.getMano());
                    }
                }

                System.out.println("-----------------------------------------");
                System.out.println("1. Pedir Carta");
                System.out.println("2. Plantarse");
                System.out.print("Selecciona una opcion: ");

                String op = scanner.nextLine();
                if (op.equals("1")) {
                    juego.pedirCartaJugadorActual();
                } else if (op.equals("2")) {
                    juego.plantarseJugadorActual();
                } else {
                    System.out.println("Opcion invalida.");
                }
            }

            // Muestra los resultados recorriendo la lista con bucle for-each
            System.out.println("\n================ RESULTADOS ================");
            System.out.println("Casa: " + juego.getDealer().getMano());
            ArrayList<Jugador> lista = juego.getJugadores();
            for (Jugador j : lista) {
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

        System.out.println("\nJuego terminado!");
    }
}