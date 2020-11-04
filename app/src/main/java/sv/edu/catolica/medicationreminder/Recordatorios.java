package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recordatorios extends AppCompatActivity {
    ManejadorBD admin;
    SQLiteDatabase db;
    private ListView lvRecordatorio;
    private EditText etBuscarRecordatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorios);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
        lvRecordatorio = findViewById(R.id.lvRecordar);
        etBuscarRecordatorio = findViewById(R.id.etBuscarRecordatorio);

        llenarListView();

        etBuscarRecordatorio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                llenarListView();
            }
        });
    }

    //Método de inicio de actividad
    public void llenarListView(){
        if (!etBuscarRecordatorio.getText().toString().trim().isEmpty()){
            db = admin.getWritableDatabase();
            Cursor fila = db.rawQuery("SELECT RE_TITULO, RE_F_INICIO, RE_INTERVALO_MDH, RE_INTERVALO_VALOR, RE_F_FINAL, RE_ESTADO FROM RECORDATORIO " +
                            "WHERE RE_TITULO like '%"+etBuscarRecordatorio.getText().toString().trim()+"%' ORDER BY RE_TITULO"
                    ,null);

            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            while (fila.moveToNext()){
                Map<String, String> datum = new HashMap<String, String>(5);
                datum.put("titulo", fila.getString(0));
                datum.put("inicio", "Fecha de inicio: " + fila.getString(1));
                datum.put("intervalo", "Intervalo: " + fila.getString(3) + " " + fila.getString(2));
                datum.put("final", "Fecha final: " + fila.getString(4));
                datum.put("estado", "Estado: " + fila.getString(5));
                data.add(datum);
            }

            db.close();

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item,
                    new String[] {"titulo", "inicio", "intervalo", "final", "estado"},
                    new int[] {R.id.list_Principal,
                            R.id.list_Fecha,
                            R.id.list_Intervalo,
                            R.id.list_FechaF,
                            R.id.list_Estado});

            lvRecordatorio.setAdapter(adapter);
        }else{
            db = admin.getWritableDatabase();
            Cursor fila = db.rawQuery("SELECT RE_TITULO, RE_F_INICIO, RE_INTERVALO_MDH, RE_INTERVALO_VALOR, RE_F_FINAL, RE_ESTADO FROM RECORDATORIO ORDER BY RE_TITULO"
                    ,null);

            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            while (fila.moveToNext()){
                Map<String, String> datum = new HashMap<String, String>(5);
                datum.put("titulo", fila.getString(0));
                datum.put("inicio", "Fecha de inicio: " + fila.getString(1));
                datum.put("intervalo", "Intervalo: " + fila.getString(3) + " "+ fila.getString(2));
                datum.put("final", "Fecha final: " + fila.getString(4));
                datum.put("estado", "Estado: " + fila.getString(5));
                data.add(datum);
            }

            db.close();

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item,
                    new String[] {"titulo", "inicio", "intervalo", "final", "estado"},
                    new int[] {R.id.list_Principal,
                            R.id.list_Fecha,
                            R.id.list_Intervalo,
                            R.id.list_FechaF,
                            R.id.list_Estado});

            lvRecordatorio.setAdapter(adapter);
        }
    }

    public void nuevoRecordatorio(View view) {
        finish();
        Intent nuevoRecord = new Intent(getApplicationContext(), Add_Recordatorios.class);
        startActivity(nuevoRecord);
    }
}