package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
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

public class Editar_Recordatorio extends AppCompatActivity {
    private EditText etTit,etValor,etDD,etMM,etAA;
    private Spinner spnMDH,spnEstado;
    private RadioButton radPerm,radFFinal;
    private String titulo,fInicio,fFinal,estado;
    private TextView validacion;
    ManejadorBD admin;
    SQLiteDatabase db;
    int filaAfectadas,valor,mdh,id,id_persona,idRec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar__recordatorio);

        etTit = findViewById(R.id.etTitRecordatorioEdit);
        etValor = findViewById(R.id.etValorIntervaloEdit);
        etDD = findViewById(R.id.etDiaEdit);
        etMM = findViewById(R.id.etMesEdit);
        etAA = findViewById(R.id.etAnioEdit);
        spnMDH = findViewById(R.id.spnMDHEdit);
        spnEstado = findViewById(R.id.spnEstadoEdit);
        radPerm = findViewById(R.id.radPermanenteEdit);
        radFFinal = findViewById(R.id.radFechaFinalEdit);
        validacion = findViewById(R.id.tvValidarReEdit);

        /**campos de extras
         * id (int)
         * idRec
         * Per_Cod (int)
         * Valor (string)
         * Rec_cod (string)
         * Dia (string)
         * Mes (string)
         * Anio (string)
         * MDH (int)
         * Estado (tentativo)**/
        Bundle extras =getIntent().getExtras();
        etValor.setText(extras.getString("Valor"));
        id=extras.getInt("Rec_Cod");
        id_persona=extras.getInt("Per_Cod");
        if (extras.getString("Dia").trim().equals("")){
            radPerm.isChecked();
            etDD.setText(null);
            etMM.setText(null);
            etAA.setText(null);
        }else{
            radFFinal.isChecked();
            etDD.setText(extras.getString("Dia"));
            etMM.setText(extras.getString("Mes"));
            etAA.setText(extras.getString("Anio"));
        }
        spnMDH.setSelection(extras.getInt("MDH"));

        //spnEstado.setSelection(extras.getString("Estado"));

        etDD.setEnabled(false);
        etMM.setEnabled(false);
        etAA.setEnabled(false);
        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
    }
    public void PermanenteEdit (View v){
        etDD.setEnabled(false);
        etMM.setEnabled(false);
        etAA.setEnabled(false);
        etDD.setText(null);
        etMM.setText(null);
        etAA.setText(null);
    }
    public void noPermanenteEdit (View v){
        etDD.setEnabled(true);
        etMM.setEnabled(true);
        etAA.setEnabled(true);
    }

    public void GuardarEdit(View view) {
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

                    registro.put("RE_COD",id);
                    registro.put("RE_COD",idRec);
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
                            validacion.setText("Los campos no pueden quedar en blanco.");
                        } else {
                            if (validarFechaEdit(fFinal)) {
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

                    filaAfectadas = db.update("RECORDATORIO", registro,"RE_COD = "+id,null);
                    if (filaAfectadas ==1){
                        validacion.setText("");
                        finish();
                        Intent ventana= new Intent(getApplicationContext(),Historial.class);
                        startActivity(ventana);
                    }else
                    {
                        validacion.setTextColor(getColor(R.color.rojo));
                        validacion.setText(R.string.error_update);
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

    public static boolean validarFechaEdit(String fecha) {
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

    public void CancelarEdit(View view) {
    }
}