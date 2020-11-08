package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class Recordatorio_pregunta extends AppCompatActivity {

    private String info;
    String Re_cod,Per_cod,Re_titulo,Re_f_inicio,Re_i_mdh,Re_i_valor,Re_estado;
    Date Re_f_final;
    ManejadorBD admin;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio_pregunta);

        Bundle extras = getIntent().getExtras();
        info=extras.getString("Info");

        String[] parts = info.split(",");
        String part1 = parts[0];//RE_COD

        String[] porPuntos = part1.split(": ");
        Re_cod = porPuntos[1];
        llenarValores();

    }
    public void llenarValores(){
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT RE_COD,PER_COD,RE_TITULO,RE_F_INICIO,RE_INTERVALO_MDH,RE_INTERVALO_VALOR,RE_F_FINAL,RE_ESTADO FROM RECORDATORIO " +
                        "WHERE RE_COD = '" + Re_cod + "'"
                ,null);

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        while (fila.moveToNext()) {
            Re_cod = fila.getString(0);
            Per_cod = fila.getString(1);

        }

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