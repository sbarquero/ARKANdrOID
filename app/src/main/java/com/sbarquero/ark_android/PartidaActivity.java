package com.sbarquero.ark_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Gestiona la partida del juego.
 * Crea miCanvas y un temporizador y lo va visualizando cada intervalo de tiempo establecido
 * hasta que:
 *   - se agota el tiempo de la partida,
 *   - ha habido una salida de bola (Game over) o
 *   - se ha ganado la partida
 *
 * @author Santiago Barquero - 2º DAM
 * @version 2020-03-03
 */
public class PartidaActivity extends AppCompatActivity {

    MiCanvas miCanvas;
    MediaPlayer mPlayer;
    CountDownTimer countDownTimer; // Objeto para la cuenta atrás
    Configuracion conf; // Objeto que guarda configuración del juego

    /**
     * Al iniciar la partida
     *   - leo la configuración del juego
     *   - reproduzco música
     *   - creo canvas y establezco un contador en que se va redibujando el canvas
     *     hasta que se agote el tiempo y se termine la partida
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Leo la configuración de las SharedPreferences
        conf = leerConfiguracion();

        // Reproduce música
        mPlayer = MediaPlayer.create(this, R.raw.arkandroid);
        mPlayer.start();

        // Creo mi canvas que redibujaré cada intervalo de tiempo
        miCanvas = new MiCanvas(this, conf);

        // Contador de tiempo de duración de partida
        countDownTimer = new CountDownTimer(conf.getDuracionPartida(), 10)
        {
            public void onTick(long millisUntilFinished)
            {
                // Si ha finalizado la partida cancelo el contador
                if (miCanvas.isFinal()) {
                    countDownTimer.cancel();
                    mPlayer.stop();
                }
                // Envío tiempo restante al canvas
                miCanvas.setTiempo(millisUntilFinished/1000);
                // Redibuja Canvas
                setContentView(miCanvas);
            }

            // Cunado finaliza el tiempo muestro mensaje de tiempo agotado
            public void onFinish()
            {
                Toast.makeText(getBaseContext(), R.string.tiempo_agotado , Toast.LENGTH_LONG).show();
                // Reproduce sonido game over
                mPlayer = MediaPlayer.create(getBaseContext(), R.raw.game_over);
                mPlayer.start();
            }

        }.start();

    }

    /**
     * Al pulsar el botón de retroceso cancelo el contador de la partida,
     * pauso la música y finalizo el Activity
     */
    @Override
    public void onBackPressed()
    {
        countDownTimer.cancel();
        mPlayer.stop();
        finish();
    }

    /**
     * Pauso la música al parar el Fragment
     */
    @Override
    protected void onStop() {
        super.onStop();
        mPlayer.stop();
    }

    /**
     * Leo configuración de las SharedPreferences
     *
     * @return Devuelve objeto con la configuración del juego
     */
    private Configuracion leerConfiguracion() {
        Configuracion conf = new Configuracion();

        SharedPreferences prefs = getApplication().getSharedPreferences("Configuracion", MODE_PRIVATE);

        conf.setDuracionPartida(prefs.getInt("DuracionPartida", 100) * 1000);
        conf.setVelocidadBola(prefs.getInt("VelocidadBola", 10));
        conf.setNumBloquesAncho(prefs.getInt("NumBloquesAncho", 5));
        conf.setNumBloquesAlto(prefs.getInt("NumBloquesAlto", 6));
        conf.setVelocidadVaus(prefs.getInt("VelocidadVaus", 20));
        conf.setTamanioVaus(prefs.getInt("TamanioVaus", 30));

        return conf;
    }

}
