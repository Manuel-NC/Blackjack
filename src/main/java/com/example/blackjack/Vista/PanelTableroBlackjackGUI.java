package com.example.blackjack.Vista;

import com.example.blackjack.Controlador.JuegoBlackjack;
import com.example.blackjack.Modelo.CartaInglesa;
import com.example.blackjack.Modelo.Jugador;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Objects;

public class PanelTableroBlackjackGUI extends VBox {

    private Label lblTurnoActual;
    private Label lblEstado;

    private HBox boxCasa;
    private Label lblPuntosCasa;

    private HBox boxTodosLosJugadores;

    private Button btnPedir;
    private Button btnPlantarse;
    private Button btnNuevaRonda;

    public PanelTableroBlackjackGUI() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-padding: 20;");

        inicializarComponentes();
        configurarFondo();
    }

    private void inicializarComponentes() {
        lblTurnoActual = new Label();
        lblTurnoActual.setFont(new Font("Arial", 18));
        lblTurnoActual.setStyle("-fx-text-fill: yellow; -fx-font-weight: bold;");

        lblEstado = new Label("");
        lblEstado.setFont(new Font("Arial", 16));
        lblEstado.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        lblPuntosCasa = new Label();
        lblPuntosCasa.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        boxCasa = new HBox(8);
        boxCasa.setAlignment(Pos.CENTER);

        boxTodosLosJugadores = new HBox(20);
        boxTodosLosJugadores.setAlignment(Pos.CENTER);

        btnPedir = new Button("Pedir Carta");
        btnPlantarse = new Button("Plantarse");
        btnNuevaRonda = new Button("Nueva Ronda");

        HBox boxBotones = new HBox(15, btnPedir, btnPlantarse, btnNuevaRonda);
        boxBotones.setAlignment(Pos.CENTER);

        VBox layoutCasa = new VBox(5);
        layoutCasa.setAlignment(Pos.CENTER);
        Label lblTituloCasa = new Label("CASA (DEALER)");
        lblTituloCasa.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        layoutCasa.getChildren().addAll(lblTituloCasa, lblPuntosCasa, boxCasa);

        this.getChildren().addAll(
                lblTurnoActual,
                layoutCasa,
                boxTodosLosJugadores,
                lblEstado,
                boxBotones
        );
    }

    private void configurarFondo() {
        try {
            Image imagenFondo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("C:/Users/manue/IdeaProjects/Blackjack/src/main/resources/com/example/blackjack/fondo_blackjack.png")));
            BackgroundImage backgroundImage = new BackgroundImage(
                    imagenFondo,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            this.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            this.setStyle("-fx-padding: 20; -fx-background-color: #1b5e20;");
        }
    }

    public void actualizarTablero(JuegoBlackjack juego) {
        // Actualizar casa
        boxCasa.getChildren().clear();
        ArrayList<CartaInglesa> cartasCasa = juego.getDealer().getMano().getCartas();
        for (CartaInglesa c : cartasCasa) {
            boxCasa.getChildren().add(crearTarjetaCarta(c));
        }
        lblPuntosCasa.setText("Puntos: " + juego.getDealer().getMano().calcularPuntaje());

        // Actualizar jugadores
        boxTodosLosJugadores.getChildren().clear();
        Jugador actual = juego.getJugadorActual();
        ArrayList<Jugador> listaJugadores = juego.getJugadores();

        for (Jugador j : listaJugadores) {
            VBox panelJugador = new VBox(8);
            panelJugador.setAlignment(Pos.CENTER);

            boolean esSuTurno = (actual != null && actual.equals(j) && !juego.esFinDeRonda());
            String estiloBorde = esSuTurno
                    ? "-fx-border-color: yellow; -fx-border-width: 3; -fx-border-radius: 10; -fx-background-color: rgba(0,0,0,0.4); -fx-padding: 10; -fx-background-radius: 10;"
                    : "-fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-color: rgba(0,0,0,0.2); -fx-padding: 10; -fx-background-radius: 10;";

            panelJugador.setStyle(estiloBorde);

            Label lblNombre = new Label(j.getNombre() + (esSuTurno ? " (TURNO)" : ""));
            lblNombre.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

            Label lblPuntos = new Label("Puntos: " + j.getMano().calcularPuntaje());
            lblPuntos.setStyle("-fx-text-fill: white;");

            HBox boxCartas = new HBox(5);
            boxCartas.setAlignment(Pos.CENTER);

            ArrayList<CartaInglesa> cartasJugador = j.getMano().getCartas();
            for (CartaInglesa c : cartasJugador) {
                boxCartas.getChildren().add(crearTarjetaCarta(c));
            }

            panelJugador.getChildren().addAll(lblNombre, lblPuntos, boxCartas);
            boxTodosLosJugadores.getChildren().add(panelJugador);
        }

        if (!juego.esFinDeRonda()) {
            lblTurnoActual.setText("TURNO DE: " + actual.getNombre());
        } else {
            lblTurnoActual.setText(">>> RONDA FINALIZADA <<<");
            btnPedir.setDisable(true);
            btnPlantarse.setDisable(true);
            mostrarResultadosFinDeRonda(juego);
        }
    }

    private void mostrarResultadosFinDeRonda(JuegoBlackjack juego) {
        StringBuilder resultados = new StringBuilder();
        ArrayList<Jugador> listaJugadores = juego.getJugadores();

        for (Jugador j : listaJugadores) {
            resultados.append(j.getNombre()).append(": ").append(juego.evaluarResultadoJugador(j)).append("   |   ");
        }
        lblEstado.setText(resultados.toString());
    }

    private Label crearTarjetaCarta(CartaInglesa carta) {
        Label lbl = new Label(carta.toString());
        lbl.setFont(new Font("Monospaced", 16));
        String colorTexto = carta.getColor().equalsIgnoreCase("rojo") ? "red" : "black";
        lbl.setStyle("-fx-background-color: white; -fx-padding: 8 12; -fx-border-color: black; -fx-border-radius: 5; -fx-background-radius: 5; -fx-text-fill: " + colorTexto + ";");
        return lbl;
    }

    public Button getBtnPedir() { return btnPedir; }
    public Button getBtnPlantarse() { return btnPlantarse; }
    public Button getBtnNuevaRonda() { return btnNuevaRonda; }
    public Label getLblEstado() { return lblEstado; }
}