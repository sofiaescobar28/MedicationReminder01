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
import android.widget.TextView;

public class Add_Personas extends AppCompatActivity {
    private EditText NombrePersona;
    private TextView validacion;
    ManejadorBD admin;
    SQLiteDatabase db;
    int filaAfectadas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__personas);
        NombrePersona=findViewById(R.id.etNombrePersona);
        validacion=findViewById(R.id.lblvalidarpersona);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
    }

    public void insertarPersona(View view) {
        if (NombrePersona.getText().toString().trim().isEmpty() )
        {
            validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText(getText(R.string.campos_vacios));
        }else{
            int count =getCountPersona();
            ContentValues registro = new ContentValues();
            if (count>0){
                registro.put("PER_COD",(count));

            }else{
                registro.put("PER_COD",1);
            }
            registro.put("PER_NOMBRE",NombrePersona.getText().toString());

            db = admin.getWritableDatabase();
            filaAfectadas = (int) db.insert("PERSONA", null, registro);

            if (filaAfectadas !=-1){
                validacion.setTextColor(getColor(R.color.verde));
                validacion.setText(R.string.persona_guardada);
               NombrePersona.setText("");
            }else
            {
                validacion.setTextColor(getColor(R.color.rojo));
                validacion.setText(getText(R.string.error_guardar));
            }
            db.close();
        }
    }

    public void Cancelar(View view) {
        Intent ventana= new Intent(getApplicationContext(),Personas.class);
        startActivity(ventana);
    }
    public int getCountPersona(){
        db = admin.getWritableDatabase();
        int num=-1;
        Cursor fila = db.rawQuery("SELECT PER_COD FROM PERSONA" +
                " ORDER BY PER_COD DESC"+
                " LIMIT 1;",null);

        if (fila.moveToFirst()){
            num=fila.getInt(0);
            num++;

        }else   {
            num = 1;
           /* validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText(R.string.no_hay_elementos);*/
        }
        db.close();
        return num;

    }

    //Al presionar el botón Atrás
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            Intent ventana= new Intent(getApplicationContext(), Personas.class);
            startActivity(ventana);
        }
        return super.onKeyDown(keyCode, event);
    }
}