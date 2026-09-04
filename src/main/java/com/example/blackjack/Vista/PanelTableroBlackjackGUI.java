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
import javafx.scene.text.FontWeight;

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
            var recurso = getClass().getResource("/com/example/blackjack/fondo_blackjack.png");

            if (recurso == null) {
                System.err.println("No se encontró la imagen en los recursos.");
                this.setStyle("-fx-padding: 20; -fx-background-color: #1b5e20;");
                return;
            }

            Image imagenFondo = new Image(recurso.toExternalForm());

            // Configuración para que actúe como mosaico (patrón repetitivo)
            BackgroundImage backgroundImage = new BackgroundImage(
                    imagenFondo,
                    BackgroundRepeat.REPEAT,   // Repetir horizontalmente
                    BackgroundRepeat.REPEAT,   // Repetir verticalmente
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT     // Mantiene el tamaño real del patrón
            );

            this.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
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

        // Estado de los botones según la fase del juego
        if (!juego.esFinDeRonda()) {
            lblTurnoActual.setText("TURNO DE: " + actual.getNombre());
            btnPedir.setDisable(false);
            btnPlantarse.setDisable(false);
            btnNuevaRonda.setDisable(true); // <--- DESHABILITADO durante la partida
        } else {
            lblTurnoActual.setText(">>> RONDA FINALIZADA <<<");
            btnPedir.setDisable(true);
            btnPlantarse.setDisable(true);
            btnNuevaRonda.setDisable(false); // <--- SOLO SE ACTIVA al terminar la ronda
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

    private VBox crearTarjetaCarta(CartaInglesa carta) {
        // Convertir el nombre del palo al símbolo Unicode correspondiente
        String paloTexto = carta.getPalo().toString().toUpperCase();
        String paloSimbolo = "";

        switch (paloTexto) {
            case "CORAZON":
            case "CORAZONES":
                paloSimbolo = "♥";
                break;
            case "DIAMANTE":
            case "DIAMANTES":
                paloSimbolo = "♦";
                break;
            case "TREBOL":
            case "TREBOLES":
                paloSimbolo = "♣";
                break;
            case "PICA":
            case "PICAS":
                paloSimbolo = "♠";
                break;
            default:
                paloSimbolo = paloTexto; // Por si acaso
                break;
        }

        // Formatear el valor del número/letra
        String valorTexto = String.valueOf(carta.getValor());
        if (carta.getValor() == 11) valorTexto = "J";
        else if (carta.getValor() == 12) valorTexto = "Q";
        else if (carta.getValor() == 13) valorTexto = "K";
        else if (carta.getValor() == 14 || carta.getValor() == 1) valorTexto = "A";

        String colorTexto = carta.getColor().equalsIgnoreCase("rojo") ? "#cc0000" : "#000000";

        // Contenedor rectangular de la carta
        VBox tarjeta = new VBox(2);
        tarjeta.setPrefSize(50, 75);
        tarjeta.setMinSize(50, 75);
        tarjeta.setMaxSize(50, 75);
        tarjeta.setAlignment(Pos.CENTER);

        tarjeta.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #222222; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        Label lblValor = new Label(valorTexto);
        lblValor.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblValor.setStyle("-fx-text-fill: " + colorTexto + ";");

        Label lblPalo = new Label(paloSimbolo);
        lblPalo.setFont(Font.font("Segoe UI Symbol", 20));
        lblPalo.setStyle("-fx-text-fill: " + colorTexto + ";");

        tarjeta.getChildren().addAll(lblValor, lblPalo);

        return tarjeta;
    }

    public Button getBtnPedir() { return btnPedir; }
    public Button getBtnPlantarse() { return btnPlantarse; }
    public Button getBtnNuevaRonda() { return btnNuevaRonda; }
    public Label getLblEstado() { return lblEstado; }
}