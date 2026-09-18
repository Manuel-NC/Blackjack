package com.example.blackjack.Vista;

import com.example.blackjack.Controlador.JuegoBlackjack;
import com.example.blackjack.Estructuras.Pila;
import com.example.blackjack.Modelo.Jugador;

import java.util.Scanner;

// Interfaz de usuario por consola para interactuar con el juego de Blackjack
public class JuegoBlackjackTextUI {
    private JuegoBlackjack juego;
    private Scanner scanner;

    // Constructor que inicializa el lector de entradas de consola
    public JuegoBlackjackTextUI() {
        this.scanner = new Scanner(System.in);
    }

    // Inicia y controla el flujo principal de la interfaz de texto
    public void ejecutar() {
        System.out.println("HORA DE JUGAR BLACKJACK! (sin apuestas)      ");
        System.out.println("=========================================");

        int numJugadores = 0;
        // Solicita el numero de jugadores validando que este entre 1 y 4
        while (numJugadores < 1 || numJugadores > 4) {
            System.out.print("Ingrese la cantidad de jugadores: (1-4): ");
            try {
                numJugadores = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un numero valido.");
            }
        }

        // Crea la instancia del juego con la cantidad de jugadores seleccionada
        this.juego = new JuegoBlackjack(numJugadores);

        boolean jugarNuevamente = true;
        // Ciclo principal de partidas
        while (jugarNuevamente) {
            // Ciclo de turnos para los jugadores dentro de la ronda
            while (!juego.esFinDeRonda()) {
                Jugador actual = juego.getJugadorActual();

                System.out.println("\n-----------------------------------------");
                System.out.println(">>> TURNO ACTUAL: " + actual.getNombre() + " <<<");
                System.out.println("-----------------------------------------");

                System.out.println("Casa: " + juego.getDealer().getMano());

                // Muestra el estado actual de las manos de todos los jugadores
                Pila<Jugador> lista = juego.getJugadores();
                for (int i = 0; i < lista.getTamano(); i++) {
                    Jugador j = lista.getElemento(i);
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

                // Procesa la decision ingresada por el usuario
                String op = scanner.nextLine();
                if (op.equals("1")) {
                    juego.pedirCartaJugadorActual();
                } else if (op.equals("2")) {
                    juego.plantarseJugadorActual();
                } else {
                    System.out.println("Opcion invalida.");
                }
            }

            // Muestra el resumen final de la ronda cuando todos los turnos terminan
            System.out.println("\n================ RESULTADOS ================");
            System.out.println("Casa: " + juego.getDealer().getMano());
            Pila<Jugador> lista = juego.getJugadores();
            for (int i = 0; i < lista.getTamano(); i++) {
                Jugador j = lista.getElemento(i);
                System.out.println(j.getNombre() + ": " + j.getMano() + " -> " + juego.evaluarResultadoJugador(j));
            }
            System.out.println("===========================================");

            // Pregunta al usuario si desea iniciar una nueva partida
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