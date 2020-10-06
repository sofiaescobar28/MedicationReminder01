package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class Add_Recordatorios extends AppCompatActivity {
    private EditText etTit,etValor,etFF;
    private Spinner spnMDH,spnEstado;
    private RadioButton radPerm,radFFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__recordatorios);
        getSupportActionBar().hide();

        etTit= findViewById(R.id.etTitRecordatorio);
        etValor = findViewById(R.id.etValorIntervalo);
        etFF = findViewById(R.id.etFechaFin);
        spnMDH = findViewById(R.id.spnMDH);
        spnEstado = findViewById(R.id.spnEstado);
        radPerm = findViewById(R.id.radPermanente);
        radFFinal = findViewById(R.id.radFechaFinal);

        etFF.setEnabled(false);
    }

    public void Permanente (View v)
    {
        etFF.setEnabled(false);
        etFF.setText(null);
    }
    public void noPermanente (View v)
    {
        etFF.setEnabled(true);
    }
}