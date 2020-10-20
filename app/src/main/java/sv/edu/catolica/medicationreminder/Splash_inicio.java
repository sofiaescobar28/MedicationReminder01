package sv.edu.catolica.medicationreminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash_inicio extends AppCompatActivity {
    private boolean botonBackPresionado = false;
    private static final int DURACION_SPLASH = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_inicio);

        Handler controlador = new Handler();
        controlador.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                if (!botonBackPresionado){
                    Intent principal = new Intent(Splash_inicio.this, Principal.class);
                    startActivity(principal);
                }
            }
        }, DURACION_SPLASH);
    }

    @Override
    public void onBackPressed() {
        botonBackPresionado=true;
        super.onBackPressed();
    }
}