package com.example.blackjack.Vista;

import com.example.blackjack.Controlador.JuegoBlackjack;
import com.example.blackjack.Estructuras.Pila;
import com.example.blackjack.Modelo.CartaInglesa;
import com.example.blackjack.Modelo.Jugador;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

// Panel grafico principal que dibuja el tablero de juego en JavaFX
public class PanelTableroBlackjackGUI extends VBox {

    private Label lblTurnoActual;
    private Label lblEstado;

    private HBox boxCasa;
    private Label lblPuntosCasa;

    private HBox boxTodosLosJugadores;

    private Button btnPedir;
    private Button btnPlantarse;
    private Button btnNuevaRonda;
    private Button btnUndo;

    // Constructor que configura el diseno inicial y los componentes visuales
    public PanelTableroBlackjackGUI() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-padding: 20;");

        inicializarComponentes();
        configurarFondo();
    }

    // Inicializa las etiquetas, contenedores y botones de la interfaz
    private void inicializarComponentes() {
        lblTurnoActual = new Label();
        lblTurnoActual.setFont(new Font("Arial", 18));
        lblTurnoActual.setStyle("-fx-text-fill: yellow; -fx-font-weight: bold;");

        lblEstado = new Label("");
        lblEstado.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblEstado.setStyle("-fx-text-fill: white; -fx-alignment: center;");
        lblEstado.setWrapText(true);
        lblEstado.setMaxWidth(750);

        lblPuntosCasa = new Label();
        lblPuntosCasa.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        boxCasa = new HBox(8);
        boxCasa.setAlignment(Pos.CENTER);

        boxTodosLosJugadores = new HBox(20);
        boxTodosLosJugadores.setAlignment(Pos.CENTER);

        btnPedir = new Button("Pedir Carta");
        btnPlantarse = new Button("Plantarse");
        btnUndo = new Button("Deshacer (Undo)");
        btnNuevaRonda = new Button("Nueva Ronda");

        // Agrupa los botones de accion en un contenedor horizontal
        HBox boxBotones = new HBox(15, btnPedir, btnPlantarse, btnUndo, btnNuevaRonda);
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

    // Carga la imagen de fondo del tablero o aplica un color verde por defecto
    private void configurarFondo() {
        try {
            var recurso = getClass().getResource("/com/example/blackjack/fondo_blackjack.png");

            if (recurso == null) {
                this.setStyle("-fx-padding: 20; -fx-background-color: #1b5e20;");
                return;
            }

            Image imagenFondo = new Image(recurso.toExternalForm());

            BackgroundImage backgroundImage = new BackgroundImage(
                    imagenFondo,
                    BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT
            );

            this.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            this.setStyle("-fx-padding: 20; -fx-background-color: #1b5e20;");
        }
    }

    // Actualiza el tablero redibujando cartas y ajustando el estado de los botones
    public void actualizarTablero(JuegoBlackjack juego) {
        // Actualizar cartas y puntos del dealer
        boxCasa.getChildren().clear();
        Pila<CartaInglesa> cartasCasa = juego.getDealer().getMano().getCartas();
        for (int i = 0; i < cartasCasa.getTamano(); i++) {
            boxCasa.getChildren().add(crearTarjetaCarta(cartasCasa.getElemento(i)));
        }
        lblPuntosCasa.setText("Puntos: " + juego.getDealer().getMano().calcularPuntaje());

        // Actualizar cartas y puntos de los jugadores
        boxTodosLosJugadores.getChildren().clear();
        Jugador actual = juego.getJugadorActual();
        Pila<Jugador> listaJugadores = juego.getJugadores();

        for (int i = 0; i < listaJugadores.getTamano(); i++) {
            Jugador j = listaJugadores.getElemento(i);
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

            Pila<CartaInglesa> cartasJugador = j.getMano().getCartas();
            for (int k = 0; k < cartasJugador.getTamano(); k++) {
                boxCartas.getChildren().add(crearTarjetaCarta(cartasJugador.getElemento(k)));
            }

            panelJugador.getChildren().addAll(lblNombre, lblPuntos, boxCartas);
            boxTodosLosJugadores.getChildren().add(panelJugador);
        }

        // Habilita o deshabilita botones segun el estado de la ronda
        if (!juego.esFinDeRonda()) {
            lblTurnoActual.setText("TURNO DE: " + actual.getNombre());
            btnPedir.setDisable(false);
            btnPlantarse.setDisable(false);
            btnUndo.setDisable(false);
            btnNuevaRonda.setDisable(true);
        } else {
            lblTurnoActual.setText("--- RONDA FINALIZADA ---");
            btnPedir.setDisable(true);
            btnPlantarse.setDisable(true);
            btnUndo.setDisable(false);
            btnNuevaRonda.setDisable(false);
            mostrarResultadosFinDeRonda(juego);
        }
    }

    // Muestra los resultados de cada jugador al concluir la ronda
    private void mostrarResultadosFinDeRonda(JuegoBlackjack juego) {
        StringBuilder resultados = new StringBuilder();
        Pila<Jugador> listaJugadores = juego.getJugadores();

        for (int i = 0; i < listaJugadores.getTamano(); i++) {
            Jugador j = listaJugadores.getElemento(i);
            resultados.append(j.getNombre())
                    .append(": ")
                    .append(juego.evaluarResultadoJugador(j))
                    .append("\n");
        }
        lblEstado.setText(resultados.toString().trim());
    }

    // Crea el componente visual individual para representar una carta
    private VBox crearTarjetaCarta(CartaInglesa carta) {
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
                paloSimbolo = paloTexto;
                break;
        }

        String valorTexto = String.valueOf(carta.getValor());
        if (carta.getValor() == 11) valorTexto = "J";
        else if (carta.getValor() == 12) valorTexto = "Q";
        else if (carta.getValor() == 13) valorTexto = "K";
        else if (carta.getValor() == 14 || carta.getValor() == 1) valorTexto = "A";

        String colorTexto = carta.getColor().equalsIgnoreCase("rojo") ? "#cc0000" : "#000000";

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

    // Getters para acceder a los controles desde la vista principal
    public Button getBtnPedir() { return btnPedir; }
    public Button getBtnPlantarse() { return btnPlantarse; }
    public Button getBtnNuevaRonda() { return btnNuevaRonda; }
    public Label getLblEstado() { return lblEstado; }
    public Button getBtnUndo() { return btnUndo; }
}