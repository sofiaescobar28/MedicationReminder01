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
import java.util.Collection;

public class Editar_Medicamento extends AppCompatActivity {
    private EditText NombreMedicamento,TipoMed;
    private TextView validacion;
    ManejadorBD admin;
    SQLiteDatabase db;
    private int filaAfectadas;
    private String cod_med, nombre_med,tipo_med;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar__medicamento);
        NombreMedicamento=findViewById(R.id.txtNombreMedEditar);
        TipoMed=findViewById(R.id.txtTipoMedEditar);
        validacion=findViewById(R.id.lblValidacionEditar);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);

        Bundle extras = getIntent().getExtras();
        cod_med = extras.getString("id_medicamento");
        nombre_med = extras.getString("nombre_medicamento");
        tipo_med = extras.getString("tipo_medicamento");

        NombreMedicamento.setText(nombre_med);
        TipoMed.setText(tipo_med);
    }

    public void updateMedicamento(View view) {
        if (NombreMedicamento.getText().toString().trim().isEmpty() &&
        TipoMed.getText().toString().trim().isEmpty()){
            validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText(R.string.campos_vacios);
        }else
        {
            if (validarInsertMedicamentoEditar().size()>0){
                validacion.setTextColor(getColor(R.color.rojo));
                validacion.setText(R.string.yaexiste_med+" "+NombreMedicamento.getText().toString().trim()+".");
            }
            else{
                ContentValues registro = new ContentValues();
                registro.put("MED_NOMBRE",NombreMedicamento.getText().toString());
                registro.put("MED_TIPO",TipoMed.getText().toString());
                db = admin.getWritableDatabase();
                filaAfectadas = db.update("MEDICAMENTO", registro,"MED_COD = "+cod_med,null);
                if (filaAfectadas ==1){
                  finish();
                    Intent ventana= new Intent(getApplicationContext(),Medicamento.class);
                    startActivity(ventana);
                }else
                {
                    validacion.setTextColor(getColor(R.color.rojo));
                    validacion.setText(R.string.error_update);

                }
            }
        }
    }

    private ArrayList<EMedicamento> validarInsertMedicamentoEditar() {
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT MED_COD,MED_NOMBRE,MED_TIPO FROM MEDICAMENTO" +
                        " WHERE MED_NOMBRE = '"+NombreMedicamento.getText().toString().trim()+"'" +
                        " and MED_COD != "+cod_med
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent ventana= new Intent(getApplicationContext(),Medicamento.class);
        startActivity(ventana);
    }
    public void Cancelar(View view) {
        finish();
        Intent ventana= new Intent(getApplicationContext(),Medicamento.class);
        startActivity(ventana);
    }
    public void LimpiarCasilas(){
        NombreMedicamento.setText("");
        TipoMed.setText("");
    }
}