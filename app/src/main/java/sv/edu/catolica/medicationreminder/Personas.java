package sv.edu.catolica.medicationreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

public class Personas extends AppCompatActivity {
    ManejadorBD admin;
    SQLiteDatabase db;
    private ListView lvPersona;
    private EditText etBuscarPersona;
    FloatingActionButton boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
        lvPersona = findViewById(R.id.lvPersona);
        etBuscarPersona = findViewById(R.id.etBuscarPersona);
        boton = findViewById(R.id.addPersonas);
        boton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.pGris)));

        //Llenar la lista
        buscarPersonaNombres();

        //Búsqueda
        etBuscarPersona.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                buscarPersonaNombres();
            }
        });

        //Asignar el menú contextual a la lista
        registerForContextMenu(lvPersona);

        lvPersona.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                recordatoriosPersona(lvPersona.getAdapter().getItem(i).toString());
            }
        });
    }

    //Método para traer todos los nombres
    public ArrayList<String> BuscarPersona(){
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT PER_NOMBRE FROM PERSONA ORDER BY PER_NOMBRE"
                ,null);
        ArrayList<String> pers = new ArrayList<String>();
        String nombre;

        while (fila.moveToNext()){
            nombre = fila.getString(0);
            pers.add(nombre);
        }

        db.close();
        return pers;
    }

    //Método para traer lista de nombres específicos
    public  void buscarPersonaNombres() {
        if (!etBuscarPersona.getText().toString().trim().isEmpty()){

            db = admin.getWritableDatabase();
            Cursor fila = db.rawQuery("SELECT PER_NOMBRE FROM PERSONA" +
                    " WHERE PER_NOMBRE like '%"+etBuscarPersona.getText().toString().trim()+"%' ORDER BY PER_NOMBRE",null);
            ArrayList<String> persona = new ArrayList<String>();
            String nombre;

            while (fila.moveToNext()){
                nombre = fila.getString(0);
                persona.add(nombre);
            }

            db.close();
            String [] datos = persona.toArray(new String[0]);
            ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
            lvPersona.setAdapter(adaptador);

        }else{
            ArrayList<String> persona = new ArrayList<String>();
            persona =BuscarPersona();
            if (persona.size()>0){
                String [] datos = persona.toArray(new String[0]);
                ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
                lvPersona.setAdapter(adaptador);
            }else{
                Toast.makeText(getApplicationContext(), getText(R.string.busquedanoencontrada), Toast.LENGTH_LONG).show();
            }
        }
    }

    //Método para eliminar una persona
    public void EliminarPersona(String nombre) {
       db = admin.getWritableDatabase();

        int fila = db.delete("PERSONA", "PER_NOMBRE='" + nombre + "'", null);
        if (fila == 1){
            Toast.makeText(getApplicationContext(), getText(R.string.persona_delete), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), getText(R.string.error_eliminar), Toast.LENGTH_LONG).show();
        }

        db.close();
        buscarPersonaNombres();
    }

    //Crear menuPersona
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflador = getMenuInflater();

        if(v.getId()==R.id.lvPersona){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(lvPersona.getAdapter().getItem(info.position).toString());
            inflador.inflate(R.menu.menupersona, menu);
        }
    }

    //Acciones a realizar dependiendo de la opción seleccionada
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.listaEditar:
                editarPersona(lvPersona.getAdapter().getItem(info.position).toString());
                break;
            case R.id.listaEliminar:
                Interrogativo(lvPersona.getAdapter().getItem(info.position).toString());
                break;
            default:
        }
        return super.onContextItemSelected(item);
    }

    //Enviar datos para ver recordatorios
    public void recordatoriosPersona(String nombre){
        db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery("SELECT PER_COD, PER_NOMBRE FROM PERSONA" +
                " WHERE PER_NOMBRE = '" + nombre + "'",null);

        if (fila.moveToFirst()){
            Intent recordatorios_persona = new Intent(Personas.this, Recordatorios.class);
            recordatorios_persona.putExtra("persona_id", fila.getString(0));
            startActivityForResult(recordatorios_persona,777);
        }else{
            Toast.makeText(getApplicationContext(), getText(R.string.error_seleccionar), Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    //Enviar datos para editar un nombre
    public void editarPersona(String nombre){
        db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery("SELECT PER_COD, PER_NOMBRE FROM PERSONA" +
                " WHERE PER_NOMBRE = '" + nombre + "'",null);

        if (fila.moveToFirst()){
            Intent add_persona = new Intent(Personas.this, Editar_Personas.class);
            add_persona.putExtra("persona_id", fila.getString(0));
            add_persona.putExtra("persona_nombre", fila.getString(1));
            startActivityForResult(add_persona,777);
        }else{
            Toast.makeText(getApplicationContext(), R.string.error_seleccionar, Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    //Método de notificación para eliminar persona
    public void Interrogativo(final String nombre){
        AlertDialog.Builder ventanita = new AlertDialog.Builder(this);
        ventanita.setMessage(R.string.mensaje_eliminar_persona);
        ventanita.setTitle(R.string.titulo_eliminar_persona);
        ventanita.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EliminarPersona(nombre);
            }
        });
        ventanita.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ventanita.create();
        ventanita.show();
    }

    //Al presionar el botón Atrás
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            Intent principal = new Intent(Personas.this, Principal.class);
            startActivity(principal);
        }
        return super.onKeyDown(keyCode, event);
    }

    //Abrir la pantalla Add_Persona
    public void persona(View v){
        finish();
        Intent perso = new Intent(Personas.this, Add_Personas.class);
        startActivity(perso);
    }
}