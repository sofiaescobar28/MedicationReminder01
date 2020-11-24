package sv.edu.catolica.medicationreminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Add_Recordatorios extends AppCompatActivity {
    private EditText etTit,etValor,etDD,etMM,etAA;
    private Spinner spnMDH;
    private RadioButton radPerm,radFFinal;
    private String titulo,fInicio,fFinal,id_persona;
    private TextView validacion;
    ManejadorBD admin;
    SQLiteDatabase db;
    int filaAfectadas,valor,mdh, estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__recordatorios);
        //getSupportActionBar().hide();

        etTit = findViewById(R.id.etTitRecordatorio);
        etValor = findViewById(R.id.etValorIntervalo);
        etDD = findViewById(R.id.etDia);
        etMM = findViewById(R.id.etMes);
        etAA = findViewById(R.id.etAnio);
        spnMDH = findViewById(R.id.spnMDH);
        radPerm = findViewById(R.id.radPermanente);
        radFFinal = findViewById(R.id.radFechaFinal);
        validacion = findViewById(R.id.tvValidarRe);

        Bundle extras =getIntent().getExtras();
        id_persona = extras.getString("PER_COD");



        etDD.setEnabled(false);
        etMM.setEnabled(false);
        etAA.setEnabled(false);
        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
    }

    public void Permanente (View v){
        etDD.setEnabled(false);
        etMM.setEnabled(false);
        etAA.setEnabled(false);
        etDD.setText(null);
        etMM.setText(null);
        etAA.setText(null);
    }
    public void noPermanente (View v){
        etDD.setEnabled(true);
        etMM.setEnabled(true);
        etAA.setEnabled(true);
    }

    public void Guardar(View view) {
        titulo = etTit.getText().toString().trim();
        if (!etValor.getText().toString().trim().isEmpty() && spnMDH.getSelectedItemPosition()>0) {
            if (!titulo.isEmpty()) {
                valor = Integer.parseInt(etValor.getText().toString().trim());
                if (valor>0){
                    ContentValues registro = new ContentValues();
                    Date Hoy = new Date();
                    mdh = spnMDH.getSelectedItemPosition();
                    estado = 2;

                    fInicio = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(Hoy);

                    registro.put("RE_COD",ultimoID_RE());
                    registro.put("PER_COD",id_persona);
                    registro.put("RE_TITULO", titulo);
                    registro.put("RE_F_INICIO", fInicio);
                    registro.put("RE_INTERVALO_MDH", mdh);
                    registro.put("RE_INTERVALO_VALOR", valor);

                    if (radFFinal.isChecked()){
                        String dd = etDD.getText().toString().trim();
                        String mm = etMM.getText().toString().trim();
                        String aa = etAA.getText().toString().trim();
                        fFinal = dd;
                        fFinal += getString(R.string.pleca) + mm;
                        fFinal += getString(R.string.pleca) + aa;
                        if (dd.isEmpty() && mm.isEmpty() && aa.isEmpty()) {
                            validacion.setTextColor(getColor(R.color.rojo));
                            validacion.setText(getText(R.string.campos_vacios));
                        } else {
                            if (validarFecha(fFinal)) {
                                registro.put("RE_F_FINAL", fFinal);
                            }else{
                                validacion.setTextColor(getColor(R.color.rojo));
                                validacion.setText(R.string.fecha_invalida);
                            }
                        }
                    }else  if(radPerm.isChecked()){
                        registro.put("RE_F_FINAL", getText(R.string.permanente).toString());
                    }
                    registro.put("RE_ESTADO", estado);
                    if (validarFecha(fFinal)) {
                        db = admin.getWritableDatabase();
                        filaAfectadas = (int) db.insert("RECORDATORIO", null, registro);
                        db.close();
                        if (filaAfectadas != -1) {
                            finish();
                            Intent ventana = new Intent(Add_Recordatorios.this, Recordatorios.class);
                            ventana.putExtra("persona_id", String.valueOf(id_persona));
                            startActivity(ventana);
                        } else {
                            validacion.setTextColor(getColor(R.color.rojo));
                            validacion.setText(R.string.error_guardar);
                        }
                    }
                }
                else{
                    validacion.setTextColor(getColor(R.color.rojo));
                    validacion.setText(R.string.fecha_mayor_cero);
                }
            } else {
                validacion.setTextColor(getColor(R.color.rojo));
                validacion.setText(getText(R.string.campos_vacios));
            }
        }
        else {
            validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText(getText(R.string.campos_vacios));
        }
    }



    public static boolean validarFecha(String fecha) {
        try {

            Date fechaactual = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
            Date Fparametro = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
            if(Fparametro.after(fechaactual)){
                return true;
            }else{
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    public int ultimoID_RE(){
        db = admin.getWritableDatabase();
        int num=-1;
        Cursor fila = db.rawQuery("SELECT RE_COD FROM RECORDATORIO" +
                " ORDER BY RE_COD DESC"+
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

    public void Cancelar(View view) {
        this.finish();
        Intent recordatorios = new Intent(getApplicationContext(),Recordatorios.class);
        recordatorios.putExtra("persona_id",id_persona);
        startActivity(recordatorios);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
        Intent recordatorios = new Intent(getApplicationContext(),Recordatorios.class);
        recordatorios.putExtra("persona_id",id_persona);
        startActivity(recordatorios);
    }
}