package com.sbarquero.ark_android;

/**
 * Clase para guardar la configuración de la partida.
 * La utilizo para pasar la configuración al canvas cuando se crea.
 *
 * @author Santiago Barquero - 2º DAM
 * @version 2020-03-03
 */
public class Configuracion {
    private int duracionPartida; // Duración de la partida en segundos
    private int velocidadBola;   // Velocidad de la bola en número de píxeles
    private int numBloquesAncho; // Ancho del bloque en píxeles
    private int numBloquesAlto;  // Alto del bloque en píxeles
    private int velocidadVaus;   // Velocidad de Vaus en número de pixeles
    private int TamanioVaus;     // Porcentaje de Vaus con respecto al ancho de la pantalla

    public Configuracion() {
    }

    public int getDuracionPartida() {
        return duracionPartida;
    }

    public void setDuracionPartida(int tiempoPartida) {
        this.duracionPartida = tiempoPartida;
    }

    public int getVelocidadBola() {
        return velocidadBola;
    }

    public void setVelocidadBola(int velocidadBola) {
        this.velocidadBola = velocidadBola;
    }

    public int getNumBloquesAncho() {
        return numBloquesAncho;
    }

    public void setNumBloquesAncho(int numBloquesAncho) {
        this.numBloquesAncho = numBloquesAncho;
    }

    public int getNumBloquesAlto() {
        return numBloquesAlto;
    }

    public void setNumBloquesAlto(int numBloquesAlto) {
        this.numBloquesAlto = numBloquesAlto;
    }

    public int getVelocidadVaus() {
        return velocidadVaus;
    }

    public void setVelocidadVaus(int velocidadVaus) {
        this.velocidadVaus = velocidadVaus;
    }

    public int getTamanioVaus() {
        return TamanioVaus;
    }

    public void setTamanioVaus(int tamanioVaus) {
        TamanioVaus = tamanioVaus;
    }
}
