package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Editar_Personas extends AppCompatActivity {
    ManejadorBD admin;
    SQLiteDatabase db;
    private EditText etNombrePersonaEditar;
    String persona_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar__personas);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
        etNombrePersonaEditar = findViewById(R.id.etNombrePersonaEditar);

        //Obtener nombre de la persona de la actividad Persona
        Bundle extras = getIntent().getExtras();
        persona_id = extras.getString("persona_id");
        String persona_nombre = extras.getString("persona_nombre");

        etNombrePersonaEditar.setText(persona_nombre);
    }

    //Metodo para verificar si el nombre de la persona ya existe
    public int verificarPersona(String nombre){
        db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery("SELECT PER_COD, PER_NOMBRE FROM PERSONA" +
                " WHERE PER_NOMBRE = '" + nombre + "' AND PER_COD != '" + persona_id + "'",null);

        int valor = 0;
        if (fila.moveToFirst()){
            valor = 1;
        }
        db.close();
        return valor;
    }

    //Metodo para actualizar el nombre de la persona
    public void Actualizar(String nombre) {
        db = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("PER_NOMBRE", nombre);
        int fila = db.update("PERSONA", registro, "PER_COD='" + persona_id + "'", null);
        if (fila == 1){
            Toast.makeText(getApplicationContext(), getText(R.string.update_ok), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), getText(R.string.error_update), Toast.LENGTH_LONG).show();
        }

        db.close();
    }

    //Boton para actualizar nombre
    public void updatePersona(View view) {
        if(etNombrePersonaEditar.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(), getText(R.string.campos_vacios), Toast.LENGTH_LONG).show();
        }else{
            if (verificarPersona(etNombrePersonaEditar.getText().toString().trim()) > 0){
                Toast.makeText(getApplicationContext(), getText(R.string.persona_existe), Toast.LENGTH_LONG).show();
            }else{
                Actualizar(etNombrePersonaEditar.getText().toString().trim());
            }
        }
    }

    //Salir de la actividad y regresar a Personas
    public void Cancelar(View v){
        finish();
        Intent ventana= new Intent(getApplicationContext(), Personas.class);
        startActivity(ventana);
    }

    //Al presionar el botón Atrás
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            finish();
            Intent ventana= new Intent(getApplicationContext(), Personas.class);
            startActivity(ventana);
        }
        return super.onKeyDown(keyCode, event);
    }
}