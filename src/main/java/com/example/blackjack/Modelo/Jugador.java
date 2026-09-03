package com.example.blackjack.Modelo;

public class Jugador {
    private String nombre;
    private ManoBlackjack mano;
    private boolean plantado;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.plantado = false;
        this.mano = new ManoBlackjack();
    }

    public String getNombre(){
        return nombre;
    }

    public boolean getPlantado(){
        return plantado;
    }

    public ManoBlackjack getMano(){
        return mano;
    }

    public void plantarse(){
        this.plantado = true;
    }

    public void reiniciarMano(){
        mano.limpiar();
        plantado = false;
    }

    public boolean estaFueraDeJuego() {
        return plantado || mano.sePaso();
    }



    @Override
    public String toString(){
        return nombre + ":" + mano.toString();
    }

}
