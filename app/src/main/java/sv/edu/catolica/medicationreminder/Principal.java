package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Principal extends AppCompatActivity {
    private ImageView ivPersona, ivRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        ivPersona = findViewById(R.id.ivPersona);
        ivRecord = findViewById(R.id.ivRecor);
    }

    public void persona(View v){
        Intent perso = new Intent(Principal.this, Personas.class);
        startActivity(perso);
    }

    public void recordatorio(View v){
        Intent recor = new Intent(Principal.this, Recordatorios.class);
        startActivity(recor);
    }
}