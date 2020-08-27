package com.sbarquero.ark_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity para configurar el juego.
 *
 * @author Santiago Barquero - 2º DAM
 * @version 2020-03-03
 */
public class ConfigurarActivity extends AppCompatActivity {

    EditText edDuracionPartida;
    EditText edVelocidadBola;
    EditText edNumBloquesAncho; // Nº de bloques a lo ancho de la pantalla
    EditText edNumBloquesAlto;  // Nº de bloques a lo largo de la pantalla
    EditText edVelocidadVaus;
    EditText edTamanioVaus;

    Button btnRestablecer;
    Button btnGuardar;

    SharedPreferences prefs;

    /**
     * Cargo la configuración de las SharedPreferences en caso de no existir
     * carga unos valores por defecto.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar);

        // Asociación con IU
        edDuracionPartida = (EditText)findViewById(R.id.edDuracionPartida);
        edVelocidadBola = (EditText)findViewById(R.id.edVelocidadBola);
        edNumBloquesAncho = (EditText)findViewById(R.id.edNumBloquesAncho);
        edNumBloquesAlto = (EditText)findViewById(R.id.edNumBloquesAlto);
        edVelocidadVaus = (EditText)findViewById(R.id.edVelocidadVaus);
        edTamanioVaus = (EditText)findViewById(R.id.edTamanioVaus);
        btnRestablecer = (Button)findViewById(R.id.btnRestablecer);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);

        // Carga configuración de las SharedPreferences

        prefs = getApplication().getSharedPreferences("Configuracion", MODE_PRIVATE);
        edDuracionPartida.setText(String.valueOf(prefs.getInt("DuracionPartida", 100)));
        edVelocidadBola.setText(String.valueOf(prefs.getInt("VelocidadBola", 10)));
        edNumBloquesAncho.setText(String.valueOf(prefs.getInt("NumBloquesAncho", 5)));
        edNumBloquesAlto.setText(String.valueOf(prefs.getInt("NumBloquesAlto", 6)));
        edVelocidadVaus.setText(String.valueOf(prefs.getInt("VelocidadVaus", 20)));
        edTamanioVaus.setText(String.valueOf(prefs.getInt("TamanioVaus", 30)));
    }

    /**
     * El botón Restablecer establece unos valores por defecto
     * @param view
     */
    public void onClickRestablecer(View view) {
        edDuracionPartida.setText("100");
        edVelocidadBola.setText("10");
        edNumBloquesAncho.setText("5");
        edNumBloquesAlto.setText("6");
        edVelocidadVaus.setText("20");
        edTamanioVaus.setText("30");
    }

    /**
     * En el botón guardar hago las comprobaciones antes de guardar.
     * En caso de un valor erróneo informo con un toast y guardo un valor por defecto.
     * @param view
     */
    public void onClickGuardar(View view) {
        SharedPreferences.Editor editor = prefs.edit();

        // Comprobaciones
        // Compruebo duración partida introducida
        int duracionPartida = Integer.parseInt((edDuracionPartida.getText().toString()));
        if (duracionPartida < 10) {
            Toast.makeText(this, R.string.duracion_minima, Toast.LENGTH_SHORT).show();
            duracionPartida = 60;
        }
        editor.putInt("DuracionPartida", duracionPartida);

        // Compruebo velocidad bola introducida
        int velocidadBola = Integer.parseInt(edVelocidadBola.getText().toString());
        if (velocidadBola < 1 || velocidadBola > 50) {
            Toast.makeText(this, R.string.velocidad_bola_error, Toast.LENGTH_SHORT).show();
            velocidadBola = 10;
        }
        editor.putInt("VelocidadBola", velocidadBola);

        // Compruebo el número de bloques de ancho introducidos
        int numBloquesAncho = Integer.parseInt(edNumBloquesAncho.getText().toString());
        if (numBloquesAncho < 1 || numBloquesAncho > 10) {
            Toast.makeText(this, R.string.num_bloques_ancho_error, Toast.LENGTH_SHORT).show();
            numBloquesAncho = 5;
        }
        editor.putInt("NumBloquesAncho", numBloquesAncho);

        // Compruebo el número de bloques de alto introcidos
        int numBloquesAlto = Integer.parseInt(edNumBloquesAlto.getText().toString());
        if (numBloquesAlto < 1 || numBloquesAlto > 10) {
            Toast.makeText(this, R.string.num_bloques_alto_error, Toast.LENGTH_SHORT).show();
            numBloquesAlto = 10;
        }
        editor.putInt("NumBloquesAlto", numBloquesAlto);

        // Compruebo la velocidad de Vaus introducida
        int velocidadVaus = Integer.parseInt(edVelocidadVaus.getText().toString());
        if (velocidadVaus < 1 || velocidadVaus > 50) {
            Toast.makeText(this, R.string.velocidad_vaus_error, Toast.LENGTH_SHORT).show();
            velocidadVaus = 15;
        }
        editor.putInt("VelocidadVaus", velocidadVaus);

        // Compruebo el tamaño de Vaus introducido
        int tamanioVaus = Integer.parseInt(edTamanioVaus.getText().toString());
        if (tamanioVaus < 10 || tamanioVaus > 30) {
            Toast.makeText(this, R.string.tamanio_vaus_error, Toast.LENGTH_SHORT).show();
            tamanioVaus = 15;
        }
        editor.putInt("TamanioVaus", tamanioVaus);

        editor.commit();
        finish();
    }
}
