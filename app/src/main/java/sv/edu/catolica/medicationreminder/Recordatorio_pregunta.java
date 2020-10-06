package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Recordatorio_pregunta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio_pregunta);
        getSupportActionBar().hide();
    }

    public void MedicamentoP (View v)
    {
        Intent medicamento = new Intent(Recordatorio_pregunta.this,medicamento_recordatorio.class);
        startActivity(medicamento);
    }
    public void HistorialP (View v)
    {

    }
}