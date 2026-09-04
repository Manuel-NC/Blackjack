package com.example.blackjack.Vista;

import com.example.blackjack.Controlador.JuegoBlackjack;
import javafx.application.Application;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class JuegoBlackjackGUI extends Application {

    private JuegoBlackjack juego;

    @Override
    public void start(Stage primaryStage) {
        ArrayList<Integer> opciones = new ArrayList<>();
        opciones.add(1);
        opciones.add(2);
        opciones.add(3);
        opciones.add(4);

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, opciones);
        dialog.setTitle("Configuración de Partida");
        dialog.setHeaderText("¡Bienvenido a Blackjack!");
        dialog.setContentText("Selecciona el número de jugadores (1-4):");

        Optional<Integer> result = dialog.showAndWait();
        int numJugadores = result.orElse(1);

        // Instanciar el controlador
        juego = new JuegoBlackjack(numJugadores);
    }
}