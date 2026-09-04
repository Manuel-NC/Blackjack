package com.example.blackjack;

import com.example.blackjack.Controlador.JuegoBlackjack;
import com.example.blackjack.Vista.PanelTableroBlackjackGUI;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class JuegoBlackjackGUI extends Application {

    private JuegoBlackjack juego;
    private PanelTableroBlackjackGUI panelTablero;

    @Override
    public void start(Stage primaryStage) {
        int numJugadores = solicitarNumeroJugadores();

        juego = new JuegoBlackjack(numJugadores);
        panelTablero = new PanelTableroBlackjackGUI();

        configurarAccionesBotones();

        panelTablero.actualizarTablero(juego);

        Scene scene = new Scene(panelTablero, 1024, 768);
        primaryStage.setTitle("Blackjack - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private int solicitarNumeroJugadores() {
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
        return result.orElse(1);
    }

    private void configurarAccionesBotones() {
        panelTablero.getBtnPedir().setOnAction(e -> {
            juego.pedirCartaJugadorActual();
            panelTablero.actualizarTablero(juego);
        });

        panelTablero.getBtnPlantarse().setOnAction(e -> {
            juego.plantarseJugadorActual();
            panelTablero.actualizarTablero(juego);
        });

        panelTablero.getBtnNuevaRonda().setOnAction(e -> {
            juego.iniciarNuevaRonda();
            panelTablero.getLblEstado().setText("");
            panelTablero.actualizarTablero(juego); // <--- Se encarga de reactivar Pedir/Plantarse y bloquear Nueva Ronda
        });
    }
}