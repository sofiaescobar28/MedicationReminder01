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

public class medicina_por_recordatorio extends AppCompatActivity {

    private TextView Nombre,Tipo,validacion;
    private EditText dosificacion,dosis;
    private int IDmed=0,IDrec=0;
    ManejadorBD admin;
    SQLiteDatabase db;
    int filaAfectadas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicina_por_recordatorio);

        Nombre = findViewById(R.id.tvNomMEDxRE);
        Tipo = findViewById(R.id.tvTipoMEDxRE);

        Bundle extras = getIntent().getExtras();

        IDmed = Integer.parseInt(extras.getString("id_medicamento"));
        //IDrec = Integer.parseInt(extras.getString("id_recordatorio"));
        Nombre.setText(extras.getString("medicamento"));
        Tipo.setText(extras.getString("tipo"));
        dosificacion = findViewById(R.id.etDosificacion);
        dosis = findViewById(R.id.etDosis);

        validacion = findViewById(R.id.lblValidacionMEDxRE);
    }

    public void GuardarMEDxRE(View view) {
        String DF = dosificacion.getText().toString().trim();
        if (!DF.isEmpty() && !dosis.getText().toString().trim().isEmpty()){
            int DO = Integer.parseInt(dosis.getText().toString().trim());
            ContentValues registro = new ContentValues();
            registro.put("MEDXRED_COD",(ultimoID_MEDxRE()+1));
            registro.put("RE_COD",IDrec);
            registro.put("MED_COD",IDmed);
            registro.put("MEDXRED_DOSIFICACION",DF);
            registro.put("RE_DOSIS",DO);

            db = admin.getWritableDatabase();
            filaAfectadas = (int) db.insert("MEDXRE", null, registro);
            if (filaAfectadas != -1) {
                this.finish();
                Intent MedicamentoRE = new Intent(getApplicationContext(),medicamento_recordatorio.class);
                startActivity(MedicamentoRE);
            } else {
                validacion.setTextColor(getColor(R.color.rojo));
                validacion.setText("Error al guardar");
            }
            db.close();
        }
        else {
            validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText(getText(R.string.campos_vacios));
        }
    }

    private int ultimoID_MEDxRE() {
        db = admin.getWritableDatabase();
        int num=-1;
        Cursor fila = db.rawQuery("SELECT COUNT(MEDXRED_COD) FROM MEDXRE;",null);
        if (fila.moveToFirst()){
            num=fila.getInt(0);
        }else   {
            validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText("No se encontró nada");
        }
        db.close();
        return num;
    }
}