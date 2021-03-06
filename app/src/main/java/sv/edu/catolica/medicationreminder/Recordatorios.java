package sv.edu.catolica.medicationreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recordatorios extends AppCompatActivity {
    ManejadorBD admin;
    SQLiteDatabase db;
    private ListView lvRecordatorio;
    private EditText etBuscarRecordatorio;
    String persona_id;
    FloatingActionButton boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorios);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
        lvRecordatorio = findViewById(R.id.lvRecordar);
        etBuscarRecordatorio = findViewById(R.id.etBuscarRecordatorio);
        boton = findViewById(R.id.addRecordatorios);
        boton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pGris)));

        //Obtener id de la persona de la actividad Persona
        Bundle extras = getIntent().getExtras();
        persona_id = extras.getString("persona_id");

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

        registerForContextMenu(lvRecordatorio);

        lvRecordatorio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                finish();
                Intent pregunta = new Intent(Recordatorios.this,Recordatorio_pregunta.class);
                pregunta.putExtra("Info",lvRecordatorio.getAdapter().getItem(i).toString());
                startActivity(pregunta);
            }
        });
    }

    //Crear menuRecordatorio
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflador = getMenuInflater();

        if(v.getId()==R.id.lvRecordar){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            inflador.inflate(R.menu.menurecordatorio, menu);
        }
    }

    //Acciones a realizar dependiendo de la opción seleccionada
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.listaRecordatorioEditar:
                try {
                    finish();
                    Intent EditarRec = new Intent(Recordatorios.this,Editar_Recordatorio.class);
                    EditarRec.putExtra("Info",lvRecordatorio.getAdapter().getItem(info.position).toString());
                    startActivity(EditarRec);
                    break;
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.toString()+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            default:
        }
        return super.onContextItemSelected(item);
    }




    //Método de inicio de actividad
    public void llenarListView(){
        String intervalo = "";
        String estado ="";
        if (!etBuscarRecordatorio.getText().toString().trim().isEmpty()){
            db = admin.getWritableDatabase();
            Cursor fila = db.rawQuery("SELECT RE_COD, RE_TITULO, RE_F_INICIO, RE_INTERVALO_MDH, RE_INTERVALO_VALOR, RE_F_FINAL, RE_ESTADO FROM RECORDATORIO " +
                            "WHERE PER_COD = '" + persona_id + "' AND RE_TITULO like '%"+etBuscarRecordatorio.getText().toString().trim()+"%' " +
                            "ORDER BY RE_TITULO"
                    ,null);

            List<Map<String, String>> data = new ArrayList<Map<String, String>>();

            while (fila.moveToNext()){
                switch (fila.getString(3)) {
                    case "1":
                        intervalo = getString(R.string.minutos);
                        break;
                    case "2":
                        intervalo = getString(R.string.horas);
                        break;
                    case "3":
                        intervalo = getString(R.string.dias);
                        break;
                    case "4":
                        intervalo = getString(R.string.semanas);
                        break;
                    default:
                }

                switch (fila.getString(6)) {
                    case "1":
                        estado= getString(R.string.en_curso);
                        break;
                    case "2":
                        estado = getString(R.string.terminado);
                        break;
                    default:
                }

                Map<String, String> datum = new HashMap<String, String>(6);
                datum.put("id", fila.getString(0));
                datum.put("titulo", fila.getString(1));
                datum.put("inicio", getString(R.string.Fecha_inicio) + fila.getString(2));
                datum.put("intervalo", getString(R.string.intervalo) + fila.getString(4) + " "+ intervalo);
                datum.put("final",  getString(R.string.fecha_final) +": "+ fila.getString(5));
                datum.put("estado", getString(R.string.estado2) + estado);
            }

            db.close();

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item,
                    new String[] {"id","titulo", "inicio", "intervalo", "final", "estado"},
                    new int[] {R.id.list_ID,
                            R.id.list_Principal,
                            R.id.list_Fecha,
                            R.id.list_Intervalo,
                            R.id.list_FechaF,
                            R.id.list_Estado});

            lvRecordatorio.setAdapter(adapter);
        }else{
            db = admin.getWritableDatabase();
            Cursor fila = db.rawQuery("SELECT RE_COD, RE_TITULO, RE_F_INICIO, RE_INTERVALO_MDH, RE_INTERVALO_VALOR, RE_F_FINAL, RE_ESTADO " +
                            "FROM RECORDATORIO WHERE PER_COD = '" + persona_id + "' ORDER BY RE_TITULO"
                    ,null);

            List<Map<String, String>> data = new ArrayList<Map<String, String>>();



            while (fila.moveToNext()){
                switch (fila.getString(3)) {
                    case "1":
                        intervalo = getString(R.string.minutos);
                        break;
                    case "2":
                        intervalo = getString(R.string.horas);
                        break;
                    case "3":
                        intervalo = getString(R.string.dias);
                        break;
                    case "4":
                        intervalo = getString(R.string.semanas);
                        break;
                    default:
                }

                switch (fila.getString(6)) {
                    case "1":
                        estado= getString(R.string.en_curso);
                        break;
                    case "2":
                        estado = getString(R.string.terminado);
                        break;
                    default:
                }

                Map<String, String> datum = new HashMap<String, String>(6);
                datum.put("id", fila.getString(0));
                datum.put("titulo", fila.getString(1));
                datum.put("inicio", getString(R.string.Fecha_inicio) + fila.getString(2));
                datum.put("intervalo", getString(R.string.intervalo) + fila.getString(4) + " "+ intervalo);
                datum.put("final",  getString(R.string.fecha_final) +": "+ fila.getString(5));
                datum.put("estado", getString(R.string.estado2) + estado);
                data.add(datum);
            }

            db.close();

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item,
                    new String[] {"id","titulo", "inicio", "intervalo", "final", "estado"},
                    new int[] {R.id.list_ID,
                            R.id.list_Principal,
                            R.id.list_Fecha,
                            R.id.list_Intervalo,
                            R.id.list_FechaF,
                            R.id.list_Estado});

            lvRecordatorio.setAdapter(adapter);
        }
    }

    //Actividad nuevo recordatorio
    public void nuevoRecordatorio(View view) {
        finish();
        Intent nuevoRecord = new Intent(getApplicationContext(), Add_Recordatorios.class);
        nuevoRecord.putExtra("PER_COD",persona_id);
        startActivity(nuevoRecord);
    }

    //Al presionar el botón Atrás
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            Intent persona = new Intent(getApplicationContext(), Personas.class);
            startActivity(persona);
        }
        return super.onKeyDown(keyCode, event);
    }
}