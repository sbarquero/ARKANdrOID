package com.sbarquero.ark_android;

import android.graphics.Color;

/**
 * Clase que almacena un objeto Bola
 */
public class Bola {

    private int x;  // Coordenada X
    private int y;  // Coordenada Y
    private int radio; // Radio de la bola
    private int color; // Color de la bola

    public Bola() {
        x = 0;
        y = 0;
        radio = 0;
        color = Color.WHITE; // Color inicial
    }

    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
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

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
