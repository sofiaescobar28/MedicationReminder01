package sv.edu.catolica.medicationreminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.Icon;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.view.Gravity;
import android.view.PointerIcon;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class medicamento_recordatorio extends AppCompatActivity {

    private String Re_cod,Per_cod;
    private LinearLayout contenedor;
    ManejadorBD admin;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamento_recordatorio);

        Bundle extras = getIntent().getExtras();
        Re_cod = extras.getString("RE_COD");
        Per_cod =extras.getString("PER_COD");
        contenedor = findViewById(R.id.ContenedorTomar);

        admin=new ManejadorBD(getApplicationContext(),"MEDICATIONREMINDER",null,1);
        buscarMedNombres();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent recordatorios = new Intent(medicamento_recordatorio.this,Recordatorios.class);
        recordatorios.putExtra("persona_id",String.valueOf(Per_cod));
        startActivity(recordatorios);
    }

    public void construirInterfaz(ArrayList<EmedXre> MedHistory){
        for ( EmedXre mr: MedHistory){
            TableLayout tablaVista=new TableLayout(getApplicationContext());
            tablaVista.setShrinkAllColumns(true);
            tablaVista.setStretchAllColumns(true);

            TableRow lineaIdRExMED = new TableRow(getApplicationContext());
            TableRow lineaNombreMED = new TableRow(getApplicationContext());
            TableRow lineaTipo= new TableRow(getApplicationContext());
            TableRow lineaBoton = new TableRow(getApplicationContext());

            Button btnTomar = new Button(this);
            Button btnEliminar = new Button(this);

            btnTomar.setText(R.string.tomar);
            btnEliminar.setText(R.string.eliminar);

            btnEliminar.setGravity(Gravity.RIGHT);
            btnEliminar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            btnTomar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            final TextView tvIdValor=new TextView(this);
            final TextView tvNombreValor=new TextView(this);
            final TextView tvTipoValor=new TextView(this);
            final TextView tvDosificValor=new TextView(this);
            final TextView tvDosisValor=new TextView(this);


            tvIdValor.setText(String.valueOf(mr.MED_COD));
            tvNombreValor.setText(mr.MED_NOMBRE);
            tvTipoValor.setText(mr.MED_TIPO);
            tvDosificValor.setText(getText(R.string.dosificacion)+": "+mr.MEDXRED_DOSIFICACION);
            tvDosisValor.setText(getText(R.string.dosis)+": "+mr.RE_DOSIS);

            tvIdValor.setVisibility(View.INVISIBLE);
            tvNombreValor.setGravity(Gravity.TOP);
            tvNombreValor.setTextSize(27);
            tvTipoValor.setTextSize(27);
            tvDosificValor.setTextSize(15);
            tvDosisValor.setTextSize(15);

            btnTomar.setGravity(Gravity.RIGHT);

            btnTomar.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              finish();
                                              Intent tomar = new Intent(medicamento_recordatorio.this,Tomar_medicamento.class);
                                              String id = tvIdValor.getText().toString().trim();
                                              tomar.putExtra("RE_COD",Re_cod);
                                              tomar.putExtra("PER_COD",Per_cod);
                                              tomar.putExtra("MEDXRED_COD",id);
                                              startActivity(tomar);
                                          }
                                      }
            );
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder pregunta = new AlertDialog.Builder(medicamento_recordatorio.this);
                    pregunta.setMessage(R.string.preg_eliminar_medicLista);
                    pregunta.setTitle(R.string.eliminar_medicLista);
                    pregunta.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            int id = Integer.parseInt(tvIdValor.getText().toString().trim());
                            /*int fila = db.delete(" MEDXRE ", " MEDXRED_COD =" + id + ";", null);
                            if (fila == 1) {
                                Toast.makeText(getApplicationContext(),"Eliminado correctamente",Toast.LENGTH_LONG);
                                buscarMedNombres();
                            }else{
                                Toast.makeText(getApplicationContext(),"Error al eliminar",Toast.LENGTH_LONG);
                            }*/
                        }
                    });
                    pregunta.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    pregunta.create();
                    pregunta.show();
                }
            });

            lineaIdRExMED.addView(tvIdValor);

            lineaNombreMED.addView(tvNombreValor);
            lineaNombreMED.addView(tvDosificValor);

            lineaTipo.addView(tvTipoValor);
            lineaTipo.addView(tvDosisValor);

            lineaBoton.addView(btnTomar);
            lineaBoton.addView(btnEliminar);

            tablaVista.addView(lineaIdRExMED);
            tablaVista.addView(lineaNombreMED);
            tablaVista.addView(lineaTipo);
            tablaVista.addView(lineaBoton);

            tablaVista.setPadding(10,10,10,10);

            contenedor.addView(tablaVista);
        }
    }

    public void elegirMed(View view) {
        finish();
        Intent elegir = new Intent(medicamento_recordatorio.this,Elegir_medicamento.class);
        elegir.putExtra("RE_COD",Re_cod);
        elegir.putExtra("PER_COD",String.valueOf(Per_cod));
        startActivity(elegir);
    }

    public  void buscarMedNombres() {
        if (contenedor.getChildCount()>0){
            contenedor.removeAllViews();
        }

        db = admin.getWritableDatabase();
        int rc = Integer.parseInt(Re_cod);
        Cursor fila = db.rawQuery(" SELECT MEDXRE.MEDXRED_COD, MEDICAMENTO.MED_NOMBRE, MEDICAMENTO.MED_TIPO, MEDXRE.MEDXRED_DOSIFICACION, MEDXRE.RE_DOSIS FROM MEDXRE" +
                        " INNER JOIN MEDICAMENTO ON MEDXRE.MED_COD = MEDICAMENTO.MED_COD"+
                        " WHERE MEDXRE.RE_COD = "+rc+";"
                ,null);
        ArrayList<EmedXre> meds = new ArrayList<EmedXre>();

        while (fila.moveToNext()){
            EmedXre _med = new EmedXre();
            _med.MED_COD = fila.getInt(0);
            _med.MED_NOMBRE = fila.getString(1);
            _med.MED_TIPO=fila.getString(2);
            _med.MEDXRED_DOSIFICACION = fila.getString(3);
            _med.RE_DOSIS = fila.getString(4);
            meds.add(_med);
        }

        db.close();
        if (contenedor.getChildCount() > 0)
            contenedor.removeAllViews();

        construirInterfaz(meds);
    }
}