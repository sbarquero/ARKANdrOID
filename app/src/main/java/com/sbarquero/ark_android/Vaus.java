package com.sbarquero.ark_android;

/**
 * Clase que representa a Vaus, la nave espacial que destruye los bloques XD
 */
public class Vaus {
    private int x;  // Coordenada X
    private int y;  // Coordenada Y
    private int ancho; // Ancho
    private int alto;  // Alto

    public Vaus() {
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }
}
