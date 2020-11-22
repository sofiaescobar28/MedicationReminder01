package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tomar_medicamento extends AppCompatActivity {

    ManejadorBD admin;
    SQLiteDatabase db;
    int filaAfectadas;
    private Spinner estado;
    private EditText comentatrio;
    private TextView validacion;
    private String Re_cod,Per_cod,MEDxRE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_medicamento);

        estado = findViewById(R.id.spnEstadoTomar);
        comentatrio = findViewById(R.id.etComentario);
        validacion = findViewById(R.id.tvValidarToma);

        Bundle extras = getIntent().getExtras();
        Re_cod=extras.getString("RE_COD");
        Per_cod=extras.getString("PER_COD");
        MEDxRE=extras.getString("MEDXRED_COD");

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
    }

    public void GuardarToma(View view) {
        if(estado.getSelectedItemPosition()>0){
            String fecha;
            ContentValues registro = new ContentValues();
            Date Hoy = new Date();
            int est =estado.getSelectedItemPosition();

            DecimalFormat df = new DecimalFormat("##");

            fecha = df.format(Hoy.getHours());
            fecha +=":";
            fecha += df.format(Hoy.getMinutes());
            fecha +=" ";
            fecha += new SimpleDateFormat("dd/MM/yyyy").format(Hoy);

            registro.put("H_COD",ultimoID_RE());
            registro.put("MEDXRED_COD",MEDxRE);
            registro.put("H_FECHA",fecha);
            registro.put("H_ESTADO",est);
            registro.put("H_COMENTARIO",comentatrio.getText().toString().trim());

            db = admin.getWritableDatabase();
            filaAfectadas = (int) db.insert("HISTORIAL", null, registro);
            if (filaAfectadas != -1) {
                finish();
                Intent mr = new Intent(Tomar_medicamento.this,medicamento_recordatorio.class);
                mr.putExtra("RE_COD",Re_cod);
                mr.putExtra("PER_COD",Per_cod);
                startActivity(mr);
            } else {
                validacion.setTextColor(getColor(R.color.rojo));
                validacion.setText(getText(R.string.error_guardar));
            }
            db.close();
        }else{
            validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText(getText(R.string.campos_vacios));
        }
    }

    public int ultimoID_RE(){
        db = admin.getWritableDatabase();
        int num=-1;
        Cursor fila = db.rawQuery("SELECT H_COD FROM HISTORIAL" +
                " ORDER BY H_COD DESC"+
                " LIMIT 1;",null);
        if (fila.moveToFirst()){
            num=fila.getInt(0);
            num++;
        }else   {
            num = 1;
        }
        db.close();
        return num;
    }

    public void CancelarToma(View view) {
        finish();
        Intent mr = new Intent(Tomar_medicamento.this,medicamento_recordatorio.class);
        mr.putExtra("RE_COD",Re_cod);
        mr.putExtra("PER_COD",Per_cod);
        startActivity(mr);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent mr = new Intent(Tomar_medicamento.this,medicamento_recordatorio.class);
        mr.putExtra("RE_COD",Re_cod);
        mr.putExtra("PER_COD",Per_cod);
        startActivity(mr);
    }
}