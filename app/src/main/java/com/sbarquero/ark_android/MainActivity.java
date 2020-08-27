package com.sbarquero.ark_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Tarea PMDM04-2 ARK-Android
 *
 * Este activity muesta la pantalla inicial con los botones Jugar, Configurar y Salir
 *
 * @author Santiago Barquero - 2º DAM
 * @version 2020-03-03
 */
public class MainActivity extends AppCompatActivity {

    private static MediaPlayer player;

    /**
     * Crea Activity y establece los listener de los botones
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asocio con IU
        Button btnJugar = (Button)findViewById(R.id.btnJugar);
        Button btnSalir = (Button)findViewById(R.id.btnSalir);
        Button btnConfigurar = (Button)findViewById(R.id.btnConfigurar);

        // Botón JUGAR
        btnJugar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                player.stop();
                Intent intentJugar = new Intent(view.getContext(), PartidaActivity.class);
                startActivity(intentJugar);
            }
        });

        // Botón CONFIGURACION
        btnConfigurar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentConfigurar = new Intent(view.getContext(), ConfigurarActivity.class);
                startActivity(intentConfigurar);
            }
        });


        // Botón SALIR
        btnSalir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmaSalida(view.getContext());
                }
            }
        );

    }


    /**
     * Intercepto la pulsación del botón retroceso para pedir confirmación de la salida.
     */
    @Override
    public void onBackPressed() {
        confirmaSalida(this);
    }

    /**
     * Al reanudar el Activity inicio de nuevo el reproductor
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        player = MediaPlayer.create(this, R.raw.intro);
        player.start();
    }

    /**
     * Al abandonar el Activity paro el reproductor
     */
    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
    }

    /**
     * Muestra un AlertDialog para confirmar la salida de la aplicación.
     *
     * @param context
     */
    private void confirmaSalida(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.mensaje_salir)
                .setCancelable(false)
                // Si confirmo salida, paro el reproductor y cierro
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        player.stop();
                        finish();
                    }
                })
                // Si no confirmo salida no hago nada
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(R.string.salir);
        alert.show();
    }


}
