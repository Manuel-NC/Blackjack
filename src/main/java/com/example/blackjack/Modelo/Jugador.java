package com.example.blackjack.Modelo;

// Representa a un jugador individual dentro de la partida de Blackjack
public class Jugador {
    private String nombre;
    private ManoBlackjack mano;
    private boolean plantado;

    // Constructor que inicializa el nombre, su estado de plantado en falso y una mano vacia
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.plantado = false;
        this.mano = new ManoBlackjack();
    }

    // Regresa el nombre del jugador
    public String getNombre(){
        return nombre;
    }

    // Regresa si el jugador ya se planto
    public boolean getPlantado(){
        return plantado;
    }

    // Regresa la mano de cartas del jugador
    public ManoBlackjack getMano(){
        return mano;
    }

    // Marca al jugador como plantado para terminar sus decisiones en el turno
    public void plantarse(){
        this.plantado = true;
    }

    // Desmarca el estado de plantado permitiendo que el jugador vuelva a pedir carta (usado para Undo)
    public void desplantar(){
        this.plantado = false;
    }

    // Limpia la mano de cartas del jugador y restablece su estado de plantado a falso
    public void reiniciarMano(){
        mano.limpiar();
        plantado = false;
    }

    // Verifica si el turno del jugador termino, ya sea porque se planto o se paso de 21 puntos
    public boolean estaFueraDeJuego() {
        return plantado || mano.sePaso();
    }

    // Representacion en formato texto del nombre del jugador y sus cartas actuales
    @Override
    public String toString(){
        return nombre + ":" + mano.toString();
    }
}
