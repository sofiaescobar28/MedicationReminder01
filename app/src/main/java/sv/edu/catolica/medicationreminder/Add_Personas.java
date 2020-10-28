package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
            validacion.setText("Los campos no pueden quedar en blanco.");
        }else{
            int count =getCountPersona();
            ContentValues registro = new ContentValues();
            if (count>0){
                registro.put("PER_COD",(count+1));

            }else{
                registro.put("PER_COD",1);
            }
            registro.put("PER_NOMBRE",NombrePersona.getText().toString());

            db = admin.getWritableDatabase();
            filaAfectadas = (int) db.insert("PERSONA", null, registro);

            if (filaAfectadas !=-1){
                validacion.setTextColor(getColor(R.color.verde));
                validacion.setText("Persona registrada correctamente.");
               NombrePersona.setText("");
            }else
            {
                validacion.setTextColor(getColor(R.color.rojo));
                validacion.setText("Sucedió un error al guardar.");
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
        Cursor fila = db.rawQuery("SELECT COUNT(PER_COD) FROM PERSONA ",null);
        if (fila.moveToFirst()){
            num=fila.getInt(0);

        }else   {
            validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText("No se encontró nada.");
        }
        db.close();
        return num;

    }
}