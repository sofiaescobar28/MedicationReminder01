package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Editar_Historial extends AppCompatActivity {
private TextView EHtitulo,EHDosificacion,EHDosis,EHFecha,EHComentario,validacion;
private Spinner EHEstado;
private String titulo,dosificacion,dosis,fecha,estado,comentario,id,medre;
    ManejadorBD admin;
    SQLiteDatabase db;
    private int filaAfectada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar__historial);

        EHtitulo = findViewById(R.id.lbltituloEditHistorial);
        EHDosificacion=findViewById(R.id.lblValorDosificacionEditar);
        EHDosis=findViewById(R.id.lblValorDosisEditar);
        EHFecha=findViewById(R.id.lblValorFechaEditar);
        EHEstado=findViewById(R.id.spnEstado);
        EHComentario = findViewById(R.id.txtEHComentario);
        validacion=findViewById(R.id.lblvalidarHistoEd);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);

        Bundle extras =getIntent().getExtras();
        titulo=extras.getString("Titulo");
        id=extras.getString("IDEH");
        medre=extras.getString("Medre");
        dosificacion=extras.getString("Dosificacion");
        dosis=extras.getString("Dosis");
        fecha=extras.getString("Fecha");
        estado=extras.getString("Estado");
        comentario=extras.getString("Comentario");

        EHtitulo.setText(titulo);
        EHDosificacion.setText(dosificacion);
        EHDosis.setText(dosis);
        EHFecha.setText(fecha);
        if (estado.equals("Tomado")){
            EHEstado.setSelection(1);
        }else if(estado.equals("No tomado")){
            EHEstado.setSelection(2);
        }else{
            EHEstado.setSelection(0);
        }
        if (!comentario.trim().isEmpty())
        {
            EHComentario.setText(comentario);
        }


    }

    public void updateHistorial(View view) {


                ContentValues registro = new ContentValues();

        registro.put("MEDXRED_COD",medre);
        registro.put("H_FECHA",fecha);
        registro.put("H_ESTADO",EHEstado.getSelectedItem().toString());
        registro.put("H_COMENTARIO",EHComentario.getText().toString());
                db = admin.getWritableDatabase();
                 filaAfectada = db.update("HISTORIAL", registro,"H_COD = "+id,null);
                if (filaAfectada ==1){
                    validacion.setText("");
                    finish();
                    Intent ventana= new Intent(getApplicationContext(),Historial.class);
                    startActivity(ventana);
                }else
                {
                    validacion.setTextColor(getColor(R.color.rojo));
                    validacion.setText("Sucedió un problema al modificar.");

                }


    }


    public void Salir(View view) {
        finish();
        Intent ventana= new Intent(getApplicationContext(),Historial.class);
        startActivity(ventana);
    }
}