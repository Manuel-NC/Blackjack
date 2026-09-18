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

// Clase principal de JavaFX que inicia la aplicacion con interfaz grafica
public class JuegoBlackjackGUI extends Application {

    private JuegoBlackjack juego;
    private PanelTableroBlackjackGUI panelTablero;

    // Metodo de entrada principal para aplicaciones JavaFX
    @Override
    public void start(Stage primaryStage) {
        int numJugadores = solicitarNumeroJugadores();

        juego = new JuegoBlackjack(numJugadores);
        panelTablero = new PanelTableroBlackjackGUI();

        configurarAccionesBotones();

        panelTablero.actualizarTablero(juego);

        Scene scene = new Scene(panelTablero, 1024, 768);
        primaryStage.setTitle("Blackjack (sin apuestas)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Muestra un dialogo emergente para elegir la cantidad de jugadores entre 1 y 4
    private int solicitarNumeroJugadores() {
        ArrayList<Integer> opciones = new ArrayList<>();
        opciones.add(1);
        opciones.add(2);
        opciones.add(3);
        opciones.add(4);

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, opciones);
        dialog.setTitle("Configuracion de Partida");
        dialog.setHeaderText("Bienvenido a Blackjack!");
        dialog.setContentText("Selecciona el numero de jugadores (1-4):");

        Optional<Integer> result = dialog.showAndWait();
        return result.orElse(1);
    }

    // Conecta los eventos de los botones de la vista con la logica del controlador
    private void configurarAccionesBotones() {

        // Boton para pedir carta
        panelTablero.getBtnPedir().setOnAction(e -> {
            juego.pedirCartaJugadorActual();
            panelTablero.actualizarTablero(juego);
        });

        // Boton para plantarse
        panelTablero.getBtnPlantarse().setOnAction(e -> {
            juego.plantarseJugadorActual();
            panelTablero.actualizarTablero(juego);
        });

        // Boton para deshacer la ultima jugada
        panelTablero.getBtnUndo().setOnAction(e -> {
            boolean exito = juego.deshacerUltimaCartaJugadorActual();
            if (exito) {
                panelTablero.getLblEstado().setText(""); // Limpia mensaje de estado si la ronda habia terminado
                panelTablero.actualizarTablero(juego);
            }
        });

        // Boton para reiniciar la ronda cuando todos han terminado
        panelTablero.getBtnNuevaRonda().setOnAction(e -> {
            juego.iniciarNuevaRonda();
            panelTablero.getLblEstado().setText("");
            panelTablero.actualizarTablero(juego);
        });
    }
}