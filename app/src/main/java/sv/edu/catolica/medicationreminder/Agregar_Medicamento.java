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

import java.util.ArrayList;

public class Agregar_Medicamento extends AppCompatActivity {
    private EditText NombreMedicamento,TipoMed;
    private TextView validacion;
    ManejadorBD admin;
    SQLiteDatabase db;
    int filaAfectadas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar__medicamento);
        NombreMedicamento=findViewById(R.id.txtNombreMedicamento);
        TipoMed=findViewById(R.id.txtTipoMedicamento);
        validacion=findViewById(R.id.lblValidacion);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
    }

    public void insertarMedicamento(View v){
        if (NombreMedicamento.getText().toString().trim().isEmpty() ||
                TipoMed.getText().toString().trim().isEmpty())
        {
            validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText(R.string.campos_vacios);
        }else{
            if (validarInsertMedicamento().size()>0){
                validacion.setTextColor(getColor(R.color.rojo));
                validacion.setText(getString(R.string.yaexiste_med)+NombreMedicamento.getText().toString().trim()+".");
            }
            else{
                int count =getCount();
                ContentValues registro = new ContentValues();
                if (count>0){
                    registro.put("MED_COD",(count));

                }else{
                    registro.put("MED_COD",1);
                }
                registro.put("MED_NOMBRE",NombreMedicamento.getText().toString());
                registro.put("MED_TIPO",TipoMed.getText().toString());
                db = admin.getWritableDatabase();
                filaAfectadas = (int) db.insert("MEDICAMENTO", null, registro);

                if (filaAfectadas !=-1){
                    validacion.setTextColor(getColor(R.color.verde));
                    validacion.setText(R.string.med_insertado);
                    LimpiarCasilas();
                }else
                {
                    validacion.setTextColor(getColor(R.color.rojo));
                    validacion.setText(R.string.error);
                }
                db.close();
            }
        }
    }

    public void LimpiarCasilas(){
        NombreMedicamento.setText("");
        TipoMed.setText("");
    }

    public int getCount(){
        db = admin.getWritableDatabase();
        int num=-1;
        Cursor fila = db.rawQuery("SELECT MED_COD FROM MEDICAMENTO" +
                " ORDER BY MED_COD DESC"+
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

    public  ArrayList<EMedicamento> validarInsertMedicamento(){
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT MED_COD,MED_NOMBRE,MED_TIPO FROM MEDICAMENTO" +
                        " WHERE MED_NOMBRE = '"+NombreMedicamento.getText().toString().trim()+"'"
                ,null);
        ArrayList<EMedicamento> meds = new ArrayList<EMedicamento>();

        while (fila.moveToNext()){
            EMedicamento _med = new EMedicamento();
            _med.MED_COD = fila.getInt(0);
            _med.MED_NOMBRE = fila.getString(1);
            _med.MED_TIPO=fila.getString(2);
            meds.add(_med);

        }

        db.close();
        return meds;
    }

    public void Cancelar(View v){
        finish();
        Intent ventana= new Intent(Agregar_Medicamento.this, Medicamento.class);
        startActivity(ventana);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent ventana= new Intent(getApplicationContext(),Medicamento.class);
        startActivity(ventana);
    }
}