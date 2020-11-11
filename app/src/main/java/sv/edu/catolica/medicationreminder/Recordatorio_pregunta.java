package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;



public class Recordatorio_pregunta extends AppCompatActivity {

    private String info;
    String Re_cod,Per_cod;
    ManejadorBD admin;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio_pregunta);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);

        Bundle extras = getIntent().getExtras();
        info=extras.getString("Info");

        String[] parts = info.split(",");
        String part1 = parts[4];//RE_COD es el elemento 4

        String[] porPuntos = part1.split("=");
        Re_cod = porPuntos[1];
        llenarValores();

    }
    public void llenarValores(){
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT RE_COD, PER_COD FROM RECORDATORIO " +
                        "WHERE RE_COD = " + Re_cod + ";"
                ,null);
        while (fila.moveToNext()) {
            Re_cod = fila.getString(0);
            Per_cod = fila.getString(1);
        }
    }

    public void MedicamentoP (View v)
    {
        finish();
        Intent medicamento = new Intent(Recordatorio_pregunta.this,medicamento_recordatorio.class);
        medicamento.putExtra("RE_COD",Re_cod);
        medicamento.putExtra("PER_COD",Per_cod);
        startActivity(medicamento);
    }
    public void HistorialR (View v)
    {
        finish();
        Intent historial = new Intent(Recordatorio_pregunta.this,Historial.class);
        historial.putExtra("RE_COD",Re_cod);
        historial.putExtra("PER_COD",Per_cod);
        startActivity(historial);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent recordatorios = new Intent(Recordatorio_pregunta.this,Recordatorios.class);
        recordatorios.putExtra("persona_id",Per_cod);
        startActivity(recordatorios);
    }
}