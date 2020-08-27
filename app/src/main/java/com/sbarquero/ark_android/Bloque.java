package com.sbarquero.ark_android;

import android.graphics.Color;

/**
 * Clase bloque que representa a cada bloque del juego
 */
public class Bloque {
    private int x;      // Coordenada X
    private int y;      // Coordenada Y
    private int ancho;  // Ancho del bloque
    private int alto;   // Alto del bloque
    private int color;  // Color del bloque
    private boolean visible;  // Indica la visibilidad del bloque en la partida

    public Bloque() {
        color = Color.YELLOW; // Color por defecto
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
