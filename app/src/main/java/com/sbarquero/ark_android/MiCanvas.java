package com.sbarquero.ark_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Canvas del juego que se crea la parte visual de la partida.
 *
 * @author Santiago Barquero López - 2º DAM
 * @version 2020-03-03
 */
public class MiCanvas extends View implements View.OnTouchListener {

    // ya sea por Game Over o porque se ha ganado.
    // Se recupera por medio de isFinal()
    private boolean finalPartida;

    private long tiempoRestante; // Tiempo restante para visualizar. Se recibo por setTiempo(x)

    private Paint pincel;

    // Objeto que se van a dibujar
    private Bola bola;
    private Vaus vaus;
    private Bloque[][] block; // Array bidimensional de bloques

    // Dimensiones
    private int altoCanvas;
    private int anchoCanvas;
    private int anchoMarco;

    // Limites
    private int limiteDerecha;
    private int limiteIzquierda;
    private int limiteSuperior;
    private int limiteInferior;

    // Velocidad de la bola
    private int incX;  // Eje X
    private int incY;  // Eje Y

    // Array con los colores para los bloques
    private int[] color = {Color.GREEN, Color.RED, Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.WHITE};

    private int numBloquesVisibles; // Número de bloques que quedan todavía visibles

    private MediaPlayer mPlayer;

    Configuracion conf; // Variable que contiene la configuración del juego

    /**
     * Constructor que recibe como parámetro el contexto y el objeto con la configuración
     * @param context
     * @param conf Objeto con la configuración del juego
     */
    public MiCanvas(Context context, Configuracion conf) {
        super(context);
        finalPartida = false; // Atributo que cambio a true cuando finalice la partida
        bola = new Bola();
        pincel = new Paint();

        // Cargo la configuración del juego que recibo como parámetro
        this.conf = conf;

        //  Establezco dirección inicial aleatoria de salida de la bola según si el milisegundo sea par o impar
        incX = conf.getVelocidadBola() * (System.currentTimeMillis()%2 == 0 ? 1 : -1);
        incY = -conf.getVelocidadBola();

        // Calculo los bloques visibles inicialmente
        numBloquesVisibles = conf.getNumBloquesAncho() * conf.getNumBloquesAlto();
    }

    /**
     * Redibuja cuando cambia el tamaño de la pantalla.
     * Se llama al dibujar el canvas por primera vez.
     * Lo utilizaba para cuando giraba el móvil pero por falta de tiempo no lo he implementado.
     * @param w  Nuevo ancho
     * @param h  Nuevo alto
     * @param oldW Ancho anterior
     * @param oldH Alto anterior
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        anchoCanvas = w;
        altoCanvas = h;

        anchoMarco = altoCanvas *3 / 100; // Ancho del marco alrededor

        // Creo Vaus
        vaus = new Vaus();
        Bitmap res = BitmapFactory.decodeResource(getResources(),R.drawable.vaus);
        
        // Establezco dimensiones de Vaus en proporción al ancho de la pantalla
        // Ancho de vaus según el ancho del canvas y el escalado
        vaus.setAncho((int)(anchoCanvas * conf.getTamanioVaus() / 100));
        // Alto de Vaus manteniendo la proporción
        vaus.setAlto((vaus.getAncho() * res.getHeight())/ res.getWidth()); 

        // Creo Bola
        bola = new Bola();
        // Esteblezco dimensiones y posición inicial de la bola
        bola.setRadio(vaus.getAlto()/2); // Hago el radio proporcional al alto de Vaus

        // Establezco los límites
        limiteDerecha = anchoCanvas - anchoMarco -  bola.getRadio();
        limiteIzquierda = anchoMarco + bola.getRadio();
        limiteSuperior = anchoMarco + bola.getRadio();
        limiteInferior = altoCanvas + bola.getRadio();

        // Establezco su posición inicial aleatoria
        vaus.setX((int)(Math.random() * (limiteDerecha-vaus.getAncho()-anchoMarco)) + anchoMarco);
        vaus.setY(altoCanvas - anchoMarco*4);
        // Establezco la posición inicial de la bola que sale de la mitad del Vaus
        bola.setX(vaus.getX() + vaus.getAncho()/2);
        bola.setY(vaus.getY() - bola.getRadio());

        // Creo BLOQUES
        block = new Bloque[conf.getNumBloquesAncho()][conf.getNumBloquesAlto()];
        int anchoBloque = (anchoCanvas - anchoMarco * 2)/ conf.getNumBloquesAncho();
        int altoBlque = vaus.getAlto();
        int numColor = 0; // Contador de color para ir asignando los colores secuencialmente
        for (int x = 0; x < conf.getNumBloquesAncho(); x++) {
            for (int y = 0; y < conf.getNumBloquesAlto(); y++) {
                block[x][y] = new Bloque();
                block[x][y].setX(x * anchoBloque + anchoMarco );
                block[x][y].setY(y * altoBlque + anchoMarco);
                block[x][y].setAncho(anchoBloque);
                block[x][y].setAlto(altoBlque);
                block[x][y].setColor(color[(numColor++)%color.length]);
                block[x][y].setVisible(true);
            }
        }
    }

    /**
     * Implementa la parte que redibuja el canvas continuamente.
     * 
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Creo el listener para mover Vaus
        getRootView().setOnTouchListener(this);

        // Dibuja el fondo de la partida
        dibujaFondo(canvas);

        // Dibujo bloques
        dibujaBloques(canvas);

        // Imprimo puntuación y tiempo
        pincel.setColor(Color.WHITE);
        pincel.setTextSize(anchoMarco-6);
        int puntuacion = conf.getNumBloquesAncho() * conf.getNumBloquesAlto() - numBloquesVisibles;
        canvas.drawText("Puntuación: " + puntuacion + "   Tiempo: " + tiempoRestante, anchoMarco, anchoMarco-8, pincel);

        //Dibuja Vaus
        Bitmap res = BitmapFactory.decodeResource(getResources(),R.drawable.vaus);
        Bitmap escalado = Bitmap.createScaledBitmap(res, vaus.getAncho(), vaus.getAlto(), false);
        canvas.drawBitmap(escalado, vaus.getX(), vaus.getY(), null);


        // Dibujo la bola
        pincel.setColor(bola.getColor());
        canvas.drawCircle(bola.getX(), bola.getY(), bola.getRadio(), pincel);

        // Establezco las nuevas coordenadas
        bola.setX(bola.getX() + incX);
        bola.setY(bola.getY() + incY);

        // Comprueba la colisión de la bola con los laterales izquierdo, derecho y superior
        compruebaColisionConLaterales();

        // Comprueba colisión bola con Vaus
        compruebaColisionConVaus();

        // Comprueba colisión bola con bloques
        compruebaColisionConBloques();

        // Comprueba si no quedan bloques
        if (numBloquesVisibles == 0) {
            Toast.makeText(getContext(), "Enhorabuena has ganado!", Toast.LENGTH_LONG).show();
            MediaPlayer player = MediaPlayer.create(getContext(), R.raw.fin);
            player.start();
            finalPartida = true;
        } else {
            // Comprueba la salida de la bola de pantalla por la parte de abajo
            compruebaSalidaPantalla();
        }

    }

    /**
     * Dibuja el fondo que consiste en un fondo rojo en todo el canvas y un rectángulo azul
     * encima de tal forma que se ve el borde rojo alrededor.
     * @param canvas
     */
    private void dibujaFondo(Canvas canvas) {
        // Establezco color de fondo
        canvas.drawColor(getResources().getColor(R.color.colorAccent));

        // Dibujo rectangulo en todo el canvas solo dejo margen alrededor
        // Este rectángulo hace de fondo del juego
        Rect rect = new Rect(anchoMarco,anchoMarco,anchoCanvas - anchoMarco, altoCanvas);
        pincel.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawRect(rect, pincel);
    }

    /**
     * Dibuja en el canvas los bloques visibles.
     *
     * @param canvas
     */
    private void dibujaBloques(Canvas canvas) {
        int separacion = 4; // Separación entre bloques
        for (int x = 0; x < conf.getNumBloquesAncho(); x++) {
            for (int y = 0; y < conf.getNumBloquesAlto(); y++) {
                // Si bloque tiene que estar visible se dibuja y si no lo hace nada
                if (block[x][y].isVisible()) {
                    Rect rectBloque = new Rect(block[x][y].getX() + separacion,
                            block[x][y].getY() + separacion,
                            block[x][y].getX() + block[x][y].getAncho() - separacion,
                            block[x][y].getY() + block[x][y].getAlto() - separacion);
                    pincel.setColor(block[x][y].getColor());
                    canvas.drawRect(rectBloque, pincel);
                }
            }
        }
    }

    /**
     * Comprueba la colisión de la bola con los laterales izquierdo, derecho y superior
     * para realizar el rebote adecuado.
     */
    private void compruebaColisionConLaterales() {
        // Si bola llega al limite izquierdo
        if (bola.getX() < limiteIzquierda) {
            bola.setX(limiteIzquierda);
            incX *= -1;
        }
        // Si bola llega a limite derecho
        if (bola.getX() > limiteDerecha) {
            bola.setX(limiteDerecha);
            incX *= -1;
        }
        // Si bola llega a limite superior
        if (bola.getY() < limiteSuperior) {
            bola.setY(limiteSuperior);
            incY *= -1;
        }
    }

    /**
     * Compruebo la colisión de la bola con Vaus.
     * Vaus tiene cinco zonas de colisión y dependiendo de la zona rebotará en una dirección.
     */
    private void compruebaColisionConVaus() {
        // Si bola esta por debajo de Vaus pero no lo ha pasado
        if ((bola.getY() + bola.getRadio() >= vaus.getY()) && (bola.getY() + bola.getRadio() < vaus.getY() + incY)) {
            bola.setY(vaus.getY()-bola.getRadio()); // dejo bola tocando Vaus

            // Zonas Vaus
            //  ---------------------------
            //  |    |     |   |     |    |
            //  X0   X1    X2  X3   X4   X5
            //  |    |     |   |     |    |
            //  ---------------------------
            int vausX0 = vaus.getX(); // ExtremoDerecho
            int vausX1 = vaus.getX() + vaus.getAncho()/5;
            int vausX2 = vaus.getX() + vaus.getAncho()/2 - bola.getRadio()/2;
            int vausX3 = vaus.getX() + vaus.getAncho()/2 + bola.getRadio()/2;
            int vausX4 = vaus.getX() + vaus.getAncho()*4/5;
            int vausX5 = vaus.getX() + vaus.getAncho(); // Extremo izquierdo


            // Si bola toca Vaus por el extremo izquierdo rebota a la izquierda
            if (bola.getX()+bola.getRadio() >= vausX0 && bola.getX() < vausX1) {
                incY *= -1;
                incX = -conf.getVelocidadBola();
            }
            // Si bola toca Vaus en zona izquierda rebota a derecha
            if (bola.getX() >= vausX1 && bola.getX() <= vausX2) {
                incY *= -1;
                incX = -conf.getVelocidadBola();
            }
            // Si bola toca Vaus en el centro sube recto hacia arriba
            else if (bola.getX() > vausX2 && bola.getX() < vausX3) {
                incY *= -1;
                incX = 0;
            }
            // Si bola toca Vaus en zona derecha rebota a la izquierda
            else if (bola.getX() >= vausX3 && bola.getX() <= vausX4) {
                incY *= -1;
                incX = conf.getVelocidadBola();
            }
            // Si bola toca Vaus por el extremo derecho rebota a la derecha
            else if (bola.getX() > vausX4 && bola.getX()-bola.getRadio() <= vausX5) {
                incY *= -1;
                incX = conf.getVelocidadBola();
            }
        }
    }

    /**
     * Comprueba la colisión de la bola con alguno de los bloques "visibles"
     * Si la bola colisiona con algún bloque,
     *      - el bloque deja de estar visible,
     *      - la bola cambia de color y
     *      - la bola rebota según el borde del bloque con el que haya colisionado
     */
    private void compruebaColisionConBloques() {
        for (int x = 0; x < conf.getNumBloquesAncho(); x++) {
            for (int y = 0; y < conf.getNumBloquesAlto(); y++) {

                // Comprueba solo si el bloque es visible
                if (block[x][y].isVisible()) {
                    // Puntos en ejes X e Y e las esquinas del bloque
                    int x1 = block[x][y].getX();
                    int x2 = block[x][y].getX() + block[x][y].getAncho();
                    int y1 = block[x][y].getY();
                    int y2 = block[x][y].getY() + block[x][y].getAlto();

                    // Comprobaciones
                    // Comprobamos que están tocando el bloque
                    if (bola.getY() - bola.getRadio() <= y2 && bola.getY() + bola.getRadio() >= y1 && bola.getX() + bola.getRadio() >= x1 && bola.getX() - bola.getRadio() <= x2)
                    {
                        // Si toca por la parte de abajo mueve hacia abajo
                        if (bola.getY() >= y2) {
                            incY = conf.getVelocidadBola();
                        }
                        // Si toca por arriba mueve hacia arriba
                        if (bola.getY() <= y1) {
                            incY = -conf.getVelocidadBola();
                        }
                        // Si toca por la izquierda mueve hacia la izquierda
                        if (bola.getX() <= x1) {
                            incX = -conf.getVelocidadBola();
                        }
                        // Si toca por la derecha mueve hacia la derecha
                        if (bola.getX() >= x2) {
                            incX = conf.getVelocidadBola();
                        }
                        MediaPlayer mp = new MediaPlayer();
                        mPlayer = MediaPlayer.create(getContext(), R.raw.laser);
                        mPlayer.start();
                        block[x][y].setVisible(false); // Oculta bloque colisionado
                        numBloquesVisibles--;

                        // La bola toma el color del bloque con el que ha colisionado
                        bola.setColor(block[x][y].getColor());
                    }
                }
            }
        }
    }

    /**
     * Comprueba la salida de la bola de la pantalla por la parte de abajo.
     * Si la bola se sale por abjo pone final de partida a true.
     */
    private void compruebaSalidaPantalla() {
        // Si se ha pasado el límite inferior termina la partida
        if (bola.getY() > limiteInferior) {
            Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();
            MediaPlayer player = MediaPlayer.create(getContext(), R.raw.game_over);
            player.start();
            finalPartida = true;
        }
    }

    /**
     * Implementa la captura del evento de toque en la pantalla.
     * Si se toca a la izquierda de Vaus se mueve a la izquierda y si se toca a la derecha va la
     * de derecha. Toma un toque cada vez que el dedo se mueve con lo cual simula bien el arrastrado
     * de Vaus.
     *
     * @param view
     * @param evento
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent evento) {

        float x = evento.getX();

        // Si el toque está a la izquierda
        if (x < vaus.getX() + vaus.getAncho()/2) {
            vaus.setX(vaus.getX()-conf.getVelocidadVaus());
            if (vaus.getX() < anchoMarco) {
                vaus.setX(anchoMarco);
            }
        }
        // Sino el toque está a la derecha
        else {
            vaus.setX(vaus.getX()+conf.getVelocidadVaus());
            if (vaus.getX() + vaus.getAncho() > anchoCanvas-anchoMarco) {
                vaus.setX(anchoCanvas - anchoMarco - vaus.getAncho());
            }
        }

        return true;
    }

    /**
     * Establece el tiempo restante de la partida en el atributo tiempoRestante
     * que se utiliza para visualizar el tiempo restante
     * @param tiempoRestante
     */
    public void setTiempo(long tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    /**
     * @return Devuelve true si se ha llegado al final de la partida
     */
    public boolean isFinal() {
        return finalPartida;
    }
}
