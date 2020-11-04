package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Add_Recordatorios extends AppCompatActivity {
    private EditText etTit,etValor,etDD,etMM,etAA;
    private Spinner spnMDH,spnEstado;
    private RadioButton radPerm,radFFinal;
    private String titulo,fInicio,fFinal,estado;
    private TextView validacion;
    ManejadorBD admin;
    SQLiteDatabase db;
    int filaAfectadas,valor,mdh;

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
        spnEstado = findViewById(R.id.spnEstado);
        radPerm = findViewById(R.id.radPermanente);
        radFFinal = findViewById(R.id.radFechaFinal);
        validacion = findViewById(R.id.tvValidarRe);

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
        if (!etValor.getText().toString().trim().isEmpty() && spnMDH.getSelectedItemPosition()>0 && spnEstado.getSelectedItemPosition()>0) {
            if (!titulo.isEmpty()) {
                valor = Integer.parseInt(etValor.getText().toString().trim());
                if (valor>0){
                    ContentValues registro = new ContentValues();
                    Date Hoy = new Date();
                    mdh = spnMDH.getSelectedItemPosition();
                    estado = spnEstado.getSelectedItem().toString();

                    fInicio = String.valueOf(Hoy.getDay()+1);
                    fInicio += getString(R.string.pleca) + Hoy.getMonth();
                    fInicio += getString(R.string.pleca) + Hoy.getYear();

                    registro.put("RE_COD",(ultimoID_RE()+1));
                    //registro.put("PER_COD");
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
                            validacion.setText("Los campos no pueden quedar en blanco.");
                        } else {
                            if (validarFecha(fFinal)) {
                                registro.put("RE_F_FINAL", fFinal);
                            }else{
                                validacion.setTextColor(getColor(R.color.rojo));
                                validacion.setText("Fecha invalida");
                            }
                        }
                    }else  if(radPerm.isChecked()){
                        registro.put("RE_F_FINAL", getText(R.string.permanente).toString());
                    }
                    registro.put("RE_ESTADO", estado);

                    db = admin.getWritableDatabase();
                    filaAfectadas = (int) db.insert("RECORDATORIO", null, registro);
                    if (filaAfectadas != -1) {
                        validacion.setTextColor(getColor(R.color.verde));
                        validacion.setText("Recordatorio guardado correctamente.");
                    } else {
                        validacion.setTextColor(getColor(R.color.rojo));
                        validacion.setText("Sucedió un error al guardar.");
                    }
                    db.close();
                }
                else{
                    validacion.setTextColor(getColor(R.color.rojo));
                    validacion.setText("El valor del intervalo tiene que ser mayor a 0");
                }
            } else {
                validacion.setTextColor(getColor(R.color.rojo));
                validacion.setText("Los campos no pueden quedar en blanco.");
            }
        }
        else {
            validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText("Los campos no pueden quedar en blanco.");
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
        Cursor fila = db.rawQuery("SELECT COUNT(RE_COD) FROM RECORDATORIO;",null);
        if (fila.moveToFirst()){
            num=fila.getInt(0);
        }else   {
            validacion.setTextColor(getColor(R.color.rojo));
            validacion.setText("No se encontró nada.");
        }
        db.close();
        return num;

    }

    public void Cancelar(View view) {
    }
}