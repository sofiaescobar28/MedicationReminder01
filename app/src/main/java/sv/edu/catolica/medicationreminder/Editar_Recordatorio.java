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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

public class Editar_Recordatorio extends AppCompatActivity {
    private EditText etTit,etValor,etDD,etMM,etAA;
    private Spinner spnMDH,spnEstado;
    private RadioButton radPerm,radFFinal;
    private String titulo,fInicio,fFinal,info;
    private TextView validacion;
    ManejadorBD admin;
    SQLiteDatabase db;
    int filaAfectadas,valor,mdh,id,id_persona,estado,encurso;

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
        radPerm = findViewById(R.id.radPEdit);
        radFFinal = findViewById(R.id.radFFinalEdit);
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

        Bundle extra = getIntent().getExtras();
        info=extra.getString("Info");

        String[] parts = info.split(",");
        String part1 = parts[4];//RE_COD

        String[] porPuntos = part1.split("=");
        id = Integer.parseInt(porPuntos[1]);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);

        llenarValores();
        if (fFinal.trim().equals(getText(R.string.permanente))){
            radPerm.setChecked(true);
            radFFinal.setChecked(false);

            etDD.setText(null);
            etMM.setText(null);
            etAA.setText(null);
            etDD.setEnabled(false);
            etMM.setEnabled(false);
            etAA.setEnabled(false);
        }else{
            radFFinal.setChecked(true);
            radPerm.setChecked(false);

            String [] ff = fFinal.split("/");
            String dia = ff[0];
            String mes = ff[1];
            String año = ff[2];

            etDD.setText(dia);
            etMM.setText(mes);
            etAA.setText(año);
        }
    }

    public void llenarValores(){
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT RE_COD, PER_COD, RE_TITULO, RE_INTERVALO_MDH, RE_INTERVALO_VALOR, RE_F_FINAL, RE_ESTADO, RE_F_INICIO FROM RECORDATORIO " +
                        "WHERE RE_COD = '" + id + "'"
                ,null);

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        while (fila.moveToNext()) {
            //id = fila.getInt(0);
            id_persona = fila.getInt(1);
            etTit.setText(fila.getString(2));
            spnMDH.setSelection( fila.getInt(3));
            etValor.setText( fila.getString(4));
            fFinal = fila.getString(5);
            spnEstado.setSelection(fila.getInt(6));
            encurso=fila.getInt(6);
            fInicio = fila.getString(7);

        }
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
                    mdh = spnMDH.getSelectedItemPosition();
                    estado = spnEstado.getSelectedItemPosition();

                    registro.put("RE_COD",id);
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
                            if (validarFechaEdit(fFinal)) {
                                registro.put("RE_F_FINAL", fFinal);
                            }else{
                                validacion.setTextColor(getColor(R.color.rojo));
                                validacion.setText(getText(R.string.fecha_invalida));
                            }
                        }
                    }else  if(radPerm.isChecked()){
                        registro.put("RE_F_FINAL", getText(R.string.permanente).toString());
                    }
                    registro.put("RE_ESTADO", estado);
                    if (estado==1)
                    {
                        if (obtenerCantidadMedicamentos(id).size()>0){
                            if (radPerm.isChecked()){
                                db = admin.getWritableDatabase();
                                filaAfectadas = (int) db.update("RECORDATORIO", registro, "RE_COD = " + id, null);
                                if (filaAfectadas == 1) {
                                    if (encurso == 1) {
                                        Alarm alarma = new Alarm();
                                        alarma.cancelAlarm(this, id);
                                    }
                                    Intent service = new Intent(this, MyService.class);

                                    service.putExtra("time", valor);
                                    service.putExtra("tipoTiempo", mdh);
                                    service.putExtra("identificador", id);
                                    service.putExtra("persona", String.valueOf(id_persona));

                                    startService(service);
                                    validacion.setText("");
                                    finish();
                                    Intent ventana = new Intent(getApplicationContext(), Recordatorios.class);
                                    ventana.putExtra("persona_id", String.valueOf(id_persona));
                                    startActivity(ventana);
                                } else {
                                    validacion.setTextColor(getColor(R.color.rojo));
                                    validacion.setText(R.string.error_update);
                                }
                            }
                            else {
                                if (validarFechaEdit(fFinal)) {
                                    db = admin.getWritableDatabase();
                                    filaAfectadas = (int) db.update("RECORDATORIO", registro, "RE_COD = " + id, null);
                                    if (filaAfectadas == 1) {
                                        if (encurso == 1) {
                                            Alarm alarma = new Alarm();
                                            alarma.cancelAlarm(this, id);
                                        }
                                        Intent service = new Intent(this, MyService.class);

                                        service.putExtra("time", valor);
                                        service.putExtra("tipoTiempo", mdh);
                                        service.putExtra("identificador", id);
                                        service.putExtra("persona", String.valueOf(id_persona));

                                        startService(service);
                                        validacion.setText("");
                                        finish();
                                        Intent ventana = new Intent(getApplicationContext(), Recordatorios.class);
                                        ventana.putExtra("persona_id", String.valueOf(id_persona));
                                        startActivity(ventana);
                                    } else {
                                        validacion.setTextColor(getColor(R.color.rojo));
                                        validacion.setText(R.string.error_update);
                                    }
                                }
                            }
                        }else {
                            Toast.makeText(this, getText(R.string.debe_agregar_med),Toast.LENGTH_LONG).show();
                        }

                    }else{
                        if (radPerm.isChecked()){
                            filaAfectadas = (int) db.update("RECORDATORIO", registro, "RE_COD = " + id, null);
                            db.close();
                            if (filaAfectadas == 1) {
//Eliinar el recordatorio
                                Alarm alarma = new Alarm();
                                alarma.cancelAlarm(this, id);
                                validacion.setText("");
                                finish();
                                Intent ventana = new Intent(getApplicationContext(), Recordatorios.class);
                                ventana.putExtra("persona_id", String.valueOf(id_persona));
                                startActivity(ventana);
                            } else {
                                validacion.setTextColor(getColor(R.color.rojo));
                                validacion.setText(R.string.error_update);
                            }
                        }
                        else {

                            if (validarFechaEdit(fFinal)) {
                                filaAfectadas = (int) db.update("RECORDATORIO", registro, "RE_COD = " + id, null);
                                db.close();
                                if (filaAfectadas == 1) {
//Eliinar el recordatorio
                                    Alarm alarma = new Alarm();
                                    alarma.cancelAlarm(this, id);
                                    validacion.setText("");
                                    finish();
                                    Intent ventana = new Intent(getApplicationContext(), Recordatorios.class);
                                    ventana.putExtra("persona_id", String.valueOf(id_persona));
                                    startActivity(ventana);
                                } else {
                                    validacion.setTextColor(getColor(R.color.rojo));
                                    validacion.setText(R.string.error_update);
                                }
                            }
                        }
                    }
                }
                else{
                    validacion.setTextColor(getColor(R.color.rojo));
                    validacion.setText(getText(R.string.fecha_mayor_cero));
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
    public ArrayList<ECantidadMed> obtenerCantidadMedicamentos(int recordatorio){
        db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("SELECT m.MED_NOMBRE " +
                        " FROM MEDICAMENTO m" +
                        " INNER JOIN MEDXRE m2 ON m.MED_COD = m2.MED_COD " +
                        " WHERE m2.RE_COD = "+recordatorio
                ,null);
        ArrayList<ECantidadMed> medicamentos=new ArrayList<ECantidadMed>();
        while (fila.moveToNext()){
            ECantidadMed _med = new ECantidadMed();
            _med.medicamento=fila.getString(0);
            medicamentos.add(_med);
        }
        db.close();
        return medicamentos;
    }

    public static boolean validarFechaEdit(String fecha) {
        try {

            Date fechaactual = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
            Date Fparametro = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
            return Fparametro.after(fechaactual);
        } catch (ParseException e) {
            return false;
        }
    }

    public void CancelarEdit(View view) {
        finish();
        Intent ventana= new Intent(getApplicationContext(),Recordatorios.class);
        ventana.putExtra("persona_id",String.valueOf(id_persona));
        startActivity(ventana);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent ventana= new Intent(getApplicationContext(),Recordatorios.class);
        ventana.putExtra("persona_id",String.valueOf(id_persona));
        startActivity(ventana);
    }
}